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

import org.masos.embed.SysConf.controller.SSHaux;
import org.masos.embed.SysConf.model.User;
import org.masos.embed.SysConf.controller.FirmwareManager;

/**
 * Servlet implementation class Arduino
 */
@WebServlet("/Arduino")
@MultipartConfig   														// indica que o formulário é multipart

public class Arduino extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Arduino() {
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
		User userSession = new User();
		userSession = (User) request.getSession().getAttribute("userSession");
		
		SSHaux serverSession = new SSHaux();
		serverSession = (SSHaux) request.getSession().getAttribute("serverSession");
		
		FirmwareManager firmMng = new FirmwareManager();
		
		String action = request.getParameter("action");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("arduino.jsp");
		
		if(action.equals("listboards")) {
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			request.setAttribute("resposta",serverSession.exec(firmMng.getArduinoListBoardsCMD()));
			serverSession.disconnect();
		}
		else if(action.equals("listlibraries")) {
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			request.setAttribute("resposta",serverSession.exec(firmMng.getArduinoListLibsCMD()));
			serverSession.disconnect();
		}
		else if(action.equals("importlib")) {
			Part filePart = request.getPart("file");
			String tmpFile = "/tmp/"+filePart.getSubmittedFileName();
			String destinationFile = "/tmp/lib_"+filePart.getSubmittedFileName();
			filePart.write(tmpFile);
			
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			serverSession.channelSFTPopen();
			serverSession.sendFile(tmpFile, destinationFile);
			serverSession.channelSFTPclose();
			
			request.setAttribute("resposta",serverSession.exec(firmMng.getArduinoImportLibCMD(destinationFile)));
			serverSession.disconnect();
		}
		else if (action.equals("compile")) {
			Part filePart = request.getPart("file");
			String tmpFile = "/tmp/"+filePart.getSubmittedFileName();
			String destinationFile = "/tmp/sketch_file.ino";
			filePart.write(tmpFile);
	
			serverSession.connect(userSession.getUsername(), userSession.getPassword());
			serverSession.channelSFTPopen();
			serverSession.sendFile(tmpFile, destinationFile);
			serverSession.channelSFTPclose();
			
			request.setAttribute("resposta",serverSession.exec(
				firmMng.getArduinoCompileSketchCMD(
					destinationFile, 
					request.getParameter("boardFQDN")
				)
			));
			
			serverSession.disconnect();
		}
		else if (action.equals("deploy")) {
			serverSession.connect(userSession.getUsername(), userSession.getPassword());

			request.setAttribute("resposta",serverSession.exec(
				firmMng.getArduinoDeploySketchCMD(
					request.getParameter("boardFQDN"), 
					request.getParameter("boardSerialPort")
				)
			));
			serverSession.disconnect();
			
		}
		dispatcher.forward(request, response);
	}

}
