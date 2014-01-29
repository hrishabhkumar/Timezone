<%@page import="org.springframework.ui.ModelMap"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Login</title>
<meta http-equiv=Content-Type content="text/html; charset=utf-8"/>
<meta name=viewport content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.min.css" rel=stylesheet>
<script src="jsfile/jquery-1.9.1.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="jsfile/script.js"></script>
<script>$(document).ready(function(){if(!$('#login').hasClass('active')){$('#login').addClass('active');}});</script>
<style>.loader{position:fixed;left:0px;top:0px;width:100%;height:100%;z-index:9999;background:transparent url('img/page-loader.gif') 50% 50% no-repeat rgb(249,249,249);}
.failed{color:red;}
.success{color:green;}</style>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<div class=container id=container>
<div class=col-sm-offset-4>
<div class=header>
<span style="color: red"></span>
<span style="color: green"></span>
<h1>Sign In</h1>
</div>
If you are already registered please enter:
<form action="#" class="form-horizontal " role=form id=loginForm>
<span id=loginStatus></span>
<div class=form-group>
<div class=col-sm-4>
<label for=username>User Name:</label>
<input placeholder=Email class=form-control name=userid id=userid>
<span id=useridSpan class=help-block></span>
</div>
</div>
<div class=form-group>
<div class=col-sm-4>
<label for=password>Password:</label>
<input type=password placeholder=Password class=form-control name=password id=password>
<span id=passwordSpan class=help-block></span>
</div>
</div>
<div class=form-group>
<div class="col-sm-4 ">
<button type=submit class="btn btn-success">Sign in</button>
<button type=reset class="btn btn-default">Reset</button>
</div>
</div>
</form>
<div class=form-group>
<p class=help-block><a href="/recover/initiate">Forgot your password?</a></p>
</div>
</div>
</div>
 <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>