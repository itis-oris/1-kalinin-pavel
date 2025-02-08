package service;

import dao.QuizDao;

import entity.QuizQuote;
import entity.Quote;
import lombok.AllArgsConstructor;
import util.DbException;

@AllArgsConstructor
public class QuizService {
    private QuizDao quizDao;

    public QuizQuote getRandomQuizQuote() throws DbException {
        return quizDao.getRandomQuiz();
    }
}
