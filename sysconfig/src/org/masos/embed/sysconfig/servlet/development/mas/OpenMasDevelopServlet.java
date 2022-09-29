package org.masos.embed.sysconfig.servlet.development.mas;

import org.masos.embed.sysconfig.controller.MasFilesReaderController;
import org.masos.embed.sysconfig.controller.MasStructureController;
import org.masos.embed.sysconfig.model.Mas;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.dto.MasDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/openMasDevelopServlet")
public class OpenMasDevelopServlet  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        Mas mas = MasFilesReaderController.getMas();

        MasStructureController masStructureController = new MasStructureController(mas.getName(), mas.getAgents());

        Response.build(response).json().ok(new MasDTO(mas.getName(), mas.getAgents(),
                masStructureController.getStructure(), masStructureController.getCompleteStructure()));

    }
}
