package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        Transaction transaction;
        String sqlRequest = "CREATE TABLE IF NOT EXISTS User (" +
                "    id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(255)," +
                "    lastName VARCHAR(255)," +
                "    age TINYINT" +
                ");";

        try (Session session = Util.getSession()) {

            transaction = session.beginTransaction();
            session.createNativeQuery(sqlRequest).executeUpdate();
            transaction.commit();

        } catch (HibernateException e) {
            throw new RuntimeException("Не удалось создать таблицу пользователей", e);
        }
    }

    @Override
    public void dropUsersTable() {
        Transaction transaction;
        String sqlRequest = "DROP TABLE IF EXISTS User";

        try (Session session = Util.getSession()) {

            transaction = session.beginTransaction();
            session.createNativeQuery(sqlRequest).executeUpdate();
            transaction.commit();

        } catch (HibernateException e) {
            throw new RuntimeException("Не удалось удалить таблицу пользователей", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = Util.getSession()) {

            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();

        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Не удалось добавить пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = Util.getSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.delete(user);
            }
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            throw new RuntimeException("Не удалось удалить пользователя", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            List<User> users = session.createQuery("FROM User", User.class).getResultList();
            transaction.commit();
            return users;
        } catch (HibernateException e) {
            throw new RuntimeException("Не удалось получить список пользователей", e);
        }
    }


    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("TRUNCATE TABLE User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            throw new RuntimeException("Не удалось очистить таблицу пользователей", e);
        }
    }
}
