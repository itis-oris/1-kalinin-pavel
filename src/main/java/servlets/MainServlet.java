package servlets;

import com.google.gson.Gson;
import dao.CategoryDao;
import dao.QuoteDao;
import entity.Category;
import entity.Quote;
import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import service.CategoryService;
import service.QuoteService;
import service.UserService;
import util.DbException;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private QuoteService quoteService;
    private UserService userService;
    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        quoteService = (QuoteService) getServletContext().getAttribute("quoteService");
        userService = (UserService) getServletContext().getAttribute("userService");
        categoryService = (CategoryService) getServletContext().getAttribute("categoryService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDate today = LocalDate.now();
        LocalDate lastQuoteDate = (LocalDate) getServletContext().getAttribute("lastQuoteDate");
        User user = userService.getCurrentUser(req, resp);
        Integer userId = user == null ? null : user.getId();

        // Если дата последнего обновления не совпадает с сегодняшней
        if (lastQuoteDate == null || !lastQuoteDate.equals(today)) {
            // Обновляем цитату и сохраняем в контексте сервлета
            try {
                Quote randomQuote = quoteService.getRandom();
                getServletContext().setAttribute("randomQuote", randomQuote);
                getServletContext().setAttribute("lastQuoteDate", today);
            } catch (DbException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            ArrayList<Category> categories = categoryService.getAllCategory();
            req.setAttribute("categories", categories);
            req.setAttribute("allQuotes", quoteService.getAll(userId));
        } catch (DbException e) {
            throw new RuntimeException(e);
        }

        getServletContext().getRequestDispatcher("/WEB-INF/views/main.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession(false);
        User user = session == null ? null : userService.getCurrentUser(req, resp);
        ArrayList<Quote> quotes = (ArrayList<Quote>) req.getAttribute("allQuotes");

        if ("like".equals(action)) {
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/signin");
                return;
            }
            int quoteId = Integer.parseInt(req.getParameter("quoteId"));
            try {
                boolean isLiked = quoteService.isLikedByUser(quoteId, user.getId());

                if (isLiked) {
                    quoteService.removeLike(quoteId, user.getId());
                } else {
                    quoteService.addLike(quoteId, user.getId());
                }
                int likesCount = quoteService.getQuoteLikesCount(quoteId);

                // Отправляем обновленные данные
                resp.setContentType("application/json");
                resp.getWriter().write("{\"liked\": " + !isLiked + ", \"likes\": " + likesCount + "}");
            } catch (DbException e) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating like status");
            }
        } else if ("filterAndSort".equals(action)) {

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }
}
