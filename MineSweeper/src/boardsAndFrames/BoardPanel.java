package boardsAndFrames;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/*
 * This class contains the JPanel on which the minesweeper board is created.  It holds the button array (the board) and the methods that are used to 
 * modify the view and model of the board.
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel
{
	//constants
	public static final String bombFlag = "\u2691";
	public static final String smile    = ":)";
	public static final String grimace  = ":o";
	public static final String cry      = ";(";
	
	//boards and frames
	private MainFrame parent;
	private CellButton[][] buttonArray;
	
	//constructor of the panel
	public BoardPanel(MainFrame parent, int x, int y, int mineAmount)
	{		
		this.parent = parent; //the panel gets a copy of the parent frame for top panel updating purposes
		
		setLayout(new GridLayout(x,y));
		
		addAllButtons(x,y);
		startNewGame(x,y,mineAmount);
		
		this.repaint();
	}
	
	//method used to initialize a game as well as start a new game when the user has lost
	public void startNewGame(int x, int y,int mineAmount)
	{	
		parent.resetMineNumber();
		parent.resetTime();
		parent.resetSmiley();
		
		clearButtons(x,y);
		addRandomMines(x,y,mineAmount);
		addAllMineNumbers(x,y);
	}
	
	//scrubs buttons of all formatting from a previous game
	private void clearButtons(int x, int y)
	{
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				buttonArray[i][j].setValue("");
				buttonArray[i][j].setText("");
				buttonArray[i][j].setBackground(null);
				buttonArray[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				buttonArray[i][j].setIcon(null);
				buttonArray[i][j].toggleClickable(true);
				buttonArray[i][j].setForeground(null);
				buttonArray[i][j].setOpaque(false);
			}
		}

		MyButtonListener.setFirstClick(); //resets the button listener so that the first click the user performs starts the timer
	}
	
	//creates the buttons, adds the action and mouse listener
	private void addAllButtons(int x, int y)
	{
		int numberCreated = 0;
		buttonArray = new CellButton[x][y];
		MyButtonListener mbl = new MyButtonListener(parent);
		
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{		
				buttonArray[i][j] = new CellButton(i,j);	
				buttonArray[i][j].addActionListener(mbl);
				buttonArray[i][j].addMouseListener(mbl);
				buttonArray[i][j].setActionCommand("" + numberCreated++);
				this.add(buttonArray[i][j]);
			}
		}
	}
	
	/*
	 * This method adds mine randomly to the board by first getting the number of the button.  The method then generates a random number between 1
	 * and the squares remaining (total squares - button number).  If the random number is less than or equal to the amount of mines remaining the
	 * button is given a value of x to indicate that it contains a bomb and the mine amount is decremented
	 */
	private void addRandomMines(int x, int y, int mineAmount)
	{		
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{		
				int cellNum = Integer.parseInt(buttonArray[i][j].getActionCommand()); //get the "number" of the button
				int randNum = (int)(Math.random()*((x*y)-cellNum)) + 1; //get a random number between 1 and the number of mines left
	
				if(randNum<=mineAmount) //if the generated random number falls under the amount of total mine remaining, place a mine on the board
				{
					buttonArray[i][j].setValue("X");
					mineAmount--;
				}
			}
		}
	}
	
	//adds all the numbers surrounding the mines
	private void addAllMineNumbers(int x, int y)
	{
		for (int i=0;i<x;i++)
		{
			for (int j=0;j<y;j++)
			{
				if(!buttonArray[i][j].getValue().equals("X")) setMineNumber(i,j,x,y);
			}
		}
	}
	
	//sets the mine number for the non-bomb square (or does nothing if the swuare is not touching mines
	private void setMineNumber(int row,int col,int rowMax,int colMax)
	{
		int number = 0;
		
		for (Point pt: getLegalSquares(row,col,rowMax,colMax))
		{
			if(buttonArray[pt.x][pt.y].getValue().equals("X")) number++;
		}
		
		if(number>0) buttonArray[row][col].setValue("" + number);
	}
	
	//returns a list of all legal squares (meaning on the board)
	public static ArrayList<Point> getLegalSquares(int row, int col,int rowMax, int colMax)
	{
		ArrayList<Point> legalSquares = new ArrayList<Point>();
		
		//check row above
		if(row-1>=0 && col-1>=0) legalSquares.add(new Point(row-1,col-1));
		if(row-1>=0) legalSquares.add(new Point(row-1,col));
		if(row-1>=0 && col+1<colMax) legalSquares.add(new Point(row-1,col+1));
		
		//check left and right
		if(col-1>=0) legalSquares.add(new Point(row,col-1));
		if(col+1<colMax) legalSquares.add(new Point(row,col+1));
		
		//check row below
		if(row+1<rowMax && col-1>=0) legalSquares.add(new Point(row+1,col-1));
		if(row+1<rowMax) legalSquares.add(new Point(row+1,col));
		if(row+1<rowMax && col+1<colMax) legalSquares.add(new Point(row+1,col+1));
		
		return legalSquares;
	}

	public void disableAllButtons()
	{
		for (int i=0;i<buttonArray.length;i++)
		{
			for (int j=0;j<buttonArray[0].length;j++)
			{
				buttonArray[i][j].toggleClickable(false);
			}
		}
	}
	
	//reveal all bombs if the user has lost the game
	private void showAllBombs()
	{		
		ImageIcon bomb = new ImageIcon(BoardPanel.class.getResource("bomb.png"));
		
		for (int i=0;i<buttonArray.length;i++)
		{
			for (int j=0;j<buttonArray[0].length;j++)
			{
				//if the user didnt find a bomb, reveal it
				if(buttonArray[i][j].getValue().equals("X") && !buttonArray[i][j].getText().equals(BoardPanel.bombFlag)) 
				{
					buttonArray[i][j].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					buttonArray[i][j].setIcon(bomb);
				}
				//if the user mistakenly placed a flag, highlight it yellow
				else if(buttonArray[i][j].getText().equals(BoardPanel.bombFlag) && !buttonArray[i][j].getValue().equals("X"))
				{
					buttonArray[i][j].setOpaque(true);
					buttonArray[i][j].setBackground(Color.YELLOW);
				}
			}
		}
	}
	
	/*
	 * This method is a recursive method which is called by the action listener upon button click.  The method stops when the given 
	 * button is a bomb (and if the depth is zero, ends the game there), reveals the number if the given button is hiding and number,
	 * and reveals the space and recursively calls itself if the given number is a blank space
	 */
	public void reveal(CellButton cbtn,int depth)
	{
		if(!cbtn.getText().equals(BoardPanel.bombFlag)) //if there has been a recursive call and the it reaches a flagged square STOP HERE
		{
			if(!cbtn.getValue().equals("")) //if the given button is not hiding a blank space
			{
				if(cbtn.getValue().equals("X")) //if the given button is hiding a bomb
				{
					if(depth == 0) //if the bomb was the first button clicked
					{
						cbtn.setBackground(Color.RED); //highlight the clicked bomb
						cbtn.setOpaque(true);
						showAllBombs(); //show all bombs because the game is over
						disableAllButtons();
						parent.stopTimer();
						parent.setGameOver();
						parent.setCry();
					}
				}
				else //if the given button is hiding a number
				{
					cbtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					cbtn.toggleClickable(false); //disable button and set foreground color
					cbtn.setText(cbtn.getValue()); //reveal the number
				}
				
				return;
			}
			
			//if the given button is hiding a blank space, reveal the space and disable it
			cbtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			cbtn.toggleClickable(false);
			cbtn.setText(cbtn.getValue());
			
			int row = cbtn.getXCord();
			int col = cbtn.getYCord();
			ArrayList<CellButton> adjButtons = new ArrayList<>(); //create a list of all potential revealable buttons surrounding
			
			for(Point pt: getLegalSquares(row, col, buttonArray.length, buttonArray[0].length))
			{
				if(buttonArray[pt.x][pt.y].isButtonEnabled()) adjButtons.add(buttonArray[pt.x][pt.y]);
			}
			
			for(CellButton btn:adjButtons) reveal(btn,depth+1); //recursive call
		}
	}
	
	//check if game has been won by seeing if every button that has not been clicked has been flagged
	public boolean checkForWin()
	{
		boolean won = true;
		
		for (int i=0;i<buttonArray.length;i++)
		{
			for (int j=0;j<buttonArray[0].length;j++)
			{
				if(buttonArray[i][j].isButtonEnabled() && !buttonArray[i][j].getText().equals(bombFlag)) 
				{
					won = false;
				}
			}
		}
		
		return won;
	}

	/*
	 * The shortcut reveal is a method which saves the user time when he/she has flagged the right squares.  Assuming the user has
	 * flagged the correct squares, he/she can double click on a number square and this method will "click" all of the obvious consequences
	 * of the right flag.
	 */
	public void shortcutReveal(CellButton jbtn)
	{		
		boolean falseMove = false;
		
		int cellNumber = Integer.parseInt(jbtn.getText());
		int correctMatches = 0;
		
		int row = jbtn.getXCord();
		int col = jbtn.getYCord();
		
		ArrayList<Point> legalSquares = getLegalSquares(row,col,buttonArray.length,buttonArray[0].length);
		
		for(Point pt: legalSquares)
		{
			if(buttonArray[pt.x][pt.y].getText().equals(BoardPanel.bombFlag))
			{
				if(buttonArray[pt.x][pt.y].getValue().equals("X")) ++correctMatches;
				else 
				{
					falseMove = true;
					break; //if any flag has been falsely placed, exit loop
				}
			}
		}
		
		if(!falseMove && correctMatches == cellNumber)
		{
			for (Point pt: legalSquares)
			{
				if(!buttonArray[pt.x][pt.y].getText().equals(BoardPanel.bombFlag)) reveal(buttonArray[pt.x][pt.y],0);
			}
		}
		parent.repaint();
	}
}
