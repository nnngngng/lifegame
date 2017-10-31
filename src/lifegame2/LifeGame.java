package lifegame2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class LifeGame extends JFrame
{
	private final World world;
	
	/**
	 * 
	 * @param rows
	 * @param columns
	 */
	public LifeGame(final int rows,final int columns)
	{
		world = new World(rows, columns);
		new Thread(world).start();
		add(world);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		final LifeGame frame=new LifeGame(40, 50);
		
		final JMenuBar menu=new JMenuBar();
		frame.setJMenuBar(menu);
		
		final JMenu options=new JMenu("Options");
		menu.add(options);
		
		final JMenuItem start=options.add("Start");
		start.addActionListener(frame.new StartActionListener());
		
		final JMenuItem stop=options.add("Stop");
		stop.addActionListener(frame.new StopActionListener());
		
		final JMenuItem restart=options.add("Restart");
		restart.addActionListener(frame.new RestartActionListener());
		
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1007, 859);
		frame.setTitle("Game of Life");
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	class StartActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.start();
		}
	}
	
	class StopActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			world.stop();
    	}
	}
	
	class RestartActionListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			world.restart();
		}
		
	}
}
