package org.masos.embed.sysconfig.servlet.development.mas;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.controller.MasStructureController;
import org.masos.embed.sysconfig.model.Agent;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.dto.MasDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/deleteAgentServlet")
public class DeleteAgentServlet extends HttpServlet {

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) {
		MasDTO masDTO = new Gson().fromJson(request.getParameter("mas"), MasDTO.class);
		String agentName = request.getParameter("agentName");

		List<Agent> agentList = new ArrayList<>();
		for (Agent agent : masDTO.getAgents()) {
			if (!agent.getName().equals(agentName)) {
				agentList.add(agent);
			}
		}
		masDTO.setAgents(agentList);

		MasStructureController masStructureController = new MasStructureController(masDTO.getMasName(), masDTO.getAgents());

		masDTO.setMasStructure(masStructureController.getStructure());
		masDTO.setMasCompleteStructure(masStructureController.getCompleteStructure());

		Response.build(response).json().ok(masDTO);
	}
	
}
