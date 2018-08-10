package br.ufal.cg;

import javax.swing.JFrame;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;

public class Main extends JFrame implements GLEventListener{

	final GLProfile profile = GLProfile.get(GLProfile.GL2);
	GLCapabilities capabilities = new GLCapabilities( profile );
	final GLCanvas glcanvas = new GLCanvas( capabilities );
	
	public Main() {
		super("Baseball Field");
	//	setSize(400, 400);
		init();
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	private void init() {
		//glcanvas.addGLEventListener(new);
		glcanvas.setSize(400, 400);
		glcanvas.addGLEventListener(this);
		getContentPane().add(glcanvas);
	}
	
	public static void main(String[] args) {
		new Main().setVisible(true);;
	}


	@Override
	public void init(GLAutoDrawable drawable) {}


	@Override
	public void dispose(GLAutoDrawable drawable) {}


	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		
		gl.glBegin(GL2.GL_LINES);
		gl.glVertex3f(0.50f, -0.50f, 0);
		gl.glVertex3f(-0.50f, 0.50f, 0);
		gl.glEnd();
	}


	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}
}
