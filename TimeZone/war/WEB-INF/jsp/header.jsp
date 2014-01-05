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
			    	<%if(session.getAttribute("key")!=null){ %><li id="home"><a href="/home.html">Home</a></li><%} %>
			    	<%if(session.getAttribute("key")==null){ %><li id="login"><a href="/login.html" >Login</a></li><%} %>
			    	<%if(session.getAttribute("key")==null){ %><li id=register><a href="/register.html" >Register</a></li><%} %>
				   <%if(session.getAttribute("key")!=null){ %> <li id=timezone><a href="/timezone.html">Timezone</a></li><%} %>
				   <%if(session.getAttribute("key")!=null){ %> <li id=logout><a href="/logout.html">Logout</a></li><%} %>
			    </ul>
	    
	    	</div>
	    </div>
    </div>
     