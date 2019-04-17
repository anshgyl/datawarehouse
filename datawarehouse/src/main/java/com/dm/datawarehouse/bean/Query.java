package com.dm.datawarehouse.bean;

public class Query
{
	String database;
	String queryString;
	
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
}
