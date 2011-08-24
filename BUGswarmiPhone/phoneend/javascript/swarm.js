
		

		//boolean toggle vars
		var togGPS=false;
		var togACCEL=false;
		function toggle(elementID){
			if(elementID=="GPS")
				togGPS=!togGPS;
			if(elementID=="ACCEL")
				togACCEL=!togACCEL;
		}

		//get swarm info here plz
		function updateCred() {
		var SWARMID = document.getElementById('swarmid').value;
		createCookie("SWARMID", SWARMID, 7);
		var APIKEY = document.getElementById('apikey').value;
		createCookie("APIKEY", APIKEY, 7);
		alert("Created cookie: " + SWARMID + " " + APIKEY);
		location.href="index.html";
		}

		///COOOKIES
		function showCookies() {
		alert("SWARMID: " + readCookie("SWARMID") + " , APIKEY: " 
			+ readCookie("APIKEY"));
		}


		function createCookie(name,value,days) {
		if (days) {
			var date = new Date();
			date.setTime(date.getTime()+(days*24*60*60*1000));
			var expires = "; expires="+date.toGMTString();
			}
		else var expires = "";
		document.cookie = name+"="+value+expires+"; path=/";
		}

		function readCookie(name) {
			var nameEQ = name + "=";
			var ca = document.cookie.split(';');
			for(var i=0;i < ca.length;i++) {
				var c = ca[i];
				while (c.charAt(0)==' ') c = c.substring(1,c.length);
				if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
			}
			return null;
		}

		function eraseCookie(name) {
			createCookie(name,"",-1);
		}


		//data to be sent to swarm
		var lat;
		var lon;
		var accel;
		
		

		


		//start gps
		navigator.geolocation.getCurrentPosition(success, fail);
	
		// success callback, gets passed position object
		function success(position) // 'position' can be named anything
		{	
			lat = position.coords.latitude; 
			lon = position.coords.longitude;
		}
		function fail(position)
		{
			console.log("Can't get GPS data.");
		}
		//accelerometer
		window.addEventListener( "devicemotion", onDeviceMotion, false );
		function onDeviceMotion( event ) 
		{

			try {
				accel = event.accelerationIncludingGravity;
			}
			catch (e)
			{
				console.log(e);
			}	
			

		}
	

		function gup( name )
		{
		  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
		  var regexS = "[\\?&]"+name+"=([^&#]*)";
		  var regex = new RegExp( regexS );
		  var results = regex.exec( window.location.href );
		  if( results == null )
		    return null;
		  else
		    return results[1];
		}
			

		var APIdef="fae2bf42003e78465abebb70524f5ce29bda83ac";
		var IDdef="f8d31289f8f6db7d7a66a46d893e74bcaf9dbcc7";
		function getCRED(){
			if(readCookie("SWARMID")!="")
				ID=readCookie("SWARMID");
			if(readCookie("APIKEY")!="")
				API=readCookie("APIKEY");
			else			
				console.log("Using default credentials");
		}
		
		var timeInt;

		function setTimeInt(){
			timeInt = document.getElementById('time').value*100;
			console.log(timeInt);
		}
		var running = false;
		function joinSwarm(){
			//look for cookies			
			getCRED();
			//use url
			if(gup('API')!=null&&gup('ID')!=null)
			{
				API=gup('API');
				ID=gup('ID');
				console.log("Using url.");
			}
			
			if(API==null || API=="" ||ID==""||ID==null)
			{
				API=APIdef;
				ID=IDdef;
			}
			if(!running){
				console.log("Joining swarm");
				SWARM.join({apikey: API, 
	                	      swarms: ID,
	                	  callback: function(message) {        
		        	}});
				setTimeInt();
				running = true;
				var t=setInterval("sendMessage()",timeInt);
				spinner.spin(target);
			}
			else {
				SWARM.leave();
				running=false;
				spinner.stop();
			}
		}
		
		function sendMessage(){
			navigator.geolocation.getCurrentPosition(success, fail);
			if(togGPS && togACCEL)
				SWARM.send({lat:lat, lon:lon, x:accel.x, y:accel.y, z:accel.z}); 
			if(togGPS && !togACCEL)
				SWARM.send({lat:lat, lon:lon}); 
			if(!togGPS && togACCEL)
				SWARM.send({x:accel.x, y:accel.y, z:accel.z}); 
		}
