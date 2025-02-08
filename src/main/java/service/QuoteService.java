package service;

import dao.QuoteDao;
import entity.Quote;
import lombok.AllArgsConstructor;
import util.DbException;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class QuoteService {
    private QuoteDao quoteDao;

    public Quote getRandom() throws DbException {
        return quoteDao.getRandom();
    }

    public ArrayList<Quote> getAll(Integer userId) throws DbException {
        return quoteDao.getAll(userId);
    }

    public boolean addQuote(int userId, String text, String author, Integer categoryId) throws DbException {
        return quoteDao.addQuote(userId, text, author, categoryId);
    }

    public ArrayList<Quote> getLikedQuotes(Integer userId) throws DbException {
        return quoteDao.getLikedQuotes(userId);
    }

    public void addLike(int quoteId, int userId) throws DbException {
        quoteDao.addLike(quoteId, userId);
    }

    public boolean isLikedByUser(int quoteId, int userId) throws DbException {
        return quoteDao.isLikedByUser(quoteId, userId);
    }

    public void removeLike(int quoteId, int userId) throws DbException {
        quoteDao.removeLike(quoteId, userId);
    }

    public int getQuoteLikesCount(int quoteId) throws DbException {
        return quoteDao.getQuoteLikesCount(quoteId);
    }

    public void deleteQuote(int quoteId) throws DbException {
        quoteDao.deleteQuote(quoteId);
    }

    public ArrayList<Quote> getMyQuotes(int userId) throws DbException {
        return quoteDao.getMyQuotes(userId);
    }

    public Quote getQuoteById(int quoteId, int userId) throws DbException {
        return quoteDao.getQuoteById(quoteId, userId);
    }

    public Quote updateQuote(int userId, int quoteId, String text, String author, int categoryId) throws DbException {
        return quoteDao.updateQuote(userId, quoteId, text, author, categoryId);
    }
}
