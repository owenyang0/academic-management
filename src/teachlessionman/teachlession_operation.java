package teachlessionman;

import java.sql.*;

import util.stringUtil;
import db.dbconn;
	
/**
*封装对学生表的所有操作
*/
public class teachlession_operation{
	/**
	 * 功能：往教师授课表增加一条记录
	 * 输入参数： teacher_id 为教师ID号， lession_id 为课程ID号
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回3 表此条授课信息已存在，返回4 表输入参数输入参数不正确，为0
	 */
	public int teachlession_add_one(int teacher_id,int lession_id){
		dbconn dbconnObject=new dbconn();
		String sqlString=null;
		ResultSet rs=null;
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		
		//输入参数不正确
		if(teacher_id==0||lession_id==0)
			return 4;
		
		//插入记录，先判断是否已存在
		sqlString="select * from teachlession where teacher_id=? and lession_id=?";
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setInt(1, teacher_id);
			preSqlSelect.setInt(2, lession_id);
			rs=preSqlSelect.executeQuery();
			if(rs.next()) return 3;		//已存在
			
			sqlString="insert into teachlession(teacher_id,lession_id) values(?,?)";
			PreparedStatement preSqlInsert=dbconn.prepareStatement(sqlString);
			preSqlInsert.setInt(1, teacher_id);
			preSqlInsert.setInt(2, lession_id);
			preSqlInsert.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
	/**
	 * 功能：根据条件查询出教师的信息
	 * 输入参数：teacher_id 为教师ID号， lession_id 为课程ID号
	 * 输出：查询出的教师授课记录，如果没有记录或操作失败返回null
	 */
	public ResultSet teacher_select_part(int teacher_id,int lession_id){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			//查询出数据
			sqlString="select distinct teachlession.teacher_id as teacher_id,"+
				"teacher.teacher_name as teacher_name,"+
				"lession.lession_id as lession_id,"+
				"lession.lession_name as lession_name,"+
				"teachlession.teachlession_id as teachlession_id"+
				" from teacher,lession,teachlession"+
				" where teachlession.teacher_id=teacher.teacher_id"+
				" and teachlession.lession_id=lession.lession_id";
			if(teacher_id!=0){
				sqlString+=" and teachlession.teacher_id="+teacher_id+" and "+
					"teacher.teacher_id="+teacher_id;
			}
			if(lession_id!=0){
				sqlString+=" and teachlession.teachlession_id="+lession_id+" and "+
					"teachlession.teachlession_id="+lession_id;
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
	public static void main(String[] args){
		teachlession_operation op=new teachlession_operation();
		ResultSet rs=op.teacher_select_part(0, 0);
		try {
			while(rs.next()){
				System.out.println(rs.getString(1));
				System.out.println(rs.getString(2));
				System.out.println(rs.getString(3));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除教师授课信息
	 * 输入参数：授课ID号
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败
	 * 返回4 表输入参数teachlession_id为0，即输入参数不正确 
	 */
	public int teachlession_delete(int teachlession_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(teachlession_id==0) return 4;
		//删除数据
		sqlString="delete from teachlession where teachlession_id=?";
		try{
			PreparedStatement preSqlDelete=dbconn.prepareStatement(sqlString);
			preSqlDelete.setInt(1, teachlession_id);
			preSqlDelete.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
	
	/**
	 * 查询出某一教师的信息
	 * 输入参数：教师的ID
	 * 输出：此教师记录，如果没有记录或操作失败返回null
	 */
	public ResultSet teacher_select_one(int teacher_id){
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null||teacher_id==0) return null;
		try{
			sqlString="select* from teachlession where teacher_id=?";
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setInt(1, teacher_id);
			rs=preSqlSelect.executeQuery();
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	
	/**
	 * 功能：更新教师授课表记录
	 * 输入参数：teachlession_id 老师授课序号，teacher_id 教师ID号, lession_id 为课程ID号
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回3 表记录已存在， 返回4 表输入参数为空或不正确
	 */
	public int teachlession_update(int teachlession_id,int teacher_id,int lession_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		ResultSet rs=null;
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(teachlession_id==0||teacher_id==0||lession_id==0)
			return 4;	//输入参数不正确
		
		//更新记录，先判断是否存在 
		sqlString="select * from teachlession where teacher_id=? and lession_id="+
		lession_id;
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setInt(1, teacher_id);
			rs=preSqlSelect.executeQuery();
			if(rs.next()) return 3;
			
			//更新记录
			sqlString="update teachlession set teacher_id=?,lession_id=? where teachlession_id=?";
			PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlString);
			preSqlUpdate.setInt(1, teacher_id);
			preSqlUpdate.setInt(2, lession_id);
			preSqlUpdate.setInt(3, teachlession_id);
			preSqlUpdate.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
}