package servlets;

import entity.QuizQuote;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.QuizService;
import service.QuoteService;
import util.DbException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

@WebServlet("/quiz")
public class QuizServlet extends HttpServlet {
    private QuizService quizService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        quizService = (QuizService) getServletContext().getAttribute("quizService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            QuizQuote quizQuote = quizService.getRandomQuizQuote();
            Collections.shuffle(quizQuote.getOptions());
            req.setAttribute("quizQuote", quizQuote);
        } catch (DbException e) {
            req.setAttribute("errorQuiz", "Ошибка базы данных: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/quiz.jsp").forward(req, resp);
    }
}
