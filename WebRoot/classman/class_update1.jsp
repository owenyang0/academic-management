<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.sql.ResultSet" %>
<html>
<!--系统交互javaScript-->
<script language="JavaScript">
<!--
	function check_data(){
		if(update_class_form.class_name.value.length==0){
			alert("输入的班级名称为空，请重新输入！");
			return false;
		}else return true;
	}
-->
</script>
<%
//接收class_id参数
int class_id=0;
try{
	class_id=Integer.parseInt(request.getParameter("class_id"));
}catch(Exception e){}
%>
<!--声明javaBean，并查询出数据-->
<jsp:useBean id="class_select" class="classman.class_operation" scope="page"/>
<%
	ResultSet rs=class_select.class_select_one(class_id);
%>
<body bgcolor="#DCDADA">
<!--导航菜单-->
<jsp:include page="navigator.html"/>
<% if(rs.next()){%>
<!--数据输入-->
<form name="update_class_form" action="class_update2.jsp" method="post"
	onsubmit="return check_data()">
<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080"
	bordercolorlight="#808080" bordercolordark="#808080">
	<input type="hidden" value="<%=rs.getLong("class_id")%>" name="class_id">
	<tr>
		<td width="100%">
			<table border="0" width="100%">
				<tr>
					<td width="100%" colspan="3">修改班级信息：<br><hr></td>
				<tr>
				<tr>
					<td width="25%" align="right">班级序号：<%=rs.getLong("class_id")%>
						&nbsp;&nbsp;班级名称：</td>					
					<td width="25%"><input type="text" name="class_name" value="<%=rs.getString("class_name") %>"
						maxlength="20"></td>
					<td width="50%"><input type="submit" value="提交"></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
<%} %>
<br>
</body>
</html>
				
					
						