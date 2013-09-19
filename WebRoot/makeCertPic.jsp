<%@page contentType="image/jpeg;charset=utf-8" %>
<jsp:useBean id="image" scope="page" class="pic.makeCertPic" />
<%
	String str=image.getCertPic(0,0,response.getOutputStream());
	//将验证码存入SESSION
	session.setAttribute("certCode",str);
 %>
