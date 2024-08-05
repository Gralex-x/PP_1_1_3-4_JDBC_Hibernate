package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    // реализуйте настройку соеденения с БД

    private static Connection connection;

    private static final SessionFactory sessionFactory = new Configuration()
            .addProperties(hibernateProperties())
            .addAnnotatedClass(User.class)
            .buildSessionFactory();

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }

    private static Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC");
        properties.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("hibernate.connection.username", "root");
        properties.setProperty("hibernate.connection.password", "12345678");
        properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "none");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        return properties;
    }

    // task 1_1_3

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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
            connection = DriverManager.getConnection(
                    connectionURL,
                    userName,
                    password
            );
        } catch (Exception e) {
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
