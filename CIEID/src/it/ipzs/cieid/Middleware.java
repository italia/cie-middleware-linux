package it.ipzs.cieid;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Middleware extends Library {
	Middleware INSTANCE = (Middleware) Native.loadLibrary("cie-pkcs11", Middleware.class);
	
	interface ProgressCallBack extends Callback {
        void invoke(int progress, String message);
    }
	
	interface CompletedCallBack extends Callback {
        void invoke(String pan, String name, String ef_seriale);
    }

	public int AbilitaCIE(String szPAN, String szPIN, int[] attempts, ProgressCallBack progressCallBack, CompletedCallBack completedCallBack);
	public int VerificaCIEAbilitata(String szPAN);
	public int DisabilitaCIE(String szPAN);
	public int CambioPIN(String  currentPIN, String nuovoPIN, int[] attempts, ProgressCallBack progressCallBack);
	public int SbloccoPIN(String puk, String nuovoPIN, int[] attempts, ProgressCallBack progressCallBack);
	
	

}

