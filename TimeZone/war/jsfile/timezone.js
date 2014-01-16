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
					else if(data.status=="wait"){
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
						var output='<div class="form-horizontal">';
						for (var i in timezonedata) {
							output+='<div class="form-group"><label class="col-sm-3 control-label ">timeZoneID:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].timeZoneID+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">TimeZone Name:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].timeZoneName+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">Country :</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].country+'</p></div></div>';

							output+='<div class="form-group"><label class="col-sm-3 control-label">Country Code :</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].countryCode+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">State:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].state+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">City:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].city+'</p></div></div>';

							output+='<div class="form-group"><label class="col-sm-3 control-label">Zip Code:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].zipCode+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">Longitude:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].longitude+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">Latitude:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].latitude+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">Raw Offset:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].rawOffset+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">DST Offset:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static">'+timezonedata[i].dstOffset+'</p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">Current DST Time:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static" id="'+timezonedata[i].city+'dst"></p></div></div>';
							
							output+='<div class="form-group"><label class="col-sm-3 control-label">Current UTC Time:</label>';
							output+='<div class="col-sm-9"><p class="form-control-static" id="'+timezonedata[i].city+'raw"></p></div></div></div>';
							
							var rawOffset=parseInt(timezonedata[i].rawOffset)+data.currentTime;
							var dstOffset=parseInt(timezonedata[i].dstOffset)+data.currentTime;
							var clientDate=new Date();
							var timeDiff=data.currentTime-clientDate.getTime();
							rawOffset=parseInt(timezonedata[i].rawOffset)+timeDiff;
							dstOffset=parseInt(timezonedata[i].dstOffset)+timeDiff;
							output+='<script type="text/javascript">window.onload = date_time("'+timezonedata[i].city+'raw", parseInt('+rawOffset+'));</script>';
							output+='<script type="text/javascript">window.onload = date_time("'+timezonedata[i].city+'dst", parseInt('+dstOffset+'));</script>';
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
				}
				else if($('#state').val()==0||$('#state').val()==null){
					$('#countrySpan').empty();
					$('#stateSpan').empty();
					$('#citySpan').empty();
					$('#stateSpan').html("Please select a State").css("color", "red");
				}
				else if($('#city').val()==0||$('#city').val()==null){
					$('#countrySpan').empty();
					$('#stateSpan').empty();
					$('#citySpan').empty();
					$('#citySpan').html("Please select a City").css("color", "red");
				}
			}
	});
		if (!$('#timezone').hasClass('active')) {
     		$('#timezone').addClass('active');
     	}
		$('#myTab a').click(function (e) {
			  e.preventDefault()
			  $(this).tab('show');
			});
		$('#phoneNumber').change(function(){
			alert($('#phoneNumber').val());
		});
        
});
	