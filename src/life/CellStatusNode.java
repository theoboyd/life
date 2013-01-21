package life;

import java.awt.Point;

public class CellStatusNode extends Point {

	/*
	 * Class to hold the status of a particular cell, and its button component.
	 */
	
	// Serialisation required by Java
	private static final long serialVersionUID = -8120376046568826647L;

	State state;
	
	// Set super's (Point's) coordinates, and also the colour.
	public CellStatusNode(int x, int y, State s) {
		super(x, y);
		state = s;
	}
	
	public void setState(State s) {
		state = s;
	}
}