package course;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JColorChooser;

public class DrawingController extends MouseAdapter implements ActionListener {
    
    private DrawingModel myModel;
 
    public DrawingController(DrawingModel model) {
        myModel = model;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        double[] p = Mouse.getPosition();
        myModel.setMousePoint(p[0], p[1]);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        double[] p = Mouse.getPosition();
        myModel.setMousePoint(p[0], p[1]);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            double[] p = Mouse.getPosition();
            myModel.addPoint(p[0], p[1]);
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            myModel.addPolygon();
        }        
    }

	@Override
	public void actionPerformed(ActionEvent e) {		
		if(e.getActionCommand().equals("Foreground")){
			Color newColor = JColorChooser.showDialog(null, "Set Foreground", Color.black);		
			myModel.setColor(newColor);
		} else {
			Color newColor = JColorChooser.showDialog(null, "Set Background", Color.black);
			myModel.setBackgroundColor(newColor);
		}
	}    
}