package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class Entity { // 玩家與其他角色NPC的Super

	GamePanel gp;
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, 
		   attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public BufferedImage image, image2, image3;
	public Boolean collision = false;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // 創建一個看不見且抽象的矩形
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);  // 創建一個實體攻擊的矩形
	public int solidAreaDefaultX, solidAreaDefaultY;	
	String dialogues[] = new String[20]; //拿來放角色對話的文字
	
	// STATE
	public int worldX, worldY;
	public String direction = "down";
	public int spriteNum = 1;
	int dialogueIndex = 0;
	public boolean collisionOn = false;
	public boolean invincible = false; // 角色被撞到時的無敵時間
	boolean attacking = false;
	public boolean alive = true;  // 怪物的生命呈現
	public boolean dying = false; // 怪物死亡呈現
	boolean hpBarOn = false; // 怪物血量的bar顯示
	
	// COUNTER
	public int spriteCounter = 0;
	public int actionLockCounter = 0;
	public int invincibleCounter = 0;
	public int shotAvailableCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	
	// CHARACTER STATUS 顯示角色生命
	
	public String name;
	public int speed;
	public int maxLife;	 // 角色最大血量
	public int life;	 // 角色血量
	public int maxMana;
	public int mana;
	public int ammo;
	public int level;	 // 角色等級
	public int strength; // 角色能力值-力量
	public int dexterity;// 角色能力值-敏捷
	public int attack;	 // 角色能力值-攻擊
	public int defense;  // 角色能力值-防禦
	public int exp;      // 角色經驗值
	public int nextLevelExp; // 角色到下個等級的經驗值
	public int coin; 	 // 持有的硬幣數量
	public Entity currentWeapon; // 目前持有的武器
	public Entity currentShield; // 目前持有的盾牌
	public Projectile projectile;
	
	// ITEM ATTRIBUTES
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int useCost; // 使出火球攻擊時的能量耗費量
	
	// TYPE
	public int type; // 0 = player, 1 = npc, 2 = monster 用來檢查是怪物還是npc與角色發生碰撞
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_sword = 3;
	public final int type_axe = 4;
	public final int type_shield = 5;
	public final int type_consumable = 6;

	public Entity(GamePanel gp) {
		this.gp = gp;
	}
	
	public void setAction() {}
	public void damageReaction() {}
	public void speak() {
		
		if(dialogues[dialogueIndex] == null) {
			dialogueIndex = 0; //當沒有新對話欄位時候，會返回第一句
		}
		
		gp.ui.currentDialogue = dialogues[dialogueIndex];
		dialogueIndex++;
		
		switch(gp.player.direction) {
		case "up":direction = "down";break;
		case "down":direction = "up";break;
		case "left":direction = "right";break;
		case "right":direction = "left";break;
		}
	}
	public void use(Entity entity) {}
	
	public void update() {
		
		setAction();
		
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
//		if(this.type == 2 && contactPlayer == true) {}
		if(this.type == type_monster && contactPlayer == true) {
			
			damagePlayer(attack); // 傳遞攻擊
		}
		
		
		//IF COLLISION IS FALSE, PLAYER CAN MOVE
		if(collisionOn == false) {
			
			switch(direction) {
			case "up": worldY -= speed; break;
			case "down": worldY += speed; break;
			case "left": worldX -= speed; break;
			case "right": worldX += speed; break;
			}
		}
		
		spriteCounter++;          //遊戲60秒循環一次，因此每針循環都會增加1，而到10會改變影像1變成2
		if(spriteCounter > 12) {
			if(spriteNum == 1) {
				spriteNum = 2;
			}
			else if(spriteNum ==2) {
				spriteNum = 1;
			}
			spriteCounter = 0;
		}
		
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 40) {  // 這裡指的是怪物的無敵狀態維持時間
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(shotAvailableCounter < 30) { // 在你發射龜派氣功後的30楨之後，才能在發射火球
			shotAvailableCounter++;
		}
	}
	public void damagePlayer(int attack) {
		
		if(gp.player.invincible == false) {
			// we can give damage 當角色被撞到減一次生命，同時無敵時間發動
			gp.playSE(6); // 當怪物觸碰到角色的音效
			
			int damage = attack - gp.player.defense;
			if(damage < 0) {
				damage = 0;
			}
			gp.player.life -= damage;         
			
			gp.player.invincible = true;
		}
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;  //screenX是我們在螢幕上繪製他的位置
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&           //創建遊戲的邊界
		   worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
		   worldY + gp.tileSize > gp.player.worldY - gp.player.screenX &&
		   worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) 
		{
			switch(direction) {
			case "up":
				if(spriteNum ==1) {image = up1;}
				if(spriteNum ==2) {image = up2;}
				break;
			case "down":
				if(spriteNum ==1) {image = down1;}
				if(spriteNum ==2) {image = down2;}
				break;
			case "left":
				if(spriteNum ==1) {image = left1;}
				if(spriteNum ==2) {image = left2;}
				break;
			case "right":
				if(spriteNum ==1) {image = right1;}
				if(spriteNum ==2) {image = right2;}
				break;	 
			}
			
			// Monster HP bar
			if(type == 2 && hpBarOn == true) {
				
				double oneScale = (double)gp.tileSize/maxLife; // 將bar長度除怪物最大血量
				double hpBarValue = oneScale*life;  // 看怪物血量狀態
				
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX-1, screenY-16, gp.tileSize+2, 12);
				
				g2.setColor(new Color(255,0,30));
				g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
				
				hpBarCounter++;
				
				if(hpBarCounter > 600) {
					hpBarCounter = 0;
					hpBarOn = false;
				}
			}
						
						
			if(invincible == true) { // 當角色受到攻擊，會呈現半透明狀態
				hpBarOn = true;      // 當受到攻擊時，血條呈現出來
				hpBarCounter = 0;
				changeAlpha(g2,0.4F);
			}
			if(dying == true) {
				dyingAnimation(g2);
			}
			
			g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		
			changeAlpha(g2,1F);
		
		}
	}
	// 怪物在快垂死前的血量的閃爍狀態，但你也可以設定其它的垂死方式
	public void dyingAnimation(Graphics2D g2) { 
		
		dyingCounter++;
		
		int i = 5;		
		// 每過五楨，怪物會顯示他受傷到攻擊的閃爍狀態，直到40楨怪物會消失
		if(dyingCounter <= i) { changeAlpha(g2,0f);}
		if(dyingCounter > i && dyingCounter <= i*2 ) {changeAlpha(g2,1f);}
		if(dyingCounter > i*2 && dyingCounter <= i*3 ) {changeAlpha(g2,0f);}
		if(dyingCounter > i*3 && dyingCounter <= i*4 ) {changeAlpha(g2,1f);}
		if(dyingCounter > i*4 && dyingCounter <= i*5 ) {changeAlpha(g2,0f);}
		if(dyingCounter > i*5 && dyingCounter <= i*6 ) {changeAlpha(g2,1f);}
		if(dyingCounter > i*6 && dyingCounter <= i*7 ) {changeAlpha(g2,0f);}
		if(dyingCounter > i*7 && dyingCounter <= i*8 ) {changeAlpha(g2,1f);}
		if(dyingCounter > i*8) {
			alive = false;
		}
		
	}
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}

//	統合管理所有角色的圖片，就不需要一直重複寫
	public BufferedImage setup(String imagePath, int width, int height) { //可以指定縮放大小

		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;

		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = uTool.scaleImage(image, width, height);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

}
