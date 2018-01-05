package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.util.ArrayList;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MainPanel extends JPanel implements Runnable,KeyListener,KeyboardFocusManagerPeer{

	
	public static final int WIDTH=400;
	public static final int HEIGHT=400;
	//Render
	private Graphics2D g2d;
	private BufferedImage image;
	
	
	//Game loop
	
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	//Game stuff
	private final int SIZE=10;
	private Entity head,apple;
	private ArrayList<Entity> snake;
	private int score;
	private int level;
	private boolean gameOver;
	
	//movement
	private int dx,dy;
	
	//key inpout
	
	private boolean up,right,down,left,start;
	
	public MainPanel(){
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public void addNotify(){
		super.addNotify();
		thread=new Thread(this);
		thread.start();
	}
	
	private void setFPS(int fps){
		targetTime=1000/fps;		
	}
	
	
	
	@Override
	public void run() {
		if(running)return;
		
		initialize();
		long startTime;
		long elapsed;
		long wait;
		while(running){
			startTime=System.nanoTime();
			update();
			requestRender();
			
			elapsed=System.nanoTime()-startTime;
			wait=targetTime-elapsed/1000000;
			if(wait>0){
				try{
					Thread.sleep(wait);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}

	private void update() {
		if(gameOver){
			if(start){
				setUpLevel();
			}
			return;
		}
		
		if(up&&dy==0){
			dy=-SIZE;
			dx=0;
		}
		if(down&&dy==0){
			dy=SIZE;
			dx=0;
		}
		if(left&&dx==0){
			dy=0;
			dx=-SIZE;
		}
		
		if(right&&dx==0&&dy!=0){
			dy=0;
			dx=SIZE;
		}
		if(dx!=0||dy!=0){
			for(int i=snake.size()-1;i>0;i--){
				snake.get(i).setPosition(
						snake.get(i-1).getX(),
						snake.get(i-1).getY());
			}
			head.move(dx, dy);
		}
		
		for(Entity e:snake){
			if(e.isCollision(head)){
				gameOver=true;
				break;
				
			}
		}
		if(apple.isCollision(head)){
			score++;
			setApple();
			
			Entity e=new Entity(SIZE);
			e.setPosition(-100,-100);
			snake.add(e);
			
			if(score%10==0){
				level++;
				if(level>10)level=10;
				setFPS(level*10);
			}

		}
		if(head.getX()<0)head.setX(WIDTH);
		if(head.getY()<0)head.setY(HEIGHT);
		if(head.getX()>WIDTH)head.setX(0); 
		if(head.getY()>HEIGHT)head.setY(0); 
	}
	
	private void setUpLevel(){
		snake=new ArrayList<Entity>();
		head=new Entity(SIZE);
		head.setPosition(WIDTH/2, HEIGHT/2);
		snake.add(head);
		for(int i=1;i<3;i++){
			Entity e=new Entity(SIZE);
			e.setPosition(head.getX()+(i*SIZE), head.getY());
			snake.add(e);
		}
		
		apple=new Entity(SIZE);
		setApple();
		score=0;
		gameOver=false;
		level=1;
		setFPS(level*10);
	}

	public void setApple(){
		int x=(int)(Math.random()*(WIDTH-SIZE));
		int y=(int)(Math.random()*(HEIGHT-SIZE));
		
		x=x-(x%SIZE);
		y=y-(y%SIZE);
		apple.setPosition(x,y);
		

	}
	private void requestRender() {
		render(g2d);
		Graphics g=getGraphics();
		g.drawImage(image,0,0,null);
		g.dispose();
		
	}
	
	public void render(Graphics2D g2d){
		g2d.clearRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.GREEN);
		for(Entity e:snake){
			e.render(g2d);	
		}
		g2d.setColor(Color.RED);
		apple.render(g2d);
		if(gameOver){
			g2d.drawString("GameOver",150,150);
		}
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Score= "+score+" Level: "+level, 10, 10);
		if(dx==0&&dy==0){
			g2d.drawString("READY!", 150, 150);
		}
	}

	private void initialize() {
		image=new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB );
		g2d=image.createGraphics();
		running=true;
		setUpLevel();
		
		
	}

	@Override
	public void setCurrentFocusedWindow(Window win) {
		
		
	}

	@Override
	public Window getCurrentFocusedWindow() {
		
		return null;
	}

	@Override
	public void setCurrentFocusOwner(Component comp) {
		
		
	}

	@Override
	public Component getCurrentFocusOwner() {
	
		return null;
	}

	@Override
	public void clearGlobalFocusOwner(Window activeWindow) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int k=e.getKeyCode();
		
		if(k==KeyEvent.VK_UP||k==KeyEvent.VK_W) up=true;
		if(k==KeyEvent.VK_DOWN||k==KeyEvent.VK_S) down=true;
		if(k==KeyEvent.VK_LEFT||k==KeyEvent.VK_A) left=true;
		if(k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D) right=true;
		if(k==KeyEvent.VK_ENTER) start=true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k=e.getKeyCode();
		
		if(k==KeyEvent.VK_UP||k==KeyEvent.VK_W) up=false;
		if(k==KeyEvent.VK_DOWN||k==KeyEvent.VK_S) down=false;
		if(k==KeyEvent.VK_LEFT||k==KeyEvent.VK_A) left=false;
		if(k==KeyEvent.VK_RIGHT||k==KeyEvent.VK_D) right=false;
		if(k==KeyEvent.VK_ENTER) start=false;
		
	}

}
