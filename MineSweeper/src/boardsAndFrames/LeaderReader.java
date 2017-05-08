package boardsAndFrames;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.table.DefaultTableModel;

public class LeaderReader
{	
	public static DefaultTableModel getTableFromFile(int gameMode)
	{
		makeHomeDirectory();
		
		String fileName = null;
		
		switch(gameMode)
		{
			case MainFrame.BEGINNER:
				fileName = System.getProperty("user.home") + File.separator + "SweeperInfo" + "/LeaderBoardBeginner.txt";
				break;
			case MainFrame.INTERMEDIATE:
				fileName = System.getProperty("user.home") + File.separator + "SweeperInfo" + "/LeaderBoardIntermediate.txt";
				break;
			case MainFrame.EXPERT:
				fileName = System.getProperty("user.home") + File.separator + "SweeperInfo" + "/LeaderBoardExpert.txt";
				break;
		}
		
		DefaultTableModel jtblModel = new DefaultTableModel();
		
		jtblModel.addColumn("Place");
		jtblModel.addColumn("Name");
		jtblModel.addColumn("Time (sec)");

		Scanner scan;
		try
		{
			scan = new Scanner(new File(fileName));
			while(scan.hasNext())
			{		
				Object[] newRow = {scan.nextInt(),scan.next(),scan.nextInt()};
				jtblModel.addRow(newRow);
			}

			scan.close();
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return jtblModel;
	}
	
	public static void placeScore(int gameMode,DefaultTableModel jtblModel,int score,String name)
	{
		String fileName = null;
		
		switch(gameMode)
		{
			case MainFrame.BEGINNER:
				fileName = System.getProperty("user.home") + File.separator + "SweeperInfo" + "/LeaderBoardBeginner.txt";
				break;
			case MainFrame.INTERMEDIATE:
				fileName = System.getProperty("user.home") + File.separator + "SweeperInfo" + "/LeaderBoardIntermediate.txt";
				break;
			case MainFrame.EXPERT:
				fileName = System.getProperty("user.home") + File.separator + "SweeperInfo" + "/LeaderBoardExpert.txt";
				break;
		}
		
		for (int i=0;i<jtblModel.getRowCount();i++)
		{
			int currentScore = (int) jtblModel.getValueAt(i, 2);
			if(currentScore == 0 || score < currentScore) 
			{
				shiftAllDownFrom(i,jtblModel);
				
				jtblModel.setValueAt(score, i, 2);
				jtblModel.setValueAt(name, i, 1);
				
				writeToFile(jtblModel,fileName);
				
				break;
			}
		}
	}
	
	private static void shiftAllDownFrom(int i, DefaultTableModel jtblModel)
	{		
		for (int j=jtblModel.getRowCount()-1;j>i;j--)
		{
			jtblModel.setValueAt(jtblModel.getValueAt(j-1, 1), j, 1);
			jtblModel.setValueAt(jtblModel.getValueAt(j-1, 2), j, 2);
		}
	}

	public static void writeToFile(DefaultTableModel jtblModel, String fileName)
	{
		File toWrite = new File(fileName);
		try
		{
			FileOutputStream writer = new FileOutputStream(toWrite);
			
			for (int i=0;i<jtblModel.getRowCount();i++)
			{
				for (int j=0;j<jtblModel.getColumnCount();j++) //start at one because we dont need to write the move number
				{ 		
					writer.write(jtblModel.getValueAt(i, j).toString().getBytes());
					writer.write(" ".getBytes());
				}
				writer.write("\n".getBytes());
			}
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public static void makeHomeDirectory()
	{
		String path = System.getProperty("user.home") + File.separator + "SweeperInfo";
		File myDir = new File(path);
		
		if(myDir.exists()) 
		{
			if (new File(myDir,"LeaderBoardBeginner.txt").exists() && 
				new File(myDir,"LeaderBoardIntermediate.txt").exists() && 
				new File(myDir,"LeaderBoardExpert.txt").exists()) 
				{
					return;
				}
			else
			{
				File beginner = new File(myDir,"LeaderBoardBeginner.txt");
				File intermediate = new File(myDir,"LeaderBoardIntermediate.txt");
				File expert = new File(myDir,"LeaderBoardExpert.txt");

				try
				{
					beginner.createNewFile();
					intermediate.createNewFile();
					expert.createNewFile();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				writeNewBlank(path + File.separator + "LeaderBoardBeginner.txt");
				writeNewBlank(path + File.separator + "LeaderBoardIntermediate.txt");
				writeNewBlank(path + File.separator + "LeaderBoardExpert.txt");
			}
		}
		else
		{
			myDir.mkdirs();
			
			File beginner = new File(myDir,"LeaderBoardBeginner.txt");
			File intermediate = new File(myDir,"LeaderBoardIntermediate.txt");
			File expert = new File(myDir,"LeaderBoardExpert.txt");

			try
			{
				beginner.createNewFile();
				intermediate.createNewFile();
				expert.createNewFile();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			writeNewBlank(path + File.separator + "LeaderBoardBeginner.txt");
			writeNewBlank(path + File.separator + "LeaderBoardIntermediate.txt");
			writeNewBlank(path + File.separator + "LeaderBoardExpert.txt");
		}
	}

	private static void writeNewBlank(String fileName)
	{
		try
		{
			FileOutputStream writer = new FileOutputStream(fileName);
			
			for (int i=1;i<=10;i++)
			{
				writer.write(("" + i).getBytes());
				writer.write(" ".getBytes());
				writer.write("untitled".getBytes());
				writer.write(" ".getBytes());
				writer.write(("" + 0).getBytes());
				writer.write("\n".getBytes());
			}
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
