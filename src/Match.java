import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


public class Match extends Frame
{
	Graphics g;
	int match=0,unmatch=0;
//	public static void main(String[] args) 
//	{
//		try{	new Match();	}
//		catch(Exception e){e.printStackTrace();}
//	}
	public Match() throws Exception
	{
//		setSize(400,400);
//		setVisible(true);
//		g=getGraphics();
		double match_percent=0;
		BufferedImage base_image=ImageIO.read(new File("m.jpg"));
		BufferedImage compare_image=ImageIO.read(new File("draw2.jpg"));
		for(int y=0;y<base_image.getHeight();y++)
		{
			for(int x=0;x<base_image.getWidth();x++)
			{
				int bcolor=base_image.getRGB(x,y);
				int ccolor=compare_image.getRGB(x, y);
				if(bcolor==ccolor)match++;
				else unmatch++;
//				if(bcolor!=-1||ccolor!=-1)
//				{
//					bcolor-=ccolor;
//					compare_image.setRGB(x, y, 255);
//					ImageIO.write(compare_image, "jpg", new File("d.jpg"));
//				}
			}
		}
		double total=match+unmatch;
		match_percent=(double)(match/(total))*100;
		if(match_percent>70)
		{
			ActionHandler action=new ActionHandler();
			action.gesture();
		}
		System.out.println(match+" "+unmatch+" "+match_percent+" ");
//		g.drawImage(base_image, 0, 0, null);
//		g.drawImage(base_image,50, 50, 100, 100, 0, 0, 400, 400, null);
//		int c=im.getRGB(x, y);
		
		
	}
}
