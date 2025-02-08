package servlets.myQuotes;

import entity.Category;
import entity.Quote;
import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.CategoryService;
import service.QuoteService;
import service.UserService;
import util.DbException;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/myQuotes")
public class MyQuoteServlet extends HttpServlet {
    private UserService userService;
    private QuoteService quoteService;
    private CategoryService categoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        quoteService = (QuoteService) getServletContext().getAttribute("quoteService");
        categoryService = (CategoryService) getServletContext().getAttribute("categoryService");
        userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ArrayList<Category> categories = categoryService.getAllCategory();
            ArrayList<Quote> myQuotes = quoteService.getMyQuotes(userService.getCurrentUser(req, resp).getId());
            req.setAttribute("myQuotes", myQuotes);
            req.setAttribute("categories", categories);
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        req.getRequestDispatcher("/WEB-INF/views/my_quotes.jsp").forward(req, resp);
    }
}
