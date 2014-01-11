<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<script src="jsfile/jquery-1.9.1.js"></script>
	<link href="css/bootstrap.min.css" rel="stylesheet">
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/converter.js"></script>
     <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	     if (!$('#conveter').hasClass('active')) {
	 		$('#conveter').addClass('active');
	 	}
	     });
	</script>
<title>Timezone Converter</title>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container col-sm-offset-3">
		<div class="header">
			<div class="jumbotrons">
	    		<h1>Timezone Converter</h1>
	    		<p>Please enter your timezone and time:</p>
	    	</div>
    	</div>
    	<div class="container">
	    			 <span id="loginFailed" style="color: red"></span>
	    			 <div class=row>
	    			 	<div class="form-group ">
	    			 	<div class="col-sm-3">
	    			 		<label for="time1">Time:</label>
		    					<input type="time" placeholder="HH:MM" class="form-control" name="time1" id="time1">
		    					<span id="time1Span" class="help-block"></span>
		    				</div>
		    				<div class="col-sm-3">
		    				<label for="time2">Time:</label>
		    				<input type="time" placeholder="HH:MM" class="form-control" name="time2" id="time2">
		    				<span id="time2Span" class="help-block"></span>
		    				</div>
		    			</div> 
		    		</div>
		    		<div class=row>
		    			<div class="form-group">
	    			 	<div class="col-sm-3">
	    			 		<label for="timezone1">Timezone:</label>
		    				<input type="text" placeholder="Timezone" class="form-control" name="timezone1" id="timezone1">
		    				<span id="timezone1Span" class="help-block"></span>
		    				</div>
		    				<div class="col-sm-3">
	    			 		<label for="timezone2">Timezone:</label>
		    					<input type="text" placeholder="Timezone" class="form-control" name="timezone2" id="timezone2">
		    					<span id="useridSpan" class="help-block"></span>
		    				</div>
		    			</div> 
		    		</div>
    			</div>
    		</div>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>