package Database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class UpdateTransfer implements Closeable {
    private Connection conn;

    public UpdateTransfer(String host,String user,String dbpassword, String type) throws SQLException, ClassNotFoundException {
        Class.forName(type);
        this.conn = DriverManager.getConnection(host, user, dbpassword);
    }

    public void delete(int index) throws SQLException {
            Statement st = conn.createStatement();
            st.execute("delete from transfer where id='"+index+"'");
    }

    public ArrayList<String> getNewData() throws SQLException {
        Statement st = conn.createStatement();
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("show columns from transfer");
        while(rs.next()){
            list.add("");
        }
        return list;
    }
    public ArrayList<ArrayList<String>> getData(String condition, String query) throws SQLException{
        Statement st = conn.createStatement();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ResultSet rs=null;
        if(condition.equals("")) {
            rs = st.executeQuery("select " + query + " from transfer");
        }else {
           rs = st.executeQuery("select " + query + " from transfer where " + condition);
        }
        int size = rs.getMetaData().getColumnCount();
        while(rs.next()){
            ArrayList<String> temp = new ArrayList<>();
            for(int i=1;i<=size;i++){
                temp.add(rs.getString(i));
            }
            list.add(temp);
        }
        return list;
    }
    public ArrayList<String> getMeta() throws SQLException {
        Statement st = conn.createStatement();
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("show columns from transfer");
        while (rs.next()){
            list.add(rs.getString(1));
        }
        return list;
    }
    public void add(ArrayList<String> list) throws SQLException {
        StringBuffer query= new StringBuffer();
        for(String i:list){
            query.append("'");
            query.append(i);
            query.append("'");
            query.append(",");
        }
        query.deleteCharAt(query.length()-1);
        Statement st = conn.createStatement();
        st.execute("insert into transfer (`chanel_id`,`name`,`description`) VALUES ("+query+")");
    }
    public void alter(String name, String value, String index) throws SQLException {
        Statement st = conn.createStatement();
        st.execute("update transfer set " + name+ "="+ "'"+value+"' where id ="+"'" +index+"'");
    }
    @Override
    public void close() throws IOException {
        try {
            conn.close();
        } catch (SQLException throwables) {
            throw new IOException(throwables);
        }
    }
}
