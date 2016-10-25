package dataSets;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.deser.std.TimestampDeserializer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDataSet {
    private Integer id;
    private String name;
    private String comment;
    private String date;
    private int time;
    private int user;
    private boolean isDeleted;
    private boolean isUpdated;
    private Timestamp updated;

    public TaskDataSet(JsonNode json) {
        if (json.has("id")) this.id = json.get("id").getIntValue();
        this.name = json.get("name").getTextValue();
        if (json.has("comment")) this.comment = json.get("comment").getTextValue();
        if (json.has("date")) this.date = json.get("date").getTextValue();
        this.time = json.get("time").getIntValue();
        this.user = json.get("user").getIntValue();
        if (json.has("isDeleted")) this.isDeleted = json.get("isDeleted").getBooleanValue();
        if (json.has("isUpdated")) this.isUpdated = json.get("isUpdated").getBooleanValue();
        this.updated = Timestamp.valueOf(json.get("updated").getTextValue());
        System.out.println("1 " + updated.toString());
    }

    public TaskDataSet(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.comment = resultSet.getString("comment");
        this.date = resultSet.getString("date");
        this.time = resultSet.getInt("time");
        this.user = resultSet.getInt("user");
        this.updated = resultSet.getTimestamp("updated");
        System.out.println("2 " + updated.toString());
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public Integer getId() {
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


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isUpdated() {
        return isUpdated;
    }

    public void setUpdated(boolean updated) {
        isUpdated = updated;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }

}
