package life;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Controller {

	/*
	 * Class to represent the controller for the system.
	 * Accepts user input and uses the Model to send them to the View.
	 */
	
	// Serialisation required by Java
	private static final long serialVersionUID = -6867155390068900019L;
	private Model model;
	
	Controller() {
		model = new Model();
	}
	
	Controller(int n) {
		String[] input = {Integer.toString(n)};
		model = new Model();
		main(input);
	}
	
	public static void main(String args[]) {
		Controller cont = new Controller();
		try {
			UIManager.setLookAndFeel(
					"com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
					// Force correct appearance on other operating systems
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		int inputSize = 0;
		if (args.length > 0) {
			try { // If an argument has been provided, and it is a valid int...
				inputSize = Integer.parseInt(args[0]);
			} catch(Exception e) {
				System.out.println(e.toString());
			}
			if (inputSize >= 4) {
				cont.model.setSize(inputSize);
			} else {
				JOptionPane.showMessageDialog(new JPanel(),
							"Minimum size is 4. Defaulting to " + 
							cont.model.getSize() + ".", "Life", 0);
			}
		}
		javax.swing.SwingUtilities.invokeLater(cont.model);
	}
}