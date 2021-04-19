
public class DrawData {
	
	private final int[] points;
	private final int brushSize;
	private final int color;
	private final long timeStamp;
	
	/**
	 * This creates a DrawData object.
	 * @param p the list of integer points {xPrev, yPrev, xCurr, yCurr}
	 * @param b the size of the brush as an integer diameter
	 * @param c the color of the brush as an integer
	 */
	public DrawData(int[] p, int b, int c) {
		timeStamp = System.currentTimeMillis();
		points = p;
		brushSize = b;
		color = c;
	}
	
	/**
	 * Returns the coordinate points in the order xPrev, yPrev, xCurr, yCurr
	 * @return the list of integer points {xPrev, yPrev, xCurr, yCurr}
	 */
	public int[] getPoints() {
		return points;
	}
	
	/**
	 * Returns the size of the brush
	 * @return the size of the brush as an integer diameter
	 */
	public int getBrushSize() {
		return brushSize;
	}
	
	/**
	 * Returns the color of the brush as defined by GUI.<some color>
	 * @return the color of the brush as an integer
	 */
	public int getColor() {
		return color;
	}
	
	/**
	 * Returns the time of creation
	 * @return the long System.currTimeMillis() at creation
	 */
	public long getTimeStamp() {
		return timeStamp;
	}

}
