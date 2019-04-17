<html>
<head>
	<title>Data WareHouse | Privacy Filters</title>
	
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
	
	<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
	<script src="http://code.jquery.com/jquery-2.0.0.min.js" type="text/javascript"></script>
	
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
			
			$('#db').prop("disabled", true);
			var columnName = {dbname:dbname, tbname:tbname, clname:clname};
			columns.push(columnName);
			
			var dropDown = 	"<select class='form-control'><option value = '0'>--None--</option>" +
							"<option value = '1'>Sensitive</option>" +
							"<option value = '2'>Insensitive</option>" +
							"<option value = '3'>Identifying</option>" +
							"<option value = '4'>Quasi-Identifying</option>";

			var index = columns.indexOf(columnName);
							
			var crossIcon =	"<button type='button' class='close' aria-label='Close' onclick='removeColumn(" + index + ")'>" +
			  				"<span aria-hidden='true'>&times;</span>" +
			  				"</button>";
			  				
			var myString = "<tr id='selRow" + index + "'><td>" + tbname + "</td><td>" + clname + "</td><td>"+ dropDown +"</td><td>" + crossIcon + "</td><tr>";
			$('#columnValues > tbody:last').append(myString);
			$('#saveColumns').prop('disabled', false);
		}
		
		function removeColumn(index)
		{
			var row = document.getElementById("selRow" + index);
		    row.parentNode.removeChild(row);
		    
			//var index = columns.indexOf(columnName);
			if (index > -1)
			{
				columns.splice(index, 1);
			}
			if(columns.length == 0)
			{
				$('#saveColumns').prop('disabled', true);
				$('#db').prop("disabled", false);
			}
		}
		
		function saveColumns()
		{
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
					//alert('success');
					printTable(result)
		        },
		        
				error: function (err) {
					alert(err.status);
				}
		    });
		}
		
		function printTable(result)
		{
			var myString = "<table class = 'table table-striped table-bordered'><thead><tr>";
			for(var i = 0; i < columns.length; i++)
			{
		 		myString = myString + "<th>" + columns[i].clname + "</th>";
			}
			myString = myString + "</tr></thead><tbody>";
			for(var i = 0; i < result.length; i++)
			{
		 		myString = myString + "<tr>";
		 		for(var j = 0; j < result[i].length; j++)
		 		{
					myString = myString + "<td>" + result[i][j] + "</td>";
		 		}
		 		myString = myString + "</tr>";
			}
			myString = myString + "</tbody></table>";
			//alert(myString);
			
			$('#resultTable').html(myString);
		}
		
		function enableAdd()
		{
			$('#addColumn').prop('disabled', false);
		}
</script>
	
</head>
<body>
	<div class="form-group col-md-5 col-md-offset-8">
	<label for="db">Databses:</label>
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
				    <th>Privacy Filter</th>
				    <th></th>
 				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	
	<div class="form-group  col-md-5 col-md-offset-8">
		<button onclick="saveColumns()" class="btn btn-primary" id="saveColumns" disabled>Save Columns</button>
	</div>
	
	<div class="container" id="resultTable"></div>
	
</body>
</html>
