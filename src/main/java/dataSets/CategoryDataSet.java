package dataSets;

import org.codehaus.jackson.JsonNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class CategoryDataSet {
    private Integer id;
    private String name;
    private int parent;
    private int user;
    private boolean isDeleted;
    private boolean isUpdated;
    private Timestamp updated;

    public CategoryDataSet(JsonNode json) {
        if (json.has("id")) this.id = json.get("id").getIntValue();
        this.name = json.get("name").getTextValue();
        if (json.has("parent")) this.parent = json.get("parent").getIntValue();
        this.user = json.get("user").getIntValue();
        if (json.has("isDeleted")) this.isDeleted = json.get("isDeleted").getBooleanValue();
        if (json.has("isUpdated")) this.isUpdated = json.get("isUpdated").getBooleanValue();
        this.updated = Timestamp.valueOf(json.get("updated").getTextValue());
    }

    public CategoryDataSet(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.parent = resultSet.getInt("parent");
        this.user = resultSet.getInt("user");
        this.updated = resultSet.getTimestamp("updated");
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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
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
