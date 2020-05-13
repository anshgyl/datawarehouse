package com.dm.datawarehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dm.datawarehouse.bean.Database;
import com.dm.datawarehouse.connection.Conn;

public class DatabaseDAO
{
	Connection con;
	String database = "information_schema";
	
	public List<Database> showDatabases()
	{
		Statement st;
		ResultSet rs;
		String query;
		Database db;
		List<Database> dbList = new ArrayList<Database>();
		try
		{
			query = "SHOW DATABASES";
			con = new Conn().connect(database);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
				db = new Database();
				db.setName(rs.getString(1));
				dbList.add(db);
			}
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return dbList;
	}
}
