package Texture;

import org.lwjgl.opengl.GL11;

public class TextureAction 	
{
	/**
	 * Texture ID
	 */
	private int tID;
	
	/**
	 * Constructor class
	 * @param _id Texture ID
	 */
	public TextureAction(int _id)
	{
		
		this.tID = _id;
		
	}
	
	/**
	 * Delete texture from memory
	 */
	public void TextureDelete()
	{
		
		GL11.glDeleteTextures(tID);
		
	}
}
