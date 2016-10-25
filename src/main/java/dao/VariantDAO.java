package dao;

import controllers.HttpResponse;

public interface VariantDAO {

//    HttpResponse addVariant(String json);
//
//    HttpResponse deleteVariant(String json);
//
//    HttpResponse updateVariant(String json);

    HttpResponse getVariants(int userId);

    HttpResponse syncVariants(String json);

    HttpResponse getVariantsByTask(int taskId);

    HttpResponse getVariantsByCategory(int categoryId);
}
