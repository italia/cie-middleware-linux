package it.ipzs.cieid;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BrokenBarrierException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

public class MiniWebView extends JPanel implements HyperlinkListener {

  private JEditorPane displayEditorPane = new JEditorPane();

  public MiniWebView() {
	setLayout(new BorderLayout());
    displayEditorPane.setContentType("text/html");
    displayEditorPane.setEditable(false);
    displayEditorPane.addHyperlinkListener(this);

    add(new JScrollPane(displayEditorPane), BorderLayout.CENTER);
  }

  public void showPage(String url)
  {
	  try {
		showPage(new URL(url));
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public void showPage(URL pageUrl) {
    try {     
      displayEditorPane.setPage(pageUrl);
    } catch (Exception e) {
      System.out.println("Unable to load page");
    }
  }

  public void hyperlinkUpdate(HyperlinkEvent event) {
    HyperlinkEvent.EventType eventType = event.getEventType();
    if (eventType == HyperlinkEvent.EventType.ACTIVATED) {
      if (event instanceof HTMLFrameHyperlinkEvent) {
        HTMLFrameHyperlinkEvent linkEvent = (HTMLFrameHyperlinkEvent) event;
        HTMLDocument document = (HTMLDocument) displayEditorPane.getDocument();
        document.processHTMLFrameHyperlinkEvent(linkEvent);
      } else {
        showPage(event.getURL());
      }
    }
  }
}
