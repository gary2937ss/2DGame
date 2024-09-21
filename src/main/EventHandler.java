package main;

import java.awt.Rectangle;
import java.util.Random;

public class EventHandler {
	
	GamePanel gp;
	EventRect eventRect[][];
	
	int previousEventX, previousEventY;
	boolean canTouchEvent = true;
	
	public EventHandler(GamePanel gp) {
		this.gp = gp;
		
		eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];
		
		int col = 0;
		int row = 0;
		while(col < gp.maxWorldCol && row < gp.maxWorldRow) 
		{			
			eventRect[col][row] = new EventRect();
			eventRect[col][row].x = 23;
			eventRect[col][row].y = 23;
			eventRect[col][row].width = 2;
			eventRect[col][row].height = 2;
			eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
			eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;
			
			col++;
			if(col == gp.maxWorldCol) {
				col = 0;
				row++;
			}
		}
	
	}
	
	public void checkEvent() {
		
		// Check if the player character is more than 1 tile away from the last event
		int xDistance = Math.abs(gp.player.worldX - previousEventX); // 將結果轉絕對值，單純用來算距離多少
		int yDistance = Math.abs(gp.player.worldY - previousEventY);
		int distance = Math.max(xDistance, yDistance); // x跟y水平及垂直的距離，兩者取最大值
		if(distance > gp.tileSize) { 
			canTouchEvent = true;
		}
		if(canTouchEvent == true) {
			if(hit(27,16,"right") == true) {damagePit(27,16,gp.dialogueState);} // 檢查碰撞陷阱時
			if(hit(21,16,"left") == true) {teleport(21,16,gp.dialogueState);}  // 傳送到其他地方
			if(hit(23,12,"up") == true) {healingPool(23,12,gp.dialogueState);}  // 檢查碰到泉水時
		}
		
		
		
	}
	
	public boolean hit(int col, int row, String reqDirection) {
		
		boolean hit = false;
		
		gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x; // 得到player 目前solidArea位置
		gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
		eventRect[col][row].x = col*gp.tileSize + eventRect[col][row].x;
		eventRect[col][row].y = row*gp.tileSize + eventRect[col][row].y;
		
		if(gp.player.solidArea.intersects(eventRect[col][row]) && eventRect[col][row].eventDone == false) {
			if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
				hit = true;  // 如果發生碰撞時
				
				previousEventX = gp.player.worldX;  // 檢查角色與事件的發生
				previousEventY = gp.player.worldY;

			}
		}
		gp.player.solidArea.x = gp.player.solidAreaDefaultX; 
		gp.player.solidArea.y = gp.player.solidAreaDefaultY;
		eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
		eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;
		
		return hit;
	}
	
	public void teleport(int col, int row, int gameState) { // 踩到後被傳送
		
		// 遊戲狀態
		gp.gameState = gameState;
		gp.ui.currentDialogue = "瞬間移動!";
		
		Random rand = new Random();
		gp.player.worldX = gp.tileSize*47;		
		gp.player.worldX = gp.tileSize*38;
		eventRect[col][row].eventDone = true;
		
		// 創建一個隨機變數
//		Random rand = new Random();
		
		// 隨機產生的 X 和 Y 座標，範圍是地圖的大小
//		int randomX = rand.nextInt(25) + 8; // 26 到 38 的隨機整數
//		int randomY = rand.nextInt(50) + 9; // 9 到 11 的隨機整數
//		gp.player.worldX = gp.tileSize*randomX;
//		gp.player.worldX = gp.tileSize*randomY;
	}
		
	public void damagePit(int col, int row, int gameState) { // 在遊戲中受到傷害
		
		gp.gameState = gameState;
		gp.playSE(6);
		gp.ui.currentDialogue = "You fall into a pit!";
		gp.player.life -= 1;
		canTouchEvent = false;
	}
	
	public void healingPool(int col, int row, int gameState) {
		
		System.out.println("healing");
		
		if(gp.keyH.enterPressed == true) {
			gp.gameState = gameState;
			gp.player.attackCanceled = true;
			gp.playSE(2);
			gp.ui.currentDialogue = "Yo drink the water. \nYour life and mana have been recovered.";
			gp.player.life = gp.player.maxLife;
			gp.player.mana = gp.player.maxMana;
			gp.aSetter.setMonster(); // 當角色在池邊休息時，怪物會重生
		}
		
	}
	
	
	
	
	

}
