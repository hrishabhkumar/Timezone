<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
 <link rel="stylesheet" href="css/jquery-ui.css">
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link rel="stylesheet" href="css/bootstrap-formhelpers.css">
<script src="jsfile/jquery-1.9.1.js"></script>
 <script src="js/jquery-ui.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="jsfile/converter.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
	     if (!$('#conveter').hasClass('active')) 
	     {
	    	 $('#conveter').addClass('active');
	     }
	     <%
		 response.setHeader("Cache-Control", "no-cache");
		 response.setHeader("Cache-Control", "no-store");
		 response.setDateHeader("Expires", 0);
		 response.setHeader("Pragma", "no-cache");
		 if (session.getAttribute("key") == null ) {
		  response.sendRedirect("/login.html");
		 }
		%>
	});
	</script>
	
<style>
       .ui-autocomplete {
            max-height: 200px;
            overflow-y: auto;
            /* prevent horizontal scrollbar */
            overflow-x: hidden;
            
        } 
        .ui-autocomplete-loading {
    		background: white url('img/loader.gif') right center no-repeat;
  		 }
  		
</style>
<title>Timezone Converter</title>
</head>
<body>

	<jsp:include page="header.jsp"></jsp:include>
	<div class="container ">
	
		<div class="header">
			<div class="jumbotrons">
	    		<h1>Timezone Converter</h1>
	    		<p>Please enter your timezone and time:</p>
	    	</div>
    	</div>
    	
    	<div class="container">
    
	    			 <div class=row>
	    			 	<div class="form-group ">
	    			 	<div class="col-sm-3">
	    			 		<label for="time1">Time1:</label>
		    					<input type="time" placeholder="HH:MM" class="form-control" name="time1" id="time1" tabindex="1">
		    					<span id="time1Span" class="help-block"></span>
		    				</div>
		    				<div class="col-sm-3">
		    				<label for="time2">Time2:</label>
		    				<input type="time" placeholder="HH:MM" class="form-control" name="time2" id="time2" tabindex="3">
		    				<span id="time2Span" class="help-block"></span>
		    				</div>
		    			</div> 
		    		</div>
		    		<div class=row>
		    			<div class="form-group">
	    			 	<div class="col-sm-3">
	    			 		<label for="timezone1">Timezone1:</label>
		    				<input type="text" placeholder="Timezone" class="form-control" name="timezone1" id="timezone1" tabindex="2">
		    				<input type="hidden" id="timezone1-offset">
		    				<span id="timezone1Span" class="help-block"></span>
		    				</div>
		    				<div class="col-sm-3">
	    			 		<label for="timezone2">Timezone2:</label>
		    					<input type="text" placeholder="Timezone" class="form-control" name="timezone2" id="timezone2" tabindex="4">
		    					<input type="hidden" id="timezone2-offset">
		    					<span id="timezone2Span" class="help-block"></span>
		    				</div>
		    			</div> 
		    		</div>
    			</div>
    			</div>
    		
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>