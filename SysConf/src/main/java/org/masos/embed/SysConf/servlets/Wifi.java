package org.masos.embed.SysConf.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.masos.embed.SysConf.controller.SSHaux;
import org.masos.embed.SysConf.controller.Connection;
import org.masos.embed.SysConf.controller.ShellCommands;
import org.masos.embed.SysConf.model.User;

/**
 * Servlet implementation class Wifi
 */
@WebServlet("/Wifi")
public class Wifi extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Wifi() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
	
		String action = request.getParameter("action");		
		RequestDispatcher dispatcher = request.getRequestDispatcher("wifi.jsp"); 						/*default*/
			
		User userSession = new User();
		userSession = (User) request.getSession().getAttribute("userSession");
		
		SSHaux serverSession = new SSHaux();
		serverSession = (SSHaux) request.getSession().getAttribute("serverSession");

		Connection conn = new Connection(userSession.getUsername(),userSession.getPassword(),
				serverSession.getHostname(),serverSession.getPort());
		
//		ShellCommands cmd = new ShellCommands();
		
		if(action!=null) {
			if(action.equals("scan")){	
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(conn.getWifiScanListCMD()));
				serverSession.disconnect();
			}
			else if(action.equals("status")){
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(conn.getWifiStatusCMD()));
				serverSession.disconnect();
			}
			else if(action.equals("apmode")){
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(
						conn.getWifiAPMode(
								request.getParameter("apESSID"), 
								request.getParameter("apKEY")
								)
						));
				serverSession.disconnect();
				
				userSession.logout();

				request.setAttribute("resposta","Configuração em Andamento. Aguarde!");
				dispatcher = request.getRequestDispatcher("home.jsp");

			}else if(action.equals("forget")) {
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(conn.getWifiForgetCMD()));
				serverSession.disconnect();
				
				request.setAttribute("resposta","Configurações de WLAN apagadas!");
				dispatcher = request.getRequestDispatcher("wifi.jsp");
			}else if(action.equals("tryreconnect")) {
				
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(conn.getWifiTryReconnectCMD()));
				serverSession.disconnect();
			}else if(action.equals("listconf")) {
				
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(conn.getWifiWellKnownListCMD()));
				serverSession.disconnect();
			}
			else if(action.equals("clientmode")) {
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(
						conn.getWifiClientMode(
								request.getParameter("connectESSID"), 
								request.getParameter("connectKEY")
								)
						));
				serverSession.disconnect();

				userSession.logout();

				request.setAttribute("resposta","Configuração em Andamento. Aguarde!");
				dispatcher = request.getRequestDispatcher("home.jsp");
			}
			else if(action.equals("ddnsConf")) {
				serverSession.connect(userSession.getUsername(), userSession.getPassword());
				request.setAttribute("resposta",serverSession.exec(
						conn.getDDNSConfCMD(
								request.getParameter("ddnsDomain"), 
								request.getParameter("ddnsUser"),
								request.getParameter("ddnsToken")
								)
						));
				serverSession.disconnect();
			}
		}
		dispatcher.forward(request, response);
	}

}
