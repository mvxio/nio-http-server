package io.mvx.hhnio;

public enum HTTPResponseContentType {
    TEXT_HTML                ("text/html"),
    APPLICATION_JAVASCRIPT   ("application/javascript"),
    IMAGE_JPEG               ("image/jpeg"),
    APPLICATION_OCTET_STREAM ("application/octet-stream");

    private final String contentType;

    HTTPResponseContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return contentType;
    }
}
