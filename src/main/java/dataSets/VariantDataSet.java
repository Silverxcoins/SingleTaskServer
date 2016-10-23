package dataSets;

import org.codehaus.jackson.JsonNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VariantDataSet {
    private int id;
    private String name;
    private int category;
    private List<Integer> tasks;

    public VariantDataSet(JsonNode json) {
        if (json.has("id")) this.id = json.get("id").getIntValue();
        this.name = json.get("name").getTextValue();
        this.category = json.get("category").getIntValue();
        if (json.has("tasks")) {
            final String[] tasksStringArray = json.get("tasks").getTextValue().split(",");
            tasks = new ArrayList<>();
            for (String task : tasksStringArray) {
                tasks.add(Integer.parseInt(task));
            }
        }
    }

    public VariantDataSet(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.category = resultSet.getInt("category");
    }

    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
