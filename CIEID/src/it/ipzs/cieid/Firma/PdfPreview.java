package it.ipzs.cieid.Firma;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class PdfPreview {
    private JPanel prPanel;
    private String filePath;
    private int pdfPageIndex;
    private int pdfNumPages;
	private List<Image> images;
	private JLabel imgLabel;
    private ImageIcon imgIcon;
    private MoveablePicture signImage;
    private JPanel imgPanel;
    
    public PdfPreview(JPanel panelPdfPreview, String pdfFilePath, String signImagePath)
    {
    	this.prPanel = panelPdfPreview;
    	this.filePath = pdfFilePath;
    	this.pdfPageIndex = 0;
    	imgIcon = new ImageIcon();
    	imgLabel = new JLabel();
    	imgPanel = new JPanel();
		imgPanel.setLayout(new BorderLayout(0,0));
		imgPanel.setBackground(Color.white);
		signImage = new MoveablePicture(signImagePath);
		imgPanel.add(signImage );
		imgPanel.add(imgLabel);

		// ghost4j dichiara le callback di stdio come StdCallCallback, quindi
		// stdcall, che esiste solo su Windows.
		// Su Linux Ghostscript.initialize() lancia una
		// IllegalArgumentException. Non essendo controllata sfuggiva ai catch
		// qui sotto e uccideva l'Event Dispatch Thread.
		// Conto le pagine e reinderizzo a 100 DPI con il binario gs.
		try {
			prPanel.removeAll();
			images = renderWithGhostscript(filePath, 100);
			pdfNumPages = images.size();
			System.out.println("Pdf page: " + pdfNumPages);

			if(pdfNumPages == 0)
				throw new IOException("nessuna pagina renderizzata");

			showPreview();
        } catch (GhostscriptMissingException e) {
            System.out.println(e.getMessage());
				JOptionPane.showMessageDialog(prPanel,
					"Per l'anteprima del documento e la firma grafica e' necessario\n"
					+ "Ghostscript, che non risulta installato.\n\n"
					+ "Installare il pacchetto \"ghostscript\" della propria distribuzione\n"
					+ "e riaprire questa finestra.",
					"Ghostscript non trovato", JOptionPane.WARNING_MESSAGE);
		} catch (IOException e) {
			System.out.println("Anteprima PDF non disponibile");
			e.printStackTrace();
		} catch (RuntimeException e) {
			// Catturo la Exception per evitare che un errore blocchi l'applicazione.
			System.out.println("Anteprima PDF non disponibile");
			e.printStackTrace();
		}
    }

    /**
     * Eccezione lanciata quando non è possibile trovare il binario gs.
     */
    static class GhostscriptMissingException extends IOException
    {
        private static final long serialVersionUID = 1L;
        GhostscriptMissingException(String message)
        {
            super(message);
        }
    }

    /**
     * Directory di fallback in cui cercare il binario gs se non è nel PATH.
     */
    private static final String[] FALLBACK_DIRS = { "/usr/bin", "/bin" };

    /**
     * Cerca il binario gs nel PATH di sistema.
     * 
     * @return il file del binario, o null se non trovato
     */
    private static File findExecutable(String name)
    {
        List<String> dirs = new ArrayList<String>();

        String path = System.getenv("PATH");
        if (path != null)
            dirs.addAll(Arrays.asList(path.split(File.pathSeparator)));

        dirs.addAll(Arrays.asList(FALLBACK_DIRS));
        
        for (String dir : dirs)
        {
            if (dir.isEmpty())
                continue;

            File candidate = new File(dir, name);
            if (candidate.isFile() && candidate.canExecute())
                return candidate;
        }
        return null;
    }

    /**
     * Nome del binario Ghostscript su Linux (e macOS).
     */
    private static final String GHOSTSCRIPT_BINARY = "gs";

    /**
     * Renderizza le pagine invocando il binario gs. Il numero di file
     * prodotti è pari al numero di pagine.
     * 
     * @param pdfPath il percorso del file PDF da renderizzare
     * @param dpi la risoluzione in DPI
     * @return una lista di immagini, una per pagina
     * @throws IOException in caso di errore durante l'esecuzione del processo o nella lettura dei file immagine
     */
    private static List<Image> renderWithGhostscript(String pdfPath, int dpi) throws IOException
    {
        Path tmpDir = Files.createTempDirectory("cieid-preview");
        try
        {
            File gs = findExecutable(GHOSTSCRIPT_BINARY);
            if (gs == null)
                throw new GhostscriptMissingException(
                        "Ghostscript non trovato: '" + GHOSTSCRIPT_BINARY
                        + "' non e' presente ne' in PATH ne' in "
                        + Arrays.toString(FALLBACK_DIRS));

            ProcessBuilder pb = new ProcessBuilder(gs.getAbsolutePath(),
                    "-q", "-dNOPAUSE", "-dBATCH", "-dSAFER",
                    "-sDEVICE=png16m",
                    "-r" + dpi,
                    "-sOutputFile=" + tmpDir.resolve("page-%d.png").toString(),
                    pdfPath);
            pb.redirectErrorStream(true);

            Process proc = pb.start();

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                output.append(line).append('\n');

            int rc;
            try
            {
                rc = proc.waitFor();
            }
            catch (InterruptedException ie)
            {
                Thread.currentThread().interrupt();
                throw new IOException("rendering interrotto");
            }

            if (rc != 0)
                throw new IOException("gs terminato con codice " + rc + ": " + output);

            File[] pages = tmpDir.toFile().listFiles();
            if (pages == null)
                return Collections.<Image>emptyList();

            // In ordine alfabetico page-2.png viene dopo page-10.png.
            // Ordino sull'indice numerico, non sul nome.
            Arrays.sort(pages, new java.util.Comparator<File>() {
                public int compare(File l, File r) {
                    return Integer.compare(pageIndexOf(l), pageIndexOf(r));
                }
            });

            List<Image> result = new ArrayList<Image>();
            for (int i = 0; i < pages.length; i++)
            {
                BufferedImage img = ImageIO.read(pages[i]);
                if (img != null)
                    result.add(img);
            }
            return result;
        }
        finally
        {
            // Pulisco la directory temporanea.
            File[] leftovers = tmpDir.toFile().listFiles();
            if (leftovers != null)
            {
                for (int i = 0; i < leftovers.length; i++)
                    leftovers[i].delete();
            }
            tmpDir.toFile().delete();
        }
    }

    /**
     * Restituisce l'indice della pagina a partire dal nome del file.
     * 
     * @param f un file con nome del tipo page-1.png, page-2.png, ecc.
     * @return l'indice della pagina, o Integer.MAX_VALUE se il nome non è valido
     */
    private static int pageIndexOf(File f)
    {
        String name = f.getName();
        int dash = name.lastIndexOf('-');
        int dot = name.lastIndexOf('.');
        if (dash < 0 || dot <= dash)
            return Integer.MAX_VALUE;
        try
        {
            return Integer.parseInt(name.substring(dash + 1, dot));
        }
        catch (NumberFormatException e)
        {
            return Integer.MAX_VALUE;
        }
    }

    private void showPreview()
    {
    	Image tmpImg = images.get(pdfPageIndex);
    	
    	int width = prPanel.getWidth();
    	int height = prPanel.getHeight();
    	
    	int tmpImgWidth = tmpImg.getWidth(null);
    	int tmpImgHeight =  tmpImg.getHeight(null);
    	
    	int imgHeigth = height;
    	int imgWidth = width;
    
    	if( tmpImgWidth > tmpImgHeight)
    	{
    		imgHeigth  = (int)(width*tmpImgHeight)/tmpImgWidth;
    		
    		if(imgHeigth > height)
    		{
    			imgWidth = (int)(height*tmpImgWidth)/tmpImgHeight;
				imgHeigth = (int)(imgWidth*tmpImgHeight)/tmpImgWidth;
    		}
    	}else
    	{
    		imgWidth = (int)(height*tmpImgWidth)/tmpImgHeight;
                    
            if(imgWidth > width)
            {
            	imgHeigth = (int)(width*imgHeigth)/tmpImgWidth;
        		imgWidth = (int)(imgHeigth*tmpImgWidth)/imgHeigth;
            }
    	}
    	
    	
		imgIcon.setImage(tmpImg.getScaledInstance(imgWidth, imgHeigth, Image.SCALE_AREA_AVERAGING));
		imgLabel.setIcon(imgIcon);
		imgLabel.setHorizontalAlignment(JLabel.CENTER);
		imgLabel.setVerticalAlignment(JLabel.CENTER);
	    imgLabel.revalidate();
	    imgLabel.repaint();
		
		//imgPanel.removeAll();
		imgPanel.setMaximumSize(new Dimension(imgWidth, imgHeigth));
		imgPanel.updateUI();
		
		prPanel.removeAll();
		prPanel.add(imgPanel);
		prPanel.updateUI();
    }
    
    public void prevImage()
    {
		if((pdfPageIndex -1) >= 0)
		{
			pdfPageIndex -= 1;
		}
		
		showPreview();
    }
    
    public void nextImage()
    {
		if((pdfPageIndex + 1) < pdfNumPages)
		{
			pdfPageIndex += 1;
		}
		
		showPreview();
    }
    
    public int getSelectedPage()
    {
    	return pdfPageIndex;
    }
    
    public float[] signImageInfos()
    {
    	float infos[] = new float[4];
    	
    	float x = ((float)signImage.getX() / (float)imgPanel.getWidth());
    	float y = ((float)(signImage.getY() + signImage.getHeight())/ (float)imgPanel.getHeight());
    	float w = ((float)signImage.getWidth() / (float)imgPanel.getWidth());
    	float h = ((float)signImage.getHeight() / (float)imgPanel.getHeight());
    	
    	infos[0] = x;
    	infos[1] = y;
    	infos[2] = w;
    	infos[3] = h;
    	
    	return infos;
    }
    
}
