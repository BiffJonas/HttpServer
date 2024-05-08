package com.httpserver.server.request;

import java.util.Map;
import java.util.HashMap;

public class RequestParser {

    public String getPath(String request) {
        return request.split("\r\n")[0].split(" ")[1];
    }

    public String getVersion(String request) {
        return request.split("\r\n")[0].split(" ")[2];
    }

    public String getMethod(String request) {
        return request.split("\r\n")[0].split(" ")[0];
    }

    public String getHost(String request) {
        return request.split("\r\n")[1].split(": ")[1];
    }

    public Map<String, String> getHeaders(String request) {
        String[] requestLines = request.split("\r\n");
        Map<String, String> headers = new HashMap<>();

        for (int h = 2; h < requestLines.length; h++) {
            String headerKey = requestLines[h].split(": ")[0];
            String headerValue = requestLines[h].split(": ")[1];
            headers.put(headerKey, headerValue);
        }
        return headers;
    }

    public String getBody(String request) {
        String[] parts = request.split("\r\n\r\n");

        // Check if index 1 exists and is not null
        if (parts.length > 1 && parts[1] != null) {
            return parts[1];
        } else {
            // Handle the case where index 1 doesn't exist or is null
            return ""; // or throw an exception, or handle it according to your logic
        }
    }

    public int getContentLength(String request) {
        String[] lines = request.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("Content-Length:")) {
                return Integer.parseInt(line.split(":")[1].trim());
            }
        }
        return 0; // Return 0 if no Content-Length header is found
    }
}
