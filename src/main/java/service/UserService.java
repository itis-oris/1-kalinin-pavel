package service;

import dao.UserDao;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import util.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class UserService {
    private UserDao userDao;

    public boolean isNonAnonymous(HttpServletRequest req, HttpServletResponse resp) {
        return req.getSession().getAttribute("user") != null;
    }

    public User getCurrentUser(HttpServletRequest req, HttpServletResponse res) {
        return (User) req.getSession().getAttribute("user");
    }

    public User getUserByUsernameAndPassword(String username, String password) throws DbException {
        return userDao.getUserByUsernameAndPassword(username, password);
    }

    public User addUser(String name, String login, String password) throws DbException {
        return userDao.addUser(name, login, password);
    }

    public User getUserByLogin(String login) throws DbException {
        return userDao.getUserByLogin(login);
    }

    public Long getUserId(String username) {
        return userDao.getUserId(username);
    }

    public User getUserById(int user_id) throws DbException {
        return userDao.getUserById(user_id);
    }
}
