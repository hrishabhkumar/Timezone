$(document).ready(function(){
	
	if (!$('#home').hasClass('active')) 
	{
		$('#home').addClass('active');
	}
 
	if(navigator.geolocation){
		navigator.geolocation.getCurrentPosition(getPosition);
	}
	function getPosition(position)
	{
		var latitude= position.coords.latitude;
		var longitude=position.coords.longitude;
		getTimezoneData(longitude, latitude);
	}
	function getTimezoneData(longitude, latitude){
		
		$.get( "/timezone?longitude="+longitude+"&latitude="+latitude, function(data){
			var timezonedata=JSON.parse(data);
			var distance=timezonedata.distance;
			timezonedata=timezonedata.data
			$('#city').html(timezonedata[0].city+" ("+distance+")");
			$('#state').html(timezonedata[0].state);
			$.get( "/getUTCTime", function(time){
				var localTime=new Date();
	 			serverTime=time;
	 			var rawOffset=serverTime-localTime.getTime()+timezonedata[0].rawOffset;
	 			$('#country').html(timezonedata[0].country+'<script type="text/javascript">window.onload = getClock('+rawOffset+',"#time" );</script>');
			});
	 		
		});
	}
	
	
});