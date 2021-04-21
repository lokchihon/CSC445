public class GUIRunner {

	public static void main(String[] args) {

		/*
		GUI g = new GUI("DrawMaster", true, true);
		boolean winner = g.addChat("GhostInTheWire", "Hello world!");
		if(winner) g.addToPoints(1);
		g.giveWord(false, "alphabet");
		for(int q = 10; q > -1; q--) {
			g.updateTimer(q);
			if(g.getTimeGuessed()!=Long.MAX_VALUE) {
				System.out.println("We have a winner");
				g.addToPoints(1);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
		
		
		//This makes the main menu GUI
		GUIMainMenu m = new GUIMainMenu();
		
		//This would be the main loop that runs stuff. You'd have the socket in here
		while(true) {
			
			//If the game has been made (if the start button was pressed)
			if(m.hasGame()) {
				
				//If the game name was valid
				if(m.gameName.equals("game")) {
					
					//Make a GUI from the main menu class, and tell it the username, word, and host status.
					//All you need to do is tell it the word; make sure to use m.uName and m.host, unless
					//it's something that the controller need to change for some reason.
					GUI g = m.makeGUI(m.uName, "flamingo", m.host);
					
					//In my example, I'm using the int count and while loop below to be the 'seconds' counting
					//down until the end of the round. 
					int secondsLeft = 20;
					g.updateTimer(secondsLeft);
					while(secondsLeft >= 0) {
						
						//This if check is necessary for making sure the window is still open.
						//It relies on the GUIWindowListener class that GUI makes inside itself.
						if(!g.isAlive()) {
							//This kills our GUI reference
							g = null;
							//This kills m's GUI reference
							m.removeGame();
							break;
						}
						//This is a time check to make sure we don't sleep the thread later
						//for more than 1000 milliseconds.
						long t0 = System.currentTimeMillis();
						//If we're playing (the round has started because the painter pressed start)
						if(g.getPlaying()) {
							//Decrease the time left on the timer.
							g.updateTimer(secondsLeft--);
							//TODO this is where the socket logic would go
							//TODO If need be, I could have a special popup window method in GUI for 
							//telling the user if something has become out of sync, or that the game
							//is otherwise not running properly. I do not have that currently.
						}
						//This is the second time check
						long t1 = System.currentTimeMillis();
						try {
							//Here, we find the difference in time checks, then subtract it from 1000 to sleep for
							//the remaining time out of the one second. 
							Thread.sleep(1000-((int)(t1-t0)));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					//This is how I suggest we notify the players that the round has ended, but a 
					//popup window would be pretty easy too.
					g.addChat("Game", "The round has ended.");
					g.addChat("Game", "Next round will begin in 5 seconds.");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				//If the game name was not valid
				else m.couldNotFindGame();
			}
			//If the game field is not populated (the user has not pressed start)
			else {
				try {
					//Removes m's GUI reference
					m.removeGame();
					//This sleep call was necessary to get the code to react. It may not be necessary
					//while there is other stuff going on, but I think it will be needed since there
					//shouldn't be much processing going on at this point in the code.
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}


		
	}

}
