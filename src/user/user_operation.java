package user;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.stringUtil;

import db.dbconn;

public class user_operation{
	
	/**
	 * 功能：判断自动生成的角色是否已经生成用户
	 * 输入：foreign_id 为教师表或学生表中的ID号，sysuser_role 为用户角色
	 * 输出：返回0 表未生成， 返回1 表已生成，返回3 表未知
	 * 说明：只要有sysuser表中可查到的记录，就表示已经生成
	 */
	public int isAutoGenOk(long foreign_id,int sysuser_role){
		if(foreign_id==0||sysuser_role==0||sysuser_role==1||sysuser_role==2){
			//用户角色为1表系统管理员，为2表教务管理人员，这两种角色手工生成，所以不为自动生成
			return 3;
		}
		String sqlString=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null)
			return 3;	//连接失败
		sqlString="select * from sysuser where foreign_id=? and sysuser_role=?";
		try{
			PreparedStatement preSqlSelect=dbconn.prepareStatement(sqlString);
			preSqlSelect.setLong(1, foreign_id);
			preSqlSelect.setInt(2, sysuser_role);
			rs=preSqlSelect.executeQuery();
			if(rs.next())
				return 1;
			else return 0;
		}catch(Exception e){
			System.out.print(e);
			return 3;
		}
	}
	
	/**
	 * 功能：生成教师用户
	 */
	public String genTeacherUser(String teacher_id[]){
		String returnString=new String("");
		String sqlString=null;
		ResultSet rs=null;
		ResultSet rs1=null;
		Statement sql=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null)
			return returnString;
		try{
			sqlString="select * from teacher";
			System.out.print(sqlString+teacher_id.length);
			int j=0;
			if(teacher_id.length!=0){//生成部分教师用户
				for(int i=0;i<teacher_id.length;i++){
					if(teacher_id[i]!=null&&teacher_id[i].length()!=0&&
							!teacher_id[i].equalsIgnoreCase("null")){
						if(j==0){
							sqlString=sqlString+" where teacher_id="+teacher_id[i];
							j++;
						}else{
							sqlString+=" or teacher_id="+teacher_id[i];
						}
					}
				}
			}
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
			while(rs.next()){
				sqlString="select * from sysuser where foreign_id="+
					rs.getLong("teacher_id")+" and sysuser_role=3";
				sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs1=sql.executeQuery(sqlString);
				if(!rs1.next()){//不存在插入，存在不做事
					sqlString="insert into sysuser(sysuser_name,sysuser_password,"+
						"sysuser_role,foreign_id) values(?,?,3,?)";
					PreparedStatement preSqlUpdate=dbconn.prepareStatement(sqlString);
					preSqlUpdate.setString(1, rs.getString("teacher_name"));
					preSqlUpdate.setString(2, "111111");
					preSqlUpdate.setLong(3, rs.getLong("teacher_id"));
					preSqlUpdate.executeUpdate();
					returnString+="生成教师["+rs.getString("teacher_name")+"]用户成功！<br>";
				}else
					returnString+="教师["+rs.getString("teacher_name")+"]用户已经生成，不再再生成！<br>";
				rs1.close();
			}
		}catch(Exception e){
			System.out.print(e);
		}
		return returnString;
	}
	/**
	 * 得到一条用户记录
	 */
	public ResultSet getUserByPrimKey(int sysuser_id){
		String sqlStrng=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbcon=dbconnObject.getDBConn();
		if(dbcon==null||sysuser_id==0)
			return null;		//连接失败
		try{
			sqlStrng="select * from sysuser where sysuser_id="+sysuser_id;
			sql=dbcon.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlStrng);
			return rs;
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
	
	
	
	/**
	 * 功能：根据用户表的外键和角色得到用户ID号
	 */
	public long getUserId(int foreign_id,int sysuser_role){
		if(foreign_id==0||sysuser_role==0)
			return 0;
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		if(dbconn==null)
			return 0;
		try{
			//查询出数据
			sqlString="select * from sysuser where foreign_id="+foreign_id+
				" and sysuser_role="+sysuser_role;
			//System.out.println(sqlString);
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			rs=sql.executeQuery(sqlString);
			if(rs.next())
				return rs.getLong("sysuser_id");
			else
				return 0;			
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * 功能：更新系统用户信息
	 */
	public int update_sysuser(int user_id,String user_name,String user_password,
			int user_role){
		if(user_role==0||user_id==0)
			return 0;
		String sqlString=null;
		Statement sql=null;
		ResultSet rs=null;
		dbconn dbconnObject=new dbconn();
		Connection dbconn=dbconnObject.getDBConn();
		//输入参数编码转换
		stringUtil stringCode=new stringUtil();
		user_name=stringCode.codeToString(user_name.trim());
		user_password=stringCode.codeToString(user_password.trim());
		if(dbconn==null)
			return 0;
		try{
			sqlString="update sysuser set sysuser_name='"+user_name+
				"',sysuser_password='"+user_password+"',sysuser_role="+
				user_role+" where sysuser_id="+user_id;
			System.out.println(sqlString);
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			return sql.executeUpdate(sqlString);
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * 功能：删除一条用户信息
	 */
	public int deleteUserByPrimKey(int sysuser_id){
		String sqlString=null;	//sql语句字符串
		Statement sql=null;		//sql语句对象
		ResultSet rs=null;		//结果记录集
		dbconn dbconnObject = new dbconn();		//数据库连接对象
		Connection dbconn=dbconnObject.getDBConn();	//得到数据库连接
		if(dbconn==null||sysuser_id==0)
			return 0;
		try{
			sqlString="delete from sysuser where sysuser_id="+sysuser_id;
			sql=dbconn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			return sql.executeUpdate(sqlString);
		}catch(Exception e){
			System.out.print(e);
			return 0;
		}
	}
	/**
	 * 得到一条用户记录
	 */
	public ResultSet getUserOne(String sysuser_name,String sysuser_password,int sysuser_role0){
		String sqlString=null;	//sql语句字符串
		Statement sql=null;		//sql语句对象
		ResultSet rs=null;		//结果记录集
		dbconn dbconnObject = new dbconn();		//数据库连接对象
		Connection dbconn=dbconnObject.getDBConn();	//得到数据库连接
		if(dbconn==null)
			return null;
		
		//输入参数编码转换
//		stringUtil stringCode = new stringUtil();
//		sysuser_name=stringCode.codeToString(sysuser_name.trim());
//		sysuser_password=stringCode.codeToString(sysuser_password.trim());
		String sysuser_role = Integer.toString(sysuser_role0);
		try{
			//查询出数据
			sqlString="select * from sysuser where sysuser_name=? and "+
				"sysuser_password=? and sysuser_role=?";
			PreparedStatement preSQLSelect=dbconn.prepareStatement(sqlString);
			preSQLSelect.setString(1, sysuser_name);
			preSQLSelect.setString(2, sysuser_password);
			preSQLSelect.setString(3, sysuser_role);
			rs=preSQLSelect.executeQuery();
			System.out.println("select * from sysuser where sysuser_name='"+
					sysuser_name+"' and sysuser_pawssword='"+sysuser_password+
					"' and sysuser_role="+sysuser_role);
			return rs;				
		}catch(Exception e){
			System.out.print(e);
			return null;
		}
	}
}