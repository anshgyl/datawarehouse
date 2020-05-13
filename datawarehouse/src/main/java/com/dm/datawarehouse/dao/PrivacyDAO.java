package com.dm.datawarehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.dm.datawarehouse.bean.Privacy;
import com.dm.datawarehouse.connection.Conn;

public class PrivacyDAO
{
	Connection con;
	String database = "datawarehouse";

	public Privacy privacyDetails(String col_name)
	{
		Statement st;
		ResultSet rs;
		String query;
		Privacy pr = null;
		try
		{
			query = "SELECT * FROM privacy WHERE column_name ='" + col_name + "'";
			con = new Conn().connect(database);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next())
			{
				pr = new Privacy();
				pr.setColumn_name(rs.getString(1));
				pr.setType(rs.getString(2));
				pr.setFile_location(rs.getString(3));
			}
		} catch (Exception e)
		{
			// TODO: handle exception
		}
		return pr;
	}
}