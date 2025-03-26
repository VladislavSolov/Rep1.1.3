package jm.task.core.jdbc.dao;

import com.mysql.cj.xdevapi.Table;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl extends Util implements UserDao {

    private Connection connection;

    public UserDaoJDBCImpl() throws SQLException {
        this.connection = Util.getConnection();
    }

    public void createUsersTable() throws SQLException {

        Statement ps = null;

        try {
            ps = connection.createStatement();
            ps.executeUpdate("CREATE TABLE IF NOT EXISTS users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "lastName VARCHAR(255) NOT NULL, " +
                    "age TINYINT NOT NULL)");
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void dropUsersTable() throws SQLException {
        PreparedStatement ps = null;
        String sql = "DROP TABLE IF EXISTS users";
        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        PreparedStatement ps = null;
        String sql = "INSERT INTO users (name, lastname, age) VALUES(?, ?, ?)";

        try {
            ps = connection.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
            System.out.println("User " + name + " " + lastName + " успешно добавлен ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void removeUserById(long id) throws SQLException {
        PreparedStatement ps = null;
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            ps = connection.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<User>();
        String sql = "SELECT * FROM users";
        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));

                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }


    }

    public void cleanUsersTable() throws SQLException {
        PreparedStatement ps = null;
        String sql = "DELETE FROM users";
        try {
            ps = connection.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }

    }
}
