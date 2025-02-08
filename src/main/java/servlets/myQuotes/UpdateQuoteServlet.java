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
import util.DbException;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/updateQuote")
public class UpdateQuoteServlet extends HttpServlet {
    private CategoryService categoryService;
    private QuoteService quoteService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        categoryService = (CategoryService) getServletContext().getAttribute("categoryService");
        quoteService = (QuoteService) getServletContext().getAttribute("quoteService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        try {
            ArrayList<Category> categories = categoryService.getAllCategory();
            req.setAttribute("categories", categories);
            int quoteId = Integer.parseInt(req.getParameter("id"));
            Quote quote = quoteService.getQuoteById(quoteId, user.getId());
            req.setAttribute("updatingQuote", quote);
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        req.getRequestDispatcher("/WEB-INF/views/updateQuote.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        String text = req.getParameter("quoteText");
        String author = req.getParameter("quoteAuthor");
        int category = Integer.parseInt(req.getParameter("quoteCategory"));
        int id = Integer.parseInt(req.getParameter("quoteId"));
        System.out.println(user.getId() + " " + text + " " + author + " " + category + " " + id);
        try {
            quoteService.updateQuote(user.getId(), id, text, author, category);
        } catch (DbException e) {
            throw new RuntimeException(e);
        }
        resp.sendRedirect(req.getContextPath() + "/myQuotes");
    }
}
