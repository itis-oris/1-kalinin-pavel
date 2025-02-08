package servlets;

import dao.QuoteDao;
import entity.Quote;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.QuoteService;
import service.UserService;
import util.DbException;

import java.io.IOException;
import java.util.List;

@WebServlet("/liked")
public class LikedServlet extends HttpServlet {
    private QuoteService quoteService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        quoteService = (QuoteService) getServletContext().getAttribute("quoteService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = userService.getCurrentUser(req, resp);
            List<Quote> likedQuotes = quoteService.getLikedQuotes(user.getId());
            req.setAttribute("likedQuotes", likedQuotes);
        } catch (DbException e) {
            req.setAttribute("error", "Could not retrieve favourite quotes.");
        }

        req.getRequestDispatcher("WEB-INF/views/favourites.jsp").forward(req, resp);
    }
}
