$(document).ready(function(){
	
	//Sending Login data and receiving Key and Status.
	$ (function(){
		$('#loginForm').submit(function(event){
			event.preventDefault();
			var dataString={
					userid: $('#userid').val(),
					password: $('#password').val()
					};
			
			dataString=JSON.stringify(dataString);
			console.log(dataString) ;
			$.ajax({
				url: "login",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: false,
				cache: false,
				processData:false,
				success: function(data){
					if(data.key!=null){
						window.location.href = "/home";
					}
					else{
						$('#loginFailed').fadeIn(5000);
						$('#loginFailed').text("Login Failed!!!! Please Check You Username and Password");
						$('#loginFailed').fadeOut(5000);
						
					}
			      },
			    error: function(data){
			    }
			});
		});
	});
	
	
	//Sending Registration data and receiving Key and Status.
	$ (function(){
		$('#registerForm').submit(function(event){
			event.preventDefault();
			var dataString={
					userid: $('#userid').val(),
					password: $('#password').val()
					};
			
			dataString=JSON.stringify(dataString);
			$.ajax({
				url: "register",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: false,
				cache: false,
				processData:false,
				success: function(data){
					if(data.status=="success"){
						alert("Registration Successfull!!!! Please Wait while we redirect you to homepage.");
						window.location.href = "/home";
					}
					else{
						$('#registerFailed').fadeIn(5000);
						$('#registerFailed').text("Registration Failed!!!! Please Check You Username and Password");
						$('#registerFailed').fadeOut(5000);
						
					}
			      },
			    error: function(data){
			    }
			});
		});
	});
});