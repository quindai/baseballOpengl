package course;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

public class DrawingView implements GLEventListener { 

	  
    private DrawingModel myModel;

    public DrawingView(DrawingModel model) {
        myModel = model;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {
    	
        GL2 gl = drawable.getGL().getGL2();
        
        Mouse.update(gl);
        
        //Ignore alpha values for now. Always set to 1
        gl.glClearColor(myModel.getBgColor()[0],myModel.getBgColor()[1],myModel.getBgColor()[2],1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        //Ignore alpha values only set rgb values.
        gl.glColor3fv(myModel.getColor(),0);
        
        for (List<Double> polygon : myModel.getPolygons()) {

            gl.glBegin(GL2.GL_POLYGON);

            for (int i = 0; i < polygon.size(); i += 2) {
                double x = polygon.get(i);
                double y = polygon.get(i + 1);

                gl.glVertex2d(x, y);
            }

            gl.glEnd();

        }

        // draw the working polygon

        List<Double> polygon = myModel.getWorkingPolygon();

        gl.glBegin(GL2.GL_LINE_STRIP);

        for (int i = 0; i < polygon.size(); i += 2) {
            double x = polygon.get(i);
            double y = polygon.get(i + 1);

            gl.glVertex2d(x, y);
        }

        // connect the mouse position if we are drawing

        if (!polygon.isEmpty()) {
            double[] p = myModel.getMousePoint();
            gl.glVertex2d(p[0], p[1]);
        }

        gl.glEnd();

    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
            int height) {

    }
    
    /**
     * Create menus to set colors
     *
     */
    private static void initMenu(JFrame f, DrawingController c) {
        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Options");
        menuBar.add(menu);
       
        JMenuItem item = new JMenuItem("Foreground");
        menu.add(item);
        JMenuItem item2 = new JMenuItem("Background");
        menu.add(item2);
        item.addActionListener(c);
        item2.addActionListener(c);
        f.setJMenuBar(menuBar);
    }


    public static void main(String[] args) {
        // Initialise OpenGL
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);

        // Create a panel to draw on
        GLJPanel panel = new GLJPanel(caps);

        final JFrame jframe = new JFrame("Drawing");
        jframe.setSize(800, 600);      
        
        // Catch window closing events and quit             
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // add a GL Event listener to handle rendering
        DrawingModel model = new DrawingModel();

        DrawingView view = new DrawingView(model);
        panel.addGLEventListener(view);

        DrawingController controller = new DrawingController(model);
        panel.addMouseListener(controller);
        panel.addMouseMotionListener(controller);
        panel.addMouseMotionListener(Mouse.theMouse);
        initMenu(jframe,controller);
        
        jframe.add(panel);
        jframe.setVisible(true);
        
        // Set the surface scale for the mouse
        //To make it work on macs and possibly windows8 (high resolution screens)
        float scale[] = new float[2];
        panel.getCurrentSurfaceScale(scale);
        System.out.println(scale[0] + ", " + scale[1]);
        Mouse.setSurfaceScale(scale);
        
        FPSAnimator animator = new FPSAnimator(60);
        animator.add(panel);
        animator.start();
    }

	
}