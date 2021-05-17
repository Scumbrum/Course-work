package Database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class UpdateData implements Closeable {
    private Connection conn;
    private Statement statement;

    public UpdateData(String host, String user, String dbpassword, String type) throws SQLException, ClassNotFoundException {
        Class.forName(type);
        this.conn = DriverManager.getConnection(host, user, dbpassword);
        statement=conn.createStatement();
    }

    public void delete(String sql) throws SQLException {
        if(!sql.startsWith("delete")){
            throw new IllegalArgumentException("Can't execute");
        }
        statement.execute(sql);
    }

    public ArrayList<ArrayList<String>> select(String sql) throws SQLException{
        if(sql.startsWith("delete") | sql.startsWith("insert")){
            throw new IllegalArgumentException("Can't execute");
        }
            ArrayList<ArrayList<String>> list = new ArrayList<>();
            ResultSet data = statement.executeQuery(sql);
            int size = data.getMetaData().getColumnCount();
            while (data.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= size; i++) {
                    temp.add(data.getString(i));
                }
                list.add(temp);
            }
            return list;
    }

    public void add(String sql) throws SQLException {
        if(sql.startsWith("show") | sql.startsWith("select") | sql.startsWith("delete")){
            throw new IllegalArgumentException("Can't execute");
        }
        statement.execute(sql);
    }

    public ArrayList<String> getMeta(String table) throws SQLException {
        Statement st = conn.createStatement();
            ArrayList<String> list = new ArrayList<>();
            ResultSet rs = st.executeQuery("show columns from " + table);
            while (rs.next()) {
                list.add(rs.getString(1));
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
