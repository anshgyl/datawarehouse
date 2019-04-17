package com.dm.datawarehouse.service;

import java.util.Iterator;

import com.dm.datawarehouse.bean.DTC;

public class QueryService
{	
	private String query;
	
	public String generateQuery(DTC dtc)
	{
		query = "SELECT ";
		Iterator<String> i = dtc.getColumns().iterator();
		while (i.hasNext())
		{
			query = query + i.next();
			if (i.hasNext())
				query = query + ", ";
		}
		
//		query = query + " FROM ";
//		i = dtc.getTables().iterator();
//		while (i.hasNext())
//		{
//			query = query + i.next();
//			if (i.hasNext())
//				query = query + ", ";
//		}
		
		String join = JoinService.returnJoin(dtc.getDatabase(),dtc.getTables());
		if(!join.equals(""))
		{
			query = query + join;
		}
		return query;
	}
}