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
    public HttpResponse addCategory(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        final CategoryDataSet category;
        try {
            category = new CategoryDataSet(json);
        } catch (Exception e) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORY_INSERT);
            stmt.setString(1, category.getName());
            stmt.setObject(2, null);
            stmt.setInt(3, category.getUser());
            stmt.execute();

            final ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();

            return new HttpResponse(new Integer(generatedKeys.getInt(1)));
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse deleteCategory(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        if (!json.has("id")) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }
        final int id = json.get("id").getIntValue();

        return deleteCategory(id);
    }

    private HttpResponse deleteCategory(int id) {
        try (Connection connection = Main.connection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORY_DELETE);
            stmt.setInt(1, id);
            stmt.execute();

            stmt = connection.prepareStatement(SQLHelper.CATEGORY_SELECT_CHILDS);
            stmt.setInt(1, id);
            final ResultSet childs = stmt.executeQuery();
            while (childs.next()) {
                final int childId = childs.getInt(1);
                deleteCategory(childId);
            }
            stmt.close();

            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse updateCategory(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        final CategoryDataSet category;
        try {
            category = new CategoryDataSet(json);
        } catch (Exception e) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.CATEGORY_UPDATE);
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getParent());
            stmt.setInt(3, category.getUser());
            stmt.setInt(4, category.getId());
            stmt.execute();

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
}
