package Texture;

public class Texture extends TextureAction
{
	/**
	 * Texture type
	 */
	private int tType;
	
	/**
	 * Texture ID
	 */
	private int tID;
	
	/**
	 * Texture Width
	 */
	private int Width;
	
	/**
	 * Texture height
	 */
	private int Height;
	
	
	/**
	 * Constructor class
	 * @param _type   Texture type
	 * @param _id     Texture ID
	 * @param _width  Texture Width
	 * @param _height Texture Height
	 */
	public Texture(int _type, int _id, int _width, int _height)
	{
		
		super(_id);
		this.tType  = _type;
		this.tID    = _id;
		this.Width  = _width;
		this.Height = _height;
		
	}
	
	
	/**
	 * Get texture type
	 * @return Texture type
	 */
	public int gettType() 
	{
		
		return tType;
		
	}

	/**
	 * Set texture type
	 * @param tType Texture Type
	 */
	public void settType(int tType) 
	{
		
		this.tType = tType;
		
	}

	/**
	 * Set texture ID
	 * @param tID
	 */
	public void settID(int tID) 
	{
		
		this.tID = tID;
		
	}

	/**
	 * Get texture ID
	 * @return Texture ID
	 */
	public int gettID() 
	{
		
		return tID;
		
	}
	
	/**
	 * Get texture width
	 * @return Texture Width
	 */
	public int getWidth() 
	{
		
		return Width;
		
	}

	/**
	 * Set texture width
	 * @param width Texture width
	 */
	public void setWidth(int width) 
	{
		
		Width = width;
		
	}

	/**
	 * Get texture height
	 * @return Texture height
	 */
	public int getHeight() 
	{
		
		return Height;
		
	}

	/**
	 * Set texture height
	 * @param height Texture height
	 */
	public void setHeight(int height) 
	{
		
		Height = height;
		
	}
}
