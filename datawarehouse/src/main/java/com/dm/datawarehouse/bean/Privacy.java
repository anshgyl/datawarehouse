package com.dm.datawarehouse.bean;

public class Privacy
{
	private String column_name;
	private String type;
	private String file_location;

	public String getColumn_name()
	{
		return column_name;
	}

	public void setColumn_name(String column_name)
	{
		this.column_name = column_name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getFile_location()
	{
		return file_location;
	}

	public void setFile_location(String file_location)
	{
		this.file_location = file_location;
	}

	@Override
	public String toString()
	{
		return "Privacy [column_name=" + column_name + ", type=" + type + ", file_location=" + file_location + "]";
	}
}
