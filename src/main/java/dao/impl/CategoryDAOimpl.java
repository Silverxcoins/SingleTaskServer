package dao.impl;

import controllers.HttpResponse;
import dao.CategoryDAO;
import dataSets.CategoryDataSet;
import main.Main;
import main.SQLHelper;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOimpl implements CategoryDAO {
    final ObjectMapper mapper;

    public CategoryDAOimpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public HttpResponse syncCategories(String jsonString) {
        final JsonNode[] nodes;
        try {
            nodes = mapper.readValue(jsonString, JsonNode[].class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            for (JsonNode node : nodes) {
                CategoryDataSet clientCategory = new CategoryDataSet(node);
                if (clientCategory.getId() == null) {
                    if (!clientCategory.isDeleted()) {
                        addCategory(connection, clientCategory);
                    }
                    continue;
                }
                CategoryDataSet serverCategory = getCategory(connection, clientCategory.getId());
                if (serverCategory == null) {
                    continue;
                }
                if (clientCategory.getUpdated().after(serverCategory.getUpdated())) {
                    if (clientCategory.isDeleted()) {
                        System.out.println(clientCategory.isDeleted());
                        deleteCategory(connection, serverCategory.getId());
                    } else if (clientCategory.isUpdated()) {
                        updateCategory(connection, clientCategory);
                    }
                }
            }

            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse getCategories(int userId) {
        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORIES_SELECT);
            stmt.setInt(1, userId);
            final ResultSet resultSet = stmt.executeQuery();
            final List<CategoryDataSet> categories = new ArrayList<>();
            while (resultSet.next()) {
                final CategoryDataSet category = new CategoryDataSet(resultSet);
                categories.add(category);
            }

            stmt.close();
            return new HttpResponse(categories);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    public int addCategory(Connection connection, CategoryDataSet category) throws SQLException{
        System.out.println("add category");
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORY_INSERT);
        stmt.setString(1, category.getName());
        stmt.setInt(2, category.getParent());
        stmt.setInt(3, category.getUser());
        stmt.setObject(4, category.getUpdated());
        System.out.println(stmt.toString());
        stmt.execute();

        final ResultSet generatedKeys = stmt.getGeneratedKeys();
        generatedKeys.next();
        final int categoryId = generatedKeys.getInt(1);

        stmt.close();
        return categoryId;
    }

    public void deleteCategory(Connection connection, int id) throws SQLException {
        System.out.println("delete category");
        PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORY_DELETE);
        stmt.setInt(1, id);
        System.out.println(stmt.toString());
        stmt.execute();

        stmt = connection.prepareStatement(SQLHelper.CATEGORY_SELECT_CHILDS);
        stmt.setInt(1, id);
        final ResultSet childs = stmt.executeQuery();
        while (childs.next()) {
            final int childId = childs.getInt(1);
            deleteCategory(connection, childId);
        }

        stmt = connection.prepareStatement(SQLHelper.VARIANT_DELETE_BY_CATEGORY);
        stmt.setInt(1, id);
        stmt.execute();

        stmt.close();
    }

    public void updateCategory(Connection connection, CategoryDataSet category) throws SQLException {
        System.out.println("update category");
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORY_UPDATE);
        stmt.setString(1, category.getName());
        stmt.setInt(2, category.getParent());
        stmt.setInt(3, category.getUser());
        stmt.setObject(4, category.getUpdated());
        stmt.setInt(5, category.getId());
        stmt.execute();

        stmt.close();
    }

    public CategoryDataSet getCategory(Connection connection, int id) throws SQLException {
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.GET_CATEGORY);
        stmt.setInt(1,id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            CategoryDataSet category = new CategoryDataSet(resultSet);
            stmt.close();
            return category;
        } else {
            stmt.close();
            return null;
        }
    }
}
