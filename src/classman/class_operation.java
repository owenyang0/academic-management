package classman;

import util.stringUtil;
import db.dbconn;
import java.sql.*;

/**
 * 封装对班级表的所有操作
 */
public class class_operation{
	/**
	 * 功能：往班级表增加一条记录
	 * 输入参数：class_name为班级名称
	 * 输出：返回1 表成功，返回2 表数据连接参数不正确或连接数据库失败或数据库操作失败
	 * 返回3 表此班级已存在，返回4 表输入参数class_name为空
	 * 
	 */
	public int class_add_one(String class_name){
		dbconn dbconnObject = new dbconn();
		String sqlString=null;
		ResultSet rs=null;
		Connection dbconn = dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		//输入参数不正确
		if(class_name==null||class_name.trim().length()==0){
			return 4;
		}
		
		//输入参数编码转换
		stringUtil stringCode = new stringUtil();
		class_name=stringCode.codeToString(class_name.trim());
		
		//插入记录，先判断是否已存在
		sqlString="select * from class where class_name=?";
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setString(1,class_name);
			rs=preSqlSelect.executeQuery();
			if(rs.next()) return 3;	//此班级已存在 
			sqlString="insert into class(class_name) values(?)";
			PreparedStatement preSqlInsert=dbconn.prepareStatement(sqlString);
			preSqlInsert.setString(1,class_name);
			preSqlInsert.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
	/**
	 * 功能：查询出所有的班级
	 * 输入参数：无
	 * 输出：所有班级的记录数，如果没有记录或操作失败返回null
	 */
	public ResultSet class_select_all(){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			sqlString="select * from class";
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	/**
	 * 功能：删除一个班级的信息
	 * 输入参数：班级的ID号
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败
	 * 返回4 表输入参数class_id 为，即输入参数不正确
	 */
	public int class_delete(int class_id){
		String sqlString=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(class_id==0) return 4;
		
		//删除数据
		sqlString="delete from class where class_id=?";
		try{
			PreparedStatement preSqlDelete=dbconn.prepareStatement(sqlString);
			preSqlDelete.setInt(1, class_id);
			preSqlDelete.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
	
	/**
	 * 功能：查询出某一班级名称
	 * 输入参数：班级的ID
	 * 输出：此条班级记录，如果没有记录或操作失败返回null
	 */
	public ResultSet class_select_one(int class_id){
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null||class_id==0) return null;
		try{
			sqlString="select * from class where class_id=?";
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setInt(1, class_id);
			rs=preSqlSelect.executeQuery();
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
		
	}
	
	/**
	 * 功能：更新班级表的一条记录
	 * @param class_id 为班级号
	 * @param class_name 为班级名称
	 * 输出：返回1 表成功， 返回2 表数据连接参数配置不正确或连接数据库失败或数据库操作失败
	 * 返回3 表记录已存在， 返回4 表输入参数为空或不正确
	 */
	public int class_update(int class_id,String class_name){
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return 2;
		if(class_id==0||class_name.trim().length()==0) return 4;
		
		//输入参数编码转换
		stringUtil stringCode=new stringUtil();
		class_name=stringCode.codeToString(class_name.trim());
		//插入记录，先判断是否存在
		sqlString="select * from class where class_name=? and class_id<>"+class_id;
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setString(1, class_name);
			rs=preSqlSelect.executeQuery();
			if(rs.next()) return 3;		//已存在此班级
			
			//更新记录
			sqlString="update class set class_name=? where class_id=?";
			PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlString);
			preSqlUpdate.setString(1, class_name);
			preSqlUpdate.setInt(2, class_id);
			preSqlUpdate.executeUpdate();
			return 1;
		}catch(Exception e){
			System.out.print(e);
			return 2;
		}
	}
}