package com.dm.datawarehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.dm.datawarehouse.bean.Node;
import com.dm.datawarehouse.connection.Conn;

public class JoinDAO
{
	Connection con;
	ArrayList<String> tables;
	String database;
	HashMap<String, ArrayList<Node>> adjList;

	public ArrayList<String> get_tables(String database)
	{   
		Conn c = new Conn();
		con = c.connect("information_schema");
		this.database = database;
		tables = new ArrayList<String>();
		adjList = new HashMap<String, ArrayList<Node> >();
		try 
		{
			Statement stmt = con.createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT TABLE_NAME FROM Tables WHERE TABLE_SCHEMA = '" + database + "'");
			while(rs.next())  
			{
				tables.add(rs.getString(1) + "");
				ArrayList<Node> temp1 = new ArrayList<Node>(); 
				adjList.put(rs.getString(1) + "", temp1);
			}
		}
		catch(Exception e){}
		return tables;
	}

	public HashMap<String, ArrayList<Node>> get_foreign(String database)
	{
		get_tables(database);
		Conn c = new Conn();
		con = c.connect("information_schema");
		this.database = database;
		try
		{
			System.out.println(tables);
			Statement stmt = con.createStatement(); 		
			for(String tbname : tables)
			{
				String dbPlusTable = database + "/" + tbname;
				ResultSet rs = stmt.executeQuery("SELECT t.REF_NAME, t.FOR_NAME, f.REF_COL_NAME, f.FOR_COL_NAME FROM INNODB_FOREIGN t, INNODB_FOREIGN_COLS f WHERE t.id = f.id AND t.FOR_NAME = '" + dbPlusTable + "'");

				while(rs.next())  
				{
					String str = rs.getString(1);
					String[] arrOfStr = str.split("/", 2);
					if(! arrOfStr[1].equals(tbname))
					{
						Node node = new Node(tbname, rs.getString(4), arrOfStr[1], rs.getString(3));
						adjList.get(tbname).add(node);
					}
				}

				rs = stmt.executeQuery("SELECT t.REF_NAME, t.FOR_NAME, f.REF_COL_NAME, f.FOR_COL_NAME FROM INNODB_FOREIGN t, INNODB_FOREIGN_COLS f WHERE t.id = f.id AND t.REF_NAME = '" + dbPlusTable + "'");  

				while(rs.next())  
				{   
					String str = rs.getString(2);
					String[] arrOfStr = str.split("/", 2);

					if(! arrOfStr[1].equals(tbname))
					{

						Node node = new Node(tbname, rs.getString(3), arrOfStr[1], rs.getString(4));
						adjList.get(tbname).add(node);
					}
				}
			}
		}
		catch(Exception e){}
		return adjList;
	}
}
