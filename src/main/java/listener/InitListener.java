package listener;

import dao.CategoryDao;
import dao.QuizDao;
import dao.QuoteDao;
import dao.UserDao;
import entity.User;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.*;
import configs.ConnectionProvider;

import java.util.HashMap;
import java.util.Map;

public class InitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Map<String, User> authData = new HashMap<>();

        ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
        QuoteDao quoteDao = new QuoteDao(connectionProvider);
        UserDao userDao = new UserDao(connectionProvider);
        CategoryDao categoryDao = new CategoryDao(connectionProvider);
        QuizDao quizDao = new QuizDao(connectionProvider);

        sce.getServletContext().setAttribute("authData", authData);
        sce.getServletContext().setAttribute("cryptService", new CryptService());
        sce.getServletContext().setAttribute("quoteService", new QuoteService(quoteDao));
        sce.getServletContext().setAttribute("categoryService", new CategoryService(categoryDao));
        sce.getServletContext().setAttribute("userService", new UserService(userDao));
        sce.getServletContext().setAttribute("quizService", new QuizService(quizDao));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionProvider.getInstance().destroy();
    }
}
