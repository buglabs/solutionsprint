
var map;
var currentLocation;

function initialize() {
	var latlng = new google.maps.LatLng(-34.397, 150.644);
    	var myOptions = {
     		zoom: 12,
		center: latlng,
		mapTypeId: google.maps.MapTypeId.ROADMAP
    	};
	map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
	var polyOptions = {
	    strokeColor: '#000000',
	    strokeOpacity: 1.0,
	    strokeWeight: 3
	  }
  	poly = new google.maps.Polyline(polyOptions);
  	poly.setMap(map);
    	
	}




function update(lat,lon,time){
	if(lat!=null){
	var path = poly.getPath();
	var latlng = new google.maps.LatLng(lat, lon);
	path.push(latlng);
	var marker = new google.maps.Marker({
      		position: latlng, 
		map: map, 
		title: '#' + path.getLength(),
		animation: google.maps.Animation.DROP
		}); 
	map.setCenter(latlng);
	}}
var j;
var panda;
var s;
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
		
	console.log("Joining swarm");
	var timeInt=3000*10;
	var t=setInterval("update(s.lat,s.lon,panda.message.from)",timeInt);
	SWARM.join({apikey: API, 
		swarms: ID,
	        callback: function(message) {  
			j = JSON.stringify(message);			
			panda = eval('(' + j + ')'); 
			if (panda!=null && panda.message != null && panda.message.body != null && panda.message.body != "This room is not anonymous")
                	{   
			s = eval('(' + panda.message.body + ')');
			//update(s.lat,s.lon,panda.message.from);     
		 } }});
	
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

	  
