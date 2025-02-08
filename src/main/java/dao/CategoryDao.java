package dao;

import entity.Category;
import lombok.AllArgsConstructor;
import configs.ConnectionProvider;
import util.DbException;

import java.sql.*;
import java.util.ArrayList;

@AllArgsConstructor
public class CategoryDao {
    private ConnectionProvider connectionProvider;

    public ArrayList<Category> getAllCategory() throws DbException {
        String sql = "SELECT * FROM category";
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection connection = connectionProvider.getCon();
             PreparedStatement st = connection.prepareStatement(sql);){
            ResultSet result = st.executeQuery();
            while (result.next()) {
                categories.add(new Category(result.getInt("id"), result.getString("name"), result.getString("description")));
            }
            connectionProvider.releaseConnection(connection);
            return categories;
        } catch (SQLException e) {
            throw new DbException("Can't get count of books in DB.", e);
        }
    }
}
