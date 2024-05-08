package com.httpserver.server.client;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.httpserver.server.request.HttpRequest;
import com.httpserver.server.utils.GZIPCompressor;

public class ClientHandler implements Runnable {
    private Socket client;

    public ClientHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            System.out.println("DEBUG: got new client: " + client.toString());

            String request = buildRequest(client.getInputStream());
            HttpRequest httpRequest = new HttpRequest(request);
            System.out.println(request);

            String accessLog = httpRequest.getAccessLog(client);
            // System.out.println(accessLog);
            // System.out.println(request);

            Path filePath = getFilePath(httpRequest.getPath());

            if (httpRequest.getMethod().equals("GET")) {
                handleGetRequest(filePath);
            } else if (httpRequest.getMethod().equals("POST")) {
                handlePostRequest(filePath, httpRequest);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handlePostRequest(Path Path, HttpRequest request) {
        try {
            String cookie = request.getCookie("Cookie");
            if (cookie.equals("")) {
                throw new Exception("No cookie in header!!");
            }

            Path filePath = getFilePath("/inccounter");
            FileOutputStream fos = new FileOutputStream(filePath.toFile());

            System.out.println(cookie);
            fos.write(cookie.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGetRequest(Path filePath) {
        try {
            String contentType = guessContentType(filePath);
            byte[] content = Files.readAllBytes(filePath);
            sendResponse(client, "200 OK", contentType, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildRequest(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder requestedBuilder = new StringBuilder();

        String line;

        // Read the request headers
        while (!(line = br.readLine()).isBlank()) {
            requestedBuilder.append(line).append("\r\n");
        }

        requestedBuilder.append("\r\n"); // Add an extra line to separate headers from body

        // Read the request body
        int contentLength = getContentLength(requestedBuilder.toString());
        if (contentLength > 0) {
            System.out.println("This is a post request!!!");
            char[] body = new char[contentLength];
            br.read(body, 0, contentLength);
            requestedBuilder.append(body);
        }

        return requestedBuilder.toString();
    }

    private int getContentLength(String request) {
        String[] lines = request.split("\r\n");
        for (String line : lines) {
            if (line.startsWith("Content-Length:")) {
                return Integer.parseInt(line.split(":")[1].trim());
            }
            break;
        }
        return 0; // Return 0 if no Content-Length header is found
    }

    private void sendResponse(Socket client, String status, String contentType, byte[] content)
            throws IOException {
        try (OutputStream clientOutput = client.getOutputStream()) {
            GZIPCompressor gc = new GZIPCompressor();
            ByteArrayOutputStream compressedContent = gc.compress(content);

            String counterValueFile = getIncCounterFileValue();
            // Send the response headers
            clientOutput.write(("HTTP/1.1 " + status + "\r\n").getBytes());
            clientOutput.write(("Content-Type: " + contentType + "\r\n").getBytes());
            clientOutput.write(("Content-Length: " + compressedContent.size() + "\r\n").getBytes());
            clientOutput.write("Content-Encoding: gzip\r\n".getBytes());
            clientOutput.write(("Cookie: " + counterValueFile + "\r\n").getBytes());
            clientOutput.write("\r\n".getBytes());

            // Send the compressed content
            clientOutput.write(compressedContent.toByteArray());
            clientOutput.flush();
        } finally {
            client.close();
        }
    }

    private String getIncCounterFileValue() throws IOException {

        Path counterFile = getFilePath("/inccounter");
        FileInputStream fis = new FileInputStream(counterFile.toFile());
        String counterValue = fis.readAllBytes().toString();
        fis.close();
        return counterValue;

    }

    private String guessContentType(Path filePath)
            throws IOException {
        return Files.probeContentType(filePath);
    }

    private Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/resources/index.html";
        } else if ("/about".equals(path)) {
            path = "/resources/about.html";
        } else if ("/form".equals(path)) {
            path = "/resources/form.html";
        } else if ("/cookie".equals(path)) {
            path = "/resources/cookie.html";
        } else if ("/inccounter".equals(path)) {
            path = "resources/countervalue";
        }

        return Paths.get("/home/BiffJonas/repos/Java/HttpServer", path);
    }

}
