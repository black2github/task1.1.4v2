package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDaoJDBCImpl implements UserDao {
    private Connection connection = null;

    public UserDaoJDBCImpl() {
        try {
            connection = Util.getConnection();
            connection.setAutoCommit(false); // отключение автокомита
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS Users ("
                + "userId INT NOT NULL AUTO_INCREMENT,"
                + "name VARCHAR(64) NULL,"
                + "lastName VARCHAR(64) NULL,"
                + "age INT NULL,"
                + "PRIMARY KEY (`userId`))";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS Users";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT into Users (name, lastName, age) "
                + " values (?,?,?)";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            // Set values for parameters
            pstm.setString(1, name);
            pstm.setString(2, lastName);
            pstm.setInt(3, age);

            pstm.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE from Users WHERE userId = ?";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, id);
            pstm.executeUpdate();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT userId, name, lastName, age from Users";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {

            // Execute SQL statement returns a ResultSet object.
            ResultSet rs = pstm.executeQuery();

            // Fetch on the ResultSet
            // Move the cursor to the next record.
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                String lastName = rs.getString(3);
                byte age = (byte) rs.getInt(4);

                User user = new User(name, lastName, age);
                user.setId(id);
                list.add(user);
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE Users";

        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.execute();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
