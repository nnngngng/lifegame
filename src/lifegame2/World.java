package lifegame2;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * 
 * @author Administrator
 *
 */
public class World extends JPanel implements Runnable
{
	private final transient int rows;
	private final transient int columns;
	static enum CellStatus
	{
		Active,
		Dead;
	}
	private final transient CellStatus[][] generation1;
	private final transient CellStatus[][] generation2;
	private transient CellStatus[][] currentGeneration;
	private transient CellStatus[][] nextGeneration;
	private transient boolean isChanging;
	private transient int[][] cell=new int[40][50];
	
	/**
	 * 
	 * @param rows
	 * @param columns
	 */
	public World(final int rows,final int columns)
	{
		isChanging=false;
		this.rows=rows;
		this.columns=columns;
		generation1=new CellStatus[rows][columns];
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				generation1[i][j]=CellStatus.Dead;
			}
		}
		
		generation2=new CellStatus[rows][columns];
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<columns;j++)
			{
				generation2[i][j]=CellStatus.Dead;
			}
		}
		
		currentGeneration=generation1;
		nextGeneration=generation2;
	}
	
	@Override
	/**
	 * 
	 */
	public void run()
	{
		while(true)
		{
			synchronized(this)
			{
				while(isChanging)
				{
					try
					{
						this.wait();
					}catch(InterruptedException e)
					{
					}
				}
				
				repaint();
				sleep(2);
				
				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<columns;j++)
					{
						evolve(i,j);
					}
				}
			
				CellStatus[][] temp;
				temp=currentGeneration;
				currentGeneration=nextGeneration;
				nextGeneration=temp;
				
				for(int i=0;i<rows;i++)
				{
					for(int j=0;j<columns;j++)
					{
						nextGeneration[i][j]=CellStatus.Dead;
					}
				}
			}
		}
	}
	
	@Override
	/**
	 * 
	 */
	public void paintComponent(final Graphics gra)
	{
		super.paintComponent(gra);
		
		for (int i=0;i<rows;i++) 
			for (int j=0;j<columns;j++)
				if(currentGeneration[i][j]==CellStatus.Active)
				{
					gra.fillRect(j*20,i*20,20,20);
				}
				else
					gra.drawRect(j*20,i*20,20,20);
	}
	
	/**
	 * 
	 */
	public void start()
	{
		for(int i=0;i<=39;i++)
		{
			for(int j=0;j<=49;j++)
			{
				cell[i][j]=(int)(Math.random()*2);
			}
		}
		setShape(cell);
	}
	
	/**
	 * 
	 */
	public void stop()
	{
		synchronized(this)
		{
			isChanging=true;
		}
	}
	
	/**
	 * 
	 */
	public void restart()
	{
		synchronized(this)
		{
			isChanging=false;
			this.notifyAll();
		}
	}
	
	/**
	 * 
	 * @param shape
	 */
	public void setShape(int[][] shape)
	{
		isChanging=true;
		
		int arrowsRows;
		arrowsRows=shape.length;
		int arrowsColumns;
		
		arrowsColumns=shape[0].length;
		
		final int minimumRows=arrowsRows<rows?arrowsRows:rows;
		int minimumColumns;
		
		minimumColumns=arrowsColumns<columns?arrowsColumns:columns;
		
		synchronized(this)
		{
			for(int i=0;i<rows;i++)
			{
				for(int j=0;j<columns;j++)
				{
					currentGeneration[i][j]=CellStatus.Dead;
				}
			}
			
			for(int i=0;i<minimumRows;i++)
			{
				for(int j=0;j<minimumColumns;j++)
				{
					if(shape[i][j]==1)
					{
						currentGeneration[i][j]=CellStatus.Active;
					}
				}
			}
			
			isChanging=false;
			this.notifyAll();
		}		
	}

	private void evolve(final int rows,final int columns)
	{
		int SurroundingCell;
		
		SurroundingCell=0;
		
		if(isValidCell(rows-1,columns-1)&&currentGeneration[rows-1][columns-1]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}
		
		if(isValidCell(rows,columns-1)&&currentGeneration[rows][columns-1]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}
		
		if(isValidCell(rows+1,columns-1)&&currentGeneration[rows+1][columns-1]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}

		if(isValidCell(rows+1,columns)&&currentGeneration[rows+1][columns]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}
		
		if(isValidCell(rows+1,columns+1)&&currentGeneration[rows+1][columns+1]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}

		if(isValidCell(rows,columns+1)&&currentGeneration[rows][columns+1]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}

		if(isValidCell(rows-1,columns+1)&&currentGeneration[rows-1][columns+1]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}

		if(isValidCell(rows-1,columns)&&currentGeneration[rows-1][columns]==CellStatus.Active)
		{
			SurroundingCell=SurroundingCell+1;
		}

		if(SurroundingCell==3)
		{
			nextGeneration[rows][columns]=CellStatus.Active;
		}
		else if(SurroundingCell==2)
		{
			nextGeneration[rows][columns]=currentGeneration[rows][columns];
		}
		else
		{
			nextGeneration[rows][columns]=CellStatus.Dead;
		}
	}

	private boolean isValidCell(final int rows1,final int columns1)
	{
		return rows1>=0&&rows1<rows&&columns1>=0&&columns1<columns;
	}

	private void sleep(final int tim)
	{
		try
		{
			Thread.sleep(1000*tim);
		}catch(InterruptedException e)
		{
		}
	}	
	
}