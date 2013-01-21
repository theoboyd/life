package life;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

class ButtonClick implements MouseListener {

	private View gui;
	
	ButtonClick(View gui) {
		this.gui = gui;
	}
	
	public void mouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1) { // Left click
			JButton b = (JButton)e.getSource();
			JPanel j = (JPanel)b.getParent();
			if (b.getText() == "Clear") {
				gui.clear();
			} else if (b.getText() == "Step") {
				gui.step();
			} else if (b.getText() == "Run") {
				b.setText("Pause");
				j.getComponent(0).setEnabled(false);
				j.getComponent(1).setEnabled(false);
				j.getComponent(3).setEnabled(false);
				j.getComponent(4).setEnabled(false);
				j.getComponent(5).setEnabled(false);
				gui.running = true;
				gui.runSteps();
			} else if (b.getText() == "Pause") {
				b.setText("Run");
				j.getComponent(0).setEnabled(true);
				j.getComponent(1).setEnabled(true);
				j.getComponent(3).setEnabled(true);
				j.getComponent(4).setEnabled(true);
				j.getComponent(5).setEnabled(true);
				gui.running = false;
			} else if (b.getText() == "Stop") {
				gui.stop();
			} else if (b.getText() == "Resize") {
				gui.resize();
			}
		}
	}
	
	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}
}