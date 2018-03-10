package io.mvx.hhnio;

import java.nio.MappedByteBuffer;

public class HTTPResponse {
    private final String httpVersion = "HTTP/1.1";
    private final HTTPResponseContentType contentType;
    private final HTTPResponseStatusCode statusCode;
    private final MappedByteBuffer fileContent;

    HTTPResponse(HTTPResponseContentType contentType,
                        HTTPResponseStatusCode statusCode) {
        this(contentType, statusCode, null);
    }

    HTTPResponse(HTTPResponseContentType contentType,
                        HTTPResponseStatusCode statusCode,
                        MappedByteBuffer fileContent) {
        this.contentType = contentType;
        this.statusCode = statusCode;
        this.fileContent = fileContent;
    }

    String getHttpVersion() {
        return httpVersion;
    }

    HTTPResponseContentType getContentType() {
        return contentType;
    }

    HTTPResponseStatusCode getStatusCode() {
        return statusCode;
    }

    MappedByteBuffer getFileContent() {
        return fileContent;
    }
}
