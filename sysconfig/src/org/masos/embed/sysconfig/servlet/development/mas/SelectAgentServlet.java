package org.masos.embed.sysconfig.servlet.development.mas;

import org.masos.embedded.chonIDE.model.Agent;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class HomePage
 */
@WebServlet("/SelectAgentServlet")
public class SelectAgentServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SelectAgentServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("smadevelopment.jsp");
		
		String agentSelected = request.getParameter("agentSelected");
		
		List<Agent> agentList = (List<Agent>) request.getSession().getAttribute("masAgentList");
		Agent agent = agentList.stream().filter(agent1 -> agent1.getName().equals(agentSelected)).findFirst().orElse(null);
		if(agent != null) {
			request.getSession().setAttribute("agentSelected", agent);
		}
		
		
		dispatcher.forward(request, response);
		
	}

}
