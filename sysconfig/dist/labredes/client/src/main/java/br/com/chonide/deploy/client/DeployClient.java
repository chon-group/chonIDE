package br.com.chonide.deploy.client;

import br.com.chonide.deploy.core.Constants;
import okhttp3.*;

import java.io.File;
import java.io.IOException;

public class DeployClient {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("<resourceServerFolder> <resourceFilePath>");
            return;
        }

        String resourceServerFolder = args[0].replace(File.separator, Constants.FILE_SEPARATOR);
        String resourceFilePath = args[1].replace(File.separator, Constants.FILE_SEPARATOR);

        String fileName = resourceFilePath.substring(resourceFilePath.lastIndexOf(Constants.FILE_SEPARATOR) + 1);
        String newFilePath = resourceServerFolder + Constants.FILE_SEPARATOR + fileName;

        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"),
                new File(resourceFilePath));
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(
                Constants.TOKEN_PARAMETER_NAME, Constants.TOKEN_PARAMETER_VALUE).addFormDataPart(
                Constants.PATH_PARAMETER_NAME, newFilePath).addFormDataPart(Constants.FILE_PARAMETER_NAME, fileName,
                fileBody).build();

        Request request = new Request.Builder().url(URL_API_PATH).put(requestBody).build();
        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(request).execute();
        System.out.println(response.code());;
    }

    private static final String URL_API_PATH = "http://localhost:8080/deploy/files";

}
