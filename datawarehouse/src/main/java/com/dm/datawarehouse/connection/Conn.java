package com.dm.datawarehouse.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conn
{
	private Connection con;
	
	public Connection connect(String database)
	{
		con = null;
		try
		{  
			Class.forName("com.mysql.jdbc.Driver");  
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", "root"); 
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}  
		return con;
	}
}

