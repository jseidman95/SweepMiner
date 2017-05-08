package boardsAndFrames;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * This class is the dialog which contains the leaderboard table and the jcombobox that allows the user to view the different levels for the leaderboard
 */
@SuppressWarnings("serial")
public class LeaderBoardDialog extends JDialog
{
	//components
	private JComboBox<String> jcmb;
	private JScrollPane jsp;
	private JTable jtbl;
	private int gameMode;
	
	public LeaderBoardDialog(int gameMode)
	{
		setSize(220,220);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		setModal(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		
		this.setTitle(getTitle());
		
		jcmb = new JComboBox<String>();
		jcmb.addItem("Beginner");
		jcmb.addItem("Intermediate");
		jcmb.addItem("Expert");
		jcmb.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						LeaderBoardDialog.this.gameMode = jcmb.getSelectedIndex() + 1;
						changeTable(jcmb.getSelectedIndex() + 1);
					}
				});
		
		this.gameMode = gameMode;
		jcmb.setSelectedIndex(gameMode - 1);
		
		jtbl = new JTable(LeaderReader.getTableFromFile(gameMode))
				{
					@Override
					public boolean isCellEditable(int row,int col)
					{
						return false;
					}
				};
		jsp = new JScrollPane(jtbl);
		
		add(jcmb,BorderLayout.PAGE_START);
		add(jsp,BorderLayout.CENTER);
		
		
		this.setVisible(false);
	}
	
	public void changeTable(int gameMode)
	{
		this.setTitle(getTitle());
		this.gameMode = gameMode;
		if(jtbl != null) 
		{
			jsp.remove(jtbl);
			this.remove(jsp);
		}
		
		jtbl = new JTable(LeaderReader.getTableFromFile(gameMode))
		{
			@Override
			public boolean isCellEditable(int row,int col)
			{
				return false;
			}
		};
		jsp = new JScrollPane(jtbl);
		
		this.add(jsp,BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}
	
	public void changeComboBox(int gameMode)
	{
		jcmb.setSelectedIndex(gameMode - 1);
	}
	
	public void insertScore(int score)
	{
		String name = JOptionPane.showInputDialog(this, "Input a name for the leaderboard");
		
		if(name == null) name = "Unnamed_Player";
		else name = name.replaceAll(" ", "_");
		
		LeaderReader.placeScore(gameMode, (DefaultTableModel)jtbl.getModel(), score,name);
	}
	
	public String getTitle()
	{
		String title = null;
		
		switch(gameMode)
		{
			case MainFrame.BEGINNER:
				title = "Beginner";
				break;
			case MainFrame.INTERMEDIATE:
				title = "Intermediate";
				break;
			case MainFrame.EXPERT:
				title = "Expert";
				break;
		}
		
		return title + " LeaderBoard";
	}
}
