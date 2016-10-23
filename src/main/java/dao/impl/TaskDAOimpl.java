package dao.impl;

import controllers.HttpResponse;
import dao.TaskDAO;
import dataSets.TaskDataSet;
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

public class TaskDAOimpl implements TaskDAO {
    final ObjectMapper mapper;

    public TaskDAOimpl() {
        mapper = new ObjectMapper();
    }

    @Override
    public HttpResponse addTask(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        final TaskDataSet task;
        try {
            task = new TaskDataSet(json);
        } catch (Exception e) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_INSERT);
            stmt.setString(1, task.getName());
            stmt.setObject(2, task.getComment());
            stmt.setObject(3, task.getDate());
            stmt.setInt(4, task.getTime());
            stmt.setInt(5, task.getUser());
            stmt.execute();

            final ResultSet generatedKeys = stmt.getGeneratedKeys();
            generatedKeys.next();
            final int taskId = generatedKeys.getInt(1);

            if (task.getVariants() != null) {
                final PreparedStatement variantStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_INSERT);
                variantStmt.setInt(1, taskId);
                for (int variantId : task.getVariants()) {
                    variantStmt.setInt(2, variantId);
                    variantStmt.execute();
                }
                variantStmt.close();
            }

            stmt.close();
            return new HttpResponse(new Integer(taskId));
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse deleteTask(String jsonString) {
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
            PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_DELETE);
            stmt.setInt(1, id);
            stmt.execute();

            stmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_DELETE);
            stmt.setInt(1, id);
            stmt.execute();

            stmt.close();
            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse updateTask(String jsonString) {
        final JsonNode json;
        try {
            json = mapper.readValue(jsonString, JsonNode.class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        final TaskDataSet task;
        try {
            task = new TaskDataSet(json);
        } catch (Exception e) {
            return new HttpResponse(HttpResponse.INCORRECT_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_UPDATE);
            stmt.setString(1, task.getName());
            stmt.setObject(2, task.getComment());
            stmt.setObject(3, task.getDate());
            stmt.setInt(4, task.getTime());
            stmt.setInt(5, task.getUser());
            stmt.setInt(6, task.getId());
            stmt.execute();

            PreparedStatement variantStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_DELETE);
            variantStmt.setInt(1, task.getId());
            variantStmt.execute();

            if (task.getVariants() != null) {
                variantStmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_INSERT);
                variantStmt.setInt(1, task.getId());
                for (int variantId : task.getVariants()) {
                    variantStmt.setInt(2, variantId);
                    variantStmt.execute();
                }
            }

            variantStmt.close();
            stmt.close();
            return new HttpResponse(HttpResponse.OK);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }

    @Override
    public HttpResponse getTasks(int userId) {
        try (Connection connection = Main.connection.getConnection()) {
            final PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASKS_SELECT);
            stmt.setInt(1, userId);
            final ResultSet resultSet = stmt.executeQuery();
            final List<TaskDataSet> tasks = new ArrayList<>();
            while (resultSet.next()) {
                final TaskDataSet task = new TaskDataSet(resultSet);
                tasks.add(task);
            }

            stmt.close();
            return new HttpResponse(tasks);
        } catch (SQLException e) {
            return new HttpResponse(HttpResponse.UNKNOWN_ERROR);
        }
    }
}
