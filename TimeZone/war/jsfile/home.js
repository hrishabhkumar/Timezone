$(document).ready(function(){
	
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
					$('#city').removeClass('loading');
					$('#city').html(timezonedata[0].city+" ("+distance+")");
					$('#state').removeClass('loading');
					$('#state').html(timezonedata[0].state);
					$.get( "/getUTCTime", function(time){
						var localTime=new Date();
			 			serverTime=time;
			 			var rawOffset=serverTime-localTime.getTime()+timezonedata[0].rawOffset;
			 			$('#country').removeClass('loading');
						$('#time').removeClass('loading');
			 			$('#country').html(timezonedata[0].country+'<script type="text/javascript">window.onload = getClock('+rawOffset+',"#time" );</script>');
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
		
		$('#city').addClass('loading');
			$('#state').addClass('loading');
			$('#country').addClass('loading');
			$('#time').addClass('loading');
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
						 $('#city').removeClass('loading');
						$('#city').html(timezonedata[0].city+" ("+distance+")");
						$('#state').removeClass('loading');
						$('#state').html(timezonedata[0].state);
						$.get( "/getUTCTime", function(time){
							var localTime=new Date();
				 			serverTime=time;
				 			var rawOffset=serverTime-localTime.getTime()+timezonedata[0].rawOffset;
				 			$('#country').removeClass('loading');
							$('#time').removeClass('loading');
				 			$('#country').html(timezonedata[0].country+'<script type="text/javascript">window.onload = getClock('+rawOffset+',"#time" );</script>');
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