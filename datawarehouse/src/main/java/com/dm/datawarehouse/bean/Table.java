package com.dm.datawarehouse.bean;

public class Table
{
	private long table_id;
	private String name;
	private int n_cols;
	private String primaryKey;
	
	public long getTable_id()
	{
		return table_id;
	}
	public void setTable_id(long table_id)
	{
		this.table_id = table_id;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public int getN_cols()
	{
		return n_cols;
	}
	public void setN_cols(int n_cols)
	{
		this.n_cols = n_cols;
	}
	
	public String getPrimaryKey()
	{
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey)
	{
		this.primaryKey = primaryKey;
	}
	@Override
	public String toString()
	{
		return "Table [table_id=" + table_id + ", name=" + name + ", n_cols=" + n_cols + ", primaryKey=" + primaryKey
				+ "]";
	}
	
}
