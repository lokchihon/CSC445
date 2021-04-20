
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
		
		GUIMainMenu m = new GUIMainMenu();
		m.run();
		
	}

}
