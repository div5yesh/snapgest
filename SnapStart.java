import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;


public class SnapStart extends Frame
{
	private int state;
	static TrayIcon trayIcon;
	public static void main(String s[]) throws Exception
	{
		new SnapStart("SnapGest");
	}	
	public SnapStart(String s) throws IOException, AWTException
	{
		super(s);
		setSize(1024,768);
		setLocation(100, 0);
		
	//	setUndecorated(true);
//		JButton min=new JButton(new ImageIcon("bulb.gif"));
//		min.setBounds(0, 0, 30, 12);
//		setLayout(null);
//		add(min);
	
		final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(ImageIO.read(new File("SnapGest.png")));
        final SystemTray tray = SystemTray.getSystemTray();
        
        final CheckboxMenuItem show = new CheckboxMenuItem("Show");
        MenuItem about = new MenuItem("About");
        MenuItem exit = new MenuItem("Exit");
        
        popup.add(show);
        popup.addSeparator();
        popup.add(about);
        popup.addSeparator();
        popup.add(exit);
        
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("SnapGest");
        trayIcon.displayMessage("SnapGest","Your gesture suite is running.", TrayIcon.MessageType.INFO);
        
        setVisible(false);
      // SnapThread.g=getGraphics();
        SnapThread.state=ItemEvent.DESELECTED;
        show.addItemListener(new ItemListener()
        {
        	public void itemStateChanged(ItemEvent e)
        	{
        		state=e.getStateChange();
        		if(state==ItemEvent.SELECTED)
        		{
        			setVisible(true);
        			SnapThread.g=getGraphics();
        			SnapThread.state=ItemEvent.SELECTED;
        		}
        		if(state==ItemEvent.DESELECTED)
        		{
        			setVisible(false);
        			SnapThread.state=ItemEvent.DESELECTED;
        		}
        	}
        });
        
        about.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		JOptionPane.showMessageDialog(null,"Created by:\nDivyesh\nJigar");
        	}
        });
        exit.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		System.exit(0);
        	}
        });
   
		
		Thread t1=new SnapThread();
		t1.start();
		
//		min.addActionListener(new ActionListener()
//		{
//			public void actionPerformed(ActionEvent e) 
//			{	
//				setVisible(false);
//    			SnapThread.state=ItemEvent.DESELECTED;
//    			show.setState(false);
//			}
//		});
		
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
			public void windowIconified(WindowEvent e)
			{
				setVisible(false);
    			SnapThread.state=ItemEvent.DESELECTED;
    			show.setState(false);
			}
		});
	}
}
