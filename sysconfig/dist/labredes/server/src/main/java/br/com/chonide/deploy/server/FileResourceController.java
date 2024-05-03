package br.com.chonide.deploy.server;

import br.com.chonide.deploy.core.Constants;
import com.google.common.io.ByteStreams;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@WebServlet("/files")
@MultipartConfig
public class FileResourceController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part file = req.getPart(Constants.FILE_PARAMETER_NAME);
        String path = req.getParameter(Constants.PATH_PARAMETER_NAME);
        String token = req.getParameter(Constants.TOKEN_PARAMETER_NAME);

        if (file == null || file.getSize() == 0 || path == null || path.isEmpty() || token == null || !token.equals(
                Constants.API_TOKEN)) {
            resp.setStatus(400);
            return;
        }

        Path folderPath = Paths.get(path.substring(0, path.lastIndexOf(Constants.FILE_SEPARATOR)));
        Files.createDirectories(folderPath);

        Path filePath = Paths.get(path);
        Files.deleteIfExists(filePath);
        Files.createFile(filePath);

        byte[] byteArray = ByteStreams.toByteArray(file.getInputStream());
        Files.write(filePath, byteArray);

        resp.setStatus(200);
    }
}
