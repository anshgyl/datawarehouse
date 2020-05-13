package com.dm.datawarehouse.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dm.datawarehouse.bean.Database;
import com.dm.datawarehouse.bean.Table;

import com.dm.datawarehouse.dao.TableDAO;

@Path("/TableController")
public class TableController
{
	@GET
	@Path("/tables/{dbname}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTables(@PathParam("dbname") String dbname)
	{
		Database db = new Database();
		db.setName(dbname);
		
		GenericEntity<List<Table>> tbList;
		tbList  = new GenericEntity<List<Table>>(new TableDAO().showTables(db)) { };
		return Response.ok(tbList).build();
	}
}

