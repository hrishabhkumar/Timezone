<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%if(session.getAttribute("key")!=null){
		response.sendRedirect("/home.html");
	}%> 
<title>Recover Password</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!--- CSS --->
	<script src="../jsfile/jquery-1.9.1.js"></script>
	<link href="../css/bootstrap.min.css" rel="stylesheet">
     <script src="../js/bootstrap.min.js"></script>
     <script src="../jsfile/recovery.js"></script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<div class="header">
    			<h1>Password recovery</h1>
    			</div>
    			<div class="container">
					Please enter your userid/email to recover your password    				
	    			<form action="#" class="form-horizontal" role=form id="recoveryForm">
	    			 	<span id="recoveryStatus"></span>
	    			 	<div class="form-group">
	    			 		<div class="col-xs-4">
	    			 			<label for="username">User ID:</label>
		    					<input type="text" placeholder="Email" class="form-control" name="userid" id="recoveryEmail">
		    					<span id="useridSpan" class="help-block"></span>
		    				</div>
		    			</div> 
					    <div class="form-group">
					    <div class="col-xs-4">
					        <button type="submit" class="btn btn-success" id=recoverySubmit>Send email</button>
					        <button type="reset" class="btn btn-default">Reset</button>
					        </div>
					    </div>
				    </form>
				    <div class="form-group">
						<p class="help-block">The recovery URL will be sent to your email address.</p>
					</div>
    			</div>
    		</div>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>