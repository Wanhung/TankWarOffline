package com.tankwar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
 
public class FrameDao {
		public ArrayList Quety(){
			ArrayList frame=new ArrayList();
	Statement stat=null;
			ResultSet rs=null;
	try {
	
		 stat= 	JDBC.getConn().createStatement();
		String sql="select * from frame";
		rs=stat.executeQuery(sql);
		while(rs.next()){
			FrameBean fb=new FrameBean();
			 fb.setGwidth(rs.getInt(2));
			 fb.setGheight(rs.getInt(3));
			 frame.add(fb);
		}
	
	}   catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	JDBC.closeAll(rs, stat);
	return frame;
}
}