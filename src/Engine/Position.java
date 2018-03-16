package Engine;

public class Position 
{
	/**
	 * Position X
	 */
	private int X;
	
	/**
	 * Position Y
	 */
	private int Y;
	
	/**
	 * Constructor class
	 * @param _x X position
	 * @param _y Y position
	 */
	public Position(int _x, int _y)
	{
		this.X = _x;
		this.Y = _y;
	}
	
	/**
	 * Get X position
	 * @return X value
	 */
	public int getX() 
	{
		
		return X;
		
	}
	
	/**
	 * Get Y position
	 * @return Y value
	 */
	public int getY() 
	{
		
		return Y;
		
	}
	
	/**
	 * Set X position
	 * @param X Value
	 */
	public void setX(int X)
	{
		
		this.X = X;
		
	}
	
	/**
	 * Set Y position
	 * @param Y Value
	 */
	public void setY(int Y)
	{
		
		this.Y = Y;
		
	}
}
