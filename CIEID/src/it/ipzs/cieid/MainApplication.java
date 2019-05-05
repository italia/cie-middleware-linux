package it.ipzs.cieid;

import java.awt.EventQueue;

import javax.swing.JFrame;

import it.ipzs.cieid.util.Utils;

public class MainApplication {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					showUI(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void showUI(String[] args)
	{
		MainApplication window = new MainApplication(args);
		window.frame.setVisible(true);
	}
	/**
	 * Create the application.
	 */
	public MainApplication(String[] args) {
		initialize(args);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String[] args) {
		
		if("false".equals(Utils.getProperty("nomore", "false")))
		{
			frame = new IntroFrame();			
		}
		else 
		{
			frame = new MainFrame(args);
		}
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
