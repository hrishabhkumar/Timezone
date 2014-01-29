$(document).ready(function(){
	function validateEmail(){
		var regemail = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		var email=$ ('#userid').val();
		
		if(regemail.test(email)){
			$('#useridSpan').empty();
			return true;
		}
		else{
			$('#useridSpan').html("User Name must be a email address").css("color", "red");
			return false;
		}
	}
	function validatePassword(){
		var pass=$ ('#password').val();
		if(pass.length>=6){
			$('#passwordSpan').empty();
			return true;
		}
		else{
			$('#passwordSpan').html("Password is too short.(length must be atleast 6)").css("color", "red");
			return false;
		}
	}
	$('#userid').on({
	    keyup: function(){
		validateEmail()
	}
	});
	$('#password').on({
	    keyup: function(){
		validatePassword()
	}
	});
	
	//Sending Login data and receiving Key and Status.
		$('#loginForm').submit(function(event){
			event.preventDefault();
			if(validateEmail()&&validatePassword()){
			var dataString={
					userid: $('#userid').val(),
					password: $('#password').val()
					};
			dataString=JSON.stringify(dataString);
			console.log(dataString) ;
			$('#loginStatus').addClass('loader');
			$.ajax({
				url: "login",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: true,
				cache: false,
				processData:false,
				success: function(data){
					if(data.key!=null){
						window.location.href = "/home";
					}
					else{
						$('#loginStatus').removeClass('loader');
						$('#loginStatus').fadeIn(5000);
						$('#loginStatus').html("Login Failed!!!! Please Check You Username and Password").addClass('failed');
						$('#loginStatus').fadeOut(5000);
						
					}
			      },
			    error: function(data){
			    }
			});
			}
			else{
				$('#loginStatus').fadeIn(5000);
				$('#loginStatus').html("Login Failed!!!! Please Check You Username and Password").addClass('failed');
				$('#loginStatus').fadeOut(5000);
			}
		});
		
	//Sending Registration data and receiving Key and Status.
	
		$('#registerForm').submit(function(event){
			event.preventDefault();
			if(validateEmail()&&validatePassword()){
				var dataString={
						userid: $('#userid').val(),
						password: $('#password').val()
						};
				dataString=JSON.stringify(dataString);
				$('#registerStatus').addClass('loader');
				$.ajax({
					url: "register",
					type: "post",
					dataType: "json",
					contentType: "application/json",
					data: dataString,
					async: true,
					cache: false,
					processData:false,
					success: function(data){
						$('#registerStatus').removeClass('loader');
						if(data.status=="success"){
							$('#registerStatus').html("Registration Successfull!!!! Please Wait while we redirect you to homepage.").addClass('success');
							$('#registerStatus').fadeOut(3000);
							window.setTimeout(function() {window.location.href = '/home';}, 1000);
						}
						else{
							$('#registerStatus').fadeIn(5000);
							$('#registerStatus').html(data.error).addClass('failed');
							$('#registerStatus').fadeOut(5000);
							
						}
				      },
				    error: function(data){
				    }
				});
			}
			else{
				$('#registerFailed').fadeIn(5000);
				$('#registerFailed').html("Registration Failed!!!! Please Check You Username and Password").css("color", "red");
				$('#registerFailed').fadeOut(5000);
			}
		});
		$('#userid').focus();
		
});