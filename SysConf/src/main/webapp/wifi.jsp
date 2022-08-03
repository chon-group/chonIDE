<%@page import="org.masos.embed.SysConf.model.User"%>
<%@ page session= "true" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
table, th, td {
  border: 1px solid black;
}
td {
  text-align: center;
}
</style>
<title>ChonOS Config</title>
</head>
<body>

<%
User userSession = new User();
userSession = (User) request.getSession().getAttribute("userSession");

 if((userSession==null) || (!userSession.getSession().equals(session.getId()))){
	// System.out.println("[wifiscan.jsp] Autenticação Inválida");
		RequestDispatcher dispatcher;
		request.setAttribute("resposta","Usuário não autenticado");
		dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	 
}else{
	//System.out.println("[wifiscan.jsp] Autenticado");
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
	

	  <form action="Wifi" method="post">
	  			<input type="hidden"	id="action" 		name="action" 		value="ddnsConf">
	  NAME:	<input type="text" 		id="ddnsDomain" 	name="ddnsDomain"	value="botname" size="15">
	  USER:	<input type="text" 		id="ddnsUser" 	name="ddnsUser"	value="ddns_user" size="15">
	  TOKEN:		<input type="password"	id="ddnsToken" 	name="ddnsToken"	value="ddns_token" size="15">
	  <input type="submit" value="Configurar DDNS">
	  </form>
<br>

	<form action="Wifi" method="post">
	  <input type="hidden" id="action" name="action" value="status">
	  <input type="submit" value="Status">
	 </form>
	 <br>
	 <form action="Wifi" method="post">
	 <input type="hidden" id="action" name="action" value="forget">
	 <input type="submit" value="Esquecer Redes">
	 </form>
	 <form action="Wifi" method="post">
	  <input type="hidden" id="action" name="action" value="listconf">
	  <input type="submit" value="Listar Redes Cadastradas">
	  </form>
	 <br>
	 <form action="Wifi" method="post">
	  <input type="hidden" id="action" name="action" value="tryreconnect">
	  <input type="submit" value="Tentar Reconectar">
	  </form>
	 <br>
	<form action="Wifi" method="post">
	  <input type="hidden" id="action" name="action" value="scan">
	  <input type="submit" value="SCAN">
	 </form>
	 <br>
	 <form action="Wifi" method="post">
	  <input type="hidden" id="action" name="action" value="apmode">
	  ESSID:	
	  <input type="text" 		id="apESSID" 	name="apESSID"	size="15">
	  KEY (8 caracteres obrigatório):		
	  <input type="password"	id="apKEY" 	name="apKEY"	size="15">
	  <input type="submit" value="APMode">
	  </form>
	  
	  <br>
	  
	  
	  

	  
	  <form action="Wifi" method="post">
	  			<input type="hidden"	id="action" 		name="action" 		value="clientmode">
	  ESSID:	<input type="text" 		id="connectESSID" 	name="connectESSID"	size="15">
	  KEY:		<input type="password"	id="connectKEY" 	name="connectKEY"	size="15">
	  <input type="submit" value="Ingressar em WLAN">
	  </form>
	  

	 
	  
	 

	
	
	
	

<!-- ############################## PARTE INFERIOR ######################## -->
	<div>
		<%
		//Barra de mensagem
		String resposta = (String) request.getAttribute("resposta");
		if (resposta != null){
			out.println("<br>");
			out.println(resposta);
		}
		%>
		<hr>
		<p>ChonOS Config</p>
		<hr>
	</div>

</body>
</html>