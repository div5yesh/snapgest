import java.awt.CheckboxMenuItem;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class Launch 
{
	private static int state;
	private static TrayIcon trayIcon;
	public static void threads()
	{
		Thread snapThread=new SnapThread();
		snapThread.start();
	}
	public static void toTray(final SnapGUI gui) throws Exception
	{
		final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(ImageIO.read(new File("SnapGest.png")));
        final SystemTray tray = SystemTray.getSystemTray();
 //----------------------------------------------------------------------------------       
        final CheckboxMenuItem show = new CheckboxMenuItem("Show");
        MenuItem about = new MenuItem("About");
        MenuItem exit = new MenuItem("Exit");
 //---------------------------------------------------------------------------------       
        popup.add(show);
        popup.addSeparator();
        popup.add(about);
        popup.addSeparator();
        popup.add(exit);
//---------------------------------------------------------------------------------------      
        trayIcon.setPopupMenu(popup);
        tray.add(trayIcon);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("SnapGest");
        trayIcon.displayMessage("SnapGest","Your gesture suite is running.", TrayIcon.MessageType.INFO);
//----------------------------------------------------------------------------------------------      
        gui.setVisible(true);
        gui.setLocation(100, 0);
        SnapThread.g=gui.getGraphics();
        SnapThread.state=ItemEvent.SELECTED;
//---------------------------------------------------------------
        show.addItemListener(new ItemListener()
        {
        	public void itemStateChanged(ItemEvent e)
        	{
        		state=e.getStateChange();
        		if(state==ItemEvent.SELECTED)
        		{
        			gui.setVisible(true);
        			gui.setLocation(100, 0);
        			SnapThread.g=gui.getGraphics();
        			SnapThread.state=ItemEvent.SELECTED;
        		}
        		if(state==ItemEvent.DESELECTED)
        		{
        			gui.setVisible(false);
        			SnapThread.state=ItemEvent.DESELECTED;
        		}
        	}
        });
//---------------------------------------------------------------
        about.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		JOptionPane.showMessageDialog(null,"Created by:\nDivyesh\nJigar");
        	}
        });
//----------------------------------------------------------------
        exit.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		System.exit(0);
        	}
        });
//-----------------------------------------------------------------
	}
}
