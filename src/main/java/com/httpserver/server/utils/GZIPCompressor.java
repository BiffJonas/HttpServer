package com.httpserver.server.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZIPCompressor {

    public ByteArrayOutputStream compress(byte[] content) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gos = new GZIPOutputStream(baos)) {

            gos.write(content);
            return baos;
        }
    }

}
