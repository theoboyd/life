package life;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class CellClick implements MouseListener {
	
	/*
	 * Handles clicks to buttons that are part of the cell grid.
	 */
	
	private View gui;
	private Model model;
	
	CellClick() {}
	
	CellClick(View gui, Model model) {
		this.gui = gui;
		this.model = model;
	}
	
	public void mouseClicked(MouseEvent e) {
		if (!gui.isEnabled()) { return; } // Don't accept a click
		switch (e.getButton()) {
			case MouseEvent.BUTTON1: // Left click
				CellButton b1 = (CellButton)e.getSource();
				model.csLL.get(To1D(b1.getXCoord(), b1.getYCoord())).state =
																	State.P1;
				break;
			case MouseEvent.BUTTON2: // Middle click
				CellButton b2 = (CellButton)e.getSource();
				model.csLL.get(To1D(b2.getXCoord(), b2.getYCoord())).state =
																	State.BLANK;
				break;
			case MouseEvent.BUTTON3: // Right click
				CellButton b3 = (CellButton)e.getSource();
				model.csLL.get(To1D(b3.getXCoord(), b3.getYCoord())).state =
																	State.P2;
				break;
			default: break;
		}
		gui.redraw(); // Update result of clicks
	}

	private int To1D(int x, int y) {
		// Convert from a 2D system to the LinkedList index
		return ((model.getSize() * x) + y);
	}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}