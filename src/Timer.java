
public class Timer extends Thread
{
	public Timer()
	{
		start();
	}
	public void run()
	{
		try {
			sleep(1500);
			GetDraw.draw=true;
			sleep(5000);
			GetDraw.expired=true;
		} catch (InterruptedException e) {e.printStackTrace();}
		
		
	}
}
