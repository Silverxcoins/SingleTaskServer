package dao;

import controllers.HttpResponse;

public interface UserDAO {
    HttpResponse signUp(String jsonString);

    HttpResponse signIn(String jsonString);
}
