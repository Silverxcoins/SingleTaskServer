package dataSets;

import org.codehaus.jackson.JsonNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class CurrentTaskDataSet {
    private Integer id;
    private Timestamp taskStart;
    private Timestamp lastUpdate;

    public CurrentTaskDataSet(JsonNode json) {
        if (json.has("currentTask")) this.id = json.get("currentTask").getIntValue();
        if (json.has("taskStart") && json.get("taskStart").getTextValue() != null)
            this.taskStart = Timestamp.valueOf(json.get("taskStart").getTextValue());
        if (json.has("lastUpdate") && json.get("lastUpdate").getTextValue() != null)
            this.lastUpdate = Timestamp.valueOf(json.get("lastUpdate").getTextValue());
    }

    public CurrentTaskDataSet(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("currentTask");
        this.taskStart = resultSet.getTimestamp("taskStart");
        this.lastUpdate = resultSet.getTimestamp("lastUpdate");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTaskStart() {
        return (taskStart != null) ? taskStart.toString() : null;
    }

    public Timestamp getTaskStartTS() {
        return taskStart;
    }

    public void setTaskStart(Timestamp taskStart) {
        this.taskStart = taskStart;
    }

    public String getLastUpdate() {
        return (lastUpdate != null) ? lastUpdate.toString() : null;
    }

    public Timestamp getLastUpdateTS() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
