package dao;

import entity.User;
import lombok.AllArgsConstructor;
import configs.ConnectionProvider;
import util.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class UserDao {
    private ConnectionProvider connectionProvider;

    public User getUserByUsernameAndPassword(String username, String password) throws DbException {
        String sql = "SELECT id, name, login, password FROM account WHERE login = ? AND password = MD5(?)";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)){
            st.setString(1, username);
            st.setString(2, password);
            ResultSet result = st.executeQuery();
            boolean hasOne = result.next();
            if(hasOne) {
                System.out.println(result.getString("login"));
                User user = new User(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("login"),
                        result.getString("password")
                );
                connectionProvider.releaseConnection(connection);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Can't get user from db.", e);
        }
    }

    public User addUser(String name, String login, String password) throws DbException {
        String sql = "INSERT INTO account (name, login, password) VALUES (?, ?, ?)";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)){
            st.setString(1, name);
            st.setString(2, login);
            st.setString(3, password);
            int result = st.executeUpdate();
            connectionProvider.releaseConnection(connection);
            if(result > 0) {
                return getUserByLogin(login);
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new DbException("Can't get user from db.", e);
        }
    }

    public User getUserByLogin(String login) throws DbException {
        String sql = "SELECT id, name, login, password FROM account WHERE login = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setString(1, login);
            ResultSet result = st.executeQuery();
            boolean hasOne = result.next();
            if(hasOne) {
                User user = new User(result.getInt("id"), result.getString("name"), result.getString("login"), result.getString("password"));
                connectionProvider.releaseConnection(connection);
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Can't get user from db.", e);
        }
    }

    public Long getUserId(String username) {
        String query = "SELECT id FROM account WHERE username = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    connectionProvider.releaseConnection(connection);
                    return id;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int user_id) throws DbException {
        String sql = "SELECT * FROM account WHERE id = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, user_id);
            ResultSet result = st.executeQuery();
            boolean hasOne = result.next();
            if (hasOne) {
                User user = new User(user_id, result.getString("name"), result.getString("login"), result.getString("password"));
                connectionProvider.releaseConnection(connection);
                return user;
            }
        } catch (SQLException e) {
            throw new DbException("Can't get user from db.", e);
        }
        return null;
    }
}
