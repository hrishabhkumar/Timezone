<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Welcome</title>

<%if(session.getAttribute("key")==null){
		response.sendRedirect("/login.html");
	}%> 
	<link href="css/bootstrap.min.css" rel="stylesheet">
	<script src="jsfile/jquery-1.9.1.js"></script>
     <script src="js/bootstrap.min.js"></script>
     <script src="jsfile/home.js"></script>
     <script type="text/javascript">
     function getClock(rawOffset, id)
 	{
 		var date=new Date();
 		date=new Date(date.getTime()+rawOffset);
 		$(id).empty();
 		$(id).html(date.toUTCString());
 	    setTimeout('getClock('+rawOffset+',"'+ id+'")','1000');
 	}
     </script>
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
	
	
		<div class="container">
		<div class=row>
			<div class="col-lg-8">
			<div class="form-horizontal" role="form">
				<div class="form-group">
		    		<label class="col-sm-3 control-label">FullName :</label>
					<div class="col-sm-9">
		     			<p class="form-control-static">${userid}</p>
		     		</div>
		     	</div>
		     	<div class="form-group">
		    		<label class="col-sm-3 control-label">Email :</label>
					<div class="col-sm-9">
		     			<p class="form-control-static">${userid}</p>
		     		</div>
		     	</div>
		     	<%if(session.getAttribute("lastLogin")!=null){ %>
			     	<div class="form-group">
			    		<label class="col-sm-3 control-label">Last Login Time :</label>
						<div class="col-sm-9">
			     			<p class="form-control-static">${lastLogin}</p>
			     		</div>
			     	</div>
				<%} %>
				<div class="form-group">
			    	<label class="col-sm-3 control-label">Your Current City :</label>
					<div class="col-sm-9">
			     		<p class="form-control-static" id=city></p>
			     	</div>
			    </div>
			    <div class="form-group">
			    	<label class="col-sm-3 control-label">Your Current State :</label>
					<div class="col-sm-9">
			     		<p class="form-control-static" id=state></p>
			     	</div>
			    </div>
			    <div class="form-group">
			    	<label class="col-sm-3 control-label">Your Current Country :</label>
					<div class="col-sm-9">
			     		<p class="form-control-static" id=country></p>
			     </div>
			     </div>
			     <div class="form-group">
			    	<label class="col-sm-3 control-label">Your Current Time :</label>
					<div class="col-sm-9">
			     		<p class="form-control-static" id=time></p>
			     </div>
			    </div>
			</div>
		</div>
	</div>
	</div>		
		<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>