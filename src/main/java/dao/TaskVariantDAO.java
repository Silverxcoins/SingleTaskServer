package dao;

import controllers.HttpResponse;

public interface TaskVariantDAO {
    HttpResponse syncTaskVariant(String json);

    HttpResponse getTaskVariant(int userId);
}
