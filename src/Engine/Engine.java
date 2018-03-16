package Engine;

import static Config.EngineConfig.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static Config.ChunkConfig.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ChunkSystem.ChunkManagement;
import ChunkSystem.Chunk;
import ChunkSystem.ChunkLoader;
import Render.BatchRender;
import Texture.Texture;
import Texture.TextureLoader;

public class Engine extends Camera
{
	/**
	 * Screen min Y value
	 */
	private int screenMinY;

	
	/**
	 * Screen max Y value
	 */
	private int screenMaxY;
	
	/**
	 * Screen min X value
	 */
	private int screenMinX;
	
	/**
	 * Screen max X value
	 */
	private int screenMaxX;
    
	/**
	 * Screen Y value with buffer
	 */
	private int viewBufferMinY;
	
	/**
	 * Screen Y value with buffer
	 */
	private int viewBufferMaxY;
    
    /**
	 * Screen X value with buffer
	 */
	private int viewBufferMinX;
    
    /**
	 * Screen X value with buffer
	 */
	private int viewBufferMaxX;
    
	/**
	 * Chunk manager
	 */
    private ChunkManagement chunkMan;
    
    /**
     * Array of chunks
     */
    private ArrayList<Chunk> chunksRender;

    /**
     * Last time check
     */
	private double lastLoopTime;
	
	/**
	 * Time calculate
	 */
	private float timeDelta;

	/**
	 * Batch render object
	 */
	private BatchRender Batch;
	
	private TextureLoader TLoader;
	
    /**
     * Constructor class
     */
	public Engine()
	{
		//	Initialize data
		super();
		chunkMan     = new ChunkManagement();
		chunksRender = new ArrayList<>();
		Batch        = new BatchRender();
		TLoader      = new TextureLoader();
		
		//	Set camera data
		setMaxMoveX(chunkMan.getWorldSizeX());
		setMaxMoveY(chunkMan.getWorldSizeY());
		
		try 
		{
			Texture nTexture = TLoader.LoadTexturePNG("0.png");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("Texture ERROR-!> " + e.getMessage());
		}
	}
	
	/**
	 * Update chunks when user move
	 */
	public void updatePosition()
	{
		// Screen areas
	 	screenMinY     = cameraMovedPositionY() - (screenHeight / 2);
	    screenMaxY     = cameraMovedPositionY() + (screenHeight / 2);
	    screenMinX     = cameraMovedPositionX() - (screenWidth / 2);
	    screenMaxX     = cameraMovedPositionX() + (screenWidth / 2);

	    // Screen with buffer
	    viewBufferMinY = screenMinY - tileBufferSize;
	    viewBufferMaxY = screenMaxY + tileBufferSize;
	    viewBufferMinX = screenMinX - tileBufferSize;
	    viewBufferMaxX = screenMaxX + tileBufferSize;
	    
	    //	Loop screen
	    for (int Y = viewBufferMinY; Y < viewBufferMaxY; Y++) 
		{
			for (int X = viewBufferMinX; X < viewBufferMaxX; X++) 
			{
				if (chunkMan.validChunkArea(X, Y))
				{
					//	Get chunk by position
					int chunkID    = chunkMan.getChunk(X, Y);
					Chunk objChunk = chunkMan.getChunkByID(chunkID);
					
					//	Add chunk to list
					if (!chunksRender.contains(objChunk))
					{
						chunksRender.add(objChunk);
						Thread thChunk = new Thread(new ChunkLoader(objChunk));
						thChunk.start();
					}
				}
			}
		}
	}
	
	/**
	 * Engine chunk render
	 */
	public void EngineRender()
	{
		for (Chunk cChunk : chunksRender) 
		{
			if (cChunk.chunkInCamera(viewBufferMinX, viewBufferMinY, viewBufferMaxX, viewBufferMaxY))
			{
				if (cChunk.isInMemory())
				{

					//System.out.println("Render-> " + cChunk.getID());
					Batch.CreateVertexBuffer(cChunk, getCameraOffSetX() - 288, getCameraOffSetY() - 288);
				
				}
			}
			else
			{
				
				//System.out.println("Remove of memory-> " + cChunk.getID());
				
			}	
		}
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
	
	/**
	 * Convert tile position to isometric view
	 * @param X Position X
	 * @param Y Position Y
	 * @return Isometric tileView
	 */
	private float convertIsoTileViewX(int X, int Y)
	{
		
		return X * isoTileWidth + (Y & 1) * halfIsoTileWidth;
		
	}

	/**
	 * Convert tile position to isometric view
	 * @param Y Position Y
	 * @return Isometric tileView
	 */
	private float convertIsoTileViewY(int X, int Y)
	{
		
		return Y * halfIsoTileHeight;
		
	}
	
	/**
	 * Camera update
	 * @param Heading Move heading
	 */
	public void EngineUpdateCamera(int Heading)
	{
		//	Camera move screen
		MoveScreen(Heading);
		
		//	Camera calculate offset
		CameraCalculateOffSet(timeDelta);
		
		//	Update render
		updatePosition();
	}
	
	/**
     * Returns the time that have passed since the last loop.
     *
     * @return Delta time in seconds
     */
    public float getDelta() 
    {
        double time = getTime();
        float delta = (float) (time - lastLoopTime);
        lastLoopTime = time;
        
        return delta;
    }
	
    /**
     * Get glfw time
     * @return float time
     */
	public double getTime() 
	{
		
	    return glfwGetTime();
	    
	}
	
	
	
	
	
	
	//--------> BASURA
	private void glRenderRECT(float X, float Y)
	{
		//GL11.glColor3f(1f, 1f, 0f);
		GL11.glBegin(GL11.GL_QUADS);
		
			GL11.glColor3d(randInt(0.1, 1.0), randInt(0.1, 1.0), randInt(0.1, 1.0));
			GL11.glVertex2f(X, Y);
			
			GL11.glColor3d(randInt(0.1, 1.0), randInt(0.1, 1.0), randInt(0.1, 1.0));
			GL11.glVertex2f(X, Y + 32);
			
			GL11.glColor3d(randInt(0.1, 1.0), randInt(0.1, 1.0), randInt(0.1, 1.0));
			GL11.glVertex2f(X + 32, Y + 32);
			
			GL11.glColor3d(randInt(0.1, 1.0), randInt(0.1, 1.0), randInt(0.1, 1.0));
			GL11.glVertex2f(X + 32, Y);
			
		GL11.glEnd();
	}

	
	public double randInt(double d, double e) {

	    Random rand = new Random();

	    double result = rand.nextDouble() * (e - d) + d;

	    return result;

	}
}
