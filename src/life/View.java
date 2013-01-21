package life;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class View extends JFrame {

	/*
	 * The user's representation of the Model, the interface.
	 */
	
	// Serialisation required by Java
	private static final long serialVersionUID = 5069794013987059105L;
	private boolean gridEnabled = false; // While stepping, the grid is disabled
									     // and doesn't accept edits to cells.
	private Model model;
	private Container cr;
	private CellClick cc;
	private int size;
	private int speed = 200;
	private int step = 0;
	boolean running = false;
	JLabel mainLabel = new JLabel("0", SwingConstants.CENTER); // Run # status
	JSlider speedSlider = new JSlider(JSlider.VERTICAL, 1, 1000, 200); // Slider
	
	CellButton[][] cbArray; // Array of buttons
	
	View() {}
	
	View(int size, Model model) {
		this.model = model;
		this.speed = 200;
		this.size = size;
	}
	
	public void display() {
		// Frame
		setTitle("Life");
		setSize(size * 20, (size * 20) + 20);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To terminate
		//Removed ^ to not quit applet
		
		// Buttons (other than cells)
		ButtonClick bc = new ButtonClick(this);
		
		JButton clear = new JButton();
		clear.setText("Clear");
		clear.addMouseListener(bc);
		
		JButton step = new JButton();
		step.setText("Step");
		step.addMouseListener(bc);

		JButton run = new JButton();
		run.setText("Run");
		run.addMouseListener(bc);
		
		JButton stop = new JButton();
		stop.setText("Stop");
		stop.addMouseListener(bc);
		
		JButton resize = new JButton();
		resize.setText("Resize");
		resize.addMouseListener(bc);
		
		JCheckBox wraptoggle = new JCheckBox();
		wraptoggle.setText("Wrap");
		wraptoggle.setSelected(model.wrap);
		wraptoggle.addMouseListener(new ToggleChange());
		wraptoggle.addKeyListener(new ToggleChangeKey());
		
		
		// Slider
		speedSlider.setMajorTickSpacing(999);
		speedSlider.setMinorTickSpacing(100);
		speedSlider.setPaintTicks(true);
		speedSlider.setPaintLabels(true);
		speedSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		speedSlider.addChangeListener(new SliderChange());
		
		// Containers
		cr = new JPanel();
		cr.setLayout(new GridLayout(size, size));
		
		Container topContainer = new JPanel();
		topContainer.add(mainLabel, BorderLayout.CENTER);
		
		Container controlContainer = new JPanel(new GridLayout(1, 6));
		
		controlContainer.add(clear);
		controlContainer.add(step);
		controlContainer.add(run);
		controlContainer.add(stop);
		controlContainer.add(resize);
		controlContainer.add(wraptoggle);
		
		// Labels
		mainLabel.setText("Size value is: " + size + ".");
		
		
		// Cells
		cc = new CellClick(this, model);
		build();
		
		// Display and add containers
		add(cr, BorderLayout.CENTER);
		add(topContainer, BorderLayout.NORTH);
		add(controlContainer, BorderLayout.SOUTH);
		add(speedSlider, BorderLayout.EAST);
		pack();
		setVisible(true);
		gridEnabled = true; // Unlock grid
	}
	
	private void build() {
		gridEnabled = false; // Prevent changes during building
		cr.removeAll();
		cbArray = new CellButton[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				CellStatusNode csn = (CellStatusNode)model.csLL.get(To1D(i, j));
				CellButton cb = new CellButton(i, j);
				cb.setPreferredSize(new Dimension(15, 15));
				cb.setBackground(csn.state.getColour());
				cb.addMouseListener(cc);
				cr.add(cb);
				cbArray[i][j] = cb;
			}
		}
		setStep(0); // Show step count
		gridEnabled = true; // Unlock grid
	}
	
	public void redraw() { // Refresh colours
		gridEnabled = false; // Prevent changes during redrawing
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				CellStatusNode csn = (CellStatusNode)model.csLL.get(To1D(i, j));
				cbArray[i][j].setBackground(csn.state.getColour());
			}
		}
		gridEnabled = true; // Unlock grid
	}
	
	private int To1D(int x, int y) {
		// Convert from a 2D system to the LinkedList index
		return ((size * x) + y);
	}
	
	class SliderChange implements ChangeListener {
	    public void stateChanged(ChangeEvent expn) {
	        JSlider source = (JSlider)expn.getSource();
	        if (!source.getValueIsAdjusting()) {
	        	speed = (int)source.getValue();
	        	setStep(step);
	        }
	    }
	}
	
	class ToggleChange implements MouseListener {
		
		// TODO FIX THIS - IT'S NOT WORKING
		// PROPERLY.
		public void mouseClicked(MouseEvent e) { 
			boolean checkValue = ((JCheckBox)e.getComponent()).isSelected();
			model.wrap = checkValue;
			if (!checkValue) {
			int result = JOptionPane.showConfirmDialog(e.getComponent(),
					"Not wrapping at edges is an experimental feature.\nSome patterns may still wrap or perform unexpectedly.\nAre you sure you wish to use it?", "Life",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (result != JOptionPane.YES_OPTION) {
					model.wrap = true;
					((JCheckBox)e.getComponent()).setSelected(true);
				}
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
	
	class ToggleChangeKey implements KeyListener {
		public void keyPressed(KeyEvent e) {
			e.consume();
			return;
		}
		public void keyReleased(KeyEvent e) { return; }
		public void keyTyped(KeyEvent e) { return; }
	}
	
	public void clear() {
		gridEnabled = false; // Prevent changes during clearing
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				// Convert from a 2D system to the LinkedList index
				CellStatusNode csn = (CellStatusNode)model.csLL.get(To1D(i, j));
				csn.setState(State.BLANK);
			}
		}
		redraw(); // Redraw and then show step count
		setStep(0);
		gridEnabled = true; // Unlock grid
	}
	
	public void step() {
		gridEnabled = false; // Prevent changes during stepping
		model.update();
		setStep(step + 1);
		gridEnabled = true; // Unlock grid
		redraw();
	}
	
	public void runSteps() {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
				// Schedule the timer to run the step() function
				public void run() {
					if (running) {
						step();
						runSteps(); // Continue to run
					}
				}
			}, speed);
	}

	public void stop() {
		gridEnabled = false; // Prevent changes during stopping
		int result = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to quit?", "Life",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			dispose(); // Destroy window
			System.exit(0); // Exit (no error)
		}
		// Didn't wish to quit
		gridEnabled = true; // Unlock grid
	}
	
	public boolean isEnabled() {
		return gridEnabled;
	}
	
	private void setStep(int s) { // Updates step and text box
		step = s;
		mainLabel.setText("Generation: " + step + "  Delay: " + speed + "ms"); // Show step count
		mainLabel.repaint();
	}
	
	public void resize() {
		gridEnabled = false; // Prevent changes during stopping
		int n = 0;
		try {
			n = Integer.parseInt(JOptionPane.showInputDialog(this, new String("Please input a new size:\n[1..100]"), Integer.toString(size)));
		} catch (NumberFormatException e) {
			//e.printStackTrace();
		}
		if (n == 0 || n > 100) { return; }
		int result = JOptionPane.showConfirmDialog(this,
					"Resizing requires a reload.\nAre you sure you want to quit?", "Life",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			dispose(); // Destroy window
			new Controller(n);
		}
		// Didn't wish to quit
		gridEnabled = true; // Unlock grid
	}
}
