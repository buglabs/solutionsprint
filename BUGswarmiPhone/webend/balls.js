	var j;
	var panda;
	var s;
	var Balls = {};

	var API;
	var ID;

	var Ball = function(uiRep, id, xc, yc) {
	    	this.uiRep = uiRep; //the css element
	    	this.id = id;

		// Position Variables
		this.x = xc;
		this.y = yc;
	 
		// Speed - Velocity
		this.vx = 0;
		this.vy = 0;
	 
		// Acceleration
		this.ax = 0;
		this.ay = 0;

		// Velocity Multiplier
		this.vMultiplier = 0.1;
	};

	function checkCollision() {
		for (var key in Balls)
		{
			var ball = Balls[key];
			for (var key2 in Balls)
			{
				if (key2 != key)
				{
					var ball2 = Balls[key2];
					if (Math.sqrt(Math.pow(Math.abs(ball2.x - ball.x), 2) 
						+ Math.pow(Math.abs(ball2.y - ball.y), 2)) < 50 )
					{
						if (ball.id > ball2.id)
						{
							console.log("collision!");
							Balls[key].vx = -(ball.vx / 2);
							Balls[key].vy = -(ball.vy / 2);
							Balls[key2].vx = -(ball2.vx / 2);
							Balls[key2].vy = -(ball2.vy / 2);
							Balls[key].ax = 0;
							Balls[key].ay = 0;
							Balls[key2].ax = 0;
							Balls[key2].ay = 0;
							console.log(Balls[key].vx);
							console.log(Balls[key].vy);
							console.log(Balls[key2].vx);
							console.log(Balls[key2].vy);
//Balls[key].uiRep.style.top = Math.floor(Math.random()*(document.documentElement.clientHeight - 50));
//Balls[key2].uiRep.style.top = Math.floor(Math.random()*(document.documentElement.clientHeight - 50));
//Balls[key].uiRep.style.left = Math.floor(Math.random()*(document.documentElement.clientWidth - 50));
//Balls[key2].uiRep.style.left = Math.floor(Math.random()*(document.documentElement.clientWidth - 50));
						}
					}
				}
			}
		}
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

				if (ball.x<0 || ball.y<0 || ball.x>document.documentElement.clientWidth - 50 || 				  			ball.y>document.documentElement.clientHeight - 50) 
					{ 
					ball.vx = -(ball.vx / 2); ball.ax = 0; 
					ball.vy = -(ball.vy / 2); ball.ay = 0;
					}

				ball.uiRep.style.top = ball.y + "px";
				ball.uiRep.style.left = ball.x + "px";;

				//console.log(ball.uiRep.style.top);
				//console.log(ball.uiRep.style.left);

				
			}
			checkCollision();
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
					var x = Math.floor(Math.random()*(document.documentElement.clientWidth - 50));
					var y = Math.floor(Math.random()*(document.documentElement.clientHeight - 50));
					d.style.top = y;
					d.style.left = x;
					document.getElementById('data').appendChild(d);
					var b = new Ball(d, browserID, x, y);
					Balls[browserID] = b;
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
						Balls[browserID].uiRep.style.opacity = 0;
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
