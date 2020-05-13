<html>
<head>
	<title>Data WareHouse | Privacy Filters</title>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="//cdn.datatables.net/1.10.19/css/jquery.dataTables.min.css">
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="http://code.jquery.com/jquery-3.3.1.min.js" type="text/javascript"></script>
	<script src="http://cdn.datatables.net/1.10.19/js/jquery.dataTables.min.js"></script>
	<script src="https://cdn.datatables.net/1.10.19/js/dataTables.bootstrap4.min.js"></script>
	
	<style type="text/css">
		table.dataTable thead .sorting:after,
		table.dataTable thead .sorting:before,
		table.dataTable thead .sorting_asc:after,
		table.dataTable thead .sorting_asc:before,
		table.dataTable thead .sorting_asc_disabled:after,
		table.dataTable thead .sorting_asc_disabled:before,
		table.dataTable thead .sorting_desc:after,
		table.dataTable thead .sorting_desc:before,
		table.dataTable thead .sorting_desc_disabled:after,
		table.dataTable thead .sorting_desc_disabled:before {
		bottom: .5em;
		}
	</style>
	
	<script>
		$(document).ready(function()
		{
			var api;
			api = "http://localhost:8055/datawarehouse/webapi/DatabaseController/alldatabases";

			$.get(api, function(data, status)
			{
				var myString="<option>--Default--</option>";
				for(var i = 0; i < data.length; i++)
				{
			 		myString=myString+"<option value = '"+ data[i].name +"'>"+ data[i].name +"</option>";
				}

				$('#db').html(myString);
			});	
		});
		
		function fetchTables()
		{
			$('#addColumn').prop('disabled', true);
			var api;
			var dbname = $('#db').find(":selected").text();
			api = "http://localhost:8055/datawarehouse/webapi/TableController/tables/" + dbname;

			$.get(api, function(data, status)
			{
				var myString="<option>--Default--</option>";
				for(var i = 0; i < data.length; i++)
				{
			 		myString=myString+"<option value = '"+ data[i].name +"'>"+ data[i].name +"</option>";
				}

				$('#tb').html(myString);
				$('#cl').html("<option>--Default--</option>");
			});	
		}
		
		function fetchColumns()
		{
			$('#addColumn').prop('disabled', true);
			var api;
			var dbname = $('#db').find(":selected").text();
			var tbname = $('#tb').find(":selected").text();
			api = "http://localhost:8055/datawarehouse/webapi/ColumnController/columns/" + dbname + "/" + tbname;

			$.get(api, function(data, status)
			{
				var myString="<option>--Default--</option>";
				for(var i = 0; i < data.length; i++)
				{
			 		myString=myString+"<option value = '"+ data[i].name +"'>"+ data[i].name +"</option>";
				}

				$('#cl').html(myString);
			});	
		}
		
		function fetchColumnValues()
		{
			var api;
			var dbname = $('#db').find(":selected").text();
			var tbname = $('#tb').find(":selected").text();
			var clname = $('#cl').find(":selected").text();
			
			api = "http://localhost:8055/datawarehouse/webapi/ColumnController/columnvalues/" + dbname + "/" + tbname + "/" + clname;

			$.get(api, function(data, status)
			{
				var myString = "";
				for(var i = 0; i < data.length; i++)
				{
			 		myString=myString+"<li>" + data[i] + "</li>";
				}
				$('#columnValues').html(myString);
			});	
		}
		
		var columns = [];
		function addColumn()
		{
			var dbname = $('#db').find(":selected").text();
			var tbname = $('#tb').find(":selected").text();
			var clname = $('#cl').find(":selected").text();
			var tbcl = tbname + "." + clname;
			
			$('#db').prop("disabled", true);
			var columnName = {"dbname":dbname, "tbname":tbname, "clname":clname, "tbcl":tbcl};
			columns.push(columnName);
			
			var index = columns.indexOf(columnName);
			
			var dropDown = 	"<select class='form-control' id='whereDrop_" + index + "'><option value = '0'>--None--</option>" +
							"<option value = '<'>&lt;</option>" +
							"<option value = '>'>&gt;</option>" +
							"<option value = '<='>&le;</option>" +
							"<option value = '>='>&ge;</option>" +
							"<option value = '='>=</option></select>";
							
			var aggDrop  = 	"<select class='form-control' id='aggDrop_" + index + "'><option value = '0'>--None--</option>" +
							"<option value = 'COUNT'>COUNT</option>" +
							"<option value = 'AVG'>AVG</option>" +
							"<option value = 'SUM'>SUM</option>" +
							"<option value = 'MIN'>MIN</option>" +
							"<option value = 'MAX'>MAX</option></select>";
							
			var groupByCheck = "<input type='checkbox' id='groupByCheck_" + index + "'>";
			
			var valueBox = '<input type="text" id="box_' + index + '" class="form-control" placeholder="Value">'
							
			var crossIcon =	"<button type='button' class='close' aria-label='Close' onclick='removeColumn(" + index + ")'>" +
			  				"<span aria-hidden='true'>&times;</span>" +
			  				"</button>";
			  				
			var myString = "<tr id='selRow" + index + "'><td>" + tbname + "</td><td>" + clname + "</td><td>" + dropDown + "</td><td>" + valueBox + "</td><td>" + aggDrop + "</td><td>" + groupByCheck + "</td><td>" + crossIcon + "</td><tr>";
			$('#columnValues > tbody:last').append(myString);
			$('#showWhere').prop('disabled', false);
		}
		
		function removeColumn(index)
		{
			var row = document.getElementById("selRow" + index);
		    row.parentNode.removeChild(row);
			$('#saveColumns').prop('disabled', true);
		    
			//var index = columns.indexOf(columnName);
			if (index > -1)
			{
				columns.splice(index, 1);
			}
			if(columns.length == 0)
			{
				$('#showWhere').prop('disabled', true);
				$('#saveColumns').prop('disabled', true);
				$('#db').prop("disabled", false);
			}
		}
		
		var whereArray = new Array();
		var groupByArray = new Array();
		function showWhere()
		{
			var whereString = "";
			whereArray = new Array();
			groupByArray = new Array();
			var index = 0;
			for(var i = 0; i < columns.length; i++)
			{
				var whereDrop = $('#whereDrop_' + i).find(":selected").val();
				var box = $('#box_' + i).val();
				var aggDrop = $('#aggDrop_' + i).find(":selected").val();
				var groupByCheck = $('#groupByCheck_' + i).is(":checked");
				
				
				if(isNaN(box))
					box = "'" + box + "'";
				if(whereDrop != 0)
				{
					var whereClause = columns[i].tbname + "." + columns[i].clname + whereDrop + box;
				
					var whereOption = 	"<select id='whereOption_" + index + "'>" +
										"<option value = 'AND'>AND</option>" +
										"<option value = 'OR'>OR</option></select>";
					index++;
										
					if(i != 0 && whereString != "")
						whereString += whereOption;
					
					whereArray.push(whereClause);
					console.log(whereClause);
					whereString += whereClause;
				}
				
				if(aggDrop != 0)
					columns[i].tbcl = aggDrop + "(" + columns[i].tbname + "." + columns[i].clname + ")";
				
				if(groupByCheck)
					groupByArray.push(columns[i].tbcl);
			}
			$("#whereString").html(whereString);
			$('#saveColumns').prop('disabled', false);
		}
		
		function generateWhere()
		{
			var whereCondition = whereArray[0];
			for (var i = 1; i < whereArray.length; i++)
			{ 
				var whereOption = $('#whereOption_' + i).find(":selected").val();
				whereCondition += " " + whereOption + " " + whereArray[i];
			}
			return whereCondition;
		}
		
		function generateGroupBy()
		{
			var groupByCondition = groupByArray[0];
			for (var i = 1; i < groupByArray.length; i++)
			{
				groupByCondition += ", " + groupByArray[i];
			}
			return groupByCondition;
		}
		
		function saveColumns()
		{
			var whereCondition = "";
			
			if(whereArray.length != 0)
			{
				whereCondition = generateWhere();
				console.log(whereCondition);
				var whereObject = {"dbname":"WHERE", "tbname":whereCondition, "clname":"WHERE", "tbcl:":"WHERE"};
				console.log(whereObject);
				columns.push(whereObject);
			}
			
			if(groupByArray.length != 0)
			{
				groupByCondition = generateGroupBy();
				console.log(groupByCondition);
				var groupByObject = {"dbname":"GROUPBY", "tbname":groupByCondition, "clname":"GROUPBY", "tbcl:":"GROUPBY"};
				console.log(groupByObject);
				columns.push(groupByObject);
			}
			
			var columnNames = JSON.stringify(columns);
			
			console.log(columnNames);
			$.ajax({
		    	type: 'POST',
		        url: 'http://localhost:8055/datawarehouse/webapi/ColumnController/setcolumns/',
		        data: columnNames, 
		        contentType: 'application/json',
		        dataType: 'json',
		        processData: false,
		        async: true,
		        
		        success:function(result) {
					console.log(columnNames);
					if(result == null)
						alert("Please Check the selected conditions.");
					printTable(result)
		        },
		        
				error: function (err) {
					alert(err.status);
				}
		    });
		}
		
		function printTable(result)
		{
			//var myString = "<table class = 'table table-striped table-bordered'><thead><tr>";
			var myString = "<thead><tr>";
			for(var k = 0; k < result[0].length; k++)
			{
				myString = myString + "<th>" + result[0][k] + "</th>";
			}
			
			myString = myString + "</tr></thead><tbody>";
			for(var i = 1; i < result.length; i++)
			{
		 		myString = myString + "<tr>";
		 		for(var j = 0; j < result[i].length; j++)
		 		{
					myString = myString + "<td>" + result[i][j] + "</td>";
		 		}
		 		myString = myString + "</tr>";
			}
			//myString = myString + "</tbody></table>";
			myString = myString + "</tbody>";
			//alert(myString);
			
			$('#resultTable').html(myString);
			$('#resultTable').DataTable();
			$('.dataTables_length').addClass('bs-select');
		}
		
		function enableAdd()
		{
			$('#addColumn').prop('disabled', false);
		}
</script>
	
</head>
<body>
	<div class="form-group col-md-5 col-md-offset-8">
	<label for="db">Databases:</label>
	<select class="form-control" id="db" onchange="fetchTables();">
		<option>--Default--</option>
		<!-- Databases Drop Down from JavaScript -->
	</select>
	</div>
	
	<div class="form-group col-md-5 col-md-offset-8">
	<label for="tb">Tables:</label>
	<select class="form-control" id="tb" onchange="fetchColumns();">
		<option>--Default--</option>
		<!-- Tables Drop Down from JavaScript -->
	</select>
	</div>
	
	<div class="form-group col-md-5 col-md-offset-8">
	<label for="cl">Columns:</label>
	<select class="form-control" id="cl" onchange="enableAdd()">
		<option>--Default--</option>
		<!-- Columns Drop Down from JavaScript -->
	</select>
	</div>
	
	<div class="form-group  col-md-5 col-md-offset-8">
		<button onclick="addColumn()" class="btn btn-primary" id="addColumn" disabled>Add Column</button>
	</div>
	
	<div class="container">
		<!-- Column Values from JavaScript -->
		<table class = "table table-striped table-bordered" id="columnValues">
			<thead>
 				<tr>
    				<th>Table Name</th>
				    <th>Column Name</th>
				    <th>Where</th>
				    <th>Value</th>
				    <th>Aggregate</th>
				    <th>Group By</th>
				    <th></th>
 				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	
	<!-- Show where -->
	<div class="container">
		<h6>Where Condition</h6>
		<div  id="whereString">
		</div>
	</div>
	
	<div class="form-group  col-md-5 col-md-offset-8">
		<button onclick="showWhere()" class="btn btn-primary" id="showWhere" disabled>Build Where</button>
	</div>
	
	<div class="form-group  col-md-5 col-md-offset-8">
		<button onclick="saveColumns()" class="btn btn-primary" id="saveColumns" disabled>Show Table</button>
	</div>
	
	<div class="container">
		<table id="resultTable" class="table table-striped table-bordered"></table>
	</div>
	
</body>
</html>
