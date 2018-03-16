package ChunkSystem;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import static Config.ChunkConfig.*;

public class ChunkManagement 
{
	/**
	 * Array of chunks
	 */
	private Chunk arrChunks[];
	
	/**
	 * Number of chunks
	 */
	private int numChunks;
	
	/**
	 * World size X
	 */
	private int worldSizeX;
	
	/**
	 * World size Y
	 */
	private int worldSizeY;
	
	/**
	 * Chunk matrix
	 */
	private int[][] mChunks;
	
	/**
	 * Chunks in X axis
	 */
	private int chunksInX;
	
	/**
	 * Chunk in Y axis
	 */
	private int chunksInY;
	
	/**
	 * Constructor class
	 */
	public ChunkManagement()
	{
		try 
		{
			LoadChunkFile();
		} 
		catch (IOException | ParseException e) 
		{
			System.out.println("Error load/parse JSON-> " + e.getMessage());
		}
	}
	
	/**
	 * Load JSON file
	 * @throws IOException IO File exception
	 * @throws ParseException JSON parser exception
	 */
	@SuppressWarnings("deprecation")
	private void LoadChunkFile() throws IOException, ParseException
	{
		InputStream inputFile = ClassLoader.getSystemResourceAsStream("chunkAreas.json");
		String JSONString     = IOUtils.toString(inputFile);
		JSONParser JParser    = new JSONParser();
		
		//	Parse JSON
		JSONObject mainJSON   = (JSONObject) JParser.parse(JSONString);

		//	Get JSON data
		numChunks             = Integer.parseInt((String) mainJSON.get("numChunks"));
		worldSizeX            = Integer.parseInt((String) mainJSON.get("worldX"));
		worldSizeY            = Integer.parseInt((String) mainJSON.get("worldY"));
		chunksInX             = worldSizeX / chunkSizeX;
		chunksInY             = worldSizeY / chunkSizeY;
		
		//	Allocate chunks
		arrChunks             = new Chunk[numChunks];
		
		//	JSON loop array
		JSONArray mainArray   = (JSONArray) mainJSON.get("Chunks");
		for(int i = 0; i < numChunks; i++)
		{
			JSONObject JChunk = (JSONObject) mainArray.get(i);
			int ID            = Integer.parseInt((String) JChunk.get("ID"));
			int X1            = Integer.parseInt((String) JChunk.get("X1"));
			int Y1            = Integer.parseInt((String) JChunk.get("Y1"));
			int X2            = Integer.parseInt((String) JChunk.get("Y1"));
			int Y2            = Integer.parseInt((String) JChunk.get("Y2"));
			arrChunks[i]      = new Chunk(ID, X1, X2, Y1, Y2);
		}
		
		/**!
		 * !Create areas
		 */
		createChunksAreas();
	}
	
	/**
	 * Get chunk by ID
	 * @param ID ID of chunk
	 * @return Chunk object
	 */
	public Chunk getChunkByID(int ID)
	{
		if (ID <= numChunks)
		{
			return arrChunks[ID];
		}
		
		return null;
	}
	
	/**
	 * Get chunkID by position
	 * @param X Position X
	 * @param Y Position Y
	 * @return ID of chunks
	 */
	public int getChunk(int X, int Y)
	{

		return mChunks[X][Y];
		
	}
	
	/**
	 * Get world size X
	 * @return worldSizeX
	 */
	public int getWorldSizeX() 
	{
		
		return worldSizeX;
		
	}

	/**
	 * Get world size Y
	 * @return worldSizeX
	 */
	public int getWorldSizeY() 
	{
		
		return worldSizeY;
		
	}
	
	/**
	 * Create chunks areas
	 */
	private void createChunksAreas()
	{
		int cNum = 0;
		
		/**!
		 * !Allocate array
		 */
		mChunks = new int[worldSizeX + 1][worldSizeY + 1];
		
		/**!
		 * !Set chunks ID
		 */
		for (int mapY = 0; mapY <= chunksInY - 1; mapY++)
		{
			for  (int mapX = 0; mapX <= chunksInX - 1; mapX++)
			{
				for (int j = (mapY * chunkSizeY) + 1; j <= (mapY * chunkSizeY) + chunkSizeY; j++)
				{
					for (int i = (mapX * chunkSizeX) + 1; i <= (mapX * chunkSizeX) + chunkSizeX; i++)
					{
						if (j <= worldSizeY && i <= worldSizeX)
						{
							mChunks[i][j] = cNum;
						}
					}
				}
				cNum++;
			}
		}
	}
	
	/**
	 * Valid chunk area
	 * @param X Position X
	 * @param Y Position Y
	 * @return Boolean value
	 */
	public boolean validChunkArea(int X, int Y)
	{
		
		return (X > 0) && (Y > 0) && (X <= worldSizeX) && (Y <= worldSizeY);
		
	}
}
