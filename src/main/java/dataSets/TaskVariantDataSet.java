package dataSets;

import org.codehaus.jackson.JsonNode;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskVariantDataSet {
    private int task;
    private int variant;
    private boolean isDeleted;

    public TaskVariantDataSet(JsonNode json) {
        this.task = json.get("task").getIntValue();
        this.variant = json.get("variant").getIntValue();
        if (json.has("isDeleted")) this.isDeleted = json.get("isDeleted").getBooleanValue();
    }

    public TaskVariantDataSet(ResultSet resultSet) throws SQLException {
        this.task = resultSet.getInt("task");
        this.variant = resultSet.getInt("variant");
    }

    public int getTask() {
        return task;
    }

    public void setTask(int task) {
        this.task = task;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
