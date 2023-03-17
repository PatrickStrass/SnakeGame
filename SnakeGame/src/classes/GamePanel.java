package classes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{
	public static final int SCREEN_WIDTH = 600;
	public static final int SCREEN_HEIGHT = 600;
	public static final int UNIT_SIZE = 25;
	public static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	public static final int DELAY = 120;
	ArrayList<Point> snakeParts = new ArrayList<Point>();
	int bodyParts = 3;
	int totalFruits = (SCREEN_WIDTH / UNIT_SIZE) * (SCREEN_HEIGHT / UNIT_SIZE) - bodyParts;
	int applesEaten;
	Point apple;
	char currentDirection = 'R';
	LinkedList<Character> directions = new LinkedList<>();
	boolean running = false;
	Timer timer;
	Random random;
	
	public GamePanel() {
		 random = new Random();
		 this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		 this.setBackground(Color.black);
		 this.setFocusable(true);
		 this.addKeyListener(new MyKeyAdapter()); 
		 startGame();
	}
	
	public void startGame() {
		initialization(); 
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		
		if (running) {
			/* Enable or disable to draw lines
			for(int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}
			*/
			
			g.setColor(Color.red);
			g.fillOval((int) apple.getX(), (int) apple.getY(), UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i < bodyParts; i++) {
				if(i == 0) {
					snakeParts.add(new Point());
					g.setColor(Color.green);
					g.fillRect((int) snakeParts.get(i).getX(), (int) snakeParts.get(i).getY(), UNIT_SIZE, UNIT_SIZE);
				} else {
					snakeParts.add(new Point());
					g.setColor(new Color(45, 180, 0));
					
					// Enable or disable to random colors
					//g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					
					g.fillRect((int) snakeParts.get(i).getX(), (int) snakeParts.get(i).getY(), UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			g.setColor(Color.orange);
			g.setFont(new Font("SF Pro", Font.PLAIN, 30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());
		} else if(applesEaten == totalFruits)
			won(g);
		else
			gameOver(g);
	}
	
	public void newApple() {
		Point randomPoint = new Point(random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE,
				random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE);
		
		for(int i = 0; i < bodyParts; i++) {
			if(randomPoint.equals(snakeParts.get(i))) {
				randomPoint.setLocation(random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE, random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE);
				i = 0;
			}
		}
		apple = randomPoint;
	}
	
	public void move() {
		for(int i = bodyParts; i > 0; i--)
			snakeParts.get(i).setLocation(snakeParts.get(i - 1).getX(), snakeParts.get(i - 1).getY());
		
		if (!directions.isEmpty())
			currentDirection = directions.pop();
		
		switch(currentDirection) {
			case 'U':
				snakeParts.get(0).setLocation(snakeParts.get(0).getX(), snakeParts.get(0).getY() - UNIT_SIZE);
				break;
			case 'D':
				snakeParts.get(0).setLocation(snakeParts.get(0).getX(), snakeParts.get(0).getY() + UNIT_SIZE);
				break;
			case 'L':
				snakeParts.get(0).setLocation(snakeParts.get(0).getX() - UNIT_SIZE, snakeParts.get(0).getY());
				break;
			case 'R':
				snakeParts.get(0).setLocation(snakeParts.get(0).getX() + UNIT_SIZE, snakeParts.get(0).getY());
				break;
		}
	}
	
	public void checkApple() {
		if((snakeParts.get(0).getX() == apple.getX()) && (snakeParts.get(0).getY() == apple.getY())) {
			bodyParts++;
			applesEaten++;
			
			if(applesEaten == totalFruits) {
				running = false;
				return;
			}
			newApple();
		}
	}
	
	public void checkCollisions() {
		for(int i = bodyParts; i > 0; i--) {
			if (snakeParts.get(i).equals(snakeParts.get(0))) 
				running = false;
		}
		if(snakeParts.get(0).getY() < 0 || snakeParts.get(0).getY() > SCREEN_HEIGHT || snakeParts.get(0).getX() < 0 || snakeParts.get(0).getX() > SCREEN_WIDTH)
			running = false;
		
		if(!running) 
			timer.stop();
	}
	
	// Initialize the snake body array
	public void initialization() {
		for(int i = 0; i < UNIT_SIZE; i++) 
			snakeParts.add(i, new Point());
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("SF Pro", Font.PLAIN, 30));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());
		
		g.setColor(Color.red);
		g.setFont(new Font("SF Pro", Font.PLAIN, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
	}
	
	public void won(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("SF Pro", Font.PLAIN, 30));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten)) / 2, g.getFont().getSize());
		
		g.setColor(Color.yellow);
		g.setFont(new Font("SF Pro", Font.PLAIN, 60));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("You win!", (SCREEN_WIDTH - metrics2.stringWidth("You win!")) / 2, SCREEN_HEIGHT / 2);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {	
			switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
            	if(currentDirection != 'L')
            		directions.add('R');
                break;
            case KeyEvent.VK_LEFT:
            	if(currentDirection != 'R')
            		directions.add('L');
                break;
            case KeyEvent.VK_UP:
            	if(currentDirection != 'D')
            		directions.add('U');
                break;
            case KeyEvent.VK_DOWN:
            	if(currentDirection != 'U')
            		directions.add('D');
                break;
			}
		}
	}
}