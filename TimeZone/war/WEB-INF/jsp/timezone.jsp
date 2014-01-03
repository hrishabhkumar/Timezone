<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 <%if(session.getAttribute("key")==null){
		response.sendRedirect("/login.html");
	}%> 
<title>Timezone</title>
	<!--- CSS --->
	<script src="jsfile/jquery-1.9.1.js"></script>
	<link href="css/bootstrap.min.css" rel="stylesheet">
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/timezone.js"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
		<div class="container" >
			<h1>Please Select Country, State and City to get time of particular place.</h1>
			<div>
				<form class='form' id="searchForm">
					<div class="row">
						<div class=col-sm-4>
							<label for="country">Country:</label>
							<select id="country" class=form-control>
							</select> 
						</div>
					</div>
					
					<div class="row">
						<div class=col-sm-4>
							<label for="state">State:</label>
							<select id=state class=form-control>
							</select>
						</div>
					</div>
				
					<div class="row">
						<div class=col-sm-4>
						<label for="state">City:</label>
						<select id=city class=form-control>
						
						</select>
						</div>
					</div>
					
					<input type="hidden" id="keyString" value='${key}'>
					<br>
					<div class=col-sm-4>
					<button type="submit" id="seachButton" class="btn btn-success">Search</button>
					</div>
				</form>
	</div>
	
	<div class="container">
	    <div class="row">
	    	<div class="col-lg-9">
	    		<div class="panel  panel-default">
	    			<div class="panel-body">
	    			<div class="page-header">
	    			
	    			</div>
	    				<div id="searchResult">
							<span></span>
						</div>
	    			</div>
	    		</div>
	    	</div>
	    </div>
    </div>
		</div>
	<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>