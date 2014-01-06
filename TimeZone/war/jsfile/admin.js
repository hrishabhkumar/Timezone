$(document).ready(function(){
	 function ajax_call(name, dataString) {
		$('#seachButton').attr("disabled",true)
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
							var output='<option selected=true value=0>select '+name+'</option>';
							for (var i in listdata) {
								output+='<option value="'+listdata[i]+'">'+listdata[i]+'</option>';
							}
							$(id).html(output);
							$('#seachButton').attr("disabled",false)
						}
					}
					else{
						$('#result').html("<h1>Please wait...</h1>");
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
		$('#state').empty();
		$('#city').empty();
		if($('#country').val()!=0){
			var str="state";
			var dataString={
					"country":$('#country option:selected').text(),
					"required":str	
				};
			dataString=JSON.stringify(dataString);
			ajax_call('state', dataString);
		}
		else{
			$('#countrySpan').html("Please select a Country").css("color", "red");
		}
	});
			
		$('#state').change(function(){
			if($('#state').val()!=0){
				$('#city').empty();
				var dataString={
					"country":$('#country option:selected').text(),
					"state": $('#state option:selected').text(),
					"required":"city"	
				};
			dataString=JSON.stringify(dataString);
			ajax_call('city', dataString);
			}
			else{
				$('#city').empty();
				$('#stateSpan').html("Please select a State").css("color", "red");
			}
		});
		
		$('#searchForm').submit(function(event){
			
			event.preventDefault();
			if($('#country').val()!=0&&$('#country').val()!=null&&$('#state').val()!=0&&$('#state').val()!=null&&$('#city').val()!=0&&$('#city').val()!=0){
				$('#countrySpan').empty();
				$('#stateSpan').empty();
				$('#citySpan').empty();
				var place={
					"country":$('#country option:selected').text(),
					"state": $('#state option:selected').text(),
					"city": $('#city option:selected').text()
					};
			var dataString={
					"key": $('#keyString').val(),
					"place": place
				};
			dataString={
				"required":"timezoneData",
				"data": dataString
			};
			dataString=JSON.stringify(dataString);
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
						var time=data.currentTime;
						timezonedata=data.data;
						var totalrawOffset;
						$('#resultHeader').html("<h1>Your timeZone Data:</h1>");
						var output='<form class="form-horizontal" role="form id=timezoneForm">';
						for (var i in timezonedata) {
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="timeZoneID">timeZone ID:</label>';
							output+='<input type="text" class="form-control" id=timeZoneID value='+timezonedata[i].timeZoneID+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="timeZoneName">TimeZone Name:</label>';
							output+='<input type="text" class="form-control" id=timeZoneName value='+timezonedata[i].timeZoneName+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="country">Country:</label>';
							output+='<input type="text" class="form-control" id=countryText value='+timezonedata[i].country+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="state">State:</label>';
							output+='<input type="text" class="form-control" id=stateText value='+timezonedata[i].state+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="city">City:</label>';
							output+='<input type="text" class="form-control" id=cityText value='+timezonedata[i].city+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="longitude">Longitude:</label>';
							output+='<input type="text" class="form-control" id=longitude value='+timezonedata[i].longitude+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="latitude">Latitude:</label>';
							output+='<input type="text" class="form-control" id=latitude value='+timezonedata[i].latitude+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="rawOffset">Raw Offset:</label>';
							output+='<input type="text" class="form-control" id=rawOffset value='+timezonedata[i].rawOffset+'></div></div>';
							
							output+='<div class="row"><div class="col-sm-4"><label class="control-label" for="dstOffset">DST Offset:</label>';
							output+='<input type="text" class="form-control" id=dstOffset value='+timezonedata[i].dstOffset+'></div></div>';
							output+='<br><div class="row"><div class="col-sm-4 ">';
							output+='<button type="button" id="update" class="btn btn-info">Update</button> ';
							output+='<button type="button" id="delete" class="btn btn-danger">Delete</button></div></div></form>';
						}
						$('#result').html(output);
					}
					else{
						$('#result').html("Sorry!! there is some error").css("color", "red");
					}
				},
				error: function(data){
					$('#result').html("Sorry!! there is some error").css("color", "red");
				}
		});
			}
			else{
				if($('#country').val()==0||$('#country').val()==null){
					$('#countrySpan').empty();
					$('#stateSpan').empty();
					$('#citySpan').empty();
					$('#countrySpan').html("Please select a Country").css("color", "red");
					$('#result').empty();
				}
				else if($('#state').val()==0||$('#state').val()==null){
					$('#countrySpan').empty();
					$('#stateSpan').empty();
					$('#citySpan').empty();
					$('#stateSpan').html("Please select a State").css("color", "red");
					$('#result').empty();
				}
				else if($('#city').val()==0||$('#city').val()==null){
					$('#countrySpan').empty();
					$('#stateSpan').empty();
					$('#citySpan').empty();
					$('#citySpan').html("Please select a City").css("color", "red");
					$('#result').empty();
				}
			}
		});
		if (!$('#timezone').hasClass('active')) {
     		$('#timezone').addClass('active');
     	}
		$('#result').on('click', '#update', function(){
			var newtimezonedata={
				"timeZoneID":$('#timeZoneID').val(),
				"timeZoneName":$('#timeZoneName').val(),
				"country": $('#countryText').val(),
				"state":$('#stateText').val(),
				"city":$('#cityText').val(),
				"longitude":$('#longitude').val(),
				"latitude":$('#latitude').val(),
				"rawOffset":$('#rawOffset').val(),
				"dstOffset":$('#dstOffset').val()
			};
			var oldtimezonedata={
					"country": $('#country').val(),
					"state":$('#state').val(),
					"city":$('#city').val()	
			};
			var dataString={
				"opreq":"update",
				"oldtimezonedata": oldtimezonedata,
				"newtimezonedata": newtimezonedata
			};
			dataString=JSON.stringify(dataString);
			$.ajax({
				url: "admin",
				type: "post",
				dataType: "json",
				contentType: "application/json",
				data: dataString,
				async: true,
				cache: false,
				processData:false,
				success: function(data){
					
				}
			});
		});
		$('#result').on('click', '#delete', function() {
			alert("delete");
		});
	});
	