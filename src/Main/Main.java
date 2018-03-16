package Main;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import Engine.Camera;
import Engine.Engine;
import Input.KeyboardHandler;

public class Main 
{

	// The window handle
	private long window;
	
	private GLFWKeyCallback keyCallback;
	
	private Engine cEngine;
	
	double fpsLastCheck;
	int FramesPerSecCounter = 0;
	int FPS;
	
	public void run() 
	{
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		
		cEngine = new Engine();
		
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	private void init() 
	{
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizablez

		// Create the window
		window = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		
		glfwSetKeyCallback(window, keyCallback = new KeyboardHandler());

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() 
	{
		GL.createCapabilities();
		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glOrtho(0, 737, 544, 0, -1, 1);
		//GL11.glTranslatef(Render.getCameraPosX() * 32, Render.getCameraPosY() * 32, 0f);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
	
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		fpsLastCheck = glfwGetTime();
		while ( !glfwWindowShouldClose(window) ) 
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1.0) {
				//update();
				updates++;
				delta--;
			}
			
			cEngine.EngineRender();
			
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println(updates + " ups, " + frames + " fps");
				updates = 0;
				frames = 0;
			}
			//	RENDER
			
			glfwSwapBuffers(window); // swap the color buffers
		
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			updateKeys();
		}
	}
	
	public static void main(String[] args) 
	{
	    
		new Main().run();
		
	}
	
	public void updateKeys()
	{

    	if(KeyboardHandler.isKeyDown(GLFW_KEY_W))
    	{
    		cEngine.EngineUpdateCamera(Camera.NORTH);
    		//cEngine.updatePosition();
    	}
    	else if (KeyboardHandler.isKeyDown(GLFW_KEY_A))
    	{
    		cEngine.EngineUpdateCamera(Camera.WEST);
    	}
    	else if (KeyboardHandler.isKeyDown(GLFW_KEY_D))
    	{
    		cEngine.EngineUpdateCamera(Camera.EAST);
    	}
    	else if (KeyboardHandler.isKeyDown(GLFW_KEY_S))
    	{
    		cEngine.EngineUpdateCamera(Camera.SOUTH);
    	}
    }
}
