package com.dm.datawarehouse.bean;

public class Node {
	public String myName;
	public String myColumn;
	public String referredName;
	public String referredColumn;
	public Node()
	{
		
	}
	public Node(String myName, String myColumn, String referredName, String referredColumn) {
		super();
		this.myName = myName;
		this.myColumn = myColumn;
		this.referredName = referredName;
		this.referredColumn = referredColumn;
	}
}

