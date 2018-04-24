import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class GetDraw extends Frame
{
	Graphics g;
	static boolean expired;
	static boolean draw;
	final BufferedImage im;
//	public static void main(String[] args) 
//	{
//		try{	new GetDraw();	}
//		catch(Exception e){e.printStackTrace();}
//	}
	public GetDraw() throws Exception
	{
		setSize(1366,768);
		setUndecorated(true);
		setVisible(true);
		setOpacity(0.55f);
//		g=getGraphics();
		im=(BufferedImage) createImage(1366, 768);
		g=im.getGraphics();
//		addWindowListener(new WindowAdapter()
//		{
//			public void windowClosing(WindowEvent e)
//			{
//				
//			}
//		});
		addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseMoved(MouseEvent e)
			{
				int x=e.getX();
				int y=e.getY();
				if(expired)
				{
					try {
						Rectangle r=getBounds(im);
//						g.drawRect(r.x-50, r.y-50, r.width+100, r.height+100);
						g.drawImage(im, 0, 0, 400, 400, r.x-50, r.y-50, r.width+50+r.x, r.height+50+r.y, null);
						BufferedImage t=(BufferedImage) createImage(400, 400);
						t=im.getSubimage(0,0,400,400);
						ImageIO.write(t, "jpg", new File("draw2.jpg"));
						new Match();
						
					} catch (Exception e1) {e1.printStackTrace();}
					dispose();
					ActionHandler.gesture=false;
					draw=false;
					expired=false;
				}
				if(draw)
				{
					g.fillOval(x,y,55,55);
					update(g);
				}
			}
		});
	}
	public void update(Graphics g)
	{
		g=getGraphics();
		g.drawImage(im,0,0,this);
	}
	public Rectangle getBounds(BufferedImage image)
	{
		int fx=0,fy=0,lx=0,ly=0;
		boolean first=true;
		for(int y=0;y<image.getHeight();y++)
		{
			for(int x=0;x<image.getWidth();x++)
			{
				int bcolor=image.getRGB(x,y);
				if(bcolor!=-1)
				{
					if(first){fx=lx=x;fy=ly=y;first=false;}
					else
					{
						if(fx>x)fx=x;
						if(fy>y)fy=y;
						if(lx<x)lx=x;
						if(ly<y)ly=y;
					}	
				}
			}
		}
		return new Rectangle(fx,fy,lx-fx,ly-fy);
	}
}
