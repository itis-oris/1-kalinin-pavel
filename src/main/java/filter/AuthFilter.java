package filter;

import dao.UserDao;
import entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;
import util.DbException;

import java.io.IOException;
import java.util.Map;

@WebFilter("/*")
public class AuthFilter extends HttpFilter {
    private static final String[] securedPaths = new String[]{"/liked", "/add_new_quote", "/myQuotes", "/quiz", "delete_quote", "/updateQuote"};
    private UserService userService;

    @Override
    public void init(FilterConfig config) throws ServletException {
        super.init(config);
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        boolean prot = false;
        for (String path : securedPaths) {
            if (path.equals(req.getRequestURI().substring(req.getContextPath().length()))) {
                prot = true;
                break;
            }
        }
        if (prot && !userService.isNonAnonymous(req, res)) {
            res.sendRedirect(req.getContextPath() + "/signin");
        } else {
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        String token = cookie.getValue();
                        Map<String, User> authData = (Map<String, User>) getServletContext().getAttribute("authData");

                        User user = authData.get(token);
                        if (user != null) {
                            req.getSession().setAttribute("user", user);
                        }
                        chain.doFilter(req, res); // Пропускаем запрос
                        return;
                    }
                }
            }
            chain.doFilter(req, res);
        }
    }
}
