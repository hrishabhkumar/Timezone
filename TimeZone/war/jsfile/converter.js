$(document).ready(function(){
	var timezonelist;
	function getTimezonenames(){
		var dataString={
				required: "timezonenames"
				};
		dataString=JSON.stringify(dataString);
		console.log(dataString) ;
		$.ajax({
			url: "converter",
			type: "post",
			dataType: "json",
			contentType: "application/json",
			data: dataString,
			async: false,
			cache: false,
			processData:false,
			success: function(data){
				alert(data.list)
				timezonelist=data.list;
		      },
		    error: function(data){
		    }
		});
	}
	
	$(function() {
	    alert( getTimezonenames());
	    $( "input:text" ).autocomplete({
	      source: timezonelist
	    });
	  });
});