package group.chon.ide.news.domain.resourceaccess.http.client;

import group.chon.ide.news.domain.resourceaccess.http.HttpBody;
import group.chon.ide.news.domain.resourceaccess.http.RequestInfo;
import group.chon.ide.news.domain.resourceaccess.http.ResponseInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.stream.Collectors;

public class StandardHttpClient implements HttpClient {

    protected StandardHttpClient() {

    }

    @Override
    public ResponseInfo request(RequestInfo request) {
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
            return new ResponseInfo(400, null);
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) requestURl.openConnection();
        } catch (IOException e) {
            return new ResponseInfo(400, null);
        }

        try {
            con.setRequestMethod(request.getMethod().toString());
        } catch (ProtocolException e) {
            return new ResponseInfo(400, null);
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

            return new ResponseInfo(200, new HttpBody(response.toString(), con.getHeaderField("Content-Type")));
        } catch (IOException e) {
            return new ResponseInfo(503, null);
        }
    }
}
