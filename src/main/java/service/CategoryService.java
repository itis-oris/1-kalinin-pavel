package service;

import dao.CategoryDao;
import entity.Category;
import lombok.AllArgsConstructor;
import util.DbException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@AllArgsConstructor
public class CategoryService {
    private CategoryDao categoryDao;

    public ArrayList<Category> getAllCategory() throws DbException {
        return categoryDao.getAllCategory();
    }
}
