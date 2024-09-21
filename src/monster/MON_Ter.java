package monster;

import java.util.Random;

import entity.Entity;
import main.GamePanel;
import object.OBJ_Rock;

public class MON_Ter extends Entity{
	
	GamePanel gp;

	public MON_Ter(GamePanel gp) { //設置怪物的大小,血量及速度
		super(gp);
		
		this.gp = gp;
		
		type = 2;
		name = "Ter";
		speed = 3;
		maxLife = 4;
		life = maxLife;
		attack = 2;
		defense = 0; 
		exp = 2;
		projectile = new OBJ_Rock(gp);
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		getImage(); // 從建構式中調用這個方法來獲取圖片
		
	}
	public void getImage() { // 獲得怪物的圖片來進行縮放
		
		up1 = setup("/monster/ter_down_1",gp.tileSize,gp.tileSize);
		up2 = setup("/monster/ter_down_2",gp.tileSize,gp.tileSize);
		down1 = setup("/monster/ter_down_1",gp.tileSize,gp.tileSize);
		down2 = setup("/monster/ter_down_2",gp.tileSize,gp.tileSize);
		left1 = setup("/monster/ter_down_1",gp.tileSize,gp.tileSize);
		left2 = setup("/monster/ter_down_2",gp.tileSize,gp.tileSize);
		right1 = setup("/monster/ter_down_1",gp.tileSize,gp.tileSize);
		right2 = setup("/monster/ter_down_2",gp.tileSize,gp.tileSize);
	}
	
	public void setAction() { //設置怪物的行為
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
		int i = new Random().nextInt(100)+1;	
		if(i > 99 && projectile.alive == false && shotAvailableCounter == 30) {
			
			projectile.set(worldX, worldY, direction, true, this);
			gp.projectileList.add(projectile);
			shotAvailableCounter = 0;
		}
	}
	public void damageReaction() {
		
		actionLockCounter = 0;
		direction = gp.player.direction;
	}

}
