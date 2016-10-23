package dao;

import controllers.HttpResponse;

public interface CategoryDAO {

    HttpResponse addCategory(String json);

    HttpResponse deleteCategory(String json);

    HttpResponse updateCategory(String json);

    HttpResponse getCategories(int userId);
}
