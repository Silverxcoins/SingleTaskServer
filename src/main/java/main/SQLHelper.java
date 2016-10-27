package main;

public class SQLHelper {
    public static final String CATEGORY_INSERT = "INSERT INTO Category (name,parent,user,lastUpdate, clientId) VALUES (?,?,?,?,?);";
    public static final String CATEGORY_DELETE = "DELETE FROM Category WHERE id=?;";
    public static final String CATEGORY_UPDATE = "UPDATE Category SET name=?,parent=?,user=?,lastUpdate=? WHERE id=?";
    public static final String CATEGORY_SELECT_CHILDS = "SELECT id FROM Category WHERE parent=?;";
    public static final String CATEGORIES_SELECT = "SELECT * FROM Category WHERE user=?;";
    public static final String TASK_INSERT = "INSERT INTO Task (name,comment,date,time,user,lastUpdate, clientId) VALUES (?,?,?,?,?,?,?);";
    public static final String TASK_DELETE = "DELETE FROM Task WHERE id=?;";
    public static final String TASK_UPDATE = "UPDATE Task SET name=?,comment=?,date=?,time=?,user=?,lastUpdate=? WHERE id=?;";
    public static final String TASKS_SELECT = "SELECT * FROM Task WHERE user=?;";
    public static final String VARIANT_INSERT = "INSERT INTO Variant (name,category, clientId) VALUES (?,?,?);";
    public static final String VARIANT_DELETE = "DELETE FROM Variant WHERE id=?;";
    public static final String VARIANT_DELETE_BY_CATEGORY = "DELETE FROM Variant WHERE category=?;";
    public static final String VARIANT_DELETE_BY_TASK = "DELETE FROM Variant WHERE task=?;";
    public static final String VARIANTS_SELECT = "SELECT DISTINCT Variant.* FROM Variant JOIN Category WHERE Category.user=?;";
    public static final String VARIANT_UPDATE = "UPDATE Variant SET name=? WHERE id=?;";
    public static final String VARIANT_SELECT_BY_TASK = "SELECT Variant.* " +
            "FROM Variant JOIN TaskVariant ON Variant.id=TaskVariant.variant " +
            "WHERE TaskVariant.task=?";
    public static final String VARIANT_SELECT_BY_CATEGORY = "SELECT * FROM Variant WHERE category=?";
    public static final String TASK_VARIANT_INSERT = "INSERT INTO TaskVariant (task,variant) VALUES (?,?);";
    public static final String TASK_VARIANT_DELETE = "DELETE FROM TaskVariant WHERE task=?";
    public static final String VARIANT_TASK_DELETE = "DELETE FROM TaskVariant WHERE variant=?";
    public static final String TASK_VARIANT_FULL_DELETE = "DELETE FROM TaskVariant WHERE task=? AND variant=?";
    public static final String TASK_VARIANT_FULL_SELECT = "SELECT DISTINCT TaskVariant.* FROM TaskVariant JOIN Task WHERE Task.user=?";
    public static final String CATEGORY_TO_TASK_SELECT = "SELECT Variant.id,Variant.category " +
            "FROM Variant JOIN TaskVariant ON Variant.id = TaskVariant.variant" +
            "WHERE TaskVariant.task = ?";

    public static final String GET_TASK = "SELECT * FROM Task WHERE id=?";
    public static final String GET_CATEGORY = "SELECT * FROM Category WHERE id=?";
    public static final String GET_VARIANT = "SELECT * FROM Variant WHERE id=?";
    public static final String GET_TASK_VARIANT = "SELECT * FROM TaskVariant WHERE task=? AND variant=?";
}
