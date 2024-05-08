package group.chon.ide.api.api.controller.development;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

@WebServlet("/api/sma/agents")
public class MindInspectorApiController extends ApiController {

    /**
     * URL de acesso a API do mindinspector.
     */
    private static final String MINDINSPECTOR_API_URL = "http://localhost:3275/mindinspector/agents";


    @Override
    protected ResponseEntity get(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        int cycle = Optional.ofNullable(parameters.get("cycle")).map(o -> Integer.parseInt(String.valueOf(o))).orElse(0);
        String agent = Optional.ofNullable(parameters.get("agent")).map(String::valueOf).orElse(null);

        URL url;
        try {
            if (agent != null && cycle != 0) {
                url = new URL(MINDINSPECTOR_API_URL + "?cycle=" + cycle + "&agent=" + agent);
            } else {
                url = new URL(MINDINSPECTOR_API_URL);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection con;
        try {
            con = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            con.setRequestMethod("GET");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
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

            return ResponseEntity.get().data(response).status(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            return ResponseEntity.get().status(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }
}
