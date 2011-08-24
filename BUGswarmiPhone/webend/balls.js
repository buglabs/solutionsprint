	var j;
	var panda;
	var s;
	var Balls = {};

	var API;
	var ID;

	var Ball = function(uiRep, id) {
	    	this.uiRep = uiRep; //the css element
	    	this.id = id;

		// Position Variables
		this.x = (document.documentElement.clientWidth - 20 )/ 2;
		this.y = (document.documentElement.clientHeight - 20)/ 2;
	 
		// Speed - Velocity
		this.vx = 0;
		this.vy = 0;
	 
		// Acceleration
		this.ax = 0;
		this.ay = 0;

		this.vMultiplier = 0.2;
	};
		
	Ball.prototype.getId = function() { return this.id; }

	function checkCollision() {
		console.log("implosion!");
	}


	function update() {
			//console.log("update");
			for (var key in Balls)
			{
				var ball = Balls[key];
				ball.vy = ball.vy + -(ball.ay);
				ball.vx = ball.vx + ball.ax;

				ball.y = parseInt(ball.y + ball.vy * ball.vMultiplier);
				ball.x = parseInt(ball.x + ball.vx * ball.vMultiplier);

				if (ball.x<0 || ball.y<0 || ball.x>document.documentElement.clientWidth - 20 || 				  			ball.y>document.documentElement.clientHeight - 20) 
					{ 
					ball.x = (document.documentElement.clientWidth - 20)/ 2; ball.vx = 0; ball.ax = 0; 
					ball.y = (document.documentElement.clientHeight - 20)/ 2; ball.vy = 0; ball.ay = 0
					}

				ball.uiRep.style.top = ball.y + "px";
				ball.uiRep.style.left = ball.x + "px";
				//console.log(ball.uiRep.style.top);
				//console.log(ball.uiRep.style.left);

				
			}
			t = setTimeout("update()", 50);
		}


	function joinSwarm(){
		API = "fae2bf42003e78465abebb70524f5ce29bda83ac";
		ID =  "f8d31289f8f6db7d7a66a46d893e74bcaf9dbcc7";
		SWARM.join({apikey: API, swarms: ID, callback: function(message) {
			
		j = JSON.stringify(message);		
		//console.log(Date.now() + ': ' + JSON.stringify(message));
		panda = eval('(' + j + ')');

		if (panda.message != null && panda.message.body != null)
		{	
			if (panda.message.body != "This room is not anonymous")
			{
				s = eval('(' + panda.message.body + ')');
				var browserID = panda.message.from;	
				if (Balls[browserID] == null)
				{	
					
					var d = document.createElement("div");
					d.setAttribute("id", browserID); 
					d.setAttribute("class", "ball");
					d.style.backgroundColor = Math.floor(Math.random()*16777215).toString(16);
					document.getElementById('data').appendChild(d);
					var b = new Ball(d, browserID);
					Balls[browserID] = b;
					console.log(Balls[browserID].uiRep.style);
					console.log(Balls[browserID].vy);
					console.log(Balls[browserID].vx);
					console.log("added ball!");
				}

				Balls[browserID].ax = s.x;			
				Balls[browserID].ay = s.y;
				//update(Balls[browserID]);
			}
			else console.log("panda");
		}
		else {
			var browserID = panda.presence.from;
			if (panda.presence != null && panda.presence.x != null &&
				panda.presence.x.item != null && panda.presence.x.item.jid != null)
			{
				// console.log(panda.presence.x.item.jid);
				console.log("presence browser id: " + browserID);
				// console.log(Date.now() + ': ' + JSON.stringify(message));
				if (Balls[browserID] != null)
				{				
					if (panda.presence.type == "unavailable")
					{
						//Balls[browserID].uiRep.style.opacity = 0;
					}
					else
					{
						//Balls[browserID].uiRep.style.opacity = 100;
					}
				}
			}
		}
		}
		}); 
	}
		
	window.onload = update;
