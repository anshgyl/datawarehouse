package com.dm.datawarehouse.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.dm.datawarehouse.bean.Node;
import com.dm.datawarehouse.bean.Query;
import com.dm.datawarehouse.connection.Conn;
import com.dm.datawarehouse.dao.JoinDAO;

public class JoinService
{
	static Connection con;
	static ArrayList<String> tables;
	static String database;
	static HashMap<String, ArrayList<Node>> adj;
	static int count = 0;
    static ArrayList<Node> GlobalPathList = new ArrayList<Node>();


	public static void getPath(Node source, Node destination)
	{
		count = 0;
		boolean[] isVisited = new boolean[tables.size()]; 
		ArrayList<Node> pathList = new ArrayList<Node>();
 
		printAllPathsUtil(source, destination, isVisited, pathList); 
	}

	
	private static void printAllPathsUtil(Node source, Node destination, boolean[] isVisited, ArrayList<Node> localPathList)
	{
		int u = tables.indexOf(source.referredName);
		isVisited[u] = true; 

		if (source.referredName.equals(destination.referredName))
		{ 
			count++;
			//GlobalPathList = new ArrayList<Node>(localPathList); 
			GlobalPathList.addAll(localPathList);
			System.out.println(localPathList);
			System.out.println(GlobalPathList);
			isVisited[u] = false; 
			return ; 
		} 
		for (Node st : adj.get(source.referredName))  
		{ 
			int i = tables.indexOf(st.referredName);
			if (!isVisited[i] && count == 0) 
			{ 
				localPathList.add(st); 
				printAllPathsUtil(st, destination, isVisited, localPathList); 
				localPathList.remove(st); 
			} 
		} 
		isVisited[u] = false; 
	}
	
			
		public static Query returnJoin(String db, LinkedHashSet<String> tbnames,Query generate)
		{  
			adj = new HashMap<String, ArrayList<Node> >();
			JoinDAO joinDAO= new JoinDAO();
			database = db;
			String join = "";
			
			@SuppressWarnings("unused")
			String from = "";
			tables = new ArrayList<String>();
			try
			{
				Conn c = new Conn();
				con = c.connect("information_schema");
				tables = joinDAO.get_tables(db);
				adj = joinDAO.get_foreign(db);
				
				Iterator<String> i = tbnames.iterator();
				String root = i.next();
				while(i.hasNext())
				{
					String temp = i.next();
					if (!temp.equals(root))
							getPath(new Node("", "", root, ""), new Node("", "", temp, ""));
				}
				
				Iterator<Node> j = GlobalPathList.iterator();
				while(j.hasNext())
				{
					Node x = j.next();
					tbnames.add(x.myName);
					tbnames.add(x.referredName);
//					generate.getFrom().add(x.myName);
//					generate.getFrom().add(x.referredName);

				}
				
//				from = from + " FROM ";
				generate.setFrom(tbnames);
//				
//				i = tbnames.iterator();
//				while (i.hasNext())
//				{
//					from = from + i.next();
//					if (i.hasNext())
//						from = from + ", ";
//				}
				
				//getPath(new Node("", "", "employee", ""), new Node("", "", "project", ""));
				j = GlobalPathList.iterator();
				while(j.hasNext())
				{
					Node x = j.next();
					join = join + x.myName + "." + x.myColumn + " = " + x.referredName + "." + x.referredColumn;
					if(j.hasNext())
						join = join + " AND ";
				}
				System.out.println(join);
				
				if (!join.equals("")) {
					generate.getWhere().add(join);
				}
//				if(!join.equals(""))
//				{
//					join = from + " WHERE " + join;
//				}
//				else
//				{
//					join = from;
//				}
				System.out.println(join);
				con.close();
			}
			catch(Exception e){}
			GlobalPathList = new ArrayList<Node>();
			return generate;
	
	}
}