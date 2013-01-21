import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LifeApplet extends Applet {
	
	// Required by Java
	private static final long serialVersionUID = 1803100828828371886L;
	
 
  /*
  public void init() {
  //public static void main(String[] args) {
    super.init();
    new LifeApplet();
  }
	*/
	//public LifeApplet() throws HeadlessException {
	public void init() {
		MouseListener mcl = new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				String[] arg = {"30"};
				life.Controller.main(arg);
			}
		};
		JFrame j = new JFrame();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To terminate
		j.setTitle("Life Applet");
		JButton launch = new JButton("Launch");
		launch.addMouseListener(mcl);
		Container cTextArea = new JPanel(new GridLayout(1, 1));
		Container cLaunch = new JPanel(new GridLayout(1, 1));
		JTextArea textArea = new JTextArea("Welcome to Theodore Boyd's implementation of Conway's Game of Life" + "\n" +
				 "\n" +
				 "Click Launch to open, or read these tips:" + "\n" +
				 "1. Clicking your left mouse button will set a dead (grey) cell to red." + "\n" + 
				 "2. Clicking your right mouse button will set a dead cell to green." + "\n" +
				 "3. Middle clicking your mouse will set a live (red/green) cell to grey." + "\n" +
				 "4. Clicking the Clear button will set all cells to dead." + "\n" +
				 "5. Clicking the Step button will run Conway's rules once*." + "\n" +
				 "6. Clicking the Run button will step through repeatedly at the speed" + "\n" +
				 "   given by the slider (delay between steps in milliseconds)." + "\n" +
				 "7. Clicking the Stop ask if you want to quit." + "\n" +
				 "8. Clicking the Resize button will restart the grid with a new size." + "\n" +
				 "9. Unchecking the Wrap checkbox will mean the program will (attempt," + "\n" +
				 "   experimentally) to not wrap, but rather kill, cells as they reach the end of the grid." + "\n" +
				 "*  With the additional rule that 'dominant colour prevails'.");
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(16);
		cTextArea.add(textArea, BorderLayout.CENTER);
		cLaunch.add(launch, BorderLayout.CENTER);
		j.add(cTextArea, BorderLayout.NORTH);
		j.add(cLaunch, BorderLayout.SOUTH);
		j.pack();
		j.setSize(600, 320);
		j.setVisible(true);
	}
}