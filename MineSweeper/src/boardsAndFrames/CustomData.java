package boardsAndFrames;

public class CustomData
{
	private int x;
	private int y;
	private int mineAmount;
	
	public CustomData(int x, int y, int mineAmount)
	{
		this.x = x;
		this.y = y;
		this.mineAmount = mineAmount;
	}
	
	public CustomData()
	{
		this.x = 0;
		this.y = 0;
		this.mineAmount = 0;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}

	public int getMineAmount()
	{
		return mineAmount;
	}
}
