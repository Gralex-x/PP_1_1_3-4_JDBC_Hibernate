package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Util {
    // реализуйте настройку соеденения с БД

    private static Connection connection;

    public static Connection getConnection() {

        if (connection != null) {
            return connection;
        }

        String hostName = "localhost";

        String dbName = "mysql";
        String userName = "root";
        String password = "12345678";

        return getConnection(hostName, dbName, userName, password);
    }

    private static Connection getConnection(String hostName, String dbName,
                                                 String userName, String password) {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
            connection = DriverManager.getConnection(
                    connectionURL,
                    userName,
                    password
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
