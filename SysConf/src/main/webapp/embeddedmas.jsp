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
	
	
		
	<form action="EmbeddedMAS" method="post" enctype="multipart/form-data">
		Carregar MAS:<br>
		<input type="hidden" id="action" name="action" value="importmas">
		<input type="file" name="file" size="30" />
		<input type="submit" value="Upload" />
	</form>
	
	<br>
	<form action="EmbeddedMAS" method="post">
	  <input type="hidden" id="action" name="action" value="startmas">
	  <input type="submit" value="Iniciar MAS">
	  </form>
	
		<BR>	
		<form action="EmbeddedMAS" method="post">
	  <input type="hidden" id="action" name="action" value="stopmas">
	  <input type="submit" value="Parar MAS">
	  </form>
		<BR>
		
	  


	

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