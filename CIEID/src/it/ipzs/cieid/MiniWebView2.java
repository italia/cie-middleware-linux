package it.ipzs.cieid;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

public class MiniWebView2 extends JPanel {

  private JWebBrowser webBrowser;
	
  public MiniWebView2() {
	setLayout(new BorderLayout());
	webBrowser = new JWebBrowser();
    webBrowser.navigate("http://www.google.com");
    add(webBrowser, BorderLayout.CENTER);        
  }

  public void showPage(String url)
  {
	  webBrowser.navigate(url);
  }
}
