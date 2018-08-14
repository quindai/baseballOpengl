package br.ufal.cg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

public class Baseball extends JFrame implements GLEventListener, ActionListener {

	final GLProfile profile = GLProfile.get(GLProfile.GL2);
	GLCapabilities capabilities = new GLCapabilities(profile);
	final GLCanvas glcanvas = new GLCanvas(capabilities);

	private String[] optString = { "Algoritmo de Bresenham", "Eq. da Reta" };
	JRadioButton[] opts = new JRadioButton[2];
	JButton btnCor = new JButton("Escolha a cor");
	private JLabel lblColor;

	public Baseball() {
		super("Campo de Baseball");
		// setSize(400, 400);
		init();
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
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
		glcanvas.setSize(400, 400);
		glcanvas.addGLEventListener(this);
		getContentPane().add(glcanvas, BorderLayout.CENTER);
		getContentPane().add(latpanel, BorderLayout.EAST);
	}

	public static void main(String[] args) {
		new Baseball().setVisible(true);
	}

	@Override
	public void init(GLAutoDrawable drawable) {
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();

	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}

	private void eq_da_reta(int x1, int y1, int x2, int y2, int color) {
		int x, y;
		float a;
		int valor;

		a = (y2 - y1) / (x2 - x1);
		for (x = x1; x <= x2; x++) {
			// arredonda y
			y = (int) (y1 + a * (x - x1));
			// TODO write_pixel(x, y, color)
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	private class colorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Color c = JColorChooser.showDialog(null, "Escolha uma cor", lblColor.getForeground());
			if (c != null)
				lblColor.setForeground(c);
		}
	}

}
