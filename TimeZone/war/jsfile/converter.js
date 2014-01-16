$(document).ready(function(){
	$(function() {
		var datalist;
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
				datalist=data.list;
		      },
		    error: function(data){
		    }
		});
		$( "#timezone1" ).autocomplete({
			minLength: 2,
			source:datalist,
			focus: function( event, ui ) {
				$( "#timezone1" ).val( ui.item.label );
				$( "#timezone1-offset" ).val( ui.item.value);
				return false;
			},
			select: function( event, ui ) {
				$( "#timezone1" ).val( ui.item.label );
				$( "#timezone1-offset" ).val( ui.item.value);
				return false;
			},
			change: function( event, ui ) {
				$( "#timezone1" ).val( ui.item.label );
				$( "#timezone1-offset" ).val( ui.item.value);
				return false;
			}
	    });
//	    .data( "ui-autocomplete" )._renderItem = function( ul, item ) 
//	    {
//	      return $( "<li>" )
//	        .append( "<a>" + item.label + "<br>" + item.desc + "</a>" )
//	        .appendTo( ul );
//	    };
	    $( "#timezone2" ).autocomplete({
			minLength: 2,
			source:datalist,
			focus: function( event, ui ) {
				$( "#timezone2" ).val( ui.item.label );
				$( "#timezone2-offset" ).val( ui.item.value );
				return false;
			},
			select: function( event, ui ) {
				$( "#timezone2" ).val( ui.item.label );
				$( "#timezone2-offset" ).val( ui.item.value );
				return false;
			},
			change: function( event, ui ) {
				$( "#timezone2" ).val( ui.item.label );
				$( "#timezone2-offset" ).val( ui.item.value);
				return false;
			}
	    });
//	    .data( "ui-autocomplete" )._renderItem = function( ul, item ) {
//	      return $( "<li>" )
//	        .append( "<a>" + item.label + "<br>" + item.desc + "</a>" )
//	        .appendTo( ul );
//	    };
	    
	  });
	
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
			converter(2,1);
		});
		$('#timezone2').on( "autocompleteselect", function( event, ui ) {
			converter(1,2);
		});
		$('#timezone1').on( "autocompletefocus", function( event, ui ) {
			converter(2,1);
		});
		$('#timezone2').on( "autocompletefocus", function( event, ui ) {
			converter(1,2);
		});
		$('#time1').focus();
});