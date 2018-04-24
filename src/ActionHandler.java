import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;

import com.googlecode.javacv.cpp.opencv_core.CvRect;


public class ActionHandler
{
	private Robot robot;
	static boolean gesture;
	public ActionHandler()
	{
		try{	robot=new Robot();	}catch(Exception e){System.out.println("Robot is out of battery.");}
	}
	public void click_scroll(CvRect cursor)
	{
		if(cursor!=null)
		{
			double scalex=(float)cursor.x()/1366;
			double scaley=(float)cursor.y()/768;
			double factorx=scalex*cursor.x();
			double factory=scaley*cursor.y();
			int x=cursor.x()-20 + (int)factorx*3;
			int y=cursor.y()-20 + (int)factory*3;
			robot.mouseMove(x, y);
			if(y<2)robot.mouseWheel(-2);
			if(y>766)robot.mouseWheel(2);
			if(y<2&&x<2&&!gesture)
			{
				try 
				{
					new GetDraw();
					new Timer();
					gesture=true;
				} 
				catch(Exception e){e.printStackTrace();}
			}
		}
		else System.out.println("You lost Cursor.");
	}
	public void left_click(CvRect cursor,CvRect lclick)
	{
		if(lclick!=null&&cursor!=null)
		{
			Rectangle r1=new Rectangle(cursor.x()-20,cursor.y()-20,cursor.width()+20,cursor.height()+20);
			Rectangle r2=new Rectangle(lclick.x()-20,lclick.y()-20,lclick.width()+20,lclick.height()+20);
			
			if(r1.intersects(r2))
			{
				System.out.println("click");
				robot.mousePress(InputEvent.BUTTON1_MASK);
			}
			else
				
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		else System.out.println("You lost your Thumb.");
	}
	public void gesture()
	{
		Runtime run = Runtime.getRuntime();
		try {
			run.exec("cmd.exe /C start calc");
		}catch(Exception e){}
	}
}
