package com.dm.datawarehouse.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.deidentifier.arx.ARXAnonymizer;
import org.deidentifier.arx.ARXConfiguration;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.AttributeType;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.AttributeType.Hierarchy.DefaultHierarchy;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.criteria.DistinctLDiversity;
import org.deidentifier.arx.criteria.KAnonymity;

import com.dm.datawarehouse.bean.Privacy;
import com.dm.datawarehouse.dao.PrivacyDAO;

public class AnonymizerLocal
{
	Data data;

	ARXConfiguration config;
	ARXAnonymizer anonymizer;
	List<String[]> table = new ArrayList<>();

	public AnonymizerLocal()
	{

	}

	public List<String[]> start(ResultSet rs)
	{
		try
		{
			config = ARXConfiguration.create();
			anonymizer = new ARXAnonymizer();
			data = Data.create(new getArrayListData().getArrayListDataSet(rs));

			int count = setAttributeType(rs);
			if (count != rs.getMetaData().getColumnCount())
			{
				editConfig();
				Anonymize(rs);
				return table;
			}
			return null;
		} catch (Exception e)
		{
			System.out.println(e);
			return table;
		}

	}

	int setAttributeType(ResultSet rs)
	{
		String table;
		String database;
		String privacyColName;
		PrivacyDAO privacyDAO = new PrivacyDAO();
		Privacy privacy = new Privacy();
		int count = 0;

		@SuppressWarnings("unused")
		DefaultHierarchy hierarchy = Hierarchy.create();
		// hierarchy.add("81667", "8166*", "816**", "81***", "8****", "*****");
		// hierarchy.add("81675", "8167*", "816**", "81***", "8****", "*****");
		try
		{
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
			{
				database = rs.getMetaData().getCatalogName(i);
				table = rs.getMetaData().getTableName(i);
				privacyColName = database + "/" + table + "/" + rs.getMetaData().getColumnName(i);
				privacy = privacyDAO.privacyDetails(privacyColName);
				String file_location = "/Users/anshgoyal/Downloads/PrivacyFilter/";

				if (privacy == null)
				{
					// data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i),
					// AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
					count++;
					System.out.println("Insensitive");
					data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i),
							AttributeType.INSENSITIVE_ATTRIBUTE);

				} else if (privacy.getType().equals("quasi"))
				{
					System.out.println("Quasi");
					file_location = file_location + privacy.getFile_location();
					System.out.println(file_location);
					data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i),
							AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
					data.getDefinition().setHierarchy(rs.getMetaData().getColumnName(i),
							Hierarchy.create(new File(file_location), Charset.defaultCharset(), ';'));
				} else if (privacy.getType().equals("sensitive"))
				{
					System.out.println("Sensitive");
					file_location = file_location + privacy.getFile_location();
					data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i),
							AttributeType.SENSITIVE_ATTRIBUTE);
					data.getDefinition().setHierarchy(rs.getMetaData().getColumnName(i),
							Hierarchy.create(new File(file_location), Charset.defaultCharset(), ';'));
					config.addPrivacyModel(new DistinctLDiversity(rs.getMetaData().getColumnName(i), 4));
				} else if (privacy.getType().equals("identifying"))
				{
					System.out.println("Identifying");
					file_location = file_location + privacy.getFile_location();
					data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i),
							AttributeType.IDENTIFYING_ATTRIBUTE);
					data.getDefinition().setHierarchy(rs.getMetaData().getColumnName(i),
							Hierarchy.create(new File(file_location), Charset.defaultCharset(), ';'));
				}

				// data.getDefinition().setAttributeType("Headquarters",
				// AttributeType.QUASI_IDENTIFYING_ATTRIBUTE);
//				data.getDefinition().setAttributeType(rs.getMetaData().getColumnName(i), hierarchy);
			}
//			data.getDefinition().setHierarchy("ssn", Hierarchy.create(new File("/Users/anshgoyal/Downloads/heirarchyForSSN.csv"),Charset.defaultCharset(),';'));

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
		return count;
	}

	void editConfig()
	{
		config.addPrivacyModel(new KAnonymity(2));
		config.setSuppressionLimit(0.1d);
	}

	void Anonymize(ResultSet rs) throws SQLException
	{
		try
		{

			// ARXResult result = anonymizer.anonymize(data, config);
			ARXResult result = anonymizer.anonymize(data, config);
			if (result.isResultAvailable())
			{
				System.out.println("Yes");
				String[] heading = new String[rs.getMetaData().getColumnCount()];
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
					heading[i - 1] = rs.getMetaData().getColumnName(i);
				table.add(heading);
				DataHandle handle = result.getOutput();
				for (int row = 0; row < handle.getNumRows(); row++)
				{
					String[] row_temp = new String[handle.getNumColumns()];

					for (int col = 0; col < handle.getNumColumns(); col++)
					{
						row_temp[col] = (handle.getValue(row, col) == null) ? null
								: handle.getValue(row, col).toString();
						System.out.print(handle.getValue(row, col) + " ");
					}
					System.out.println("");
					table.add(row_temp);

				}
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
