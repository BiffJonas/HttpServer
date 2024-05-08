package com.httpserver.server.request;

import java.net.Socket;
import java.util.Map;

public class HttpRequest {
    private String method;
    private String path;
    private String version;
    private String host;
    private Map<String, String> headers;
    private String body;
    RequestParser requestParser = new RequestParser();

    public HttpRequest(String request) {
        this.setRequestParams(request);
    }

    public void setRequestParams(String request) {

        method = requestParser.getMethod(request);
        path = requestParser.getPath(request);
        version = requestParser.getVersion(request);
        host = requestParser.getHost(request);
        headers = requestParser.getHeaders(request);
        body = requestParser.getBody(request);
    }

    public String getAccessLog(Socket client) {

        return String.format(
                "\r\nClient %s, \r\nmethod %s, \r\npath %s, \r\nversion %s, \r\nhost %s, \r\nheaders %s, \r\nbody %s",
                client.toString(),
                this.getMethod(),
                this.getPath(),
                this.getVersion(),
                this.getHost(),
                this.getHeaders(),
                this.getBody());
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public String getCookie(String cookieName) {
        return headers.get(cookieName);
    }

    public String getHeaders() {
        StringBuilder headersString = new StringBuilder();
        headers.forEach((key, value) -> {
            headersString.append(key + ": " + value + "\r\n");
        });
        return headersString.toString();
    }

    public String getBody() {
        return body;
    }
}
