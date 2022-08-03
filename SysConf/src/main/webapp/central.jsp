<%@page import="org.masos.embed.SysConf.controller.SSHaux"%>
<%@page import="org.masos.embed.SysConf.model.User"%>
<%@ page session= "true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ChonOS Config</title>
</head>
<body>
<%
		User userSession = new User();
		userSession = (User) request.getSession().getAttribute("userSession");
		
		 if((userSession==null) || (!userSession.getSession().equals(session.getId()))){
			 //System.out.println("[central.jsp] Autenticação Inválida");
				RequestDispatcher dispatcher;
				request.setAttribute("resposta","Usuário não autenticado");
				dispatcher = request.getRequestDispatcher("home.jsp");
				dispatcher.forward(request, response);
			 
		}else{
			//System.out.println("[central.jsp] Autenticado");
		}
%>
<nav>
		<p>ChonOS Config</p>
		<hr>
		<a href="central.jsp"><button>Principal</button></a>
		<!--  
		<a href="commander.jsp"><button>Commander</button></a>
		<a href="wifiscan.jsp"><button>Wifi</button></a>
		-->
	<hr>
	</nav>
	<!-- ############################## PARTE SUPERIOR ######################## -->
	
	
	<div>
		<form action="HomePage" method="post">
			<input type="hidden" id="action" name="action" value="wifi">
			<input type="submit" value="WIFI">
		</form>
		
		<form action="HomePage" method="post">
			<input type="hidden" id="action" name="action" value="arduino">
			<input type="submit" value="Firmware">
		</form>
		
		<form action="HomePage" method="post">
			<input type="hidden" id="action" name="action" value="embeddedmas">
			<input type="submit" value="Reasoning">
		</form>
		
		<form action="HomePage" method="post">
			<input type="hidden" id="action" name="action" value="logout">
			<input type="submit" value="SAIR">
		</form>
		
		<form action="HomePage" method="post">
			<input type="hidden" id="action" name="action" value="reboot">
			<input type="submit" value="Reiniciar">
		</form>
		
		<form action="HomePage" method="post">
			<input type="hidden" id="action" name="action" value="poweroff">
			<input type="submit" value="Desligar">
		</form>
		
	</div>
	<!-- ############################## PARTE INFERIOR ######################## -->
	<div>
		<%
		//Barra de mensagem
		String resposta = (String) request.getAttribute("resposta");
		if (resposta != null){
			out.print("<br>");
			out.print(resposta);
		}
		%>
		<hr>
		<p>ChonOS Config</p>
		<hr>
	</div>
</body>
</html>