package group.chon.ide.news.infra.accessmethods;

import group.chon.ide.news.app.accessmethods.httpclient.HttpClient;
import group.chon.ide.news.app.accessmethods.httpclient.HttpBody;
import group.chon.ide.news.app.accessmethods.httpclient.HttpRequest;
import group.chon.ide.news.app.accessmethods.httpclient.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

public class StandardHttpClient implements HttpClient {

    @Override
    public HttpResponse request(HttpRequest request) {
        String urlText = request.getUrl();

        if (request.getQueryParameters() != null && !request.getQueryParameters().isEmpty()) {
            String queryParameters = request.getQueryParameters().stream()
                                            .map(query -> query.getName() + "=" + query.getValue())
                                            .collect(Collectors.joining("&"));
            urlText += "?" + queryParameters;
        }

        URL requestURl;
        try {
            requestURl = new URL(urlText);
        } catch (MalformedURLException e) {
            return new HttpResponse(400, null);
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) requestURl.openConnection();
        } catch (IOException e) {
            return new HttpResponse(400, null);
        }

        try {
            con.setRequestMethod(request.getMethod().toString());
        } catch (ProtocolException e) {
            return new HttpResponse(400, null);
        }

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            con.disconnect();

            return new HttpResponse(200, new HttpBody(response.toString(), con.getHeaderField("Content-Type")));
        } catch (IOException e) {
            return new HttpResponse(503, null);
        }
    }
}
