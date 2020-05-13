package com.dm.datawarehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dm.datawarehouse.bean.Database;
import com.dm.datawarehouse.bean.Table;
import com.dm.datawarehouse.bean.Column;
import com.dm.datawarehouse.connection.Conn;

public class ColumnDAO
{
	Connection con;
	String database = "information_schema";
	
	public List<Column> showColumns(Database db, Table tb)
	{
		Statement st;
		ResultSet rs;
		String query;
		Column cl;
		List<Column> clList = new ArrayList<Column>();
		try
		{
			query = "SELECT c.NAME FROM INNODB_TABLES t, INNODB_COLUMNS c WHERE t.NAME = '"+ db.getName() + "/" +tb.getName() + "' and t.TABLE_ID=c.TABLE_ID";
			System.out.println(query);
			con = new Conn().connect(database);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
				cl = new Column();
				cl.setName(rs.getString(1));
				clList.add(cl);
			}
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return clList;
	}
	
	public List<String> showColumnValues(Database db, Table tb, Column cl)
	{
		Statement st;
		ResultSet rs;
		String query;
		List<String> output = new ArrayList<String>();
		String ele;
		try
		{
			query = "SELECT " + cl.getName() + " FROM " + tb.getName();
			System.out.println(query);
			con = new Conn().connect(db.getName());
			st = con.createStatement();
			rs = st.executeQuery(query);
			while(rs.next())
			{
				ele = rs.getString(1);
				output.add(ele);
			}
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return output;
	}
}
