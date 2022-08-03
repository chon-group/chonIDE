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
	 //System.out.println("[commander.jsp] Autenticação Inválida");
		RequestDispatcher dispatcher;
		request.setAttribute("resposta","Usuário não autenticado");
		dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	 
}else{
	//System.out.println("[commander.jsp] Autenticado");
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
	
	  <form action="Arduino" method="post">
	  <input type="hidden" id="action" name="action" value="listboards">
	  <input type="submit" value="listar Placas">
	  </form>
	
		<BR>	
		<form action="Arduino" method="post">
	  <input type="hidden" id="action" name="action" value="listlibraries">
	  <input type="submit" value="listar Bibliotecas">
	  </form>
		<BR>
		
		
	<form action="Arduino" method="post" enctype="multipart/form-data">
		Carregar Biblioteca:<br>
		<input type="hidden" id="action" name="action" value="importlib">
		<input type="file" name="file" size="30" />
		<input type="submit" value="Upload" />
	</form>

	  


		<br>
		<form action="Arduino" method="post" enctype="multipart/form-data">
		Compilar Firmware: <br>
		<input type="file" name="file" size="30" />	
		<br>
		  Board:
	  <input type="text"   id="boardFQDN" 	name="boardFQDN"	value="arduino:avr:uno" size="15">
	  <input type="hidden" id="action" name="action" value="compile">
	  <input type="submit" value="Compilar">
	  </form>

<br>
	
	<form action="Arduino" method="post">
		Deploy Firmware:<br>
		Board:
	  <input type="text"   id="boardFQDN" 	name="boardFQDN"	value="arduino:avr:uno" size="15">
	  <br>
		Port:
	  <input type="text"   id="boardSerialPort" 	name="boardSerialPort"	value="/dev/ttyACM0" size="15">	  
	  <input type="hidden" id="action" name="action" value="deploy">
	  <input type="submit" value="deploy">
	  </form>

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