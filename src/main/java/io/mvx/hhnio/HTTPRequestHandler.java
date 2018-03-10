package io.mvx.hhnio;

import java.io.IOException;
import java.nio.MappedByteBuffer;

public class HTTPRequestHandler {

    private final DataManager dataManager = new DataManager();

    private HTTPResponseContentType getContentTypeByExtension(String extension) {
        HTTPResponseContentType contentType;
        switch (extension) {
            case "htm":
            case "html":
                contentType = HTTPResponseContentType.TEXT_HTML;
                break;
            case "js":
                contentType = HTTPResponseContentType.APPLICATION_JAVASCRIPT;
                break;
            case "jpg":
            case "jpeg":
                contentType = HTTPResponseContentType.IMAGE_JPEG;
                break;
            default:
                contentType = HTTPResponseContentType.APPLICATION_OCTET_STREAM;
        }
        return contentType;
    }

    private boolean checkHttpMethod(String httpMethod) {
        // checks if method is 'GET'.
        return httpMethod.equalsIgnoreCase("GET");
    }

    private boolean checkURI(String uri) {
        // checks if uri is valid path.
        // Valid uris: /index.html, /path/to/1/image.jpeg, ...
        return uri.matches("^(/[0-9a-zA-Z]+)+(\\.[a-zA-Z]+)?$");
    }

    private String prepareURI(String uri) {
        // if uri is '/' then change it to '/index.html'.
        if (uri.equals("/")) {
            uri = "/index.html";
        }
        return uri;
    }

    private String getExtension(String uri) {
        // extract extension from uri.
        return uri.substring(uri.indexOf(".") + 1);
    }

    public HTTPResponse processRequest(HTTPRequest httpRequest) {
        if (!checkHttpMethod(httpRequest.getHttpMethod())) {
            return new HTTPResponse(HTTPResponseContentType.TEXT_HTML, HTTPResponseStatusCode.METHOD_NOT_ALLOWED);
        }

        String uri = prepareURI(httpRequest.getURI());

        if (!checkURI(uri)) {
            return new HTTPResponse(HTTPResponseContentType.TEXT_HTML, HTTPResponseStatusCode.BAD_REQUEST);
        }

        try {
            MappedByteBuffer fileContent = dataManager.getFile(uri);
            HTTPResponseStatusCode statusCode = HTTPResponseStatusCode.OK;
            HTTPResponseContentType contentType = getContentTypeByExtension(getExtension(uri));
            return new HTTPResponse(contentType, statusCode, fileContent);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return new HTTPResponse(HTTPResponseContentType.TEXT_HTML, HTTPResponseStatusCode.NOT_FOUND);
    }

}
