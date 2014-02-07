$(document).ready(function(){
	$('#logout').click(function(event){
		var leave=confirm("Do you want to logout?");
		if(leave){
			event.preventDefault;
			window.location.assign("/logout.html");
		}
		else{
			event.preventDefault();
		}
	});
	var zip;
		$( "#timezone1" ).autocomplete({
			minLength: 3,
			source:function (request, response) {
				zip=null;
	            var dataString={
	            	term: $.ui.autocomplete.escapeRegex(request.term)
	            };
	            dataString=JSON.stringify(dataString);
	            console.log($.ui.autocomplete.escapeRegex(request.term));
	            $.ajax({
					url: "timezonebycity",
					type: "post",
					dataType: "json",
					contentType: "application/json",
					data: dataString,
					async: true,
					cache: true,
					processData:false,
					success: function(data){
						$('#timezone1Span').empty();
						$('#timezone1').removeClass("ui-autocomplete-loading");
						var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
				          response( $.grep( data, function( item ){
				              return (matcher.test( item.city))+(matcher.test( item.state))+(matcher.test( item.country));
				          }) );
					},
	            	error: function(data){
	            		console.clear();
	            		$('#timezone1Span').html("city not found").css("color", "red");
	            		$('#timezone1').removeClass("ui-autocomplete-loading");
	            	}
	            })
			},
			select: function( event, ui ) {
				$( "#timezone1" ).val(ui.item.city);
				zip=ui.item.zip;
				event.preventDefault();
			}
		})
	    .data( "ui-autocomplete" )._renderItem = function( ul, item ) 
		    {
		      return $( "<li>" )
		        .append( "<a>"+item.city + "<i class='glyphicon bfh-flag-"+item.countryCode+" pull-right'></i><br>" + item.state+", "+item.country+ "</a></li>" )
		        .appendTo( ul );
		    };
	    $( "#timezone2" ).autocomplete({
			minLength:3,
			source:function (request, response) {
				zip=null;
	            var dataString={
	            	term: $.ui.autocomplete.escapeRegex(request.term)
	            };
	            dataString=JSON.stringify(dataString);
	            console.log($.ui.autocomplete.escapeRegex(request.term));
	            $.ajax({
					url: "timezonebycity",
					type: "post",
					dataType: "json",
					contentType: "application/json",
					data: dataString,
					async: true,
					cache: true,
					processData:false,
					success: function(data){
						$('#timezone2Span').empty();
						$('#timezone2').removeClass("ui-autocomplete-loading");
						var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
				          response( $.grep( data, function( item ){
				              return (matcher.test( item.city))+(matcher.test( item.state))+(matcher.test( item.country));
				          }) );
					},
	            	error: function(data){
	            		console.clear();
	            		$('#timezone2Span').html("city not found").css("color", "red");
	            		$('#timezone2').removeClass("ui-autocomplete-loading");
	            	}
	            })
			},
			select: function( event, ui ) {
				$( "#timezone2" ).val(ui.item.city);
				event.preventDefault();
			}
		})
	    .data( "ui-autocomplete" )._renderItem = function( ul, item ) 
		    {
		      return $( "<li>" )
		        .append( "<a>"+item.city + "<i class='glyphicon bfh-flag-"+item.countryCode+" pull-right'></i><br>" + item.state+", "+item.country+ "</a></li>" )
		        .appendTo( ul );
		    };
	  
	
		function converter(src, dest){
			var hour=$('#time'+src).val().split(':')[0];
			var min=$('#time'+src).val().split(':')[1];
			if($('#time'+src).val()!=='')
			{
				var raw_offset_origin=$( "#timezone"+src+"-offset" ).val();
				var raw_offset_dest=$( "#timezone"+dest+"-offset" ).val();
				var total_offset=parseInt(raw_offset_dest)-(parseInt(raw_offset_origin));
				var input_miliseconds=(parseInt(hour)*60*60*1000)+(parseInt(min)*60*1000);
				var date=new Date();
				var localOffset = date.getTimezoneOffset()*60000;
				var date=new Date(input_miliseconds+localOffset+total_offset);
				hour=date.getHours();
				min=date.getMinutes();
				if(min<10){
					min="0"+min
				}
				if(hour<10){
					hour="0"+hour
				}
				if(date.getDate()==1)
				{
					$('#time'+src+'Span').empty();
					$('#time'+dest+'Span').html("Today");
				}
				else if(date.getDate()==2)
				{
					$("#time"+src+"Span").empty();
					$("#time"+dest+"Span").html("Tommorow");
				}
				else if(date.getDate()==31)
				{
					$("#time"+src+"Span").empty();
					$("#time"+dest+"Span").html("YesterDay");
				}
				$('#time'+dest).val(hour+":"+min+":00");
		//		if(hour==0){
		//			hour=12;
		//			$('#time'+dest).val(hour+":"+min+" AM");
		//		}
		//		else if(hour<12){
		//			$('#time'+dest).val(hour+":"+min+" AM");
		//		}
		//		else if(hour==12){
		//			$('#time'+dest).val(hour+":"+min+" PM");
		//		}
		//		else{
		//			hour-=12;
		//			$('#time'+dest).val(hour+":"+min+" PM");
		//		}
			}
		}
		$('#time1').change(function(){
			converter(1,2)
		});
		$('#time2').change(function(){
			converter(2,1)
		});
		$('#timezone1').on( "autocompleteselect", function( event, ui ) {
			$.ajax({
				url: "/timezone?zipcode="+ui.item.zipCode,
				dataType: "json",
				success: function(data){
					place=data.data;
					$('#timezone1-offset').val(place[0].rawOffset);
					converter(2,1);
				}
			});
		});
		$('#timezone2').on( "autocompleteselect", function( event, ui ) {
			$.ajax({
				url: "/timezone?zipcode="+ui.item.zipCode,
				dataType: "json",
				success: function(data){
					place=data.data;
					console
					$('#timezone2-offset').val(place[0].rawOffset);
					converter(1,2);
				}
			});
			
		});
		$('#time1').focus();
});