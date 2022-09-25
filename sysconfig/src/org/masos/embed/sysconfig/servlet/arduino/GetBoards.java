package org.masos.embed.sysconfig.servlet.arduino;

import org.masos.embed.sysconfig.model.SSHConnection;
import org.masos.embed.sysconfig.model.User;
import org.masos.embed.sysconfig.script.FirmwareScriptManager;
import org.masos.embed.sysconfig.model.Response;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/boards")
public class GetBoards extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Response.build(resp).json().ok(
                    SSHConnection.getDefault(user).execute(FirmwareScriptManager.ARDUINO_LIST_BOARDS));
        }
    }
}
