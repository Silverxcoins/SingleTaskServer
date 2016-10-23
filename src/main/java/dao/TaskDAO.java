package dao;

import controllers.HttpResponse;

public interface TaskDAO {

    HttpResponse addTask(String json);

    HttpResponse deleteTask(String json);

    HttpResponse updateTask(String json);

    HttpResponse getTasks(int userId);
}
