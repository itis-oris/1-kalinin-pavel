package servlets.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/signout")
public class SignoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (session.getAttribute("user") != null) {
            session.invalidate();
        }

        Cookie userCookie = new Cookie("token", "");
        userCookie.setMaxAge(0);
        resp.addCookie(userCookie);

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
