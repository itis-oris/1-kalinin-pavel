package servlets.authentication;

import dao.UserDao;
import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CryptService;
import service.UserService;
import util.DbException;

import java.io.IOException;

@WebServlet("/signup")
public class SignupServlet extends HttpServlet {
    private UserService userService;
    private CryptService cryptService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) this.getServletContext().getAttribute("userService");
        cryptService = (CryptService) this.getServletContext().getAttribute("cryptService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        boolean hasErrors = false;
        String name = req.getParameter("name");
        String login = req.getParameter("login");
        String password = cryptService.encryptPassword(req.getParameter("password"));

        if (name == null || name.isEmpty()) {
            req.setAttribute("errorName", "Имя не может быть пустым.");
            hasErrors = true;
        }

        if (login == null || login.isEmpty()) {
            req.setAttribute("errorEmail", "Email не может быть пустым.");
            hasErrors = true;
        } else {
            try {
                if (userService.getUserByLogin(login) != null) {
                    req.setAttribute("errorEmail", "Пользователь с таким email уже существует.");
                    hasErrors = true;
                }
            } catch (DbException e) {
                throw new RuntimeException(e);
            }
        }

        if (password == null || password.isEmpty()) {
            req.setAttribute("errorPassword", "Пароль не может быть пустым.");
            hasErrors = true;
        }

        if (hasErrors) {
            req.getRequestDispatcher("/WEB-INF/views/signup.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userService.addUser(name, login, password);
            if (user != null) {
                req.getSession().setAttribute("user", user);
                resp.sendRedirect(req.getContextPath() + "/");
            }
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
    }
}
