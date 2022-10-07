package org.masos.embed.sysconfig.servlet.development.mas;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.controller.MasStructureController;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.dto.MasDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/masNameServlet")
public class MasNameServlet extends HttpServlet {

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) {
		MasDTO masDTO = new Gson().fromJson(request.getParameter("mas"), MasDTO.class);
		String masName = request.getParameter("masName");

		masDTO.setMasName(masName);

		MasStructureController masStructureController = new MasStructureController(masDTO.getMasName(), masDTO.getAgents());

		masDTO.setMasStructure(masStructureController.getStructure());
		masDTO.setMasCompleteStructure(masStructureController.getCompleteStructure());

		Response.build(response).json().ok(masDTO);

	}

}
