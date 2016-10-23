package dataSets;

import org.codehaus.jackson.JsonNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDataSet {
    private int id;
    private String name;
    private String comment;
    private String date;
    private int time;
    private int user;
    private List<Integer> variants;

    public TaskDataSet(JsonNode json) {
        if (json.has("id")) this.id = json.get("id").getIntValue();
        this.name = json.get("name").getTextValue();
        if (json.has("comment")) this.comment = json.get("comment").getTextValue();
        if (json.has("date")) this.date = json.get("date").getTextValue();
        this.time = json.get("time").getIntValue();
        this.user = json.get("user").getIntValue();
        if (json.has("variants")) {
            final String[] variantsStringArray = json.get("tasks").getTextValue().split(",");
            variants = new ArrayList<>();
            for (String task : variantsStringArray) {
                variants.add(Integer.parseInt(task));
            }
        }
    }

    public TaskDataSet(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.comment = resultSet.getString("comment");
        this.date = resultSet.getString("date");
        this.time = resultSet.getInt("time");
        this.user = resultSet.getInt("user");
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Integer> getVariants() {
        return variants;
    }

    public void setVariants(List<Integer> variants) {
        this.variants = variants;
    }

}
