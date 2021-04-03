package Database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class UpdateChannel implements Closeable {
    private Connection conn;

    public UpdateChannel(String host,String user,String dbpassword, String type) throws SQLException, ClassNotFoundException {
        Class.forName(type);
        this.conn = DriverManager.getConnection(host, user, dbpassword);
    }

    public void delete(int index) throws SQLException {
        Statement st = conn.createStatement();
        st.execute("delete from chanels where id='"+index+"'");
    }
    public String getId(String name) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet select = st.executeQuery("select * from chanels where name = " + "'"+name+"'");
        select.next();
        String id = select.getString(1);
        return id;
    }

    public ArrayList<ArrayList<String>> getData(String condition, String query, String sort) throws SQLException{
        Statement st = conn.createStatement();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ResultSet rs=null;
        if(condition.equals("")) {
            rs = st.executeQuery("select " + query + " from chanels order by " + sort);
        }else {
            rs = st.executeQuery("select " + query + " from chanels where " + condition +" order by "+ sort);
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
    public String getName(String id) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet select = st.executeQuery("select * from chanels where id = " + "'"+id+"'");
        select.next();
        String name = select.getString(2);
        return name;
    }
    public void add(String name) throws SQLException {
        Statement st = conn.createStatement();
        st.execute("insert into chanels (`name`) VALUE ('" + name +"')");
    }
    public TreeMap<String,Integer> getChannels() throws SQLException {
        TreeMap<String,Integer> list = new TreeMap<>();
        Statement st = conn.createStatement();
        ResultSet select = st.executeQuery("select * from chanels");
        while(select.next()){
            list.put(select.getString(2),select.getInt(1));
        }
        return list;
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
