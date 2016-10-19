package controllers;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    public static final int OK = 0;
    public static final int NOT_FOUND = 1;
    public static final int INVALID_REQUEST = 2;
    public static final int INCORRECT_REQUEST = 3;
    public static final int UNKNOWN_ERROR = 4;
    public static final int ALREADY_EXIST = 5;
    private static final Map<Integer, String> CODES;
    private int code;
    @NotNull
    private Object response;

    static {
        CODES = new HashMap<>();

        CODES.put(OK, "OK");
        CODES.put(NOT_FOUND, "NOT_FOUND");
        CODES.put(INVALID_REQUEST, "INVALID_REQUEST");
        CODES.put(INCORRECT_REQUEST, "INCORRECT_REQUEST");
        CODES.put(UNKNOWN_ERROR, "UNKNOWN_ERROR");
        CODES.put(ALREADY_EXIST, "ALREADY_EXIST");
    }

    public HttpResponse(@NotNull Object response) {
        this.code = OK;
        this.response = response;
    }

    public HttpResponse(int code) {
        this.code = code;
        this.response = CODES.get(code);
    }

    public HttpResponse() {
        this.response = "";
        this.code = -1;
    }

    @NotNull
    public Object getResponse() { return response; }

    public void setResponse(@NotNull Object response) { this.response = response; }

    public int getCode() { return code; }

    public void setCode(int code) { this.code = code; }
}
