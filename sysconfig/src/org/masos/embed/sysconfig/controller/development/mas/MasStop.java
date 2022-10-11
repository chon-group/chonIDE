package org.masos.embed.sysconfig.controller.development.mas;

import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.executor.Executor;
import org.masos.embed.sysconfig.script.ReasoningScriptManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mas/stop")
public class MasStop extends HttpServlet {

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        Executor executor = (Executor) req.getSession().getAttribute("executor");
        if (executor != null) {
            String response = executor.execute(ReasoningScriptManager.EMBEDDED_MAS_STOP);
            Response.build(resp).text().ok(response);
        }
    }
}
