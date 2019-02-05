package course;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

//https://webcms3.cse.unsw.edu.au/COMP3421/16s2/resources/4860
public class Mouse extends MouseAdapter {

    public static final Mouse theMouse = new Mouse();
    private MouseEvent myMouse;

    private double myMouseCoords[];
    private float surfaceScale[];

    private Mouse() {
        myMouse = null;
        myMouseCoords = new double[3];
        surfaceScale = new float[]{1.0f, 1.0f};
    }
    
    public static double[] getPosition() {
        return theMouse.myMouseCoords;
    }
    
    public static void update(GL2 gl) {
        theMouse.computeMousePosition(gl);
    }
    
    public static void setSurfaceScale(float[] scale) {
        theMouse.surfaceScale = scale;
    }
    
    private void computeMousePosition(GL2 gl) {
        int viewport[] = new int[4];
        double mvmatrix[] = new double[16];
        double projmatrix[] = new double[16];

        if (myMouse != null) {
            double x = myMouse.getX()*surfaceScale[0];
            double y = myMouse.getY()*surfaceScale[1];
            
            gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
            gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
            gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);

            GLU glu = new GLU();
            /* note viewport[3] is height of window in pixels */
            y = viewport[3] - y - 1;
            
            glu.gluUnProject(x, y, 0.0, //
                mvmatrix, 0, projmatrix, 0, viewport, 0, myMouseCoords, 0);

        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        myMouse = e;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        myMouse = e;
    }
    
}
