package lessionman;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;

import db.dbconn;

public class classlession_operation{
	/**
	 * 功能：得到所有录入了成绩的班级与课程数据
	 * 输入：class_id 为班级ID号,lession_id为课程ID号
	 * 输出：有成绩的班级与课程数据记录集
	 */
	public ResultSet getCoreClassLessionRs(int class_id,int lession_id){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		sqlString="select distinct score.lession_id as lession_id,"+
			"class.class_name as class_name,student.class_id as class_id,"+
			"lession.lession_name from score,student,lession,class"+
			" where score.student_id=student.student_id and"+
			" student.class_id=class.class_id and lession.lession_id=score.lession_id";
		if(class_id!=0){
			sqlString+=" and student.class_id="+class_id+" and score.lession_id="+lession_id;
		}
		if(lession_id!=0){
			sqlString+=" and lession.lession_id="+lession_id+" and score.lession_id="+lession_id;
		}
		try{
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
		}catch(Exception e){
			System.out.print(e);
		}
		return rs;
	}
	
	/**
	 * 功能：得到班级的某课程的封存状态
	 * 输入：clas_id为班级ID号，lession_id为课程ID号
	 * 输出：封存状态字符串
	 * 提示：只在班级的所有学生的成绩都得到封存才算是成功封存状态
	 */
	public String getClassLessionCloseStatus(int class_id,int lession_id){
		String returnString=new String("");
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return returnString;
		if(class_id==0||lession_id==0)
			return returnString;
		sqlString="select distinct score.lession_id as lession_id,"+
			"student.class_id as class_id,"+
			"score.close_status as close_status from score,student "+
			"where score.student_id=student.student_id and student.class_id="+class_id+
			" and score.lession_id="+lession_id;
		try{
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
			rs.last();
			int rowCount=rs.getRow();
			rs.beforeFirst();
			switch(rowCount){
			case 0:
				returnString="没有此班级此课程的成绩数据";
				break;
			case 1:
				rs.next();
				switch(rs.getInt("close_status")){
				case 0:
					returnString="未封存";
					break;
				case 1:
					returnString="已封存";
					break;
				case 2:
					returnString="部分封存";
					break;
				default:
					returnString="未知";
					break;						
				}
				break;
			case 2:
				returnString="部分封存";
				break;
			default:
				returnString="未知";
				break;
			}
		}catch(Exception e){
			System.out.println(e);
			returnString="未知";
		}
		return returnString;
	}
	
	/**
	 * 功能：置某班某课程的状态为封存
	 * 输入：class_id 为班级ID号，lession_id为课程ID号
	 * 输出：返回1 表成功，返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回4 表输入参数不正确
	 */
	public int classLessionCloseSave(int class_id,int lession_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(class_id==0||lession_id==0)
			return 4;
		//更新状态为封存
		sqlString="update score set close_status=1"+
			" where lession_id=? and student_id in"+
			" (select student_id from student where class_id=?)";
		try{
			PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlString);
			preSqlUpdate.setInt(1, lession_id);
			preSqlUpdate.setInt(2, class_id);
			preSqlUpdate.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.println(e);
			return 2;
		}
	}
	
	/**
	 * 功能：置某班的某课程的状态为解封
	 * 输入：class_id 为班级ID事情，、lession_id为课程ID号
	 * 输出：返回1 表成功，返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回4 表输入参数不正确
	 */
	public int classLessionOpenSave(int class_id,int lession_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(class_id==0||lession_id==0) return 4;
		//更新状态为解封
		sqlString="update score set close_status=0"+
			" where lession_id=? and student_id in"+
			" (select student_id from student where class_id=?)";
		try{
			PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlString);
			preSqlUpdate.setInt(1, lession_id);
			preSqlUpdate.setInt(2, class_id);
			preSqlUpdate.executeUpdate();
			return 1;			
		}catch(Exception e){
			System.out.println(e);
			return 2;
		}
		
	}
}