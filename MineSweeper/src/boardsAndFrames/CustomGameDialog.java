package boardsAndFrames;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CustomGameDialog extends JDialog
{	
	private JLabel jlblX;
	private JLabel jlblY;
	private JLabel jlblMineAmount;
	
	private JTextField jtfX;
	private JTextField jtfY;
	private JTextField jtfMineAmount;
	
	private JButton jbtnEnter;
	
	private CustomData givenData;
	
	public CustomGameDialog(JFrame parent)
	{
		this.setModal(true);
		this.setSize(300,150);
		this.setLayout(new FlowLayout());
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(parent);
		
		this.jlblX = new JLabel("Enter a width greater than 0");
		this.jlblY = new JLabel("Enter a height greater than 0");
		this.jlblMineAmount = new JLabel("Enter a legal mine amount");
		
		this.jtfX = new JTextField(2);
		this.jtfY = new JTextField(2);
		this.jtfMineAmount = new JTextField(6);
		
		this.jbtnEnter = new JButton("Enter");
		this.jbtnEnter.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						try
						{
							int x = Integer.parseInt(CustomGameDialog.this.jtfX.getText());
							int y = Integer.parseInt(CustomGameDialog.this.jtfY.getText());
							int mineAmount = Integer.parseInt(CustomGameDialog.this.jtfMineAmount.getText());
							
							if(x <= 0 || y <= 0 || mineAmount < 0 || mineAmount > x*y)
							{
								resetAllFields();
							}
							
							else
							{
								CustomGameDialog.this.givenData = new CustomData(x,y,mineAmount);
								CustomGameDialog.this.setVisible(false);
							}
						}
						catch(NumberFormatException ex)
						{
							resetAllFields();
						}
					}
				});
		
		this.add(jlblX);
		this.add(jtfX);
		
		this.add(jlblY);
		this.add(jtfY);
		
		this.add(jlblMineAmount);
		this.add(jtfMineAmount);
		
		this.add(jbtnEnter);
		
		this.setVisible(true);
	}
	
	public void resetAllFields()
	{
		this.jtfX.setText("");
		this.jtfY.setText("");
		this.jtfMineAmount.setText("");
	}
	
	public static CustomData showCustomDialog(JFrame parent)
	{
		CustomData givenData = null;
		
		CustomGameDialog cdg = new CustomGameDialog(parent);
		givenData = cdg.givenData;
		cdg.dispose();
		
		return givenData;
	}
}
