package teacherman;

import java.sql.*;

import teachlessionman.teachlession_operation;
import util.stringUtil;
import db.dbconn;
	
/**
*封装对教师表的所有操作
*/
public class teacher_operation{
	
	public ResultSet teacher_select_by_name(String teacher_name){
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			if(teacher_name==null)
				teacher_name="";
			sqlString="select * from teacher where teacher_name like '%"+teacher_name+"%'";
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			if(teacher_name==null)
				teacher_name="";
			rs=preSqlSelect.executeQuery();
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	public static void main(String[] args){
		teacher_operation op=new teacher_operation();
		ResultSet rs=op.teacher_select_by_name("t");
		try {
			while(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能：往学生表增加一条记录
	 * 输入参数： student_name为学生姓名
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回3 表此班级已存在，返回4 表输入参数class_name为空
	 */
	public int student_add_one(String student_name,int class_id){
		dbconn dbconnObject=new dbconn();
		String sqlString=null;
		ResultSet rs=null;
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		
		//输入参数不正确
		if(student_name==null||student_name.trim().length()==0||class_id==0)
			return 4;
		
		//输入参数转换
		stringUtil stringCode=new stringUtil();
		student_name=stringCode.codeToString(student_name.trim());
		//插入记录，先判断是否已存在
		sqlString="select * from student where student_name=? and class_id=?";
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setString(1, student_name);
			preSqlSelect.setInt(2, class_id);
			rs=preSqlSelect.executeQuery();
			if(rs.next()) return 3;		//已存在
			sqlString="insert into student(student_name,class_id) values(?,?)";
			PreparedStatement preSqlInsert=dbconn.prepareStatement(sqlString);
			preSqlInsert.setString(1, student_name);
			preSqlInsert.setInt(2, class_id);
			preSqlInsert.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
	
	/**
	 * 功能：查询出所有学生信息
	 * 输入参数：无
	 * 输出：所有学生的记录集，如果没有记录或操作失败返回null
	 */
	public ResultSet student_select_all(){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			//查询出数据
			sqlString="select distinct student.student_id as student_id,"+
				"student.student_name as student_name,"+
				"student.class_id as class_id,"+
				"class.class_name as class_name from student,class "+
				"where class.class_id=student.class_id";
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	/**
	 * 功能：根据条件查询出学生的信息
	 * 输入参数：class_id为班级ID号， student_name为学生姓名(支持模糊查询)
	 * 输出：查询出的学生的记录，如果没有记录或操作失败返回null
	 */
	public ResultSet student_select_part(int class_id,String student_name){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			//查询出数据
			sqlString="select distinct student.student_id as student_id,"+
				"student.student_name as student_name,"+
				"student.class_id as class_id,"+
				"class.class_name as class_name from student,class "+
				"where class.class_id=student.class_id";
			if(class_id!=0){
				sqlString+=" and class.class_id="+class_id+" and "+
					"student.class_id="+class_id;
			}
			if(student_name!=null&&student_name.trim().length()!=0){
				sqlString+=" and student.student_name like '%"+
					student_name+"%'";
			}
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	
	/**
	 * 删除一个学生信息
	 * 输入参数：学生ID号
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败
	 * 返回4 表输入参数student_id为0，即输入参数不正确 
	 */
	public int student_delete(int student_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(student_id==0) return 4;
		//删除数据
		sqlString="delete from student where student_id=?";
		try{
			PreparedStatement preSqlDelete=dbconn.prepareStatement(sqlString);
			preSqlDelete.setInt(1, student_id);
			preSqlDelete.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
	
	/**
	 * 查询出某一学生的信息
	 * 输入参数：学生的ID
	 * 输出：此学生记录，如果没有记录或操作失败返回null
	 */
	public ResultSet student_select_one(int student_id){
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null||student_id==0) return null;
		try{
			sqlString="select* from student where student_id=?";
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setInt(1, student_id);
			rs=preSqlSelect.executeQuery();
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	
	/**
	 * 功能：更新学生表的一条记录
	 * 输入参数：student_id为学生序号，student_name为学生姓名,class_id为所属班级ID号
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回3 表记录已存在， 返回4 表输入参数为空或不正确
	 */
	public int student_update(int student_id,String student_name,int class_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		ResultSet rs=null;
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(student_id==0||class_id==0||student_name.trim().length()==0)
			return 4;	//输入参数不正确
		
		stringUtil stringCode=new stringUtil();
		student_name=stringCode.codeToString(student_name.trim());
		
		//更新记录，先判断是否存在 
		sqlString="select * from student where student_name=? and class_id=? and student_id<>"+
			student_id;
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setString(1, student_name);
			preSqlSelect.setInt(2, class_id);
			rs=preSqlSelect.executeQuery();
			if(rs.next()) return 3;
			
			//更新记录
			sqlString="update student set student_name=?,class_id=? where student_id=?";
			PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlString);
			preSqlUpdate.setString(1, student_name);
			preSqlUpdate.setInt(2, class_id);
			preSqlUpdate.setInt(3, student_id);
			preSqlUpdate.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
}