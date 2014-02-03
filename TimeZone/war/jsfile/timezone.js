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
	//Ajax call to get country city and state list.
	 function ajax_call(name, dataString) {
		$('#seachButton').attr("disabled",true);
		 var id='#'+name;
		 $(id).addClass('ui-autocomplete-loading');
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
								if(typeof(listdata[i])=='string')
								{
									output+='<option value="'+listdata[i]+'">'+listdata[i]+'</option>';
								}
								else
								{
									output+='<option value="'+listdata[i].country+'">'+listdata[i].country+'</option>';
								}
								
							}
							$(id).removeClass('ui-autocomplete-loading');
							$(id).html(output);
							$('#seachButton').attr("disabled",false)
						}
					}
					else if(data.status=="wait"){
						$('#result').html("<h1>Please wait...</h1>");
					}
			      },
			    error: function(data){
			    	console.log(data);
			    }
			});
	    }
	 //Ajax call to get timezone details
	function getTimezoneData(dataString){
		$('#result').empty();
		$('#result').addClass('loader');
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
					$('#resultHeader').empty();
					$('#result').removeAttr('style');
					$('#result').removeClass('loader');
					$('#resultHeader').html("<h1>Your timeZone Data:</h1>");
					var output='<div class="form-horizontal col-lg-6">';
					for (var i in timezonedata) {
						output+='<div class="form-group"><label class="col-sm-4 control-label ">timeZoneID:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].timeZoneID+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">TimeZone Name:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].timeZoneName+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">Country :</label>';
						output+='<div class="col-sm-6"><p class="form-control-static"><i class="glyphicon bfh-flag-'+timezonedata[i].countryCode+' pull-left"></i>'+timezonedata[i].country+'</p></div></div>';

						output+='<div class="form-group"><label class="col-sm-4 control-label">Country Code :</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].countryCode+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">State:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].state+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">City:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].city+'</p></div></div>';

						output+='<div class="form-group"><label class="col-sm-4 control-label">Zip Code:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].zipCode+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">Longitude:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].longitude+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">Latitude:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].latitude+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">Raw Offset:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].rawOffset+'</p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">DST Offset:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static">'+timezonedata[i].dstOffset+'</p></div></div></div>';
						
						output+='<div class="form-horizontal col-lg-6 panel  panel-default"><div class="form-group"><label class="col-sm-4 control-label">Current DST Time:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static" id="'+timezonedata[i].city+'dst"></p></div></div>';
						
						output+='<div class="form-group"><label class="col-sm-4 control-label">Current UTC Time:</label>';
						output+='<div class="col-sm-6"><p class="form-control-static" id="'+timezonedata[i].city+'raw"></p></div></div></div>';
						
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
					$('#result').removeClass('loader');
					$('#result').html("Sorry!! there is some error").css("color", "red");
				}
			},
			error: function(data){
				$('#result').removeClass('loader');
				$('#result').html("Sorry!! there is some error").css("color", "red");
			}
	});
	}
	//Getting Country list.
	$('#searchByPlaceTab').click(function(){
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
		//Getting city List
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
		
		//Timezonde data from city, state and country.
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
			getTimezoneData(dataString);
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
		// Serach using phone number
		$('#serachByPhone').submit(function(){
			event.preventDefault();
			
		});
		//Search using city.
		var zip;
		$(function(){
			$('#cityName').autocomplete({
				minLength: 3,
				source: function (request, response) {
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
							$('#cityNameSpan').empty();
							$('#cityName').removeClass("ui-autocomplete-loading");
							var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
					          response( $.grep( data, function( item ){
					              return (matcher.test( item.city))+(matcher.test( item.state))+(matcher.test( item.country));
					          }) );
						},
		            	error: function(data){
		            		console.clear();
		            		$('#cityNameSpan').html("city not found").css("color", "red");
		            		$('#cityName').removeClass("ui-autocomplete-loading");
		            	}
		            })
				},
				focus: function( event, ui ) {
					$( "#cityName" ).val( ui.item.city);
					zip= ui.item.zipCode;
					event.preventDefault();
				},
				select: function( event, ui ) {
					$( "#cityName" ).val( ui.item.city);
					zip= ui.item.zipCode;
					event.preventDefault();
				}
			})
		    .data( "ui-autocomplete" )._renderItem = function( ul, item ) 
		    {
		      return $( "<li>" )
		        .append( "<a>" + item.city + "<i class='glyphicon bfh-flag-"+item.countryCode+" pull-right'></i><br>" + item.state+", "+item.country+ "</a></li>" )
		        .appendTo( ul );
		    };
		});
		$('#cityName').on('autocompletefocus',function(event, ui){
			$('#cityName').keyup(function(event){
				if(event.which==27){
					$( "#cityName" ).val( ui.item.city);
					zip= ui.item.zipCode;
					event.preventDefault();
				}
			})			
		});
		//Search using City name.
		$('#cityName').on( "autocompleteselect", function( event, ui ) {
			$('#searchByCity').attr('disabled', true);
			$('#cityNameSpan').empty();
			var place={
					zipCode: zip
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
			getTimezoneData(dataString);
			$('#searchByCity').attr('disabled', false);
		});
		
		//search city submit action.
		$('#serachByCity').submit(function(event){
			event.preventDefault();
			$('#searchByCity').attr('disabled', true);
			if(zip!=null&&zip!=''&&($('#cityName').val()!='')&&($('#cityName').val()!=null))
			{
				$('#cityNameSpan').empty();
				var place={
						zipCode: zip
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
				getTimezoneData(dataString);
			}
			else
			{
				$('#cityNameSpan').html('please select place').css('color','red');
			}
			$('#searchByCity').attr('disabled', false);
		});
		if (!$('#timezone').hasClass('active')) 
		{
     		$('#timezone').addClass('active');
     	}
		// Empty result Field and show clicked tab.
		$('#myTab a').click(function (e) {
			  e.preventDefault()
			  $('#resultHeader').empty();
			  $('#result').empty();
			  $(this).tab('show');
		});
		//Search By ZipCode.
		var countryData;
		$('#searchByZipTab').click(function(){
			 var str="country";	
	        	var dataString={
	        			required: str
	        	};
	        	dataString=JSON.stringify(dataString);
	            $.ajax({
					url: "getList",
					type: "post",
					dataType: "json",
					contentType: "application/json",
					data: dataString,
					async: true,
					cache: true,
					processData:false,
					success: function(data){
						countryData=data.list;
					}
		})
		});
		$(function(){
			$('#countryName').autocomplete({
				minLength: 1,
				source: function(request, response){
					$('#zip').val('');
					$("#zipResult").empty();
					var matcher = new RegExp( "^" + $.ui.autocomplete.escapeRegex( request.term ), "i" );
					response( $.grep( countryData, function( item ){
			              return (matcher.test( item.countryCode))+(matcher.test( item.country));
			          }) );
				},
				focus: function( event, ui ) {
					$( "#countryName" ).val( ui.item.country);
					$( "#countryCode" ).val( ui.item.countryCode);
					event.preventDefault();
				},
				select: function( event, ui ) {
					$( "#countryName" ).val( ui.item.country);
					$( "#countryCode" ).val( ui.item.countryCode);
					event.preventDefault();
				}
			})
		    .data( "ui-autocomplete" )._renderItem = function( ul, item ) 
		    {
		      return $( "<li>" )
		        .append( "<a>" + item.country + "<i class='glyphicon bfh-flag-"+item.countryCode+" pull-right'></i></a></li>" )
		        .appendTo( ul );
		    }
		});
		
		//zip code api call
			$('#zip').keyup(function(event) {
				
				var zipCode=$('#zip').val();
				console.log(zipCode);
				var countryCode=$('#countryCode').val();
					//$.getJSON("http://api.zippopotam.us/"+countryCode+"/"+zipCode,function(data){
					$.ajax({
						url: "http://api.zippopotam.us/"+countryCode+"/"+zipCode,
						dataType: "json",
						success: function(data){
							console.clear();
							console.log('zip found');
							$('#zipDiv').removeClass('has-error');
							$('#zipDiv').addClass('has-success');
							$('#zipSpan').empty();
							var output='<div class="row"><div class="col-sm-4"><label for="zipCity">Place:</label><select id=zipCity class=form-control>';
							output+='<option selected=true value=0>select place</option>';
							var places=data.places;
							for (var i in places)
							{
								output+='<option value="'+places[i].longitude+','+places[i].latitude+'">'+places[i]['place name']+'</option>';
							}
							output+='</select></div></div>';
							output+='<div class="row"><div class="col-sm-4"><label for="StateName">State:</label><input id=Zipcity value="'+places[0].state+'" class=form-control readonly>';
							output+='<span id="stateNameSpan" class="help-block"></span></div></div>';
							$("#zipResult").html(output);
						},
						error: function(data){
							console.clear();
							$('#zipDiv').removeClass('has-success');
							console.log('zip not found')
							$('#zipSpan').html("zip not found").css("color", 'red');
							$('#zipDiv').addClass('has-error');
							$("#zipResult").empty();
						}
					})
				
				event.preventDefault();
			});
			$(document).on('change', '#zipCity', function(){
				var longitude=$('#zipCity').val().split(",")[0];
				var latitude=$('#zipCity').val().split(",")[1];
				var place={
						longitude: longitude,
						latitude: latitude
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
				getTimezoneData(dataString);
			});
			
			$('#serachByZip').submit(function(event){
				event.preventDefault();
				if($('#zipCity').length!=0)
					{
					var longitude=$('#zipCity').val().split(",")[0];
					var latitude=$('#zipCity').val().split(",")[1];
					var place={
							longitude: longitude,
							latitude: latitude
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
					getTimezoneData(dataString);
					}
				else
				{
					$('#zipSpan').html("Please enter zipcode").css("color", "red");
				}
			});
			
});
	