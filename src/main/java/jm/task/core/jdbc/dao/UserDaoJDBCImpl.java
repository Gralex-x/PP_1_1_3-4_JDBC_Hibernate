package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection;

    public UserDaoJDBCImpl() {
        connection = Util.getConnection();
    }

    @Override
    public void createUsersTable() {

        String sqlRequest = "CREATE TABLE IF NOT EXISTS User (" +
                "    id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(255)," +
                "    lastName VARCHAR(255)," +
                "    age TINYINT" +
                ");";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropUsersTable() {

        String sqlRequest = "DROP TABLE IF EXISTS User";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sqlRequest = "INSERT INTO User (name, lastName, age) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        String sqlRequest = "DELETE FROM User WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.setLong(1, id);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            rollback();
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        String sqlRequest = "SELECT * FROM User";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest);
             ResultSet resultSet = statement.executeQuery()) {
            List<User> users = new ArrayList<>();

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                Byte age = resultSet.getByte("age");

                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
            connection.commit();
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void cleanUsersTable() {
        String sqlRequest = "TRUNCATE TABLE User";

        try (PreparedStatement statement = connection.prepareStatement(sqlRequest)) {
            statement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при очистке таблицы пользователей", e);
        }

    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при управлении транзакцией", e);
        }
    }
}
