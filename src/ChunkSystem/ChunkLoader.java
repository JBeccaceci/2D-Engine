package ChunkSystem;

public class ChunkLoader implements Runnable
{
	/**
	 * Chunk
	 */
	private Chunk currentChunk;
	
	/**
	 * Constructor class
	 * @param _chunk currentChunk
	 */
	public ChunkLoader(Chunk _chunk)
	{
		
		this.currentChunk = _chunk;
		
	}
	
	@Override
	public void run() 
	{
		
		currentChunk.LoadChunk();
		currentChunk.setInMemory(true);
		
	}
	
	
}
