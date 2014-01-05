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
					var time=data.currentTime;
					timezonedata=data.data;
					var totalrawOffset;
					$('#resultHeader').html("<h1>Your timeZone Data:</h1>");
					var output='<div class="form-horizontal">';
					for (var i in timezonedata) {
						output+='<div class="form-group"><label class="col-sm-3 control-label ">timeZoneID:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].timeZoneID+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">TimeZone Name:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].timeZoneName+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">Country :</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].country+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">State:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].state+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">City:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].city+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">Longitude:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].longitude+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">Latitude:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].latitude+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">Raw Offset:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].rawOffset+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">DST Offset:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].dstOffset+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">Current DST Time:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static" id="dstTime"></p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-3 control-label">DST Offset:</label>';
						output+='<div class="col-sm-9"><p class="form-control-static" id="rawTime"></p></div></div></div>';
						
						var rawOffset=parseInt(timezonedata[i].rawOffset)+data.currentTime;
						var dstOffset=parseInt(timezonedata[i].dstOffset)+data.currentTime;
						var clientDate=new Date();
						var timeDiff=data.currentTime-clientDate.getTime();
						rawOffset=parseInt(timezonedata[i].rawOffset)+timeDiff;
						dstOffset=parseInt(timezonedata[i].dstOffset)+timeDiff;
						output+='<script type="text/javascript">window.onload = date_time("rawTime", parseInt('+rawOffset+'));</script>';
						output+='<script type="text/javascript">window.onload = date_time("dstTime", parseInt('+dstOffset+'));</script>';
					}
					$('#searchResult').html(output);
				},
				error: function(data){
				}
		});
	});
});
	