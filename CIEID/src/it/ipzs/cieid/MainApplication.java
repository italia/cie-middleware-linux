package it.ipzs.cieid;

import java.awt.EventQueue;

import javax.swing.JFrame;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEvent;
import ch.swingfx.twinkle.event.NotificationEventAdapter;
import ch.swingfx.twinkle.style.closebutton.NullCloseButton;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;
import it.ipzs.cieid.util.Utils;

public class MainApplication {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				if(args.length > 0 && args[0].equals("pinwrong"))
				{
					notifyPinWrong();
				}
				else if(args.length > 0 && args[0].equals("cardnotregistered"))
				{
					notifyCardNotRegistered();
				}
				else if(args.length > 0 && args[0].equals("pinlocked"))
				{
					notifyPinLocked();
				}
				else 
				{				
					try {
						showUI(args);
					} catch (Exception e) {
						e.printStackTrace();
					}
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
	
	private static void notifyPinWrong()
	{
		NotificationBuilder nb = createNotificationBuilder();
		nb.withTitle("CIE ID");
		nb.withMessage("PIN errato");		
		//nb.withIcon(CrmIcons.CALL);
		nb.withDisplayTime(1000 * 5);

//		nb.withListener(new NotificationEventAdapter() {		
//			@Override
//			public void clicked(NotificationEvent event)
//			{
//				// do nothing
//				setLookAndFeel();
//				MainApplication.showUI(new String[] {});
//			}
//		});

		nb.showNotification();				
	}
	
	private static void notifyCardNotRegistered()
	{		
		NotificationBuilder nb = createNotificationBuilder();
		nb.withTitle("CIE ID");
		nb.withMessage("Carta non abbinata, premere qui per abbinare la CIE");		
		//nb.withIcon(CrmIcons.CALL);
		nb.withDisplayTime(1000 * 10);

		nb.withListener(new NotificationEventAdapter() {		
			@Override
			public void clicked(NotificationEvent event)
			{
//				setLookAndFeel();
				MainApplication.showUI(new String[] {});
			}
		});

		nb.showNotification();		
	}
	
	private static void notifyPinLocked()
	{
		NotificationBuilder nb = createNotificationBuilder();
		nb.withTitle("CIE ID");
		nb.withMessage("Carta bloccata, premere qui per sbloccarla con il PUK");		
		//nb.withIcon(CrmIcons.CALL);
		nb.withDisplayTime(1000 * 10);

		nb.withListener(new NotificationEventAdapter() {		
			@Override
			public void clicked(NotificationEvent event)
			{
				MainApplication.showUI(new String[] {"unlock"});
			}
		});

		nb.showNotification();		
	}
	
	public static NotificationBuilder createNotificationBuilder()
	{
		NotificationBuilder nb = new NotificationBuilder();
		LightDefaultNotification style = new LightDefaultNotification();
		style.withCloseButton(new NullCloseButton());
		nb.withStyle(style);
		nb.withFadeInAnimation(true);
		nb.withFadeOutAnimation(true);
		nb.withPosition(Positions.NORTH_EAST);
		//nb.withDisplayTime(10000);

		return nb;
	}

}
