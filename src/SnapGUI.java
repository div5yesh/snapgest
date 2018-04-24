



import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



@SuppressWarnings("serial")
public class SnapGUI extends Frame
{
	public static void main(String s[]) throws Exception
	{
		new SnapGUI("SnapGest");
	}	
	public SnapGUI(String title) throws Exception
	{
		super(title);
		setSize(1024,768);
//----------------------------------------------------------------
		Launch.toTray(this);
		Launch.threads();
//----------------------------------------------------------------
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
//    			show.setState(false);    ?????????????????????????????
			}
		});
	}
}
