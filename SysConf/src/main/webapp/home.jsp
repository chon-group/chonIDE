<%@page import="org.masos.embed.SysConf.model.User"%>
<%@page session="true"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
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
	
	if(userSession==null){
		//System.out.println("[home.jsp] Ninguem autenticado");
	}else if((userSession.getSession() != null) && (userSession.getSession().equals(session.getId()))){
	//	System.out.println("[home.jsp] Autenticado");
		request.setAttribute("resposta", "Autenticado com Sucesso!");
		RequestDispatcher dispatcher;
		dispatcher = request.getRequestDispatcher("central.jsp");
		dispatcher.forward(request, response);
	}else{
		//System.out.println("[home.jsp] Autenticação Inválida");
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
		LOGIN <br>
		<br>
		<form action="AuthUser" method="post">
			<label for="username">Username:</label> 
				<input type="text" id="username" name="username"><br> <br> 
			<label for="password">Password:</label>
				<input type="password" id="password" name="password"><br><br>

				<input type="hidden"id="session" name="session" value="<%out.print(session.getId());%>"><br> 
				
				<input type="submit" value="ENTER"> <br><br>
				
				<label for="hostname">hostname</label>
				<input type="text" id="hostname" name="hostname" size="15" value="localhost">
				
				<label for="port">port</label>
				<input type="text" id="port" name="port" size="8" value="22"><br><br>
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