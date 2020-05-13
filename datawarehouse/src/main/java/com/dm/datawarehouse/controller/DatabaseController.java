package com.dm.datawarehouse.controller;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.dm.datawarehouse.bean.Database;
import com.dm.datawarehouse.dao.DatabaseDAO;

@Path("/DatabaseController")
public class DatabaseController
{
	@GET
	 @Path("/alldatabases")
	 @Produces(MediaType.APPLICATION_JSON)
	public Response getAllDatabases()
	{
		GenericEntity<List<Database>> dbList;
		dbList  = new GenericEntity<List<Database>>(new DatabaseDAO().showDatabases()) { };
		return Response.ok(dbList).build();
	}
}
