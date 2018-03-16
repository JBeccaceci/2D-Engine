package Texture;

import static Config.ChunkConfig.chunkArea;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TextureLoader
{
	/**
	 * Load OpenGL texture
	 * @param tName
	 * @return
	 * @throws IOException 
	 */
	public Texture LoadTexturePNG(String tName) throws IOException
	{
		//	Load Stream resource
		ClassLoader classLoader = new TextureLoader().getClass().getClassLoader();
		PNGDecoder fileDecoder  = new PNGDecoder(classLoader.getResourceAsStream(tName));
		
		//	Texture data
		int fileWidth  = fileDecoder.getWidth();
		int fileHeight = fileDecoder.getHeight();
		
		//	Create buffer
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * fileWidth * fileHeight);
		//ByteBuffer byteBuffer = BufferUtils.createByteBuffer(4 * fileWidth * fileHeight);
		
		//	Read data
		fileDecoder.decode(byteBuffer, fileWidth * 4, PNGDecoder.Format.RGBA);
		byteBuffer.flip();
		
		//	Generate texture ID
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		int textureID = GL11.glGenTextures();
		
		//	Set texture in OpenGL
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
		//	Texture params
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
	    
		//	Upload texture
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, 
						  fileWidth, fileHeight, 0, 
				          GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, 
				          byteBuffer);
		
		//	Generate mipmap
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		//	New texture
		return new Texture(TextureInfo.TEXTURE_PNG, textureID, fileWidth, fileHeight);
	}
	
}
