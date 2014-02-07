$(document).ready(function(){
	function placeDetail(city, state,country)
	{
		var output='<div class="form-group"><label class="col-sm-3 control-label">Your Current City:</label><div class="col-sm-9">';
			output+='<p class="form-control-static" id=city>'+city+'</p></div></div>';
			output+='<div class="form-group"><label class="col-sm-3 control-label">Your Current State:</label><div class="col-sm-9">';
			output+='<p class="form-control-static" id=state>'+state+'</p></div></div>';
			output+='<div class="form-group"><label class="col-sm-3 control-label">Your Current Country:</label><div class="col-sm-9">';
			output+='<p class="form-control-static" id=country>'+country+'</p></div></div>';    
			output+='<div class="form-group"><label class="col-sm-3 control-label">Your Current Time:</label><div class="col-sm-9">';
			output+='<p class="form-control-static" id=time></p></div></div>';
			console.log(output);
			$('#place').html(output);
	}
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
	if (!$('#home').hasClass('active')) 
	{
		$('#home').addClass('active');
	}
 
	if(navigator.geolocation)
	{
		navigator.geolocation.getCurrentPosition(getPosition);
	}
	function getPosition(position)
	{
		var latitude= position.coords.latitude;
		var longitude=position.coords.longitude;
		getTimezoneData(longitude, latitude);
	}
	function getTimezoneData(longitude, latitude){
		if(typeof(Storage)!=="undefined")
		  {
		  if (sessionStorage.data)
		    {
			    var timezonedata= JSON.parse(sessionStorage.data);
				var distance=timezonedata.distance;
				timezonedata=timezonedata.data
				if(longitude==sessionStorage.longitude&&latitude==sessionStorage.latitude)
					{
					var time;
					$.get( "/getUTCTime", function(time){
						var localTime=new Date();
			 			serverTime=time;
			 			var rawOffset=serverTime-localTime.getTime()+timezonedata[0].rawOffset;
			 			time='<script type="text/javascript">window.onload = getClock('+rawOffset+',"#time" );</script>';
			 			$('#place').removeClass('loading');
						placeDetail(timezonedata[0].city,timezonedata[0].state,timezonedata[0].country+time);
				    });
					}
				else
				{
					 sessionStorage.clear();
					getLocation(longitude, latitude);
				}
		  }
		  else
		  {
			  sessionStorage.clear();
			 getLocation(longitude, latitude);
		  }
		
		}
		else
		{
			getLocation(longitude, latitude);
		}
	}
	function getLocation(longitude, latitude){
		
		$('#place').addClass('loading');
			dataString="longitude="+longitude+"&latitude="+latitude;
			$.ajax({
				url: "timezone",
				type: "get",
				dataType: "json",
				data: dataString,
				async: true,
				cache: false,
				processData:false,
				  success: function(data)
				  	{
						responseData=data;
						var distance=responseData.distance;
						timezonedata=responseData.data
						$.get( "/getUTCTime", function(time){
							var localTime=new Date();
				 			serverTime=time;
				 			var rawOffset=serverTime-localTime.getTime()+timezonedata[0].rawOffset;
				 			var time='<script type="text/javascript">window.onload = getClock('+rawOffset+',"#time" );</script>';
							$('#place').removeClass('loading');
							placeDetail(timezonedata[0].city,timezonedata[0].state,timezonedata[0].country+time);
				 			if(typeof(Storage)!=="undefined"){
				 				sessionStorage.data=JSON.stringify(responseData);
					 			sessionStorage.longitude=longitude;
					 			sessionStorage.latitude=latitude;
				 			}
						});
				  	}
				});
		}
});