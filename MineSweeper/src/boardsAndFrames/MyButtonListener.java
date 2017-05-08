package boardsAndFrames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/*
 * This class contains both a mouse and action listener.  While that seems redundant, I found this to be better because the action listener of the Button
 * is far more more responsive to clicks than the mouse listener.  However, the mouse listener was essential for the double clicks and right clicks
 */
public class MyButtonListener extends MouseAdapter implements ActionListener
{
	private MainFrame parent;  //the class gets a reference to the window it is applied to so it is able to change the frame and board
	private static boolean isFirstClick; //the first click is a special case because it starts the timer
	
	public MyButtonListener(MainFrame jfrm)
	{
		parent = jfrm;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{	
		CellButton cellButton = (CellButton) e.getSource();
		
		if(cellButton.isButtonEnabled()) 
		{
			if(SwingUtilities.isRightMouseButton(e))
			{	
				if(!cellButton.getText().equals(BoardPanel.bombFlag) && parent.getMineNumber() > 0) //if there is no flag already and there are bombs left
				{
					cellButton.setForeground(Color.RED);
					cellButton.setText(BoardPanel.bombFlag);
					parent.updateMineNumber(-1);
					parent.repaint();
					
					if(parent.getMineNumber() == 0) //if this is the last flag placed check for win
					{
						if(parent.getBoardPanel().checkForWin()) 
						{
							parent.stopTimer();
							parent.getBoardPanel().disableAllButtons();
							
							if(parent.getCurrentMode() != MainFrame.CUSTOM)
							{
								parent.getLBD().changeComboBox(parent.getCurrentMode());
								parent.getLBD().insertScore(parent.getTime());
								parent.getLBD().setVisible(true);
							}
						}
					}
				}
				else //if a flag is being remove, reset the text and increment the mine number
				{
					cellButton.setText("");
					parent.updateMineNumber(1);
					parent.repaint();
				}
			}
		}
		else 
		{    //if the button is disabled AND it is not a blank space it can potentially be shortcutted
			if(e.getClickCount() == 2  && !cellButton.getText().equals("")) 
			{
				parent.getBoardPanel().shortcutReveal(cellButton);
				
				if(parent.getMineNumber() == 0) //if this is the last flag placed check for win
				{
					if(parent.getBoardPanel().checkForWin()) 
					{
						parent.stopTimer();
						parent.getBoardPanel().disableAllButtons();
						
						parent.getLBD().changeComboBox(parent.getCurrentMode());
						parent.getLBD().insertScore(parent.getTime());
						parent.getLBD().setVisible(true);
					}
				}
			}
		}
	}	
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(!parent.getSmiley().equals(BoardPanel.cry)) parent.setSmuggy();
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(!parent.getSmiley().equals(BoardPanel.cry)) parent.resetSmiley();
	}
	
	public static void setFirstClick()
	{
		isFirstClick = true;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		CellButton cellButton = (CellButton) e.getSource();
		
		if(cellButton.isButtonEnabled())
		{
			if(isFirstClick)
			{
				parent.startTimer();
				parent.setGameStart();
				isFirstClick = false;
			}
			
			if(!cellButton.getText().equals(BoardPanel.bombFlag)) //you cant click a flag
			{
				parent.getBoardPanel().reveal(cellButton,0);
				
				if(parent.getMineNumber() == 0) //if this is the last flag placed check for win
				{
					if(parent.getBoardPanel().checkForWin()) 
					{			
						parent.stopTimer();
						parent.getBoardPanel().disableAllButtons();
						parent.setGameOver();
						
						if(parent.getCurrentMode() != MainFrame.CUSTOM)
						{
							parent.getLBD().changeComboBox(parent.getCurrentMode());
							parent.getLBD().insertScore(parent.getTime());
							parent.getLBD().setVisible(true);
						}
					}
				}
			}
			
			parent.repaint();
		}
	}
}
