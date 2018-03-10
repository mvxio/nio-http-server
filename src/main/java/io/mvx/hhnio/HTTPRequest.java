package io.mvx.hhnio;

public class HTTPRequest {
    private final String httpMethod;
    private final String uri;

    HTTPRequest(String requestString) {
        String[] requestStrings = requestString.split("\\s+");

        httpMethod = requestStrings[0];
        uri = requestStrings[1];
    }

    String getHttpMethod() {
        return httpMethod;
    }

    String getURI() {
        return uri;
    }
}
