package main;
import javax.swing.*;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    final int originalTileSize = 16;
    final int scale = 3;

    public int tileSize = originalTileSize * scale;
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol;
    public int screenHeight = tileSize * maxScreenRow;
    
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWight = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;

    int FPS = 60;
    
    TileManager TileM = new TileManager(this);
    KeyHandler KeyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();

public CollisionChecker cChecker = new CollisionChecker(this);
public AssetSetter aSetter = new AssetSetter(this);
public UI ui = new UI(this);
Thread gameThread;


public Player player = new Player(this,KeyH);
public SuperObject obj[] = new SuperObject[10];


public int gameState;
public static int playState = 1;
public final int pauseState = 2;


    public GamePanel() {


        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(KeyH);
        this.setFocusable(true);
    }
    
    public void zoomInOut(int i) {
    	
    	int oldWorldWigth = tileSize * maxWorldCol;
    	tileSize += i;
    	
    	int newWorldWigth = tileSize * maxWorldCol;
    	
    	player.speed = newWorldWigth/600;
    	
    	double multiplier = (double)newWorldWigth/oldWorldWigth;
    	
    	System.out.println("tileSize " + tileSize);
    	System.out.println("" + newWorldWigth);
    	System.out.println("worldX " + player.worldX);
    	
    	double newPlayerWorldX = player.worldX * multiplier;
    	double newPlayerWorldY = player.worldY * multiplier;
    	
    	player.worldX = (int) newPlayerWorldX;
    	player.worldY = (int) newPlayerWorldY;
    }
    
    public void setUpGame() {
    	aSetter.setObject();
    	
    	playMusic(0);
    	gameState = playState;
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }
    

    @Override
//    public void run() {
//
//        double drawInterval = 1000000000/FPS;
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while (gameThread != null) {
//
//
//        long currentTime = System.nanoTime();
//            System.out.println("Местное время"+currentTime);
//            update();
//
//            repaint();
//
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();
//                remainingTime = remainingTime / 1000000;
//            if(remainingTime < 0)
//              remainingTime = 0;                    {
//            }
//                Thread.sleep((long)remainingTime);
//
//                nextDrawTime += drawInterval;
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//    }
    public void run(){

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer+= (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }
         if(timer >=1000000000){
             System.out.println("FPS:" + drawCount);
             drawCount = 0;
             timer = 0;
         }
        }
    }
    public void update(){

    	if(gameState == playState) {
    		 player.update();
        if(gameState == pauseState) {
        	
        }
    	}



    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        
        long drawStart = 0;
        if(KeyH.checkDrawTime == true) {
        	drawStart = System.nanoTime();
        }
        
       

        TileM.draw(g2);
        for(int i = 0; i < obj.length; i++) {
        	if(obj[i] != null) {
        		obj[i].draw(g2, this);
        	}
        }
        player.draw(g2);
        
        ui.draw(g2);
        
        if(KeyH.checkDrawTime == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setColor(Color.white);
            g2.drawString("DrawTime : "+passed, 10,400);
            System.out.println("Draw Time : "+passed );
        }
        


        g2.dispose();
    }
    public void playMusic(int i) {
    	
    	music.setFile(i);
    	music.play();
    	music.loop();
    	
    }
    public void stopMusic() {
    	music.stop();
    }
    public void playSE(int i) {
    	
    	se.setFile(i);
    	se.play();
    }
}


