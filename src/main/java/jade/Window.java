package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final int width,height;
    private final String title;
    private long glfwWindow;
    private static Window window=null;
    private static Scene currentScene = null;
    public float r,g,b,a;
    private boolean fadeToBlack=false;

    private Window(){
        this.height=1920;
        this.width=1080;
        this.title="Penguin Bros";
        r=1;
        g=1;
        b=1;
        a=1;
    }

    public static Window get(){
        if(Window.window==null){
            Window.window=new Window();
        }
        return Window.window;
    }

    public static void changeScene( int newScene){
        switch (newScene){
            case 0 :
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1 :
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene '"+ newScene +"'";
                break;
        }
    }

    public void run(){
        System.out.println("Hello LWJGL" + Version.getVersion() +"!");

        init();

        loop();
        //Free the memory
        glfwFreeCallbacks(glfwWindow);
        GLFW.glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and the free error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

    }

    /**
     * this method create the window
     */
    public void init(){
        //Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();
        //Initialize GLFW
        if(!GLFW.glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //Configure GLFW
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE,GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE,GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED,GLFW.GLFW_TRUE);
        //Create the window
        glfwWindow=GLFW.glfwCreateWindow(this.width,this.height,this.title,NULL,NULL);
        if(glfwWindow==NULL){
            throw  new IllegalStateException("Failed to create the GLFW window");
        }
        //Mouse
        GLFW.glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        GLFW.glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        GLFW.glfwSetScrollCallback(glfwWindow,MouseListener::mouseScrollCallBack);
        //Keyboard
        GLFW.glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);

        //Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(glfwWindow);
        //Enable v-sync
        GLFW.glfwSwapInterval(1);
        //Make the window visible
        GLFW.glfwShowWindow(glfwWindow);

        //This line is critical for LWJGL's interoperation with GLFW's
        //OpenGL context , or any context that is managed externally.
        //LWJGL detects the context that is current in the current thread,
        //create the GLCapabilities instance and makes the OpenGL
        //bindings available for use.
        GL.createCapabilities();

        Window.changeScene(0);

    }

    /**
     * this method allows that the window stay open
     */
    public void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;
        boolean esc=false;

        //stay open while the windows should not close
        while (!GLFW.glfwWindowShouldClose(glfwWindow) && !esc){
            //Pool events
            GLFW.glfwPollEvents();

            //set the color
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            //is to change the color of the window to black
            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_ESCAPE)){
                 esc=true;
            }

            //to change the Scene
            if(dt>=0) {
                currentScene.update(dt);
            }

            GLFW.glfwSwapBuffers(glfwWindow);
            endTime= Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;

        }
    }
}
