package br.ufal.cg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

//https://github.com/sgothel/jogl-demos
//https://download.java.net/media/jogl/demos/www/
//http://forum.jogamp.org/question-about-the-GLCanvas-and-GLJPanel-td3844025.html
//http://www.cs.umd.edu/~meesh/kmconroy/JOGLTutorial/

public class Hockey extends JFrame implements GLEventListener, ActionListener, MouseListener {

	private class MyLines{
		public float x, y, fy, fx;
		public Color color;
		public MyLines(float x, float y, float fx, float fy, Color color) {
			this.x = x;
			this.y = y;
			this.fy = fy;
			this.fx = fx;
			this.color = (color==null ? Color.white: color);
		}
		
	}
	final GLProfile profile = GLProfile.get(GLProfile.GL2);
	GLCapabilities capabilities = new GLCapabilities(profile);
	final GLCanvas glcanvas = new GLCanvas(capabilities);

	private String[] optString = { "Eq. da Reta", "Algoritmo de Bresenham" };
	JRadioButton[] opts = new JRadioButton[2];
	JButton btnCor = new JButton("Escolha a cor");
	private JLabel lblColor;
	public Color coloR = null ;
	public float myX, myY , fY, fX;
	FPSAnimator animator;
	boolean choose = false;  //false=eq reta, true=bresenh

	ArrayList<MyLines> linhas = new ArrayList<>();

	public Hockey() {
		super("Campo de Hockey");
		// setSize(400, 400);
		init();
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               // Use a dedicate thread to run the stop() to ensure that the
               // animator stops before program exits.
               new Thread() {
                  @Override
                  public void run() {
                     if (animator.isStarted()) animator.stop();
                     System.exit(0);
                  }
               }.start();
            }
         });
	}

	private void init() {
		// glcanvas.addGLEventListener(new);
		JPanel algpanel, colorpanel, latpanel = new JPanel();
		ButtonGroup rdGroup = new ButtonGroup();
		lblColor = new JLabel("Cor escolhida");
		latpanel.setLayout(new BoxLayout(latpanel, BoxLayout.Y_AXIS));
		latpanel.setBorder(BorderFactory.createTitledBorder("Escolha o metodo:"));
		for (int i = 0; i < 2; i++) {
			opts[i] = new JRadioButton(optString[i]);
			opts[i].setActionCommand(optString[i]);
			opts[i].addActionListener(this);
			rdGroup.add(opts[i]);
			latpanel.add(opts[i]);
		}

		opts[0].setSelected(true);
		btnCor.addActionListener(new colorAction());
		latpanel.add(btnCor);
		latpanel.add(lblColor);
		// tamanho da area de desenho
		glcanvas.setSize(600, 600);
		glcanvas.addGLEventListener(this);
		glcanvas.addMouseListener(this);
		getContentPane().add(glcanvas, BorderLayout.CENTER);
		getContentPane().add(latpanel, BorderLayout.EAST);
		
		animator = new FPSAnimator(glcanvas, 60, true);
        //animator.add(glcanvas);
        animator.start();
	}

	public static void main(String[] args) {
		new Hockey().setVisible(true);
		
	}
	
	

	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		//gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); 
		System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
		System.err.println("INIT GL IS: " + gl.getClass().getName());
		System.err.println("GL_VENDOR: " + gl.glGetString(GL2.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL2.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL2.GL_VERSION));
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}
	
	public void drawLines(GL2 gl) {
		gl.glBegin(GL2.GL_LINES);
			// glVertex3f(x, y, z)
	
			// linhas laterais
			gl.glVertex3f(-0.50f, 0.70f, 0); // esquerda
			gl.glVertex3f(-0.50f, -0.70f, 0);
	
			gl.glVertex3f(0.50f, 0.70f, 0); // direita
			gl.glVertex3f(0.50f, -0.70f, 0);
	
			gl.glVertex3f(-0.50f, 0.0f, 0); // linha central
			gl.glVertex3f(0.50f, 0.0f, 0);
	
			gl.glVertex3f(-0.40f, -0.80f, 0); // linha topo
			gl.glVertex3f(0.41f, -0.80f, 0);
			
			gl.glVertex3f(-0.50f, -0.72f, 0); // linha topo segunda
			gl.glVertex3f(0.50f, -0.72f, 0);
			
			gl.glVertex3f(-0.50f, 0.2f, 0); // linha topo terceira
			gl.glVertex3f(0.50f, 0.2f, 0);
	
			gl.glVertex3f(-0.40f, 0.80f, 0); // linha baixo
			gl.glVertex3f(0.41f, 0.80f, 0);
	
			gl.glVertex3f(-0.50f, 0.72f, 0); // linha baixo segunda
			gl.glVertex3f(0.50f, 0.72f, 0);
	
			gl.glVertex3f(-0.50f, -0.2f, 0); // linha baixo terceira
			gl.glVertex3f(0.50f, -0.2f, 0);
			
			//linhas coladas aos circulos
			gl.glVertex3f(0.17f, 0.48f, 0); // superior direito
			gl.glVertex3f(0.43f, 0.48f, 0);
			
			gl.glVertex3f(0.17f, 0.52f, 0); // superior direito
			gl.glVertex3f(0.43f, 0.52f, 0);
			
			gl.glVertex3f(0.17f, -0.48f, 0); // inferior direito
			gl.glVertex3f(0.43f, -0.48f, 0);
			
			gl.glVertex3f(0.17f, -0.52f, 0); // inferior direito
			gl.glVertex3f(0.43f, -0.52f, 0);
			
			gl.glVertex3f(-0.17f, -0.48f, 0); // inferior esquerdo
			gl.glVertex3f(-0.43f, -0.48f, 0);
			
			gl.glVertex3f(-0.17f, -0.52f, 0); // inferior esquerdo
			gl.glVertex3f(-0.43f, -0.52f, 0);
			
			gl.glVertex3f(-0.17f, 0.48f, 0); // superior esquerdo
			gl.glVertex3f(-0.43f, 0.48f, 0);
			
			gl.glVertex3f(-0.17f, 0.52f, 0); // superior esquerdo
			gl.glVertex3f(-0.43f, 0.52f, 0);
			
			for(MyLines l: linhas) {
				gl.glColor3f(l.color.getRed(), l.color.getGreen(), l.color.getBlue());
				gl.glVertex3f(l.x, l.y, 0); 
				gl.glVertex3f(l.fx, l.fy, 0);
				//gl.glColor3f(1.0f, 1.0f, 1.0f);   //branco
			}
		gl.glEnd();
		
		gl.glFlush();
		 /*sinTheta = (float)Math.sin(mytheta);
	      cosTheta = (float)Math.cos(mytheta);
		 gl.glBegin(GL.GL_TRIANGLES);
         gl.glColor3f(1.0f, 0.0f, 0.0f);   // Red
         gl.glVertex2d(-cosTheta, -cosTheta);
         gl.glColor3f(0.0f, 1.0f, 0.0f);   // Green
         gl.glVertex2d(0.0f, cosTheta);
         gl.glColor3f(0.0f, 0.0f, 1.0f);   // Blue
         gl.glVertex2d(sinTheta, -sinTheta);
      gl.glEnd();*/
	}


	
	@Override
	public void display(GLAutoDrawable drawable) {
		// ponto origem é o centro da viewport
		// positivo:: y=move para cima, x=move para direita
		// negativo:: y=para baixo, x=move para esquerda
		final GL2 gl = drawable.getGL().getGL2();

		//gl.glColor3f(0.0f, 0.0f, 0.0f);
		
		drawLines(gl);
		
		gl.glColor3f(1.0f, 1.0f, 1.0f);   //branco
		double theta;
		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 360; ++i) { // circulo central
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.1f * (float) Math.cos(theta), 0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 360; ++i) { // circulo superior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.1f * (float) Math.cos(theta), 0.50f+0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 360; ++i) { // circulo superior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.30f+0.1f * (float) Math.cos(theta), 0.50f+0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 360; ++i) { // circulo inferior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.30f+0.1f * (float) Math.cos(theta), -0.50f+0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 360; ++i) { // circulo inferior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.1f * (float) Math.cos(theta), -0.50f+0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();

		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 90; ++i) { // arco superior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.40f + 0.1f * (float) Math.cos(theta), 0.70f + 0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();

		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 0; i < 90; ++i) {  // arco inferior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.40f + 0.1f * (float) Math.cos(theta), -0.70f - 0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();

		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 90; i < 180; ++i) {  //arco superior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.40f + 0.1f * (float) Math.cos(theta), 0.70f + 0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();

		gl.glBegin(GL2.GL_LINE_STRIP);
			for (int i = 180; i < 270; ++i) {  //arco inferior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.40f + 0.1f * (float) Math.cos(theta), -0.70f + 0.1f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP); 
			for (int i = 0; i < 180; ++i) {  //arco central inferior
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.05f * (float) Math.cos(theta), -0.72f + 0.05f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_LINE_STRIP); 
			for (int i = 180; i < 360; ++i) {  //arco central inferior
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.05f * (float) Math.cos(theta), 0.72f + 0.05f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		//arcos auxiliares
		gl.glColor3f(0.0f, 0.0f, 0.0f);
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo superior direito auxiliar
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.095f * (float) Math.cos(theta), 0.50f+0.095f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo superior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.30f+0.095f * (float) Math.cos(theta), 0.50f+0.095f * (float) Math.sin(theta));
			}
		gl.glEnd();
	
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo inferior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.30f+0.095f * (float) Math.cos(theta), -0.50f+0.095f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo inferior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.095f * (float) Math.cos(theta), -0.50f+0.095f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		//todos os pontinhos do campo
		//tentar criar array de pontos e desenha-lo depois
		gl.glColor3f(1.0f, 1.0f, 1.0f); 
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo central
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.02f * (float) Math.cos(theta), 0.02f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo superior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.01f * (float) Math.cos(theta), 0.50f+0.01f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < 360; ++i) { // circulo superior esquerdo
			theta = i * Math.PI / 180;
			gl.glVertex2f(-0.30f+0.01f * (float) Math.cos(theta), 0.50f+0.01f * (float) Math.sin(theta));
		}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo inferior esquerdo
				theta = i * Math.PI / 180;
				gl.glVertex2f(-0.30f+0.01f * (float) Math.cos(theta), -0.50f+0.01f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // circulo inferior direito
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.01f * (float) Math.cos(theta), -0.50f+0.01f * (float) Math.sin(theta));
			}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
			for (int i = 0; i < 360; ++i) { // mini circulo central direito baixo
				theta = i * Math.PI / 180;
				gl.glVertex2f(0.30f+0.01f * (float) Math.cos(theta), -0.17f+0.01f * (float) Math.sin(theta));
			}
		gl.glEnd();
			
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < 360; ++i) { // mini circulo central direito topo
			theta = i * Math.PI / 180;
			gl.glVertex2f(0.30f+0.01f * (float) Math.cos(theta), 0.17f+0.01f * (float) Math.sin(theta));
		}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < 360; ++i) { // mini circulo central esquerdo topo
			theta = i * Math.PI / 180;
			gl.glVertex2f(-0.30f+0.01f * (float) Math.cos(theta), 0.17f+0.01f * (float) Math.sin(theta));
		}
		gl.glEnd();
		
		gl.glBegin(GL2.GL_POLYGON);
		for (int i = 0; i < 360; ++i) { // mini circulo central esquerdo baixo
			theta = i * Math.PI / 180;
			gl.glVertex2f(-0.30f+0.01f * (float) Math.cos(theta), -0.17f+0.01f * (float) Math.sin(theta));
		}
		gl.glEnd();
		
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0, 0, width, height);
	}

	private void eq_da_reta(GL2 gl, int x1, int y1, int x2, int y2, int color) {
		int x, y;
		float a;

		a = (y2 - y1) / (x2 - x1);
		for (x = x1; x <= x2; x++) {
			// arredonda y
			y = (int) (y1 + a * (x - x1));
			gl.glBegin(GL2.GL_POINT);
				gl.glVertex2f(x, y);
			gl.glEnd();
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	private class colorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Color c = JColorChooser.showDialog(null, "Escolha uma cor", lblColor.getForeground());
			if (c != null) {
				coloR = c;
				lblColor.setForeground(c);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//System.out.println(arg0.getX());
		
		/*mGL.glColor3f(1.0f, 0.5f, 0.31f);
		mGL.glBegin(GL.GL_LINES);
		mGL.glVertex3f(-0.50f, 0.0f, 0); // linha central
		mGL.glVertex3f(0.50f, 0.0f, 0);
		mGL.glEnd();
		
		
		
		mGL.glBegin(GL.GL_LINES);
		mGL.glColor3f(0.5f, 0.5f, 1.0f);
		mGL.glVertex3f(-1.0f, 1.0f, 0.0f);
		mGL.glVertex3f(1.0f, 1.0f, 0.0f);
		mGL.glVertex3f(1.0f, -1.0f, 0.0f);
		mGL.glVertex3f(-1.0f, -1.0f, 0.0f);
		mGL.glEnd();
		

mGL.glFlush();*/
		//JOptionPane.showMessageDialog(null, arg0, "Evento Mouse clicado", 1);
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		myX =  arg0.getX()/300.0f - 1.0f;
		myY =  1.0f - arg0.getY()/300.0f ;
		//glcanvas.repaint();
		System.out.printf("X = %d ; Y = %d\n myX= %f ;  myY = %f\n\n", arg0.getX(), arg0.getY(), myX, myY);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		fX = arg0.getX()/300.0f - 1.0f;
		fY = 1.0f - arg0.getY()/300.0f ;
		System.out.printf("FX = %d ; FY = %d\n fX= %f ;  fY = %f\n\n", arg0.getX(), arg0.getY(),fX, fY);
		//glcanvas.repaint();
		linhas.add(new MyLines(myX, myY, fX, fY, coloR));
		
	}

}
