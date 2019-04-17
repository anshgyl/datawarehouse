package com.dm.datawarehouse.controller;

import java.util.LinkedHashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.dm.datawarehouse.bean.Database;
import com.dm.datawarehouse.bean.Query;
import com.dm.datawarehouse.bean.Table;
import com.dm.datawarehouse.bean.Column;
import com.dm.datawarehouse.bean.DTC;
import com.dm.datawarehouse.dao.ColumnDAO;
import com.dm.datawarehouse.dao.QueryDAO;
import com.dm.datawarehouse.service.QueryService;


@Path("/ColumnController")
public class ColumnController
{
	@GET
	@Path("/columns/{dbname}/{tbname}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getColumns(	@PathParam("dbname") String dbname,
								@PathParam("tbname") String tbname)
	{
		Database db = new Database();
		db.setName(dbname);
		
		Table tb = new Table();
		tb.setName(tbname);
		
		GenericEntity<List<Column>> clList;
		clList  = new GenericEntity<List<Column>>(new ColumnDAO().showColumns(db, tb)) { };
		return Response.ok(clList).build();
	}
	
	@GET
	@Path("/columnvalues/{dbname}/{tbname}/{clname}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getColumnValues(@PathParam("dbname") String dbname,
									@PathParam("tbname") String tbname,
									@PathParam("clname") String clname)
	{
		Database db = new Database();
		db.setName(dbname);
		
		Table tb = new Table();
		tb.setName(tbname);
		
		Column cl = new Column();
		cl.setName(clname);
		
		GenericEntity<List<String>> output;
		output  = new GenericEntity<List<String>>(new ColumnDAO().showColumnValues(db, tb, cl)) { };
		return Response.ok(output).build();
	}
	
	@POST
	@Path("/setcolumns")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response setColumns(String selectedCols)
	{
		System.out.println("Hello");
		JSONArray jArray = (JSONArray) new JSONTokener(selectedCols).nextValue();
		
		DTC dtc;
		LinkedHashSet<String> tables = new LinkedHashSet<String>();
		LinkedHashSet<String> columns = new LinkedHashSet<String>();
		LinkedHashSet<String> dbPlusTables = new LinkedHashSet<String>();
		
		for (int i = 0; i < jArray.length(); i++)
		{
			JSONObject jObject = jArray.getJSONObject(i);
			//dtc = new DTC(jObject.getString("dbname"), jObject.getString("tbname"), jObject.getString("clname"));
			
			tables.add(jObject.getString("tbname"));
			columns.add(jObject.getString("tbname") + "." + jObject.getString("clname"));
			dbPlusTables.add(jObject.getString("dbname") + "/" + jObject.getString("tbname"));

		}
		dtc = new DTC(jArray.getJSONObject(0).getString("dbname"), tables, columns, dbPlusTables);
		
		GenericEntity<List<String[]>> output;
		output  = new GenericEntity<List<String[]>>(new QueryDAO().executeQuery(new Query(jArray.getJSONObject(0).getString("dbname"), new QueryService().generateQuery(dtc)))) { };
		return Response.ok(output).build();
		
	    //return Response.status(Response.Status.OK).entity("Salam").build();
	}
}


