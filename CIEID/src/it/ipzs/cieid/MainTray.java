package it.ipzs.cieid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.sun.jna.Platform;
import com.ugos.crypt.hash.SHA1;

import ch.swingfx.twinkle.NotificationBuilder;
import ch.swingfx.twinkle.event.NotificationEvent;
import ch.swingfx.twinkle.event.NotificationEventAdapter;
import ch.swingfx.twinkle.style.closebutton.NullCloseButton;
import ch.swingfx.twinkle.style.theme.LightDefaultNotification;
import ch.swingfx.twinkle.window.Positions;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.SystemTray;
import dorkbox.util.CacheUtil;
import dorkbox.util.SwingUtil;


public class MainTray {

	/**
	 * The CORS {@code Access-Control-Allow-Methods} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
	/**
	 * The CORS {@code Access-Control-Allow-Origin} response header field name.
	 * @see <a href="http://www.w3.org/TR/cors/">CORS W3C recommendation</a>
	 */
	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	
	public static byte[] key = "this is a fake key".getBytes();
	
	public static void main(String[] args) {
		//SystemTray.SWING_UI = new Swing

		int p = Platform.getOSType();
		System.out.println(p);
		
		System.out.println(UIManager.getLookAndFeel());
		
        try {
			//SystemTray.DEBUG = true; // for test apps, we always want to run in debug mode
			//CacheUtil.clear(); // for test apps, make sure the cache is always reset. You should never do this in production.

			SwingUtil.setLookAndFeel(null); // set Native L&F (this is the System L&F instead of CrossPlatform L&F)
			SystemTray.SWING_UI = new CustomSwingUI();
			
			SystemTray systemTray = SystemTray.get();
			if (systemTray == null) {
			    throw new RuntimeException("Unable to load SystemTray!");
			}
 
			systemTray.setImage(MainTray.class.getResource("/it/ipzs/cieid/res/Risorsa 19.png"));
			//systemTray.setStatus("Running");		
			systemTray.getMenu().add(new MenuItem("Quit", new ActionListener() {
			    @Override
			    public void actionPerformed(final ActionEvent e) {
			    	
			        systemTray.shutdown();
			        System.exit(0);  //not necessary if all non-daemon threads have stopped.
			    }
			})).setShortcut('q'); // case does not matter
			
			System.out.println("CIEID Service starting on port 8888");
			
			try (ServerSocket serverSocket = new ServerSocket(8888)) {
				 
	            System.out.println("Server is listening on port " + 8888);
	 
	            byte[] buffer = new byte[1000];
	            
	            while (true) {
	                Socket socket = serverSocket.accept();
	 
	                System.out.println("New client connected");
	 
	                InputStream ins = socket.getInputStream();
	                int len = ins.read(buffer, 0, 1000);
	                
	                byte[] ciphertext = new byte[len];
	                System.arraycopy(buffer,  0, ciphertext, 0, len);
	                
	                byte[] derivedKey = SHA1.hash(key);
	                
	                byte[] fullKey = new byte[16];
	                
	                byte[] iv = new byte[16];
	                
	                System.arraycopy(derivedKey, 0, fullKey, 0, 16);
	                
	                byte[] bClientMessage = AESFast.decryptCBC(ciphertext, 4, fullKey, 16, iv, 16);
	                	                	
	                OutputStream output = socket.getOutputStream();
	                PrintWriter writer = new PrintWriter(output, true);
	                
	                String clientMessage = new String(bClientMessage);
	 	                
	                if(clientMessage.startsWith("pinlocked"))
	                {
	                	writer.println(clientMessage);	                    	                
	                	notifyPinLocked();
	                }
	                else if(clientMessage.startsWith("pinwrong"))
	                {
	                	writer.println(clientMessage);
	                    
	                	notifyPinWrong();
	                }
	                else if(clientMessage.startsWith("cardnotregistered"))
	                {
	                	writer.println(clientMessage);
	                    
	                	notifyCardNotRegistered();
	                }
	                else
	                {
	                    System.out.println("invalid message received " + clientMessage);
	                }	                	                	               
	            }
	 
	        } catch (IOException ex) {
	            System.out.println("Server exception: " + ex.getMessage());
	            ex.printStackTrace();
	        }
					
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        

	}

	private static void notifyPinWrong()
	{
//		Notify.create()
//	      .title("CIE ID")
//	      .text("PIN errato")
//	      .position(Pos.TOP_RIGHT)
//	      .darkStyle()
//	      .onAction(new ActionHandler<Notify>() {
//			
//			@Override
//			public void handle(Notify arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		})
//	    .showError();
	}
	
	private static void notifyCardNotRegistered()
	{		
		NotificationBuilder nb = createNotificationBuilder();
		nb.withTitle("CIE ID");
		nb.withMessage("Carta non abbinata, premere qui per abbinare la CIE");		
		//nb.withIcon(CrmIcons.CALL);
		nb.withDisplayTime(10000000);

		nb.withListener(new NotificationEventAdapter() {		
			@Override
			public void clicked(NotificationEvent event)
			{
				setLookAndFeel();
//				new Thread(new Runnable() {
//				
//					@Override
//					public void run() {
						// TODO Auto-generated method stub
						MainApplication.showUI(new String[] {});
//					}
//				}).start();
			}
		});

		nb.showNotification();
		
//		Notify.create()
//	      .title("CIE ID")
//	      .text("Carta non abbinata, premere qui per abbinare la CIE")
//	      .position(Pos.TOP_RIGHT)
//	      .darkStyle()
//	      .onAction(new ActionHandler<Notify>() {
//			
//			@Override
//			public void handle(Notify arg0) {
//				
//				
//				MainApplication.showUI(new String[] {});				
//			}
//		})
//	    .showWarning();
	}
	
	private static void notifyPinLocked()
	{
		NotificationBuilder nb = createNotificationBuilder();
		nb.withTitle("CIE ID");
		nb.withMessage("Carta bloccata, premere qui per sbloccarla con il PUK");		
		//nb.withIcon(CrmIcons.CALL);
		//nb.withDisplayTime(30000);

		nb.withListener(new NotificationEventAdapter() {		
			@Override
			public void clicked(NotificationEvent event)
			{
//				new Thread(new Runnable() {
//				
//					@Override
//					public void run() {
						// TODO Auto-generated method stub
						MainApplication.showUI(new String[] {"unlock"});
//					}
//				}).start();
			}
		});

		nb.showNotification();
		
//		Notify.create()
//	      .title("CIE ID")
//	      .text("Carta bloccata, premere qui per sbloccarla con il PUK")
//	      .position(Pos.TOP_RIGHT)
//	      .darkStyle()
//	      .onAction(new ActionHandler<Notify>() {
//			
//			@Override
//			public void handle(Notify arg0) {
////				new Thread(new Runnable() {
////					
////					@Override
////					public void run() {
////						// TODO Auto-generated method stub
////						MainApplication.main(new String[] {"unlock"});
////					}
////				}).start();
//								
//			}
//		})
//	    .showWarning();
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
	
	private static void setLookAndFeel()
	{
		try {
            // Set cross-platform Java L&F (also called "Metal")
        UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
        
        //SwingUtil.setLookAndFeel(UIManager.get); // set Native L&F (this is the System L&F instead of CrossPlatform L&F)
	    } 
	    catch (UnsupportedLookAndFeelException e) {
	       // handle exception
	    }
	    catch (ClassNotFoundException e) {
	       // handle exception
	    }
	    catch (InstantiationException e) {
	       // handle exception
	    }
	    catch (IllegalAccessException e) {
	       // handle exception
	    }	
	}
	
}
