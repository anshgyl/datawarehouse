package com.dm.datawarehouse.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class getArrayListData {
	public ArrayList<String[]> getArrayListDataSet	(ResultSet rs){
		ArrayList<String[]> data = new ArrayList<>();
		//ResultSet rs = QueryDAO.rs;
		String[] row;
		if(rs!=null) {
			try {
				row = new String[rs.getMetaData().getColumnCount()];
				for(int i=1; i<=rs.getMetaData().getColumnCount(); i++)
					row[i-1] =  rs.getMetaData().getColumnName(i);	
					//data.getDefinition().setAttributeType("Headquarters", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
					data.add(row);
				//System.out.println("hello arraylist1"+rs.getFetchSize());
				while(rs.next()) {
					//System.out.println("hello arraylist2");
					row = new String[rs.getMetaData().getColumnCount()];
					for(int i=0; i<rs.getMetaData().getColumnCount(); i++)
						row[i] = (String)rs.getObject(i+1);
					data.add(row);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			System.out.println("empty rs");
		}
		
		return data;
		
	}
}
