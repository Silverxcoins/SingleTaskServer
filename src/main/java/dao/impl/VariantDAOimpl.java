package dao.impl;

import controllers.HttpResponse;
import dao.VariantDAO;
import dataSets.CategoryDataSet;
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
    public HttpResponse syncVariants(String jsonString) {
        final JsonNode[] nodes;
        try {
            nodes = mapper.readValue(jsonString, JsonNode[].class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            for (JsonNode node : nodes) {
                VariantDataSet clientVariant = new VariantDataSet(node);
                if (clientVariant.getId() == null) {
                    if (!clientVariant.getIsDeleted()) {
                        addVariant(connection, clientVariant);
                    }
                    continue;
                }
                VariantDataSet serverVariant = getVariant(connection, clientVariant.getId());
                if (serverVariant == null) {
                    continue;
                }
                if (clientVariant.getIsDeleted()) {
                    System.out.println(clientVariant.getIsDeleted());
                    deleteVariant(connection, serverVariant.getId());
                }
            }

            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse getVariants(int userId) {
        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANTS_SELECT);
            stmt.setInt(1, userId);
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

    public void addVariant(Connection connection, VariantDataSet variant) throws SQLException{
        if (new CategoryDAOimpl().getCategory(connection, variant.getCategory()) == null) {
            return;
        }
        System.out.println("add variant");
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_INSERT);
        stmt.setString(1, variant.getName());
        stmt.setInt(2, variant.getCategory());
        System.out.println(stmt.toString());
        stmt.execute();

        final ResultSet generatedKeys = stmt.getGeneratedKeys();
        generatedKeys.next();
        final int variantId = generatedKeys.getInt(1);

        stmt.close();
    }

    public void deleteVariant(Connection connection, int id) throws SQLException {
        System.out.println("delete variant");
        PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_DELETE);
        stmt.setInt(1, id);
        System.out.println(stmt.toString());
        stmt.execute();

        stmt = connection.prepareStatement(SQLHelper.VARIANT_TASK_DELETE);
        stmt.setInt(1, id);
        stmt.execute();

        stmt.close();
    }

    public VariantDataSet getVariant(Connection connection, int id) throws SQLException {
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.GET_VARIANT);
        stmt.setInt(1,id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            VariantDataSet variant = new VariantDataSet(resultSet);
            stmt.close();
            return variant;
        } else {
            stmt.close();
            return null;
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
