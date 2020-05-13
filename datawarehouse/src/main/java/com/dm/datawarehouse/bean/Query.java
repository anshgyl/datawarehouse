package com.dm.datawarehouse.bean;

import java.util.LinkedHashSet;

public class Query
{

	String database;
	String queryString;
	LinkedHashSet<String> select = new LinkedHashSet<>();
	LinkedHashSet<String> from = new LinkedHashSet<>();
	LinkedHashSet<String> where = new LinkedHashSet<>();
	LinkedHashSet<String> orderBy = new LinkedHashSet<>();
	String groupBy = "";

	public Query()
	{
	}

	public Query(String database, String queryString)
	{
		super();
		this.database = database;
		this.queryString = queryString;
	}

	public String getDatabase()
	{
		return database;
	}

	public void setDatabase(String database)
	{
		this.database = database;
	}

	public String getQueryString()
	{
		return queryString;
	}

	public void setQueryString(String queryString)
	{
		this.queryString = queryString;
	}

	public LinkedHashSet<String> getSelect()
	{
		return select;
	}

	public void setSelect(LinkedHashSet<String> select)
	{
		this.select = select;
	}

	public LinkedHashSet<String> getFrom()
	{
		return from;
	}

	public void setFrom(LinkedHashSet<String> from)
	{
		this.from = from;
	}

	public LinkedHashSet<String> getWhere()
	{
		return where;
	}

	public void setWhere(LinkedHashSet<String> where)
	{
		this.where = where;
	}

	public LinkedHashSet<String> getOrderBy()
	{
		return orderBy;
	}

	public void setOrderBy(LinkedHashSet<String> orderBy)
	{
		this.orderBy = orderBy;
	}

	public String getGroupBy()
	{
		return groupBy;
	}

	public void setGroupBy(String groupBy)
	{
		this.groupBy = groupBy;
	}

	@Override
	public String toString()
	{
		return "Query [database=" + database + ", queryString=" + queryString + ", select=" + select + ", from=" + from
				+ ", where=" + where + ", orderBy=" + orderBy + ", groupBy=" + groupBy + "]";
	}

}
