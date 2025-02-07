package it.ipzs.cieid;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import it.ipzs.cieid.util.Utils;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.BoxLayout;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;

public class PINNoticeDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private final JLabel lblAnnotaIlNuovo = new JLabel("Annota il nuovo codice PIN");

	Timer timer;
	int timerCountdown = 5;
	private JButton btnOk;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PINNoticeDialog dialog = new PINNoticeDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PINNoticeDialog() {
		getContentPane().setBackground(Color.WHITE);
		setResizable(false);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBackground(Color.WHITE);
		setAlwaysOnTop(true);
		setBounds(100, 100, 404, 388);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblPrimaDiProcedere = new JLabel("PRIMA DI PROCEDERE");
			lblPrimaDiProcedere.setBounds(104, 12, 197, 19);
			lblPrimaDiProcedere.setHorizontalAlignment(SwingConstants.CENTER);
			lblPrimaDiProcedere.setFont(new Font("Dialog", Font.BOLD, 16));
			contentPanel.add(lblPrimaDiProcedere);
		}
		lblAnnotaIlNuovo.setFont(new Font("Dialog", Font.BOLD, 14));
		lblAnnotaIlNuovo.setBounds(95, 29, 232, 29);
		contentPanel.add(lblAnnotaIlNuovo);
		
		JTextPane txtpnSarNecessarioPer = new JTextPane();
		txtpnSarNecessarioPer.setText("sarà necessario per le prossime richieste di autenticazione.");
		txtpnSarNecessarioPer.setFont(new Font("Dialog", Font.PLAIN, 14));
		txtpnSarNecessarioPer.setEditable(false);
		txtpnSarNecessarioPer.setBounds(12, 53, 380, 46);
		StyledDocument doc = txtpnSarNecessarioPer.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();		
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		contentPanel.add(txtpnSarNecessarioPer);
		{
			JLabel lblImportante = new JLabel("IMPORTANTE");
			lblImportante.setFont(new Font("Dialog", Font.BOLD, 14));
			lblImportante.setBounds(146, 126, 105, 29);
			contentPanel.add(lblImportante);
		}
		{
			JTextPane txtpnSeUtilizziCie = new JTextPane();
			txtpnSeUtilizziCie.setText("se utilizzi CIE ID anche su altre piattaforme (WIndows, Mac, Linux o altri dispositivi Android), è necessario ripetere l'abbinamento della CIE utilizzando il nuovo PIN su ogni piattaforma in uso.");
			txtpnSeUtilizziCie.setFont(new Font("Dialog", Font.PLAIN, 14));
			txtpnSeUtilizziCie.setEditable(false);
			txtpnSeUtilizziCie.setBounds(12, 150, 380, 79);
			contentPanel.add(txtpnSeUtilizziCie);
			doc = txtpnSeUtilizziCie.getStyledDocument();
			center = new SimpleAttributeSet();		
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			doc.setParagraphAttributes(0, doc.getLength(), center, false);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(Color.WHITE);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnOk = new JButton("  OK  ");
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						close();
					}
				});
				btnOk.setForeground(Color.WHITE);
				btnOk.setBackground(new Color(30, 144, 255));				
				buttonPane.add(btnOk);
			}
		}
		{
			JPanel headerPane = new JPanel();
			getContentPane().add(headerPane, BorderLayout.NORTH);
			headerPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			{
				JLabel label = new JLabel("");
				try {
					label.setIcon(new ImageIcon(Utils.scaleimage(40, 40, ImageIO.read(MainFrame.class.getResource("/it/ipzs/cieid/res/Logo_Cie_ID_Windowed@2x.png")))));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setSize(50, 50);
				headerPane.add(label);
			}
		}
		
		timer = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				timerCountdown--;
				
				if(timerCountdown < 0)
				{
					close();
				}
				else
				{
					btnOk.setText("OK (" + timerCountdown + ")");
				}
				
			}
		});
		
		btnOk.setText("  OK  ");
		timer.setDelay(1000);		
		timer.start();
	}
	
	private void close()
	{
		timer.stop();
		setVisible(false);		
	}
}
