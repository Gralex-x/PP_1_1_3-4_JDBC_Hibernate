package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            try {
                String sqlRequest = "CREATE TABLE IF NOT EXISTS User (" +
                        "    id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                        "    name VARCHAR(255)," +
                        "    lastName VARCHAR(255)," +
                        "    age TINYINT" +
                        ");";
                session.createNativeQuery(sqlRequest).executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) session.getTransaction().rollback();
                throw new RuntimeException("Не удалось создать таблицу пользователей", e);
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            try {
                String sqlRequest = "DROP TABLE IF EXISTS User";
                session.createNativeQuery(sqlRequest).executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) session.getTransaction().rollback();
                throw new RuntimeException("Не удалось удалить таблицу пользователей", e);
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            try {
                session.save(new User(name, lastName, age));
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) session.getTransaction().rollback();
                throw new RuntimeException("Не удалось добавить пользователя", e);
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            try {
                User user = session.get(User.class, id);
                if (user != null) {
                    session.delete(user);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) session.getTransaction().rollback();
                throw new RuntimeException("Не удалось удалить пользователя", e);
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            try {
                List<User> result = session.createQuery("FROM User", User.class).getResultList();
                session.getTransaction().commit();
                return result;
            } catch (Exception e) {
                if (session.getTransaction() != null) session.getTransaction().rollback();
                throw new RuntimeException("Не удалось получить список пользователей", e);
            }
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSession()) {
            session.beginTransaction();
            try {
                session.createNativeQuery("TRUNCATE TABLE User").executeUpdate();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null) session.getTransaction().rollback();
                throw new RuntimeException("Не удалось очистить таблицу пользователей", e);
            }
        }
    }
}
