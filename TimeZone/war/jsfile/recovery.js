$(document).ready(function(){
	$('#recoverySubmit').attr("disabled", true);
	function validateEmail(){
		var regemail = /^\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
		var email=$ ('#recoveryEmail').val();
		if(regemail.test(email)){
			$('#useridSpan').empty();
			return true;
		}
		else{
			$('#useridSpan').html("User Name must be a email address").css("color", "red");
			return false;
		}
	}
	function checkUser(){
		if(validateEmail()){
			var dataString="userid="+$('#recoveryEmail').val();
			$.ajax({
				url: "searchuser",
				type: "post",
				data: dataString,
				success : function(data){
					if(data=="found"){
						$("#useridSpan").html("you are registred. So you can proceed now.").css("color", "green");
					    $('#recoverySubmit').attr("disabled", false);
					}
					else{
						$("#useridSpan").html("This is not yet registered").css("color", "red");
						$('#recoverySubmit').attr("disabled", true);
					}
				}	
			});
		}
		else{
			$('#recoverySubmit').attr("disabled", true);
		}
	}
	$('#recoveryEmail').on({
	    keyup: function(){
		checkUser();
	 }
	});
	$('#recoveryEmail').on({
	    change: function(){
		checkUser();
	 }
	});
	
	
	$('#recoveryForm').submit(function(event){
		event.preventDefault();
		if(validateEmail()){
			var dataString=	{
					userid: $('#recoveryEmail').val()
			};
			dataString=JSON.stringify(dataString);
			$.ajax({
				url: "initiate",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: false,
				cache: false,
				processData:false,
				success: function(data){
					if(data.status=="success"){
						$("#useridSpan").html(data.msg).css("color", "green");
					}
					else{
						$("#useridSpan").html(data.msg).css("color", "red");
					}
				}
			});
		}
	});
});