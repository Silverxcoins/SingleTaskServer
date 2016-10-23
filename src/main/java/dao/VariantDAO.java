package dao;

import controllers.HttpResponse;

public interface VariantDAO {

    HttpResponse addVariant(String json);

    HttpResponse deleteVariant(String json);

    HttpResponse updateVariant(String json);

    HttpResponse getVariantsByTask(int taskId);

    HttpResponse getVariantsByCategory(int categoryId);
}
