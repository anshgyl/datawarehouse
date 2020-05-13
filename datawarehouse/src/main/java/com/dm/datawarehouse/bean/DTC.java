package com.dm.datawarehouse.bean;

import java.util.LinkedHashSet;

public class DTC
{
	private String database;
	private LinkedHashSet<String> tables;
	private LinkedHashSet<String> columns;
	private LinkedHashSet<String> dbPlusTables;
	
	public DTC()
	{
	}
	
	public DTC(String database, LinkedHashSet<String> tables, LinkedHashSet<String> columns, LinkedHashSet<String> dbPlusTables)
	{
		super();
		this.database = database;
		this.tables = tables;
		this.columns = columns;
		this.dbPlusTables = dbPlusTables;
	}

	public String getDatabase()
	{
		return database;
	}

	public void setDatabase(String database)
	{
		this.database = database;
	}

	public LinkedHashSet<String> getTables()
	{
		return tables;
	}

	public void setTables(LinkedHashSet<String> tables)
	{
		this.tables = tables;
	}

	public LinkedHashSet<String> getColumns()
	{
		return columns;
	}

	public void setColumns(LinkedHashSet<String> columns)
	{
		this.columns = columns;
	}

	public LinkedHashSet<String> getDbPlusTables()
	{
		return dbPlusTables;
	}

	public void setDbPlusTables(LinkedHashSet<String> dbPlusTables)
	{
		this.dbPlusTables = dbPlusTables;
	}

	@Override
	public String toString()
	{
		return "DTC [database=" + database + ", tables=" + tables + ", columns=" + columns + ", dbPlusTables="
				+ dbPlusTables + "]";
	}
	
	
}
