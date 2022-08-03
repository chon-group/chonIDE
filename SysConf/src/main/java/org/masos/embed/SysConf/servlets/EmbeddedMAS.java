package org.masos.embed.SysConf.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.masos.embed.SysConf.controller.ReasoningManager;
import org.masos.embed.SysConf.controller.SSHaux;
import org.masos.embed.SysConf.model.User;

/**
 * Servlet implementation class EmbeddedMAS
 */
@WebServlet("/EmbeddedMAS")
@MultipartConfig 
public class EmbeddedMAS extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmbeddedMAS() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User userSession = new User();
		userSession = (User) request.getSession().getAttribute("userSession");
		
		SSHaux serverSession = new SSHaux();
		serverSession = (SSHaux) request.getSession().getAttribute("serverSession");
		
		ReasoningManager reasoningMng = new ReasoningManager();
		
		
		String action = request.getParameter("action");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("embeddedmas.jsp");
		
		
		if(action.equals("importmas")) {
			Part filePart = request.getPart("file");
			String tmpFile = "/tmp/"+filePart.getSubmittedFileName();
			String destinationFile = "/tmp/mas_"+filePart.getSubmittedFileName();
			filePart.write(tmpFile);
			
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			serverSession.channelSFTPopen();
			serverSession.sendFile(tmpFile, destinationFile);
			serverSession.channelSFTPclose();
			
			request.setAttribute("resposta",serverSession.exec(reasoningMng.getEmbeddedMASImportCMD(destinationFile)));
			serverSession.disconnect();
		}else if(action.equals("startmas")) {
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			request.setAttribute("resposta",serverSession.exec(reasoningMng.getEmbeddedMASStartCMD()));
			serverSession.disconnect();
		}else if(action.equals("stopmas")) {
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			request.setAttribute("resposta",serverSession.exec(reasoningMng.getEmbeddedMASStopCMD()));
			serverSession.disconnect();
		}
		
		dispatcher.forward(request, response);
	}
}