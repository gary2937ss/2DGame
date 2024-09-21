package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener; //用來接收鍵盤輸入的事件

public class KeyHandler implements KeyListener {

	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed;

	// DEBUG
	boolean showDebugText = false;

	// 遊戲暫停
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) { // 按著按鍵的方法

		int code = e.getKeyCode();

		// TITLE STATE
		if (gp.gameState == gp.titleState) {
			titleState(code);
		}
		// PLAY STATE
		else if (gp.gameState == gp.playState) {
			playState(code);
		}
		// PAUSE STATE 暫停功能
		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		// DIALOGUE STATE 對話狀態
		else if (gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}
		// CHARACTER STATE 角色狀態
		else if (gp.gameState == gp.characterState) {
			characterState(code);
		}
	}
	public void titleState(int code) {
		
		if (code == KeyEvent.VK_W) { // W,S,A,D 用於鍵盤控制方向移動
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) {
				gp.ui.commandNum = 2;
			}
		}
		if (code == KeyEvent.VK_S) {
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 2) {
				gp.ui.commandNum = 0;
			}
		}
		if (code == KeyEvent.VK_ENTER) {
			if (gp.ui.commandNum == 0) { // 如果是0表示我們正在選擇
				gp.gameState = gp.playState;
			}
			if (gp.ui.commandNum == 1) { // 1正在加載遊戲
				// add later
			}
			if (gp.ui.commandNum == 2) { // 選擇2後將退出遊戲
				System.exit(0);

			}
		}
	}	
	public void playState(int code) {
		
		if (code == KeyEvent.VK_W) { // W,S,A,D 用於鍵盤控制方向移動
			upPressed = true;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
		if (code == KeyEvent.VK_P) { // 將P設置為暫停
			gp.gameState = gp.pauseState;
		}
		if (code == KeyEvent.VK_C) {
			gp.gameState = gp.characterState;
		}
		if (code == KeyEvent.VK_ENTER) { 
			enterPressed = true;
		}
		if (code == KeyEvent.VK_F) { // 將F設置為龜派氣功
			shotKeyPressed = true;
		}

		// DEBUG 以T作為快捷鍵，檢查遊戲繪製時間花了多久
		if (code == KeyEvent.VK_T) {
			if (showDebugText == false) {
				showDebugText = true;
			} 
			else if (showDebugText == true) {
				showDebugText = false;
			}
		}
		// 可以在遊戲執行的過程中，直接修改地圖內容
		if (code == KeyEvent.VK_R) {
			gp.tileM.loadMap("/maps/worldV2.txt");
		}
	}
	public void pauseState(int code) {
		
		if(code == KeyEvent.VK_P) {
			gp.gameState = gp.playState;
		}
	}
	public void dialogueState(int code) {
		
		if(code == KeyEvent.VK_ENTER) {
			gp.gameState = gp.playState;  
		}
	}
	public void characterState(int code) { //角色狀態
		if(code == KeyEvent.VK_C) {
			gp.gameState = gp.playState;  
		}
		// 這裡是寫在移動庫存游標的動作
		if(code == KeyEvent.VK_W) {
			if(gp.ui.slotRow != 0) {
				gp.ui.slotRow--;  
				gp.playSE(9);
			}
			
		}
		if(code == KeyEvent.VK_A) {
			if(gp.ui.slotCol != 0) {
				gp.ui.slotCol--;  
				gp.playSE(9);
			}
			
		}
		if(code == KeyEvent.VK_S) {
			if(gp.ui.slotRow != 3) {
				gp.ui.slotRow++;
				gp.playSE(9);
			}
			
		}
		if(code == KeyEvent.VK_D) {
			if(gp.ui.slotCol != 4) {
				gp.ui.slotCol++;
				gp.playSE(9);
			}	
		}
		if(code == KeyEvent.VK_ENTER) {
			gp.player.seletItem();
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) { // 放開按鍵的方法
		
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
		if (code == KeyEvent.VK_F) { // 將F設置為龜派氣功
			shotKeyPressed = false;
		}

	}
}
