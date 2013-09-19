<%@page contentType="text/html;charset=utf-8" %>
<%@page import="java.sql.ResultSet,util.commonTag,util.stringUtil" %>
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
	String teacher_name=request.getParameter("teacher_name");
	if(teacher_name!=null&&teacher_name.equals("null"))
		teacher_name=null;
	stringUtil stringCode=new stringUtil();
	teacher_name=stringCode.codeToString(teacher_name);
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
			<p align="left">您当前所在位置：教师授课信息管理-->查询和维护教师授课信息</p>
		</td>
	</tr>
	<jsp:include page="navigator.html"/>
</table>
<!--数据输入-->
<form name="add_teacher_form" action="teachlessionman.jsp" method="post"
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
					<td width="24%" align="right">授课教师：</td>
					<td width="19%">
						<%	//生成教师所属下拉框
							commonTag teachertag=new commonTag();
							out.println(teachertag.getTeacherTag(teacher_id));
						%>
					</td>
					<td width="11%" align="right">课程：</td>
					<td width="21%">
					<%
						//生成课程所属下拉框
						commonTag lessiontag=new commonTag();
						out.println(lessiontag.getLessionTag(lession_id));
					%>
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
<jsp:useBean id="teacher_select" class="teachlessionman.teachlession_operation" scope="page"/>
<%
	//用JavaBean查询出数据，并得到 总记录条数
	ResultSet rs=teacher_select.teacher_select_part(teacher_id,lession_id);
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
所有授课信息(共<%=rowCount %>条)&nbsp;&nbsp;&nbsp;&nbsp;
<%
	String refName[]={"teacher_id","lession_id"};
	String refValue[]={teacher_id+"",lession_id+""};
	out.print(student_rsCutPage.get_cutPage_String("teacherman.jsp",refName,refValue));
%>
</td>
</tr>
<tr>
	<td width="15%" align="center">教师授课信息序号</td>
	<td width="25%" align="center">教师姓名</td>
	<td width="25%" align="center">所教课程</td>
	<td width="20%" align="center">修改？</td>
	<td width="15%" align="center">删除？</td>
</tr>
<%
	for(int i=0;i<student_rsCutPage.getPageSize()&&rs.next();i++){ 
%>
<tr>
	<td align="center"><%=rs.getInt("teacher_id") %></td>
	<td align="center"><%=rs.getString("teacher_name") %></td>
	<td align="center"><%=rs.getString("lession_name") %></td>
	<td align="center">
		<a href='teachlession_update1.jsp?teacher_id=<%=rs.getInt("teacher_id")%>&lession_id=<%=rs.getString("lession_id") %>'>修改</a>
	</td>
	<td align="center">
		<a href="teachlession_delete.jsp?teachlession_id=<%=rs.getLong("teachlession_id") %>"
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
					<td width="100%">注意：删除教师授课信息将删除此教师对应的课程。</td>
				</tr>
			</table>
		</td>
	</tr>
</table>


</body>
</html>