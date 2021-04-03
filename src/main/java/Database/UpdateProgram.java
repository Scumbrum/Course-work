package Database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class UpdateProgram implements Closeable {
    Connection conn;
    public UpdateProgram(String host,String user,String dbpassword, String type) throws SQLException, ClassNotFoundException {
        Class.forName(type);
        this.conn = DriverManager.getConnection(host, user, dbpassword);
    }
    public ArrayList<ArrayList<String>> getData(String condition, String query,String sort) throws SQLException {
        Statement st = conn.createStatement();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ResultSet rs = null;
        if (condition.equals("")) {
            rs = st.executeQuery("select " + query + " from program order by " + sort);
        } else {
            rs = st.executeQuery("select " + query + " from program where " + condition + " order by "+ sort);
        }
        int size = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                temp.add(rs.getString(i));
            }
            list.add(temp);
        }
        return list;
    }

    public ArrayList<ArrayList<String>> getDate(String condition, String query) throws SQLException {
        Statement st = conn.createStatement();
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        ResultSet rs = null;
        if (condition.equals("")) {
            rs = st.executeQuery("select " + query + " from program");
        } else {
            rs = st.executeQuery("select " + query + " from program where " + condition);
        }
        int size = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            ArrayList<String> temp = new ArrayList<>();
            for (int i = 1; i <= size; i++) {
                temp.add(rs.getString(i));
            }
            list.add(temp);
        }
        return list;
    }
    public ArrayList<String> getMeta() throws SQLException {
        Statement st = conn.createStatement();
        ArrayList<String> list = new ArrayList<>();
        ResultSet rs = st.executeQuery("show columns from program");
        while (rs.next()){
            list.add(rs.getString(1));
        }
        return list;
    }
    public void add(String time,String day) throws SQLException {
        Statement st = conn.createStatement();
        st.execute("insert into program (`time`,`day`) VALUES ('"+time+"','"+day+"')");
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
