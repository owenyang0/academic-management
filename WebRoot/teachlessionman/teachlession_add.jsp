<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.sql.ResultSet,util.commonTag" %>
<html>

<!--接收输入参数-->
<%
	//接收 class_id 和  student_name 参数
	int teacher_id=0;
	int lession_id=0;
	try{
		teacher_id=Integer.parseInt(request.getParameter("teacher_id"));
		lession_id=Integer.parseInt(request.getParameter("lession_id"));
	}catch(Exception e){}
%>

<!--系统交互JavaScript-->
<script language="javaScript">
<!--
	function chec_data(){
		//单击提交是，检查输入数据
		if(add_teachlession_form.teacher_id.value==0){
			alert("请选择授课教师！");
			return false;
		}
		if(add_teachlession_form.lession_id.value==0){
			alert("请选择所授课程！");
			return false;
		}
		return true;
	}
-->
</script>
<body bgcolor="#DCDADA">
<!--导航菜单-->
<table border="0" width="100%">
	<tr>
		<td width="100%">
			<p align="left">您当前所在位置：教师授课信息管理-->录入老师授课信息</p>
		</td>
	</tr>
	<jsp:include page="navigator.html"/>
</table>
<!-- 数据输入 -->
<form name="add_teachlession_form" action="teachlession_add_insert.jsp" method="post"
	onsubmit="return check_data()">
	<table border="1" width="100%" cellspacing="0" cellpadding="0"
		bordercolor="#808080">
	<tr>
		<td width="100%">
			<table border="0" width="100%">
				<tr>
					<td width="100%" colspan="5">录入教师授课信息：<br><hr></td>
				</tr>
				<tr>
					<td width="24%" align="right">授课教师：</td>
					<td width="19%">
					<%	//生成教师所属下拉框
						commonTag teachertag=new commonTag();
						out.println(teachertag.getTeacherTag(teacher_id));
					%>
					</td>
					<td width="11%" align="right">所授课程：</td>
					<td width="21%">
					<%
						//生成课程所属下拉框
						commonTag lessiontag=new commonTag();
						out.println(lessiontag.getLessionTag(lession_id));
					%>
					</td>
					<td width="25%">
						<input type="submit" value="添加"/>
					</td>
				</tr>
			</table>
		</td>
	</tr>
	</table>
</form>
<br>
</body>
</html>