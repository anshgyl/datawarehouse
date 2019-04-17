package com.dm.datawarehouse.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dm.datawarehouse.bean.Query;
import com.dm.datawarehouse.connection.Conn;
import com.dm.datawarehouse.service.AnonymizerLocal;

public class QueryDAO
{
	Connection con;

	public  ResultSet rs;
	
	public  ResultSet getRs() {
		return rs;
	}

	public List<String[]> executeQuery(Query q)
	{
		Statement st;
		List<String[]> table = new ArrayList<>();
		try
		{
			System.out.println(q.getQueryString());
			con = new Conn().connect(q.getDatabase());
			st = con.createStatement();
			rs = st.executeQuery(q.getQueryString());

			int nCol = rs.getMetaData().getColumnCount();
			while(rs.next())
			{
			    String[] row = new String[nCol];
			    for(int iCol = 1; iCol <= nCol; iCol++ ){
			            Object obj = rs.getObject( iCol );
			            row[iCol-1] = (obj == null) ?null:obj.toString();
			    }
			    table.add(row);
			}

			// print result
			for( String[] row: table ){
			    for( String s: row ){
			        System.out.print( " " + s );
			    }
			    System.out.println();
			}
			rs = st.executeQuery(q.getQueryString());

			//privacy
			AnonymizerLocal a= new AnonymizerLocal();
			a.start(rs);
		} 
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return table;
	}
}
