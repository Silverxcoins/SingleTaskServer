package dao.impl;

import controllers.HttpResponse;
import dao.TaskVariantDAO;
import dataSets.TaskVariantDataSet;
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

public class TaskVariantDAOimpl implements TaskVariantDAO {
    final ObjectMapper mapper;

    public TaskVariantDAOimpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public HttpResponse syncTaskVariant(String jsonString) {
        final JsonNode[] nodes;
        try {
            nodes = mapper.readValue(jsonString, JsonNode[].class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            for (JsonNode node : nodes) {
                TaskVariantDataSet clientTV = new TaskVariantDataSet(node);
                if (!clientTV.isDeleted()) {
                    addTaskVariant(connection, clientTV);
                    continue;
                } else {
                    System.out.println(clientTV.isDeleted());
                    deleteTaskVariant(connection, clientTV);
                }
            }

            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse getTaskVariant(int userId) {
        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_FULL_SELECT);
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

    public void addTaskVariant(Connection connection, TaskVariantDataSet tV) throws SQLException{
        if (getTaskVariant(connection, tV.getTask(), tV.getVariant()) != null
                || new TaskDAOimpl().getTask(connection, tV.getTask()) == null
                || new VariantDAOimpl().getVariant(connection, tV.getVariant()) == null) {
            return;
        }
        System.out.println("add taskVariant");
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.VARIANT_INSERT);
        stmt.setInt(1, tV.getTask());
        stmt.setInt(2, tV.getVariant());
        System.out.println(stmt.toString());
        stmt.execute();

        stmt.close();
    }

    public void deleteTaskVariant(Connection connection, TaskVariantDataSet tV) throws SQLException {
        System.out.println("delete taskVariant");
        PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_FULL_DELETE);
        stmt.setInt(1, tV.getTask());
        stmt.setInt(2, tV.getVariant());
        System.out.println(stmt.toString());
        stmt.execute();

        stmt.close();
    }

    private TaskVariantDataSet getTaskVariant(Connection connection, int task, int variant) throws SQLException {
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.GET_TASK_VARIANT);
        stmt.setInt(1, task);
        stmt.setInt(2, variant);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            TaskVariantDataSet tV = new TaskVariantDataSet(resultSet);
            stmt.close();
            return tV;
        } else {
            stmt.close();
            return null;
        }
    }
}
