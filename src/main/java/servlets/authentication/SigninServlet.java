package servlets.authentication;

import dao.UserDao;
import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CryptService;
import service.UserService;
import util.DbException;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@WebServlet("/signin")
public class SigninServlet extends HttpServlet {
    private UserService userService;
    private CryptService cryptService;
    private Map<String, User> authData;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) this.getServletContext().getAttribute("userService");
        cryptService = (CryptService) this.getServletContext().getAttribute("cryptService");
        authData = (Map<String, User>) getServletContext().getAttribute("authData");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/signin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/signin?error=empty");
            return;
        }

        try {
            User user = userService.getUserByLogin(username);
            if (cryptService.checkPassword(password, user.getPassword())) {
                req.getSession().setAttribute("user", user);

                if ("on".equals(rememberMe)) {
                    String rememberToken = UUID.randomUUID().toString();
                    authData.put(rememberToken, user);
                    Cookie rememberCookie = new Cookie("token", rememberToken);
                    rememberCookie.setMaxAge(7 * 24 * 60 * 60);
                    resp.addCookie(rememberCookie);
                }

                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                resp.sendRedirect(req.getContextPath() + "/signin?error=invalid");
            }
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
    }
}
