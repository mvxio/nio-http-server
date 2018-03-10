package io.mvx.hhnio;

public enum HTTPResponseStatusCode {
    OK                 (200, "OK"),
    BAD_REQUEST        (400, "Bad Request"),
    NOT_FOUND          (404, "Not Found"),
    METHOD_NOT_ALLOWED (405, "Method Not Allowed");

    private final int code;
    private final String description;

    HTTPResponseStatusCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return description;
    }
}
