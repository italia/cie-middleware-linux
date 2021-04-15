package it.ipzs.cieid;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.ugos.util.Runner;

import it.ipzs.cieid.util.Utils;
import it.ipzs.cieid.Firma.FileDrop;
import it.ipzs.cieid.Firma.PdfPreview;
import it.ipzs.cieid.Firma.VerifyTable;
import it.ipzs.cieid.Middleware.verifyInfo;
import it.ipzs.carousel.*;
import java.awt.FlowLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;

import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Image;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;

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
    public static final int CARD_ALREADY_ENABLED = 0x000000F0;
    public static final int CARD_PAN_MISMATCH = 0x000000F1;
    public static final int INVALID_FILE_TYPE = 0x84000005;

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
	private JPasswordField passwordSignFields[] = new JPasswordField[4];
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
	private JPanel btnPanel;
	private JButton btnRemoveAll;
	private JButton btnRemoveSelected;
	private JButton btnNewButton;
	private JButton btnAnnulla;
	private carousel cieCarousel;
	private Map<String, Cie> cieDictionary;
	private JButton btnFirma;
	
	private String filePath;
	private JPanel selectFile;
	private JLabel lblFirmaElettronica;
	private JPanel panelLoadFile;
	private JLabel lblNewLabel;
	private JTextArea txtrTrascinaITuoi;
	private JTextArea txtrOppure;
	private JButton btnSelezionaUnDocumento;
	private JPanel panel_11;
	private JTextArea lblSFP;
	private JLabel lblPersonalizza;
	private JLabel lblNewLabel_2;
	private JPanel selectOperation;
	private JLabel lblFirmaElettronica_1;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JTextArea lblPathOp;
	private JPanel panel_12;
	private JLabel lblNewLabel_3;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JButton btnAnnullaOp;
	private JPanel panel_13;
	private JPanel panel_14;
	private JPanel selectFirmaOperation;
	private JLabel lblFirmaElettronica_2;
	private JPanel panel_15;
	private JLabel lblNewLabel_7;
	private JTextArea lblPathSignOp;
	private JLabel imgP7m;
	private JLabel lblCadesTitle;
	private JTextArea lblCadesSub;
	private JPanel panel_18;
	private JPanel panel_19;
	private JLabel imgPdf;
	private JLabel lblPadesTitle;
	private JTextArea lblPadesSub;
	private JPanel panel_20;
	private JCheckBox cbGraphicSig;
	private JPanel pdfPreview;
	private JLabel lblFirmaElettronica_3;
	private JPanel panel_21;
	private JLabel lblNewLabel_9;
	private JTextArea lblPathPreview;
	private JLabel lblNewLabel_10;
	private JButton btnAnnullaOp_3;
	private JPanel panelPdfPreview;
	private JPanel panel_23;
	private JPanel panel_24;
	private JPanel panel_25;
	private JButton btnUp;
	private JButton btnDown;
	private JPanel panel_22;
	protected SignOp signOperation;
	private JPanel panel_16;
	private JPanel firmaPin;
	private JLabel lblFirmaElettronica_4;
	private JPanel panel_26;
	private JPanel panel_27;
	private JLabel lblNewLabel_11;
	private JTextArea lblPathPin;
	private JButton btnAnnullaPin;
	private JLabel lblNewLabel_12;
	private JButton btnFirmaPin;
	private JPanel panel_28;
	private JLabel lblNewLabel1_1;
	private JLabel lblProgressFirmaPin;
	private JPasswordField passwordField_8;
	private JPasswordField passwordField_9;
	private JPasswordField passwordField_10;
	private JPasswordField passwordField_11;
	private JLabel lblEsitoFirma;
	private JLabel imgEsitoFirma;
	private JPanel panel_29;
	private JPanel personalizzaFirma;
	private JLabel lblFirmaElettronica_5;
	private JButton btnAnnullaOp_6;
	private JPanel panel_31;
	private JProgressBar progressFirmaPin;
	private JButton btnConcludiFirma; 
	PdfPreview preview;
	private JButton btnSelezionaCIE;
	private JLabel lblFirmaPersonalizzata;
	private JTextArea lblHint;
	private JLabel lblFPOK;
	private JPanel verifica;
	private JLabel lblFirmaElettronica_6;
	private JScrollPane verificaScrollPane;
	private JPanel panel_32;
	private JButton btnConcludiVerifica;
	private JTextArea lblPathVerifica;
	private JButton btnProseguiOp;
	private JButton btnCreaFirma;
	private JPanel Impostazioni;
	private JLabel lblConfigProxy;
	private JPanel panel_33;
	private JButton btnSalva;
	private JLabel lblNewLabel_14;
	private JTextField txtProxyAddr;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JTextField txtPorta;
	private JButton btnImpostazioni;
	private JButton btnModificaProxy;
	private JCheckBox chckbxMostraPassword;
	
	private enum SignOp
	{
		OP_NONE,
		PADES,
		CADES
	}
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
		signOperation = SignOp.OP_NONE;
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
		btnCambiaPin.setBounds(0, 221, 200, 45);
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
		btnSbloccaCarta.setBounds(0, 267, 200, 45);
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
		btnTutorial.setBounds(0, 313, 200, 45);
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
		btnAiuto.setBounds(0, 359, 200, 45);
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
		btnInformazioni.setBounds(0, 405, 200, 45);
		leftPanel.add(btnInformazioni);
		
		btnFirma = new JButton("   Firma Elettronica");
		btnFirma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				selectButton(btnFirma);
				
				imgP7m.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/p7m_grey.png")));
				imgPdf.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/pdd_gray.png")));
				cbGraphicSig.setForeground(Color.gray);
				cbGraphicSig.setSelected(false);
				lblPadesSub.setForeground(Color.gray);
				lblPadesTitle.setForeground(Color.gray);
				lblCadesTitle.setForeground(Color.gray);
				lblCadesSub.setForeground(Color.gray);
				signOperation = SignOp.OP_NONE;
				btnProseguiOp.setEnabled(false);
				
				progressFirmaPin.setVisible(false);
				lblProgressFirmaPin.setText("Inserisci le ultime 4 cifre del pin");
				
		        for (int i = 0; i < passwordSignFields.length; i++)
		        {
		            JPasswordField field = passwordSignFields[i];

		            field.setVisible(true);
		        }

				imgEsitoFirma.setVisible(false);
				lblEsitoFirma.setVisible(false);
				btnConcludiFirma.setVisible(false);
				btnAnnullaPin.setVisible(true);
				btnFirma.setVisible(true);
				
				txtpnCieAbbinataCon.setText("Seleziona la CIE da usare");
				lblCieId.setText("Firma Elettronica");
				
				btnSelezionaCIE.setVisible(true);
				btnRemoveAll.setVisible(false);
				btnRemoveSelected.setVisible(false);
				btnNewButton.setVisible(false);
				
				tabbedPane.setSelectedIndex(2);
			}
		});
		
		btnFirma.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/firma_gray.png")));
		btnFirma.setHorizontalAlignment(SwingConstants.LEFT);
		btnFirma.setBorderPainted(false);
		btnFirma.setBackground(SystemColor.menu);
		btnFirma.setBounds(0, 176, 200, 45);
		leftPanel.add(btnFirma);
		
		btnImpostazioni = new JButton("   Impostazioni");
		btnImpostazioni.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				btnModificaProxy.setEnabled(false);
				
				if(Utils.getProperty("proxyURL", "").equals(""))
				{
					txtProxyAddr.setEnabled(true);
					txtUsername.setEnabled(true);
					txtPassword.setEnabled(true);
					txtPorta.setEnabled(true);
					chckbxMostraPassword.setEnabled(true);
					chckbxMostraPassword.setSelected(false);
					btnSalva.setEnabled(true);
					btnModificaProxy.setEnabled(false);			
					
					selectButton(btnImpostazioni);
					tabbedPane.setSelectedIndex(17);
				}else
				{
					if(Utils.getProperty("credentials", "").equals(""))
					{
						txtPassword.setText("");
						txtUsername.setText("");
					}else
					{
						String encryptedCredentials = Utils.getProperty("credentials", "");
						ProxyInfoManager proxyInfoManager = new ProxyInfoManager();
						String credentials = proxyInfoManager.decrypt(encryptedCredentials);
						System.out.println("Credentials -> " + credentials);
						if(credentials.substring(0, 5).equals("cred="))
						{
							String[] infos = credentials.substring(5).split(":");
							txtUsername.setText(infos[0]);
							txtPassword.setText(infos[1]);
						}
					}
					
					txtProxyAddr.setText(Utils.getProperty("proxyURL", ""));
					txtPorta.setText(Utils.getProperty("proxyPort", ""));
					
					txtProxyAddr.setEnabled(false);
					txtUsername.setEnabled(false);
					txtPassword.setEnabled(false);
					txtPorta.setEnabled(false);
					chckbxMostraPassword.setEnabled(false);
					chckbxMostraPassword.setSelected(false);
					btnSalva.setEnabled(false);
					btnModificaProxy.setEnabled(true);	
					
					selectButton(btnImpostazioni);
					tabbedPane.setSelectedIndex(17);
				}
			}
		});
		btnImpostazioni.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/settings_icon.png")));
		btnImpostazioni.setHorizontalAlignment(SwingConstants.LEFT);
		btnImpostazioni.setBorderPainted(false);
		btnImpostazioni.setBackground(SystemColor.window);
		btnImpostazioni.setBounds(0, 451, 200, 45);
		leftPanel.add(btnImpostazioni);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(200, -65, 600, 635);
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
		btnAbbina.setBounds(258, 524, 114, 25);
		//panel_1.add(btnAbbina);
		
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
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBackground(new Color(255, 255, 255));
		FlowLayout flowLayout = (FlowLayout) buttonsPanel.getLayout();
		flowLayout.setHgap(100);
		buttonsPanel.setBounds(133, 500, 384, 36);
		
	    btnAnnulla = new JButton("Annulla");
		
		btnAnnulla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectHome();
			}
		});
		
		btnAnnulla.setForeground(Color.WHITE);
		btnAnnulla.setBackground(new Color(30, 144, 255));
		buttonsPanel.add(btnAnnulla);
		buttonsPanel.add(btnAbbina);

		panel_1.add(buttonsPanel);
		
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
		
		checkBox = new JCheckBox("Non mostrare piÃ¹");
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
		cieCarousel = new carousel();
		cieCarousel.cieLeft.getLblName().setLocation(15, 140);
		cieCarousel.cieRight.getLblName().setSize(130, 15);
		cieCarousel.cieRight.getLblCardNumber().setLocation(15, 102);
		cieCarousel.cieRight.getLblNumberValue().setLocation(15, 113);
		cieCarousel.cieRight.getLblIntestatario().setLocation(15, 128);
		cieCarousel.cieRight.getLblName().setLocation(15, 140);
		cieCarousel.cieCenter.getLblName().setLocation(25, 190);
		cieCarousel.cieCenter.getLblIntestatario().setLocation(25, 177);
		cieCarousel.cieCenter.getLblNumberValue().setLocation(25, 151);
		cieCarousel.cieCenter.getLblCardNumber().setLocation(25, 138);
		cieCarousel.cieLeft.getLblIntestatario().setLocation(15, 128);
		cieCarousel.cieLeft.getLblNumberValue().setLocation(15, 113);
		cieCarousel.cieLeft.getLblCardNumber().setLocation(15, 102);
		cieCarousel.cieCenter.setLocation(190, 23);
		cieCarousel.setSize(595, 307);
		cieCarousel.setLocation(0, 170);
		panel_3.add(cieCarousel);
		
		btnPanel = new JPanel();
		btnPanel.setBounds(0, 500, 595, 46);
		btnPanel.setBackground(Color.WHITE);
		
		btnRemoveAll = new JButton("Rimuovi tutte");
		btnRemoveAll.setForeground(Color.WHITE);
		btnRemoveAll.setBackground(new Color(30, 144, 255));
		btnRemoveAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
						List<Cie> cieList= new ArrayList<Cie>(cieDictionary.values());
						
						disabilitaAllCIE(cieList);
				}
		});
		btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
		
		btnPanel.add(btnRemoveAll);		
		
		btnRemoveSelected = new JButton("Rimuovi carta selezionata");
		btnRemoveSelected.setForeground(Color.WHITE);
		btnRemoveSelected.setBackground(new Color(30, 144, 255));
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cie cieToDelete = cieCarousel.getCardAtIndex();
					disabilitaCIE(cieToDelete.getPan(), cieToDelete.getName());	

			}
		});
		
		btnPanel.add(btnRemoveSelected);
		
		btnNewButton = new JButton("Aggiungi carta");
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBackground(new Color(30, 144, 255));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
				//abbinaCIE();
			}
		});
		btnPanel.add(btnNewButton);

		panel_3.add(btnPanel);
		
		btnSelezionaCIE = new JButton("Seleziona");
		btnSelezionaCIE.setForeground(Color.WHITE);
		btnSelezionaCIE.setBackground(new Color(30, 144, 255));
		btnSelezionaCIE.setVisible(false);

		btnSelezionaCIE.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CieCard selectedCIE = getSelectedCIE();
				
				if(selectedCIE.getCard().getIsCustomSign())
				{
				    lblPersonalizza.setText("Aggiorna");
				    lblHint.setText("Un tua firma personalizzata è già stata caricata. Vuoi aggiornarla?");
				    lblFPOK.setVisible(true);
				    lblSFP.setVisible(false);
				}else
				{
				    lblPersonalizza.setText("Personalizza");
				    lblHint.setText("Abbiamo creato per te una firma grafica, ma se preferisci puo personalizzarla. "
				    		+ "Questo passaggio non è indispensabile, ma ti consentirà di dare un tocco personale ai documenti firmati.");
				    lblSFP.setVisible(true);
				    lblFPOK.setVisible(false);

				}
				
				tabbedPane.setSelectedIndex(10);
			}
		});
		btnPanel.add(btnSelezionaCIE);
		
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
		txtpnIlPinDella.setBounds(147, 84, 316, 46);
		panel_4.add(txtpnIlPinDella);
		
		label_5 = new JLabel("");
		label_5.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(29, 194, 211, 205);
		panel_4.add(label_5);
		
		checkBox_2 = new JCheckBox("Non mostrare piÃ¹");
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
		txtpnIlPinDella_1.setBounds(147, 84, 327, 46);
		panel_5.add(txtpnIlPinDella_1);
		
		label_6 = new JLabel("");
		label_6.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setBounds(29, 194, 211, 205);
		panel_5.add(label_6);
		
		checkBox_3 = new JCheckBox("Non mostrare piÃ¹");
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
		txtpnUtilizzaIlCodice.setBounds(126, 84, 334, 46);
		
		
		label_7 = new JLabel("");
		label_7.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setBounds(29, 194, 211, 205);
		panel_6.add(label_7);
		
		checkBox_4 = new JCheckBox("Non mostrare piÃ¹");
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
		txtpnUtilizzaIlCodice_1.setBounds(132, 84, 355, 46);
		panel_7.add(txtpnUtilizzaIlCodice_1);
		
		label_8 = new JLabel("");
		label_8.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setBounds(29, 194, 211, 205);
		panel_7.add(label_8);
		
		checkBox_5 = new JCheckBox("Non mostrare piÃ¹");
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
		
		selectFile = new JPanel();
		selectFile.setBackground(SystemColor.text);
		selectFile.setLayout(null);
		tabbedPane.addTab("New tab", null, selectFile, null);
		
		lblFirmaElettronica = new JLabel("Firma Elettronica");
		lblFirmaElettronica.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica.setBounds(165, 45, 302, 39);
		selectFile.add(lblFirmaElettronica);
		
		panel_24 = new JPanel();
		panel_24.setBackground(SystemColor.text);
		panel_24.setBounds(28, 136, 540, 401);
		selectFile.add(panel_24);
		panel_24.setLayout(null);
		
		panelLoadFile = new JPanel();
		panelLoadFile.setBackground(SystemColor.text);
		panelLoadFile.setBounds(12, 12, 513, 300);
		panel_24.add(panelLoadFile);
		panelLoadFile.setLayout(null);
		panelLoadFile.setBorder(BorderFactory.createDashedBorder(null, 5, 5));
		
        new FileDrop( panelLoadFile, new FileDrop.Listener()
	      {   public void  filesDropped( java.io.File[] files )
	          {   
	              // handle file drop
	              filePath = files[0].getAbsolutePath();
	              lblPathOp.setText(filePath);
	              tabbedPane.setSelectedIndex(11);
	          }   // end filesDropped
	      });
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/upload.png")));
		lblNewLabel.setBounds(223, 12, 80, 104);
		panelLoadFile.add(lblNewLabel);
		
		txtrTrascinaITuoi = new JTextArea();
		txtrTrascinaITuoi.setHighlighter(null);
		txtrTrascinaITuoi.setWrapStyleWord(true);
		txtrTrascinaITuoi.setText("Trascina i tuoi documenti qui dentro per firmarli o per verificare una firma elettronica esistente");
		txtrTrascinaITuoi.setRows(3);
		txtrTrascinaITuoi.setLineWrap(true);
		txtrTrascinaITuoi.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtrTrascinaITuoi.setEditable(false);
		txtrTrascinaITuoi.setBackground(SystemColor.text);
		txtrTrascinaITuoi.setBounds(70, 141, 385, 47);
		panelLoadFile.add(txtrTrascinaITuoi);
		
		txtrOppure = new JTextArea();
		txtrOppure.setHighlighter(null);
		txtrOppure.setWrapStyleWord(true);
		txtrOppure.setText("oppure");
		txtrOppure.setRows(3);
		txtrOppure.setLineWrap(true);
		txtrOppure.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtrOppure.setEditable(false);
		txtrOppure.setBackground(SystemColor.text);
		txtrOppure.setBounds(239, 212, 64, 23);
		panelLoadFile.add(txtrOppure);
		
		btnSelezionaUnDocumento = new JButton("Seleziona un documento");
		btnSelezionaUnDocumento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    JFileChooser fileChooser = new JFileChooser();
			    int returnValue = fileChooser.showOpenDialog(null);
			    if (returnValue == JFileChooser.APPROVE_OPTION)
			    {
			        File selectedFile = fileChooser.getSelectedFile();
			        filePath = selectedFile.getAbsolutePath();
			        
			        lblPathOp.setText(filePath);
			        tabbedPane.setSelectedIndex(11);
			    }
			}
		});
		btnSelezionaUnDocumento.setForeground(Color.WHITE);
		btnSelezionaUnDocumento.setBackground(new Color(30, 144, 255));
		btnSelezionaUnDocumento.setBounds(161, 265, 209, 23);
		panelLoadFile.add(btnSelezionaUnDocumento);
		
		panel_11 = new JPanel();
		panel_11.setBackground(SystemColor.text);
		panel_11.setBounds(0, 336, 540, 65);
		panel_24.add(panel_11);
		panel_11.setLayout(null);
		
		lblSFP = new JTextArea();
		lblSFP.setWrapStyleWord(true);
		lblSFP.setText("Abbiamo creato per te una firma grafica, ma se preferisci puo personalizzarla. Questo passaggio non \u00E8 indispensabile, ma ti consentir\u00E0 di dare un tocco personale ai documenti firmati.");
		lblSFP.setRows(3);
		lblSFP.setLineWrap(true);
		lblSFP.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSFP.setEditable(false);
		lblSFP.setBackground(SystemColor.text);
		lblSFP.setBounds(80, 1, 346, 64);
		lblSFP.setHighlighter(null);
		panel_11.add(lblSFP);
		
		lblPersonalizza = new JLabel("Personalizza");
		lblPersonalizza.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Font font = lblPersonalizza.getFont();
				Map attributes = font.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				lblPersonalizza.setFont(font.deriveFont(attributes));
			}
			@Override
			public void mouseClicked(MouseEvent e) {

				
				CieCard selectedCie = getSelectedCIE();
			    String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
			    Image signImage;
				try {
					
					if(!Files.exists(Paths.get(signImagePath)))
					{
					    drawText(selectedCie.getCard().getName(), signImagePath);
					}
					
					signImage = ImageIO.read(new File(signImagePath));
				    ImageIcon imageIcon = new ImageIcon();
				    
				    imageIcon.setImage(signImage.getScaledInstance(lblFirmaPersonalizzata.getWidth(), lblFirmaPersonalizzata.getHeight(), Image.SCALE_SMOOTH));
				    lblFirmaPersonalizzata.setIcon(imageIcon);
					tabbedPane.setSelectedIndex(15);
					
					if(selectedCie.getCard().getIsCustomSign())
					{
						btnCreaFirma.setEnabled(true);
					}else {
						btnCreaFirma.setEnabled(false);
					}
					
				} catch (IOException e1) {
					System.out.println(e1);
				    lblFirmaPersonalizzata.setText("Immagine firma personalizzata non trovata");
					tabbedPane.setSelectedIndex(15);
				}

			}
		@Override
			public void mouseExited(MouseEvent e) {
			Font font = lblPersonalizza.getFont();
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.UNDERLINE, null);
			lblPersonalizza.setFont(font.deriveFont(attributes));
			}
		});
		lblPersonalizza.setForeground(new Color(30, 144, 255));
		lblPersonalizza.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPersonalizza.setBounds(433, 26, 95, 25);

		panel_11.add(lblPersonalizza);
		
		lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/firma@4x.png")));
		lblNewLabel_2.setBounds(0, 1, 79, 64);
		panel_11.add(lblNewLabel_2);
		
		lblFPOK = new JLabel("Firma personalizzata correttamente");
		lblFPOK.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblFPOK.setBounds(80, 1, 346, 64);
		panel_11.add(lblFPOK);
		
		selectOperation = new JPanel();
		selectOperation.setBackground(SystemColor.text);
		selectOperation.setLayout(null);
		tabbedPane.addTab("New tab", null, selectOperation, null);
		
		lblFirmaElettronica_1 = new JLabel("Firma Elettronica");
		lblFirmaElettronica_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica_1.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica_1.setBounds(165, 45, 307, 39);
		selectOperation.add(lblFirmaElettronica_1);
		
		panel_16 = new JPanel();
		panel_16.setBackground(SystemColor.text);
		panel_16.setBounds(76, 132, 449, 415);
		selectOperation.add(panel_16);
		panel_16.setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 449, 82);
		panel_16.add(panel);
		panel.setBackground(SystemColor.text);
		panel.setLayout(null);
		
		lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/generica.png")));
		lblNewLabel_1.setBounds(0, 0, 60, 82);
		panel.add(lblNewLabel_1);
		
		lblPathOp = new JTextArea();
		lblPathOp.setWrapStyleWord(true);
		lblPathOp.setText("pathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFile");
		lblPathOp.setRows(3);
		lblPathOp.setLineWrap(true);
		lblPathOp.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblPathOp.setEditable(false);
		lblPathOp.setBackground(SystemColor.text);
		lblPathOp.setBounds(70, 18, 379, 64);
		panel.add(lblPathOp);
		
		panel_12 = new JPanel();
		panel_12.setBounds(102, 121, 228, 208);
		panel_16.add(panel_12);
		panel_12.setBackground(SystemColor.text);
		panel_12.setLayout(null);
		
		panel_14 = new JPanel();
		panel_14.setBackground(SystemColor.text);
		panel_14.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panel_14.setBorder(BorderFactory.createLineBorder(Color.black));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel_14.setBorder(null);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				lblPathVerifica.setText(filePath);
				Runner.run(new Runnable() {
				
				@Override
				public void run() {
					
		            String proxyAddress = null;
		            String proxyCredentials = null;
		            int proxyPort = -1;
					
					if(!Utils.getProperty("proxyURL", "").equals(""))
					{
						proxyAddress = Utils.getProperty("proxyURL", "");
						proxyPort = Integer.parseInt(Utils.getProperty("proxyPort", ""));
						if(!Utils.getProperty("credentials", "").equals(""))
						{
							String encryptedCredentials = Utils.getProperty("credentials", "");
							ProxyInfoManager proxyInfoManager = new ProxyInfoManager();
							String credentials = proxyInfoManager.decrypt(encryptedCredentials);
							if(credentials.substring(0, 5).equals("cred="))
							{
								proxyCredentials = credentials.substring(5);
							}
						}
					}
					
					System.out.printf("Verifica con CIE - Url: %s, Port: %s, credentials: %s", proxyAddress, proxyPort, proxyCredentials);
					
					final long ret = Middleware.INSTANCE.verificaConCIE(filePath, proxyAddress, proxyPort, proxyCredentials);
					
					if(ret == 0)
					{
						int nSign = Middleware.INSTANCE.getNumberOfSign();
						if(nSign == 0)
						{
	                    	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il file selezionato non contiene firme", "Verifica completata", JOptionPane.INFORMATION_MESSAGE);
	                    	tabbedPane.setSelectedIndex(10);
						}else
						{
							VerifyTable vTable = new VerifyTable(verificaScrollPane);
							verifyInfo vInfo = new verifyInfo();
							verifyInfo[] vInfos = (verifyInfo[])vInfo.toArray(nSign);
							
							for(int i = 0; i<nSign; i++)
							{
								Middleware.INSTANCE.getVerifyInfo(i, vInfos[i]);
								vInfos[i].printVerifyInfo();
								vTable.addDataToModel(verificaScrollPane, vInfos[i]);
							}

							verificaScrollPane.repaint();
							tabbedPane.setSelectedIndex(16);
						}
					}else if(ret == (long)INVALID_FILE_TYPE)
					{
	                	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il file selezionato non è un file valido. E' possibile verificare solo file con estensione .p7m o .pdf", "Errore nella verifica", JOptionPane.ERROR_MESSAGE);
	                	tabbedPane.setSelectedIndex(10);
					}else
					{
	                	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Si è verificato un errore durante la verifica", "Errore nella verifica", JOptionPane.ERROR_MESSAGE);
	                	tabbedPane.setSelectedIndex(10);
					}
				}
			});
			}
		});
		
		panel_14.setBounds(0, 115, 228, 93);
		panel_12.add(panel_14);
		panel_14.setLayout(null);
		
		lblNewLabel_3 = new JLabel("Verifica   >");
		lblNewLabel_3.setBounds(107, 22, 121, 40);
		panel_14.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Dialog", Font.PLAIN, 20));
		
		lblNewLabel_6 = new JLabel("");
		lblNewLabel_6.setBounds(0, 0, 70, 93);
		panel_14.add(lblNewLabel_6);
		lblNewLabel_6.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/Coppia file certificato.png")));
		
		panel_13 = new JPanel();
		panel_13.setBackground(SystemColor.text);
		panel_13.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				panel_13.setBorder(BorderFactory.createLineBorder(Color.black));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel_13.setBorder(null);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				lblPathSignOp.setText(filePath);
				if(getFileExtension(filePath).equals(".pdf"))
				{
					cbGraphicSig.setEnabled(true);
				}else
				{
					cbGraphicSig.setEnabled(false);
				}
				
				tabbedPane.setSelectedIndex(12);
			}
		});
		panel_13.setBounds(0, 0, 228, 93);
		panel_12.add(panel_13);
		panel_13.setLayout(null);
		
		lblNewLabel_4 = new JLabel("Firma      >");
		lblNewLabel_4.setBounds(107, 22, 121, 40);
		panel_13.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Dialog", Font.PLAIN, 20));
		
		lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setBounds(0, 0, 70, 93);
		panel_13.add(lblNewLabel_5);
		lblNewLabel_5.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/Coppia file firma.png")));
		
		btnAnnullaOp = new JButton("Annulla");
		btnAnnullaOp.setBounds(147, 392, 136, 23);
		panel_16.add(btnAnnullaOp);
		btnAnnullaOp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imgP7m.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/p7m_grey.png")));
				imgPdf.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/pdd_gray.png")));
				cbGraphicSig.setForeground(Color.gray);
				cbGraphicSig.setSelected(false);
				lblPadesSub.setForeground(Color.gray);
				lblPadesTitle.setForeground(Color.gray);
				lblCadesTitle.setForeground(Color.gray);
				lblCadesSub.setForeground(Color.gray);
				signOperation = SignOp.OP_NONE;
				btnProseguiOp.setEnabled(false);
				
				tabbedPane.setSelectedIndex(10);
			}
		});
		btnAnnullaOp.setForeground(Color.WHITE);
		btnAnnullaOp.setBackground(new Color(30, 144, 255));
		
		selectFirmaOperation = new JPanel();
		selectFirmaOperation.setBackground(SystemColor.text);
		tabbedPane.addTab("New tab", null, selectFirmaOperation, null);
		selectFirmaOperation.setLayout(null);
		
		lblFirmaElettronica_2 = new JLabel("Firma Elettronica");
		lblFirmaElettronica_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica_2.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica_2.setBounds(165, 45, 291, 39);
		selectFirmaOperation.add(lblFirmaElettronica_2);
		
		panel_23 = new JPanel();
		panel_23.setBackground(SystemColor.text);
		panel_23.setBounds(76, 132, 449, 415);
		selectFirmaOperation.add(panel_23);
		panel_23.setLayout(null);
		
		panel_15 = new JPanel();
		panel_15.setBackground(SystemColor.text);
		panel_15.setBounds(0, 0, 448, 82);
		panel_23.add(panel_15);
		panel_15.setLayout(null);
		
		lblNewLabel_7 = new JLabel("");
		lblNewLabel_7.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/generica.png")));
		lblNewLabel_7.setBounds(0, 0, 60, 82);
		panel_15.add(lblNewLabel_7);
		
		lblPathSignOp = new JTextArea();
		lblPathSignOp.setWrapStyleWord(true);
		lblPathSignOp.setText("pathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFile");
		lblPathSignOp.setRows(3);
		lblPathSignOp.setLineWrap(true);
		lblPathSignOp.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblPathSignOp.setEditable(false);
		lblPathSignOp.setBackground(SystemColor.text);
		lblPathSignOp.setBounds(70, 18, 378, 64);
		panel_15.add(lblPathSignOp);
		
		panel_20 = new JPanel();
		panel_20.setBackground(SystemColor.text);
		panel_20.setBounds(0, 120, 448, 249);
		panel_23.add(panel_20);
		panel_20.setLayout(null);
		
		JLabel lblNewLabel_8_1 = new JLabel("Seleziona il tipo di firma");
		lblNewLabel_8_1.setBounds(129, 11, 202, 39);
		panel_20.add(lblNewLabel_8_1);
		lblNewLabel_8_1.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		panel_18 = new JPanel();
		panel_18.setBackground(SystemColor.text);
		panel_18.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				panel_18.setBorder(BorderFactory.createLineBorder(Color.black));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				panel_18.setBorder(null);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				imgP7m.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/p7m.png")));
				imgPdf.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/pdd_gray.png")));
				cbGraphicSig.setForeground(Color.gray);
				cbGraphicSig.setSelected(false);
				lblPadesSub.setForeground(Color.gray);
				lblPadesTitle.setForeground(Color.gray);
				lblCadesTitle.setForeground(Color.blue);
				lblCadesSub.setForeground(Color.black);
				btnProseguiOp.setEnabled(true);
				signOperation = SignOp.CADES;
			}
		});
		panel_18.setBounds(0, 63, 198, 186);
		panel_20.add(panel_18);
		panel_18.setLayout(null);
		
		imgP7m = new JLabel("New label");
		imgP7m.setBounds(0, 0, 50, 67);
		panel_18.add(imgP7m);
		imgP7m.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/p7m_grey.png")));
		
		lblCadesTitle = new JLabel("Firma CADES");
		lblCadesTitle.addMouseListener(panel_18.getMouseListeners()[0]);
		lblCadesTitle.setForeground(SystemColor.activeCaptionBorder);
		lblCadesTitle.setBounds(60, 11, 126, 31);
		panel_18.add(lblCadesTitle);
		lblCadesTitle.setFont(new Font("Dialog", Font.PLAIN, 17));
		
		lblCadesSub = new JTextArea();
		lblCadesSub.setHighlighter(null);
		lblCadesSub.addMouseListener(panel_18.getMouseListeners()[0]);
		
		lblCadesSub.setForeground(SystemColor.activeCaptionBorder);
		lblCadesSub.setBounds(60, 40, 128, 103);
		panel_18.add(lblCadesSub);
		lblCadesSub.setWrapStyleWord(true);
		lblCadesSub.setText("Si appone su una qualsiasi tipologia di documentoi e prevede la generazione di una busta crittografica. Il documento firmato avr\u00E0 estensione .p7m");
		lblCadesSub.setRows(3);
		lblCadesSub.setLineWrap(true);
		lblCadesSub.setFont(new Font("Dialog", Font.PLAIN, 11));
		lblCadesSub.setEditable(false);
		lblCadesSub.setBackground(SystemColor.text);
		
		panel_19 = new JPanel();
		panel_19.setBackground(SystemColor.text);
		
				panel_19.setBounds(250, 63, 198, 186);
				panel_20.add(panel_19);
				panel_19.setLayout(null);
				
				imgPdf = new JLabel("New label");
				imgPdf.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/pdd_gray.png")));
				imgPdf.setBounds(0, 0, 50, 67);
				panel_19.add(imgPdf);
				
				lblPadesTitle = new JLabel("Firma PADES");
				//lblPadesTitle.addMouseListener(panel_19.getMouseListeners()[0]);
				lblPadesTitle.setForeground(SystemColor.activeCaptionBorder);
				lblPadesTitle.setFont(new Font("Dialog", Font.PLAIN, 17));
				lblPadesTitle.setBounds(60, 11, 126, 31);
				panel_19.add(lblPadesTitle);
				
				lblPadesSub = new JTextArea();
				lblPadesSub.setHighlighter(null);
				lblPadesSub.setForeground(SystemColor.activeCaptionBorder);
				lblPadesSub.setWrapStyleWord(true);
				lblPadesSub.setText("Si appone su documenti PDF nella versione grafica oppure in maniera invisibile. Il documento firmato avr\u00E0 estensione .pdf");
				lblPadesSub.setRows(3);
				lblPadesSub.setLineWrap(true);
				lblPadesSub.setFont(new Font("Dialog", Font.PLAIN, 11));
				lblPadesSub.setEditable(false);
				lblPadesSub.setBackground(SystemColor.text);
				lblPadesSub.setBounds(60, 40, 128, 104);
				lblCadesSub.setHighlighter(null);
				panel_19.add(lblPadesSub);
				
				cbGraphicSig = new JCheckBox("Aggiungi firma grafica");
				cbGraphicSig.setBackground(SystemColor.text);
				cbGraphicSig.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(getFileExtension(filePath).equals(".pdf"))
						{
							panel_19.getMouseListeners()[0].mouseClicked(e);
						}
					}
					@Override
					public void mouseExited(MouseEvent e) {
						panel_19.getMouseListeners()[0].mouseExited(e);
					}
					@Override
					public void mouseEntered(MouseEvent e) {
						panel_19.getMouseListeners()[0].mouseEntered(e);
					}
					
				});
				cbGraphicSig.setForeground(SystemColor.activeCaptionBorder);
				cbGraphicSig.setFont(new Font("Dialog", Font.PLAIN, 11));
				cbGraphicSig.setBounds(6, 156, 162, 23);
				panel_19.add(cbGraphicSig);
				
				JPanel panel_17 = new JPanel();
				panel_17.setBackground(SystemColor.text);
				panel_17.setBounds(48, 392, 359, 23);
				panel_23.add(panel_17);
				panel_17.setLayout(null);
				
				JButton btnAnnullaOp_1 = new JButton("Annulla");
				btnAnnullaOp_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						imgP7m.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/p7m_grey.png")));
						imgPdf.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/pdd_gray.png")));
						cbGraphicSig.setForeground(Color.gray);
						lblPadesSub.setForeground(Color.gray);
						lblPadesTitle.setForeground(Color.gray);
						lblCadesTitle.setForeground(Color.gray);
						lblCadesSub.setForeground(Color.gray);
						cbGraphicSig.setSelected(false);
						btnProseguiOp.setEnabled(false);
						
						tabbedPane.setSelectedIndex(11);
					}
				});
				btnAnnullaOp_1.setBounds(0, 0, 136, 23);
				panel_17.add(btnAnnullaOp_1);
				btnAnnullaOp_1.setForeground(Color.WHITE);
				btnAnnullaOp_1.setBackground(new Color(30, 144, 255));
				
				btnProseguiOp = new JButton("PROSEGUI");
				btnProseguiOp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
				  	   for (int i = 0; i < passwordSignFields.length; i++)
				       {
				           JPasswordField field = passwordSignFields[i];
				           field.setText("");                                 
				       }
				  	   
						if((signOperation == SignOp.PADES) && cbGraphicSig.isSelected())
						{
							
							CieCard selectedCie = getSelectedCIE();
							String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
							lblPathPreview.setText(filePath);
							
							if(!Files.exists(Paths.get(signImagePath)))
							{
							    drawText(selectedCie.getCard().getName(), signImagePath);
							}
							
							preview = new PdfPreview(panelPdfPreview, filePath, signImagePath);
							tabbedPane.setSelectedIndex(13);
						}else
						{
							lblPathPin.setText(filePath);
							tabbedPane.setSelectedIndex(14);
						}
					}
				});
				
				btnProseguiOp.setBounds(223, 0, 136, 23);
				panel_17.add(btnProseguiOp);
				btnProseguiOp.setForeground(Color.WHITE);
				btnProseguiOp.setBackground(new Color(30, 144, 255));
				panel_19.addMouseListener(new MouseAdapter() {
					public void mouseEntered(MouseEvent e) {
						panel_19.setBorder(BorderFactory.createLineBorder(Color.black));
					}
					@Override
					public void mouseExited(MouseEvent e) {
						panel_19.setBorder(null);
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						if(getFileExtension(filePath).equals(".pdf"))
						{
							imgP7m.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/p7m_grey.png")));
							imgPdf.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/pdf.png")));
							cbGraphicSig.setForeground(Color.black);
							lblPadesSub.setForeground(Color.black);
							lblPadesTitle.setForeground(Color.red);
							lblCadesTitle.setForeground(Color.gray);
							lblCadesSub.setForeground(Color.gray);

							btnProseguiOp.setEnabled(true);
							signOperation = SignOp.PADES;
							
							//TODO salvare tipo di operazione
						}

					}
				});
				
				lblPadesSub.addMouseListener(panel_19.getMouseListeners()[0]);
				lblPadesTitle.addMouseListener(panel_19.getMouseListeners()[0]);
		
		JLabel lblNewLabel_8 = new JLabel("Firma documento");
		lblNewLabel_8.setFont(new Font("Dialog", Font.PLAIN, 17));
		lblNewLabel_8.setBounds(237, 81, 151, 39);
		selectFirmaOperation.add(lblNewLabel_8);
		
		pdfPreview = new JPanel();
		pdfPreview.setBackground(SystemColor.text);
		pdfPreview.setLayout(null);
		tabbedPane.addTab("New tab", null, pdfPreview, null);
		
		lblFirmaElettronica_3 = new JLabel("Firma Elettronica");
		lblFirmaElettronica_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica_3.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica_3.setBounds(149, 47, 311, 39);
		pdfPreview.add(lblFirmaElettronica_3);
		
		panel_25 = new JPanel();
		panel_25.setBackground(SystemColor.text);
		panel_25.setBounds(76, 132, 449, 415);
		pdfPreview.add(panel_25);
		panel_25.setLayout(null);
		
		panel_21 = new JPanel();
		panel_21.setBackground(SystemColor.text);
		panel_21.setBounds(0, 0, 449, 82);
		panel_25.add(panel_21);
		panel_21.setLayout(null);
		
		lblNewLabel_9 = new JLabel("");
		lblNewLabel_9.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/generica.png")));
		lblNewLabel_9.setBounds(0, 0, 60, 82);
		panel_21.add(lblNewLabel_9);
		
		lblPathPreview = new JTextArea();
		lblPathPreview.setWrapStyleWord(true);
		lblPathPreview.setText("pathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFile");
		lblPathPreview.setRows(3);
		lblPathPreview.setLineWrap(true);
		lblPathPreview.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblPathPreview.setEditable(false);
		lblPathPreview.setBackground(SystemColor.text);
		lblPathPreview.setBounds(70, 18, 379, 64);
		panel_21.add(lblPathPreview);
		
		btnAnnullaOp_3 = new JButton("PROSEGUI");
		btnAnnullaOp_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
		  	   for (int i = 0; i < passwordSignFields.length; i++)
		       {
		           JPasswordField field = passwordSignFields[i];
		           field.setText("");                                 
		       }
		  	   
				tabbedPane.setSelectedIndex(14);
				lblPathPin.setText(filePath);
			}
		});
		btnAnnullaOp_3.setBounds(147, 392, 136, 23);
		panel_25.add(btnAnnullaOp_3);
		btnAnnullaOp_3.setForeground(Color.WHITE);
		btnAnnullaOp_3.setBackground(new Color(30, 144, 255));
		
		panelPdfPreview = new JPanel();
		panelPdfPreview.setBorder(null);
		panelPdfPreview.setBackground(SystemColor.control);
		panelPdfPreview.setBounds(10, 94, 377, 286);
		panel_25.add(panelPdfPreview);
		GridBagLayout gbl_panelPdfPreview = new GridBagLayout();
		gbl_panelPdfPreview.columnWidths = new int[]{0};
		gbl_panelPdfPreview.rowHeights = new int[]{0};
		gbl_panelPdfPreview.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panelPdfPreview.rowWeights = new double[]{Double.MIN_VALUE};
		panelPdfPreview.setLayout(gbl_panelPdfPreview);
		
		panel_22 = new JPanel();
		panel_22.setBackground(SystemColor.text);
		panel_22.setBounds(397, 153, 42, 172);
		panel_25.add(panel_22);
		panel_22.setLayout(null);


		btnUp = new JButton("");
		btnUp.setBackground(SystemColor.text);
		btnUp.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/up@2x.png")));
		btnUp.setBounds(0, 0, 42, 38);
		btnUp.setOpaque(false);
		btnUp.setBorderPainted(false);
		btnUp.setContentAreaFilled(false);
		btnUp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				preview.prevImage();
			}
		});		
		panel_22.add(btnUp);
		
		btnDown = new JButton("");
		btnDown.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/down@2x.png")));
		btnDown.setBackground(SystemColor.text);
		btnDown.setBounds(0, 134, 42, 38);
		btnDown.setOpaque(false);
		btnDown.setBorderPainted(false);
		btnDown.setContentAreaFilled(false);
		btnDown.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				preview.nextImage();
			}
		});		
		panel_22.add(btnDown);
		
		lblNewLabel_10 = new JLabel("Trascina la firma in un punto desiderato all'interno del documento");
		lblNewLabel_10.setFont(new Font("Dialog", Font.PLAIN, 16));
		lblNewLabel_10.setBounds(35, 81, 515, 39);
		pdfPreview.add(lblNewLabel_10);
		
		firmaPin = new JPanel();
		firmaPin.setLayout(null);
		firmaPin.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, firmaPin, null);
		
		lblFirmaElettronica_4 = new JLabel("Firma Elettronica");
		lblFirmaElettronica_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica_4.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica_4.setBounds(165, 45, 304, 39);
		firmaPin.add(lblFirmaElettronica_4);
		
		panel_26 = new JPanel();
		panel_26.setLayout(null);
		panel_26.setBackground(Color.WHITE);
		panel_26.setBounds(76, 132, 449, 415);
		firmaPin.add(panel_26);
		
		panel_27 = new JPanel();
		panel_27.setLayout(null);
		panel_27.setBackground(Color.WHITE);
		panel_27.setBounds(0, 0, 449, 82);
		panel_26.add(panel_27);
		
		lblNewLabel_11 = new JLabel("");
		lblNewLabel_11.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/generica.png")));
		lblNewLabel_11.setBounds(0, 0, 60, 82);
		panel_27.add(lblNewLabel_11);
		
		lblPathPin = new JTextArea();
		lblPathPin.setWrapStyleWord(true);
		lblPathPin.setText("pathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFile");
		lblPathPin.setRows(3);
		lblPathPin.setLineWrap(true);
		lblPathPin.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblPathPin.setEditable(false);
		lblPathPin.setBackground(Color.WHITE);
		lblPathPin.setBounds(70, 18, 379, 64);
		panel_27.add(lblPathPin);
		
		panel_28 = new JPanel();
		panel_28.setBackground(SystemColor.text);
		panel_28.setBounds(48, 392, 360, 23);
		panel_26.add(panel_28);
		panel_28.setLayout(null);
		
		btnAnnullaPin = new JButton("Annulla");
		btnAnnullaPin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(cbGraphicSig.isSelected())
				{
					tabbedPane.setSelectedIndex(13);
				}else {
					tabbedPane.setSelectedIndex(12);
				}
			}
		});
		btnAnnullaPin.setBounds(-12, 0, 136, 23);
		panel_28.add(btnAnnullaPin);
		btnAnnullaPin.setForeground(Color.WHITE);
		btnAnnullaPin.setBackground(new Color(30, 144, 255));
		
		btnFirmaPin = new JButton("FIRMA");
		btnFirmaPin.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(btnFirmaPin.isEnabled())
				{
					String outfilePath = null;
			        JFrame frame = new JFrame();
			        frame.setAlwaysOnTop(true);
			        
			        JFileChooser fileChooser = new JFileChooser();
			        fileChooser.setAcceptAllFileFilterUsed(false);

			        String fileName = FilenameUtils.getBaseName(filePath);
			        
				    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
				    fileChooser.setDialogTitle("Seleziona il percorso in cui salvare il file");
			        FileNameExtensionFilter filter; 
			        if(signOperation == signOperation.PADES)
			        {
			        	fileName = fileName + "_signed.pdf";
				        filter = new FileNameExtensionFilter("File pdf",".pdf");
			        }else
			        {
			        	fileName = fileName + "_signed.p7m";
			        	filter = new FileNameExtensionFilter("File p7m",".p7m");
			        }

				    fileChooser.setSelectedFile(new File(fileName));
			        fileChooser.addChoosableFileFilter(filter);
			        int returnVal = fileChooser.showSaveDialog(frame);
			        
			        if (returnVal == JFileChooser.APPROVE_OPTION)
			        {
			        	outfilePath = fileChooser.getSelectedFile().getPath();
			        	String fileExtension = filter.getExtensions()[0];
			            if (!outfilePath.endsWith(fileExtension))
			            	outfilePath += fileExtension;
			            
			        	firma(outfilePath);
			        }
				}

			}
		});
		
		btnFirmaPin.setBounds(224, 0, 136, 23);
		panel_28.add(btnFirmaPin);
		btnFirmaPin.setForeground(Color.WHITE);
		btnFirmaPin.setBackground(new Color(30, 144, 255));
		
		btnConcludiFirma = new JButton("Concludi");
		btnConcludiFirma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				progressFirmaPin.setVisible(false);
				lblProgressFirmaPin.setText("Inserisci le ultime 4 cifre del pin");
				lblProgressFirmaPin.setVisible(true);
				
		        for (int i = 0; i < passwordSignFields.length; i++)
		        {
		            JPasswordField field = passwordSignFields[i];

		            field.setVisible(true);
		        }

				imgEsitoFirma.setVisible(false);
				lblEsitoFirma.setVisible(false);
				btnConcludiFirma.setVisible(false);
				btnAnnullaPin.setVisible(true);
				btnFirmaPin.setVisible(true);
				
				
				tabbedPane.setSelectedIndex(10);
			}
		});
		
		btnConcludiFirma.setForeground(Color.WHITE);
		btnConcludiFirma.setBackground(new Color(30, 144, 255));
		btnConcludiFirma.setBounds(108, -1, 136, 23);
		btnConcludiFirma.setVisible(false);
		panel_28.add(btnConcludiFirma);
		
		
		panel_29 = new JPanel();
		panel_29.setBackground(SystemColor.text);
		panel_29.setBounds(0, 153, 449, 141);
		panel_26.add(panel_29);
		panel_29.setLayout(null);
		
		lblNewLabel1_1 = new JLabel("");
		lblNewLabel1_1.setBounds(0, 0, 141, 141);
		panel_29.add(lblNewLabel1_1);
		lblNewLabel1_1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white_small.png")));
		lblNewLabel1_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblProgressFirmaPin = new JLabel("Inserisci le ultime 4 cifre del pin");
		lblProgressFirmaPin.setBounds(153, 18, 239, 23);
		panel_29.add(lblProgressFirmaPin);
		lblProgressFirmaPin.setFont(new Font("Dialog", Font.BOLD, 13));
		
		progressFirmaPin = new JProgressBar();
		progressFirmaPin.setBounds(159, 53, 220, 14);
		panel_29.add(progressFirmaPin);
		
		passwordField_8 = new JPasswordField();
		passwordField_8.setBounds(193, 53, 25, 25);
		panel_29.add(passwordField_8);
		passwordField_8.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_8.setFont(new Font("Dialog", Font.BOLD, 25));
		passwordField_8.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
				{
					passwordField_9.requestFocus();
				}
			}
		});
		
		passwordField_9 = new JPasswordField();
		passwordField_9.setBounds(230, 53, 25, 25);
		panel_29.add(passwordField_9);
		passwordField_9.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_9.setFont(new Font("Dialog", Font.BOLD, 25));
		passwordField_9.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(e.getKeyChar() == '\b')
				{
					passwordField_8.setText("");
					passwordField_8.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else
				{
					passwordField_10.requestFocus();
				}
			}
		});
		
		passwordField_10 = new JPasswordField();
		passwordField_10.setBounds(267, 53, 25, 25);
		panel_29.add(passwordField_10);
		passwordField_10.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_10.setFont(new Font("Dialog", Font.BOLD, 25));
		passwordField_10.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				
				if(e.getKeyChar() == '\b')
				{
					passwordField_9.setText("");
					passwordField_9.requestFocus();
				}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else {
					
					passwordField_11.requestFocus();
				}
			}
		});
		
		passwordField_11 = new JPasswordField();
		passwordField_11.setBounds(305, 53, 25, 25);
		panel_29.add(passwordField_11);
		passwordField_11.setHorizontalAlignment(SwingConstants.CENTER);
		passwordField_11.setFont(new Font("Dialog", Font.BOLD, 25));
		passwordField_11.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar() == '\b')
				{
					passwordField_10.setText("");
					passwordField_10.requestFocus();		}
				else if(e.getKeyChar() < '0' || e.getKeyChar() > '9')
				{
					e.consume();
				}
				else if(passwordField_11.getText().length() > 0)
				{
					e.consume();
				}
			}
		});
		
		passwordSignFields[0] = passwordField_8;
		passwordSignFields[1] = passwordField_9;
		passwordSignFields[2] = passwordField_10;
		passwordSignFields[3] = passwordField_11;
	
		
		lblEsitoFirma = new JLabel("File firmato con successo");
		lblEsitoFirma.setBounds(221, 64, 166, 23);
		panel_29.add(lblEsitoFirma);
		lblEsitoFirma.setFont(new Font("Dialog", Font.PLAIN, 13));
		
		imgEsitoFirma = new JLabel("");
		imgEsitoFirma.setBounds(172, 53, 48, 48);
		panel_29.add(imgEsitoFirma);
		imgEsitoFirma.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/check.png")));
		imgEsitoFirma.setVisible(false);
		lblEsitoFirma.setVisible(false);
		progressFirmaPin.setVisible(false);
		
		lblNewLabel_12 = new JLabel("Appoggia la carta sul lettore");
		lblNewLabel_12.setFont(new Font("Dialog", Font.PLAIN, 17));
		lblNewLabel_12.setBounds(185, 82, 256, 39);
		firmaPin.add(lblNewLabel_12);
		
		personalizzaFirma = new JPanel();
		personalizzaFirma.setLayout(null);
		personalizzaFirma.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, personalizzaFirma, null);
		
		lblFirmaElettronica_5 = new JLabel("Firma Elettronica");
		lblFirmaElettronica_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica_5.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica_5.setBounds(149, 45, 306, 39);
		personalizzaFirma.add(lblFirmaElettronica_5);
		
		JPanel panel_30 = new JPanel();
		panel_30.setBackground(Color.WHITE);
		panel_30.setBounds(76, 132, 449, 415);
		personalizzaFirma.add(panel_30);
		panel_30.setLayout(null);
		
		panel_31 = new JPanel();
		panel_31.setBackground(Color.WHITE);
		panel_31.setBounds(0, 392, 449, 23);
		panel_30.add(panel_31);
		panel_31.setLayout(null);
		JButton btnSelectImg = new JButton("Seleziona un file");
		btnSelectImg.setFont(new Font("Dialog", Font.BOLD, 11));
		btnSelectImg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Png file", "png", "PNG");
				fileChooser.setFileFilter(filter);
				
			    int returnValue = fileChooser.showOpenDialog(null);
			    
			    if (returnValue == JFileChooser.APPROVE_OPTION)
			    {
			        File selectedFile = fileChooser.getSelectedFile();
			        String source = selectedFile.getAbsolutePath();
			        
					CieCard selectedCie = getSelectedCIE();
					String dest = getSignImagePath(selectedCie.getCard().getSerialNumber());
			        
			        try {
			        	
			            FileUtils.copyFile(new File(source), new File(dest));
						Image signImage = ImageIO.read(new File(dest));
					    ImageIcon imageIcon = new ImageIcon();
					    
					    imageIcon.setImage(signImage.getScaledInstance(lblFirmaPersonalizzata.getWidth(), lblFirmaPersonalizzata.getHeight(), Image.SCALE_SMOOTH));
					    lblFirmaPersonalizzata.setIcon(imageIcon);
					    
					    lblPersonalizza.setText("Aggiorna");
					    lblHint.setText("Una tua firma personalizzata è già stata caricata. Vuoi aggiornarla?");
					    lblSFP.setVisible(false);
					    lblFPOK.setVisible(true);
					    
					    selectedCie.getCard().setIsCustomSign(true);
					    
					    cieDictionary.put(selectedCie.getCard().getPan(), selectedCie.getCard());
					    
					    Gson gson = new Gson();
						String serialDictionary = gson.toJson(cieDictionary);
						Utils.setProperty("cieDictionary", serialDictionary);	
						
						btnCreaFirma.setEnabled(true);

			        } catch (IOException er) {
			            er.printStackTrace();
			            lblFirmaPersonalizzata.setText("Errore nel caricamento della firma personalizzata");
			        }


			    }
			    
			}
		});
		btnSelectImg.setBounds(149, 0, 151, 23);
		panel_31.add(btnSelectImg);
		btnSelectImg.setForeground(Color.WHITE);
		btnSelectImg.setBackground(new Color(30, 144, 255));
		
		btnAnnullaOp_6 = new JButton("Indietro");
		btnAnnullaOp_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane.setSelectedIndex(10);
			}
		});
		btnAnnullaOp_6.setBounds(0, 0, 137, 23);
		panel_31.add(btnAnnullaOp_6);
		btnAnnullaOp_6.setForeground(Color.WHITE);
		btnAnnullaOp_6.setBackground(new Color(30, 144, 255));
		
		btnCreaFirma = new JButton("Crea firma");
		btnCreaFirma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				CieCard selectedCie = getSelectedCIE();
				
			    String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
			    drawText(selectedCie.getCard().getName(), signImagePath);
			    Image signImage;
			    
				try {
					
					if(!Files.exists(Paths.get(signImagePath)))
					{
					    drawText(selectedCie.getCard().getName(), signImagePath);
					}
					
					signImage = ImageIO.read(new File(signImagePath));
				    ImageIcon imageIcon = new ImageIcon();
				    
				    imageIcon.setImage(signImage.getScaledInstance(lblFirmaPersonalizzata.getWidth(), lblFirmaPersonalizzata.getHeight(), Image.SCALE_SMOOTH));
				    lblFirmaPersonalizzata.setIcon(imageIcon);
				    
				    selectedCie.getCard().setIsCustomSign(false);
				    
				    cieDictionary.put(selectedCie.getCard().getPan(), selectedCie.getCard());
				    
				    Gson gson = new Gson();
					String serialDictionary = gson.toJson(cieDictionary);
					Utils.setProperty("cieDictionary", serialDictionary);
					
					lblPersonalizza.setText("Personalizza");
					lblHint.setText("Abbiamo creato per te una firma grafica, ma se preferisci puo personalizzarla. "
							+ "Questo passaggio non è indispensabile, ma ti consentirà di dare un tocco personale ai documenti firmati.");
					lblSFP.setVisible(true);
					lblFPOK.setVisible(false);
				    
				} catch (IOException e1) {

				    lblFirmaPersonalizzata.setText("Immagine firma personalizzata non trovata");
					tabbedPane.setSelectedIndex(15);
				}
			    
			    btnCreaFirma.setEnabled(false);
				
			}
		});
		btnCreaFirma.setForeground(Color.WHITE);
		btnCreaFirma.setBackground(new Color(30, 144, 255));
		btnCreaFirma.setBounds(312, -1, 137, 23);
		panel_31.add(btnCreaFirma);
		
		lblHint = new JTextArea();
		lblHint.setHighlighter(null);
		lblHint.setBounds(0, 133, 449, 80);
		panel_30.add(lblHint);
		lblHint.setWrapStyleWord(true);
		lblHint.setText("Abbiamo creato per te una firma grafica, ma se preferisci puoi personalizzarla. Questo passaggio non \u00E8 indispensabile, ma ti consentir\u00E0 di dare un tocco personale ai documenti firmati.");
		lblHint.setRows(3);
		lblHint.setLineWrap(true);
		lblHint.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblHint.setEditable(false);
		lblHint.setBackground(Color.WHITE);
		
		JTextArea txtrAbbiamoCreatoPer_1_1 = new JTextArea();
		txtrAbbiamoCreatoPer_1_1.setHighlighter(null);
		txtrAbbiamoCreatoPer_1_1.setBounds(0, 280, 449, 72);
		panel_30.add(txtrAbbiamoCreatoPer_1_1);
		txtrAbbiamoCreatoPer_1_1.setWrapStyleWord(true);
		txtrAbbiamoCreatoPer_1_1.setText("Puoi caricare un file in formato PNG, se non hai un file contenente una firma grafica puoi realizzarne uno utilizzando l'app CieSign disponibile per smartphone iOS o Android");
		txtrAbbiamoCreatoPer_1_1.setRows(3);
		txtrAbbiamoCreatoPer_1_1.setLineWrap(true);
		txtrAbbiamoCreatoPer_1_1.setFont(new Font("Dialog", Font.PLAIN, 15));
		txtrAbbiamoCreatoPer_1_1.setEditable(false);
		txtrAbbiamoCreatoPer_1_1.setBackground(Color.WHITE);
		
		lblFirmaPersonalizzata = new JLabel("");
		lblFirmaPersonalizzata.setBounds(0, 0, 449, 93);
		panel_30.add(lblFirmaPersonalizzata);
		
		verifica = new JPanel();
		verifica.setLayout(null);
		verifica.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, verifica, null);
		
		lblFirmaElettronica_6 = new JLabel("Firma Elettronica");
		lblFirmaElettronica_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblFirmaElettronica_6.setFont(new Font("Dialog", Font.BOLD, 30));
		lblFirmaElettronica_6.setBounds(149, 45, 306, 39);
		verifica.add(lblFirmaElettronica_6);
		
		panel_32 = new JPanel();
		panel_32.setBackground(Color.WHITE);
		panel_32.setBounds(76, 132, 449, 415);
		verifica.add(panel_32);
		panel_32.setLayout(null);
		
		verificaScrollPane = new JScrollPane();
		verificaScrollPane.setBounds(26, 110, 396, 259);
		verificaScrollPane.setBorder(BorderFactory.createEmptyBorder());
		verificaScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
		panel_32.add(verificaScrollPane);
		
		JPanel panel_27_1 = new JPanel();
		panel_27_1.setBounds(0, 0, 449, 82);
		panel_32.add(panel_27_1);
		panel_27_1.setLayout(null);
		panel_27_1.setBackground(Color.WHITE);
		
		JLabel lblNewLabel_11_1 = new JLabel("");
		lblNewLabel_11_1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/generica.png")));
		lblNewLabel_11_1.setBounds(0, 0, 60, 82);
		panel_27_1.add(lblNewLabel_11_1);
		
		lblPathVerifica = new JTextArea();
		lblPathVerifica.setWrapStyleWord(true);
		lblPathVerifica.setText("pathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFilepathToFile");
		lblPathVerifica.setRows(3);
		lblPathVerifica.setLineWrap(true);
		lblPathVerifica.setFont(new Font("Dialog", Font.PLAIN, 15));
		lblPathVerifica.setEditable(false);
		lblPathVerifica.setBackground(Color.WHITE);
		lblPathVerifica.setBounds(70, 18, 379, 64);
		panel_27_1.add(lblPathVerifica);
		
		btnConcludiVerifica = new JButton("Concludi");
		btnConcludiVerifica.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				tabbedPane.setSelectedIndex(10);
			}
			
		});
		btnConcludiVerifica.setForeground(Color.WHITE);
		btnConcludiVerifica.setBackground(new Color(30, 144, 255));
		btnConcludiVerifica.setBounds(153, 392, 136, 23);
		panel_32.add(btnConcludiVerifica);
		
		JLabel lblNewLabel_13 = new JLabel("Verifica firma elettronica");
		lblNewLabel_13.setFont(new Font("Dialog", Font.BOLD, 17));
		lblNewLabel_13.setBounds(190, 82, 246, 15);
		verifica.add(lblNewLabel_13);
		
		Impostazioni = new JPanel();
		Impostazioni.setLayout(null);
		Impostazioni.setBackground(Color.WHITE);
		tabbedPane.addTab("New tab", null, Impostazioni, null);
		
		lblConfigProxy = new JLabel("Configurazione server Proxy");
		lblConfigProxy.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfigProxy.setFont(new Font("Dialog", Font.BOLD, 28));
		lblConfigProxy.setBounds(65, 45, 471, 39);
		Impostazioni.add(lblConfigProxy);
		
		panel_33 = new JPanel();
		panel_33.setLayout(null);
		panel_33.setBackground(Color.WHITE);
		panel_33.setBounds(76, 132, 449, 415);
		Impostazioni.add(panel_33);
		
		btnSalva = new JButton("Salva");
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if((txtUsername.getText().equals("") && !txtPassword.getText().equals("")) || (!txtUsername.getText().equals("") && txtPassword.getText().equals("")))
				{
					JOptionPane.showMessageDialog(btnSalva.getParent(), "Campo username o password mancante", "Credenziali proxy mancanti", JOptionPane.ERROR_MESSAGE);
		            return;
				}
				
				if((txtPorta.getText().equals("") && !txtProxyAddr.getText().equals("")) || (!txtPorta.getText().equals("") && txtProxyAddr.getText().equals("")) )
				{
					JOptionPane.showMessageDialog(btnSalva.getParent(), "Indirizzo o porta del proxy mancante", "Informazione proxy mancanti", JOptionPane.ERROR_MESSAGE);
		            return;
				}
				
				
				if(txtUsername.getText().equals(""))
				{
                	Utils.setProperty("credentials", "");	
				}else
				{
					String credentials = String.format("cred=%s:%s", txtUsername.getText(), txtPassword.getText());
					ProxyInfoManager proxyInfoManager = new ProxyInfoManager();
					String encryptedCredentials = proxyInfoManager.encrypt(credentials);
				    Utils.setProperty("credentials", encryptedCredentials);
				}
				
				Utils.setProperty("proxyURL", txtProxyAddr.getText());
				
				if(txtPorta.getText() == "")
				{
					Utils.setProperty("proxyPort", String.valueOf(0));
				}else
				{
					Utils.setProperty("proxyPort", txtPorta.getText());
				}
				
				txtProxyAddr.setEnabled(false);
				txtUsername.setEnabled(false);
				txtPassword.setEnabled(false);
				txtPorta.setEnabled(false);
				chckbxMostraPassword.setEnabled(false);
				chckbxMostraPassword.setSelected(false);
				btnSalva.setEnabled(false);
				btnModificaProxy.setEnabled(true);	
			}
			
		});
		
		btnSalva.setForeground(Color.WHITE);
		btnSalva.setBackground(new Color(30, 144, 255));
		btnSalva.setBounds(45, 392, 136, 23);
		panel_33.add(btnSalva);
		
		btnModificaProxy = new JButton("Modifica");
		btnModificaProxy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtProxyAddr.setEnabled(true);
				txtUsername.setEnabled(true);
				txtPassword.setEnabled(true);
				txtPorta.setEnabled(true);
				chckbxMostraPassword.setEnabled(true);
				chckbxMostraPassword.setSelected(false);
				btnSalva.setEnabled(true);
				btnModificaProxy.setEnabled(false);	
			}
		});
		btnModificaProxy.setForeground(Color.WHITE);
		btnModificaProxy.setBackground(new Color(30, 144, 255));
		btnModificaProxy.setBounds(271, 391, 136, 23);
		panel_33.add(btnModificaProxy);
		
		JLabel lblProxyAddr = new JLabel("Indirizzo (URL o indirizzo IP)");
		lblProxyAddr.setHorizontalAlignment(SwingConstants.LEFT);
		lblProxyAddr.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblProxyAddr.setBounds(62, 95, 215, 23);
		panel_33.add(lblProxyAddr);
		
		txtProxyAddr = new JTextField();
		txtProxyAddr.setBounds(62, 124, 234, 25);
		panel_33.add(txtProxyAddr);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblUsername.setBounds(62, 160, 299, 36);
		panel_33.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(62, 189, 234, 25);
		panel_33.add(txtUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
		lblPassword.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPassword.setBounds(62, 226, 299, 36);
		panel_33.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(62, 255, 234, 25);
		panel_33.add(txtPassword);
		
		txtPorta = new JTextField();
		
		txtPorta.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                if (!Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
		

		txtPorta.setBounds(340, 124, 49, 25);
		panel_33.add(txtPorta);
		
		JLabel lblPorta = new JLabel("Porta");
		lblPorta.setHorizontalAlignment(SwingConstants.LEFT);
		lblPorta.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPorta.setBounds(340, 95, 58, 23);
		panel_33.add(lblPorta);
		
		chckbxMostraPassword = new JCheckBox("Mostra password");
		chckbxMostraPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxMostraPassword.isSelected())
				{
					txtPassword.setEchoChar((char)0);
				}else {
					txtPassword.setEchoChar('*');
				}
			}
		});
		chckbxMostraPassword.setFont(new Font("Dialog", Font.BOLD, 10));
		chckbxMostraPassword.setBackground(Color.WHITE);
		chckbxMostraPassword.setBounds(304, 256, 129, 23);
		panel_33.add(chckbxMostraPassword);
		
		lblNewLabel_14 = new JLabel("Inserisci l'indirizzo del server proxy ed eventuali credenziali");
		lblNewLabel_14.setFont(new Font("Dialog", Font.BOLD, 15));
		lblNewLabel_14.setBounds(70, 82, 493, 15);
		Impostazioni.add(lblNewLabel_14);
		
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
		btnFirma.setBackground(SystemColor.control);
		btnImpostazioni.setBackground(SystemColor.control);
		
		button.setBackground(SystemColor.LIGHT_GRAY);
		
	}
	
	private CieCard getSelectedCIE()
	{
		return cieCarousel.cieCenter;
	}
	
	private String getSignImagePath(String serialNumber)
	{
		String home = System.getProperty("user.home");
		String signPath = home + "/.CIEPKI/" + serialNumber+"_default.png";
		System.out.println("Image path " + signPath);
		return signPath;
	}

	private String getFileExtension(String name) {
	    int lastIndexOf = name.lastIndexOf(".");
	    if (lastIndexOf == -1) {
	        return ""; // empty extension
	    }
	    return name.substring(lastIndexOf);
	}
	
	
	private void drawText(String text, String path)
	{
	    BufferedImage bufferedImage = new BufferedImage(1, 1,
	        BufferedImage.TYPE_INT_RGB);
	    
	    
	    Graphics graphics = bufferedImage.getGraphics();

	    
		try {

		    text = toFirstCharUpperAll(toTitleCase(text).toLowerCase());
		    
		    /*
		    System.out.println(MainFrame.class.getResource("/it/ipzs/cieid/res/Allura-Regular.ttf").toExternalForm());
			File file = new File(MainFrame.class.getResource("/it/ipzs/cieid/res/Allura-Regular.ttf").toExternalForm());
		    //File file = new File("/usr/share/CIEID/cieid.jar/it/ipzs/cieid/res/Allura-Regular.ttf");
			InputStream is = new FileInputStream(file);
		     */

		    
		    File file = null;
		    String resource = "/it/ipzs/cieid/res/Allura-Regular.ttf";
		    URL res = getClass().getResource(resource);
		    if (res.getProtocol().equals("jar")) {
		        try {
		            InputStream input = getClass().getResourceAsStream(resource);
		            file = File.createTempFile("tempfile", ".tmp");
		            OutputStream out = new FileOutputStream(file);
		            int read;
		            byte[] bytes = new byte[1024];

		            while ((read = input.read(bytes)) != -1) {
		                out.write(bytes, 0, read);
		            }
		            out.close();
		            file.deleteOnExit();
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
		    } else {
		        //this will probably work in your IDE, but not from a JAR
		        file = new File(res.getFile());
		    }

		    if (file != null && !file.exists()) {
		        throw new RuntimeException("Error: File " + file + " not found!");
		    }
		    
		    
		    InputStream is = new FileInputStream(file);
		    Font customFont = Font.createFont(Font.TRUETYPE_FONT, is); 
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    //register the font
		    ge.registerFont(customFont);
		    
		    graphics.setFont(customFont.deriveFont(Font.LAYOUT_LEFT_TO_RIGHT, 150f));
		    FontMetrics fM = graphics.getFontMetrics();
		    bufferedImage = new BufferedImage(fM.stringWidth(text), fM.getHeight(),
			        BufferedImage.TYPE_INT_RGB);
			    
			graphics = bufferedImage.getGraphics();
			graphics.setFont(customFont.deriveFont(Font.LAYOUT_LEFT_TO_RIGHT, 150f));
		    graphics.setColor(Color.white);
		    graphics.fillRect(0, 0, fM.stringWidth(text), fM.getHeight());
		    graphics.setColor(Color.BLACK);
		    graphics.drawString(text, 0, fM.getAscent());
			
		    
		    ImageIO.write(bufferedImage, "png", new File(path));
		} catch (FontFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	private void firma(String outFilePath)
	{
		
		String pin = "";

        int i;
        for (i = 0; i < passwordSignFields.length; i++)
        {
            JPasswordField field = passwordSignFields[i];

            pin += field.getText();
        }

        if (pin.length() != 4)
        {
       	 JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto dalle ultime 4 cifre", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
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
       	 JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto dalle ultime 4 cifre", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        btnAnnullaPin.setEnabled(false);
        btnFirmaPin.setEnabled(false);
        
        for (i = 0; i < passwordSignFields.length; i++)
        {
            JPasswordField field = passwordSignFields[i];

            field.setVisible(false);
        }
        
        progressFirmaPin.setVisible(true);
        lblProgressFirmaPin.setText("Firma in corso...");

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
										progressFirmaPin.setValue(progress);									
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							
						}
					};
							
					Middleware.SignCompletedCallBack signCompletedCallBack = new Middleware.SignCompletedCallBack() {
						@Override
						public void invoke(int retValue) {
							// TODO Auto-generated method stub
		
							System.out.println("Sign Completed!!");
							if(retValue == 0)
							{
								lblEsitoFirma.setText("File firmato con successo");
								imgEsitoFirma.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/check.png")));
								
							}else
							{
								lblEsitoFirma.setText("Si è verificato un errore durante la firma");
								imgEsitoFirma.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/cross.png")));
							}
			
							lblEsitoFirma.setVisible(true);
							imgEsitoFirma.setVisible(true);
							progressFirmaPin.setVisible(false);
							btnConcludiFirma.setVisible(true);
							btnAnnullaPin.setVisible(false);
							btnFirmaPin.setVisible(false);
							lblProgressFirmaPin.setVisible(false);
						}
					};
			
			
					CieCard selectedCie = getSelectedCIE();
					final int ret;
					if(signOperation == SignOp.PADES)
					{
						if(cbGraphicSig.isSelected())
						{
							float infos[] = preview.signImageInfos();
							String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
							int pageNumber = preview.getSelectedPage();
							String pan = selectedCie.getCard().getPan();
							ret = Middleware.INSTANCE.firmaConCIE(filePath, "pdf", pinfin, pan, pageNumber, infos[0], infos[1], infos[2], infos[3], 
									signImagePath, outFilePath, progressCallBack, signCompletedCallBack);
						}else 
						{
							ret = Middleware.INSTANCE.firmaConCIE(filePath, "pdf", pinfin, selectedCie.getCard().getPan(), 0, 0, 0, 0, 0, null, outFilePath, progressCallBack, signCompletedCallBack);
						}
					}else
					{
						ret = Middleware.INSTANCE.firmaConCIE(filePath, "p7m", pinfin, selectedCie.getCard().getPan(), 0, 0, 0, 0, 0, null, outFilePath, progressCallBack, signCompletedCallBack);
					}
        
			        EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								
						        btnAnnullaPin.setEnabled(true);
						        btnFirmaPin.setEnabled(true);
						        
				                switch (ret)
			                    {
			                        case CKR_TOKEN_NOT_RECOGNIZED:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
			                        	showFirmaPin();
			                            break;
			
			                        case CKR_TOKEN_NOT_PRESENT:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
			                        	showFirmaPin();
			                            break;
			
			                        case CKR_PIN_INCORRECT:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato. rimangono %d tentativi", attempts[0]), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
			                        	showFirmaPin();
			                            break;
			
			                        case CKR_PIN_LOCKED:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(),"Munisciti del codice PUK e utilizza la funzione di sblocco carta per abilitarla", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
			                        	showFirmaPin();
			                            break;
			
			                        case CKR_GENERAL_ERROR:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
			                        	showFirmaPin();
			                            break;
			
			                        case CARD_PAN_MISMATCH:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE selezionata diversa da quella presente sul lettore", "CIE non corrispondente", JOptionPane.ERROR_MESSAGE);                        	
			                        	showFirmaPin();
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
	
	private void showFirmaPin()
	{
		progressFirmaPin.setVisible(false);
		lblProgressFirmaPin.setText("Inserisci le ultime 4 cifre del pin");
		
        for (int i = 0; i < passwordSignFields.length; i++)
        {
            JPasswordField field = passwordSignFields[i];

            field.setVisible(true);
        }
        
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
								/*
								MainFrame.this.serialNumber = pan;
								MainFrame.this.cardHolder = cardholder;
								MainFrame.this.ef_seriale = ef_seriale;
								*/

							    String signPath = getSignImagePath(ef_seriale);
							    drawText(cardholder, signPath);
								
								System.out.println("Pan: "+ pan + "ef seriale " +  ef_seriale);
								Cie newCie = new Cie(pan, cardholder, ef_seriale);
								cieDictionary.put(pan, newCie);
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
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato."), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
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
			              
			                        	Gson gson = new Gson();
			                        	String serialDictionary = gson.toJson(cieDictionary);
			                        	Utils.setProperty("cieDictionary", serialDictionary);			                            
			                            
			                        	cieCarousel.configureCards(cieDictionary);
			                        	
			                            selectCardholder();	                            
			                            break;
			                        case CARD_ALREADY_ENABLED:
			                        	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Carta già abilitata", "Impossibile abbinare la carta", JOptionPane.ERROR_MESSAGE);
			                            selectHome();
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
	
	private void disabilitaCIE(String pan, String name)
	{
		
		if(JOptionPane.showConfirmDialog(this.getContentPane(), "Stai rimuovendo la Carta di Identità di " + name + "\n dal sistema, per utilizzarla nuovamente "
				+ " dovrai ripetere l'abbinamento" , "Vuoi rimuovere la carta?", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.YES_OPTION)
			return;
		
		int ret = Middleware.INSTANCE.DisabilitaCIE(pan);
        
        switch (ret)
        {
            case CKR_OK:
                
                
                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE disabilitata con successo", "CIE disabilitata", JOptionPane.INFORMATION_MESSAGE);
                
            	Cie cie = cieDictionary.get(pan);
            	
            	String signPath = getSignImagePath(cie.getSerialNumber());
            	try {
            			Files.deleteIfExists(Paths.get(signPath));
            		} catch (NoSuchFileException x) {
            		    System.err.format("%s: no such" + " file or directory%n", signPath);
            		} catch (DirectoryNotEmptyException x) {
            		    System.err.format("%s not empty%n", signPath);
            		} catch (IOException x) {
            		    // File permission problems are caught here.
            		    System.err.println(x);
            		}

            	cieDictionary.remove(pan);
            	
            	Gson gson = new Gson();
				String stringDictionary = gson.toJson(cieDictionary);
				Utils.setProperty("cieDictionary", stringDictionary);

				selectHome();
                break;
            default:
            	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Impossibile disabilitare la CIE di " + name, "CIE non disabilitata", JOptionPane.ERROR_MESSAGE);
                break;
        }
	}
	
	private void disabilitaAllCIE(List<Cie> cieList)
	{
		
		if(JOptionPane.showConfirmDialog(this.getContentPane(), "Vuoi disabilitare tutte le Cie attualmente abbinate?", "Disabilita CIE", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
			return;
		
				
		for(int i = 0; i<cieList.size(); i++)
		{
			
			int ret = Middleware.INSTANCE.DisabilitaCIE(cieList.get(i).getPan());
	        
	        switch (ret)
	        {
	            case CKR_OK:
	                
	            	cieDictionary.remove(cieList.get(i).getPan());
	                
	            	String signPath = getSignImagePath(cieList.get(i).getSerialNumber());
	            	try {
	            			Files.deleteIfExists(Paths.get(signPath));
	            		} catch (NoSuchFileException x) {
	            		    System.err.format("%s: no such" + " file or directory%n", signPath);
	            		} catch (DirectoryNotEmptyException x) {
	            		    System.err.format("%s not empty%n", signPath);
	            		} catch (IOException x) {
	            		    // File permission problems are caught here.
	            		    System.err.println(x);
	            		}
	            	
	            	Gson gson = new Gson();
					String stringDictionary = gson.toJson(cieDictionary);
					Utils.setProperty("cieDictionary", stringDictionary);
					
	                break;
	            default:
	            	JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Impossibile disabilitare la CIE numero " + cieList.get(i).getSerialNumber(), "CIE non disabilitata", JOptionPane.ERROR_MESSAGE);
	                break;
	        }
		}
		

        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE disabilitata con successo", "CIE disabilitata", JOptionPane.INFORMATION_MESSAGE);
		
		selectHome();

	}
	
	private void configureHomeButtons(Map<String, Cie> cieDictionary)
	{
		btnSelezionaCIE.setVisible(false);
		btnNewButton.setVisible(true);
		btnRemoveSelected.setVisible(true);
		
		if(cieDictionary.size() > 1)
		{
			btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
			btnRemoveAll.setVisible(true);
		}else
		{
			btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
			btnRemoveAll.setVisible(false);
		}
		
		if(cieDictionary.size() == 0)
		{
			btnAnnulla.setVisible(false);
		}else
		{
			btnAnnulla.setVisible(true);
		}
		
	}
	
	private void selectHome()
	{
		/*
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
		}*/
		        
		//Utils.setProperty("cieDictionary", "");
		

		txtpnCieAbbinataCon.setText("Carta di identità elettronica abbinata correttamente");
		txtpnCieAbbinataCon.setHighlighter(null);
		lblCieId.setText("CIE ID");
		
		if(!Utils.getProperty("serialnumber", "").equals(""))
		{
            String serialNumber = Utils.getProperty("serialnumber", "");
            String cardholder = Utils.getProperty("cardholder", "");
            String efSeriale = Utils.getProperty("ef_seriale", "");
            
            cieDictionary = new HashMap<String, Cie>();
            cieDictionary.put(serialNumber, new Cie(serialNumber, cardholder, efSeriale));
            
            /**********/
        	Gson gson = new Gson();
			String stringDictionary = gson.toJson(cieDictionary);
			Utils.setProperty("cieDictionary", stringDictionary);
			Utils.setProperty("serialnumber", "");
			Utils.setProperty("cardholder", "");
			Utils.setProperty("ef_seriale", "");
			/***********/
			
			cieCarousel.configureCards(cieDictionary);
			btnFirma.setEnabled(true);
			tabbedPane.setSelectedIndex(2);
            
		}else 
		{
			if(!Utils.getProperty("cieDictionary", "").equals("") && !Utils.getProperty("cieDictionary", "").equals("{}"))
			{
				Gson gson = new Gson();
				java.lang.reflect.Type type = new TypeToken<HashMap<String, Cie>>(){}.getType();
				cieDictionary = gson.fromJson(Utils.getProperty("cieDictionary", ""), type);
				
				for (String key : cieDictionary.keySet()) {
				    if(cieDictionary.get(key).getIsCustomSign() == null)
				    {
				    	cieDictionary.get(key).setIsCustomSign(false);

					    String signPath = getSignImagePath(cieDictionary.get(key).getSerialNumber());
					    drawText(cieDictionary.get(key).getName(), signPath);
				    }
				}
				
				String serialDictionary = gson.toJson(cieDictionary);
				Utils.setProperty("cieDictionary", serialDictionary);	
				
				cieCarousel.configureCards(cieDictionary);
				btnFirma.setEnabled(true);
				tabbedPane.setSelectedIndex(2);
			}else
			{
				btnFirma.setEnabled(false);
				cieDictionary = new HashMap<String, Cie>();
				
				tabbedPane.setSelectedIndex(0);
				EventQueue.invokeLater(new Runnable() 
		        {
					public void run() 
					{
						passwordField.requestFocus();
					}
		        });
			}
		}
				
		configureHomeButtons(cieDictionary);
		selectButton(btnHome);
	}
	
	private String toTitleCase(String input) {
	    StringBuilder titleCase = new StringBuilder(input.length());
	    boolean nextTitleCase = true;

	    for (char c : input.toCharArray()) {
	        if (Character.isSpaceChar(c)) {
	            nextTitleCase = true;
	        } else if (nextTitleCase) {
	            c = Character.toTitleCase(c);
	            nextTitleCase = false;
	        }

	        titleCase.append(c);
	    }

	    return titleCase.toString();
	}
	
	private String toFirstCharUpperAll(String string){
	     StringBuffer sb=new StringBuffer(string);
	        for(int i=0;i<sb.length();i++)
	            if(i==0 || sb.charAt(i-1)==' ')//first letter to uppercase by default
	                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
	     return sb.toString();
	}
	
	private void selectCardholder()
	{
		//tabbedPane.setSelectedIndex(2);
		selectHome();
		configureHomeButtons(cieDictionary);
		selectButton(btnHome);
	}
	
	private void selectUnlock()
	{
		tabbedPane.setSelectedIndex(5);
		selectButton(btnSbloccaCarta);
	}
}
