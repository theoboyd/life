package life;

import java.awt.Color;

public enum State {

	BLANK (Color.LIGHT_GRAY),
	P1 (Color.RED),
	P2 (Color.GREEN);
	
	private Color c;
	
	State(Color c) {
		this.c = c;
	}
	
	public Color getColour() {
		return c;
	}
}
