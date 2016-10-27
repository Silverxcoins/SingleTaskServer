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
    public HttpResponse syncTasks(String jsonString) {
        final JsonNode[] nodes;
        try {
            nodes = mapper.readValue(jsonString, JsonNode[].class);
        } catch (IOException e) {
            return new HttpResponse(HttpResponse.INVALID_REQUEST);
        }

        try (Connection connection = Main.connection.getConnection()) {
            for (JsonNode node : nodes) {
                TaskDataSet clientTask = new TaskDataSet(node);
                if (clientTask.getId() == null || clientTask.getId() == 0) {
                    if (!clientTask.getIsDeleted()) {
                        addTask(connection, clientTask);
                    }
                    continue;
                }
                TaskDataSet serverTask = getTask(connection, clientTask.getId());
                if (serverTask == null) {
                    continue;
                }
                if (clientTask.getLastUpdateTS().after(serverTask.getLastUpdateTS())) {
                    if (clientTask.getIsDeleted()) {
                        deleteTask(connection, serverTask.getId());
                    } else if (clientTask.getIsUpdated()) {
                        updateTask(connection, clientTask);
                    }
                }
                updateClientId(connection, clientTask.getClientId(), serverTask.getId());
            }

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

    public int addTask(Connection connection, TaskDataSet task) throws SQLException{
        System.out.println("add task");
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_INSERT);
        stmt.setString(1, task.getName());
        stmt.setObject(2, task.getComment());
        stmt.setObject(3, task.getDate());
        stmt.setInt(4, task.getTime());
        stmt.setInt(5, task.getUser());
        stmt.setObject(6, task.getLastUpdateTS());
        stmt.setInt(7, task.getClientId());
        stmt.execute();

        final ResultSet generatedKeys = stmt.getGeneratedKeys();
        generatedKeys.next();
        final int taskId = generatedKeys.getInt(1);

        stmt.close();
        return taskId;
    }

    public void deleteTask(Connection connection, int id) throws SQLException {
        System.out.println("update task");
        PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_DELETE);
        stmt.setInt(1, id);
        stmt.execute();

        stmt = connection.prepareStatement(SQLHelper.TASK_VARIANT_DELETE);
        stmt.setInt(1, id);
        stmt.execute();

        stmt.close();
    }

    public void updateTask(Connection connection, TaskDataSet task) throws SQLException {
        System.out.println("update task");
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_UPDATE);
        stmt.setString(1, task.getName());
        stmt.setObject(2, task.getComment());
        stmt.setObject(3, task.getDate());
        stmt.setInt(4, task.getTime());
        stmt.setInt(5, task.getUser());
        stmt.setObject(6, task.getLastUpdateTS());
        stmt.setInt(7, task.getId());
        System.out.println(stmt.toString());
        stmt.execute();

        stmt.close();
    }

    public TaskDataSet getTask(Connection connection, int id) throws SQLException {
        final PreparedStatement stmt = connection.prepareStatement(SQLHelper.GET_TASK);
        stmt.setInt(1,id);
        ResultSet resultSet = stmt.executeQuery();
        if (resultSet.next()) {
            TaskDataSet task = new TaskDataSet(resultSet);
            stmt.close();
            return task;
        } else {
            stmt.close();
            return null;
        }
    }

    public void updateClientId(Connection connection, int clientId, int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SQLHelper.TASK_UPDATE_CLIENT_ID);
        stmt.setInt(1, clientId);
        stmt.setInt(2, id);
        stmt.execute();
        stmt.close();
    }
}
