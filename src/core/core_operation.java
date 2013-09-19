package core;

import java.sql.*;

import db.dbconn;

public class core_operation{
	/**
	 * 功能：获得学生该门课程的成绩
	 * 输入参数：student_id,lession_id
	 */
	public float getStudent_lession_core(long student_id,int lession_id){
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null)
			return -1;
		sqlString="select score from score"+
			" where score.student_id=?"+
			" and score.lession_id=?";
		try{			
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setLong(1, student_id);
			preSqlSelect.setInt(2, lession_id);
			rs=preSqlSelect.executeQuery();
			if(rs.next()){
				return rs.getFloat("score");
			}
			return -1;	
		}catch(Exception e){
			System.out.print(e);
			return -1;
		}
	}
	
	/**
	 * 功能：录入学生的成绩
	 */
	public String saveStudent_core(String[] refName,String[] refValue,int lession_id){
		String returnString=new String("成绩未改变");
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null)
			return "数据库读取错误！";
		sqlString="insert into score(lession_id,student_id,score,close_status)"+
			" values(?,?,?,0)";
		String sqlUpdateString="update score set score=?"+
			" where lession_id=? and student_id=?";
		//判断学生对应课程是否存在
		String sqlSelectString="select * from score where lession_id=? and student_id=?";
		try{  
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlSelectString);
			preSqlSelect.setInt(1, lession_id);
			PreparedStatement preSqlInsert=dbconn.prepareStatement(sqlString);
			preSqlInsert.setInt(1, lession_id);
			PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlUpdateString);
			for(int i=0;i<refName.length;i++){
				preSqlSelect.setString(2, refName[i]);
				rs=preSqlSelect.executeQuery();
				System.out.println(sqlSelectString);
				if(rs.next()){//如果存在则更新
					preSqlUpdate.setString(1, refValue[i]);
					preSqlUpdate.setInt(2, lession_id);
					preSqlUpdate.setString(3, refName[i]);
					preSqlUpdate.executeUpdate();
					System.out.println(sqlUpdateString);
				}else{//如果不存在，则插入
					preSqlInsert.setString(2, refName[i]);
					preSqlInsert.setString(3, refValue[i]);	
					preSqlInsert.executeUpdate();
					System.out.println(sqlString);
				}
			}
			returnString= "成绩变更成功！";
			return returnString;
		}catch(Exception e){
			System.out.print(e);
			return "成绩更新错误！";
		}
	}
	
	/**
	 * 功能：查询学生成绩
	 */
	public ResultSet student_core_view(int class_id,String student_name){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			//查询出数据
			sqlString="select distinct student.student_name as student_name,"+
			"lession.lession_name as lession_name,"+
			"score.score as score from student,lession,score"+
			" where score.lession_id=lession.lession_id"+
			" and score.student_id=student.student_id";
			if(class_id!=0){
				sqlString+=" and student.class_id="+class_id;
			}
			if(student_name!=null&&student_name.trim().length()!=0){
				sqlString+=" and student.student_name like '%"+
					student_name+"%'";
			}
			sqlString+=" order by student_name";
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
	 * 功能获取每个学生的课程门数
	 * @param args
	 */
	public ResultSet student_core_rowcount(int class_id,String student_name){
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null) return null;
		try{
			//查询出数据
			sqlString="SELECT count(*) as count FROM (select distinct student.student_name as student_name,"+
			"lession.lession_name as lession_name,"+
			"score.score as score from student,lession,score"+
			" where score.lession_id=lession.lession_id"+
			" and score.student_id=student.student_id";
			if(class_id!=0){
				sqlString+=" and student.class_id="+class_id;
			}
			if(student_name!=null&&student_name.trim().length()!=0){
				sqlString+=" and student.student_name like '%"+
					student_name+"%'";
			}
			sqlString+=" order by student_name)as tem GROUP BY student_name";
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
		core_operation co=new core_operation();
		ResultSet rs=co.student_core_view(0,"");
		int rowCount=0;
		if(rs!=null){
			try {
				rs.last();
				rowCount=rs.getRow();
				rs.beforeFirst();
				while(rs!=null&&rs.next()){
					System.out.println(rs.getString("student_name"));
					System.out.println(rs.getFloat("score"));
					System.out.println(rs.getString("lession_name"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}
}
