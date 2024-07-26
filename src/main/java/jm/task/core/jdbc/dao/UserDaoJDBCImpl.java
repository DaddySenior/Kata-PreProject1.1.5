package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.Main;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS users(" +
                    "id INT PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(45) NOT NULL, " +
                    "lastname VARCHAR(45) NOT NULL, " +
                    "age INTEGER NOT NULL);");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS users");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        Connection connection = Util.getConnection();
        try (PreparedStatement ps = connection.
                prepareStatement("INSERT INTO users VALUES(id, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        Connection connection = Util.getConnection();
        try (PreparedStatement ps = connection
                .prepareStatement("DELETE FROM users WHERE ID = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = Util.getConnection();
        try (PreparedStatement ps = connection
                .prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public void cleanUsersTable() {
        Connection connection = Util.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
