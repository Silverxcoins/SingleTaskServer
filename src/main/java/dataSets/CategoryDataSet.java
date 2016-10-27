package dataSets;

import org.codehaus.jackson.JsonNode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public class CategoryDataSet {
    private Integer id;
    private Integer clientId;
    private String name;
    private int parent;
    private int user;
    private boolean isDeleted;
    private boolean isUpdated;
    private Timestamp lastUpdate;

    public CategoryDataSet(JsonNode json) {
        if (json.has("serverId")) this.id = json.get("serverId").getIntValue();
        if (json.get("serverId").getIntValue() == 0) id = null;
        if (json.has("id")) this.clientId = json.get("id").getIntValue();
        this.name = json.get("name").getTextValue();
        if (json.has("parent")) this.parent = json.get("parent").getIntValue();
        this.user = json.get("user").getIntValue();
        if (json.has("isDeleted")) this.isDeleted = json.get("isDeleted").getBooleanValue();
        if (json.has("isUpdated")) this.isUpdated = json.get("isUpdated").getBooleanValue();
        this.lastUpdate = Timestamp.valueOf(json.get("lastUpdate").getTextValue());
    }

    public CategoryDataSet(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt("id");
        this.name = resultSet.getString("name");
        this.parent = resultSet.getInt("parent");
        this.user = resultSet.getInt("user");
        this.lastUpdate = resultSet.getTimestamp("lastUpdate");
        System.out.println("lastupdate! " + lastUpdate);
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

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean getIsUpdated() {
        return isUpdated;
    }

    public void setIsUpdated(boolean lastUpdate) {
        isUpdated = lastUpdate;
    }


    public String getLastUpdate() {
        return lastUpdate.toString();
    }

    public Timestamp getLastUpdateTS() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


    public Integer getClientId() {
        return clientId;
    }

    public Integer getParentAsObject() {
        return new Integer(parent);
    }
}
