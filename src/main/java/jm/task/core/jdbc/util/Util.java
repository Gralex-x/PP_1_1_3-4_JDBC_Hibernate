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

    private static final String HOSTNAME = "localhost";

    private static final String PORT = "3306";

    private static final String DATABASE = "mysql";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "12345678";

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String CONNECTION_URL = "jdbc:mysql://" + HOSTNAME + ":" + PORT + "/" + DATABASE;

    private static final String MYSQL_DIALECT = "org.hibernate.dialect.MySQLDialect";

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
        properties.setProperty("hibernate.connection.url", CONNECTION_URL + "?useSSL=false&serverTimezone=UTC");
        properties.setProperty("hibernate.connection.driver_class", DRIVER);
        properties.setProperty("hibernate.connection.username", USERNAME);
        properties.setProperty("hibernate.connection.password", PASSWORD);
        properties.setProperty("hibernate.current_session_context_class", "thread");
        properties.setProperty("hibernate.dialect", MYSQL_DIALECT);
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

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(
                    CONNECTION_URL,
                    USERNAME,
                    PASSWORD
            );
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                connection = null;
            }
        } catch (SQLException ignored) {}
    }

}
