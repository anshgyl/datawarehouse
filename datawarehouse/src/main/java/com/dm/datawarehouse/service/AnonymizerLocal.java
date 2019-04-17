package com.dm.datawarehouse.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.criteria.KAnonymity;

public class AnonymizerLocal {
	Data data;
	
	ARXConfiguration config;
	ARXAnonymizer anonymizer;
	
	public AnonymizerLocal() {
		
	}
	
	public void start(ResultSet rs)
	{
		try
		{
			config = ARXConfiguration.create();
			anonymizer = new ARXAnonymizer();
			data = Data.create(new getArrayListData().getArrayListDataSet(rs));
			setAttributeType(rs);
			editConfig();
			Anonymize();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	void setAttributeType(ResultSet rs) {
		
		try {
			for(int i=1; i<=rs.getMetaData().getColumnCount(); i++) {
				data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i), AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);	
				//data.getDefinition().setAttributeType("Headquarters", AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
				
			}
			data.getDefinition().setHierarchy("ssn", Hierarchy.create(new File("/Users/anshgoyal/Downloads/heirarchyForSSN.csv"),Charset.defaultCharset(),';'));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
		
	}
	
	void editConfig() {
		config.addPrivacyModel(new KAnonymity(2));
		config.setSuppressionLimit(0.8d);
	}
	
	void Anonymize() {
		try {
			
			ARXResult result = anonymizer.anonymize(data, config);
			if(result.isResultAvailable()) {
				System.out.println("Yes");
				DataHandle handle = result.getOutput();
				for(int row=0; row < handle.getNumRows(); row++) {
					for(int col=0; col<handle.getNumColumns(); col++) {
						System.out.print(handle.getValue(row, col)+" ");
					}
					System.out.println("");
				}
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
