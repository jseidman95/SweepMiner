package boardsAndFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{	
	public static final int BEGINNER     = 1;
	public static final int INTERMEDIATE = 2;
	public static final int EXPERT       = 3;
	public static final int CUSTOM       = 4;
	
	//the current game mode
	private int currentMode = BEGINNER;
	private boolean gameIsOver;
	
	//the JMenuBar components
	private JMenuBar jmb;
	private JMenu jmLevel;
	private JRadioButtonMenuItem jrbBeginner; 
	private JRadioButtonMenuItem jrbIntermediate; 
	private JRadioButtonMenuItem jrbExpert; 
	private JRadioButtonMenuItem jrbCustom;
	private JMenu jmAbout;
	private JMenuItem jmiCreator;
	private JMenuItem jmiLeaderBoard;
	
	//the variables for the components that will be placed in the top panel
	private int time = 0;
	private int mineNumber;
	private Timer gameTimer;
	private JLabel timerLabel;
	private JLabel mineLabel;
	private JButton jbtnNewGame;
	
	//the panels that will be placed on the frame
	private BoardPanel brdPanel;
	private JPanel topPanel;
	private JPanel guiPanelOne;
	private JPanel guiPanelTwo;
	private JPanel guiPanelThree;
	
	//the leaderboard dialog
	private LeaderBoardDialog lbd;
	
	private CustomData currentCustomData = null;
	
	public MainFrame()
	{
		//set up frame GUI 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
	
		//set the menus and menubar
		jmb = new JMenuBar();
		makeLevelMenu();
		makeAboutMenu();
		
		jmb.add(jmAbout);
		jmb.add(jmLevel);
		setJMenuBar(jmb);
		
		//make top panel
		topPanel = new JPanel(new GridLayout(1,3)); // create the top panel with space for the new game button, timer, and mine number
		
		//make panels to place on the top panel
		makePanelOne();
		makePanelTwo();
		makePanelThree();
		
		//add panels to top panel
		topPanel.add(guiPanelOne);
		topPanel.add(guiPanelTwo);
		topPanel.add(guiPanelThree);
		
		//make the panel that holds the board
		setGameMode(MainFrame.BEGINNER);
		
		lbd = new LeaderBoardDialog(getCurrentMode());
		
		//add panels to frame
		this.add(topPanel,BorderLayout.PAGE_START);
		this.add(brdPanel,BorderLayout.CENTER);
		
		gameIsOver = true;
		
		this.addWindowFocusListener(new WindowFocusListener()
			{
				@Override
				public void windowGainedFocus(WindowEvent e)
				{
					if(!gameIsOver) startTimer();
				}

				@Override
				public void windowLostFocus(WindowEvent e)
				{
					stopTimer();
				}
			});
		this.setVisible(true);
	}
	
	private void makeLevelMenu()
	{
		ActionListener al = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						int gameMode = 0;
						switch(e.getActionCommand())
						{
							case "Beginner":
								gameMode = MainFrame.BEGINNER;
								break;
							case "Intermediate":
								gameMode = MainFrame.INTERMEDIATE;
								break;
							case "Expert":
								gameMode = MainFrame.EXPERT;
								break;
						}
						setGameMode(gameMode);
					}
				};
		jmLevel = new JMenu("Level");
		
		ButtonGroup jrbGroup = new ButtonGroup();
		
		jrbBeginner = new JRadioButtonMenuItem("Beginner");
		jrbBeginner.addActionListener(al);
		jrbBeginner.setSelected(true);
		
		jrbIntermediate = new JRadioButtonMenuItem("Intermediate");
		jrbIntermediate.addActionListener(al);
		
		jrbExpert = new JRadioButtonMenuItem("Expert");
		jrbExpert.addActionListener(al);
		
		jrbCustom = new JRadioButtonMenuItem("Custom");
		jrbCustom.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						setCustomGame();
					}
				});
		jrbGroup.add(jrbBeginner);
		jrbGroup.add(jrbIntermediate);
		jrbGroup.add(jrbExpert);
		jrbGroup.add(jrbCustom);
		
		jmLevel.add(jrbBeginner);
		jmLevel.add(jrbIntermediate);
		jmLevel.add(jrbExpert);
		jmLevel.add(jrbCustom);
	}
	
	private void makeAboutMenu()
	{
		jmAbout = new JMenu("About");
		
		jmiCreator = new JMenuItem("About the Creator");
		jmiCreator.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						JOptionPane.showMessageDialog(MainFrame.this, "<html>Developed by Jesse Seidman <br> Contact info: seidmanjesse@gmail.com </html>");
					}
				});
		jmiLeaderBoard = new JMenuItem("Show Leaderboard");
		jmiLeaderBoard.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if(MainFrame.this.currentMode != MainFrame.CUSTOM)
						{
							lbd.changeComboBox(MainFrame.this.getCurrentMode());
							lbd.setVisible(true);
						}
						else
						{
							lbd.changeComboBox(MainFrame.BEGINNER);
							lbd.setVisible(true);
						}
					}
				});
		jmAbout.add(jmiCreator);
		jmAbout.addSeparator();
		jmAbout.add(jmiLeaderBoard);
		
	}
	
	public void setGameMode(int gameMode)
	{
		int x = 0,y = 0,mineAmount = 0;
		int frameX = 0,frameY = 0;
		
		this.currentMode = gameMode;
		
		switch(gameMode)
		{
			case MainFrame.BEGINNER:
				x = y = 8;
				mineAmount = 10;
				frameX = frameY = 325;
				break;
			case MainFrame.INTERMEDIATE:
				x = y = 16;
				mineAmount = 40;
				frameX = 700;
				frameY = 500;
				break;
			case MainFrame.EXPERT:
				x = 16;
				y = 30;
				mineAmount = 99;
				frameX = 700;
				frameY = 500;
				break;
		}
		
		makeNewGame(x,y,mineAmount,frameX,frameY);
	}
	
	public void setCustomGame()
	{
		CustomData givenData = CustomGameDialog.showCustomDialog(this);	
		if(givenData != null) 
		{
			this.currentCustomData = givenData;
			this.currentMode = MainFrame.CUSTOM;
			
			makeNewGame(givenData.getX(),givenData.getY(),givenData.getMineAmount(),0,0);
		}
		else
		{
			selectMenuItem(this.currentMode);
		}
	}
	
	public void selectMenuItem(int mode)
	{
		switch(mode)
		{
			case MainFrame.BEGINNER:
				this.jrbBeginner.setSelected(true);
				break;
			case MainFrame.INTERMEDIATE:
				this.jrbIntermediate.setSelected(true);
				break;
			case MainFrame.EXPERT:
				this.jrbExpert.setSelected(true);
				break;
			case MainFrame.CUSTOM:
				this.jrbCustom.setSelected(true);
				break;
		}
	}
	
	public void makeNewGame(int x, int y, int mineAmount, int frameX, int frameY)
	{
		if (frameX == 0 && frameY == 0)
		{
			Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
			frameX = x * 40;
			if(frameX > screenDimension.getWidth()) frameX = (int) screenDimension.getWidth();
			
			frameY = y * 16;
			if(frameY > screenDimension.getHeight()) frameY = (int) screenDimension.getHeight();
		}
		
		if(brdPanel != null) this.remove(brdPanel); //if this is not the original call, remove the current boardPanel
		brdPanel = new BoardPanel(this,x,y,mineAmount);
		this.add(brdPanel,BorderLayout.CENTER);
		
		setSize(new Dimension(frameX,frameY));
		setPreferredSize(new Dimension(frameX,frameY));
		setMinimumSize(new Dimension(frameX - 100, frameY - 100));
		
		this.gameIsOver = false;
		this.setLocationRelativeTo(null);
		this.revalidate();
		this.repaint();
		this.pack();
	}
	
	//panel one holds the mine number counter
	private void makePanelOne()
	{
		guiPanelOne = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		//this is the label that holds the number of mines remaining
		mineLabel = new JLabel();
		mineLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		mineLabel.setForeground(Color.BLUE);
		mineLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		mineLabel.setPreferredSize(new Dimension(100,30));
		mineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		guiPanelOne.add(mineLabel);
	}
	
	//panel two holds the new game button
	private void makePanelTwo()
	{
		guiPanelTwo = new JPanel();
		
		jbtnNewGame = new JButton();
		jbtnNewGame.setText(BoardPanel.smile);
		jbtnNewGame.setPreferredSize(new Dimension(30,30));
		jbtnNewGame.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int x = 0, y = 0, mineAmount = 0;
					switch(currentMode)
					{
						case MainFrame.BEGINNER:
							x = y = 8;
							mineAmount = 10;
							break;
						case MainFrame.INTERMEDIATE:
							x = y = 16;
							mineAmount = 40;
							break;
						case MainFrame.EXPERT:
							x = 16;
							y = 30;
							mineAmount = 99;
							break;
						case MainFrame.CUSTOM:
							x = MainFrame.this.currentCustomData.getX();
							y = MainFrame.this.currentCustomData.getY();
							mineAmount = MainFrame.this.currentCustomData.getMineAmount();
							break;
					}
					brdPanel.startNewGame(x, y, mineAmount);
					brdPanel.repaint();
				}
			});
		
		guiPanelTwo.add(jbtnNewGame);
	}
	
	//panel three holds the timer 
	private void makePanelThree()
	{
		guiPanelThree = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		//this label holds the timer that starts when the user clicks a square
		createTimer();
		timerLabel = new JLabel();
		timerLabel.setFont(new Font("Times New Roman",Font.BOLD,20));
		timerLabel.setForeground(Color.RED);
		timerLabel.setPreferredSize(new Dimension(100,30));
		timerLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		guiPanelThree.add(timerLabel,BorderLayout.EAST);	
	}
	
	//this method creates the timer that will be used for the game 
	private void createTimer()
	{
		gameTimer = new Timer(1000,new ActionListener() //the action listener fires every second
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						updateTime();
					}
			
				});
	}

	//increments the time by one
	private void updateTime()
	{
		timerLabel.setText("" + (++time));
	}
	
	//adds or subtracts from the number of mines, depending on input
	public void updateMineNumber(int operation)
	{
		mineNumber+=operation;
		mineLabel.setText("" + mineNumber);
	}
	
	public int getCurrentMode()
	{
		return this.currentMode;
	}
	
	public int getMineNumber()
	{
		return mineNumber;
	}
	
	public LeaderBoardDialog getLBD()
	{
		return lbd;
	}
	
	//used in a new game
	public void resetMineNumber()
	{
		switch(getCurrentMode())
		{
			case MainFrame.BEGINNER:
				mineNumber = 10;
				break;
			case MainFrame.INTERMEDIATE:
				mineNumber = 40;
				break;
			case MainFrame.EXPERT:
				mineNumber = 99;
				break;
			case MainFrame.CUSTOM:
				mineNumber = this.currentCustomData.getMineAmount();
				break;
		}
		
		mineLabel.setText("" + mineNumber);
	}
	
	public String getSmiley()
	{
		return jbtnNewGame.getText();
	}
	public void resetSmiley()
	{
		jbtnNewGame.setText(BoardPanel.smile);
	}
	
	public void setSmuggy()
	{
		jbtnNewGame.setText(BoardPanel.grimace);
	}
	
	public void setCry()
	{
		jbtnNewGame.setText(BoardPanel.cry);
	}
	public BoardPanel getBoardPanel()
	{
		return brdPanel;
	}
	
	public JPanel getTopPanel()
	{
		return topPanel;
	}
	
	public void resetTime()
	{
		gameTimer.stop();
		timerLabel.setText("" + (time = 0));
	}
	
	public void stopTimer()
	{
		this.gameTimer.stop();
	}
	
	public void startTimer()
	{
		this.gameTimer.start();
	}
	
	public int getTime()
	{
		return Integer.parseInt(timerLabel.getText());
	}
	
	public boolean gameIsOver()
	{
		return this.gameIsOver;
	}
	
	public void setGameOver()
	{
		this.gameIsOver = true;
	}
	
	public void setGameStart()
	{
		this.gameIsOver = false;
	}
}
