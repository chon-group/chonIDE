package org.masos.embed.SysConf.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.masos.embed.SysConf.model.*;
import org.masos.embed.SysConf.controller.*;
/**
 * Servlet implementation class AuthUser
 */
@WebServlet("/AuthUser")
public class AuthUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	doGet(request, response);
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp"); /* default */

		String username = null, password = null, session = null, hostname = null;
		Integer port = 0;

		try {
			username = request.getParameter("username");
			if (username.equals("") || username==null) 
				throw new Exception();

			password = request.getParameter("password");
			if (password.equals("") || password==null)
				throw new Exception();

			hostname = request.getParameter("hostname");
			if (hostname.equals("") || hostname==null)
				throw new Exception();

			port = Integer.parseInt(request.getParameter("port"));

			session = request.getParameter("session");
			
			SSHaux serverSession = new SSHaux(hostname, port);
			User userSession = new User(username, password, session);

			if (serverSession.testConnect(username, password)) {
				request.getSession().setAttribute("userSession", userSession);
				request.getSession().setAttribute("serverSession", serverSession);

				request.setAttribute("resposta", "Autenticado com Sucesso!");
				dispatcher = request.getRequestDispatcher("central.jsp");
			} else {
				request.setAttribute("resposta", "Crendenciais inválidas!");
			}		
		} catch (Exception e) {
			request.setAttribute("resposta", "Preenchimento incorreto!");
		}

		dispatcher.forward(request, response);
		
		
	}

}
