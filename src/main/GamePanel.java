package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import entity.Entity;
import entity.Player;

import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	// GamePanel繼承JPanel可以使用所有功能。 GamePanel當作遊戲畫面
	// field
	// SCREEN SETTING 螢幕設置
	final int originalTileSize = 16; // 我先創建一個大小16*16的圖塊 (包含玩家,NPC及背景)
	final int scale = 3; // 再以3倍大小(48 pixels * 48pixels) 在螢幕上顯示此內容

	public final int tileSize = originalTileSize * scale; // 遊戲實際顯示在螢幕上的尺寸 48*48
	public final int maxScreenCol = 16; // 橫的有16塊，直的有12塊
	public final int maxScreenRow = 12; // 使螢幕比例4:3
	public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
	public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

	// WORLD SETTING
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;
	// public final int worldWidth = tileSize * maxWorldCol;
	// public final int worldHeight = tileSize * maxWorldRow;

	// FPS
	int FPS = 60;

	// SYSTEM
	TileManager tileM = new TileManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	Sound music = new Sound();
	Sound se = new Sound();

	public CollisionChecker cChecker = new CollisionChecker(this);
	public AssetSetter aSetter = new AssetSetter(this); // 實體化物件
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);
	
	Thread gameThread; // 線程可以前進或停止，保持遊戲運行

	// ENTITY AND OBJECT
	public Player player = new Player(this, keyH);
	public Entity obj[] = new Entity[10]; // 準備10個欄位，以便我們更換遊戲中的物件內容
	public Entity npc[] = new Entity[10];
	public Entity monster[] = new Entity[20];
	
	public ArrayList<Entity> projectileList = new ArrayList<>(); // 遠距離攻擊 如氣功都存放在這裡
	ArrayList<Entity> entityList = new ArrayList<>(); //目的是將所有實體類，如NPC 玩家，在存在這裡
	// GAME STATE 遊戲的狀態1:代表執行 2:代表暫停
	public int gameState;
	public final int titleState = 0; //標題設計
	public final int playState = 1;
	public final int pauseState = 2;
	public final int dialogueState = 3; //創建一個新的遊戲狀態
	public final int characterState = 4;
	
	

	
	public GamePanel() {

		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // java.awt.Dimension 封裝一個構建的寬及高度
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH); // 使GamePanel能偵測按鍵盤動作
		this.setFocusable(true);

	}

	// methods

	public void setupGame() // 遊戲開始前，運用這個方法去調用所設置的物件
	{
		aSetter.setObject();
		aSetter.setNPC();
		aSetter.setMonster();
		playMusic(0);
//		stopMusic();
		gameState = titleState;
	}

	public void startGameThread() {

		gameThread = new Thread(this); //
		gameThread.start(); // 會自動去呼叫下面的run方法
	}

	
	public void run() {

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				System.out.println("FPS:" + drawCount);
				drawCount = 0;
				timer = 0;
			}

		}
	}

	public void update() {

		if (gameState == playState) {
			// PLAYER
			player.update();
			// NPC
			for(int i = 0 ; i< npc.length ; i++) {
				if(npc[i] != null) {
					npc[i].update();
				}
			}
			// MONSTER
			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {	
					if(monster[i].alive == true && monster[i].dying == false) {
						monster[i].update();
					}
					if(monster[i].alive == false) {
						monster[i] = null;
					}
					
				}
			}
			// 龜派氣功的狀態
			for(int i = 0; i < projectileList.size(); i++) {
				if(projectileList.get(i) != null) {	
					if(projectileList.get(i).alive == true) {
						projectileList.get(i).update();
					}
					if(projectileList.get(i).alive == false) {
						projectileList.remove(i);
					}
					
				}
			}
		}
		if (gameState == pauseState) {
			// nothing甚麼都不做
		}
	}

	public void paintComponent(Graphics g) { // 呼叫方法的圖形類

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		// DEBUG 檢查遊戲繪製時間花了多久
		long drawStart = 0;
		if (keyH.showDebugText == true) {
			drawStart = System.nanoTime();
		}
		
		// TITLE SCREEN
		if(gameState == titleState) {
			ui.draw(g2);
		}
		//OTHERS
		else {
			
			// TILE
			tileM.draw(g2); // 先繪製背景在繪製角色
			
			// ADD ENTITIES TO THE LIST
			entityList.add(player);
			
			for(int i = 0; i < npc.length; i++) {
				if(npc[i] != null) {
					entityList.add(npc[i]);
				}
			}
			
			for(int i = 0; i < obj.length; i++) {
				if(obj[i] != null) {
					entityList.add(obj[i]);
				}
			}
			for(int i = 0; i < monster.length; i++) {
				if(monster[i] != null) {
					entityList.add(monster[i]);
				}
			}
			for(int i = 0; i < projectileList.size(); i++) {
				if(projectileList.get(i) != null) {
					entityList.add(projectileList.get(i));
				}
			}
			
			
			// SORT
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					
					int result = Integer.compare(e1.worldX, e2.worldY);
					return result;
				}
				
			});
			
			// DRAW ENTITIES
			for(int i = 0; i < entityList.size(); i++) {
				entityList.get(i).draw(g2);
			}
			// EMPTY ENTITY LIST 調閱完實體角色資料後再清空
			
			entityList.clear();
			
			// UI
			ui.draw(g2);
			
		}

		// DEBUG
		if (keyH.showDebugText == true) {
			long drawEnd = System.nanoTime();
			long passed = drawEnd - drawStart;
			// 用來顯示所設置角色座標位置及繪製時間等
			g2.setFont(new Font("Arial",Font.PLAIN,20));
			g2.setColor(Color.white);
			int x = 10;
			int y = 400;
			int lineHeight = 20;
			
			g2.drawString("WorldX: " + player.worldX, x, y); y += lineHeight;
			g2.drawString("WorldY: " + player.worldY, x, y); y += lineHeight;
			g2.drawString("Col: " + (player.worldX + player.solidArea.x)/tileSize, x, y); y += lineHeight;
			g2.drawString("Row: " + (player.worldY + player.solidArea.y)/tileSize, x, y); y += lineHeight;
			g2.drawString("Draw Time: " + passed, x, y); y += lineHeight;
			
			
//			System.out.println("Draw Time: " + passed);
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
