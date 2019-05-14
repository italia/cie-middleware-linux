package it.ipzs.cieid;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import it.ipzs.cieid.util.*;

import java.awt.CardLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

public class IntroFrame extends JFrame {

	private JPanel contentPane;
	private CardLayout cardLayout;
	private JTabbedPane tabbedPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IntroFrame frame = new IntroFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IntroFrame() {
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(cardLayout = new CardLayout(0, 0));
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, -30, 800, 630);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("New tab", null, panel, null);
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		
		JLabel label = new JLabel("Benvenuto in CIE ID");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Dialog", Font.BOLD, 30));
		label.setBounds(229, 34, 332, 36);
		panel.add(label);
		
		JTextPane textPane = new JTextPane();
		textPane.setText("Il software che ti permette di autenticarti ai servizi online della Pubblica Amministrazione per mezzo della nuova carta d'identità elettronica");
		textPane.setEditable(false);
		textPane.setBounds(153, 111, 521, 53);
		panel.add(textPane);
		
		JButton button = new JButton("Continua");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(1);
			}
		});
		button.setForeground(Color.WHITE);
		button.setBackground(new Color(30, 144, 255));
		button.setBounds(334, 499, 114, 25);
		panel.add(button);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			lblNewLabel.setIcon(new ImageIcon(Utils.scaleimage(400, 300, ImageIO.read(IntroFrame.class.getResource("/it/ipzs/cieid/res/flusso_intro_01.png")))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		lblNewLabel.setBounds(111, 152, 563, 335);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		panel_1.setLayout(null);
		panel_1.setBackground(Color.WHITE);
		
		JLabel label_1 = new JLabel("Per iniziare");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Dialog", Font.BOLD, 30));
		label_1.setBounds(264, 36, 190, 36);
		panel_1.add(label_1);
		
		JTextPane textPane_1 = new JTextPane();
		textPane_1.setText("Munisciti di un lettore di smart card contactless, della tua Carta di Identità Elettronica e del PIN");
		textPane_1.setEditable(false);
		textPane_1.setBounds(63, 84, 583, 21);
		panel_1.add(textPane_1);
		
		JLabel lblNewLabel1 = new JLabel("");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		try {
			lblNewLabel1.setIcon(new ImageIcon(Utils.scaleimage(400, 300, ImageIO.read(IntroFrame.class.getResource("/it/ipzs/cieid/res/flusso_intro_02.png")))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		lblNewLabel1.setBounds(111, 152, 563, 335);
		panel_1.add(lblNewLabel1);
		
		final JCheckBox chckbxNoMore = new JCheckBox("Non mostrare più");
		chckbxNoMore.setBackground(Color.WHITE);
		chckbxNoMore.setBounds(591, 508, 157, 23);
		panel_1.add(chckbxNoMore);
		
		JButton btnInizia = new JButton("Inizia");
		btnInizia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxNoMore.isSelected())
					Utils.setProperty("nomore", "true");
				
				// TODO open main frame
				JFrame frame = new MainFrame(new String[] { });			
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				setVisible(false);
				frame.setVisible(true);
			}
		});
		
		btnInizia.setForeground(Color.WHITE);
		btnInizia.setBackground(new Color(30, 144, 255));
		btnInizia.setBounds(337, 507, 114, 25);
		panel_1.add(btnInizia);
		
		
		StyledDocument doc = textPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		doc = textPane_1.getStyledDocument();
		center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
				
	}
}
