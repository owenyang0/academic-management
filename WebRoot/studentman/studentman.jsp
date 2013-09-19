<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.sql.ResultSet,util.commonTag,util.stringUtil" %>
<html>
<!--接收输入参数-->
<%
	//接收 class_id 和  student_name 参数
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
<!--系统交互 JavaScript-->
<script language="javaScript">
<!--
	function delete_confirm(){
		//单击删除链接时，弹出确认对话框
		if(confirm("确认要删除吗？")){
			return true;
		}else{
			return false;
		}
	}
-->
</script>
<body bgcolor="#DCDADA">
<br><br><!--导航菜单-->
<table border="0" width="100%">
	<tr>
		<td width="100%">
			<p align="left">您当前所在位置：学生信息管理-->查询和维护学生信息</p>
		</td>
	</tr>
	<jsp:include page="navigator.html"/>
</table>
<!--数据输入-->
<form name="add_student_form" action="studentman.jsp" method="post"
	onsubmit="return check_data()">
<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080"
	bordercolorlight="#808080" bordercolordark="#808080">
	<tr>
		<td width="100%">
			<table border="0" width="100%">
				<tr>
					<td width="100%" colspan="5">输入查询条件:<br><hr></td>
				</tr>
				<tr>
					<td width="24%" align="right">学生所属班级：</td>
					<td width="19%">
						<%	//生成学生所属下拉框
							commonTag classtag=new commonTag();
							out.println(classtag.getClassTag(class_id));
						%>
					</td>
					<td width="11%" align="right">学生姓名：</td>
					<td width="21%">
					<input type="text" name="student_name" maxlength="20" 
						value="<%if(student_name!=null) out.print(student_name); %>"/>
					</td>
					<td width="25%"><input type="submit" value="提交"/></td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
<!--数据输出-->
<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080"
	bordercolorlight="#808080" bordercolordark="#808080">
<jsp:useBean id="student_select" class="studentman.student_operation" scope="page"/>
<%
	//用JavaBean查询出数据，并得到 总记录条数
	ResultSet rs=student_select.student_select_part(class_id,student_name);
	rs.last();
	int rowCount=rs.getRow();
	rs.beforeFirst();
%>
<jsp:useBean id="student_rsCutPage" class="util.rsCutPage" scope="page" />
<jsp:setProperty name="student_rsCutPage" property="rs" value="<%=rs %>"/>
<!-- pageSize记录每页记录条数 -->
<jsp:setProperty name="student_rsCutPage" property="pageSize" value="10"/>
<tr>
	<td colspan="5" align="center">
<%// 从请求参数中得到当前页码 
	String currentPage=request.getParameter("currentPage");
	try{
		student_rsCutPage.setCurrentPage(Integer.parseInt(currentPage));
	}catch(Exception e){
		//如果参数不正确，设置当前页为1
		student_rsCutPage.setCurrentPage(1);
	}
%>
所有学生(共<%=rowCount %>位)&nbsp;&nbsp;&nbsp;&nbsp;
<%
	String refName[]={"class_id","student_name"};
	String refValue[]={class_id+"",student_name};
	out.print(student_rsCutPage.get_cutPage_String("studentman.jsp",refName,refValue));
%>
</td>
</tr>
<tr>
	<td width="10%" align="center">学生序号</td>
	<td width="25%" align="center">学生姓名</td>
	<td width="25%" align="center">学生所属班级</td>
	<td width="20%" align="center">修改？</td>
	<td width="20%" align="center">删除？</td>
</tr>
<%
	for(int i=0;i<student_rsCutPage.getPageSize()&&rs.next();i++){ 
%>
<tr>
	<td align="center"><%=rs.getLong("student_id") %></td>
	<td align="center"><%=rs.getString("student_name") %></td>
	<td align="center"><%=rs.getString("class_name") %></td>
	<td align="center">
		<a href='student_update1.jsp?student_id=<%=rs.getLong("student_id") %>&class_id=<%=rs.getLong("class_id") %>'>修改</a>
	</td>
	<td align="center">
		<a href="student_delete.jsp?student_id=<%=rs.getLong("student_id") %>"
			onclick="return delete_confirm()">X</a>
	</td>
</tr>
<%} %>
</table><br>
<!-- 操作提示信息 -->
<table border="1" width="100%" cellspacing="0" cellpadding="0" bordercolor="#808080"
	bordercolorlight="#808080" bordercolordark="#808080">
	<tr>
		<td width="100%">
			<table border="0" width="100%">
				<tr>
					<td width="100%">注意：删除学生信息将删除所有此学生的所有成绩信息。</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


</body>
</html>