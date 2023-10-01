package org.masos.embed.sysconfig.api.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ResponseUtil {

    private static final int ARBITARY_SIZE = 4096;

    public static void writeBinary(HttpServletResponse response, InputStream inputStream) {
        try {
            OutputStream os = response.getOutputStream();
            byte[] buffer = new byte[ARBITARY_SIZE];

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeText(HttpServletResponse response, String text) {
        try {
            // Obtenha o OutputStream da resposta
            OutputStream outputStream = response.getOutputStream();
            // Crie um OutputStreamWriter para escrever texto no OutputStream
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            // Escreva texto na resposta
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
