package org.masos.embed.SysConf.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.masos.embed.SysConf.controller.Connection;
import org.masos.embed.SysConf.controller.SSHaux;
import org.masos.embed.SysConf.model.User;

/**
 * Servlet implementation class HomePage
 */
@WebServlet("/HomePage")
public class HomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomePage() {
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
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("central.jsp"); 						/*default*/
		
		User userSession = new User();
		userSession = (User) request.getSession().getAttribute("userSession");
		
		SSHaux serverSession = new SSHaux();
		serverSession = (SSHaux) request.getSession().getAttribute("serverSession");

		String action = request.getParameter("action");
		
		if(action.equals("logout")) {
			userSession.logout();																			/*Apagando as credencias do usuário*/
			request.getSession().setAttribute("userSession",userSession);									/*Atualizando dados do usuário na sessão*/
			
			request.setAttribute("resposta","Sessão encerrada com sucesso");								
			dispatcher = request.getRequestDispatcher("home.jsp");
		}
		else if(action.equals("commander")) {
			dispatcher = request.getRequestDispatcher("commander.jsp");
		}
		else if(action.equals("wifi")) {
			dispatcher = request.getRequestDispatcher("wifi.jsp");
		}
		else if(action.equals("arduino")) {
			dispatcher = request.getRequestDispatcher("arduino.jsp");
		}else if(action.equals("embeddedmas")) {
			dispatcher = request.getRequestDispatcher("embeddedmas.jsp");
		}
		else if(action.equals("reboot") || (action.equals("poweroff"))) {
			serverSession.connect(userSession.getUsername(), userSession.getPassword());					/*Conectando via SSH*/
			
			if(action.equals("reboot"))
				serverSession.exec("reboot");															/*Enviando comando para reiniciar*/				

			if(action.equals("poweroff"))
				serverSession.exec("poweroff");														/*Enviando comando para desligar*/
			
			serverSession.disconnect();																		/*Desconectando*/

			userSession.logout();																			/*Apagando as credencias do usuário*/
			
			request.setAttribute("resposta", action+" em execução...");
			dispatcher = request.getRequestDispatcher("home.jsp");
		}
		dispatcher.forward(request, response);
		
	}

}
