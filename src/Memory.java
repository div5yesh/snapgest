import java.util.ArrayList;
import java.util.Iterator;

import com.googlecode.javacv.cpp.opencv_core.CvMat;
import com.googlecode.javacv.cpp.opencv_core.IplImage;


public class Memory 
{
	private static ArrayList<Object> al=null;
	public static void add(Object image)
	{
		if(al==null)al=new ArrayList<Object>();
		al.add(image);
	}
	public static void release()
	{
		Iterator<Object> i=al.iterator();
		while(i.hasNext())
		{
			Object o=i.next();
			if(o instanceof IplImage)
			{
				((IplImage)o).release();
			}
			if(o instanceof CvMat)
			{
				((CvMat)o).release();
			}
		}
	}
}
