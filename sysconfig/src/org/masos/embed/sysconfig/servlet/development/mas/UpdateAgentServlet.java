package org.masos.embed.sysconfig.servlet.development.mas;

import com.google.gson.Gson;
import org.masos.embed.sysconfig.controller.MasStructureController;
import org.masos.embed.sysconfig.model.Agent;
import org.masos.embed.sysconfig.model.AgentArchClassEnum;
import org.masos.embed.sysconfig.model.Response;
import org.masos.embed.sysconfig.model.dto.MasDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/updateAgentServlet")
public class UpdateAgentServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    
    @Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) {
		MasDTO masDTO = new Gson().fromJson(request.getParameter("mas"), MasDTO.class);

		String agentName = request.getParameter("agentName");
		String agentNewName = request.getParameter("agentNewName");
		String agentArchClass = request.getParameter("agentArchClass");

		boolean hasToUpdateName = false;
		if (agentNewName != null && !agentNewName.isEmpty() && !agentNewName.equals(agentName)) {
			hasToUpdateName = true;
			agentNewName = agentNewName.substring(0, 1).toLowerCase() + agentNewName.substring(1);
		}

		boolean hasToUpdateAgentArchClass = agentArchClass != null && !agentArchClass.isEmpty()
				&& AgentArchClassEnum.has(agentArchClass);

		if (hasToUpdateName || hasToUpdateAgentArchClass) {
			for (Agent agent : masDTO.getAgents()) {
				if (agent.getName().equals(agentName)) {
					if (hasToUpdateName) {
						agent.setName(agentNewName);
					}
					if (hasToUpdateAgentArchClass && !agent.getArchClass().equals(agentArchClass)) {
						agent.setArchClass(agentArchClass);
					}
					break;
				}
			}

			MasStructureController masStructureController = new MasStructureController(masDTO.getMasName(), masDTO.getAgents());

			masDTO.setMasStructure(masStructureController.getStructure());
			masDTO.setMasCompleteStructure(masStructureController.getCompleteStructure());

		}
		Response.build(response).json().ok(masDTO);

	}

}
