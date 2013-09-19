package util;

import java.io.IOException;

public class stringUtil{
	
	public String codeToString(String getStr){
		try{
			if(getStr==null){
				getStr="";
			}else{
				getStr=new String(getStr.getBytes("ISO-8859-1"),"utf8");
			} 
		}catch(IOException e){
			e.printStackTrace();
		}
		return getStr;
	}
}