package it.ipzs.cieid;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.ugos.util.Runner;

import it.ipzs.cieid.util.Utils;

public class MainFrame extends JFrame {

	public static final int CKR_OK = 0x00000000;
    public static final int CKR_CANCEL = 0x00000001;
    public static final int CKR_TOKEN_NOT_PRESENT = 0x000000E0;
    public static final int CKR_TOKEN_NOT_RECOGNIZED = 0x000000E1;
    public static final int CKR_DEVICE_ERROR = 0x00000030;
    public static final int CKR_GENERAL_ERROR = 0x00000005;
    public static final int CKR_PIN_INCORRECT = 0x000000A0;
    public static final int CKR_PIN_INVALID = 0x000000A1;
    public static final int CKR_PIN_LEN_RANGE = 0x000000A2;

    /* CKR_PIN_EXPIRED and CKR_PIN_LOCKED are new for v2.0 */
    public static final int CKR_PIN_EXPIRED = 0x000000A3;
    public static final int CKR_PIN_LOCKED = 0x000000A4;
    
	private JPanel contentPane;
	private CardLayout cardLayout;
	private JTabbedPane tabbedPane;
	private JButton btnHome;
	private JButton btnCambiaPin;
	private JButton btnSbloccaCarta;
	private JButton btnTutorial;
	private JButton btnAiuto;
	private JButton btnInformazioni;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;
	private JPasswordField passwordField_2;
	private JPasswordField passwordField_3;
	private JPasswordField passwordField_4;
	private JPasswordField passwordField_5;
	private JPasswordField passwordField_6;
	private JPasswordField passwordField_7;

	private JPasswordField passwordFields[] = new JPasswordField[8];
	private JPanel panel_2;
	private JLabel label;
	private JTextPane textPane_1;
	private JLabel label_1;
	private JCheckBox checkBox;
	private JPanel panel_3;
	private JLabel lblCieId;
	private JTextPane txtpnCieAbbinataCon;
	private JLabel label_4;
	private JCheckBox checkBox_1;
	private JButton buttonRemove;
	private JLabel lblNumeroCarta;
	private JLabel labelSerial;
	private JLabel lblIntestatario;
	private JLabel labelCardholder;
	private JButton btnAbbina;
	
	private String serialNumber;
	private String cardHolder;
	private String ef_seriale;
	private JProgressBar progressBar;
	private JLabel lblProgress;
	private JPanel panel_4;
	private JLabel lblCambiaPin;
	private JTextPane txtpnIlPinDella;
	private JLabel label_5;
	private JCheckBox checkBox_2;
	private JLabel lblInserisciIlVecchio;
	private JLabel lblInserisciIlNuovo;
	private JLabel lblRipetiIlNuovo;
	private JPasswordField oldpin;
	private JPasswordField newpin1;
	private JPasswordField newpin2;
	private JButton btnDoCambiaPin;
	private JPanel panel_5;
	private JLabel lblCambiaPin_1;
	private JTextPane txtpnIlPinDella_1;
	private JLabel label_6;
	private JCheckBox checkBox_3;
	private JLabel labelProgressChangePIN;
	private JProgressBar progressBarChangePIN;
	private JPanel panel_6;
	private JLabel lblSbloccoCarta;
	private JTextPane txtpnUtilizzaIlCodice;
	private JLabel label_7;
	private JCheckBox checkBox_4;
	private JButton btnSblocca;
	private JLabel lblInserisciIlPuk;
	private JLabel label_9;
	private JPasswordField pin01;
	private JLabel label_10;
	private JPasswordField pin02;
	private JTextPane textPane_2;
	private JPasswordField puk01;
	private JPanel panel_7;
	private JLabel lblSbloccaCarta;
	private JTextPane txtpnUtilizzaIlCodice_1;
	private JLabel label_8;
	private JCheckBox checkBox_5;
	private JLabel labelProgressUnlock;
	private JProgressBar progressBarUnlock;
	private JPanel panel_8;
	private JLabel lblAiuto;
	private JPanel panel_9;
	private JLabel label_11;
	private MiniWebView miniWebView;
	private JPanel panel_10;
	private JLabel lblInformazioni;
	private MiniWebView miniWebView_1;
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame(args);
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
	public MainFrame(String[] args) {
		setResizable(false);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(SystemColor.control);
		leftPanel.setBounds(0, 0, 200, 600);
		contentPane.add(leftPanel);
		leftPanel.setLayout(null);
		
		JLabel label_2 = new JLabel("");
		try {
			label_2.setIcon(new ImageIcon(Utils.scaleimage(80, 80, ImageIO.read(MainFrame.class.getResource("/it/ipzs/cieid/res/Logo_Cie_ID_Windowed@2x.png")))));
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}				
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(60, 30, 80, 80);
		leftPanel.add(label_2);
		
		btnHome = new JButton("   Home");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton(btnHome);
				selectHome();
			}
		});
		btnHome.setBackground(SystemColor.LIGHT_GRAY);
		btnHome.setHorizontalAlignment(SwingConstants.LEFT);
		btnHome.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 25.png")));
		btnHome.setBounds(0, 130, 200, 45);
		btnHome.setBorderPainted(false);
		leftPanel.add(btnHome);
		
		btnCambiaPin = new JButton("   Cambia PIN");
		btnCambiaPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton(btnCambiaPin);
				oldpin.setText("");
			    newpin1.setText("");
			    newpin2.setText("");
				tabbedPane.setSelectedIndex(3);
			}
		});
		btnCambiaPin.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 24.png")));
		btnCambiaPin.setHorizontalAlignment(SwingConstants.LEFT);
		btnCambiaPin.setBorderPainted(false);
		btnCambiaPin.setBackground(SystemColor.control);
		btnCambiaPin.setBounds(0, 190, 200, 45);
		leftPanel.add(btnCambiaPin);
		
		btnSbloccaCarta = new JButton("   Sblocca Carta");
		btnSbloccaCarta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton(btnSbloccaCarta);
				pin01.setText("");
				pin02.setText("");
				puk01.setText("");
				tabbedPane.setSelectedIndex(5);
			}
		});
		btnSbloccaCarta.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 23.png")));
		btnSbloccaCarta.setHorizontalAlignment(SwingConstants.LEFT);
		btnSbloccaCarta.setBorderPainted(false);
		btnSbloccaCarta.setBackground(SystemColor.control);
		btnSbloccaCarta.setBounds(0, 250, 200, 45);
		leftPanel.add(btnSbloccaCarta);
		
		btnTutorial = new JButton("   Tutorial");
		btnTutorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton(btnTutorial);
				tabbedPane.setSelectedIndex(7);
			}
		});
		btnTutorial.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 22.png")));
		btnTutorial.setHorizontalAlignment(SwingConstants.LEFT);
		btnTutorial.setBorderPainted(false);
		btnTutorial.setBackground(SystemColor.window);
		btnTutorial.setBounds(0, 310, 200, 45);
		leftPanel.add(btnTutorial);
		
		btnAiuto = new JButton("   Aiuto");
		btnAiuto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton(btnAiuto);
				tabbedPane.setSelectedIndex(8);
			}
		});
		btnAiuto.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 21.png")));
		btnAiuto.setHorizontalAlignment(SwingConstants.LEFT);
		btnAiuto.setBorderPainted(false);
		btnAiuto.setBackground(SystemColor.control);
		btnAiuto.setBounds(0, 370, 200, 45);
		leftPanel.add(btnAiuto);
		
		btnInformazioni = new JButton("   Informazioni");
		btnInformazioni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButton(btnInformazioni);
				tabbedPane.setSelectedIndex(9);
			}
		});
		btnInformazioni.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 20.png")));
		btnInformazioni.setHorizontalAlignment(SwingConstants.LEFT);
		btnInformazioni.setBorderPainted(false);
		btnInformazioni.setBackground(SystemColor.control);
		btnInformazioni.setBounds(0, 430, 200, 45);
		leftPanel.add(btnInformazioni);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(200, -50, 600, 630);
		contentPane.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("New tab", null, panel_1, null);
		panel_1.setLayout(null);
		panel_1.setBackground(Color.WHITE);
		
		JLabel lblAbbinaLaTua = new JLabel("Abbina la tua CIE");
		lblAbbinaLaTua.setHorizontalAlignment(SwingConstants.CENTER);
		lblAbbinaLaTua.setFont(new Font("Dialog", Font.BOLD, 30));
		lblAbbinaLaTua.setBounds(147, 36, 299, 36);
		panel_1.add(lblAbbinaLaTua);
		
		JTextPane txtpnDopoAverCollegato = new JTextPane();
		txtpnDopoAverCollegato.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnDopoAverCollegato.setText("Dopo aver collegato e installato il lettore di smart card, posiziona la CIE sul lettore ed inserisci il PIN");
		txtpnDopoAverCollegato.setEditable(false);
		txtpnDopoAverCollegato.setBounds(63, 84, 492, 46);
		panel_1.add(txtpnDopoAverCollegato);
		
		JLabel lblNewLabel1 = new JLabel("");
		lblNewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		
		lblNewLabel1.setBounds(29, 194, 211, 205);
		panel_1.add(lblNewLabel1);
		
		btnAbbina = new JButton("Abbina");
		btnAbbina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				abbinaCIE();
				
			}
		});
		
		btnAbbina.setForeground(Color.WHITE);
		btnAbbina.setBackground(new Color(30, 144, 255));
		btnAbbina.setBounds(206, 507, 114, 25);
		panel_1.add(btnAbbina);
		
		passwordFields[0] = passwordField = new JPasswordField();
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField.setFont(new Font("Dialog", Font.BOLD, 25));
		passwordField.setBounds(258, 321, 25, 25);
		panel_1.add(passwordField);
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_1.requestFocus();
			}
		});
		
		passwordFields[1] = passwordField_1 = new JPasswordField();
		passwordField_1.setBounds(295, 321, 25, 25);
		passwordField_1.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_1.setFont(new Font("Dialog", Font.BOLD, 25));
		panel_1.add(passwordField_1);
		passwordField_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(e.getKeyChar() == '\b')
				{
					passwordField.setText("");
					passwordField.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_2.requestFocus();
			}
		});
		
		passwordFields[2] = passwordField_2 = new JPasswordField();
		passwordField_2.setBounds(332, 321, 25, 25);
		passwordField_2.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_2.setFont(new Font("Dialog", Font.BOLD, 25));
		panel_1.add(passwordField_2);
		passwordField_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\b')
				{
					passwordField_1.setText("");
					passwordField_1.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_3.requestFocus();
			}
		});
		
		passwordFields[3] = passwordField_3 = new JPasswordField();
		passwordField_3.setBounds(370, 321, 25, 25);
		passwordField_3.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_3.setFont(new Font("Dialog", Font.BOLD, 25));
		panel_1.add(passwordField_3);
		passwordField_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\b')
				{
					passwordField_2.setText("");
					passwordField_2.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_4.requestFocus();
			}
		});
		
		passwordFields[4] = passwordField_4 = new JPasswordField();
		passwordField_4.setBounds(407, 321, 25, 25);
		passwordField_4.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_4.setFont(new Font("Dialog", Font.BOLD, 25));		
		panel_1.add(passwordField_4);
		passwordField_4.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\b')
				{
					passwordField_3.setText("");
					passwordField_3.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_5.requestFocus();
			}
		});
		
		passwordFields[5] = passwordField_5 = new JPasswordField();
		passwordField_5.setBounds(444, 321, 25, 25);
		passwordField_5.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_5.setFont(new Font("Dialog", Font.BOLD, 25));		
		panel_1.add(passwordField_5);
		passwordField_5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\b')
				{
					passwordField_4.setText("");
					passwordField_4.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_6.requestFocus();
			}
		});
		
		passwordFields[6] = passwordField_6 = new JPasswordField();
		passwordField_6.setBounds(481, 321, 25, 25);
		passwordField_6.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_6.setFont(new Font("Dialog", Font.BOLD, 25));
		panel_1.add(passwordField_6);
		passwordField_6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\b')
				{
					passwordField_6.setText("");
					passwordField_5.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
					passwordField_7.requestFocus();
			}
		});
		
		passwordFields[7] = passwordField_7 = new JPasswordField();
		passwordField_7.setBounds(518, 321, 25, 25);
		passwordField_7.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_7.setFont(new Font("Dialog", Font.BOLD, 25));
		panel_1.add(passwordField_7);
		passwordField_7.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\n' || e.getKeyChar() == '\r')
				{					
					abbinaCIE();				
				}
				else if(e.getKeyChar() == '\b')
				{
					passwordField_6.setText("");
					passwordField_6.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else if(passwordField_7.getText().length() > 0)
				{
					e.consume();
				}
			}
		});
		
		JLabel lblInserisciIlPin = new JLabel("Inserisci il PIN");
		lblInserisciIlPin.setHorizontalAlignment(SwingConstants.CENTER);
		lblInserisciIlPin.setFont(new Font("Dialog", Font.BOLD, 22));
		lblInserisciIlPin.setBounds(252, 259, 299, 36);
		panel_1.add(lblInserisciIlPin);
		
		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_2, null);
		
		label = new JLabel("Abbina la tua CIE");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Dialog", Font.BOLD, 30));
		label.setBounds(147, 36, 299, 36);
		panel_2.add(label);
		
		textPane_1 = new JTextPane();
		textPane_1.setFont(new Font("Dialog", Font.PLAIN, 16));
		textPane_1.setText("Dopo aver collegato e installato il lettore di smart card, posiziona la CIE sul lettore ed inserisci il PIN");
		textPane_1.setEditable(false);
		textPane_1.setBounds(63, 84, 492, 46);
		panel_2.add(textPane_1);
		
		label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(29, 194, 211, 205);
		panel_2.add(label_1);
		
		checkBox = new JCheckBox("Non mostrare più");
		checkBox.setBackground(Color.WHITE);
		checkBox.setBounds(591, 508, 157, 23);
		panel_2.add(checkBox);
		
		lblProgress = new JLabel("progress");
		lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgress.setFont(new Font("Dialog", Font.BOLD, 22));
		lblProgress.setBounds(252, 259, 299, 36);
		panel_2.add(lblProgress);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(258, 324, 293, 14);
		panel_2.add(progressBar);
		
		panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_3, null);
		
		lblCieId = new JLabel("CIE ID");
		lblCieId.setHorizontalAlignment(SwingConstants.CENTER);
		lblCieId.setFont(new Font("Dialog", Font.BOLD, 30));
		lblCieId.setBounds(147, 36, 299, 36);
		panel_3.add(lblCieId);
		
		txtpnCieAbbinataCon = new JTextPane();
		txtpnCieAbbinataCon.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnCieAbbinataCon.setText("Carta di identità elettronica abbinata correttamente");
		txtpnCieAbbinataCon.setEditable(false);
		txtpnCieAbbinataCon.setBounds(63, 84, 492, 46);
		panel_3.add(txtpnCieAbbinataCon);
		
		label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(29, 194, 211, 205);
		panel_3.add(label_4);
		
		checkBox_1 = new JCheckBox("Non mostrare più");
		checkBox_1.setBackground(Color.WHITE);
		checkBox_1.setBounds(591, 508, 157, 23);
		panel_3.add(checkBox_1);
		
		buttonRemove = new JButton("Rimuovi Carta");
		buttonRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disabilitaCIE();
			}
		});
		buttonRemove.setForeground(Color.WHITE);
		buttonRemove.setBackground(new Color(30, 144, 255));
		buttonRemove.setBounds(206, 507, 150, 25);
		panel_3.add(buttonRemove);
		
		lblNumeroCarta = new JLabel("Numero Carta");
		lblNumeroCarta.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumeroCarta.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNumeroCarta.setBounds(252, 228, 299, 36);
		panel_3.add(lblNumeroCarta);
		
		labelSerial = new JLabel("213123213");
		labelSerial.setHorizontalAlignment(SwingConstants.LEFT);
		labelSerial.setFont(new Font("Dialog", Font.BOLD, 16));
		labelSerial.setBounds(252, 253, 299, 36);
		panel_3.add(labelSerial);
		
		lblIntestatario = new JLabel("Intestatario");
		lblIntestatario.setHorizontalAlignment(SwingConstants.LEFT);
		lblIntestatario.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblIntestatario.setBounds(252, 293, 299, 36);
		panel_3.add(lblIntestatario);
		
		labelCardholder = new JLabel("mario rossi");
		labelCardholder.setHorizontalAlignment(SwingConstants.LEFT);
		labelCardholder.setFont(new Font("Dialog", Font.BOLD, 16));
		labelCardholder.setBounds(252, 318, 299, 36);
		panel_3.add(labelCardholder);
		
		panel_4 = new JPanel();
		panel_4.setLayout(null);
		panel_4.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_4, null);
		
		lblCambiaPin = new JLabel("Cambia PIN");
		lblCambiaPin.setHorizontalAlignment(SwingConstants.CENTER);
		lblCambiaPin.setFont(new Font("Dialog", Font.BOLD, 30));
		lblCambiaPin.setBounds(147, 36, 299, 36);
		panel_4.add(lblCambiaPin);
		
		txtpnIlPinDella = new JTextPane();
		txtpnIlPinDella.setText("IL PIN della tua CIE è un dato sensibile,\ntrattalo con cautela.");
		txtpnIlPinDella.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnIlPinDella.setEditable(false);
		txtpnIlPinDella.setBounds(63, 84, 492, 46);
		panel_4.add(txtpnIlPinDella);
		
		label_5 = new JLabel("");
		label_5.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(29, 194, 211, 205);
		panel_4.add(label_5);
		
		checkBox_2 = new JCheckBox("Non mostrare più");
		checkBox_2.setBackground(Color.WHITE);
		checkBox_2.setBounds(591, 508, 157, 23);
		panel_4.add(checkBox_2);
		
		btnDoCambiaPin = new JButton("Cambia PIN");
		btnDoCambiaPin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cambiaPIN();
			}
		});
		btnDoCambiaPin.setForeground(Color.WHITE);
		btnDoCambiaPin.setBackground(new Color(30, 144, 255));
		btnDoCambiaPin.setBounds(206, 507, 114, 25);
		panel_4.add(btnDoCambiaPin);
		
		lblInserisciIlVecchio = new JLabel("Inserisci il vecchio PIN");
		lblInserisciIlVecchio.setHorizontalAlignment(SwingConstants.LEFT);
		lblInserisciIlVecchio.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblInserisciIlVecchio.setBounds(252, 201, 299, 36);
		panel_4.add(lblInserisciIlVecchio);
		
		oldpin = new JPasswordField();
		oldpin.setBounds(252, 230, 234, 25);
		oldpin.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER)
					cambiaPIN();
			}
		});
		panel_4.add(oldpin);
		
		lblInserisciIlNuovo = new JLabel("Inserisci il nuovo PIN");
		lblInserisciIlNuovo.setHorizontalAlignment(SwingConstants.LEFT);
		lblInserisciIlNuovo.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblInserisciIlNuovo.setBounds(252, 266, 299, 36);
		panel_4.add(lblInserisciIlNuovo);
		
		newpin1 = new JPasswordField();
		newpin1.setBounds(252, 295, 234, 25);
		newpin1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER)
					cambiaPIN();
			}
		});
		panel_4.add(newpin1);
		
		lblRipetiIlNuovo = new JLabel("Ripeti il nuovo PIN");
		lblRipetiIlNuovo.setHorizontalAlignment(SwingConstants.LEFT);
		lblRipetiIlNuovo.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblRipetiIlNuovo.setBounds(252, 332, 299, 36);
		panel_4.add(lblRipetiIlNuovo);
		
		newpin2 = new JPasswordField();
		newpin2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER)
					cambiaPIN();
			}
		});
		newpin2.setBounds(252, 361, 234, 25);
		panel_4.add(newpin2);
		
		JTextPane txtpnIlPinDella_2 = new JTextPane();
		txtpnIlPinDella_2.setFont(new Font("Dialog", Font.PLAIN, 10));
		txtpnIlPinDella_2.setText("Il PIN della CIE deve essere composto da 8 cifre numeriche, non sono ammessi altri tipi di caratteri. Non sono ammessi PIN composti da tutti i numeri uguali (es: 11111111) o da numeri consecutivi (es: 12345678).");
		txtpnIlPinDella_2.setBounds(262, 398, 234, 76);
		txtpnIlPinDella_2.setEditable(false);

		panel_4.add(txtpnIlPinDella_2);
		
		panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_5, null);
		
		lblCambiaPin_1 = new JLabel("Cambia PIN");
		lblCambiaPin_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblCambiaPin_1.setFont(new Font("Dialog", Font.BOLD, 30));
		lblCambiaPin_1.setBounds(147, 36, 299, 36);
		panel_5.add(lblCambiaPin_1);
		
		txtpnIlPinDella_1 = new JTextPane();
		txtpnIlPinDella_1.setText("IL PIN della tua CIE è un dato sensibile,\ntrattalo con cautela.");
		txtpnIlPinDella_1.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnIlPinDella_1.setEditable(false);
		txtpnIlPinDella_1.setBounds(63, 84, 492, 46);
		panel_5.add(txtpnIlPinDella_1);
		
		label_6 = new JLabel("");
		label_6.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setBounds(29, 194, 211, 205);
		panel_5.add(label_6);
		
		checkBox_3 = new JCheckBox("Non mostrare più");
		checkBox_3.setBackground(Color.WHITE);
		checkBox_3.setBounds(591, 508, 157, 23);
		panel_5.add(checkBox_3);
		
		labelProgressChangePIN = new JLabel("progress");
		labelProgressChangePIN.setHorizontalAlignment(SwingConstants.CENTER);
		labelProgressChangePIN.setFont(new Font("Dialog", Font.BOLD, 22));
		labelProgressChangePIN.setBounds(252, 259, 299, 36);
		panel_5.add(labelProgressChangePIN);
		
		progressBarChangePIN = new JProgressBar();
		progressBarChangePIN.setBounds(258, 324, 293, 14);
		panel_5.add(progressBarChangePIN);
		
		panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_6, null);
		
		lblSbloccoCarta = new JLabel("Sblocco Carta");
		lblSbloccoCarta.setHorizontalAlignment(SwingConstants.CENTER);
		lblSbloccoCarta.setFont(new Font("Dialog", Font.BOLD, 30));
		lblSbloccoCarta.setBounds(147, 36, 299, 36);
		panel_6.add(lblSbloccoCarta);
		
		txtpnUtilizzaIlCodice = new JTextPane();
		txtpnUtilizzaIlCodice.setText("Utilizza il codice PUK ricevuto con la CIE");
		txtpnUtilizzaIlCodice.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnUtilizzaIlCodice.setEditable(false);
		txtpnUtilizzaIlCodice.setBounds(63, 84, 492, 46);
		
		
		label_7 = new JLabel("");
		label_7.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setBounds(29, 194, 211, 205);
		panel_6.add(label_7);
		
		checkBox_4 = new JCheckBox("Non mostrare più");
		checkBox_4.setBackground(Color.WHITE);
		checkBox_4.setBounds(591, 508, 157, 23);
		panel_6.add(checkBox_4);
		
		btnSblocca = new JButton("Sblocca");
		btnSblocca.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sbloccaPIN();
			}
		});
		btnSblocca.setForeground(Color.WHITE);
		btnSblocca.setBackground(new Color(30, 144, 255));
		btnSblocca.setBounds(206, 507, 114, 25);
		panel_6.add(btnSblocca);
		
		lblInserisciIlPuk = new JLabel("Inserisci il PUK");
		lblInserisciIlPuk.setHorizontalAlignment(SwingConstants.LEFT);
		lblInserisciIlPuk.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblInserisciIlPuk.setBounds(252, 201, 299, 36);
		panel_6.add(lblInserisciIlPuk);
		
		puk01 = new JPasswordField();
		puk01.setBounds(252, 230, 234, 25);
		puk01.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER)
					sbloccaPIN();
			}
		});
		panel_6.add(puk01);
		
		label_9 = new JLabel("Inserisci il nuovo PIN");
		label_9.setHorizontalAlignment(SwingConstants.LEFT);
		label_9.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_9.setBounds(252, 266, 299, 36);
		panel_6.add(label_9);
		
		pin01 = new JPasswordField();
		pin01.setBounds(252, 295, 234, 25);
		pin01.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER)
					sbloccaPIN();
			}
		});
		panel_6.add(pin01);
		
		label_10 = new JLabel("Ripeti il nuovo PIN");
		label_10.setHorizontalAlignment(SwingConstants.LEFT);
		label_10.setFont(new Font("Dialog", Font.PLAIN, 14));
		label_10.setBounds(252, 332, 299, 36);
		panel_6.add(label_10);
		
		pin02 = new JPasswordField();
		pin02.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER)
					sbloccaPIN();
			}
		});
		pin02.setBounds(252, 361, 234, 25);
		panel_6.add(pin02);
		
		textPane_2 = new JTextPane();
		textPane_2.setText("Il PIN della CIE deve essere composto da 8 cifre numeriche, non sono ammessi altri tipi di caratteri. Non sono ammessi PIN composti da tutti i numeri uguali (es: 11111111) o da numeri consecutivi (es: 12345678).");
		textPane_2.setFont(new Font("Dialog", Font.PLAIN, 10));
		textPane_2.setEditable(false);		
		textPane_2.setBounds(262, 398, 234, 76);
		panel_6.add(textPane_2);
		
		panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_7, null);
		
		lblSbloccaCarta = new JLabel("Sblocca Carta");
		lblSbloccaCarta.setHorizontalAlignment(SwingConstants.CENTER);
		lblSbloccaCarta.setFont(new Font("Dialog", Font.BOLD, 30));
		lblSbloccaCarta.setBounds(147, 36, 299, 36);
		panel_7.add(lblSbloccaCarta);
		
		txtpnUtilizzaIlCodice_1 = new JTextPane();
		txtpnUtilizzaIlCodice_1.setText("Utilizza il codice PUK ricevuto con la CIE");
		txtpnUtilizzaIlCodice_1.setFont(new Font("Dialog", Font.PLAIN, 16));
		txtpnUtilizzaIlCodice_1.setEditable(false);
		txtpnUtilizzaIlCodice_1.setBounds(63, 84, 492, 46);
		panel_7.add(txtpnUtilizzaIlCodice_1);
		
		label_8 = new JLabel("");
		label_8.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setBounds(29, 194, 211, 205);
		panel_7.add(label_8);
		
		checkBox_5 = new JCheckBox("Non mostrare più");
		checkBox_5.setBackground(Color.WHITE);
		checkBox_5.setBounds(591, 508, 157, 23);
		panel_7.add(checkBox_5);
		
		labelProgressUnlock = new JLabel("progress");
		labelProgressUnlock.setHorizontalAlignment(SwingConstants.CENTER);
		labelProgressUnlock.setFont(new Font("Dialog", Font.BOLD, 22));
		labelProgressUnlock.setBounds(252, 259, 299, 36);
		panel_7.add(labelProgressUnlock);
		
		progressBarUnlock = new JProgressBar();
		progressBarUnlock.setBounds(258, 324, 293, 14);
		panel_7.add(progressBarUnlock);
		
		panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_8, null);
		
		lblAiuto = new JLabel("Tutorial");
		lblAiuto.setHorizontalAlignment(SwingConstants.CENTER);
		lblAiuto.setFont(new Font("Dialog", Font.BOLD, 30));
		lblAiuto.setBounds(147, 36, 299, 36);
		panel_8.add(lblAiuto);
		
		MiniWebView webView = new MiniWebView();
		webView.setBounds(12, 99, 571, 362);
		panel_8.add(webView);
		webView.showPage("https://idserver.servizicie.interno.gov.it/idp/tutorial/computer/lettoreusb/linux/tutorial_linux_firefox.jsp");

		panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_9, null);
		
		label_11 = new JLabel("Aiuto");
		label_11.setHorizontalAlignment(SwingConstants.CENTER);
		label_11.setFont(new Font("Dialog", Font.BOLD, 30));
		label_11.setBounds(147, 36, 299, 36);
		panel_9.add(label_11);
		
		miniWebView = new MiniWebView();
		miniWebView.setBounds(12, 99, 571, 362);
		panel_9.add(miniWebView);
		miniWebView.showPage("https://idserver.servizicie.interno.gov.it/idp/aiuto.jsp");
		
		panel_10 = new JPanel();
		panel_10.setLayout(null);
		panel_10.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, panel_10, null);
		
		lblInformazioni = new JLabel("Informazioni");
		lblInformazioni.setHorizontalAlignment(SwingConstants.CENTER);
		lblInformazioni.setFont(new Font("Dialog", Font.BOLD, 30));
		lblInformazioni.setBounds(147, 36, 299, 36);
		panel_10.add(lblInformazioni);
		
		miniWebView_1 = new MiniWebView();
		miniWebView_1.setBounds(12, 99, 571, 362);
		panel_10.add(miniWebView_1);		
		miniWebView_1.showPage("https://idserver.servizicie.interno.gov.it/idp/privacy.jsp");
		
		
		StyledDocument doc = textPane_1.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		doc = txtpnDopoAverCollegato.getStyledDocument();
		center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		doc = txtpnCieAbbinataCon.getStyledDocument();
		center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
							
		doc = txtpnUtilizzaIlCodice.getStyledDocument();
		center = new SimpleAttributeSet();		
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		panel_6.add(txtpnUtilizzaIlCodice);
		
		if(args.length > 0 && args[0].equals("unlock"))
		{
			selectUnlock();
		}
		else 
		{
			selectHome();
		}
	}
	
	private void selectButton(JButton button)
	{
		btnCambiaPin.setBackground(SystemColor.control);
		btnAiuto.setBackground(SystemColor.control);
		btnInformazioni.setBackground(SystemColor.control);
		btnHome.setBackground(SystemColor.control);
		btnSbloccaCarta.setBackground(SystemColor.control);
		btnTutorial.setBackground(SystemColor.control);
		
		button.setBackground(SystemColor.LIGHT_GRAY);
		
	}
	
	private void abbinaCIE()
	{
		 String pin = "";

         int i;
         for (i = 0; i < passwordFields.length; i++)
         {
             JPasswordField field = passwordFields[i];

             pin += field.getText();
         }

         if (pin.length() != 8)
         {
        	 JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
             return;
         }

         char c = pin.charAt(0);

         i = 1;
         for (i = 1; i < pin.length() && (c >= '0' && c <= '9'); i++)
         {
             c = pin.charAt(i);
         }

         if (i < pin.length() || !(c >= '0' && c <= '9'))
         {
        	 JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
             return;
         }

         for (i = 1; i < 9; i++)
         {
        	 for (i = 0; i < passwordFields.length; i++)
             {
                 JPasswordField field = passwordFields[i];
                 field.setText("");                                 
             }
         }

         btnAbbina.setEnabled(false);
         tabbedPane.setSelectedIndex(1);
         
         final String pinfin = pin;
         
         Runner.run(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				try
	            {
	                final int[] attempts = new int[1];

	                Middleware.ProgressCallBack progressCallBack = new Middleware.ProgressCallBack() {
						
						@Override
						public void invoke(final int progress, final String message) {
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										progressBar.setValue(progress);
										lblProgress.setText(message);										
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							
						}
					};
					
					 Middleware.CompletedCallBack completedCallBack = new Middleware.CompletedCallBack() {
							
							@Override
							public void invoke(String pan, String cardholder, String ef_seriale) {
								// TODO Auto-generated method stub
								MainFrame.this.serialNumber = pan;
								MainFrame.this.cardHolder = cardholder;
								MainFrame.this.ef_seriale = ef_seriale;
							}
						};
					
					
					
	                final int ret = Middleware.INSTANCE.AbilitaCIE(null, pinfin, attempts, progressCallBack, completedCallBack);
	                
	                EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								btnAbbina.setEnabled(true);
				                
				                switch (ret)
			                    {
			                        case CKR_TOKEN_NOT_RECOGNIZED:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
			                            selectHome();
			                            //[self showHomeFirstPage];
			                            break;

			                        case CKR_TOKEN_NOT_PRESENT:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
			                            selectHome();
			                            break;

			                        case CKR_PIN_INCORRECT:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato. rimangono %d tentativi", attempts[0]), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
			                            selectHome();
			                            break;

			                        case CKR_PIN_LOCKED:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(),"Munisciti del codice PUK e utilizza la funzione di sblocco carta per abilitarla", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
			                            selectHome();
			                            break;

			                        case CKR_GENERAL_ERROR:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
			                            selectHome();
			                            break;

			                        case CKR_OK:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "L'abilitazione della CIE è avvennuta con successo", "CIE abilitata", JOptionPane.INFORMATION_MESSAGE);                        	
			                            labelSerial.setText(MainFrame.this.ef_seriale);
			                            labelCardholder.setText(cardHolder);
			                            
			                            Utils.setProperty("serialnumber", serialNumber);
			                            Utils.setProperty("cardholder", cardHolder);
			                            Utils.setProperty("ef_seriale", ef_seriale);
			                            
			                            selectCardholder();	                            
			                            break;
			                    }	                
		                            									
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
	            }
	            catch (Exception ex)
	            {
	                ex.printStackTrace();
	            }
			}
		});
	}

	private void cambiaPIN()
	{
		final String pin = oldpin.getText();
        final String pin1 = newpin1.getText();
        final String pin2 = newpin2.getText();

        int i;

        if (pin.length() != 8)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pin1.length() != 8)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }


        char c = pin.charAt(0);

        i = 1;
        for (i = 1; i < pin.length() && (c >= '0' && c <= '9'); i++)
        {
            c = pin.charAt(i);
        }

        if (i < pin.length() || !(c >= '0' && c <= '9'))
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);

        i = 1;
        for (i = 1; i < pin1.length() && (c >= '0' && c <= '9'); i++)
        {
            c = pin1.charAt(i);
        }

        if (i < pin1.length() || !(c >= '0' && c <= '9'))
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pin1.equals(pin2))
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "I PIN non corrispondono", "PIN non corrispondenti", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        char lastchar = c;

        for (i = 1; i < pin1.length() && c == lastchar; i++)
        {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre uguali", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)((int)c - 1);

        for (i = 1; i < pin1.length() && c == lastchar + 1; i++)
        {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar + 1)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)(c + 1);

        for (i = 1; i < pin1.length() && c == lastchar - 1; i++)
        {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar - 1)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        oldpin.setText("");
        newpin1.setText("");
        newpin2.setText("");

        btnDoCambiaPin.setEnabled(false);
        tabbedPane.setSelectedIndex(4);
          
        final int[] attempts = new int[1];

        final Middleware.ProgressCallBack progressCallBack = new Middleware.ProgressCallBack() {
			
			@Override
			public void invoke(final int progress, final String message) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							progressBarChangePIN.setValue(progress);
							labelProgressChangePIN.setText(message);										
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});				
			}
		};
				
		Runner.run(new Runnable() {
			
			@Override
			public void run() {
				
				final int ret = Middleware.INSTANCE.CambioPIN(pin, pin1, attempts, progressCallBack);
		        
		        EventQueue.invokeLater(new Runnable() 
		        {
					public void run() 
					{
						btnDoCambiaPin.setEnabled(true);
		    
			            switch (ret)
			            {
			                case CKR_TOKEN_NOT_RECOGNIZED:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Cambio PIN", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(3);
			                    break;
			
			                case CKR_TOKEN_NOT_PRESENT:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Cambio PIN", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(3);
			                    break;
			
			                case CKR_PIN_INCORRECT:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato. rimangono %d tentativi", attempts[0]), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(3);
			                    break;
			
			                case CKR_PIN_LOCKED:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Munisciti del codice PUK e utilizza la funzione di sblocco carta per abilitarla", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(5);
			                    break;
			
			                case CKR_GENERAL_ERROR:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(3);
			                    break;
			
			                case CKR_OK:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN è stato modificato con successo", "Operazione completata", JOptionPane.INFORMATION_MESSAGE);
			                    selectHome();
			                    PINNoticeDialog pindlg = new PINNoticeDialog();
			                    pindlg.setLocationRelativeTo(MainFrame.this);
			                    pindlg.setVisible(true);
			                    break;
			            }
					}
		        });
				
			}
		});
		
        
	}
	
	private void sbloccaPIN()
	{
		final String puk = puk01.getText();
		final String pin1 = pin01.getText();
		final String pin2 = pin02.getText();

        int i;

        if (puk.length() != 8)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PUK deve essere composto da 8 numeri", "PUK non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pin1.length() != 8)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }


        char c = puk.charAt(0);

        i = 1;
        for (i = 1; i < puk.length() && (c >= '0' && c <= '9'); i++)
        {
            c = puk.charAt(i);
        }

        if (i < puk.length() || !(c >= '0' && c <= '9'))
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PUK deve essere composto da 8 numeri", "PUK non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);

        i = 1;
        for (i = 1; i < pin1.length() && (c >= '0' && c <= '9'); i++)
        {
            c = pin1.charAt(i);
        }

        if (i < pin1.length() || !(c >= '0' && c <= '9'))
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pin1.equals(pin2))
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "I PIN non corrispondono", "PIN non corrispondenti", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        char lastchar = c;

        for (i = 1; i < pin1.length() && c == lastchar; i++)
        {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre uguali", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)((int)c - 1);

        for (i = 1; i < pin1.length() && c == lastchar + 1; i++)
        {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar + 1)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)(c + 1);

        for (i = 1; i < pin1.length() && c == lastchar - 1; i++)
        {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar - 1)
        {
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        oldpin.setText("");
        newpin1.setText("");
        newpin2.setText("");

        btnDoCambiaPin.setEnabled(false);
        tabbedPane.setSelectedIndex(6);
          
        final int[] attempts = new int[1];

        final Middleware.ProgressCallBack progressCallBack = new Middleware.ProgressCallBack() {
			
			@Override
			public void invoke(final int progress, final String message) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							progressBarUnlock.setValue(progress);
							labelProgressUnlock.setText(message);										
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});				
			}
		};
				
		Runner.run(new Runnable() {
			
			@Override
			public void run() {
				
				final int ret = Middleware.INSTANCE.SbloccoPIN(puk, pin1, attempts, progressCallBack);
		        
		        EventQueue.invokeLater(new Runnable() 
		        {
					public void run() 
					{
						btnDoCambiaPin.setEnabled(true);
		    
			            switch (ret)
			            {
			                case CKR_TOKEN_NOT_RECOGNIZED:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Sblocco CIE", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(5);
			                    break;
			
			                case CKR_TOKEN_NOT_PRESENT:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Sblocco CIE", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(5);
			                    break;
			
			                case CKR_PIN_INCORRECT:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PUK digitato è errato. rimangono %d tentativi", attempts[0]), "PUK non corretto", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(5);
			                    break;
			
			                case CKR_PIN_LOCKED:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "PUK bloccato. La tua CIE deve essere sostutuita", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(5);
			                    break;
			
			                case CKR_GENERAL_ERROR:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
			                    tabbedPane.setSelectedIndex(5);
			                    break;
			
			                case CKR_OK:
			                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "La CIE è stata sbloccata con successo", "Operazione completata", JOptionPane.INFORMATION_MESSAGE);
			                    selectHome();
			                    PINNoticeDialog pindlg = new PINNoticeDialog();
			                    pindlg.setLocationRelativeTo(MainFrame.this);
			                    pindlg.setVisible(true);
			                    break;
			            }
					}
		        });
				
			}
		});
		
        
	}
	
	private void disabilitaCIE()
	{
		if(JOptionPane.showConfirmDialog(this.getContentPane(), "Vuoi disabilitare la CIE", "Disabilita CIE", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;
		
		int ret = Middleware.INSTANCE.DisabilitaCIE(Utils.getProperty("serialnumber", ""));
        
        switch (ret)
        {
            case CKR_OK:
                tabbedPane.setSelectedIndex(0);
                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE disabilitata con successo", "CIE disabilitata", JOptionPane.INFORMATION_MESSAGE);
                labelSerial.setText("");                
                labelCardholder.setText("");
                Utils.setProperty("serialnumber", "");
                Utils.setProperty("cardholder", "");  
                Utils.setProperty("ef_seriale", "");
                break;

//            case CKR_TOKEN_NOT_PRESENT:
//            	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Disabilitazione CIE", JOptionPane.ERROR_MESSAGE);
//                break;

            default:
            	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Impossibile disabilitare la CIE", "CIE non disabilitata", JOptionPane.ERROR_MESSAGE);
                break;
        }
	}
	private void selectHome()
	{
		
		if(Utils.getProperty("ef_seriale", "").equals("") && !Utils.getProperty("cardholder", "").equals("") )
		{
			
			labelSerial.setText("<html>Per visualizzarlo<br/>occorre riabilitare la CIE<html>");
			labelCardholder.setText(Utils.getProperty("cardholder", ""));
			
			if(JOptionPane.showConfirmDialog(this.getContentPane(), "E' necessario effettuare un nuovo abbinamento. Procedere?", "Abbinare nuovamente la CIE", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			{
				tabbedPane.setSelectedIndex(2);
			}
			else 
			{
				tabbedPane.setSelectedIndex(0);
				
			}
		}
		else if(!Utils.getProperty("ef_seriale", "").equals(""))
		{
			labelSerial.setText(Utils.getProperty("ef_seriale", ""));
			labelCardholder.setText(Utils.getProperty("cardholder", ""));
			
			tabbedPane.setSelectedIndex(2);
		}
		else
		{			
			tabbedPane.setSelectedIndex(0);
			EventQueue.invokeLater(new Runnable() 
	        {
				public void run() 
				{
					passwordField.requestFocus();
				}
	        });
		}
		
		selectButton(btnHome);
	}
	
	private void selectCardholder()
	{
		tabbedPane.setSelectedIndex(2);
		selectButton(btnHome);
	}
	
	private void selectUnlock()
	{
		tabbedPane.setSelectedIndex(5);
		selectButton(btnSbloccaCarta);
	}
}
