package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Vegeta extends Entity {

	public NPC_Vegeta(GamePanel gp) {
		super(gp);

		direction = "down";
		speed = 2;
		
		getImage();
		setDialogue();
	}

	public void getImage() {

		up1 = setup("/npc/vegeta_up_1",gp.tileSize,gp.tileSize);
		up2 = setup("/npc/vegeta_up_2",gp.tileSize,gp.tileSize);
		down1 = setup("/npc/vegeta_down_1",gp.tileSize,gp.tileSize);
		down2 = setup("/npc/vegeta_down_2",gp.tileSize,gp.tileSize);
		left1 = setup("/npc/vegeta_left_1",gp.tileSize,gp.tileSize);
		left2 = setup("/npc/vegeta_left_2",gp.tileSize,gp.tileSize);
		right1 = setup("/npc/vegeta_right_1",gp.tileSize,gp.tileSize);
		right2 = setup("/npc/vegeta_right_2",gp.tileSize,gp.tileSize);

	}
	// 讓角色儲存一些對話內容
	public void setDialogue() {
		
		dialogues[0] = "Hello, Goku! 今天天氣真好";
		dialogues[1] = "現在是想來這裡找什麼? \n什麼? 龍珠?";
		dialogues[2] = "龍珠雷達就在這裡 \n快去找尋龍珠!";
		dialogues[3] = "祝好運!";
		
	}
	// 用隨機條件式來自動控制角色上下左右走
	public void setAction() {
		
		
		actionLockCounter ++ ;
		// 重置計時器，當NPC選擇了一個方向後，他在120幀時間單位(也就是2秒)角色都不會變換方向
		if(actionLockCounter == 60) {
			Random random = new Random();
			int i = random.nextInt(100)+1; //pick up a number from 1 to 100
			
			if(i <= 25) {
				direction = "up";
			}
			if(i >25 && i <=50) {
				direction = "down";
			}
			if(i > 50 && i<=75) {
				direction = "left";
			}
			if(i>75 && i<=100) {
				direction = "right";
			}
			actionLockCounter = 0; //重置計時器
		}
				
	}
	
	public void speak() {
		
		super.speak();	
	}

	
	
	
	
	
}
