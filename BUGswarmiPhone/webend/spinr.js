var ball, rotation, ax, ay, interval, direction, myScore, speed, switchAt, switches, lastScore;
    var warningHidden = false;
    
    function load() {
	   ball = document.getElementById("myBall");
	   rotation 		= 0;
	   clearInterval(interval);
	   interval 		= setInterval("nextFrame()");
	   ball.className 	= "playing";
	   myScore 			= 0;
	   direction 		= 1;
	   speed 			= 0.1;
	   switchAt 		= 5 * parseInt(Math.random() * 8);
	   switches			= 0;
	   lastScore		= 0;
    }
    
    function nextFrame() {
    	rotate();
    	
    	rotationInt = parseInt(rotation);
    	axInt		= parseInt(ax);
    	diff 		= 0;
    	
    	if(rotationInt < axInt)
    		diff = axInt - rotationInt;
    	else
    		diff = rotationInt - axInt;
    	
    	if(diff <= 5) myScore += 5-diff;
    	
    	updateScore(myScore);
    }
    
    function updateScore(val) {
    	document.getElementById("score").innerHTML = val;
    }
    
    function generateRandomSwitch() {
    	switchAt = direction * (5 + (4 * parseInt(Math.random() * 8)));
    }
    
    function rotate() {
    	cssRotation = rotation;
    	if(cssRotation < 0) cssRotation = 360 + rotation;
    	
    	ball.style.webkitTransform = "rotate(" + cssRotation + "deg)";
    	rotation += speed * direction;
    	
    	if(rotation >= 40 || (switchAt > 0 && rotation >= switchAt)) {
    		switchDirection()
    		generateRandomSwitch()
    	} else if(rotation <= -40 || (switchAt < 0 && rotation <= switchAt)) {
    		switchDirection()
    		speed += 0.1;
    		generateRandomSwitch();
    	}
    }
    
    function switchDirection() {
    	if(direction == 1)
    		direction = -1;
    	else
    		direction = 1;
    		
    	switches++;
    	
    	if(myScore - lastScore >= 300) {
    		switches 	= 0;
    	} else if(switches >= 2) {
    		clearInterval(interval);
    		ball.className = "done";
    	}
    	lastScore 	= myScore;
    }
	
	function removeWarning() {
		console.log(document.documentElement);
		if(navigator.userAgent.indexOf('AppleWebKit/') != -1) {
			window.addEventListener('devicemotion', function (e) {
				ax = e.accelerationIncludingGravity.x  * -(40/7);
				if(!warningHidden) {
					warningHidden = true;
					document.getElementById("warning").className = "hide";
					document.getElementById("myBall").addEventListener('click', load, true);
				}
			}, false);
		}
	}
