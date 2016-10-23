package dao.impl;

import controllers.HttpResponse;
import dao.VariantDAO;
import dataSets.VariantDataSet;
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

public class VariantDAOimpl implements VariantDAO{
    final ObjectMapper mapper;

    public VariantDAOimpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public HttpResponse addVariant(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        final VariantDataSet variant;
        try {
            variant = new VariantDataSet(json);
        } catch (Exception e) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_INSERT);
            stmt.setString(1, variant.getName());
            stmt.setInt(2, variant.getCategory());
            stmt.execute();

            final ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            final int variantId = generatedKeys.getInt(1);

            if (variant.getTasks() != null) {
                final PreparedStatement taskStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_INSERT);
                taskStmt.setInt(2, variantId);
                final PreparedStatement categoryTaskStmt = connection.prepareStatement(SQLHelper.CATEGORY_TO_TASK_SELECT);
                final PreparedStatement varTaskDelStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_FULL_DELETE);
                for (int taskId : variant.getTasks()) {
                    categoryTaskStmt.setInt(1, taskId);
                    final ResultSet resultSet = categoryTaskStmt.executeQuery();
                    while (resultSet.next()) {
                        if (resultSet.getInt("category") == variant.getCategory()) {
                            varTaskDelStmt.setInt(2, resultSet.getInt("id"));
                            varTaskDelStmt.setInt(1, taskId);
                        }

                    }
                    taskStmt.setInt(1, taskId);
                    taskStmt.execute();
                }
                taskStmt.close();
            }

            return new HttpResponse(new Integer(generatedKeys.getInt(1)));
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse deleteVariant(String jsonString) {
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

        try (Connection connection = Main.connection.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_DELETE);
            stmt.setInt(1, id);
            stmt.execute();

            stmt = connection.prepareStatement(SQLHelper.VARIANT_TASK_DELETE);
            stmt.setInt(1, id);
            stmt.execute();

            stmt.close();
            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse updateVariant(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        final VariantDataSet variant;
        try {
            variant = new VariantDataSet(json);
        } catch (Exception e) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_UPDATE);
            stmt.setString(1, variant.getName());
            stmt.setInt(2, variant.getId());
            stmt.execute();
            System.out.println("1");

            PreparedStatement taskStmt = connection.prepareStatement(SQLHelper.VARIANT_TASK_DELETE);
            taskStmt.setInt(1, variant.getId());

            if (variant.getTasks() != null) {
                taskStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_INSERT);
                taskStmt.setInt(2, variant.getId());
                final PreparedStatement categoryTaskStmt = connection.prepareStatement(SQLHelper.CATEGORY_TO_TASK_SELECT);
                final PreparedStatement varTaskDelStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_FULL_DELETE);
                for (int taskId : variant.getTasks()) {
                    categoryTaskStmt.setInt(1, taskId);
                    final ResultSet resultSet = categoryTaskStmt.executeQuery();
                    while (resultSet.next()) {
                        if (resultSet.getInt("category") == variant.getCategory()) {
                            varTaskDelStmt.setInt(1, taskId);
                            varTaskDelStmt.setInt(2, resultSet.getInt("id"));
                        }

                    }

                    taskStmt.setInt(1, taskId);
                    taskStmt.execute();
                }
                taskStmt.close();
            }

            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse getVariantsByTask(int taskId) {
        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_SELECT_BY_TASK);
            stmt.setInt(1, taskId);
            final ResultSet resultSet = stmt.executeQuery();
            final List<VariantDataSet> variants = new ArrayList<>();
            while (resultSet.next()) {
                final VariantDataSet variant = new VariantDataSet(resultSet);
                variants.add(variant);
            }

            stmt.close();
            return new HttpResponse(variants);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse getVariantsByCategory(int categoryId) {
        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_SELECT_BY_CATEGORY);
            stmt.setInt(1, categoryId);
            final ResultSet resultSet = stmt.executeQuery();
            final List<VariantDataSet> variants = new ArrayList<>();
            while (resultSet.next()) {
                final VariantDataSet variant = new VariantDataSet(resultSet);
                variants.add(variant);
            }

            stmt.close();
            return new HttpResponse(variants);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }
}
