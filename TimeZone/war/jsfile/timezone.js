$(document).ready(function(){
	$(function(){
	var str="country";	
	var dataString={
			required: str
	};
	dataString=JSON.stringify(dataString);
	console.log(dataString);
	$.ajax({
		url: "getList",
		type: "post",
		dataType: "json",
		contentType: "application/json",
		data: dataString,
		async: false,
		cache: false,
		processData:false,
		success: function(data){
		
			var listdata=data.list;
			
			if(listdata.length!=0){
				var output='<option selected=true>select Country</option>';
				for (var i in listdata) {
					output+='<option value="'+listdata[i]+'">'+listdata[i]+'</option>';
				}
				$('#country').html(output);
			}
			else{
				
				
			}
	      },
	    error: function(data){
	    }
	});
	});
	$('#country').change(function(){
		var str="state";
		var dataString={
				"country":$('#country option:selected').text(),
				"required":str	
			};
			dataString=JSON.stringify(dataString);
			$.ajax({
				url: "getList",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: false,
				cache: false,
				processData:false,
				success: function(data){
					listdata=data.list;
					if(listdata.length!=0){
						var output='<option selected=true>select State</option>';
					
						for (var i in listdata) {
							output+='<option value="'+listdata[i]+'">'+listdata[i]+'</option>';
						}
						$('#state').html(output);
					}
					else{
						
						
					}
			      },
			    error: function(data){
			    }
			
		});
	});
			
		$('#state').change(function(){
			var dataString={
				"country":$('#country option:selected').text(),
				"state": $('#state option:selected').text(),
				"required":"city"	
			};
			dataString=JSON.stringify(dataString);
			$.ajax({
				url: "getList",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: false,
				cache: false,
				processData:false,
				success: function(data){
					listdata=data.list;
					if(listdata.length!=0){
						var output='<option selected=true>select City</option>';		
						for (var i in listdata) {
							output+='<option value="'+listdata[i]+'">'+listdata[i]+'</option>';
						}
						$('#city').html(output);
					}
					else{			
					}
				},
				error: function(data){
				}
			});
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
					async: false,
					cache: false,
					processData:false,
					success: function(data){
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
//								
//								
							}
							$('#searchResult').html(output);
						
					},
					error: function(data){
					}
				
			});
		});
});
	