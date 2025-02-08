package dao;

import entity.Quote;
import lombok.AllArgsConstructor;
import configs.ConnectionProvider;
import util.DbException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class QuoteDao {
    private ConnectionProvider connectionProvider;

    public Quote getRandom() throws DbException {
        try (Connection connection = connectionProvider.getCon();
            Statement st = connection.createStatement()){
            ResultSet result = st.executeQuery("SELECT quote.id, text, author.name as author, category.name as category, quote.date_added " +
                    "FROM quote " +
                    "JOIN author ON quote.author_id = author.id " +
                    "join category on quote.category_id = category.id " +
                    "ORDER BY RANDOM() LIMIT 1 ");
            if (result.next()) {
                Quote quote =  new Quote(Integer.parseInt(result.getString("id")), result.getString("text"), result.getString("author"), 0, result.getDate("date_added"), result.getString("category"), false);
                connectionProvider.releaseConnection(connection);
                return quote;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException("Can't get random quote in DB.", e);
        }
    }

    public ArrayList<Quote> getAll(Integer userId) throws DbException {
        ArrayList<Quote> quotes = new ArrayList<>();
        String sql = "SELECT q.id, q.text, author.name as author, category.name as category, q.date_added, (select count(id) from liked_quote where quote_id = q.id) as rating, " +
                "       CASE WHEN ? IS NULL THEN FALSE " +
                "            ELSE EXISTS(SELECT 1 FROM liked_quote WHERE quote_id = q.id AND account_id = ?) " +
                "       END AS liked " +
                "FROM quote q " +
                "JOIN author ON q.author_id = author.id " +
                "JOIN category ON q.category_id = category.id " +
                "ORDER BY date_added ";

        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)){
            if (userId != null) {
                st.setInt(1, userId);
                st.setInt(2, userId);
            } else {
                st.setNull(1, Types.INTEGER);
                st.setNull(2, Types.INTEGER);
            }
            ResultSet result = st.executeQuery();
            while (result.next()) {
                Quote quote = new Quote(
                        result.getInt("id"),
                        result.getString("text"),
                        result.getString("author"),
                        result.getInt("rating"),
                        result.getDate("date_added"),
                        result.getString("category"),
                        result.getBoolean("liked")
                );
                quotes.add(quote);
            }
            connectionProvider.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DbException("Can't get all quotes in DB.", e);
        }
        return quotes;
    }

    public boolean addQuote(int userId, String text, String author, Integer categoryId) throws DbException {
        String insertAuthorSql = "INSERT INTO author (name) VALUES (?) ON CONFLICT (name) DO NOTHING";
        String selectAuthorIdSql = "SELECT id FROM author WHERE name = ?";
        String insertQuoteSql = "INSERT INTO quote (text, author_id, category_id, date_added, adder_id) VALUES (?, ?, ?, CURRENT_DATE, ?)";

        try (Connection connection = connectionProvider.getCon()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertAuthorSt = connection.prepareStatement(insertAuthorSql);
                 PreparedStatement selectAuthorIdSt = connection.prepareStatement(selectAuthorIdSql);
                 PreparedStatement insertQuoteSt = connection.prepareStatement(insertQuoteSql)) {

                // Insert the author if it doesn't exist
                insertAuthorSt.setString(1, author);
                insertAuthorSt.executeUpdate();

                // Retrieve the author ID
                selectAuthorIdSt.setString(1, author);
                ResultSet authorRs = selectAuthorIdSt.executeQuery();

                if (!authorRs.next()) {
                    throw new DbException("Failed to retrieve author ID for author: " + author);
                }
                int authorId = authorRs.getInt("id");

                // Insert the quote
                insertQuoteSt.setString(1, text);
                insertQuoteSt.setInt(2, authorId);
                insertQuoteSt.setInt(3, categoryId);
                insertQuoteSt.setInt(4, userId);
                int rowsInserted = insertQuoteSt.executeUpdate();

                // Commit the transaction
                connection.commit();
                connectionProvider.releaseConnection(connection);
                return rowsInserted > 0;

            } catch (SQLException e) {
                connection.rollback();
                connectionProvider.releaseConnection(connection);
                throw new DbException("Error occurred while adding quote, transaction rolled back.", e);
            }

        } catch (SQLException e) {
            throw new DbException("Can't add quote to DB.", e);
        }
    }

    public ArrayList<Quote> getLikedQuotes(Integer userId) throws DbException {
        ArrayList<Quote> favoriteQuotes = new ArrayList<>();
        String sql = "SELECT q.id, q.text, author.name as author, (select count(id) from liked_quote where id = q.id) as rating, category.name as category, q.date_added " +
                "FROM liked_quote lq " +
                "JOIN account a ON lq.account_id = a.id " +
                "JOIN quote q ON lq.quote_id = q.id " +
                "JOIN author on q.author_id = author.id " +
                "join category on q.category_id = category.id " +
                "where a.id = ?";

        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Quote quote = new Quote(rs.getInt("id"), rs.getString("text"), rs.getString("author"), rs.getInt("rating"), rs.getDate("date_added"), rs.getString("category"), true);
                favoriteQuotes.add(quote);
            }
            connectionProvider.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DbException("Error fetching favourite quotes", e);
        }
        return favoriteQuotes;
    }

    public void addLike(int quoteId, int userId) throws DbException {
        String sql = "INSERT INTO liked_quote (quote_id, account_id) VALUES (?, ?)";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, quoteId);
            st.setInt(2, userId);
            st.executeUpdate();

            st.close();
            connectionProvider.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DbException("Error adding like", e);
        }
    }

    public boolean isLikedByUser(int quoteId, int userId) throws DbException {
        String sql = "SELECT 1 FROM liked_quote WHERE quote_id = ? AND account_id = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, quoteId);
            st.setInt(2, userId);
            ResultSet rs = st.executeQuery();
            boolean hasNext = rs.next();
            connectionProvider.releaseConnection(connection);
            return hasNext;
        } catch (SQLException e) {
            throw new DbException("Error checking like status", e);
        }
    }

    public void removeLike(int quoteId, int userId) throws DbException {
        String sql = "DELETE FROM liked_quote WHERE quote_id = ? AND account_id = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {

            st.setInt(1, quoteId);
            st.setInt(2, userId);
            st.executeUpdate();
            connectionProvider.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public int getQuoteLikesCount(int quoteId) throws DbException {
        String sql = "SELECT count(id) FROM liked_quote WHERE quote_id = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, quoteId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                int cnt = rs.getInt(1);
                connectionProvider.releaseConnection(connection);
                return cnt;
            }
        }catch (SQLException e) {
            throw new DbException("Error getting quote rating", e);
        }
        return 0;
    }

    public void deleteQuote(int quoteId) throws DbException {
        String sql = "DELETE FROM quote WHERE id = ?";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, quoteId);
            ResultSet rs = st.executeQuery();
            connectionProvider.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DbException("Error deleting quote", e);
        }
    }

    public ArrayList<Quote> getMyQuotes(int userId) throws DbException {
        String sql = "SELECT q.id, q.text, author.name as author, category.name as category, q.date_added, (select count(id) from liked_quote where quote_id = q.id) as rating, " +
                "       CASE WHEN ? IS NULL THEN FALSE " +
                "            ELSE EXISTS(SELECT 1 FROM liked_quote WHERE quote_id = q.id AND account_id = ?) " +
                "       END AS liked " +
                "FROM quote q " +
                "JOIN author ON q.author_id = author.id " +
                "JOIN category ON q.category_id = category.id " +
                "where q.adder_id = ? " +
                "ORDER BY date_added ";
        ArrayList<Quote> quotes = new ArrayList<>();
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)){
            st.setInt(1, userId);
            st.setInt(2, userId);
            st.setInt(3, userId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Quote quote = new Quote(
                        rs.getInt("id"),
                        rs.getString("text"),
                        rs.getString("author"),
                        rs.getInt("rating"),
                        rs.getDate("date_added"),
                        rs.getString("category"),
                        rs.getBoolean("liked")
                );
                quotes.add(quote);
            }
            connectionProvider.releaseConnection(connection);
            return quotes;
        } catch (SQLException e) {
            throw new DbException(e.getMessage(), e);
        }
    }

    public Quote getQuoteById(int quoteId, int userId) throws DbException {
        String sql = "SELECT q.id, q.text, author.name as author, category.name as category, q.date_added, (select count(id) from liked_quote where quote_id = q.id) as rating, " +
                "       CASE WHEN ? IS NULL THEN FALSE " +
                "            ELSE EXISTS(SELECT 1 FROM liked_quote WHERE quote_id = q.id AND account_id = ?) " +
                "       END AS liked " +
                "FROM quote q " +
                "JOIN author ON q.author_id = author.id " +
                "JOIN category ON q.category_id = category.id " +
                "where q.id = ?";
        try (PreparedStatement st = connectionProvider.getCon().prepareStatement(sql)) {
            st.setInt(1, userId);
            st.setInt(2, userId);
            st.setInt(3, quoteId);
            ResultSet result = st.executeQuery();
            if (result.next()) {
                return new Quote(
                        result.getInt("id"),
                        result.getString("text"),
                        result.getString("author"),
                        result.getInt("rating"),
                        result.getDate("date_added"),
                        result.getString("category"),
                        result.getBoolean("liked")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new DbException("Error fetching quote", e);
        }
    }

    public Quote updateQuote(int userId, int quoteId, String text, String author, int categoryId) throws DbException {
        String checkAuthorSql = "SELECT id FROM author WHERE name = ?";
        String insertAuthorSql = "INSERT INTO author (name) VALUES (?)";
        String updateQuoteSql = "UPDATE quote SET text = ?, author_id = ?, category_id = ? WHERE id = ?";

        Connection connection = null;
        try {
            connection = connectionProvider.getCon();
            connection.setAutoCommit(false); // Начало транзакции

            // Получение ID автора
            int authorId;
            try (PreparedStatement checkAuthorSt = connection.prepareStatement(checkAuthorSql)) {
                checkAuthorSt.setString(1, author);
                ResultSet authorRs = checkAuthorSt.executeQuery();
                if (authorRs.next()) {
                    authorId = authorRs.getInt("id");
                } else {
                    try (PreparedStatement insertAuthorSt = connection.prepareStatement(insertAuthorSql, Statement.RETURN_GENERATED_KEYS)) {
                        insertAuthorSt.setString(1, author);
                        insertAuthorSt.executeUpdate();
                        ResultSet generatedKeys = insertAuthorSt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            authorId = generatedKeys.getInt(1);
                        } else {
                            throw new DbException("Failed to insert author");
                        }
                    }
                }
            }

            // Обновление цитаты
            try (PreparedStatement updateQuoteSt = connection.prepareStatement(updateQuoteSql)) {
                updateQuoteSt.setString(1, text);
                updateQuoteSt.setInt(2, authorId);
                updateQuoteSt.setInt(3, categoryId);
                updateQuoteSt.setInt(4, quoteId);
                int rowsAffected = updateQuoteSt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DbException("No quote found with the given ID");
                }
            }

            connection.commit(); // Завершение транзакции
            return getQuoteById(quoteId, userId); // Возвращаем обновленную цитату

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Откат транзакции в случае ошибки
                } catch (SQLException rollbackEx) {
                    throw new DbException("Error rolling back transaction", rollbackEx);
                }
            }
            throw new DbException("Error updating quote", e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Возвращаем в исходное состояние
                    connectionProvider.releaseConnection(connection);
                } catch (SQLException e) {
                    throw new DbException("Error releasing connection", e);
                }
            }
        }
    }
}
