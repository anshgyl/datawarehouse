package com.dm.datawarehouse.service;

import java.util.Iterator;

import com.dm.datawarehouse.bean.DTC;
import com.dm.datawarehouse.bean.Query;

public class QueryService {
	private String query;
	private Query generate;

	public String generateQuery(DTC dtc, Query q) {
		generate = new Query();
		generate.setSelect(dtc.getColumns());
		//generate.setFrom(dtc.getTables());
		query = "SELECT ";
		Iterator<String> i = generate.getSelect().iterator();
		// Iterator<String> i = dtc.getColumns().iterator();
		while (i.hasNext()) {
			query = query + i.next();
			if (i.hasNext())
				query = query + ", ";
		}

		

		generate = JoinService.returnJoin(dtc.getDatabase(), dtc.getTables() , generate);
		
		query = query + " FROM ";
		i = generate.getFrom().iterator();
		while (i.hasNext()) {
			query = query + i.next();
			if (i.hasNext())
				query = query + ", ";
		}
		System.out.println("\n\nGET WHERE" + q.getWhere() + "-");
		if(q.getWhere().size() > 0)
		{
			generate.getWhere().addAll(q.getWhere());
		}
		i = generate.getWhere().iterator();
		if (i.hasNext()) {
			query = query + " WHERE ";

			while (i.hasNext()) {
				query = query + "(" +i.next() + ")";
				if (i.hasNext())
					query = query + " AND ";
			}
		}
		if (q.getGroupBy() != "")
		{
			query = query + " GROUP BY " + q.getGroupBy();
		}

		System.out.println(generate);
//		if (!join.equals("")) {
//			query = query + join;
//		}
		return query;

	}
}