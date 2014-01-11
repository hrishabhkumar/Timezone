$(document).ready(function(){
     $('#passwordSubmint').attr("disabled", true);
     $('#userid').attr("disabled", true);
     function conPassvalidation(){
    	 var newPass=$('#newPass').val();
    	 var conPass=$('#confirmPass').val();
    	 if(newPass==conPass&&newPass.length>=6){
    		 $('#passwordSubmint').attr("disabled", false);
    		 $('#conPassSpan').html("Hmm!!It seems you have remembered first one.").css("color","green");
    	 }
    	 else{
    		 $('#passwordSubmint').attr("disabled", true);
    		 $('#conPassSpan').html("ohh!! You haven't remembered first one?").css("color","red")
    	 }
     	}    	 
     $('#confirmPass').on({keyup: function(){
    	 conPassvalidation();
     }
     });
     $('#newPass').on({keyup: function(){
    	 var newPass=$('#newPass').val();
    	 if(newPass.length>=6){
    		 if($('#confirmPass').val()==""||$('#confirmPass').val()==null){
    		 	$('#newPassSpan').html("Hmm!!now its ok.").css("color","green");
    		 	$('#oldPassSpan').empty();
    		 }
    		 else{
    			 conPassvalidation();
    			 $('#newPassSpan').html("Hmm!!now its ok.").css("color","green");
    		 }
    	 }
    	 else{
    		 if($('#confirmPass').val()==""||$('#confirmPass').val()==null){
	    		 $('#passwordSubmint').attr("disabled", true);
	    		 $('#newPassSpan').html("ohh!! Password should have atleast 6 digits").css("color","red");
	    		 $('#oldPassSpan').empty();
    	 	}
    	 	else{
	    		 $('#passwordSubmint').attr("disabled", true);
	    		 $('#newPassSpan').html("ohh!! Password should have atleast 6 digits").css("color","red");
	    		 $('#oldPassSpan').html("ohh!! Password should have atleast 6 digits").css("color","red");
    		}
    		 
    	 }
     	}
     });
 });