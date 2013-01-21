package life;

import java.util.LinkedList;

public class Model implements Runnable {

	/*
	 * Encapsulates states and program rules.
	 */
	
	private int size;
	boolean wrap;
	LinkedList<CellStatusNode> csLL;
	
	Model() {
		this.size = 30; // Default value according to model.
		this.wrap = true;
		csLL = createCSLL();
	}
	
	Model(int size, boolean wrap) {
		this.size = size;
		this.wrap = wrap;
		csLL = createCSLL();
	}

	LinkedList<CellStatusNode> createCSLL(){
		LinkedList<CellStatusNode> newCSLL = new LinkedList<CellStatusNode>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				CellStatusNode csn = new CellStatusNode(i, j, State.BLANK);
				newCSLL.add(csn);
			}
		}
		return newCSLL;
	}
	
	void setSize(int size) {
		this.size = size;
		csLL = createCSLL();
	}
	
	int getSize() {
		return this.size;
	}
	
    public void run() {
        View gui = new View(size, this);
        gui.display();
    }
    
    public int[] countData(int x, int y) {
    	int count = 0;
    	int countP1 = 0;
    	int countP2 = 0;
    	for (int i = 0; i < 3; i++) {
    		for (int j = 0; j < 3; j++) {
    			if (!((i == 1) && (j == 1))) { // Ignore middle cell
    				int wrappedX = wrap(x + i - 1);
        			int wrappedY = wrap(y + j - 1);
        			State cellState = csLL.get(To1D(wrappedX, wrappedY)).state;
            		if (cellState != State.BLANK) {
            			// Then it's either P1 (red) or P2 (green)
            			count++;
            			if (cellState == State.P1){
            				countP1++;
            			}
            			if (cellState == State.P2){
            				countP2++;
            			}
            		}
    			}
        	}
    	}
		return new int[] {count, countP1, countP2};
    }
    
    public int getNeighbourCount(int x, int y) {
    	return countData(x, y)[0];
    }
    
    public State getDominant(int x, int y) {
    	// TODO not call twice
    	int[] dominant = countData(x, y);
    	if (dominant[1] > dominant[2]) { // More reds than greens
    		// Note, for creation, neighbour count must be 3, so can't be equal
    		return State.P1;
    	} else {
    		return State.P2;
    	}
    }
    
    public State nextState(int x, int y, State s) {
    	State next = State.BLANK;
    	int count = getNeighbourCount(x, y);
    	
    	/*
    	 * Implementation of the rules:
    	 * 
    	 * A cell surrounded by two or three neighbouring cells will survive.
    	 * A cell with one or no neighbours dies from isolation.
    	 * A cell with four or more neighbours suffocates and dies.
    	 * A cell is born in an empty square surrounded by exactly three cells.
    	 * 
    	 * Cells are coloured either red or green and when a new cell is born,
    	 * it inherits the majority colour of its surrounding cells.
    	 */
    	
    	switch (s) {
	    	case BLANK: {
	    		if (count == 3) { // Birth (into dominant colour)
	    			next = getDominant(x, y);
	    		}
	    		break;
	    	}
	    	default: {
	    		switch (count) {
		    		case 0: // Isolation
		    		case 1: next = State.BLANK; break;
		    		
		    		case 2: // Survival
		    		case 3: next = s; break;
		    		
		    		case 4: // Suffocation
		    		case 5:
		    		case 6:
		    		case 7:
		    		case 8: next = State.BLANK; break; 
		    		default: break;
	    		}
	    		break;
	    	}
    	}
		return next;
    }
    
    private int To1D(int x, int y) {
		// Convert from a 2D system to the LinkedList index
		return ((size * x) + y);
	}
    
    private int wrap(int n) {
    	if (wrap) {
    		n += size;
    		return (n % size);
    	} else {
    		// Experimental
    		if (n < 0 || n > size - 1) {
    			return 0;
    		} else {
    			return n;
    		}
    	}
    }

	public void update() {
		LinkedList<CellStatusNode> newLL = createCSLL();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				CellStatusNode csn =
								(CellStatusNode)csLL.get(To1D(i, j)).clone();
				csn.state = nextState(i, j, csn.state);
				newLL.set(To1D(i, j), csn);
			}
		}
		csLL = newLL;
	}
}