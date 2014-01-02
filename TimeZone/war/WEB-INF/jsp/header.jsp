<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <div class="navbar navbar-inverse navbar-static-top"> 
	    <div class="container">
		    <button class="navbar-toggle" data-toggle="collapse" data-target=".navHeaderCollapse">
			    <span class="icon-bar"></span>
			    <span class="icon-bar"></span>
			    <span class="icon-bar"></span>
		    </button>
		    <a href="/" class="navbar-brand">Timezone-Adaptavant</a>
		    <div class="collapse navbar-collapse navHeaderCollapse">
			    <ul class="nav navbar-nav navbar-right">
			    	<li class="active"><a href="/home.html">Home</a></li>
			    	<%if(session.getAttribute("key")==null){ %><li ><a href="/login.html" data-toggle="modal">Login</a></li><%} %>
			    	<%if(session.getAttribute("key")==null){ %><li ><a href="/register.html" data-toggle="modal">Register</a></li><%} %>
				   <%if(session.getAttribute("key")!=null){ %> <li><a href="/timezone.html">Timezone</a></li><%} %>
				   <%if(session.getAttribute("key")!=null){ %> <li><a href="/logout.html">Logout</a></li><%} %>
			    </ul>
	    
	    	</div>
	    </div>
    </div>
     