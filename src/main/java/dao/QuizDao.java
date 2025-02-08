package dao;

import configs.ConnectionProvider;
import entity.QuizQuote;
import entity.Quote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import util.DbException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@AllArgsConstructor
@Setter
@Getter
public class QuizDao {
    private ConnectionProvider connectionProvider;

    public QuizQuote getRandomQuiz() throws DbException {
        String sql = "SELECT * FROM quiz ORDER BY RANDOM() LIMIT 1";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                ArrayList<String> options = new ArrayList<>();
                options.add(rs.getString("option_a"));
                options.add(rs.getString("option_b"));
                options.add(rs.getString("option_c"));
                options.add(rs.getString("option_d"));
                QuizQuote quizQuote =  new QuizQuote(
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        options,
                        rs.getString("correct_option")
                );
                connectionProvider.releaseConnection(connection);
                return quizQuote;
            }
        }catch (SQLException e) {
            throw new DbException("Error fetching random quiz quote", e);
        }
        return null;
    }

    public ArrayList<QuizQuote> getAllQuizzes() throws DbException {
        ArrayList<QuizQuote> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quiz ORDER BY RANDOM()";
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql)) {
            ResultSet rs = st.executeQuery();
            ArrayList<String> options = new ArrayList<>();
            options.add(rs.getString("option_a"));
            options.add(rs.getString("option_b"));
            options.add(rs.getString("option_c"));
            options.add(rs.getString("option_d"));
            while (rs.next()) {
                quizzes.add(new QuizQuote(
                        rs.getInt("id"),
                        rs.getString("question_text"),
                        options,
                        rs.getString("correct_option")
                ));
            }
            connectionProvider.releaseConnection(connection);
        } catch (SQLException e) {
            throw new DbException("Error fetching all quizzes", e);
        }
        return quizzes;
    }
}
