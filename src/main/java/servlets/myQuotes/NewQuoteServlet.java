package servlets.myQuotes;

import entity.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.QuoteService;
import util.DbException;

import java.io.IOException;

@WebServlet("/add_new_quote")
public class NewQuoteServlet extends HttpServlet {
    private QuoteService quoteService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        quoteService = (QuoteService) getServletContext().getAttribute("quoteService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/my_quotes");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String state;
        String text = req.getParameter("quoteText");
        String author = req.getParameter("quoteAuthor");
        String category = req.getParameter("quoteCategory");

        try {
            if (text == null || text.equals("") || author == null || author.equals("") || category == null || category.equals("")) {
                state = "error";
            } else {
                quoteService.addQuote(((User) req.getSession().getAttribute("user")).getId(), text, author, Integer.parseInt(category));
                state = "success";
            }
        } catch (DbException e) {
            state = "error";
        }

        resp.sendRedirect(req.getContextPath() + "/myQuotes?state=" + state);
    }
}
