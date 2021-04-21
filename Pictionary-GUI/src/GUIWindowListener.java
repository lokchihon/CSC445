import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUIWindowListener implements WindowListener{

	GUI g;
	
	public GUIWindowListener(GUI gui) {
		g = gui;
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		g.alive = false;
		System.out.println("Listen: closed");
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		g.alive = false;
		System.out.println("Listen: Closing");
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		g.alive = false;
		System.out.println("Listen: deactivated");
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		g.alive = false;
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}

}
