package boardsAndFrames;

import java.awt.Color;

import javax.swing.JButton;

/*
 * This class is a modified JButton class.  The added values are the stored private coordinates to keep track of the buttons place within the array
 * and the "hiddenValue" stored (in this case for the mines or mine numbers.  In addition the toggleEnabled and isButtonEnabled methods were overwritten
 * to get around the fact that a JButton usually grays out once disabled.
 */
@SuppressWarnings("serial")
public class CellButton extends JButton
{
	//the private variables particular to the CellButton Class
	private boolean enabled;
	private String hiddenValue;
	private int xCord;
	private int yCord;
	
	public CellButton(String hiddenValue,int x, int y)
	{
		this.hiddenValue = hiddenValue;
		this.xCord = x;
		this.yCord = y;
	}
	
	public CellButton(int x, int y) 
	{
		this.hiddenValue = "";
		this.xCord = x;
		this.yCord = y;
	}

	public void setValue(String hiddenValue)
	{
		this.hiddenValue = hiddenValue;
	}
	
	public String getValue()
	{
		return this.hiddenValue;
	}
	
	public void setCords(int x, int y)
	{
		this.xCord = x;
		this.yCord = y;
	}
	
	public int getXCord()
	{
		return this.xCord;
	}
	
	public int getYCord()
	{
		return this.yCord;
	}

	/*
	 * For the next two methods, I dont simply override setEnabled and isEnabled of the button class because doing that causes a lot of problems.
	 * I assume these problems stem from the fact that actionListener is on the event dispatch thread and the other methods are not
	 */
	public void toggleClickable(boolean flag)
	{
		enabled = flag;
		
		if(!flag)
		{
			Color fontColor = null;

			switch(this.getValue())
			{
				case "1":
					fontColor = Color.BLUE;
					break;
				case "2":
					fontColor = new Color(0,100,0);
					break;
				case "3":
					fontColor = Color.RED;
					break;
				case "4":
					fontColor = new Color(75,0,130);
					break;
				case "5":
					fontColor = new Color(128,0,0);
					break;
				case "6":
					fontColor = new Color(0,206,209);
					break;
				case "7":
					fontColor = Color.BLACK;
					break;
				case "8":
					fontColor = Color.DARK_GRAY;
					break;
			}

			if(!this.getText().equals(BoardPanel.bombFlag)) this.setForeground(fontColor);	
		}
	}	
	
	public boolean isButtonEnabled()
	{
		return enabled;
	}
}
