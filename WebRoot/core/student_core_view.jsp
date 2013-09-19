<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.sql.ResultSet,util.commonTag,util.stringUtil" %>
<html>
<!--系统交互 JavaScript-->
<script language="JavaScript">
<!--
	function check_data(){
		//检查数据是否为空
		if(core.student_name.value.length==0){
			alert("输入学生姓名为空，请重新输入！");
			return false;
		}
	}
-->
</script>
<!--接收输入参数-->
<%
	//接收 class_id 和 student_name参数
	int class_id=0;
	try{
		class_id=Integer.parseInt(request.getParameter("class_id"));
	}catch(Exception e){}
	String student_name=request.getParameter("student_name");
	if(student_name!=null&&student_name.equals("null"))
		student_name=null;
	stringUtil stringCode=new stringUtil();
	student_name=stringCode.codeToString(student_name);
%>
<body bgcolor="#DCDADA">
<!--导航菜单-->
<table border="0" width="100%">
<tr>
	<td width="100%">
		<p align="left">您当前所在位置：学生成绩查询</p>
	</td>
</tr>
</table>
<!--数据输入-->
<form name="core" action="student_core_view.jsp" method="post" onsubmit="return check_data()">
<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080">
<tr>
	<td width="100%">
		<table border="0" width="100%">
		<tr>
			<td width="100%" colspan="5">输入查询条件：<br><hr></td>
		</tr>
		<tr>
			<td width="24%" align="right">学生所属班级：</td>
			<td width="19%">
			<%//生成学生所属班级的下拉框
				commonTag classtag=new commonTag();
				out.print(classtag.getClassTag(class_id));
			%>
			</td>
			<td width="11%" align="right">学生姓名：</td>
			<td width="21%">
			<input type="text" name="student_name" maxlength="20"
				value="<%if(student_name!=null) out.print(student_name); %>">
			</td>
			<td width="25%"><input type="submit" value="提交"></td>
		</tr>
		</table>
	</td>
</tr>
</table>
</form><br>
<!-- 数据输出 -->
<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080">
<jsp:useBean id="core_select" class="core.core_operation" scope="page" />
<%
	//用JavaBean查询出数据，并得到总记录条数
	ResultSet rs=core_select.student_core_view(class_id,student_name);
	ResultSet rscount=core_select.student_core_rowcount(class_id,student_name);
	int rowCount=0;

%>
<tr>
	<td colspan="3" align="center">各科目成绩</td>
</tr>
<tr>
	<td width="10%" align="center">学生姓名</td>
	<td width="25%" align="center">课程名称</td>
	<td width="25%" align="center">成绩</td>
</tr>
<% 
	int i=0;
	while(rs!=null&&rs.next()){
%>
<tr>
	<%if(i==0){
		if(rscount!=null&&rscount.next()){
		rowCount=rscount.getInt("count");
	} %>
	<td align="center" rowspan="<%=rowCount%>"><%=rs.getString("student_name")%></td>
	<%i=rowCount;}
	i=(--rowCount)==0?0:1; %>
	<td align="center"><%=rs.getString("lession_name") %></td>
	<td align="center"><%=rs.getFloat("score") %></td>
</tr>
<%} %>
</table><br>
</body>
</html>
				