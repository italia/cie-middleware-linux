package it.ipzs.cieid.Firma;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MoveablePicture extends JPanel implements MouseListener, MouseMotionListener{

	private int x,y;
	//private JPanel picturePanel;
	
	public MoveablePicture(String signFilePath) {
		// TODO Auto-generated constructor stub
		this.setSize(90, 25);
		this.setBackground(Color.white);
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		try {
			Image img;
			img = ImageIO.read(new File(signFilePath));
			JLabel imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(this.getWidth()-5, this.getHeight()-10, Image.SCALE_SMOOTH)));
			this.add(imgLabel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int tmpX = (e.getX()+e.getComponent().getX()-x);
		int tmpY = (e.getY()+e.getComponent().getY()-y);
		
		if(tmpX < 0)
		{
			tmpX = 0;
		}
		if(tmpY < 0)
		{
			tmpY = 0;
		}
		
		if(tmpX > this.getParent().getWidth() - this.getWidth())
		{
			tmpX = this.getParent().getWidth() - this.getWidth();
		}
		if(tmpY > this.getParent().getHeight() - this.getHeight())
		{
			tmpY = this.getParent().getHeight() - this.getHeight();
		}
		
		
		e.getComponent().setLocation(tmpX, tmpY);
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		x = e.getX();
		y = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
