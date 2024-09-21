package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import object.OBJ_Heart;
import object.OBJ_Key;
import object.OBJ_ManaCrystal;
import entity.Entity;
public class UI { //處理螢幕上所有的UI活動
	
	GamePanel gp;
	Graphics2D g2;
	Font maruMonica, purisaB;  //加上B代表是粗體字
	BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;
	public boolean messageOn = false; //當角色拿到key時，會顯示訊息提示
//	public String message = "";
//	int messageCounter = 0;
	ArrayList<String> message = new ArrayList<>();
	ArrayList<Integer> messageCounter = new ArrayList<>();
	
	public boolean gameFinished = false;
	public String currentDialogue = "";
	public int commandNum = 0;
	public int titleScreenState = 0; //進入遊戲前，要加載的狀態 0: the first screen, 1:the second screen
	public int slotCol = 0;
	public int slotRow = 0;
	

	
	public UI(GamePanel gp) {
		this.gp = gp;
		// 遊戲循環開始前實體化以下程式
		
		try {
			InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
			maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
			is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
			purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// CREATE HUD OBJECT
		Entity heart = new OBJ_Heart(gp);
		heart_full = heart.image;
		heart_half = heart.image2;
		heart_blank = heart.image3;
		Entity crystal = new OBJ_ManaCrystal(gp);
		crystal_full = crystal.image;
		crystal_blank = crystal.image2;
		
//		arial_40 = new Font("Arial", Font.PLAIN, 40);//第一個參數:字體名稱 第二個:字體樣式 第三個:字體大小
		

	}
	
	public void addMessage(String text) {
		
		message.add(text);
		messageCounter.add(0);
	}
	
	public void draw(Graphics2D g2) { 
		
		this.g2 = g2;
		g2.setFont(maruMonica);
//		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.white);
		
		// TITLE STATE
		if(gp.gameState == gp.titleState) {
			drawTitleScreen();
		}
		
		// PLAY STATE
		if(gp.gameState == gp.playState) {
			drawPlayerLife();
			drawMessage();
		}
		// PAUSE STATE
		if(gp.gameState == gp.pauseState) {
			drawPlayerLife();
			drawPauseScreen();		
		}
		// DIALOGUE STATE
		if(gp.gameState == gp.dialogueState) {
			drawPlayerLife();
			drawDialogueScreen();		
		}
		// CHARACTER STATE
		if(gp.gameState == gp.characterState) {
			drawCharacterScreen();
			drawInventory(); // 管理庫存
		}
		
	}
	public void drawPlayerLife() {
				
		int x = gp.tileSize/2;
		int y = gp.tileSize/2;
		int i = 0;
		
		// DRAW MAX LIFE
		while(i < gp.player.maxLife/2) {
			g2.drawImage(heart_blank, x, y, null);
			i++;
			x += gp.tileSize;
		}
		// RESET
		x = gp.tileSize/2;
		y = gp.tileSize/2;
		i = 0;
		
		// DRAW CURRENT LIFE
		while(i < gp.player.life) {
			g2.drawImage(heart_half, x, y, null);
			i++;
			if(i < gp.player.life) {
				g2.drawImage(heart_full, x, y, null);
			}
			i++;
			x += gp.tileSize;
		}
		// DRAW MAX MANA
		x = (gp.tileSize/2)-5;
		y = (int)(gp.tileSize*1.5);
		i = 0;
		while(i < gp.player.maxMana) {
			g2.drawImage(crystal_blank, x, y, null);
			i++;
			x += 35;
		}
		// DRAW MANA
		x = (gp.tileSize/2)-5;
		y = (int)(gp.tileSize*1.5);
		i = 0;
		while(i < gp.player.mana) {
			g2.drawImage(crystal_full, x, y, null);
			i++;
			x += 35;
		}
		
	}
	
	public void drawMessage() {
		
		int messageX = gp.tileSize;
		int messageY = gp.tileSize*4;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		
		for(int i=0; i<message.size(); i++) {
			
			g2.setColor(Color.BLACK);
			g2.drawString(message.get(i), messageX+2, messageY+2); // 讓訊息顯示出陰影
			g2.setColor(Color.white);
			g2.drawString(message.get(i), messageX, messageY);
			
			int counter = messageCounter.get(i) + 1; // messageCounter++
			messageCounter.set(i, counter); // set the counter to the array
			messageY += 50; // 顯示下一則訊息
			
			if(messageCounter.get(i) > 180) { // 訊息會顯示3秒鐘
				message.remove(i);
				messageCounter.remove(i);
			}
			
		}
	}
	
	public void drawTitleScreen() {
	    
	    // 客製化設定你的背景顏色
	    if(titleScreenState == 0) {
	        
	        g2.setColor(new Color(0,0,0));
	        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
	        
	        // TITLE NAME
	        g2.setFont(g2.getFont().deriveFont(Font.BOLD,96F));
	        String text = "七龍珠大冒險";
	        int x = getXforCenteredText(text);
	        int y = gp.tileSize*3;
	        
	        // SHADOW 標題添加陰影
	        g2.setColor(Color.gray);
	        g2.drawString(text, x+5, y+5);
	        // MAIN COLOR
	        g2.setColor(Color.white);
	        g2.drawString(text, x, y);
	        
	        // GOKU INAGE
	        x = gp.screenWidth/2 - (gp.tileSize*2)/2;
	        y += gp.tileSize*2;
	        g2.drawImage(gp.player.down1, x, y, gp.tileSize*2, gp.tileSize*2, null);
	        
	        // MENU
	        g2.setFont(g2.getFont().deriveFont(Font.BOLD,48F));
	        
	        text = "NEW GAME";
	        x = getXforCenteredText(text);
	        y += gp.tileSize*3.5;
	        g2.drawString(text, x, y);
	        if(commandNum == 0) {
	            g2.drawString(">",x-gp.tileSize,y);
	        }
	        
	        text = "LOAD GAME";
	        x = getXforCenteredText(text);
	        y += gp.tileSize;
	        g2.drawString(text, x, y);
	        if(commandNum == 1) {
	            g2.drawString(">",x-gp.tileSize,y);
	        }
	        
	        text = "QUIT";
	        x = getXforCenteredText(text);
	        y += gp.tileSize;
	        g2.drawString(text, x, y);
	        if(commandNum == 2) {
	            g2.drawString(">",x-gp.tileSize,y);
	        }
	        
	    } else if(titleScreenState == 1) {
	    	
	        // CLASS SELECTION SCREEN
	        g2.setColor(Color.white);
	        g2.setFont(g2.getFont().deriveFont(42F));
	        
	        String text = "Select your class!";
	        int x = getXforCenteredText(text);
	        int y = gp.tileSize*3;
	        g2.drawString(text, x, y);
	        
	        text = "Fighter";
	        x = getXforCenteredText(text);
	        y += gp.tileSize*3;
	        g2.drawString(text, x, y);
	        if(commandNum == 0) {
	            g2.drawString(">", x-gp.tileSize, y);
	        }
	        
	        text = "Thief"; //盜賊
	        x = getXforCenteredText(text);
	        y += gp.tileSize;
	        g2.drawString(text, x, y);
	        if(commandNum == 1) {
	            g2.drawString(">", x-gp.tileSize, y);
	        }
	        
	        text = "Sorcerer";  //巫師
	        x = getXforCenteredText(text);
	        y += gp.tileSize;
	        g2.drawString(text, x, y);
	        if(commandNum == 2) {
	            g2.drawString(">", x-gp.tileSize, y);
	        }
	        
	        text = "Back";
	        x = getXforCenteredText(text);
	        y += gp.tileSize*2;
	        g2.drawString(text, x, y);
	        if(commandNum == 3) {
	            g2.drawString(">", x-gp.tileSize, y);
	        }
	    }		
	}
		
	public void drawDialogueScreen() {
		
		// WINDOW 設置視窗的大小
		int x = gp.tileSize*2;
		int y = gp.tileSize/2;
		int width = gp.screenWidth - (gp.tileSize*4);
		int height = gp.tileSize*4;		
		drawSubWindow(x, y, width, height);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
		x += gp.tileSize;
		y += gp.tileSize;
		
		for(String line : currentDialogue.split("\n")) {
			g2.drawString(line, x, y);
			y += 40;
		}
		
		
	}
	public void drawCharacterScreen() {
		
		// CREATE A FRAME
		final int frameX = gp.tileSize;
		final int frameY = gp.tileSize;
		final int frameWidth = gp.tileSize *5;
		final int frameHeight = gp.tileSize*10;
		drawSubWindow(frameX,frameY,frameWidth,frameHeight);
		
		// TEXT
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX = frameX + 20;
		int textY = frameY + gp.tileSize;
		final int lineHeight = 35;
		
		// NAMES
		g2.drawString("Level", textX, textY);textY += lineHeight;
		g2.drawString("Life", textX, textY);textY += lineHeight;
		g2.drawString("Mana", textX, textY);textY += lineHeight;

		g2.drawString("Strength", textX, textY);textY += lineHeight;
		g2.drawString("Dexterity", textX, textY);textY += lineHeight;
		g2.drawString("Attack", textX, textY);textY += lineHeight;
		g2.drawString("Defense", textX, textY);textY += lineHeight;
		g2.drawString("Exp", textX, textY);textY += lineHeight;
		g2.drawString("Next Level", textX, textY);textY += lineHeight;
		g2.drawString("Coin", textX, textY);textY += lineHeight + 10;
		g2.drawString("Weapon", textX, textY);textY += lineHeight + 15;
		g2.drawString("Shield", textX, textY);textY += lineHeight;
		
		// VALUES
		int tailX = (frameX + frameWidth) -30;
		// Reset textY
		textY = frameY + gp.tileSize;
		String value;
		
		value = String.valueOf(gp.player.level);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.life+"/"+gp.player.maxLife);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX-25, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.mana+"/"+gp.player.maxMana);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX-25, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.strength);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.dexterity);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.attack);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.defense);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.exp);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.nextLevelExp);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		value = String.valueOf(gp.player.coin);
		textX = getXforAlignToRightText(value,tailX);
		g2.drawString(value, tailX, textY);
		textY += lineHeight;
		
		g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tileSize, textY-25, null);
		textY += gp.tileSize;
		g2.drawImage(gp.player.currentShield.down1, tailX - gp.tileSize, textY-25, null);
		
	}
	
	public void drawInventory() { //管理庫存
		
		// FRAME 視窗
		int frameX = gp.tileSize*9;
		int frameY = gp.tileSize;
		int frameWidth = gp.tileSize*6;
		int frameHeight = gp.tileSize*5;
		drawSubWindow(frameX,frameY,frameWidth,frameHeight);
		
		// SLOT 設定20個庫存槽的位置 水平槽5個 垂直槽4個
		final int slotXstart = frameX + 20;
		final int slotYstart = frameY + 20;
		int slotX = slotXstart;
		int slotY = slotYstart;
		int slotSize = gp.tileSize+3;
		
		// DRAW PLAYER'S ITEMS
		for(int i=0; i <gp.player.inventory.size(); i++) {
			
			// EQUIP CURSOR 裝備
			if(gp.player.inventory.get(i) == gp.player.currentWeapon ||
					gp.player.inventory.get(i) == gp.player.currentShield) {
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
			}
			g2.drawImage(gp.player.inventory.get(i).down1, slotX, slotY, null);
			
			
			slotX += gp.tileSize;
			
			if(i ==4 || i == 9 || i == 14) {
				slotX = slotXstart;
				slotY += slotSize;
			}
			
		}
		
		// CURSOR 設定游標，水平槽5個 垂直槽4個，移動商品
		int cursorX = slotXstart + (slotSize * slotCol);
		int cursorY = slotYstart + (slotSize * slotRow);
		int cursorWidth = gp.tileSize;
		int cursorHeight = gp.tileSize;
		
		// DRAW CURSOR
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3)); // 設定邊框(Stroke)
		g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
		
		// DESCRIPTION FRAME
		int dFrameX = frameX;
		int dFrameY = frameY + frameHeight;
		int dFrameWidth = frameWidth;
		int dFrameHeight = gp.tileSize*3;
		
		
		// DRAW DESCRIPTION 
		int textX = dFrameX + 20;
		int textY = dFrameY + gp.tileSize;
		g2.setFont(g2.getFont().deriveFont(28F));
		
		int itemIndex = getItemIndexOnSlot();
		if(itemIndex < gp.player.inventory.size()) {
			
			drawSubWindow(dFrameX,dFrameY,dFrameWidth,dFrameHeight);// 你選擇一個商品時，介紹小視窗才會跳出來
			
			for(String line: gp.player.inventory.get(itemIndex).description.split("\n")){
				g2.drawString(line, textX, textY);
				textY += 32;
			}		
		}		
	}
	public int getItemIndexOnSlot() {
		int itemIndex = slotCol + (slotRow*5);
		return itemIndex;
	}
 	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c = new Color(0,0,0,180); //運用RGB繪製對話框的顏色，第四個為alpha值(調整視窗透明度)
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		c= new Color(255,255,255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
	}
	
	
	public void drawPauseScreen() {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String text = "PAUSED";
		int x = getXforCenteredText(text);	
		int y = gp.screenHeight/2;
		
		g2.drawString(text, x , y);
		
	}
	public int getXforCenteredText(String text) {
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = gp.screenWidth/2 - length/2;
		return x;
	}
	
	public int getXforAlignToRightText(String text, int tailX) {
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}
		

		
}
