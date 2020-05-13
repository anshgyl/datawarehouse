package com.dm.datawarehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dm.datawarehouse.bean.Database;
import com.dm.datawarehouse.bean.Table;
import com.dm.datawarehouse.connection.Conn;

public class TableDAO
{
	Connection con;
	
	public List<Table> showTables(Database db)
	{
		Statement st;
		ResultSet rs;
		String query;
		Table tb;
		List<Table> tbList = new ArrayList<Table>();
		try
		{
			query = "SHOW TABLES";
			con = new Conn().connect(db.getName());
			st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
				tb = new Table();
				tb.setName(rs.getString(1));
				tbList.add(tb);
			}
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return tbList;
	}
	
	public static void main(String[] args)
	{
		TableDAO t = new TableDAO();
		Database db = new Database();
		db.setName("amz");
		for (Table x: t.showTables(db))
		{
			System.out.println(x.getName());
		}
	}
}
