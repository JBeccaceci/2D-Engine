package Engine;

public class Camera 
{
	/**
	 * Camera position
	 */
	private Position cameraPos;
	
	/**
	 * Camera add position
	 */
	private Position cameraAddPos;
	
	/**
	 * North direction
	 */
	public final static int NORTH = 1;
	
	/**
	 * East direction
	 */
	public final static int EAST = 2;
	
	/**
	 * South direction
	 */
	public final static int SOUTH = 3;
	
	/**
	 * West direction
	 */
	public final static int WEST = 4;
	
	/**
	 * Camera move
	 */
	private boolean cameraMove;
	
	/**
	 * Camera offset X
	 */
	private float cameraOffSetX;
	
	/**
	 * Camera offset Y
	 */
	private float cameraOffSetY;
	
	/**
	 * Move pixel per frame
	 */
	private final int PIXEL_PER_FRAME = 4;
	
	/**
	 * Max X position
	 */
	private int maxMoveX;
	
	/**
	 * Max Y position
	 */
	private int maxMoveY;
	
	/**
	 * Construct function
	 */
	public Camera()
	{
		
		cameraPos    = new Position(1, 1);
		cameraAddPos = new Position(0, 0);
		
	}

	/**
	 * Get camera position X
	 * @return int Value
	 */
	public int getCameraPosX() 
	{
		
		return cameraPos.getX();
		
	}
	
	/**
	 * Get camera position Y
	 * @return int Value
	 */
	public int getCameraPosY() 
	{
		
		return cameraPos.getY();
		
	}
	
	/**
	 * Set camera position
	 * @param cameraPos Object position
	 */
	public void setCameraPos(Position cameraPos) 
	{
		
		this.cameraPos = cameraPos;
		
	}
	
	/**
	 * Update camera X value
	 * @param X Value
	 */
	public void setCameraPosX(int X)
	{
		
		this.cameraPos.setX(X);
		
	}
	
	/**
	 * Update camera Y value
	 * @param Y Value
	 */
	public void setCameraPosY(int Y)
	{
		
		this.cameraPos.setY(Y);
		
	}
	
	/**
	 * Camera move by heading
	 * @param Heading Heading
	 */
	public void MoveScreen(int Heading)
	{
		int X  = 0;
		int Y  = 0;
		int tX = 0;
		int tY = 0;
		
		//	Camera direction
		switch(Heading)
		{
			case NORTH:
				Y = -1;
				break;
				
			case EAST:
				X = 1;
				break;
				
			case SOUTH:
				Y = 1;
				break;
				
			case WEST:
				X = -1;
				break;
		}
		
		//	Calculate camera posX
		tX = getCameraPosX() + X;
		
		//	Calculate camera posY
		tY = getCameraPosY() + Y;
		
		//	Move camera
		if (validMove(tX, tY))
		{
			//	Update camera posX
			cameraAddPos.setX(X);
			setCameraPosX(tX);
			
			//	Update camera posY
			cameraAddPos.setY(Y);
			setCameraPosY(tY);
			
			//	Move camera
			cameraMove = true;
			
			System.out.println("MoveX-> " + tX + " MoveY-> " + tY);
		}

	}
	
	/**
	 * Camera calculate offset when move
	 * @param timeDelta Engine time
	 */
	public void CameraCalculateOffSet(float timeDelta)
	{
		if (cameraMove)
	    {
	    	if (cameraAddPos.getX() != 0)
	    	{
	    		cameraOffSetX = cameraOffSetX - (PIXEL_PER_FRAME + 10) * cameraAddPos.getX() * timeDelta;
	    		if (Math.abs(cameraOffSetX) >= Math.abs(32 * cameraAddPos.getX()))
	    		{
	    			cameraOffSetX = 0;
	    			cameraAddPos.setX(0);
	    			cameraMove  = false;
	    		}
	    	}
	    	
	    	if (cameraAddPos.getY() != 0)
	    	{
	    		cameraOffSetY = cameraOffSetY - (PIXEL_PER_FRAME + 10) * cameraAddPos.getY() * timeDelta;
	    		if (Math.abs(cameraOffSetY) >= Math.abs(32 * cameraAddPos.getY()))
	    		{
	    			cameraOffSetY = 0;
	    			cameraAddPos.setY(0);
	    			cameraMove  = false;
	    		}
	    	}
	    }
	}
	
	/**
	 * Get camera calculated position X
	 * @return X position
	 */
	public int cameraMovedPositionX()
	{
		
		return getCameraPosX() - cameraAddPos.getX();
		
	}
	
	/**
	 * Get camera calculated position Y
	 * @return Y position
	 */
	public int cameraMovedPositionY()
	{
		
		return getCameraPosY() - cameraAddPos.getY();
		
	}
	
	/**
	 * Get camera offset X
	 * @return X offset
	 */
	public float getCameraOffSetX()
	{
		
		return cameraOffSetX;
		
	}
	
	/**
	 * Get camera offset Y
	 * @return Y offset
	 */
	public float getCameraOffSetY()
	{
		
		return cameraOffSetY;
		
	}
	
	/**
	 * Set maxMove position X
	 * @param maxMoveX Move value
	 */
	public void setMaxMoveX(int maxMoveX) 
	{
		
		this.maxMoveX = maxMoveX;
		
	}

	/**
	 * Set maxMove position Y
	 * @param maxMoveY Move value
	 */
	public void setMaxMoveY(int maxMoveY) 
	{
		
		this.maxMoveY = maxMoveY;
		
	}
	
	/**
	 * Camera valid movement
	 * @param moveX MoveTo X
	 * @param moveY MoveTo Y
	 * @return Boolean value
	 */
	protected boolean validMove(int moveX, int moveY)
	{
		
		return (moveX > 0) && (moveY > 0) && (moveX <= maxMoveX) && (moveY <= maxMoveY);
		
	}
}
