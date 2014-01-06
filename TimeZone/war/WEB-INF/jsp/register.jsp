<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="jsfile/jquery-1.9.1.js"></script>
	<link href="css/bootstrap.min.css" rel="stylesheet">
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/script.js"></script>
<title>Register</title>
<script type="text/javascript">
$(document).ready(function(){
    if (!$('#register').hasClass('active')) {
		$('#register').addClass('active');
	}
    });
</script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<div class="header">
    			<h1>Register YourSelf</h1>
    			</div>
    			
    			<div class="container">
    			Please fill following details: 
	    			<form action="#"  class="form-horizontal" role=form id="registerForm">
	    			<span id=registerFailed></span>
	    			 	<div class="form-group">
	    			 	<div class="col-xs-4">
	    			 		<label for="username">User Name:</label>
		    					<input type="text" placeholder="Email" class="form-control" name="userid" id="userid">
		    					<span id="useridSpan" class="help-block"></span>
		    				</div>
		    			</div> 
		    			
		    			<div class="form-group">
		    			<div class="col-xs-4">
		    			
		    			<label for="password">Password:</label>
					        <input type="password" placeholder="Password" class="form-control" name="password" id="password">
					        <span id="passwordSpan" class="help-block"></span>
						</div>
						</div>
					    <div class="form-group">
					    <div class="col-xs-4">
					        <button type="submit" class="btn btn-success">Register</button>
					        <button type="reset" class="btn btn-default">Reset</button>
					        </div>
					    </div>
				    </form>
				    <p class="help-block">If You are already registered then <a href="/login.html" >login</a></p>
    			</div>
    		</div>
	
	 <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>