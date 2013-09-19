<%@page contentType="text/html;charset=utf-8" %>
<html>
<body bgcolor="#DCDADA">
<%
	//接收class_id参数
	int class_id=0;
	try{
		class_id=Integer.parseInt(request.getParameter("class_id"));
	}catch(Exception e){}
%>
<jsp:useBean id="class_delete" class="classman.class_operation" scope="page" />
<%
	//接收结果提示信息
	int deleteResult=class_delete.class_delete(class_id);
	switch(deleteResult){
		case 1:
			out.print("删除数据库成功！"); break;
		case 2:
			out.print("数据库操作失败！"); break;
		case 4:
			out.print("输入数据非法！"); break;
		default:
			out.print("操作失败！");
	}
%>
<br><a href="class_add.jsp">返回</a>
</body>
</html>