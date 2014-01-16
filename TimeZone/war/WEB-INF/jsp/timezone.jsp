<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 
<title>Timezone</title>
	<!--- CSS --->
	<script src="jsfile/jquery-1.9.1.js"></script>
	<link href="css/bootstrap.min.css" rel="stylesheet">
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/timezone.js"></script>
     <script src="jsfile/bootstrap-formhelpers-phone.js"></script>
     <script type="text/javascript">
     function date_time(id, offset){
			date = new Date();
			date=new Date(date.getTime()+offset).toUTCString();
		    document.getElementById(id).innerHTML = date;
		    setTimeout('date_time("'+id+'",'+offset+');','1000');
		 }
     </script>
     <style>
     	.input-group-addon{
     		padding: 0px 0px;
 			border-radius: 0px;
     	}
     </style>
</head>
<body>
	<%
	 response.setHeader("Cache-Control", "no-cache");
	 response.setHeader("Cache-Control", "no-store");
	 response.setDateHeader("Expires", 0);
	 response.setHeader("Pragma", "no-cache");
	 if (session.getAttribute("key") == null ) {
	  response.sendRedirect("/login");
	 }
	%>
	<jsp:include page="header.jsp"></jsp:include>
		<div class="container" >
		<ul class="nav nav-tabs" id="myTab">
 			<li class="active"><a href="#searchByPlace" data-toggle="tab">Search by Place</a></li>
  			<li><a href="#searchByPhone" data-toggle="tab">Search by Phone Number</a></li>
		</ul>
		<div class='tab-content'>
			<div id='searchByPlace' class='tab-pane active'>
				<h1>Please Select Country, State and City to get time of particular place.</h1>
				<form class='form' id='searchForm'>
					<div class='row'>
						<div class=col-sm-4>
							<label for='country'>Country:</label>
							<select id='country' class=form-control></select> 
							<span id='countrySpan' class='help-block'></span>
						</div>
					</div>
					<span></span>
					<div class='row'>
						<div class=col-sm-4>
							<label for='state'>State:</label>
							<select id=state class=form-control></select>
							<span id='stateSpan' class='help-block'></span>
						</div>
					</div>
				
					<div class='row'>
						<div class=col-sm-4>
						<label for='state'>City:</label>
						<select id=city class=form-control></select>
						<span id='citySpan' class='help-block'></span>
						</div>
					</div>
					
					<input type='hidden' id='keyString' value='${key}'>
					<div class='row'>
					<div class=col-sm-4>
					<button type='submit' id='seachButton' class='btn btn-success form-control'>Search</button>
					</div>
					</div>
				</form>
	</div>
	<div id='searchByPhone' class='tab-pane'>
	<h1>Please enter Phone Number to get time of particular place.</h1>
		<form id=serachByPhone class=form>
			<div class='row'>
				<div class=col-sm-4>
					<label for='phone'>Phone Number:</label>
					<div class="input-group">
  						<span class="input-group-btn"><input type="text" style="width: 70px; border-radius:3px" value="+91" class="form-control bfh-phone" data-format="+ddd"></span>
  						<input type="text" class="form-control bfh-phone" value="55555555555" data-format="(ddd) ddd-dddd" id='phoneNumber'>
						</div>
					<span id='countrySpan' class='help-block'></span>
				</div>
			</div>
			<input type='hidden' id='keyString' value='${key}'>
			<div class='row'>
				<div class=col-sm-4>
					<button type='submit' class='btn btn-success form-control'>Search</button>
				</div>
			</div>
		</form>
	</div>
	</div>
	</div>
	<br>
	<div class="container">
	    <div class="row">
	    	<div class="col-lg-9">
	    		<div class="panel  panel-default">
	    			<div class="panel-body">
	    			<div class="page-header" id=resultHeader>
	    			
	    			</div>
	    				<div id="result">
							
						</div>
	    			</div>
	    		</div>
	    	</div>
	    </div>
    </div>
		
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>