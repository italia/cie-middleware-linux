package it.ipzs.cieid.Firma;

import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Stream;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import it.ipzs.cieid.MainFrame;
import it.ipzs.cieid.Middleware.verifyInfo;

public class VerifyTable {

	private JTable table;
    
    private static final int REVOCATION_STATUS_GOOD = 0;
    private static final int REVOCATION_STATUS_REVOKED = 1;
    private static final int REVOCATION_STATUS_SUSPENDED = 2;
    private static final int REVOCATION_STATUS_UNKNOWN = 3;
    
    Object[][] data = new Object[0][0];
    
    String[] columnNames = {"", ""}; 
	
	
    private TableModel model;
    
    private void getDataTable(verifyInfo vInfo)
    {
    	String name = vInfo.get_name() + " " + vInfo.get_surname() + '\n' + vInfo.get_cn();
    	Icon nameIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/user.png"));
    	
    	String s_time = vInfo.get_signingTime();
    	Icon timeIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/calendar.png"));
    	
    	if(s_time != null)
    	{
    		String signingTime = s_time.substring(0, 12);
    		    		
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmmss", Locale.ITALY);
    		LocalDateTime dateTime = LocalDateTime.parse(signingTime, formatter);
    		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ITALY);    		
    		s_time = dateTime.format(formatterOut);
    	}else {

    		s_time = "Attributo Signing Time non presente";
    	}
    	
        String s_cert = "Il certificato non è valido";
        Icon certIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/orange_checkbox.png"));
        if (vInfo.isCertValid == 1) 
        {
            s_cert = "Il certificato è valido";
            certIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/green_checkbox.png"));
        }
    	
        String s_sign = "La firma non è valida";
        Icon signIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/orange_checkbox.png"));
        if (vInfo.isSignValid == 1)
        {
            s_sign = "La firma è valida";
            signIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/green_checkbox.png"));
        }
        
        String s_revoc = "Servizio di revoca non raggiungibile";
        Icon certRevIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/orange_checkbox.png"));
        if (vInfo.CertRevocStatus == VerifyTable.REVOCATION_STATUS_GOOD)
        {
            s_revoc = "Il certificato non è revocato";
            certRevIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/green_checkbox.png"));
        }
        else if (vInfo.CertRevocStatus == VerifyTable.REVOCATION_STATUS_REVOKED)
        {
            s_revoc = "Il certificato è revocato";
        }
        else if (vInfo.CertRevocStatus == VerifyTable.REVOCATION_STATUS_SUSPENDED)
        {
            s_revoc = "Il certificato è sospeso";
        }else if( vInfo.CertRevocStatus == VerifyTable.REVOCATION_STATUS_UNKNOWN)
        {
        	s_cert = "Certificato non verificato";
        }
        
        String s_cadn = vInfo.get_cadn(); 
        Icon cadnIcon = new ImageIcon(MainFrame.class.getResource("/it/ipzs/cieid/res/Firma/medal.png"));
        
        
        
        Object[][] data_new = new Object[][]
        		{ 
                { nameIcon, name}, 
                { timeIcon, s_time},
                { signIcon, s_sign}, 
                { certIcon, s_cert}, 
                { certRevIcon, s_revoc}, 
                { cadnIcon, s_cadn}
                
             }; 
             
             
         Object[][] combi = Stream.concat( Arrays.stream( data ), Arrays.stream( data_new ) ).toArray( Object[][]::new );     
         
         data = combi;
    }
	
	public VerifyTable(JScrollPane scrollPane)
	{        
		
		//addDataToModel(vInfo);
		model = new DefaultTableModel(data, columnNames) {

	        @Override
	        public Class<?> getColumnClass(int column) {
	            switch (column) {
	                case 0:
	                    return ImageIcon.class;
	                case 1:
	                    return JTextArea.class;
	                default:
	                    return String.class;
	            }
	        }

	        public boolean isCellEditable(int row, int column) {                
	            return false;               
	        };
	        
	    };
	    
        updateTable(scrollPane);
	}

	private void updateTable(JScrollPane verificaScrollPane) {
		
		table = new JTable(model) {
            
            @Override
            public void doLayout() {
                TableColumn col = getColumnModel().getColumn(1);
                for (int row = 0; row < getRowCount(); row++) {
                    Component c = prepareRenderer(col.getCellRenderer(), row, 0);
                    if (c instanceof JTextArea) {
                        int h = getContentHeight(row) + getIntercellSpacing().height;
                        if (getRowHeight(row) != h) {
                            setRowHeight(row, h);
                        }
                    }                    
                }
                
                
                super.doLayout();
            }
            
            
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                
                if (col == 0 && renderer.getClass() != TextAreaCellRenderer.class) {
                	JLabel c = (JLabel) super.prepareRenderer(renderer, row, col);
                    c.setVerticalAlignment(JLabel.TOP);
                    return c;
                }else {
                	return super.prepareRenderer(renderer, row, col);
                }
           }
        
            
            public int getContentHeight(int row) {                
                if((((row+1)%6) == 0))
                {
                    return 90;
                }else 
                {
                    return 35;
                }
            }
        }; 
        
        table.setTableHeader(null);
        table.removeEditor();
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setFocusable(false);
        table.getColumnModel().getColumn(1).setCellRenderer(new TextAreaCellRenderer());
        table.setShowGrid(false);
        table.setAutoResizeMode( JTable.AUTO_RESIZE_LAST_COLUMN );
        
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        //table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer());
        verificaScrollPane.getViewport().add(table);
        verificaScrollPane.add(Box.createVerticalStrut(20));
	}

	public void addDataToModel(JScrollPane verificaScrollPane, verifyInfo vInfo) {
		getDataTable(vInfo);
		
		model = new DefaultTableModel(data, columnNames) {

	        @Override
	        public Class<?> getColumnClass(int column) {
	            switch (column) {
	                case 0:
	                    return ImageIcon.class;
	                case 1:
	                    return JTextArea.class;
	                default:
	                    return String.class;
	            }
	        }

	        public boolean isCellEditable(int row, int column) {                
	            return false;               
	        };
	        
	    };
	    
	    updateTable(verificaScrollPane);
	}
}

class TextAreaCellRenderer extends JTextArea implements TableCellRenderer {

    private static final long serialVersionUID = 1L;

    public TextAreaCellRenderer() {
        super();
        setLineWrap(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
    	    	
        setFont(table.getFont());
        setText((value == null) ? "" : value.toString());
        return this;
    }
    
    
}
