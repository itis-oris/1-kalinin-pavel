package servlets.myQuotes;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.QuoteService;
import util.DbException;

import java.io.IOException;

@WebServlet("/delete_quote")
public class DeleteQuoteServlet extends HttpServlet {
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
        int quoteId = Integer.parseInt(req.getParameter("quoteId"));
        try {
            quoteService.deleteQuote(quoteId);
            resp.sendRedirect(req.getContextPath() + "/my_quotes");
        } catch (DbException e) {
            resp.sendRedirect(req.getContextPath() + "/myQuotes?state=fail");
        }
    }
}
