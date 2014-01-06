<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<%if(session.getAttribute("key")!=null){
		response.sendRedirect("/home.html");
	}%> 
<title>Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!--- CSS --->
	<script src="jsfile/jquery-1.9.1.js"></script>
	<link href="css/bootstrap.min.css" rel="stylesheet">
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/script.js"></script>
	<script type="text/javascript">
	$(document).ready(function(){
	     if (!$('#login').hasClass('active')) {
	 		$('#login').addClass('active');
	 	}
	     });
	</script>
</head>
<body>
	<jsp:include page="header.jsp"></jsp:include>
	<div class="container">
		<div class="header">
    			<h1>Sign In</h1>
    			</div>
    			<div class="container">
    				If you are already registered please enter:
    				
	    			<form action="#" class="form-horizontal" role=form id="loginForm">
	    			 	<span id="loginFailed" style="color: red"></span>
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
					        <button type="submit" class="btn btn-success">Sign in</button>
					        <button type="reset" class="btn btn-default">Reset</button>
					        </div>
					    </div>
				    </form>
				    <!--  
				    	Or Login with:
				    	<div class="form-group">
				    	<a  class="btn btn-primary" id="facebook" href="https://www.facebook.com/dialog/oauth?client_id=380368472106874&response_type=code&redirect_uri=http://springbyhrishabh.appspot.com/outh2facebook&scope=publish_stream,email">Facebook</a>
				    	<a type="button" class="btn btn-danger " href="https://accounts.google.com/o/oauth2/auth?redirect_uri=http://springbyhrishabh.appspot.com/outh2callback
				&response_type=code&client_id=854504709839-g6h8lb5pidkqqu6gk5g9qgeup3cphv5p.apps.googleusercontent.com
				&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile&approval_prompt=force&access_type=offline">Google</a>
				    	<p class="help-block">If You don't have these then <a href="/register.html" >Register Here</a></p>
				    </div>-->
    			</div>
    		</div>
    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>