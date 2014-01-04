$(document).ready(function(){
	 function ajax_call(name, dataString) {
		 var id='#'+name;
		 $.ajax({
				url: "getList",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: true,
				cache: false,
				processData:false,
				success: function(data){
					if(data.status=="success"){
						var listdata=data.list;
						if(listdata.length!=0){
							var output='<option selected=true>select '+name+'</option>';
							for (var i in listdata) {
								output+='<option value="'+listdata[i]+'">'+listdata[i]+'</option>';
							}
							$(id).html(output);
						}
					}
					else{
						$('#searchResult').html("<h1>Please wait...</h1>");
					}
			      },
			    error: function(data){
			    }
			});
	    }
	$(function(){
		
	var str="country";	
	var dataString={
			required: str
	};
	dataString=JSON.stringify(dataString);
	console.log(dataString);
	ajax_call('country', dataString);
	});
	
	//Getting State List
	$('#country').change(function(){
		$('#city').empty();
		var str="state";
		var dataString={
				"country":$('#country option:selected').text(),
				"required":str	
			};
			dataString=JSON.stringify(dataString);
			ajax_call('state', dataString);
	});
			
		$('#state').change(function(){
			var dataString={
				"country":$('#country option:selected').text(),
				"state": $('#state option:selected').text(),
				"required":"city"	
			};
			dataString=JSON.stringify(dataString);
			ajax_call('city', dataString);
		});
		
		$('#searchForm').submit(function(event){
			event.preventDefault();
			var place={
					"country":$('#country option:selected').text(),
					"state": $('#state option:selected').text(),
					"city": $('#city option:selected').text()
					};
			var dataString={
					"key": $('#keyString').val(),
					"place": place
				};
				dataString=JSON.stringify(dataString);
				$.ajax({
					url: "timezone",
					type: "post",
					dataType: "json",
					contentType: "application/json",
					data: dataString,
					async: true,
					cache: false,
					processData:false,
					success: function(data){
						var time=data.currentTime;
						timezonedata=data.data;
							var output='<h1>Your timeZone Data:</h1><br>';
							for (var i in timezonedata) {
								output+="timeZoneID: "+timezonedata[i].timeZoneID+'<br>';
								output+='rawOffset: '+timezonedata[i].rawOffset+'<br>';
								output+='dstOffset: '+timezonedata[i].dstOffset+'<br>';
								output+='state: '+timezonedata[i].state+'<br>';
								output+='longitude: '+timezonedata[i].longitude+'<br>';
								output+='timeZoneName: '+timezonedata[i].timeZoneName+'<br>';
								output+='latitude: '+timezonedata[i].latitude+'<br>';
								output+='country: '+timezonedata[i].country+'<br>';
								output+='city: '+timezonedata[i].city+'<br>';
								output+='Current DST Time: '+data.currentDSTTime+'<br>';
								output+='Current UTC Time: '+data.currentUTCTime+'<br>';
							}
							
							$('#searchResult').html(output);
						
					},
					error: function(data){
					}
			});
		});
});
	