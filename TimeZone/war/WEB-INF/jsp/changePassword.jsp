<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Change Password</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!--- CSS --->
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<!-- Java Script -->
	<script src="jsfile/jquery-1.9.1.js"></script>
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/changePassword.js"></script>
     
</head>
<body>
	<%
	 response.setHeader("Cache-Control", "no-cache");
	 response.setHeader("Cache-Control", "no-store");
	 response.setDateHeader("Expires", 0);
	 response.setHeader("Pragma", "no-cache");
	 if(session.getAttribute("recover")==null){
		 response.sendRedirect("/login");
	 }
	%>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<div class="header">
    			<h1>Password recovery</h1>
    			<span style="color: red">${changefail}</span>
    			</div>
    			<div class="container">
					Please enter your userid/email to recover your password    				
	    			<form action=passwordchange class="form-horizontal" role=form id="recoveryForm" method="post">
	    			 	<span id="recoveryStatus"></span>
	    			 	<div class="form-group">
	    			 		<div class="col-xs-4">
	    			 			<label for="userid">User ID:</label>
		    					<input type="text" placeholder="Email" class="form-control" value='${userid}' name="userid" id="userid">
		    					<span id="useridSpan" class="help-block"></span>
		    				</div>
		    			</div> 
	    			 	<div class="form-group">
	    			 		<div class="col-xs-4">
	    			 			<label for="newPass">New password:</label>
		    					<input type="password" placeholder="New Password" class="form-control" name="pass" id="newPass">
		    					<span id="newPassSpan" class="help-block"></span>
		    				</div>
		    			</div> 
		    			<div class="form-group">
	    			 		<div class="col-xs-4">
	    			 			<label for="confirmPass">Confirm password:</label>
		    					<input type="password" placeholder="confirm Password" class="form-control" name="confirmPass" id="confirmPass">
		    					<span id="conPassSpan" class="help-block"></span>
		    				</div>
		    			</div> 
					    <div class="form-group">
					    <div class="col-xs-4">
					        <button type="submit" class="btn btn-success" id=passwordSubmint>submit</button>
					        <button type="reset" class="btn btn-default">Reset</button>
					        </div>
					    </div>
				    </form>
    			</div>
    		</div>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>