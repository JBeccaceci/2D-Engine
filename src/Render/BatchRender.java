package Render;

import static Config.ChunkConfig.chunkArea;
import static Config.EngineConfig.tileSize;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.GL11;

import ChunkSystem.Chunk;
import Engine.Position;

public class BatchRender 
{
	/**
	 * Vertex X, Y format
	 */
	private final int VERTEX_FORMAT_XY = 3;
	
	/**
	 * RGB Color format
	 */
	private final int COLOR_FORMAT_RGB = 3;
	
	/**
	 * Vertex buffer ID
	 */
	private int vbVID;
	
	/**
	 * Color buffer ID
	 */
	private int vbCID;
	
	/**
	 * Texture buffer ID
	 */
	private int vbTID;
	
	/**
	 * Constructor
	 * @param _chunk Current chunk
	 */
	public BatchRender()
	{
		
		this.vbVID = 0;
		this.vbCID = 0;
		this.vbTID = 0;
		
	}

	/**
	 * Create and render vertex buffer
	 * @param chunkRender Chunk to render
	 * @param offsetX Camera offsetX
	 * @param offsetY Camera offsetY
	 */
	public void CreateVertexBuffer(Chunk chunkRender, float offsetX, float offsetY)
	{
		//	Create vertex buffer
		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(chunkArea * VERTEX_FORMAT_XY * 4);
		FloatBuffer cBuffer = BufferUtils.createFloatBuffer(chunkArea * COLOR_FORMAT_RGB * 4);
		FloatBuffer tBuffer = BufferUtils.createFloatBuffer(chunkArea * COLOR_FORMAT_RGB * 4);
		
		int TexturePositionX = 0;
		int TexturePositionY = 0;
		int TextureDimensionX = 32;
		int TextureDimensionY = 32;
		
		//	Loop vertex
		for (int Layer = 0; Layer < chunkArea; Layer++)
		{
			Position grhPos = chunkRender.getGrhPosition(Layer);
			
			//	Create tile view position
			float pixelX = convertTileToViewX(grhPos.getX()) + chunkRender.getX1();
			float pixelY = convertTileToViewY(grhPos.getY()) + chunkRender.getY1();
			
			//	Final position
			float positionX = pixelX + offsetX;
			float positionY = pixelY + offsetY;
			
			//	Add to buffer
			vBuffer.put(positionX).put(positionY).put(0.0f);
			vBuffer.put(positionX).put(positionY + 32).put(0.0f);
			vBuffer.put(positionX + 32).put(positionY + 32).put(0.0f);
			vBuffer.put(positionX + 32).put(positionY).put(0.0f);
			
			//	Color buffer
			cBuffer.put(1).put(0).put(1);
			cBuffer.put(0).put(1).put(1);
			cBuffer.put(0).put(1).put(0);
			cBuffer.put(1).put(1).put(1);
			
			//	Texture buffer
			tBuffer.put((TexturePositionX / TextureDimensionX)).put((TexturePositionY / TextureDimensionY)).put(0);
			tBuffer.put((TexturePositionX + 32) / TextureDimensionX).put(TexturePositionY / TextureDimensionY).put(0);
			tBuffer.put(TexturePositionX / TextureDimensionX).put((TexturePositionY + 32) / TextureDimensionY).put(0);
			tBuffer.put((TexturePositionX + 32) / TextureDimensionX).put((TexturePositionY + 32) / TextureDimensionY).put(0);
		}
		
		//	Flip buffer
		vBuffer.flip();
		cBuffer.flip();
		tBuffer.flip();
		
		//	Int buffer
		IntBuffer vbO = BufferUtils.createIntBuffer(3);
		
		//	Generate buffer
		ARBVertexBufferObject.glGenBuffersARB(vbO);
		
		//	Get buffer
		vbVID = vbO.get(0);
		vbCID = vbO.get(1);
		vbTID = vbO.get(2);
		
		//	Enable color and vertex buffer
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		//	Bind texture
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		//	Generate vertex buffer
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbVID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0L);
		
		//	Generate color buffer
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbCID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, cBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0L);
		
		//	Generate texture buffer
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbTID);
		ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, tBuffer, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
		GL11.glTexCoordPointer(3, GL11.GL_FLOAT, 0, 0L);
			
		//	Draw data
		GL11.glDrawArrays(GL11.GL_QUADS, 0, chunkArea * 4);
		ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
		
		//	Disable buffers
		GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
		GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
		
		//	Delete buffers
		vbO.put(0, vbVID);
		vbO.put(1, vbCID);
		vbO.put(2, vbTID);
		vBuffer.clear();
		cBuffer.clear();
		tBuffer.clear();
		
		//	Delete OpenGL buffers
		ARBVertexBufferObject.glDeleteBuffersARB(vbO);
	}
	
	/**
	 * Convert X position in screen position
	 * @param X Position
	 * @return Screen position
	 */
	private int convertTileToViewX(int X)
	{

		return X * tileSize;

	}
	
	/**
	 * Convert Y position in screen position
	 * @param Y Position
	 * @return Screen position
	 */
	private int convertTileToViewY(int Y)
	{

		return Y * tileSize;
		
	}
}
