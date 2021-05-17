package Client;

import Additional.LoginClient;
import Database.UpdateData;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "ClientView", value = "/ClientView")
public class ClientView extends LoginClient {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        String id = request.getParameter("id");
        String login = (String) session.getAttribute("login");
        ArrayList<String> params = getparam();
        if(!chekCustomer(request,response)){
            response.sendRedirect("index.jsp");
            return;
        }
        if(action!=null && action.equals("Quit")){
            session.invalidate();
            response.sendRedirect("Signin");
            return;
        }
        String sort = request.getParameter("sortby");
        try (
                UpdateData uc = new UpdateData(params.get(0), params.get(1), params.get(2), params.get(3));) {
            LOCK.readLock().lock();
            try {
                if (id == null) {
                    ArrayList<ArrayList<String>> idlist = uc.select("select curr_id from view_id where customer_login = '" + login + "'");
                    if (idlist.size() != 0) {
                        id = idlist.get(0).get(0);
                        action = "View";
                    }
                }
            } finally {
                LOCK.readLock().unlock();
            }
            if (action != null) {
                switch (action) {
                    case ("View"):
                        request.setAttribute("sortby", sort);
                        LOCK.readLock().lock();
                        try {

                            ArrayList<String> paramlist = showData(uc, id);
                            if(paramlist!=null) {
                                ArrayList<String> meta = showMeta(uc, id);
                                request.setAttribute("data", paramlist);
                                request.setAttribute("meta", meta);
                            }
                        }catch (ArrayStoreException e){
                            request.setAttribute("exception",e.getMessage());
                        }
                        finally {
                            LOCK.readLock().unlock();
                        }
                        LOCK.writeLock().lock();
                        try {
                            addId(uc, login, id);
                        } finally {
                            LOCK.writeLock().unlock();
                        }
                        request.setAttribute("action", action);
                        break;
                    case ("Quit"):
                        session.invalidate();
                        response.sendRedirect("Signin");
                        return;
                }
            }
            LinkedHashMap<String, ArrayList<String>> showlist = new LinkedHashMap<>();
            if (sort != null) {
                request.setAttribute("sortby", sort);
                if (sort.equals("date")) {showlist = sort(uc,"select id from program order by day, time");}
                if (sort.equals("channel")) {
                    showlist = sort(uc,"select id from chanels order by name");
                }
            } else {
                showlist=defaultSort(uc);

            }
            request.setAttribute("showlist", showlist);
        } catch (SQLException | ClassNotFoundException throwables) {
            response.sendError(500);
            return;
        }
        RequestDispatcher rd = request.getRequestDispatcher("clientpage.jsp");
        rd.forward(request,response);

    }

    private LinkedHashMap<String,ArrayList<String>> defaultSort(UpdateData uc) throws SQLException {
        LOCK.readLock().lock();
        try {
            String sql = "select id,name,program_id from transfer";
            LinkedHashMap<String, ArrayList<String>> out = new LinkedHashMap<>();
            ArrayList<ArrayList<String>> list = uc.select(sql);
            for (int i = 0; i < list.size(); i++) {
                sql = "select time, day from program where id ='" + list.get(i).get(2) + "'";
                ArrayList<String> temp = uc.select(sql).get(0);
                temp.add(list.get(i).get(1));
                out.put(list.get(i).get(0), temp);
            }
            return out;
        }finally {
            LOCK.readLock().unlock();
        }
    }
    private ArrayList<String> showData(UpdateData uc, String id) throws SQLException {
        ArrayList<String> paramlist = new ArrayList<>();
        ArrayList<ArrayList<String>> namelist = uc.select("select chanel_id, name, description, program_id from transfer where id ='" + id + "'");
        if(namelist.size()==0){
            return null;
        }
        String name = uc.select("select name from chanels where id = '" +namelist.get(0).get(0) + "'").get(0).get(0);
        paramlist.add(name);
        paramlist.add(namelist.get(0).get(1));
        paramlist.add(namelist.get(0).get(2));
        ArrayList<ArrayList<String>> date = uc.select("select time, day from program where id ='" +namelist.get(0).get(3) + "'");
        paramlist.add(date.get(0).get(0));
        paramlist.add(date.get(0).get(1));
        return separate(paramlist);
    }
    private ArrayList<String> separate(ArrayList<String> a){
        ArrayList<String> newList = new ArrayList<>(a.size());
        for(String i: a){
            if(i==null){
                throw new ArrayStoreException("Немає інформації про дану телепередачу");
            }
            StringBuffer sb = new StringBuffer();
            int curr=0;
            for(curr =25;curr<i.length();curr+=25){
                sb.append(i.substring(curr-25,curr));
                sb.append("<br>");
            }
            sb.append(i.substring(curr-25,i.length()));
            newList.add(sb.toString());
        }
        return newList;
    }

    private ArrayList<String> showMeta(UpdateData uc, String id) throws SQLException {
        ArrayList<String> meta= new ArrayList<>();
        ArrayList<String> temp = uc.getMeta("transfer");
        temp.remove(0);
        temp.remove(3);
        meta.addAll(temp);
        temp = uc.getMeta("program");
        temp.remove(0);
        meta.addAll(temp);
        return meta;
    }
    private LinkedHashMap<String, ArrayList<String>> sort(UpdateData uc, String sort) throws SQLException {
        LOCK.readLock().lock();
        try {
            LinkedHashMap<String, ArrayList<String>> showlist = new LinkedHashMap<>();
            ArrayList<ArrayList<String>> date = uc.select(sort);
            for (ArrayList<String> i : date) {
                ArrayList<ArrayList<String>> temp = uc.select("select id,name,program_id from transfer where program_id = '" + i.get(0) + "'");
                    for (int row = 0; row < temp.size(); row++) {
                        ArrayList<ArrayList<String>> currDate = uc.select("select time, day from program where id ='" + temp.get(row).get(2) + "'");
                        currDate.get(0).add(temp.get(row).get(1));
                                showlist.put(temp.get(row).get(0), currDate.get(0));
                    }
            }
            return showlist;
        } finally {
            LOCK.readLock().unlock();
        }
    }

    private void addId(UpdateData uc, String login, String id) throws SQLException {
        ArrayList<ArrayList<String>> list = uc.select("select * from view_id where `customer_login` = '" + login + "'");
        if(list.size()==0){
            uc.add("insert into view_id (`customer_login`,`curr_id`) values ('"+ login+"','" +id + "')");
        }else {
            uc.add("update view_id set curr_id ='" +id  + "' where customer_login ='" + login + "'");
        }
    }
}
