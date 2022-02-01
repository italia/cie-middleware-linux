package it.ipzs.carousel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import it.ipzs.cieid.MainFrame;

import it.ipzs.cieid.util.*;

import java.awt.AlphaComposite;
import java.awt.Color;

public class CieCard extends JPanel {

	private JLabel lblCardNumber;
	private JLabel lblNumberValue;
	private JLabel lblIntestatario;
	private JLabel lblName;
	private ImageIcon imageIcon1;
	private JLabel lblCieImage;
	private Cie cie;
	private Size s;
	
	public enum Size{
		CarouselSizeRegular,
		CarouselSizeSmall
	}
	

	@Override
    protected void paintComponent(Graphics graphics) {
		if(this.s == Size.CarouselSizeSmall)
		{
	        super.paintComponent(graphics);
	        Graphics2D g2d = (Graphics2D) graphics;
	        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
		}else
		{
			 super.paintComponent(graphics);
		}
    }
	
	public CieCard(Size s)
	{
		this.s = s;
		
		setBackground(Color.WHITE);
		setLayout(null);		
		
		switch(s)
		{
			case CarouselSizeRegular:
				setLayout(null);
				this.setSize(237, 206);

				lblCardNumber = new JLabel("Numero carta:");
				lblCardNumber.setBounds(46, 138, 111, 15);
				lblCardNumber.setLocation(46, 170);
				lblCardNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
				this.add(lblCardNumber);
				
				lblNumberValue = new JLabel();
				lblNumberValue.setBounds(46, 141, 190, 25);
				lblNumberValue.setLocation(85, 200);
				lblNumberValue.setFont(new Font("Tahoma", Font.BOLD, 14));
				this.add(lblNumberValue);
				
				lblIntestatario = new JLabel("Intestatario:");
				lblIntestatario.setBounds(82, 170, 100, 15);
				lblIntestatario.setLocation(46, 177);
				lblIntestatario.setFont(new Font("Tahoma", Font.PLAIN, 12));
				this.add(lblIntestatario);
				
				lblName = new JLabel();
				lblName.setBounds(113, 240, 201, 15);
				lblName.setLocation(46, 192);
				lblName.setFont(new Font("Tahoma", Font.BOLD, 14));
				lblName.setSize(190, 25);
				this.add(lblName);
				
				try {
					imageIcon1 = new ImageIcon(Utils.scaleimage(216, 138, ImageIO.read(MainFrame.class.getResource("/it/ipzs/cieid/res/cie.jpg"))));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}	
				
				lblCieImage = new JLabel();
				lblCieImage.setBounds(26, 11, 220, 120);
				lblCieImage.setIcon(imageIcon1);
				this.add(lblCieImage);
				break;
				
			case CarouselSizeSmall:
			
				setLayout(null);
				this.setSize(150, 158);
						
				lblCardNumber = new JLabel("Numero carta:");
				//lblCardNumber.setSize(139, 15);
				lblCardNumber.setBounds(29, 102, 100, 15);
				//lblCardNumber.setLocation(59, 102);
				lblCardNumber.setFont(new Font("Tahoma", Font.PLAIN, 8));
				lblCardNumber.setEnabled(false);
				this.add(lblCardNumber);
				
				lblNumberValue = new JLabel();
				//lblNumberValue.setBounds(82, 144, 150, 15);
				lblNumberValue.setLocation(29, 113);
				lblNumberValue.setSize(116, 15);
				lblNumberValue.setFont(new Font("Tahoma", Font.PLAIN, 8));
				lblNumberValue.setEnabled(false);
				this.add(lblNumberValue);
				
				lblIntestatario = new JLabel("Intestatario:");
				lblIntestatario.setBounds(82, 170, 100, 15);
				lblIntestatario.setLocation(29, 128);
				lblIntestatario.setFont(new Font("Tahoma", Font.PLAIN, 8));
				lblIntestatario.setEnabled(false);
				this.add(lblIntestatario);
				
				lblName = new JLabel();
				lblName.setBounds(82, 183, 150, 15);
				lblName.setLocation(29, 143);
				lblName.setSize(116, 15);
				lblName.setFont(new Font("Tahoma", Font.PLAIN, 8));
				lblName.setEnabled(false);
				this.add(lblName);
				
				//TODO sostituire l'immagine
				try {
					imageIcon1 = new ImageIcon(Utils.scaleimage(140, 86, ImageIO.read(MainFrame.class.getResource("/it/ipzs/cieid/res/cie.jpg"))));
					
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}		
				lblCieImage = new JLabel();
				//lblCieImage.setBounds(29, 102, 100, 15);
				lblCieImage.setSize(140, 80);
				lblCieImage.setLocation(0, 11);
				lblCieImage.setIcon(imageIcon1);
				//lblCieImage.setEnabled(false);
				this.add(lblCieImage);
				
				
				break;
		}
		
	}
	
	
	
	public JLabel getLblNumberValue() {
		return lblNumberValue;
	}


	public void setLblNumberValue(JLabel lblNumberValue) {
		this.lblNumberValue = lblNumberValue;
	}


	public JLabel getLblName() {
		return lblName;
	}


	public void setLblName(JLabel lblName) {
		this.lblName = lblName;
	}


	public JLabel getLblCieImage() {
		return lblCieImage;
	}


	public void setLblCieImage(JLabel lblCieImage) {
		this.lblCieImage = lblCieImage;
	}


	public JLabel getLblCardNumber() {
		return lblCardNumber;
	}


	public void setLblCardNumber(JLabel lblCardNumber) {
		this.lblCardNumber = lblCardNumber;
	}


	public JLabel getLblIntestatario() {
		return lblIntestatario;
	}


	public void setLblIntestatario(JLabel lblIntestatario) {
		this.lblIntestatario = lblIntestatario;
	}


	public ImageIcon getImageIcon1() {
		return imageIcon1;
	}


	public void setImageIcon1(ImageIcon imageIcon1) {
		this.imageIcon1 = imageIcon1;
	}
	
	public Cie getCard()
	{
		return cie;
	}
	
	public void configureCard(Cie card)
	{
		this.cie = card;
		this.lblNumberValue.setText(card.getSerialNumber());
		this.lblName.setText(card.getName());
	}
	

}
