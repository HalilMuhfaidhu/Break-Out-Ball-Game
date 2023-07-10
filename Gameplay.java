package breakoutBall;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements ActionListener, KeyListener{
	
	private boolean play = false;
	private int score = 0;
	private int totalbricks = 36;
	
	//Timer
	private Timer timer;
	private int delay = 8;
	
	//position of paddle
	private int playerX = 330;
	
	//position of ball
	private int ballposX = 120;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;

	public Gameplay()
	{
		map = new MapGenerator(4,9);
	        addKeyListener(this); //to detect keys
	        setFocusable(true);
	        setFocusTraversalKeysEnabled(false);
	        timer = new Timer(delay, this);
	        timer.start(); //to start the game cycle
	}
	
	
	public void paint(Graphics g)
	{         
		// background
		g.setColor(new Color(255,248,220));
		g.fillRect(1, 1, 692, 592);
		
		//drawing map
		map.draw((Graphics2D) g);
		
		// borders
		g.setColor(new Color(178,34,34));
		g.fillRect(0, 0, 5, 598);
		g.fillRect(0, 0, 700, 5);
		g.fillRect(694, 0, 9, 598);
		
		// the scores 		
		g.setColor(new Color(160,82,45));
		g.setFont(new Font("serif",Font.BOLD, 25));
		g.drawString(""+score, 590,40);
		
		// the paddle
		g.setColor(new Color(0,206,209));
		g.fillRect(playerX, 500, 120, 8);
		
		// the ball
		g.setColor(new Color(186,85,211));
		g.fillOval(ballposX, ballposY, 30, 30);
		

		if(totalbricks <= 0)
		{
			play = false;
		    ballXdir = 0;
		    ballYdir = 0;
		    g.setColor(new Color(219,112,147));
		    g.setFont(new Font("serif",Font.BOLD, 30));
		    g.drawString("Congrats, You Win!", 260, 300);
		             
		    g.setColor(new Color(188,143,143));
		    g.setFont(new Font("serif",Font.BOLD, 20));           
		    g.drawString("Press (Enter) to Restart", 230,350);  
		}
				
		// when you lose the game
		if(ballposY > 570)
		{
			play = false;
		    ballXdir = 0;
		    ballYdir = 0;
		    g.setColor(new Color(219,112,147));
		    g.setFont(new Font("serif",Font.BOLD, 30));
		    g.drawString("Game Over, Score: "+score, 190,300);
		             
		    g.setColor(new Color(188,143,143));
		    g.setFont(new Font("serif",Font.BOLD, 20));           
		    g.drawString("Press (Enter) to Restart", 230,350);        
		}
		
		
		
		g.dispose();
		
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	@Override
	public void keyReleased(KeyEvent e) {}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600) {
				playerX = 600;
			}
			else {
				moveRight();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10) {
				playerX = 10;
			}
			else {
				moveLeft();
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{          
			if(!play)
			{
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalbricks = 36;
				map = new MapGenerator(4, 9);
				
				repaint();
			}
        }		
			
	}

	public void moveLeft() {
		play = true;
		playerX -=15;		
	}

	public void moveRight() {
		play = true;
		playerX += 15;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		timer.start();
		if(play) {
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 500, 100, 8)))
			{
				ballYdir = -ballYdir;
				//ballXdir = -2;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 70, 500, 100, 8)))
			{
				ballYdir = -ballYdir;
				ballXdir = ballXdir + 1;
			}
			else if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX + 30, 500, 100, 8)))
			{
				ballYdir = -ballYdir;
			}
			
			// check map collision with the ball		
			A: for(int i = 0; i<map.map.length; i++)
			{
				for(int j =0; j<map.map[0].length; j++)
				{				
					if(map.map[i][j] > 0)
					{
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);					
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect))
						{					
							map.setBrickValue(0, i, j);
							score+=5;	
							totalbricks--;
							
							// when ball hit right or left of brick
							if(ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)	
							{
								ballXdir = -ballXdir;
							}
							// when ball hits top or bottom of brick
							else
							{
								ballYdir = -ballYdir;				
							}
							
							break A;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if(ballposX < 0)
			{
				ballXdir = -ballXdir;
			}
			if(ballposY < 0)
			{
				ballYdir = -ballYdir;
			}
			if(ballposX > 670)
			{
				ballXdir = -ballXdir;
			}
			
			repaint();
		}
			
		
		
	}

}