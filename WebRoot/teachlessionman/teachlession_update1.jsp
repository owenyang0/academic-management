<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.sql.ResultSet,util.commonTag" %>
<html>
<!--系统交互JavaScript-->
<script language="javascript">
<!--
	function check_data(){
		//检查是否为空
		if(update_teacher_form.teacher_id.value==0){
			alert("请选择授课教师！");
			return false;
		}
		if(update_teacher_form.lession_id==0){
		 	alert("请选择所授课程！");
		 	return false;
		}
		return true;
	}
-->
</script>
<%	
	//接收 student_id,class_id 参数
	int teacher_id=0;
	int lession_id=0;
	try{
		teacher_id=Integer.parseInt(request.getParameter("teacher_id"));
		lession_id=Integer.parseInt(request.getParameter("lession_id"));
	}catch(Exception e){}
%>
<!--声明JavaBean，并查询出数据-->
<jsp:useBean id="teacher_select" class="teachlessionman.teachlession_operation" scope="page" />
<%
	ResultSet rs=teacher_select.teacher_select_one(teacher_id);
%>
<body bgcolor="#DCDADA">
<!--导航菜单-->
<jsp:include page="navigator.html"/>
<%if(rs.next()){%>
<!--数据输入-->
<form name="update_teacher_form" action="teachlession_update2.jsp" method="post" 
	onsubmit="return check_data()">
	<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080">
		<tr>
			<td width="100%">
				<table border="0" width="103%">
					<tr>
						<td width="100%" colspan="5">修改教师授课信息:<br><hr></td>
					</tr>
					<tr>
						<td width="30%" align="right">教师授课序号：<%=rs.getLong("teachlession_id")%>
							&nbsp;&nbsp;授课教师：</td>
						<input type="hidden" value="<%=rs.getLong("teachlession_id")%>" name="teachlession_id"/>
						<td width="20%">
						<%
							//生成教师下拉框
							commonTag teachertag=new commonTag();
							out.print(teachertag.getTeacherTag(teacher_id));
						%>
						</td>
						<td width="10%" align="right">所授课程：</td>
						<td width="20%">
						<%
							//生成课程下拉框
							commonTag lessiontag=new commonTag();
							out.print(lessiontag.getLessionTag(lession_id));
						%>
						</td>
						<td width="20%"><input type="submit" value="提交"/></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form>
<%}%>
</body>
</html>