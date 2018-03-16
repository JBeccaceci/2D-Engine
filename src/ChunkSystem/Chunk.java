package ChunkSystem;

import Engine.Position;
import static Config.ChunkConfig.*;

public class Chunk 
{
	/**
	 * Chunk ID
	 */
	private int ID;
	
	/**
	 * Chunk start position in X axis
	 */
	private int X1;
	
	/**
	 * Chunk start position in Y axis
	 */
	private int Y1;
	
	/**
	 * Chunk end position in X axis
	 */
	private int X2;
	
	/**
	 * Chunk end position in Y axis
	 */
	private int Y2;
	
	/**
	 * Array of GRH position (CHANGE POSITION TO MAPBLOCK)
	 */
	private Position Grh[];
	
	/**
	 * Chunk in memory
	 */
	private boolean inMemory;
	
	/**
	 * Constructor class
	 * @param _id ID of chunk
	 * @param _x1 Chunk start position in X axis
	 * @param _x2 Chunk end position in X axis
	 * @param _y1 Chunk start position in Y axis
	 * @param _y2 Chunk end position in Y axis
	 */
	public Chunk(int _id, int _x1, int _x2, int _y1, int _y2)
	{
		this.ID  = _id;
		this.X1  = _x1;
		this.Y1  = _y1;
		this.X2  = _x2;
		this.Y2  = _y2;
		this.Grh      = new Position[chunkArea];
		this.inMemory = false;
	}
	
	/**
	 * Load chunk data
	 */
	public void LoadChunk()
	{
		for (int Y = 0; Y < chunkSizeY; Y++) 
		{
			for (int X = 0; X < chunkSizeX; X++) 
			{
				Grh[Y * chunkSizeX + X] = new Position(X, Y);
			}
		}
	}
	
	/**
	 * Set memory flag
	 * @param inMem Boolean value
	 */
	public void setMemory(boolean inMem)
	{
		
		inMemory = inMem;
		
	}
	
	/**
	 * Chunk in area visiom
	 * @param cameraX1 Camera minViewX
	 * @param cameraY1 Camera minViewY
	 * @param cameraX2 Camera maxViewX
	 * @param cameraY2 Camera maxViewY
	 * @return Chunk in area vision
	 */
	public boolean chunkInCamera(int cameraX1, int cameraY1, int cameraX2, int cameraY2)
	{

	    return (cameraX1 < this.X1 + chunkSizeX) && (cameraX2 > this.X1) && (cameraY1 < this.Y1 + chunkSizeY) && (cameraY2 > this.Y1);

	}

	/**
	 * Get chunk memory value
	 * @return Chunk memory flag
	 */
	public boolean isInMemory() 
	{
		
		return inMemory;
		
	}
	
	/**
	 * Set chunk memory flag
	 * @param inMemory Memory flag
	 */
	public void setInMemory(boolean inMemory) 
	{
		
		this.inMemory = inMemory;
		
	}
	
	/**
	 * Get grh position object
	 * @param Position Value
	 * @return Position object
	 */
	public Position getGrhPosition(int Position)
	{
		
		return Grh[Position];
		
	}
	
	/**
	 * Chunk start position in X axis
	 * @return X1 position
	 */
	public int getX1() 
	{
		
		return X1;
		
	}

	/**
	 * Chunk start position in Y axis
	 * @return Y1 position
	 */
	public int getY1() 
	{
		
		return Y1;
		
	}
	
	/**
	 * Chunk end position in X axis
	 * @return X2 position
	 */
	public int getX2() 
	{
		
		return X2;
		
	}
	
	/**
	 * Chunk end position in Y axis
	 * @return Y2 position
	 */
	public int getY2() 
	{
		
		return Y2;
		
	}
	
	/**
	 * Get chunk ID
	 * @return ID value
	 */
	public int getID() 
	{
		
		return ID;
		
	}
}
