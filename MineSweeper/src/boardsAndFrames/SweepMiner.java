package boardsAndFrames;

import javax.swing.SwingUtilities;

public class SweepMiner
{
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
				{
					@Override
					public void run()
					{
						new MainFrame();
					}
				});
	}

}
