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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import it.ipzs.cieid.Logger.LogLevel;

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
import javax.swing.BoxLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JRadioButton;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import java.awt.Rectangle;
import java.awt.Point;

public class MainFrame extends JFrame {
    private Logger logger;
    private LogLevelConfig logConfig;
    private static final String LOG_CONFIG_PREFIX_APP = "APP_LOG_LEVEL";
    private static final String LOG_CONFIG_PREFIX_LIB = "LIB_LOG_LEVEL";

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
    private JButton btnChangePIN;
    private JButton btnUnlockCard;
    private JButton btnTutorial;
    private JButton btnHelp;
    private JButton btnInformation;
    private JPasswordField passwordField;
    private JPasswordField passwordField_1;
    private JPasswordField passwordField_2;
    private JPasswordField passwordField_3;
    private JPasswordField passwordField_4;
    private JPasswordField passwordField_5;
    private JPasswordField passwordField_6;
    private JPasswordField passwordField_7;

    private JPasswordField passwordFields[] = new JPasswordField[8];
    private JPasswordField passwordSignFields[] = new JPasswordField[8];
    private JPanel panel_PairCIE;
    private JLabel label;
    private JTextPane textPane_1;
    private JLabel label_1;
    private JCheckBox checkBox;
    private JPanel pnCIEHomeSelector_Index_3;
    private JLabel lblCieId;
    private JTextPane txtpnCIEPanelsSubtitle;
    private JLabel label_4;
    private JCheckBox checkBox_1;
    private JButton buttonRemove;
    private JLabel lblCardNumber;
    private JLabel labelSerial;
    private JLabel labelCardholder;
    private JButton btnPair;

    private String serialNumber;
    private String cardHolder;
    private String ef_seriale;
    private JProgressBar progressBar;
    private JLabel lblProgress;
    private JPanel pnChangePINTypingScreen_Index_4;
    private JLabel lblChangePIN;
    private JTextPane txtpnThePINOfYourCard;
    private JLabel label_5;
    private JCheckBox checkBox_2;
    private JLabel lblTypeTheOldValue;
    private JLabel lblTypeTheNewValue;
    private JLabel lblTypeAgainTheNewValue;
    private JPasswordField oldPIN;
    private JPasswordField newPIN;
    private JPasswordField repeatNewPIN;
    private JButton btnPerformChangePIN;
    private JPanel panel_5;
    private JLabel lblChangePINText;
    private JTextPane txtpnThePINOfYourCIE;
    private JLabel label_6;
    private JCheckBox checkBox_3;
    private JLabel labelProgressChangePIN;
    private JProgressBar progressBarChangePIN;
    private JPanel panel_6;
    private JTextPane txtpnUseYourPUK;
    private JLabel label_7;
    private JCheckBox checkBox_4;
    private JButton btnUnlockPIN;
    private JLabel lblInsertYourPUK;
    private JLabel label_9;
    private JPasswordField pin01;
    private JLabel label_10;
    private JPasswordField pin02;
    private JTextPane textPane_2;
    private JPasswordField puk01;
    private JPanel panel_7;
    private JLabel lblUnlockCard;
    private JTextPane txtpnTypeThePUKOfYourCIE;
    private JLabel label_8;
    private JCheckBox checkBox_5;
    private JLabel labelProgressUnlock;
    private JProgressBar progressBarUnlock;
    private JPanel panel_8;
    private JLabel lblHelp;
    private JPanel panel_9;
    private JLabel label_11;
    private MiniWebView miniWebView;
    private JPanel panel_10;
    private JLabel lblInformation;
    private MiniWebView miniWebView_1;
    private JPanel btnPanel;
    private JButton btnRemoveAll;
    private JButton btnRemoveSelected;
    private JButton btnNewButton;
    private JButton btnCancel;
    private carousel cieCarousel;
    private Map<String, Cie> cieDictionary;
    private JButton btnDigitalSignature;

    private String filePath;
    private JPanel selectFile;
    private JLabel lblDigitalSignature;
    private JPanel panelLoadFile;
    private JLabel lblNewLabel;
    private JTextArea txtrDragAndDropDocuments;
    private JTextArea txtrOtherwise;
    private JButton btnSelectDocument;
    private JPanel panel_11;
    private JTextArea lblSFP;
    private JLabel lblCustomize;
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
    private JButton btnCancelOp;
    private JPanel panel_13;
    private JPanel panel_14;
    private JPanel selectSignatureOperation;
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
    private JButton btnUndoPINTyping;
    private JLabel lblNewLabel_12;
    private JButton btnSignInPINTypingScreen;
    private JPanel panel_28;
    private JLabel lblNewLabel1_1;
    private JLabel lblProgressSignPIN;
    private JPasswordField passwordField_8;
    private JPasswordField passwordField_9;
    private JPasswordField passwordField_10;
    private JPasswordField passwordField_11;
    private JPasswordField passwordField_12;
    private JPasswordField passwordField_13;
    private JPasswordField passwordField_14;
    private JPasswordField passwordField_15;
    private JLabel lblSignatureResult;
    private JLabel imgSignatureResult;
    private JPanel panel_29;
    private JPanel customizeGraphicSignature;
    private JLabel lblFirmaElettronica_5;
    private JButton btnAnnullaOp_6;
    private JPanel panel_31;
    private JProgressBar progressSignPIN;
    private JButton btnSignCompleted;
    PdfPreview preview;
    private JButton btnSelectCIE;
    private JButton btnSignWithoutPairing;
    private JLabel lblCustomizedGraphicSignature;
    private JTextArea lblHint;
    private JLabel lblFPOK;
    private JPanel pnVerify;
    private JLabel lblFirmaElettronica_6;
    private JScrollPane verifyScrollPane;
    private JPanel panel_32;
    private JButton btnConcludiVerifica;
    private JTextArea lblPathVerifica;
    private JButton btnProseguiOp;
    private JButton btnGenerateGraphicSignature;
    private JPanel pnSettings;
    private JLabel lblConfigProxyTitle;
    private JPanel configProxyBodyPanel;
    private JButton btnSave;
    private JLabel lblConfigProxyCaption;
    private JTextField txtProxyAddr;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtPort;
    private JButton btnSettings;
    private JButton btnChangeProxy;
    private JCheckBox chckbxShowPassword;
    private JButton btnExtractP7M;
    private JPanel configButtonsPanel;
    private Component verticalGlue;
    private Component verticalGlue_1;
    private Component verticalGlue_2;
    private Component verticalGlue_3;
    private Component verticalGlue_4;
    private Component verticalGlue_5;
    private Component verticalGlue_6;
    private Component verticalGlue_7;
    private Component verticalGlue_8;
    private Component verticalGlue_9;
    private JPanel panelConfigLoggingApp;
    private JPanel panelConfigLoggingLib;
    private JRadioButton rdbtnLoggingAppNone;
    private JRadioButton rdbtnLoggingAppError;
    private JRadioButton rdbtnLoggingAppInfo;
    private JRadioButton rdbtnLoggingAppDebug;
    private JRadioButton rdbtnLoggingLibError;
    private JRadioButton rdbtnLoggingLibInfo;
    private JRadioButton rdbtnLoggingLibDebug;
    private JRadioButton rdbtnLoggingLibNone;
    private final ButtonGroup buttonGroupLoggingApp = new ButtonGroup();
    private final ButtonGroup buttonGroupLoggingLib = new ButtonGroup();
    private JButton btnDigitalSignatureVerify;
    private boolean shouldSignWithoutPairing = false;
    private String signingCIEPAN = "";
    private JPanel configPreferencesPanel;
    private JLabel lblConfigPreferencesTitle;
    private JLabel lblConfigPreferencesCaption;
    private JLabel lblConfigPreferencesCaption_1;
    private JCheckBox cboxShowTutorial;
    private JButton btnDeleteLogs;
    private JButton btnCollectLogs;

    private enum SignOp {
        OP_NONE,
        PADES,
        CADES,
        VERIFY
    }

    public class LogLevelConfig {
        private LogLevel app;
        private LogLevel lib;

        public LogLevelConfig() {
            this(Logger.defaultLogLevel, Logger.defaultLogLevel);
        }

        public LogLevelConfig(LogLevel appLevel, LogLevel libLevel) {
            this.app = appLevel;
            this.lib = libLevel;
        }
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
        logger = Logger.getInstance();
        logConfig = new LogLevelConfig();
        loadLogConfigFromFile();
        logger.Info("Inizializza frame principale");
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
                logger.Info("Inizia  'Home'");
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
        btnChangePIN = new JButton("   Cambia PIN");
        btnChangePIN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Cambia PIN'");
                selectButton(btnChangePIN);
                oldPIN.setText("");
                newPIN.setText("");
                repeatNewPIN.setText("");
                tabbedPane.setSelectedIndex(3);
            }
        });
        btnChangePIN.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 24.png")));
        btnChangePIN.setHorizontalAlignment(SwingConstants.LEFT);
        btnChangePIN.setBorderPainted(false);
        btnChangePIN.setBackground(SystemColor.control);
        btnChangePIN.setBounds(0, 265, 200, 45);
        leftPanel.add(btnChangePIN);
        btnUnlockCard = new JButton("   Sblocca Carta");
        btnUnlockCard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Sblocca Carta'");
                selectButton(btnUnlockCard);
                pin01.setText("");
                pin02.setText("");
                puk01.setText("");
                tabbedPane.setSelectedIndex(5);
            }
        });
        btnUnlockCard.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 23.png")));
        btnUnlockCard.setHorizontalAlignment(SwingConstants.LEFT);
        btnUnlockCard.setBorderPainted(false);
        btnUnlockCard.setBackground(SystemColor.control);
        btnUnlockCard.setBounds(0, 310, 200, 45);
        leftPanel.add(btnUnlockCard);
        btnTutorial = new JButton("   Tutorial");
        btnTutorial.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Tutorial'");
                selectButton(btnTutorial);
                tabbedPane.setSelectedIndex(7);
            }
        });
        btnTutorial.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 22.png")));
        btnTutorial.setHorizontalAlignment(SwingConstants.LEFT);
        btnTutorial.setBorderPainted(false);
        btnTutorial.setBackground(SystemColor.window);
        btnTutorial.setBounds(0, 355, 200, 45);
        leftPanel.add(btnTutorial);
        btnHelp = new JButton("   Aiuto");
        btnHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Aiuto'");
                selectButton(btnHelp);
                tabbedPane.setSelectedIndex(8);
            }
        });
        btnHelp.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 21.png")));
        btnHelp.setHorizontalAlignment(SwingConstants.LEFT);
        btnHelp.setBorderPainted(false);
        btnHelp.setBackground(SystemColor.control);
        btnHelp.setBounds(0, 400, 200, 45);
        leftPanel.add(btnHelp);
        btnInformation = new JButton("   Informazioni");
        btnInformation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Informazioni'");
                selectButton(btnInformation);
                tabbedPane.setSelectedIndex(9);
            }
        });
        btnInformation.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Risorsa 20.png")));
        btnInformation.setHorizontalAlignment(SwingConstants.LEFT);
        btnInformation.setBorderPainted(false);
        btnInformation.setBackground(SystemColor.control);
        btnInformation.setBounds(0, 445, 200, 45);
        leftPanel.add(btnInformation);
        btnDigitalSignature = new JButton("   Firma Elettronica");
        btnDigitalSignature.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Firma Elettronica'");
                selectButton(btnDigitalSignature);
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
                progressSignPIN.setVisible(false);

                showSigningPINInputFields();       

                imgSignatureResult.setVisible(false);
                lblSignatureResult.setVisible(false);
                btnSignCompleted.setVisible(false);
                btnUndoPINTyping.setVisible(true);
                btnSignInPINTypingScreen.setVisible(true);
                btnUndoPINTyping.setEnabled(true);
                btnSignInPINTypingScreen.setEnabled(true);
                btnDigitalSignature.setVisible(true);
                txtpnCIEPanelsSubtitle.setText("Seleziona la CIE da usare");
                lblCieId.setText("Firma Elettronica");
                btnSelectCIE.setVisible(true);
                btnSignWithoutPairing.setVisible(true);
                btnRemoveAll.setVisible(false);
                btnRemoveSelected.setVisible(false);
                btnNewButton.setVisible(false);
                lblDigitalSignature.setText("Firma Elettronica");
                lblSFP.setVisible(true);
                lblNewLabel_2.setVisible(true);
                lblCustomize.setVisible(true);
                
                if(cieDictionary.size() != 0)
                	tabbedPane.setSelectedIndex(2);
                else
                	btnSignWithoutPairing.doClick();
            }
        });
        btnDigitalSignature.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/firma_gray.png")));
        btnDigitalSignature.setHorizontalAlignment(SwingConstants.LEFT);
        btnDigitalSignature.setBorderPainted(false);
        btnDigitalSignature.setBackground(SystemColor.control);
        btnDigitalSignature.setBounds(0, 175, 200, 45);
        leftPanel.add(btnDigitalSignature);
        btnDigitalSignatureVerify = new JButton("   Verifica firma");
        btnDigitalSignatureVerify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Verifica Firma'");
                selectButton(btnDigitalSignatureVerify);
                signOperation = SignOp.VERIFY;
                lblDigitalSignature.setText("Verifica firma");
                lblSFP.setVisible(false);
                lblFPOK.setVisible(false);
                lblNewLabel_2.setVisible(false);
                lblCustomize.setVisible(false);
                tabbedPane.setSelectedIndex(10);
            }
        });
        btnDigitalSignatureVerify.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/firma_gray.png")));
        btnDigitalSignatureVerify.setHorizontalAlignment(SwingConstants.LEFT);
        btnDigitalSignatureVerify.setBorderPainted(false);
        btnDigitalSignatureVerify.setBackground(SystemColor.control);
        btnDigitalSignatureVerify.setBounds(0, 220, 200, 45);
        leftPanel.add(btnDigitalSignatureVerify);
        btnSettings = new JButton("   Impostazioni");
        btnSettings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Impostazioni'");
                loadLogConfigFromFile();
                disableConfigurationPaneControls();

                if (!Utils.getProperty("proxyURL", "").equals("")) {
                    logger.Debug("  Impostazione proxy presente");

                    if (Utils.getProperty("credentials", "").equals("")) {
                        txtPassword.setText("");
                        txtUsername.setText("");
                    } else {
                        String encryptedCredentials = Utils.getProperty("credentials", "");
                        ProxyInfoManager proxyInfoManager = new ProxyInfoManager();
                        String credentials = proxyInfoManager.decrypt(encryptedCredentials);
                        System.out.println("Credentials -> " + credentials);

                        if (credentials.substring(0, 5).equals("cred=")) {
                            String[] infos = credentials.substring(5).split(":");
                            txtUsername.setText(infos[0]);
                            txtPassword.setText(infos[1]);
                        }
                    }

                    txtProxyAddr.setText(Utils.getProperty("proxyURL", ""));
                    txtPort.setText(Utils.getProperty("proxyPort", ""));
                }

                rdbtnLoggingAppNone.setSelected(logConfig.app.equals(LogLevel.NONE));
                rdbtnLoggingAppDebug.setSelected(logConfig.app.equals(LogLevel.DEBUG));
                rdbtnLoggingAppInfo.setSelected(logConfig.app.equals(LogLevel.INFO));
                rdbtnLoggingAppError.setSelected(logConfig.app.equals(LogLevel.ERROR));
                rdbtnLoggingLibNone.setSelected(logConfig.lib.equals(LogLevel.NONE));
                rdbtnLoggingLibDebug.setSelected(logConfig.lib.equals(LogLevel.DEBUG));
                rdbtnLoggingLibInfo.setSelected(logConfig.lib.equals(LogLevel.INFO));
                rdbtnLoggingLibError.setSelected(logConfig.lib.equals(LogLevel.ERROR));
                selectButton(btnSettings);
                boolean showTutorial = ("false".equals(Utils.getProperty("nomore", "false"))) ?
                		true : false;
                cboxShowTutorial.setSelected(showTutorial);
                tabbedPane.setSelectedIndex(17);
            }
        });
        btnSettings.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/settings_icon.png")));
        btnSettings.setHorizontalAlignment(SwingConstants.LEFT);
        btnSettings.setBorderPainted(false);
        btnSettings.setBackground(SystemColor.window);
        btnSettings.setBounds(0, 490, 200, 45);
        leftPanel.add(btnSettings);
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(200, -65, 600, 635);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("TabbedPan Selected Index: " + tabbedPane.getSelectedIndex());
            }
        });
        contentPane.add(tabbedPane);
        JPanel pnPairCIE_Index_1 = new JPanel();
        tabbedPane.addTab("New tab", null, pnPairCIE_Index_1, null);
        pnPairCIE_Index_1.setLayout(null);
        pnPairCIE_Index_1.setBackground(Color.WHITE);
        JLabel lblPairYourCIE = new JLabel("Abbina la tua CIE");
        lblPairYourCIE.setHorizontalAlignment(SwingConstants.CENTER);
        lblPairYourCIE.setFont(new Font("Dialog", Font.BOLD, 30));
        lblPairYourCIE.setBounds(147, 36, 299, 36);
        pnPairCIE_Index_1.add(lblPairYourCIE);
        JTextPane txtpnConnectAndPlaceYourCIEOnTheReader = new JTextPane();
        txtpnConnectAndPlaceYourCIEOnTheReader.setFont(new Font("Dialog", Font.PLAIN, 16));
        txtpnConnectAndPlaceYourCIEOnTheReader.setText("Dopo aver collegato e installato il lettore di smart card, posiziona la CIE sul lettore ed inserisci il PIN");
        txtpnConnectAndPlaceYourCIEOnTheReader.setEditable(false);
        txtpnConnectAndPlaceYourCIEOnTheReader.setBounds(63, 84, 492, 46);
        pnPairCIE_Index_1.add(txtpnConnectAndPlaceYourCIEOnTheReader);
        JLabel lblNewLabel1 = new JLabel("");
        lblNewLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
        lblNewLabel1.setBounds(29, 194, 211, 205);
        pnPairCIE_Index_1.add(lblNewLabel1);
        btnPair = new JButton("Abbina");
        btnPair.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Abbina'");
                pairCIE();
            }
        });
        btnPair.setForeground(Color.WHITE);
        btnPair.setBackground(new Color(30, 144, 255));
        btnPair.setBounds(258, 524, 114, 25);
        //pnPairCIE_Index_1.add(btnPair);
        passwordFields[0] = passwordField = new JPasswordField();
        passwordField.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField.setBounds(250, 321, 25, 25);
        pnPairCIE_Index_1.add(passwordField);
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_1.requestFocus();
                }
            }
        });
        passwordFields[1] = passwordField_1 = new JPasswordField();
        passwordField_1.setBounds(287, 321, 25, 25);
        passwordField_1.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_1.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_1);
        passwordField_1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField.setText("");
                    passwordField.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_2.requestFocus();
                }
            }
        });
        passwordFields[2] = passwordField_2 = new JPasswordField();
        passwordField_2.setBounds(324, 321, 25, 25);
        passwordField_2.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_2.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_2);
        passwordField_2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_1.setText("");
                    passwordField_1.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_3.requestFocus();
                }
            }
        });
        passwordFields[3] = passwordField_3 = new JPasswordField();
        passwordField_3.setBounds(362, 321, 25, 25);
        passwordField_3.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_3.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_3);
        passwordField_3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_2.setText("");
                    passwordField_2.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_4.requestFocus();
                }
            }
        });
        passwordFields[4] = passwordField_4 = new JPasswordField();
        passwordField_4.setBounds(399, 321, 25, 25);
        passwordField_4.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_4.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_4);
        passwordField_4.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_3.setText("");
                    passwordField_3.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_5.requestFocus();
                }
            }
        });
        passwordFields[5] = passwordField_5 = new JPasswordField();
        passwordField_5.setBounds(436, 321, 25, 25);
        passwordField_5.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_5.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_5);
        passwordField_5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_4.setText("");
                    passwordField_4.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_6.requestFocus();
                }
            }
        });
        passwordFields[6] = passwordField_6 = new JPasswordField();
        passwordField_6.setBounds(473, 321, 25, 25);
        passwordField_6.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_6.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_6);
        passwordField_6.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_5.setText("");
                    passwordField_5.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_7.requestFocus();
                }
            }
        });
        passwordFields[7] = passwordField_7 = new JPasswordField();
        passwordField_7.setBounds(510, 321, 25, 25);
        passwordField_7.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_7.setFont(new Font("Dialog", Font.BOLD, 25));
        pnPairCIE_Index_1.add(passwordField_7);
        passwordField_7.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n' || e.getKeyChar() == '\r') {
                    pairCIE();
                } else if (e.getKeyChar() == '\b') {
                    passwordField_6.setText("");
                    passwordField_6.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else if (passwordField_7.getText().length() > 0) {
                    e.consume();
                }
            }
        });
        JLabel lblTypeYourCIEPIN = new JLabel("Inserisci il PIN");
        lblTypeYourCIEPIN.setHorizontalAlignment(SwingConstants.CENTER);
        lblTypeYourCIEPIN.setFont(new Font("Dialog", Font.BOLD, 22));
        lblTypeYourCIEPIN.setBounds(252, 259, 299, 36);
        pnPairCIE_Index_1.add(lblTypeYourCIEPIN);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBackground(new Color(255, 255, 255));
        FlowLayout flowLayout = (FlowLayout) buttonsPanel.getLayout();
        flowLayout.setHgap(100);
        buttonsPanel.setBounds(133, 500, 384, 36);
        btnCancel = new JButton("Annulla");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Annulla abbinamento carta");
                selectHome();
            }
        });
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setBackground(new Color(30, 144, 255));
        buttonsPanel.add(btnCancel);
        buttonsPanel.add(btnPair);
        pnPairCIE_Index_1.add(buttonsPanel);
        panel_PairCIE = new JPanel();
        panel_PairCIE.setLayout(null);
        panel_PairCIE.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, panel_PairCIE, null);
        label = new JLabel("Abbina la tua CIE");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, 30));
        label.setBounds(147, 36, 299, 36);
        panel_PairCIE.add(label);
        textPane_1 = new JTextPane();
        textPane_1.setFont(new Font("Dialog", Font.PLAIN, 16));
        textPane_1.setText("Dopo aver collegato e installato il lettore di smart card, posiziona la CIE sul lettore ed inserisci il PIN");
        textPane_1.setEditable(false);
        textPane_1.setBounds(63, 84, 492, 46);
        panel_PairCIE.add(textPane_1);
        label_1 = new JLabel("");
        label_1.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
        label_1.setHorizontalAlignment(SwingConstants.CENTER);
        label_1.setBounds(29, 194, 211, 205);
        panel_PairCIE.add(label_1);
        checkBox = new JCheckBox("Non mostrare piÃ¹");
        checkBox.setBackground(Color.WHITE);
        checkBox.setBounds(591, 508, 157, 23);
        panel_PairCIE.add(checkBox);
        lblProgress = new JLabel("progress");
        lblProgress.setHorizontalAlignment(SwingConstants.CENTER);
        lblProgress.setFont(new Font("Dialog", Font.BOLD, 22));
        lblProgress.setBounds(252, 259, 299, 36);
        panel_PairCIE.add(lblProgress);
        progressBar = new JProgressBar();
        progressBar.setBounds(258, 324, 293, 14);
        panel_PairCIE.add(progressBar);
        pnCIEHomeSelector_Index_3 = new JPanel();
        pnCIEHomeSelector_Index_3.setLayout(null);
        pnCIEHomeSelector_Index_3.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, pnCIEHomeSelector_Index_3, null);
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
        pnCIEHomeSelector_Index_3.add(cieCarousel);
        btnPanel = new JPanel();
        btnPanel.setBounds(0, 500, 595, 46);
        btnPanel.setBackground(Color.WHITE);
        btnRemoveAll = new JButton("Rimuovi tutte");
        btnRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  Rimozione tutte le carte abbinate");
                List<Cie> cieList = new ArrayList<Cie>(cieDictionary.values());
                removeAllCIE(cieList);
            }
        });
        btnRemoveAll.setForeground(Color.WHITE);
        btnRemoveAll.setBackground(new Color(30, 144, 255));
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnPanel.add(btnRemoveAll);
        btnRemoveSelected = new JButton("Rimuovi carta selezionata");
        btnRemoveSelected.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Rimuovi carta selezionata'");
                Cie cieToDelete = cieCarousel.getCardAtIndex();
                removeCIE(cieToDelete.getPan(), cieToDelete.getName());
            }
        });
        btnRemoveSelected.setForeground(Color.WHITE);
        btnRemoveSelected.setBackground(new Color(30, 144, 255));
        btnPanel.add(btnRemoveSelected);
        btnNewButton = new JButton("Aggiungi carta");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Aggiungi carta'");
                tabbedPane.setSelectedIndex(0);
                //abbinaCIE();
            }
        });
        btnNewButton.setForeground(Color.WHITE);
        btnNewButton.setBackground(new Color(30, 144, 255));
        btnPanel.add(btnNewButton);
        pnCIEHomeSelector_Index_3.add(btnPanel);
        
        btnSelectCIE = new JButton("Firma con la CIE selezionata");
        btnSelectCIE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Seleziona'");
                CieCard selectedCIE = getSelectedCIE();
                shouldSignWithoutPairing = false;

                lblNewLabel_2.setVisible(true);
                showSigningPINInputFields();
                
                if (selectedCIE.getCard().getIsCustomSign()) {
                    logger.Debug("Firma personalizzata presente");
                    lblCustomize.setText("Aggiorna");
                    lblHint.setText("La tua firma personalizzata è già stata caricata. Vuoi aggiornarla?");
                    lblFPOK.setVisible(true);
                    lblSFP.setVisible(false);
                } else {
                    logger.Debug("Firma personalizzata assente - crea firma default");
                    lblCustomize.setText("Personalizza");
                    lblHint.setText("Abbiamo creato per te una firma grafica, ma se preferisci puoi personalizzarla. "
                                    + "Questo passaggio non è indispensabile, ma ti consentirà di dare un tocco personale ai documenti firmati.");
                    lblSFP.setVisible(true);
                    lblFPOK.setVisible(false);
                }

                tabbedPane.setSelectedIndex(10);
            }
        });
        btnSelectCIE.setForeground(Color.WHITE);
        btnSelectCIE.setBackground(new Color(30, 144, 255));
        btnSelectCIE.setVisible(false);
        btnPanel.add(btnSelectCIE);
        
        btnSignWithoutPairing = new JButton("Firma con CIE non associata");
        btnSignWithoutPairing.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	shouldSignWithoutPairing = true;

                lblNewLabel_2.setVisible(false);
                lblSFP.setVisible(false);
                lblFPOK.setVisible(false);
                lblCustomize.setVisible(false);

                lblProgressSignPIN.setText("Inserisci le 8 cifre del PIN");
                showSigningPINInputFields();       
                tabbedPane.setSelectedIndex(10);
            }
        });
        btnSignWithoutPairing.setForeground(Color.WHITE);
        btnSignWithoutPairing.setBackground(new Color(30, 144, 255));
        btnSignWithoutPairing.setVisible(false);
        btnPanel.add(btnSignWithoutPairing);
        
        lblCieId = new JLabel("CIE ID");
        lblCieId.setHorizontalAlignment(SwingConstants.CENTER);
        lblCieId.setFont(new Font("Dialog", Font.BOLD, 30));
        lblCieId.setBounds(147, 36, 299, 36);
        pnCIEHomeSelector_Index_3.add(lblCieId);
        txtpnCIEPanelsSubtitle = new JTextPane();
        txtpnCIEPanelsSubtitle.setEditable(false);
        txtpnCIEPanelsSubtitle.setFont(new Font("Dialog", Font.PLAIN, 16));
        txtpnCIEPanelsSubtitle.setText("Carta di Identità Elettronica abbinata correttamente");
        txtpnCIEPanelsSubtitle.setBounds(63, 84, 492, 46);
        pnCIEHomeSelector_Index_3.add(txtpnCIEPanelsSubtitle);
        pnChangePINTypingScreen_Index_4 = new JPanel();
        pnChangePINTypingScreen_Index_4.setLayout(null);
        pnChangePINTypingScreen_Index_4.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, pnChangePINTypingScreen_Index_4, null);
        lblChangePIN = new JLabel("Cambia PIN");
        lblChangePIN.setHorizontalAlignment(SwingConstants.CENTER);
        lblChangePIN.setFont(new Font("Dialog", Font.BOLD, 30));
        lblChangePIN.setBounds(147, 36, 299, 36);
        pnChangePINTypingScreen_Index_4.add(lblChangePIN);
        txtpnThePINOfYourCard = new JTextPane();
        txtpnThePINOfYourCard.setText("Il PIN della tua CIE è un dato sensibile,\ntrattalo con cautela.");
        txtpnThePINOfYourCard.setFont(new Font("Dialog", Font.PLAIN, 16));
        txtpnThePINOfYourCard.setEditable(false);
        txtpnThePINOfYourCard.setBounds(147, 84, 316, 46);
        pnChangePINTypingScreen_Index_4.add(txtpnThePINOfYourCard);
        label_5 = new JLabel("");
        label_5.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
        label_5.setHorizontalAlignment(SwingConstants.CENTER);
        label_5.setBounds(29, 194, 211, 205);
        pnChangePINTypingScreen_Index_4.add(label_5);
        checkBox_2 = new JCheckBox("Non mostrare piÃ¹");
        checkBox_2.setBackground(Color.WHITE);
        checkBox_2.setBounds(591, 508, 157, 23);
        pnChangePINTypingScreen_Index_4.add(checkBox_2);
        btnPerformChangePIN = new JButton("Cambia PIN");
        btnPerformChangePIN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Cambia PIN'");
                changePIN();
            }
        });
        btnPerformChangePIN.setForeground(Color.WHITE);
        btnPerformChangePIN.setBackground(new Color(30, 144, 255));
        btnPerformChangePIN.setBounds(206, 507, 114, 25);
        pnChangePINTypingScreen_Index_4.add(btnPerformChangePIN);
        lblTypeTheOldValue = new JLabel("Inserisci il vecchio PIN");
        lblTypeTheOldValue.setHorizontalAlignment(SwingConstants.LEFT);
        lblTypeTheOldValue.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblTypeTheOldValue.setBounds(252, 201, 299, 36);
        pnChangePINTypingScreen_Index_4.add(lblTypeTheOldValue);
        oldPIN = new JPasswordField();
        oldPIN.setBounds(252, 230, 234, 25);
        oldPIN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    changePIN();
                }
            }
        });
        pnChangePINTypingScreen_Index_4.add(oldPIN);
        lblTypeTheNewValue = new JLabel("Inserisci il nuovo PIN");
        lblTypeTheNewValue.setHorizontalAlignment(SwingConstants.LEFT);
        lblTypeTheNewValue.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblTypeTheNewValue.setBounds(252, 266, 299, 36);
        pnChangePINTypingScreen_Index_4.add(lblTypeTheNewValue);
        newPIN = new JPasswordField();
        newPIN.setBounds(252, 295, 234, 25);
        newPIN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    changePIN();
                }
            }
        });
        pnChangePINTypingScreen_Index_4.add(newPIN);
        lblTypeAgainTheNewValue = new JLabel("Ripeti il nuovo PIN");
        lblTypeAgainTheNewValue.setHorizontalAlignment(SwingConstants.LEFT);
        lblTypeAgainTheNewValue.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblTypeAgainTheNewValue.setBounds(252, 332, 299, 36);
        pnChangePINTypingScreen_Index_4.add(lblTypeAgainTheNewValue);
        repeatNewPIN = new JPasswordField();
        repeatNewPIN.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    changePIN();
                }
            }
        });
        repeatNewPIN.setBounds(252, 361, 234, 25);
        pnChangePINTypingScreen_Index_4.add(repeatNewPIN);
        JTextPane txtpnThePINOfYourCIEMustContainEightDigits = new JTextPane();
        txtpnThePINOfYourCIEMustContainEightDigits.setFont(new Font("Dialog", Font.PLAIN, 10));
        txtpnThePINOfYourCIEMustContainEightDigits.setText("Il PIN della CIE deve essere composto da 8 cifre numeriche, non sono ammessi altri tipi di caratteri. Non sono ammessi PIN composti da tutti i numeri uguali (es: 11111111) o da numeri consecutivi (es: 12345678).");
        txtpnThePINOfYourCIEMustContainEightDigits.setBounds(262, 398, 234, 76);
        txtpnThePINOfYourCIEMustContainEightDigits.setEditable(false);
        pnChangePINTypingScreen_Index_4.add(txtpnThePINOfYourCIEMustContainEightDigits);
        panel_5 = new JPanel();
        panel_5.setLayout(null);
        panel_5.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, panel_5, null);
        lblChangePINText = new JLabel("Cambia PIN");
        lblChangePINText.setHorizontalAlignment(SwingConstants.CENTER);
        lblChangePINText.setFont(new Font("Dialog", Font.BOLD, 30));
        lblChangePINText.setBounds(147, 36, 299, 36);
        panel_5.add(lblChangePINText);
        txtpnThePINOfYourCIE = new JTextPane();
        txtpnThePINOfYourCIE.setText("Il PIN della tua CIE è un dato sensibile,\ntrattalo con cautela.");
        txtpnThePINOfYourCIE.setFont(new Font("Dialog", Font.PLAIN, 16));
        txtpnThePINOfYourCIE.setEditable(false);
        txtpnThePINOfYourCIE.setBounds(147, 84, 327, 46);
        panel_5.add(txtpnThePINOfYourCIE);
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
        lblUnlockCard = new JLabel("Sblocco Carta");
        lblUnlockCard.setHorizontalAlignment(SwingConstants.CENTER);
        lblUnlockCard.setFont(new Font("Dialog", Font.BOLD, 30));
        lblUnlockCard.setBounds(147, 36, 299, 36);
        panel_6.add(lblUnlockCard);
        txtpnUseYourPUK = new JTextPane();
        txtpnUseYourPUK.setText("Utilizza il codice PUK ricevuto con la CIE");
        txtpnUseYourPUK.setFont(new Font("Dialog", Font.PLAIN, 16));
        txtpnUseYourPUK.setEditable(false);
        txtpnUseYourPUK.setBounds(126, 84, 334, 46);
        label_7 = new JLabel("");
        label_7.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/icona_lettore_card_white.png")));
        label_7.setHorizontalAlignment(SwingConstants.CENTER);
        label_7.setBounds(29, 194, 211, 205);
        panel_6.add(label_7);
        checkBox_4 = new JCheckBox("Non mostrare piÃ¹");
        checkBox_4.setBackground(Color.WHITE);
        checkBox_4.setBounds(591, 508, 157, 23);
        panel_6.add(checkBox_4);
        btnUnlockPIN = new JButton("Sblocca");
        btnUnlockPIN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Sblocca'");
                unlockPIN();
            }
        });
        btnUnlockPIN.setForeground(Color.WHITE);
        btnUnlockPIN.setBackground(new Color(30, 144, 255));
        btnUnlockPIN.setBounds(206, 507, 114, 25);
        panel_6.add(btnUnlockPIN);
        lblInsertYourPUK = new JLabel("Inserisci il PUK");
        lblInsertYourPUK.setHorizontalAlignment(SwingConstants.LEFT);
        lblInsertYourPUK.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblInsertYourPUK.setBounds(252, 201, 299, 36);
        panel_6.add(lblInsertYourPUK);
        puk01 = new JPasswordField();
        puk01.setBounds(252, 230, 234, 25);
        puk01.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    unlockPIN();
                }
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
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    unlockPIN();
                }
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
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    unlockPIN();
                }
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
        lblUnlockCard = new JLabel("Sblocca Carta");
        lblUnlockCard.setHorizontalAlignment(SwingConstants.CENTER);
        lblUnlockCard.setFont(new Font("Dialog", Font.BOLD, 30));
        lblUnlockCard.setBounds(147, 36, 299, 36);
        panel_7.add(lblUnlockCard);
        txtpnTypeThePUKOfYourCIE = new JTextPane();
        txtpnTypeThePUKOfYourCIE.setText("Utilizza il codice PUK ricevuto con la CIE");
        txtpnTypeThePUKOfYourCIE.setFont(new Font("Dialog", Font.PLAIN, 16));
        txtpnTypeThePUKOfYourCIE.setEditable(false);
        txtpnTypeThePUKOfYourCIE.setBounds(132, 84, 355, 46);
        panel_7.add(txtpnTypeThePUKOfYourCIE);
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
        lblHelp = new JLabel("Tutorial");
        lblHelp.setHorizontalAlignment(SwingConstants.CENTER);
        lblHelp.setFont(new Font("Dialog", Font.BOLD, 30));
        lblHelp.setBounds(147, 36, 299, 36);
        panel_8.add(lblHelp);
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
        lblInformation = new JLabel("Informazioni");
        lblInformation.setHorizontalAlignment(SwingConstants.CENTER);
        lblInformation.setFont(new Font("Dialog", Font.BOLD, 30));
        lblInformation.setBounds(147, 36, 299, 36);
        panel_10.add(lblInformation);
        miniWebView_1 = new MiniWebView();
        miniWebView_1.setBounds(12, 99, 571, 362);
        panel_10.add(miniWebView_1);
        miniWebView_1.showPage("https://idserver.servizicie.interno.gov.it/idp/privacy.jsp");
        StyledDocument doc = textPane_1.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc = txtpnConnectAndPlaceYourCIEOnTheReader.getStyledDocument();
        center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc = txtpnCIEPanelsSubtitle.getStyledDocument();
        center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        doc = txtpnUseYourPUK.getStyledDocument();
        center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        panel_6.add(txtpnUseYourPUK);
        selectFile = new JPanel();
        selectFile.setBackground(SystemColor.text);
        selectFile.setLayout(null);
        tabbedPane.addTab("New tab", null, selectFile, null);
        lblDigitalSignature = new JLabel("Firma Elettronica");
        lblDigitalSignature.setHorizontalAlignment(SwingConstants.CENTER);
        lblDigitalSignature.setFont(new Font("Dialog", Font.BOLD, 30));
        lblDigitalSignature.setBounds(165, 45, 302, 39);
        selectFile.add(lblDigitalSignature);
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
        new FileDrop( panelLoadFile, new FileDrop.Listener() {
            public void  filesDropped( java.io.File[] files ) {
                // handle file drop
                logger.Info("Inizia  'filesDropped'");
                filePath = files[0].getAbsolutePath();
                chooseSignOrVerifyFileOperation(filePath);
            }   // end filesDropped
        });
        lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/upload.png")));
        lblNewLabel.setBounds(223, 12, 80, 104);
        panelLoadFile.add(lblNewLabel);
        txtrDragAndDropDocuments = new JTextArea();
        txtrDragAndDropDocuments.setHighlighter(null);
        txtrDragAndDropDocuments.setWrapStyleWord(true);
        txtrDragAndDropDocuments.setText("Trascina i tuoi documenti qui dentro per firmarli o per verificare una firma elettronica esistente");
        txtrDragAndDropDocuments.setRows(3);
        txtrDragAndDropDocuments.setLineWrap(true);
        txtrDragAndDropDocuments.setFont(new Font("Dialog", Font.PLAIN, 15));
        txtrDragAndDropDocuments.setEditable(false);
        txtrDragAndDropDocuments.setBackground(SystemColor.text);
        txtrDragAndDropDocuments.setBounds(70, 141, 385, 47);
        panelLoadFile.add(txtrDragAndDropDocuments);
        txtrOtherwise = new JTextArea();
        txtrOtherwise.setHighlighter(null);
        txtrOtherwise.setWrapStyleWord(true);
        txtrOtherwise.setText("oppure");
        txtrOtherwise.setRows(3);
        txtrOtherwise.setLineWrap(true);
        txtrOtherwise.setFont(new Font("Dialog", Font.PLAIN, 15));
        txtrOtherwise.setEditable(false);
        txtrOtherwise.setBackground(SystemColor.text);
        txtrOtherwise.setBounds(239, 212, 64, 23);
        panelLoadFile.add(txtrOtherwise);
        btnSelectDocument = new JButton("Seleziona un documento");
        btnSelectDocument.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Seleziona un documento'");
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    chooseSignOrVerifyFileOperation(filePath);
                }
            }
        });
        btnSelectDocument.setForeground(Color.WHITE);
        btnSelectDocument.setBackground(new Color(30, 144, 255));
        btnSelectDocument.setBounds(161, 265, 209, 23);
        panelLoadFile.add(btnSelectDocument);
        panel_11 = new JPanel();
        panel_11.setBackground(SystemColor.text);
        panel_11.setBounds(0, 336, 540, 65);
        panel_24.add(panel_11);
        panel_11.setLayout(null);
        lblSFP = new JTextArea();
        lblSFP.setWrapStyleWord(true);
        lblSFP.setText("Abbiamo creato per te una firma grafica, ma se preferisci puoi personalizzarla. Questo passaggio non \u00E8 indispensabile, ma ti consentir\u00E0 di dare un tocco personale ai documenti firmati.");
        lblSFP.setRows(3);
        lblSFP.setLineWrap(true);
        lblSFP.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblSFP.setEditable(false);
        lblSFP.setBackground(SystemColor.text);
        lblSFP.setBounds(80, 1, 346, 64);
        lblSFP.setHighlighter(null);
        panel_11.add(lblSFP);
        lblCustomize = new JLabel("Personalizza");
        lblCustomize.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent arg0) {
                Font font = lblCustomize.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                lblCustomize.setFont(font.deriveFont(attributes));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                CieCard selectedCie = getSelectedCIE();
                String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
                Image signImage;

                try {
                    if (!Files.exists(Paths.get(signImagePath))) {
                        drawText(selectedCie.getCard().getName(), signImagePath);
                    }

                    signImage = ImageIO.read(new File(signImagePath));
                    ImageIcon imageIcon = new ImageIcon();
                    imageIcon.setImage(signImage.getScaledInstance(lblCustomizedGraphicSignature.getWidth(), lblCustomizedGraphicSignature.getHeight(), Image.SCALE_SMOOTH));
                    lblCustomizedGraphicSignature.setIcon(imageIcon);
                    tabbedPane.setSelectedIndex(15);

                    if (selectedCie.getCard().getIsCustomSign()) {
                        btnGenerateGraphicSignature.setEnabled(true);
                    } else {
                        btnGenerateGraphicSignature.setEnabled(false);
                    }
                } catch (IOException e1) {
                    System.out.println(e1);
                    lblCustomizedGraphicSignature.setText("Immagine firma personalizzata non trovata");
                    tabbedPane.setSelectedIndex(15);
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                Font font = lblCustomize.getFont();
                Map attributes = font.getAttributes();
                attributes.put(TextAttribute.UNDERLINE, null);
                lblCustomize.setFont(font.deriveFont(attributes));
            }
        });
        lblCustomize.setForeground(new Color(30, 144, 255));
        lblCustomize.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblCustomize.setBounds(433, 26, 95, 25);
        panel_11.add(lblCustomize);
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

                        if (!Utils.getProperty("proxyURL", "").equals("")) {
                            proxyAddress = Utils.getProperty("proxyURL", "");
                            proxyPort = Integer.parseInt(Utils.getProperty("proxyPort", ""));

                            if (!Utils.getProperty("credentials", "").equals("")) {
                                String encryptedCredentials = Utils.getProperty("credentials", "");
                                ProxyInfoManager proxyInfoManager = new ProxyInfoManager();
                                String credentials = proxyInfoManager.decrypt(encryptedCredentials);

                                if (credentials.substring(0, 5).equals("cred=")) {
                                    proxyCredentials = credentials.substring(5);
                                }
                            }
                        }

                        System.out.printf("Verifica con CIE - Url: %s, Port: %s, credentials: %s", proxyAddress, proxyPort, proxyCredentials);
                        final int ret = Middleware.INSTANCE.verificaConCIE(filePath, proxyAddress, proxyPort, proxyCredentials);

                        if (ret > 0 && ret != (long)INVALID_FILE_TYPE) {

                            VerifyTable vTable = new VerifyTable(verifyScrollPane);
                            verifyInfo vInfo = new verifyInfo();
                            verifyInfo[] vInfos = (verifyInfo[])vInfo.toArray(ret);

                            for (int i = 0; i < ret; i++) {
                                Middleware.INSTANCE.getVerifyInfo(i, vInfos[i]);
                                vInfos[i].printVerifyInfo();
                                vTable.addDataToModel(verifyScrollPane, vInfos[i]);
                            }

                            verifyScrollPane.repaint();

                            if (FilenameUtils.getExtension(filePath).equals("p7m")) {
                                btnExtractP7M.setEnabled(true);
                            } else {
                            	btnExtractP7M.setEnabled(false);
                            }

                            tabbedPane.setSelectedIndex(16);
                        }
                         else if (ret == (long)INVALID_FILE_TYPE) {
                            logger.Error("Il file selezionato non è un file valido");
                            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il file selezionato non è un file valido. E' possibile verificare solo file con estensione .p7m o .pdf", "Errore nella verifica", JOptionPane.ERROR_MESSAGE);
                            tabbedPane.setSelectedIndex(10);
                        } 
                         else if (ret == 0) {
                            logger.Info("Verifica completata");
                            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il file selezionato non contiene firme.", "Verifica completata", JOptionPane.INFORMATION_MESSAGE);
                            tabbedPane.setSelectedIndex(10);
                         }
                         else {
                            logger.Error("Errore generico durante la verifica");
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

                if (getFileExtension(filePath).equals(".pdf") && !shouldSignWithoutPairing) {
                    cbGraphicSig.setEnabled(true);
                    cbGraphicSig.setVisible(true);
                } else {
                    cbGraphicSig.setEnabled(false);
                    cbGraphicSig.setVisible(false);
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
        btnCancelOp = new JButton("Annulla");
        btnCancelOp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Annulla'");
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
        btnCancelOp.setBounds(147, 392, 136, 23);
        panel_16.add(btnCancelOp);
        btnCancelOp.setForeground(Color.WHITE);
        btnCancelOp.setBackground(new Color(30, 144, 255));
        selectSignatureOperation = new JPanel();
        selectSignatureOperation.setBackground(SystemColor.text);
        tabbedPane.addTab("New tab", null, selectSignatureOperation, null);
        selectSignatureOperation.setLayout(null);
        lblFirmaElettronica_2 = new JLabel("Firma Elettronica");
        lblFirmaElettronica_2.setHorizontalAlignment(SwingConstants.CENTER);
        lblFirmaElettronica_2.setFont(new Font("Dialog", Font.BOLD, 30));
        lblFirmaElettronica_2.setBounds(165, 45, 291, 39);
        selectSignatureOperation.add(lblFirmaElettronica_2);
        panel_23 = new JPanel();
        panel_23.setBackground(SystemColor.text);
        panel_23.setBounds(76, 132, 449, 415);
        selectSignatureOperation.add(panel_23);
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
        lblCadesSub.setText("Si appone su una qualsiasi tipologia di documento e prevede la generazione di una busta crittografica. Il documento firmato avr\u00E0 estensione .p7m");
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
                if (getFileExtension(filePath).equals(".pdf")) {
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
                logger.Info("Inizia  'Annulla'");
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
                logger.Info("Inizia  'PROSEGUI'");
                int pinLength = (shouldSignWithoutPairing) ? 8 : 4;

                for (int i = 0; i < pinLength; i++) {
                    JPasswordField field = passwordSignFields[i];
                    field.setText("");
                }

                if ((signOperation == SignOp.PADES) && cbGraphicSig.isSelected()) {
                    CieCard selectedCie = getSelectedCIE();
                    String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
                    lblPathPreview.setText(filePath);

                    if (!Files.exists(Paths.get(signImagePath))) {
                        drawText(selectedCie.getCard().getName(), signImagePath);
                    }

                    preview = new PdfPreview(panelPdfPreview, filePath, signImagePath);
                    tabbedPane.setSelectedIndex(13);
                } else {
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
                if (getFileExtension(filePath).equals(".pdf")) {
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
        selectSignatureOperation.add(lblNewLabel_8);
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
                for (int i = 0; i < passwordSignFields.length; i++) {
                    if(passwordSignFields[i] != null) {
                        JPasswordField field = passwordSignFields[i];
                        field.setText("");
                    }
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
        gbl_panelPdfPreview.columnWidths = new int[] {0};
        gbl_panelPdfPreview.rowHeights = new int[] {0};
        gbl_panelPdfPreview.columnWeights = new double[] {Double.MIN_VALUE};
        gbl_panelPdfPreview.rowWeights = new double[] {Double.MIN_VALUE};
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
        btnUndoPINTyping = new JButton("Annulla");
        btnUndoPINTyping.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (cbGraphicSig.isSelected()) {
                    tabbedPane.setSelectedIndex(13);
                } else {
                    tabbedPane.setSelectedIndex(12);
                }
            }
        });
        btnUndoPINTyping.setBounds(-12, 0, 136, 23);
        panel_28.add(btnUndoPINTyping);
        btnUndoPINTyping.setForeground(Color.WHITE);
        btnUndoPINTyping.setBackground(new Color(30, 144, 255));
        btnSignInPINTypingScreen = new JButton("FIRMA");
        btnSignInPINTypingScreen.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (btnSignInPINTypingScreen.isEnabled()) {
                    String outfilePath = null;
                    JFrame frame = new JFrame();
                    frame.setAlwaysOnTop(true);
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setAcceptAllFileFilterUsed(false);
                    String fileName = "";
                    fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                    fileChooser.setDialogTitle("Seleziona il percorso in cui salvare il file");
                    FileNameExtensionFilter filter;

                    if (signOperation == signOperation.PADES) {
                        fileName = FilenameUtils.getBaseName(filePath) + "_signed.pdf";
                        filter = new FileNameExtensionFilter("File pdf", ".pdf");
                    } else {
                        fileName = FilenameUtils.getName(filePath).replace(".p7m", "") + "_signed.p7m";
                        System.out.println("File name p7m: " + fileName);
                        filter = new FileNameExtensionFilter("File p7m", ".p7m");
                    }

                    fileChooser.setSelectedFile(new File(fileName));
                    fileChooser.addChoosableFileFilter(filter);
                    int returnVal = fileChooser.showSaveDialog(frame);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        outfilePath = fileChooser.getSelectedFile().getPath();
                        String fileExtension = filter.getExtensions()[0];

                        if (!outfilePath.endsWith(fileExtension)) {
                            outfilePath += fileExtension;
                        }

                        sign(outfilePath);
                    }
                }
            }
        });
        btnSignInPINTypingScreen.setBounds(224, 0, 136, 23);
        panel_28.add(btnSignInPINTypingScreen);
        btnSignInPINTypingScreen.setForeground(Color.WHITE);
        btnSignInPINTypingScreen.setBackground(new Color(30, 144, 255));
        btnSignCompleted = new JButton("Concludi");
        btnSignCompleted.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Concludi'");
                progressSignPIN.setVisible(false);
                lblProgressSignPIN.setVisible(true);
                
                showSigningPINInputFields();

                imgSignatureResult.setVisible(false);
                lblSignatureResult.setVisible(false);
                btnSignCompleted.setVisible(false);
                btnUndoPINTyping.setVisible(true);
                btnSignInPINTypingScreen.setVisible(true);
                selectHome();
            }
        });
        btnSignCompleted.setForeground(Color.WHITE);
        btnSignCompleted.setBackground(new Color(30, 144, 255));
        btnSignCompleted.setBounds(108, -1, 136, 23);
        btnSignCompleted.setVisible(false);
        panel_28.add(btnSignCompleted);
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
        lblProgressSignPIN = new JLabel("Inserisci le ultime 4 cifre del pin");
        lblProgressSignPIN.setBounds(153, 18, 239, 23);
        panel_29.add(lblProgressSignPIN);
        lblProgressSignPIN.setFont(new Font("Dialog", Font.BOLD, 13));
        progressSignPIN = new JProgressBar();
        progressSignPIN.setBounds(159, 53, 220, 14);
        panel_29.add(progressSignPIN);
        
        passwordField_8 = new JPasswordField();
        passwordField_8.setBounds(193, 53, 25, 25);
        passwordField_8.setVisible(false);
        panel_29.add(passwordField_8);
        passwordField_8.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_8.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_8.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_9.requestFocus();
                }
            }
        });
        
        passwordField_9 = new JPasswordField();
        passwordField_9.setBounds(230, 53, 25, 25);
        passwordField_9.setVisible(false);
        panel_29.add(passwordField_9);
        passwordField_9.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_9.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_9.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_8.setText("");
                    passwordField_8.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_10.requestFocus();
                }
            }
        });
        
        passwordField_10 = new JPasswordField();
        passwordField_10.setBounds(267, 53, 25, 25);
        passwordField_10.setVisible(false);
        panel_29.add(passwordField_10);
        passwordField_10.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_10.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_10.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_9.setText("");
                    passwordField_9.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_11.requestFocus();
                }
            }
        });
        
        passwordField_11 = new JPasswordField();
        passwordField_11.setBounds(305, 53, 25, 25);
        passwordField_11.setVisible(false);
        panel_29.add(passwordField_11);
        passwordField_11.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_11.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_11.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_10.setText("");
                    passwordField_10.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_12.requestFocus();
                }
            }
        });
        
        passwordField_12 = new JPasswordField();
        passwordField_12.setBounds(343, 53, 25, 25);
        passwordField_12.setVisible(false);
        panel_29.add(passwordField_12);
        passwordField_12.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_12.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_12.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_11.setText("");
                    passwordField_11.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_13.requestFocus();
                }
            }
        });
        
        passwordField_13 = new JPasswordField();
        passwordField_13.setBounds(381, 53, 25, 25);
        passwordField_13.setVisible(false);
        panel_29.add(passwordField_13);
        passwordField_13.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_13.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_13.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_12.setText("");
                    passwordField_12.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_14.requestFocus();
                }
            }
        });
        
        passwordField_14 = new JPasswordField();
        passwordField_14.setBounds(419, 53, 25, 25);
        passwordField_14.setVisible(false);
        panel_29.add(passwordField_14);
        passwordField_14.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_14.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_14.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_13.setText("");
                    passwordField_13.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else {
                    passwordField_15.requestFocus();
                }
            }
        });
        
        passwordField_15 = new JPasswordField();
        passwordField_15.setBounds(457, 53, 25, 25);
        passwordField_15.setVisible(false);
        panel_29.add(passwordField_15);
        passwordField_15.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField_15.setFont(new Font("Dialog", Font.BOLD, 25));
        passwordField_15.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\b') {
                    passwordField_14.setText("");
                    passwordField_14.requestFocus();
                } else if (e.getKeyChar() < '0' || e.getKeyChar() > '9') {
                    e.consume();
                } else if (passwordField_15.getText().length() > 0) {
                    e.consume();
                }
            }
        });
        
        lblSignatureResult = new JLabel("File firmato con successo");
        lblSignatureResult.setBounds(221, 64, 166, 23);
        panel_29.add(lblSignatureResult);
        lblSignatureResult.setFont(new Font("Dialog", Font.PLAIN, 13));
        imgSignatureResult = new JLabel("");
        imgSignatureResult.setBounds(172, 53, 48, 48);
        panel_29.add(imgSignatureResult);
        imgSignatureResult.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/check.png")));
        imgSignatureResult.setVisible(false);
        lblSignatureResult.setVisible(false);
        progressSignPIN.setVisible(false);
        lblNewLabel_12 = new JLabel("Appoggia la carta sul lettore");
        lblNewLabel_12.setFont(new Font("Dialog", Font.PLAIN, 17));
        lblNewLabel_12.setBounds(185, 82, 256, 39);
        firmaPin.add(lblNewLabel_12);
        customizeGraphicSignature = new JPanel();
        customizeGraphicSignature.setLayout(null);
        customizeGraphicSignature.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, customizeGraphicSignature, null);
        lblFirmaElettronica_5 = new JLabel("Firma Elettronica");
        lblFirmaElettronica_5.setHorizontalAlignment(SwingConstants.CENTER);
        lblFirmaElettronica_5.setFont(new Font("Dialog", Font.BOLD, 30));
        lblFirmaElettronica_5.setBounds(149, 45, 306, 39);
        customizeGraphicSignature.add(lblFirmaElettronica_5);
        JPanel panel_30 = new JPanel();
        panel_30.setBackground(Color.WHITE);
        panel_30.setBounds(76, 132, 449, 415);
        customizeGraphicSignature.add(panel_30);
        panel_30.setLayout(null);
        panel_31 = new JPanel();
        panel_31.setBackground(Color.WHITE);
        panel_31.setBounds(0, 392, 449, 23);
        panel_30.add(panel_31);
        panel_31.setLayout(null);
        JButton btnSelectImg = new JButton("Seleziona un file");
        btnSelectImg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Seleziona un file'");
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Png file", "png", "PNG");
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String source = selectedFile.getAbsolutePath();
                    CieCard selectedCie = getSelectedCIE();
                    String dest = getSignImagePath(selectedCie.getCard().getSerialNumber());

                    try {
                        FileUtils.copyFile(new File(source), new File(dest));
                        Image signImage = ImageIO.read(new File(dest));
                        ImageIcon imageIcon = new ImageIcon();
                        imageIcon.setImage(signImage.getScaledInstance(lblCustomizedGraphicSignature.getWidth(), lblCustomizedGraphicSignature.getHeight(), Image.SCALE_SMOOTH));
                        lblCustomizedGraphicSignature.setIcon(imageIcon);
                        lblCustomize.setText("Aggiorna");
                        lblHint.setText("Una tua firma personalizzata è già stata caricata. Vuoi aggiornarla?");
                        lblSFP.setVisible(false);
                        lblFPOK.setVisible(true);
                        selectedCie.getCard().setIsCustomSign(true);
                        cieDictionary.put(selectedCie.getCard().getPan(), selectedCie.getCard());
                        Gson gson = new Gson();
                        String serialDictionary = gson.toJson(cieDictionary);
                        Utils.setProperty("cieDictionary", serialDictionary);
                        btnGenerateGraphicSignature.setEnabled(true);
                    } catch (IOException er) {
                        logger.Error("Errore nel caricamento della firma personalizzata");
                        er.printStackTrace();
                        lblCustomizedGraphicSignature.setText("Errore nel caricamento della firma personalizzata");
                    }
                }
            }
        });
        btnSelectImg.setFont(new Font("Dialog", Font.BOLD, 11));
        btnSelectImg.setBounds(149, 0, 151, 23);
        panel_31.add(btnSelectImg);
        btnSelectImg.setForeground(Color.WHITE);
        btnSelectImg.setBackground(new Color(30, 144, 255));
        btnAnnullaOp_6 = new JButton("Indietro");
        btnAnnullaOp_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Indietro'");
                tabbedPane.setSelectedIndex(10);
            }
        });
        btnAnnullaOp_6.setBounds(0, 0, 137, 23);
        panel_31.add(btnAnnullaOp_6);
        btnAnnullaOp_6.setForeground(Color.WHITE);
        btnAnnullaOp_6.setBackground(new Color(30, 144, 255));
        btnGenerateGraphicSignature = new JButton("Crea firma");
        btnGenerateGraphicSignature.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Crea firma'");
                CieCard selectedCie = getSelectedCIE();
                String signImagePath = getSignImagePath(selectedCie.getCard().getSerialNumber());
                drawText(selectedCie.getCard().getName(), signImagePath);
                Image signImage;

                try {
                    if (!Files.exists(Paths.get(signImagePath))) {
                        drawText(selectedCie.getCard().getName(), signImagePath);
                    }

                    signImage = ImageIO.read(new File(signImagePath));
                    ImageIcon imageIcon = new ImageIcon();
                    imageIcon.setImage(signImage.getScaledInstance(lblCustomizedGraphicSignature.getWidth(), lblCustomizedGraphicSignature.getHeight(), Image.SCALE_SMOOTH));
                    lblCustomizedGraphicSignature.setIcon(imageIcon);
                    selectedCie.getCard().setIsCustomSign(false);
                    cieDictionary.put(selectedCie.getCard().getPan(), selectedCie.getCard());
                    Gson gson = new Gson();
                    String serialDictionary = gson.toJson(cieDictionary);
                    Utils.setProperty("cieDictionary", serialDictionary);
                    lblCustomize.setText("Personalizza");
                    lblHint.setText("Abbiamo creato per te una firma grafica, ma se preferisci puoi personalizzarla. "
                                    + "Questo passaggio non è indispensabile, ma ti consentirà di dare un tocco personale ai documenti firmati.");
                    lblSFP.setVisible(true);
                    lblFPOK.setVisible(false);
                } catch (IOException e1) {
                    lblCustomizedGraphicSignature.setText("Immagine firma personalizzata non trovata");
                    tabbedPane.setSelectedIndex(15);
                }

                btnGenerateGraphicSignature.setEnabled(false);
            }
        });
        btnGenerateGraphicSignature.setForeground(Color.WHITE);
        btnGenerateGraphicSignature.setBackground(new Color(30, 144, 255));
        btnGenerateGraphicSignature.setBounds(312, -1, 137, 23);
        panel_31.add(btnGenerateGraphicSignature);
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
        lblCustomizedGraphicSignature = new JLabel("");
        lblCustomizedGraphicSignature.setBounds(0, 0, 449, 93);
        panel_30.add(lblCustomizedGraphicSignature);
        pnVerify = new JPanel();
        pnVerify.setLayout(null);
        pnVerify.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, pnVerify, null);
        lblFirmaElettronica_6 = new JLabel("Firma Elettronica");
        lblFirmaElettronica_6.setHorizontalAlignment(SwingConstants.CENTER);
        lblFirmaElettronica_6.setFont(new Font("Dialog", Font.BOLD, 30));
        lblFirmaElettronica_6.setBounds(149, 45, 306, 39);
        pnVerify.add(lblFirmaElettronica_6);
        panel_32 = new JPanel();
        panel_32.setBackground(Color.WHITE);
        panel_32.setBounds(76, 132, 449, 415);
        pnVerify.add(panel_32);
        panel_32.setLayout(null);
        verifyScrollPane = new JScrollPane();
        verifyScrollPane.setBounds(26, 110, 396, 259);
        verifyScrollPane.setBorder(BorderFactory.createEmptyBorder());
        verifyScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        panel_32.add(verifyScrollPane);
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
                logger.Info("Inizia  'Concludi'");
                tabbedPane.setSelectedIndex(10);
            }
        });
        btnConcludiVerifica.setForeground(Color.WHITE);
        btnConcludiVerifica.setBackground(new Color(30, 144, 255));
        btnConcludiVerifica.setBounds(257, 392, 136, 23);
        panel_32.add(btnConcludiVerifica);
        btnExtractP7M = new JButton("Estrai");
        btnExtractP7M.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Estrai'");
                String outfilePath = null;
                JFrame frame = new JFrame();
                frame.setAlwaysOnTop(true);
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                String fileName = FilenameUtils.getBaseName(filePath).replace("_signed", "");
                fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
                fileChooser.setDialogTitle("Seleziona il percorso in cui salvare il file");
                String fileExtension = null;

                try {
                    fileExtension = FilenameUtils.getExtension(fileName);
                } catch (Exception e) {
                    fileExtension = "";
                }

                FileNameExtensionFilter filter = null;

                if (!fileExtension.isEmpty()) {
                    filter = new FileNameExtensionFilter("File " + fileExtension, fileExtension);
                    fileChooser.addChoosableFileFilter(filter);
                }

                fileChooser.setSelectedFile(new File(fileName));
                int returnVal = fileChooser.showSaveDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    outfilePath = fileChooser.getSelectedFile().getPath();
                    long ret = Middleware.INSTANCE.estraiP7m(filePath, outfilePath);

                    if (ret != 0) {
                        logger.Error("Impossibile estrarre il file");
                        JOptionPane.showMessageDialog(btnExtractP7M.getParent(), "Impossibile estrarre il file", "Estrazione completata", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        logger.Info("Estrazione completata");
                        JOptionPane.showMessageDialog(btnExtractP7M.getParent(), "File estratto correttamente", "Estrazione completata", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
        });
        btnExtractP7M.setForeground(Color.WHITE);
        btnExtractP7M.setBackground(new Color(30, 144, 255));
        btnExtractP7M.setBounds(57, 391, 136, 23);
        panel_32.add(btnExtractP7M);
        JLabel lblNewLabel_13 = new JLabel("Verifica firma elettronica");
        lblNewLabel_13.setFont(new Font("Dialog", Font.BOLD, 17));
        lblNewLabel_13.setBounds(190, 82, 246, 15);
        pnVerify.add(lblNewLabel_13);
        pnSettings = new JPanel();
        pnSettings.setBackground(Color.WHITE);
        tabbedPane.addTab("New tab", null, pnSettings, null);
        pnSettings.setLayout(null);
        JTabbedPane configTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        configTabbedPane.setBackground(Color.WHITE);
        configTabbedPane.setBounds(12, 12, 571, 500);
        pnSettings.add(configTabbedPane);
        JPanel configProxyPanel = new JPanel();
        configProxyPanel.setBackground(Color.WHITE);
        configTabbedPane.addTab("Proxy", null, configProxyPanel, null);
        configProxyPanel.setLayout(null);
        lblConfigProxyTitle = new JLabel("Configurazione server Proxy");
        lblConfigProxyTitle.setBounds(67, 5, 450, 33);
        configProxyPanel.add(lblConfigProxyTitle);
        lblConfigProxyTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfigProxyTitle.setFont(new Font("Dialog", Font.BOLD, 28));
        lblConfigProxyCaption = new JLabel("Inserisci l'indirizzo del server proxy ed eventuali credenziali");
        lblConfigProxyCaption.setBounds(53, 43, 484, 18);
        configProxyPanel.add(lblConfigProxyCaption);
        lblConfigProxyCaption.setFont(new Font("Dialog", Font.BOLD, 15));
        configProxyBodyPanel = new JPanel();
        configProxyBodyPanel.setBounds(37, 109, 500, 331);
        configProxyPanel.add(configProxyBodyPanel);
        configProxyBodyPanel.setLayout(null);
        configProxyBodyPanel.setBackground(Color.WHITE);
        JLabel lblProxyAddr = new JLabel("Indirizzo (URL o indirizzo IP)");
        lblProxyAddr.setHorizontalAlignment(SwingConstants.LEFT);
        lblProxyAddr.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblProxyAddr.setBounds(62, 95, 215, 23);
        configProxyBodyPanel.add(lblProxyAddr);
        txtProxyAddr = new JTextField();
        txtProxyAddr.setBounds(62, 124, 234, 25);
        configProxyBodyPanel.add(txtProxyAddr);
        JLabel lblUsername = new JLabel("Username");
        lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
        lblUsername.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblUsername.setBounds(62, 160, 299, 36);
        configProxyBodyPanel.add(lblUsername);
        txtUsername = new JTextField();
        txtUsername.setBounds(62, 189, 234, 25);
        configProxyBodyPanel.add(txtUsername);
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setHorizontalAlignment(SwingConstants.LEFT);
        lblPassword.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblPassword.setBounds(62, 226, 299, 36);
        configProxyBodyPanel.add(lblPassword);
        txtPassword = new JPasswordField();
        txtPassword.setBounds(62, 255, 234, 25);
        configProxyBodyPanel.add(txtPassword);
        txtPort = new JTextField();
        txtPort.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                if (!Character.isDigit(evt.getKeyChar())) {
                    evt.consume();
                }
            }
        });
        txtPort.setBounds(340, 124, 49, 25);
        configProxyBodyPanel.add(txtPort);
        JLabel lblPorta = new JLabel("Porta");
        lblPorta.setHorizontalAlignment(SwingConstants.LEFT);
        lblPorta.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblPorta.setBounds(340, 95, 58, 23);
        configProxyBodyPanel.add(lblPorta);
        chckbxShowPassword = new JCheckBox("Mostra password");
        chckbxShowPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'Mostra password'");

                if (chckbxShowPassword.isSelected()) {
                    txtPassword.setEchoChar((char)0);
                } else {
                    txtPassword.setEchoChar('*');
                }
            }
        });
        chckbxShowPassword.setFont(new Font("Dialog", Font.BOLD, 10));
        chckbxShowPassword.setBackground(Color.WHITE);
        chckbxShowPassword.setBounds(304, 256, 129, 23);
        configProxyBodyPanel.add(chckbxShowPassword);
        JPanel configLoggingPanel = new JPanel();
        configLoggingPanel.setBackground(Color.WHITE);
        configTabbedPane.addTab("Log", null, configLoggingPanel, null);
        configLoggingPanel.setLayout(null);
        JLabel lblConfigLoggingTitle = new JLabel("Configurazione livello di log");
        lblConfigLoggingTitle.setBounds(73, 5, 444, 33);
        lblConfigLoggingTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfigLoggingTitle.setFont(new Font("Dialog", Font.BOLD, 28));
        configLoggingPanel.add(lblConfigLoggingTitle);
        JLabel lblConfigLoggingCaption = new JLabel("Seleziona il livello desiderato per applicazione e libreria");
        lblConfigLoggingCaption.setBounds(69, 43, 446, 18);
        lblConfigLoggingCaption.setFont(new Font("Dialog", Font.BOLD, 15));
        configLoggingPanel.add(lblConfigLoggingCaption);
        JPanel configLoggingBodyPanel = new JPanel();
        configLoggingBodyPanel.setBounds(37, 109, 500, 331);
        configLoggingBodyPanel.setLayout(null);
        configLoggingBodyPanel.setBackground(Color.WHITE);
        configLoggingPanel.add(configLoggingBodyPanel);
        panelConfigLoggingApp = new JPanel();
        panelConfigLoggingApp.setBackground(Color.WHITE);
        panelConfigLoggingApp.setBorder(new TitledBorder(null, "Applicazione desktop", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelConfigLoggingApp.setBounds(12, 12, 238, 307);
        configLoggingBodyPanel.add(panelConfigLoggingApp);
        panelConfigLoggingApp.setLayout(new BoxLayout(panelConfigLoggingApp, BoxLayout.Y_AXIS));
        verticalGlue_5 = Box.createVerticalGlue();
        panelConfigLoggingApp.add(verticalGlue_5);
        rdbtnLoggingAppNone = new JRadioButton("None");
        buttonGroupLoggingApp.add(rdbtnLoggingAppNone);
        rdbtnLoggingAppNone.setBackground(Color.WHITE);
        rdbtnLoggingAppNone.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingApp.add(rdbtnLoggingAppNone);
        verticalGlue_6 = Box.createVerticalGlue();
        panelConfigLoggingApp.add(verticalGlue_6);
        rdbtnLoggingAppDebug = new JRadioButton("Debug");
        buttonGroupLoggingApp.add(rdbtnLoggingAppDebug);
        rdbtnLoggingAppDebug.setBackground(Color.WHITE);
        rdbtnLoggingAppDebug.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingApp.add(rdbtnLoggingAppDebug);
        verticalGlue_7 = Box.createVerticalGlue();
        panelConfigLoggingApp.add(verticalGlue_7);
        rdbtnLoggingAppInfo = new JRadioButton("Info");
        buttonGroupLoggingApp.add(rdbtnLoggingAppInfo);
        rdbtnLoggingAppInfo.setBackground(Color.WHITE);
        rdbtnLoggingAppInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingApp.add(rdbtnLoggingAppInfo);
        verticalGlue_8 = Box.createVerticalGlue();
        panelConfigLoggingApp.add(verticalGlue_8);
        rdbtnLoggingAppError = new JRadioButton("Error");
        buttonGroupLoggingApp.add(rdbtnLoggingAppError);
        rdbtnLoggingAppError.setBackground(Color.WHITE);
        rdbtnLoggingAppError.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingApp.add(rdbtnLoggingAppError);
        verticalGlue_9 = Box.createVerticalGlue();
        panelConfigLoggingApp.add(verticalGlue_9);
        panelConfigLoggingLib = new JPanel();
        panelConfigLoggingLib.setBackground(Color.WHITE);
        panelConfigLoggingLib.setBorder(new TitledBorder(null, "Libreria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelConfigLoggingLib.setBounds(252, 12, 238, 307);
        configLoggingBodyPanel.add(panelConfigLoggingLib);
        panelConfigLoggingLib.setLayout(new BoxLayout(panelConfigLoggingLib, BoxLayout.Y_AXIS));
        verticalGlue = Box.createVerticalGlue();
        panelConfigLoggingLib.add(verticalGlue);
        rdbtnLoggingLibNone = new JRadioButton("None");
        buttonGroupLoggingLib.add(rdbtnLoggingLibNone);
        rdbtnLoggingLibNone.setBackground(Color.WHITE);
        rdbtnLoggingLibNone.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingLib.add(rdbtnLoggingLibNone);
        verticalGlue_1 = Box.createVerticalGlue();
        panelConfigLoggingLib.add(verticalGlue_1);
        rdbtnLoggingLibDebug = new JRadioButton("Debug");
        buttonGroupLoggingLib.add(rdbtnLoggingLibDebug);
        rdbtnLoggingLibDebug.setBackground(Color.WHITE);
        rdbtnLoggingLibDebug.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingLib.add(rdbtnLoggingLibDebug);
        verticalGlue_2 = Box.createVerticalGlue();
        panelConfigLoggingLib.add(verticalGlue_2);
        rdbtnLoggingLibInfo = new JRadioButton("Info");
        buttonGroupLoggingLib.add(rdbtnLoggingLibInfo);
        rdbtnLoggingLibInfo.setBackground(Color.WHITE);
        rdbtnLoggingLibInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingLib.add(rdbtnLoggingLibInfo);
        verticalGlue_3 = Box.createVerticalGlue();
        panelConfigLoggingLib.add(verticalGlue_3);
        rdbtnLoggingLibError = new JRadioButton("Error");
        buttonGroupLoggingLib.add(rdbtnLoggingLibError);
        rdbtnLoggingLibError.setBackground(Color.WHITE);
        rdbtnLoggingLibError.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelConfigLoggingLib.add(rdbtnLoggingLibError);
        verticalGlue_4 = Box.createVerticalGlue();
        panelConfigLoggingLib.add(verticalGlue_4);
        
        btnDeleteLogs = new JButton("Elimina cache dei log");
        btnDeleteLogs.setForeground(Color.WHITE);
        btnDeleteLogs.setBackground(new Color(30, 144, 255));
        btnDeleteLogs.setBounds(292, 436, 236, 25);
        btnDeleteLogs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		logger.Info("Inizia 'deleteLogs'");
        		deleteLogs();
        	}
        });
        configLoggingPanel.add(btnDeleteLogs);
        
        btnCollectLogs = new JButton("Raccogli log per diagnostica");
        btnCollectLogs.setForeground(Color.WHITE);
        btnCollectLogs.setBackground(new Color(30, 144, 255));
        btnCollectLogs.setBounds(47, 436, 236, 25);
        btnCollectLogs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logger.Info("Inizia  'CollectLogs'");
                collectLogs();
            }
        });
        configLoggingPanel.add(btnCollectLogs);
        
        configPreferencesPanel = new JPanel();
        configTabbedPane.addTab("Preferenze", null, configPreferencesPanel, null);
        configPreferencesPanel.setLayout(null);
        
        lblConfigPreferencesTitle = new JLabel("Configurazione preferenze");
        lblConfigPreferencesTitle.setBounds(72, 5, 421, 33);
        lblConfigPreferencesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblConfigPreferencesTitle.setFont(new Font("Dialog", Font.BOLD, 28));
        configPreferencesPanel.add(lblConfigPreferencesTitle);
        
        lblConfigPreferencesCaption = new JLabel("Le opzioni di seguito riportate consentono di personalizzare");
        lblConfigPreferencesCaption.setBounds(40, 43, 486, 18);
        lblConfigPreferencesCaption.setFont(new Font("Dialog", Font.BOLD, 15));
        configPreferencesPanel.add(lblConfigPreferencesCaption);
        
        lblConfigPreferencesCaption_1 = new JLabel("il comportamento di CIE ID secondo le proprie preferenze");
        lblConfigPreferencesCaption_1.setBounds(45, 66, 475, 18);
        lblConfigPreferencesCaption_1.setFont(new Font("Dialog", Font.BOLD, 15));
        configPreferencesPanel.add(lblConfigPreferencesCaption_1);
        
        cboxShowTutorial = new JCheckBox("Mostra schermate introduttive all'avvio di CIE ID");
        cboxShowTutorial.setSize(363, 23);
        cboxShowTutorial.setLocation(new Point(94, 148));
        configPreferencesPanel.add(cboxShowTutorial);
        cboxShowTutorial.setActionCommand("");
        configButtonsPanel = new JPanel();
        configButtonsPanel.setBackground(Color.WHITE);
        configButtonsPanel.setBounds(0, 524, 595, 47);
        pnSettings.add(configButtonsPanel);
        btnSave = new JButton("Salva");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Salva'");
                LogLevel logLevelAppValue = Logger.defaultLogLevel;

                if (rdbtnLoggingAppNone.isSelected()) {
                    logConfig.app = LogLevel.NONE;
                    logger.setLevel(LogLevel.NONE);
                } else if (rdbtnLoggingAppDebug.isSelected()) {
                    logConfig.app = LogLevel.DEBUG;
                    logger.setLevel(LogLevel.DEBUG);
                } else if (rdbtnLoggingAppInfo.isSelected()) {
                    logConfig.app = LogLevel.INFO;
                    logger.setLevel(LogLevel.INFO);
                } else if (rdbtnLoggingAppError.isSelected()) {
                    logConfig.app = LogLevel.ERROR;
                    logger.setLevel(LogLevel.ERROR);
                }

                if (rdbtnLoggingLibNone.isSelected()) {
                    logConfig.lib = LogLevel.NONE;
                } else if (rdbtnLoggingLibDebug.isSelected()) {
                    logConfig.lib = LogLevel.DEBUG;
                } else if (rdbtnLoggingLibInfo.isSelected()) {
                    logConfig.lib = LogLevel.INFO;
                } else if (rdbtnLoggingLibError.isSelected()) {
                    logConfig.lib = LogLevel.ERROR;
                }

                saveLogConfigToFile();

                if ((txtUsername.getText().equals("") && !txtPassword.getText().equals("")) || (!txtUsername.getText().equals("") && txtPassword.getText().equals(""))) {
                    JOptionPane.showMessageDialog(btnSave.getParent(), "Campo username o password mancante", "Credenziali proxy mancanti", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ((txtPort.getText().equals("") && !txtProxyAddr.getText().equals("")) || (!txtPort.getText().equals("") && txtProxyAddr.getText().equals("")) ) {
                    JOptionPane.showMessageDialog(btnSave.getParent(), "Indirizzo o porta del proxy mancante", "Informazione proxy mancanti", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (txtUsername.getText().equals("")) {
                    Utils.setProperty("credentials", "");
                } else {
                    String credentials = String.format("cred=%s:%s", txtUsername.getText(), txtPassword.getText());
                    ProxyInfoManager proxyInfoManager = new ProxyInfoManager();
                    String encryptedCredentials = proxyInfoManager.encrypt(credentials);
                    Utils.setProperty("credentials", encryptedCredentials);
                }

                Utils.setProperty("proxyURL", txtProxyAddr.getText());

                if (txtPort.getText() == "") {
                    Utils.setProperty("proxyPort", String.valueOf(0));
                } else {
                    Utils.setProperty("proxyPort", txtPort.getText());
                }
                
				Utils.setProperty("nomore", cboxShowTutorial.isSelected() ? "false" : "true");
                disableConfigurationPaneControls();
            }
        });
        btnSave.setForeground(Color.WHITE);
        btnSave.setBackground(new Color(30, 144, 255));
        configButtonsPanel.add(btnSave);
        btnChangeProxy = new JButton("Modifica");
        btnChangeProxy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                logger.Info("Inizia  'Modifica'");
                enableConfigurationPaneControls();
            }
        });
        btnChangeProxy.setForeground(Color.WHITE);
        btnChangeProxy.setBackground(new Color(30, 144, 255));
        configButtonsPanel.add(btnChangeProxy);

        if (args.length > 0 && args[0].equals("unlock")) {
            selectUnlock();
        } else {
            selectHome();
        }
        
        System.out.println("tabbedPanel: " + tabbedPane);
    }
    
    private void collectLogs() {
    	boolean logFound = false;
    	JFileChooser fileChooser = new JFileChooser();
    	
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle("Seleziona il percorso in cui salvare l'archivio dei log");
        FileNameExtensionFilter filter;
        filter = new FileNameExtensionFilter("Archivio zip", ".zip");
        fileChooser.setFileFilter(filter);
        fileChooser.setSelectedFile(new File(System.getProperty("user.home") + "/CIEIDLog_" +
        new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime()) + ".zip"));
    	
    	if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
    	    File zipFile = fileChooser.getSelectedFile();
    	    ZipOutputStream logZipOutputStream;
	  		try {
	  			logZipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
	  			File folder = new File(System.getProperty("user.home"), ".CIEPKI");
	  			for (final File fileEntry : folder.listFiles()) {
	  		            if (fileEntry.getName().endsWith(".log")) {
	  		            	logFound = true;
		  			    	ZipEntry entry = new ZipEntry(fileEntry.getName());
		  			    	logZipOutputStream.putNextEntry(entry);
		  					
		  			    	byte[] data = Files.readAllBytes(fileEntry.toPath());
		  			    	logZipOutputStream.write(data, 0, data.length);
		  			    	logZipOutputStream.closeEntry();
	  		            }
	  		        }
	
	  			logZipOutputStream.close();
	  			
	  			if(logFound) {
	  				logger.Info("[INFO] collectLogs() - Archivio dei log creato con successo al path: " + zipFile.getAbsolutePath());
	  				JOptionPane.showMessageDialog(this.getContentPane(), "La raccolta dei log di diagnostica è avvenuta con successo.\n" + 
	  						"Puoi adesso condividere con gli sviluppatori l'archivio\ngenerato per un'analisi della problematica riscontrata.",
	  						"Raccolta completata", JOptionPane.INFORMATION_MESSAGE);
	  			}
	  			
	  			else {
	  				JOptionPane.showMessageDialog(this.getContentPane(), "Non sono presenti log. Effettua prima delle operazioni con l'applicativo, " + 
	  						"quindi ripeti l'operazione di raccolta dei log.", "Raccolta log", JOptionPane.INFORMATION_MESSAGE);
	  				
	  				Files.deleteIfExists(zipFile.toPath());
	  			}
	  		} catch (IOException ex) {
	  			logger.Error("[ERROR] collectLogs() - Si è verificato un errore durante l'operazione:\n\n" + ex.getLocalizedMessage());
	            JOptionPane.showMessageDialog(this.getContentPane(), "Si è verificato un errore durante l'operazione:\n\n" + ex.getLocalizedMessage(),
	            		"Attenzione", JOptionPane.ERROR_MESSAGE);
	  		}
    	}
    }
    
    private void deleteLogs() {
        if (JOptionPane.showConfirmDialog(this.getContentPane(), "Avanzando con l'operazione, verranno eliminati tutti i file di log\n" + 
        		"generati dal software CIE ID. Confermi di voler procedere?", "Eliminazione log", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
    	
    	try {
  			File folder = new File(System.getProperty("user.home"), ".CIEPKI");
  			for (final File fileEntry : folder.listFiles()) {
  		            if (fileEntry.getName().endsWith(".log")) {
  		            	Files.deleteIfExists(fileEntry.toPath());
  		            }
  		        }

			logger.Info("[INFO] deleteLogs() - Log eliminati con successo.");
			JOptionPane.showMessageDialog(this.getContentPane(), "L'eliminazione dei log è avvenuta con successo. Se hai riscontrato un'anomalia nel software\n" + 
					"che intendi segnalare, puoi impostare il livello di logging su 'Debug', replicare l'operazione,\n" + 
					"raccogliere i log con l'apposito pulsante e condividerli con lo sviluppatore.",
					"Eliminazione completata", JOptionPane.ERROR_MESSAGE);

  		} catch (Exception ex) {
  			logger.Error("[ERROR] deleteLogs() - Si è verificato un errore durante l'operazione:" + ex.getLocalizedMessage());
            JOptionPane.showMessageDialog(this.getContentPane(), "Si è verificato un errore durante la cancellazione dei log.\n" + 
            		"È possibile che alcuni file siano aperti ed in uso da terze parti, per cui non\n" + 
            		" è stato possibile procedere con l'eliminazione. Motivazione:\n\n" + ex.getLocalizedMessage(),
            		"Attenzione", JOptionPane.ERROR_MESSAGE);
  		}
    }

    private void chooseSignOrVerifyFileOperation(String filePath) {
        logger.Info("Inizia  'filesDropped'");
        logger.Info(String.format("file path: %s", filePath));
        lblPathOp.setText(filePath);
        MouseEvent event = new MouseEvent(panel_12, MouseEvent.MOUSE_CLICKED,
                                          0, 0, 100, 100, 1, false);

        if (signOperation == SignOp.VERIFY) {
            logger.Info("Switch to verify operation");
            panel_14.getMouseListeners()[0].mouseClicked(event);
        } else {
            logger.Info("Switch to sign operation");
            panel_13.getMouseListeners()[0].mouseClicked(event);
        }
    }

    private void enableConfigurationPaneControls() {
        logger.Info("Inizia - enableConfigurationPaneControls()");
        setConfigurationPaneControlsState(true);
    }

    private void disableConfigurationPaneControls() {
        logger.Info("Inizia - disableConfigurationPaneControls()");
        setConfigurationPaneControlsState(false);
    }

    private void setConfigurationPaneControlsState(boolean value) {
        txtProxyAddr.setEnabled(value);
        txtUsername.setEnabled(value);
        txtPassword.setEnabled(value);
        txtPort.setEnabled(value);
        chckbxShowPassword.setEnabled(value);
        chckbxShowPassword.setSelected(false);
        btnSave.setEnabled(value);
        btnChangeProxy.setEnabled(!value);
        panelConfigLoggingApp.setEnabled(value);
        rdbtnLoggingAppNone.setEnabled(value);
        rdbtnLoggingAppDebug.setEnabled(value);
        rdbtnLoggingAppInfo.setEnabled(value);
        rdbtnLoggingAppError.setEnabled(value);
        panelConfigLoggingLib.setEnabled(value);
        rdbtnLoggingLibNone.setEnabled(value);
        rdbtnLoggingLibDebug.setEnabled(value);
        rdbtnLoggingLibInfo.setEnabled(value);
        rdbtnLoggingLibError.setEnabled(value);
        cboxShowTutorial.setEnabled(value);
    }

    private void selectButton(JButton button) {
        btnChangePIN.setBackground(SystemColor.control);
        btnHelp.setBackground(SystemColor.control);
        btnInformation.setBackground(SystemColor.control);
        btnHome.setBackground(SystemColor.control);
        btnUnlockCard.setBackground(SystemColor.control);
        btnTutorial.setBackground(SystemColor.control);
        btnDigitalSignature.setBackground(SystemColor.control);
        btnDigitalSignatureVerify.setBackground(SystemColor.control);
        btnSettings.setBackground(SystemColor.control);
        button.setBackground(SystemColor.LIGHT_GRAY);
    }

    private CieCard getSelectedCIE() {
        return cieCarousel.cieCenter;
    }

    private String getSignImagePath(String serialNumber) {
        String home = System.getProperty("user.home");
        String signPath = home + "/.CIEPKI/" + serialNumber + "_default.png";
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

    private void drawText(String text, String path) {
        BufferedImage bufferedImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();

        try {
            text = toFirstCharUpperAll(toTitleCase(text).toLowerCase());

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
                logger.Error(String.format("File '%s' non trovato", file));
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
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sign(String outFilePath) {
        logger.Info("Inizia - sign()");
        String pin = "";
        int pinLength = (shouldSignWithoutPairing) ? 8 : 4;

        for (int i = 0; i < pinLength; i++) {
            JPasswordField field = passwordSignFields[i];
            pin += field.getText();
        }

        if (pin.length() != pinLength) {
            logger.Error("Il PIN deve essere composto " + ((shouldSignWithoutPairing) ? "da 8" : "dalle ultime 4") + " cifre - PIN non corretto");
            JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto " + ((shouldSignWithoutPairing) ? "da 8" : "dalle ultime 4") + " cifre", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char c = pin.charAt(0);

        for (int i = 1; i < pin.length() && (c >= '0' && c <= '9'); i++) {
            c = pin.charAt(i);
        }

        if (pinLength > pin.length() || !(c >= '0' && c <= '9')) {
            logger.Error("Il PIN deve essere composto " + ((shouldSignWithoutPairing) ? "da 8" : "dalle ultime 4") + " cifre - PIN non corretto");
            JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto " + ((shouldSignWithoutPairing) ? "da 8" : "dalle ultime 4") + " cifre", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        btnUndoPINTyping.setEnabled(false);
        btnSignInPINTypingScreen.setEnabled(false);

        for (int i = 0; i < pinLength; i++) {
            JPasswordField field = passwordSignFields[i];
            field.setVisible(false);
        }

        progressSignPIN.setVisible(true);
        lblProgressSignPIN.setText("Firma in corso...");
        final String pinfin = pin;
        
        if(shouldSignWithoutPairing) 
        {
            final int attempts[] = new int[1];

            Middleware.ProgressCallBack progressCallBack = new Middleware.ProgressCallBack() {
                @Override
                public void invoke(final int progress, final String message) {

                }
            };

            Middleware.CompletedCallBack completedCallback = new Middleware.CompletedCallBack() {
                @Override
                public void invoke(String pan, String cardholder, String ef_seriale) {
                    String signPath = getSignImagePath(ef_seriale);
                    drawText(cardholder, signPath);
                    System.out.println("Pan: " + pan + "ef seriale " +  ef_seriale);
                    Cie newCie = new Cie(pan, cardholder, ef_seriale);
                    cieDictionary.put(pan, newCie);
                    signingCIEPAN = pan;
                }
            };

            final int ret = Middleware.INSTANCE.AbilitaCIE(null, pinfin, attempts, progressCallBack, completedCallback);
            
            if(ret == CARD_ALREADY_ENABLED) {
                logger.Error("La CIE risulta già associata - Errore durante la firma");
                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "La CIE risulta essere già stata associata precedentemente,\nper cui l'operazione di firma è stata annullata.\nRipetere il procedimento, selezionando la CIE\ndal selettore presente in 'Firma Elettronica'.", "Carta già associata", JOptionPane.ERROR_MESSAGE);
                shouldSignWithoutPairing = false;
                selectHome();
                return;
            }

            else if(ret == CKR_PIN_INCORRECT) {
                logger.Error("Il PIN inserito è errato. - Errore durante la firma");
                JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN inserito è errato.", "Errore durante la firma", JOptionPane.ERROR_MESSAGE);
                shouldSignWithoutPairing = false;
                selectHome();
                return;
            }

            else if(ret == CKR_PIN_LOCKED) {
                logger.Error("Il PIN della CIE è bloccato - Errore durante la firma");
                JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN della CIE è bloccato. Procedi con lo sblocco prima di procedere.", "Errore durante la firma", JOptionPane.ERROR_MESSAGE);
                shouldSignWithoutPairing = false;
                selectHome();
                return;
            }

            else if(ret != CKR_OK) {
                logger.Error("Si è verificato un errore durante la lettura dei dati della CIE - Errore durante la firma");
                JOptionPane.showMessageDialog(this.getContentPane(), "Si è verificato un errore durante la lettura dei dati della CIE.", "Errore durante la firma", JOptionPane.ERROR_MESSAGE);
                shouldSignWithoutPairing = false;
                selectHome();
                return;
            }
        }
                
        Runner.run(new Runnable() {
            @Override
            public void run() {
                Cie selCIE = ((shouldSignWithoutPairing) ? cieDictionary.get(signingCIEPAN) : getSelectedCIE().getCard());
                signMWCall(outFilePath, (shouldSignWithoutPairing) ? pinfin.substring(4) : pinfin, selCIE);
            }
        });
    }

    private void signMWCall(String outFilePath, String pinfin, Cie selectedCIE) {
        try {
            final int[] attempts = new int[1];
            Middleware.ProgressCallBack progressCallBack = new Middleware.ProgressCallBack() {
                @Override
                public void invoke(final int progress, final String message) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                progressSignPIN.setValue(progress);
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
                    System.out.println("Sign Completed!!");
                    logger.Info("Firma completata");

                    if (retValue == 0) {
                        logger.Info("File firmato con successo");
                        lblSignatureResult.setText("File firmato con successo");
                        imgSignatureResult.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/check.png")));
                    } else {
                        logger.Error("Si è verificato un errore durante la firma");
                        lblSignatureResult.setText("Si è verificato un errore durante la firma");
                        imgSignatureResult.setIcon(new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/cross.png")));
                    }

                    lblSignatureResult.setVisible(true);
                    imgSignatureResult.setVisible(true);
                    progressSignPIN.setVisible(false);
                    btnSignCompleted.setVisible(true);
                    btnUndoPINTyping.setVisible(false);
                    btnSignInPINTypingScreen.setVisible(false);
                    lblProgressSignPIN.setVisible(false);

                    if(shouldSignWithoutPairing) {
                        removeCIE(signingCIEPAN, "");
                        signingCIEPAN = "";

                        shouldSignWithoutPairing = false;
                    }
                }   
            };

            final int ret;

            if (signOperation == SignOp.PADES) {
                if (cbGraphicSig.isSelected()) {
                    float infos[] = preview.signImageInfos();
                    String signImagePath = getSignImagePath(selectedCIE.getSerialNumber());
                    int pageNumber = preview.getSelectedPage();
                    String pan = selectedCIE.getPan();
                    ret = Middleware.INSTANCE.firmaConCIE(filePath, "pdf", pinfin, pan, pageNumber, infos[0], infos[1], infos[2], infos[3],
                                                            signImagePath, outFilePath, progressCallBack, signCompletedCallBack);
                } else {
                    ret = Middleware.INSTANCE.firmaConCIE(filePath, "pdf", pinfin, selectedCIE.getPan(), 0, 0, 0, 0, 0, null, outFilePath, progressCallBack, signCompletedCallBack);
                }
            } else {
                ret = Middleware.INSTANCE.firmaConCIE(filePath, "p7m", pinfin, selectedCIE.getPan(), 0, 0, 0, 0, 0, null, outFilePath, progressCallBack, signCompletedCallBack);
            }

            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    try {
                        btnUndoPINTyping.setEnabled(true);
                        btnSignInPINTypingScreen.setEnabled(true);

                        switch (ret) {
                            case CKR_TOKEN_NOT_RECOGNIZED:
                                logger.Error("Abilitazione CIE - CIE non presente sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
                                showSigningPINInputFields();
                                break;

                            case CKR_TOKEN_NOT_PRESENT:
                                logger.Error("Abilitazione CIE - CIE non presente sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
                                showSigningPINInputFields();
                                break;

                            case CKR_PIN_INCORRECT:
                                logger.Error(String.format("Il PIN digitato è errato. Rimangono %d tentativi", attempts[0]));
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato. Rimangono %d tentativi", attempts[0]), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
                                showSigningPINInputFields();
                                break;

                            case CKR_PIN_LOCKED:
                                logger.Error("Carta bloccata");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Munisciti del codice PUK e utilizza la funzione di sblocco carta per abilitarla", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
                                showSigningPINInputFields();
                                break;

                            case CKR_GENERAL_ERROR:
                                logger.Error("Errore inaspettato durante la comunicazione con la smart card");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
                                showSigningPINInputFields();
                                break;

                            case CARD_PAN_MISMATCH:
                                logger.Error("CIE selezionata diversa da quella presente sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE selezionata diversa da quella presente sul lettore", "CIE non corrispondente", JOptionPane.ERROR_MESSAGE);
                                showSigningPINInputFields();
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showSigningPINInputFields() {
        logger.Info("Inizia - showSigningPINInputFields()");
        progressSignPIN.setVisible(false);
        int PINStartPos = 197, spacing = 32, index = 1;
        
        if(shouldSignWithoutPairing) {
            passwordSignFields[0] = passwordField_8;
            passwordSignFields[0].setLocation(PINStartPos - spacing, passwordField_8.getLocation().y);
            passwordSignFields[1] = passwordField_9;
            passwordSignFields[1].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_9.getLocation().y);
            passwordSignFields[2] = passwordField_10;
            passwordSignFields[2].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_10.getLocation().y);
            passwordSignFields[3] = passwordField_11;
            passwordSignFields[3].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_11.getLocation().y);

            passwordSignFields[4] = passwordField_12;
            passwordSignFields[4].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_12.getLocation().y);
            passwordSignFields[5] = passwordField_13;
            passwordSignFields[5].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_13.getLocation().y);
            passwordSignFields[6] = passwordField_14;
            passwordSignFields[6].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_14.getLocation().y);
            passwordSignFields[7] = passwordField_15;
            passwordSignFields[7].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_15.getLocation().y);
        }
        else {
            passwordSignFields[0] = passwordField_8;
            passwordSignFields[0].setLocation(PINStartPos, passwordField_8.getLocation().y);
            passwordSignFields[1] = passwordField_9;
            passwordSignFields[1].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_9.getLocation().y);
            passwordSignFields[2] = passwordField_10;
            passwordSignFields[2].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_10.getLocation().y);
            passwordSignFields[3] = passwordField_11;
            passwordSignFields[3].setLocation(passwordSignFields[0].getLocation().x + (index++ * spacing), passwordField_11.getLocation().y);
        }
        
        lblProgressSignPIN.setVisible(true);
        lblProgressSignPIN.setText("Inserisci le " + ((shouldSignWithoutPairing) ? "8" : "ultime 4") + " cifre del PIN");

        for (int i = 0; i < passwordSignFields.length; i++) {
            if(passwordSignFields[i] != null) {
                JPasswordField field = passwordSignFields[i];
                if(!shouldSignWithoutPairing && i > 3)
                    field.setVisible(false);
                else
                    field.setVisible(true);
            }
        }
    }

    private void pairCIE() {
        logger.Info("Inizia - pairCIE()");
        String pin = "";
        int i;

        for (i = 0; i < passwordFields.length; i++) {
            JPasswordField field = passwordFields[i];
            pin += field.getText();
        }

        if (pin.length() != 8) {
            logger.Error("PIN non corretto");
            JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char c = pin.charAt(0);
        i = 1;

        for (i = 1; i < pin.length() && (c >= '0' && c <= '9'); i++) {
            c = pin.charAt(i);
        }

        if (i < pin.length() || !(c >= '0' && c <= '9')) {
            logger.Error("PIN non corretto");
            JOptionPane.showMessageDialog(this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (i = 0; i < passwordFields.length; i++) {
            JPasswordField field = passwordFields[i];
            field.setText("");
        }

        btnPair.setEnabled(false);
        tabbedPane.setSelectedIndex(1);
        final String pinfin = pin;
        
        Runner.run(new Runnable() {
            @Override
            public void run() {
                pairCIEMWCall(pinfin);
            }
        });
    }

    private void pairCIEMWCall(String PIN) {
        try {
            final int[] attempts = new int[1];
            Middleware.ProgressCallBack progressCallBack = new Middleware.ProgressCallBack() {
                @Override
                public void invoke(final int progress, final String message) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                if(!shouldSignWithoutPairing) {
                                    progressBar.setValue(progress);
                                    lblProgress.setText(message);
                                }
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
                    String signPath = getSignImagePath(ef_seriale);
                    drawText(cardholder, signPath);
                    System.out.println("Pan: " + pan + "ef seriale " +  ef_seriale);
                    Cie newCie = new Cie(pan, cardholder, ef_seriale);
                    cieDictionary.put(pan, newCie);
                    signingCIEPAN = pan;
                }
            };
            final int ret = Middleware.INSTANCE.AbilitaCIE(null, PIN, attempts, progressCallBack, completedCallBack);
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                if(!shouldSignWithoutPairing)
                                    btnPair.setEnabled(true);

                                switch (ret) {
                                    case CKR_TOKEN_NOT_RECOGNIZED:
                                        logger.Error("CIE non presente sul lettore");
                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
                                        selectHome();
                                        break;

                                    case CKR_TOKEN_NOT_PRESENT:
                                        logger.Error("CIE non presente sul lettore");
                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Abilitazione CIE", JOptionPane.ERROR_MESSAGE);
                                        selectHome();
                                        break;

                                    case CKR_PIN_INCORRECT:
                                        logger.Error("PIN non corretto");
                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato."), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
                                        selectHome();
                                        break;

                                    case CKR_PIN_LOCKED:
                                        logger.Error("Carta bloccata");
                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Munisciti del codice PUK e utilizza la funzione di sblocco carta per abilitarla", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
                                        selectHome();
                                        break;

                                    case CKR_GENERAL_ERROR:
                                        logger.Error("Errore inaspettato durante la comunicazione con la smart card");
                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
                                        selectHome();
                                        break;

                                    case CKR_OK:
                                        logger.Info("CIE abilitata con successo");

                                        Gson gson = new Gson();
                                        String serialDictionary = gson.toJson(cieDictionary);
                                        Utils.setProperty("cieDictionary", serialDictionary);
                                        cieCarousel.configureCards(cieDictionary);

                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "L'abilitazione della CIE è avvenuta con successo", "CIE abilitata", JOptionPane.INFORMATION_MESSAGE);
                                        selectCardholder();
                                        break;

                                    case CARD_ALREADY_ENABLED:
                                        logger.Error("Carta già abilitata, abbinamento impossibile");
                                        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Carta già abilitata", "Impossibile abbinare la carta", JOptionPane.ERROR_MESSAGE);
                                        selectHome();
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }});
        } catch(Exception ex) {
            ex.printStackTrace();
        }                       
    }

    private void changePIN() {
        logger.Info("Inizia - changePIN()");
        final String pin = oldPIN.getText();
        final String pin1 = newPIN.getText();
        final String pin2 = repeatNewPIN.getText();
        int i;

        if (pin.length() != 8) {
            logger.Error("PIN non corretto");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pin1.length() != 8) {
            logger.Error("PIN non corretto");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char c = pin.charAt(0);
        i = 1;

        for (i = 1; i < pin.length() && (c >= '0' && c <= '9'); i++) {
            c = pin.charAt(i);
        }

        if (i < pin.length() || !(c >= '0' && c <= '9')) {
            logger.Error("PIN non corretto");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        i = 1;

        for (i = 1; i < pin1.length() && (c >= '0' && c <= '9'); i++) {
            c = pin1.charAt(i);
        }

        if (i < pin1.length() || !(c >= '0' && c <= '9')) {
            logger.Error("PIN non corretto");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pin1.equals(pin2)) {
            logger.Error("PIN non corrispondenti");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "I PIN non corrispondono", "PIN non corrispondenti", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        char lastchar = c;

        for (i = 1; i < pin1.length() && c == lastchar; i++) {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar) {
            logger.Error("PIN non valido - cifre tutte uguali");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre uguali", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)((int)c - 1);

        for (i = 1; i < pin1.length() && c == lastchar + 1; i++) {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar + 1) {
            logger.Error("PIN non corretto - cifre consecutive");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)(c + 1);

        for (i = 1; i < pin1.length() && c == lastchar - 1; i++) {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar - 1) {
            logger.Error("PIN non corretto - cifre consecutive");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        oldPIN.setText("");
        newPIN.setText("");
        repeatNewPIN.setText("");
        btnPerformChangePIN.setEnabled(false);
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
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        btnPerformChangePIN.setEnabled(true);

                        switch (ret) {
                            case CKR_TOKEN_NOT_RECOGNIZED:
                                logger.Error("CIE non riconosciuta sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Cambio PIN", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(3);
                                break;

                            case CKR_TOKEN_NOT_PRESENT:
                                logger.Error("CIE non presente sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Cambio PIN", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(3);
                                break;

                            case CKR_PIN_INCORRECT:
                                logger.Error(String.format("Il PIN digitato è errato. rimangono %d tentativi", attempts[0]));
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PIN digitato è errato. Rimangono %d tentativi", attempts[0]), "PIN non corretto", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(3);
                                break;

                            case CKR_PIN_LOCKED:
                                logger.Error("Carta bloccata");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Munisciti del codice PUK e utilizza la funzione di sblocco carta per abilitarla", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(5);
                                break;

                            case CKR_GENERAL_ERROR:
                                logger.Error("Errore inaspettato durante la comunicazione con la smart card");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(3);
                                break;

                            case CKR_OK:
                                logger.Info("Il PIN è stato modificato con successo");
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

    private void unlockPIN() {
        logger.Info("Inizia - unlockPIN()");
        final String puk = puk01.getText();
        final String pin1 = pin01.getText();
        final String pin2 = pin02.getText();
        int i;

        if (puk.length() != 8) {
            logger.Error("PUK non corretto - deve essere composto da 8 numeri");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PUK deve essere composto da 8 numeri", "PUK non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (pin1.length() != 8) {
            logger.Error("PIN non corretto - deve essere composto da 8 numeri");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char c = puk.charAt(0);
        i = 1;

        for (i = 1; i < puk.length() && (c >= '0' && c <= '9'); i++) {
            c = puk.charAt(i);
        }

        if (i < puk.length() || !(c >= '0' && c <= '9')) {
            logger.Error("PUK non corretto - deve essere composto da 8 numeri");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PUK deve essere composto da 8 numeri", "PUK non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        i = 1;

        for (i = 1; i < pin1.length() && (c >= '0' && c <= '9'); i++) {
            c = pin1.charAt(i);
        }

        if (i < pin1.length() || !(c >= '0' && c <= '9')) {
            logger.Error("PIN non corretto - deve essere composto da 8 numeri");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il PIN deve essere composto da 8 numeri", "PIN non corretto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pin1.equals(pin2)) {
            logger.Error("PIN non corrispondenti");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "I PIN non corrispondono", "PIN non corrispondenti", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        char lastchar = c;

        for (i = 1; i < pin1.length() && c == lastchar; i++) {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar) {
            logger.Error("PIN non valido - cifre tutte uguali");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre uguali", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)((int)c - 1);

        for (i = 1; i < pin1.length() && c == lastchar + 1; i++) {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar + 1) {
            logger.Error("PIN non valido - cifre consecutive");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        c = pin1.charAt(0);
        lastchar = (char)(c + 1);

        for (i = 1; i < pin1.length() && c == lastchar - 1; i++) {
            lastchar = c;
            c = pin1.charAt(i);
        }

        if (c == lastchar - 1) {
            logger.Error("PIN non valido - cifre consecutive");
            JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Il nuovo PIN non deve essere composto da cifre consecutive", "PIN non valido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        oldPIN.setText("");
        newPIN.setText("");
        repeatNewPIN.setText("");
        btnPerformChangePIN.setEnabled(false);
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
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        btnPerformChangePIN.setEnabled(true);

                        switch (ret) {
                            case CKR_TOKEN_NOT_RECOGNIZED:
                                logger.Error("CIE non presente sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Sblocco CIE", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(5);
                                break;

                            case CKR_TOKEN_NOT_PRESENT:
                                logger.Error("CIE non presente sul lettore");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE non presente sul lettore", "Sblocco CIE", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(5);
                                break;

                            case CKR_PIN_INCORRECT:
                                logger.Error(String.format("Il PUK digitato è errato. Rimangono %d tentativi", attempts[0]));
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), String.format("Il PUK digitato è errato. Rimangono %d tentativi", attempts[0]), "PUK non corretto", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(5);
                                break;

                            case CKR_PIN_LOCKED:
                                logger.Error("PUK bloccato - la CIE deve essere sostituita");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "PUK bloccato. La tua CIE deve essere sostituita", "Carta bloccata", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(5);
                                break;

                            case CKR_GENERAL_ERROR:
                                logger.Error("Errore inaspettato durante la comunicazione con la smart card");
                                JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Errore inaspettato durante la comunicazione con la smart card", "Errore inaspettato", JOptionPane.ERROR_MESSAGE);
                                tabbedPane.setSelectedIndex(5);
                                break;

                            case CKR_OK:
                                logger.Info("CIE sbloccata con successo");
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

    private void removeCIE(String pan, String name) {
        logger.Info("Inizia - removeCIE()");

        if(!shouldSignWithoutPairing) {
            if (JOptionPane.showConfirmDialog(this.getContentPane(), "Stai rimuovendo la Carta di Identità di " + name + "\n dal sistema, per utilizzarla nuovamente "
                                              + " dovrai ripetere l'abbinamento", "Vuoi rimuovere la carta?", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
        }   

        int ret = Middleware.INSTANCE.DisabilitaCIE(pan);

        switch (ret) {
            case CKR_OK:
                logger.Info("CIE disabilitata con successo");
      
                Cie cie = cieDictionary.get(pan);
                String signPath = getSignImagePath(cie.getSerialNumber());

                try {
                    logger.Info(String.format("Cancello '%s'", signPath));
                    Files.deleteIfExists(Paths.get(signPath));
                } catch (NoSuchFileException x) {
                    String msg = String.format("%s: no such file or directory", signPath);
                    logger.Error(msg);
                    System.err.println(msg);
                } catch (DirectoryNotEmptyException x) {
                    String msg = String.format("%s not empty", signPath);
                    logger.Error(msg);
                    System.err.println(msg);
                } catch (IOException x) {
                    // File permission problems are caught here.
                    String msg = x.toString();
                    logger.Error(msg);
                    System.err.println(msg);
                }

                cieDictionary.remove(pan);
                Gson gson = new Gson();
                String stringDictionary = gson.toJson(cieDictionary);
                Utils.setProperty("cieDictionary", stringDictionary);

                if(!shouldSignWithoutPairing) {
                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE disabilitata con successo", "CIE disabilitata", JOptionPane.INFORMATION_MESSAGE);
                    selectHome();
                }

                break;

            default:
                logger.Error("Impossibile disabilitare la CIE");
                if(!shouldSignWithoutPairing)
                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Impossibile disabilitare la CIE di " + name, "CIE non disabilitata", JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void removeAllCIE(List<Cie> cieList) {
        logger.Info("Inizia - removeAllCIE()");

        if (JOptionPane.showConfirmDialog(this.getContentPane(), "Vuoi disabilitare tutte le CIE attualmente abbinate?", "Disabilita CIE", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        for (int i = 0; i < cieList.size(); i++) {
            int ret = Middleware.INSTANCE.DisabilitaCIE(cieList.get(i).getPan());

            switch (ret) {
                case CKR_OK:
                    cieDictionary.remove(cieList.get(i).getPan());
                    String signPath = getSignImagePath(cieList.get(i).getSerialNumber());

                    try {
                        logger.Info(String.format("Cancello '%s'", signPath));
                        Files.deleteIfExists(Paths.get(signPath));
                    } catch (NoSuchFileException x) {
                        String msg = String.format("%s: no such" + " file or directory", signPath);
                        logger.Error(msg);
                        System.err.println(msg);
                    } catch (DirectoryNotEmptyException x) {
                        String msg = String.format("%s not empty", signPath);
                        logger.Error(msg);
                        System.err.println(msg);
                    } catch (IOException x) {
                        // File permission problems are caught here.
                        String msg = x.toString();
                        logger.Error(msg);
                        System.err.println(msg);
                    }

                    Gson gson = new Gson();
                    String stringDictionary = gson.toJson(cieDictionary);
                    Utils.setProperty("cieDictionary", stringDictionary);
                    break;

                default:
                    logger.Error("Impossibile disabilitare la CIE " + cieList.get(i).getSerialNumber());
                    JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "Impossibile disabilitare la CIE numero " + cieList.get(i).getSerialNumber(), "CIE non disabilitata", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }

        JOptionPane.showMessageDialog(MainFrame.this.getContentPane(), "CIE disabilitata con successo", "CIE disabilitata", JOptionPane.INFORMATION_MESSAGE);
        selectHome();
    }

    private void configureHomeButtons(Map<String, Cie> cieDictionary) {
        logger.Info("Inizia - configureHomeButtons()");
        btnSelectCIE.setVisible(false);
        btnSignWithoutPairing.setVisible(false);
        btnNewButton.setVisible(true);
        btnRemoveSelected.setVisible(true);

        if (cieDictionary.size() > 1) {
            btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));
            btnRemoveAll.setVisible(true);
        } else {
            btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 5));
            btnRemoveAll.setVisible(false);
        }

        if (cieDictionary.size() == 0) {
            btnCancel.setVisible(false);
        } else {
            btnCancel.setVisible(true);
        }
    }

    private void selectHome() {
        logger.Info("Inizia - selectHome()");
        shouldSignWithoutPairing = false;

        //Utils.setProperty("cieDictionary", "");
        txtpnCIEPanelsSubtitle.setText("Carta di Identità Elettronica abbinata correttamente");
        txtpnCIEPanelsSubtitle.setHighlighter(null);
        lblCieId.setText("CIE ID");

        if (!Utils.getProperty("serialnumber", "").equals("")) {
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
            tabbedPane.setSelectedIndex(2);
        } else {
            if (!Utils.getProperty("cieDictionary", "").equals("") && !Utils.getProperty("cieDictionary", "").equals("{}")) {
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<HashMap<String, Cie>>() {} .getType();
                cieDictionary = gson.fromJson(Utils.getProperty("cieDictionary", ""), type);

                for (String key : cieDictionary.keySet()) {
                    if (cieDictionary.get(key).getIsCustomSign() == null) {
                        cieDictionary.get(key).setIsCustomSign(false);
                        String signPath = getSignImagePath(cieDictionary.get(key).getSerialNumber());
                        drawText(cieDictionary.get(key).getName(), signPath);
                    }
                }

                String serialDictionary = gson.toJson(cieDictionary);
                Utils.setProperty("cieDictionary", serialDictionary);
                cieCarousel.configureCards(cieDictionary);
                tabbedPane.setSelectedIndex(2);
            } else {
                cieDictionary = new HashMap<String, Cie>();
                tabbedPane.setSelectedIndex(0);
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
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

    private String toFirstCharUpperAll(String string) {
        StringBuffer sb = new StringBuffer(string);

        for (int i = 0; i < sb.length(); i++)
            if (i == 0 || sb.charAt(i - 1) == ' ') { //first letter to uppercase by default
                sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
            }

        return sb.toString();
    }

    private void selectCardholder() {
        logger.Info("Inizia - selectCardholder()");
        //tabbedPane.setSelectedIndex(2);
        selectHome();
        configureHomeButtons(cieDictionary);
        selectButton(btnHome);
    }

    private void selectUnlock() {
        logger.Info("Inizia - selectUnlock()");
        tabbedPane.setSelectedIndex(5);
        selectButton(btnUnlockCard);
    }

    /** Logger */
    public void loadLogConfigFromFile() {
        boolean writeLogConfigFile = false;
        Path configFilePath = Paths.get(System.getProperty("user.home"), ".CIEPKI", "config");

        try {
            List<String> configFileLines = Files.readAllLines(configFilePath);

            for (String line : configFileLines) {
                boolean isAppConfigLine = line.contains(LOG_CONFIG_PREFIX_APP);
                boolean isLibConfigLine = line.contains(LOG_CONFIG_PREFIX_LIB);

                if ((isAppConfigLine || isLibConfigLine) && (isAppConfigLine != isLibConfigLine)) {
                    String value = line.split("=")[1];

                    try {
                        Integer intValue = new Integer(value);

                        try {
                            LogLevel valueLevel = LogLevel.getLevelFromInteger(intValue);

                            if (valueLevel.compareTo(LogLevel.NONE) >= 0 && valueLevel.compareTo(LogLevel.ERROR) <= 0) {
                                if (isAppConfigLine) {
                                    logConfig.app = valueLevel;
                                } else {
                                    logConfig.lib = valueLevel;
                                }
                            }
                        } catch (IllegalArgumentException exception) {
                            if (isAppConfigLine) {
                                System.out.println(String.format("valore '%s' del livello di log applicazione fuori intervallo - uso default", intValue));
                            } else {
                                System.out.println(String.format("valore '%s' del livello di log libreria fuori intervallo - uso default", intValue));
                            }

                            writeLogConfigFile = true;
                        }
                    } catch (Exception exception) {
                        System.out.println(String.format("valore '%s' del livello di log libreria non valido - uso default", value));
                        writeLogConfigFile = true;
                    }
                } else {
                    System.out.println(String.format("Riga di configurazione log non valida: %s", line));
                }
            }
        } catch (Exception exception) {
            System.out.println("File configurazione log non trovato, lo creo con valori di default");
            writeLogConfigFile = true;
        }

        if (writeLogConfigFile) {
            saveLogConfigToFile();
        }

        logger.setLevel(logConfig.app);
    }

    private void saveLogConfigToFile() {
        logger.Info("Inizia - saveLogConfigToFile()");
        Path configFilePath = Paths.get(System.getProperty("user.home"), ".CIEPKI", "config");
        List<String> configList = new ArrayList<String>();
        configList.add(String.format("%s=%s", LOG_CONFIG_PREFIX_LIB, logConfig.lib.ordinal()));
        configList.add(String.format("%s=%s", LOG_CONFIG_PREFIX_APP, logConfig.app.ordinal()));

        try {
            Files.write(configFilePath, configList);
        } catch (Exception exception) {
            System.out.println("Impossibile salvare il file di configurazione");
            System.out.println(exception);
        }
    }
}
