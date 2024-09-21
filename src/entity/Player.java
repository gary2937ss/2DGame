package entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.OBJ_Fireball;
import object.OBJ_Key;
import object.OBJ_Rock;
import object.OBJ_Shield_Wood;
import object.OBJ_Sword_Normal;

public class Player extends Entity {

	
	KeyHandler keyH;
	public final int screenX;
	public final int screenY;
	int standCounter = 0;
	public boolean attackCanceled = false; 
	public ArrayList<Entity> inventory = new ArrayList<>();
	public final int maxInventorySize = 20; //設置商品庫存最多可以有20個
	
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		super(gp);
		
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeight/2 - (gp.tileSize/2);
		
		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
//		attackArea.width = 36;
//		attackArea.height = 36;
		
		setDefaultValues();
		getPlayerImage();
		getPlayerAttackImage();
		setItems();
	}

	public void setDefaultValues() { // 角色資訊,能力值攻擊訊息的位置

		worldX = gp.tileSize * 23;  //角色出現的起點位置
		worldY = gp.tileSize * 21;
		speed = 4;
		direction = "down";
		
		// PLAYER STATUS
		level = 1;
		maxLife = 6;
		life = maxLife;
		maxMana = 4;
		mana = maxMana;
		ammo = 10;
		strength = 1;	// The more Strength he has, the more damage he gives.
		dexterity = 1;  // The more dexterity he has, the less damage he receives.
		exp = 0;
		nextLevelExp = 5;
		coin = 0;
		currentWeapon = new OBJ_Sword_Normal(gp);
		currentShield = new OBJ_Shield_Wood(gp);
		projectile = new OBJ_Fireball(gp);
//		projectile = new OBJ_Rock(gp);
		attack = getAttack(); 	// The total attack value is decided by strength and weapon
		defense = getDefense(); // The total defense value is decided by dexterity and shield
	}
	public void setItems() {
		
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		inventory.add(new OBJ_Key(gp));
		
	}
	
	public int getAttack() {
		attackArea = currentWeapon.attackArea;
		return attack = strength * currentWeapon.attackValue; // 力量*當前武器的攻擊值 = 玩家的攻擊力
	}
	public int getDefense() {
		return defense = dexterity * currentShield.defenseValue; // 敏捷*當前盾牌的防禦值 = 玩家的防禦力
	}
	
	
	public void getPlayerImage() {
		
		up1 = setup("/player/boy_up_1",gp.tileSize,gp.tileSize);
		up2 = setup("/player/boy_up_2",gp.tileSize,gp.tileSize);
		down1 = setup("/player/boy_down_1",gp.tileSize,gp.tileSize);
		down2 = setup("/player/boy_down_2",gp.tileSize,gp.tileSize);
		left1 = setup("/player/boy_left_1",gp.tileSize,gp.tileSize);
		left2 = setup("/player/boy_left_2",gp.tileSize,gp.tileSize);
		right1 = setup("/player/boy_right_1",gp.tileSize,gp.tileSize);
		right2 = setup("/player/boy_right_2",gp.tileSize,gp.tileSize);
		
		
	}
	public void getPlayerAttackImage() {
		
		if(currentWeapon.type == type_sword) {
			attackUp1 = setup("/player/boy_attack_up_1",gp.tileSize,gp.tileSize*2);
			attackUp2 = setup("/player/boy_attack_up_2",gp.tileSize,gp.tileSize*2);
			attackDown1 = setup("/player/boy_attack_down_1",gp.tileSize,gp.tileSize*2);
			attackDown2 = setup("/player/boy_attack_down_2",gp.tileSize,gp.tileSize*2);
			attackLeft1 = setup("/player/boy_attack_left_1",gp.tileSize*2,gp.tileSize);
			attackLeft2 = setup("/player/boy_attack_left_2",gp.tileSize*2,gp.tileSize);
			attackRight1 = setup("/player/boy_attack_right_1",gp.tileSize*2,gp.tileSize);
			attackRight2 = setup("/player/boy_attack_right_2",gp.tileSize*2,gp.tileSize);
		}
		if(currentWeapon.type == type_axe) {
			attackUp1 = setup("/player/boy_axe_up_1",gp.tileSize,gp.tileSize*2);
			attackUp2 = setup("/player/boy_axe_up_2",gp.tileSize,gp.tileSize*2);
			attackDown1 = setup("/player/boy_axe_down_1",gp.tileSize,gp.tileSize*2);
			attackDown2 = setup("/player/boy_axe_down_2",gp.tileSize,gp.tileSize*2);
			attackLeft1 = setup("/player/boy_axe_left_1",gp.tileSize*2,gp.tileSize);
			attackLeft2 = setup("/player/boy_axe_left_2",gp.tileSize*2,gp.tileSize);
			attackRight1 = setup("/player/boy_axe_right_1",gp.tileSize*2,gp.tileSize);
			attackRight2 = setup("/player/boy_axe_right_2",gp.tileSize*2,gp.tileSize);
		}

	}
		
	
	public void update() { // 更新角色的條件
		
		if(attacking == true) { // 基本上在攻擊的時候繞過以下條件判斷
			attacking();
			
		}	
		else if(keyH.upPressed == true || keyH.downPressed == true ||
				keyH.leftPressed == true || keyH.rightPressed == true || keyH.enterPressed == true) {
			
			if (keyH.upPressed == true) {
				direction = "up";				
			} else if (keyH.downPressed == true) {
				direction = "down";							
			} else if (keyH.leftPressed == true) {
				direction = "left";								
			} else if (keyH.rightPressed == true) {
				direction = "right";				
			}
			// CHECK TILE COLLISION
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			// CHECK OBJECT COLLISION
			int objIndex = gp.cChecker.checkObject(this, true);
			pickUpObject(objIndex);

			// CHECK NPC COLLISION
			int npcIndex = gp.cChecker.checkEntity(this,gp.npc);
			interactNPC(npcIndex);
			
			// CHECK MONSTER COLLISION
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
			contactMonster(monsterIndex);
			
			// CHECK EVENT
			gp.eHandler.checkEvent();

			
			//IF COLLISION IS FALSE, PLAYER CAN MOVE
			if(collisionOn == false && keyH.enterPressed == false) {
				
				switch(direction) {
				case "up": worldY -= speed; break;
				case "down": worldY += speed; break;
				case "left": worldX -= speed; break;
				case "right": worldX += speed; break;
				}
			}
			// 假如我們按下Enter，但是卻沒有取消攻擊時， 就可以攻擊
			if(keyH.enterPressed == true && attackCanceled == false) { 
				gp.playSE(7);
				attacking = true;
				spriteCounter = 0;
			}
			attackCanceled = false;
			gp.keyH.enterPressed = false;
			
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
			
		}
		if(gp.keyH.shotKeyPressed == true && projectile.alive == false 
				&& shotAvailableCounter == 30 && projectile.haveResource(this) == true) { 
			//如果前一顆攻擊還在，就不能在發射。 你要有魔力值，才能夠發射火焰
			
			// SET DEFAULT COORDINATES, DIRECTION AND USER
			projectile.set(worldX, worldY, direction, true, this);
			
			// SUBTRACT THE COST (MANA, AMMO ETC.)
			projectile.subtractResource(this);
			
			// ADD IT TO THE LIST 當發出龜派氣功時
			gp.projectileList.add(projectile);
			
			shotAvailableCounter = 0;
			
			gp.playSE(10);
		}
		
		// This needs to be outside of key if statement!
		if(invincible == true) {
			invincibleCounter++;
			if(invincibleCounter > 60) {
				invincible = false;
				invincibleCounter = 0;
			}
		}
		if(shotAvailableCounter < 30) { // 在你發射龜派氣功後的30楨之後，才能在發射火球
			shotAvailableCounter++;
		}
		
	}
	
	public void attacking() {
		
		spriteCounter++;
		
		if(spriteCounter <=  5) {
			spriteNum = 1;
		}
		if(spriteCounter > 5 && spriteCounter <= 25) {
			spriteNum = 2;
		} 	
		
			// Save the current worldX, worldY, solidArea
			int currentWorldX = worldX;
			int currentWorldY = worldY;
			int solidAreaWidth = solidArea.width;
			int solidAreaHeight = solidArea.height;
			
			// Adjust player's worldX/Y for the attackArea
			switch(direction) {
			case "up": worldY -= attackArea.height; break;
			case "down": worldY += attackArea.height; break;
			case "left": worldX -= attackArea.width; break;
			case "right": worldX += attackArea.width; break;
			}
			// attackArea becomes solidArea
			solidArea.width = attackArea.width;
			solidArea.height = attackArea.height; // 改寫角色攻擊時候的實體大小
			
			// Check monster collision with the updated worldX, worldY and solidArea
			int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
//			 if (monsterIndex != 999) {
//		            System.out.println("Monster hit detected at index: " + monsterIndex); // 添加這行來檢查怪物碰撞
//		    }
			damageMonster(monsterIndex, attack);
			
			// After checking collision, resorte the original data
			worldX = currentWorldX;
			worldY = currentWorldY;
			solidArea.width = solidAreaWidth;
			solidArea.height = solidAreaHeight;
			
		if(spriteCounter > 25) {
			spriteNum = 1;
			spriteCounter = 0;
			attacking = false;
		}
		
		
	}
	
	public void pickUpObject(int i) {
		if(i != 999) {
			
			String text;
			
			if(inventory.size() != maxInventorySize) {
				inventory.add(gp.obj[i]);
				gp.playSE(1);
				text = "得到一個" + gp.obj[i].name + "!";
			}
			else {
				text = "You cannot carry any more!";
			}
			gp.ui.addMessage(text);
			gp.obj[i] = null;
		}		
	}			
	public void interactNPC(int i) {
		
		if(gp.keyH.enterPressed == true) {
			
			if(i != 999) {		
				attackCanceled = true;
				gp.gameState = gp.dialogueState;
				gp.npc[i].speak();	
			}		
//			else {
//				gp.playSE(7); // 聲音7的特效
//				attacking = true;	
//			}
		}
	}	
	public void contactMonster(int i) { // 被怪物碰到時
		
		if(i != 999) {
			
			if(invincible == false && gp.monster[i].dying == false) {
				gp.playSE(6);
				  
				int damage = gp.monster[i].attack - defense;
				if(damage < 0) {
					damage = 0;
				}		
				life -= damage; 
				invincible = true;
			}
						
		} 
	}
	public void damageMonster(int i, int attack) { // 看有無擊中怪物
		
		if(i != 999) {
			if(gp.monster[i].invincible == false) { //假如他們不是無敵的就false
				
				gp.playSE(5);
				
				int damage = attack - gp.monster[i].defense;
				if(damage < 0) {
					damage = 0;
				}
				gp.monster[i].life -= damage;			
				gp.ui.addMessage(damage + " 龜派氣功!"); // 顯示受到攻擊時的提示畫面
				
				gp.monster[i].invincible = true; // 被攻擊後又無敵狀態
				gp.monster[i].damageReaction();  // 怪物被攻擊時會轉向
				
				if(gp.monster[i].life <= 0) {
					gp.monster[i].dying = true;
					gp.ui.addMessage("Killed the " + gp.monster[i].name + "!");
					gp.ui.addMessage("Exp +" + gp.monster[i].exp);
					exp += gp.monster[i].exp;
					checkLevelUp();
				}
			}
		}		
	}
	
	public void checkLevelUp() { // 確定升等的狀態
		
		if(exp >= nextLevelExp) {
			
			level++;
			nextLevelExp = nextLevelExp*3; //下一個等級所需要的經驗值
			maxLife += 2;
			strength++;
			dexterity++;
			attack = getAttack();
			defense = getDefense();
			
			gp.playSE(8);
			gp.gameState = gp.dialogueState;
			gp.ui.currentDialogue = "提升至等級" + level + "\n現在"
					+"氣變的更強了!";
		}
	}
	public void seletItem() {
		int itemIndex = gp.ui.getItemIndexOnSlot();
		if(itemIndex < inventory.size()) {
			Entity selectedItem = inventory.get(itemIndex);
			
			if(selectedItem.type == type_sword || selectedItem.type == type_axe) {
				currentWeapon = selectedItem;
				attack = getAttack();
				getPlayerAttackImage();
			}
			if(selectedItem.type == type_shield) {
				currentShield = selectedItem;
				defense = getDefense();
			}
			if(selectedItem.type == type_consumable) { // 當選擇消耗品時
				
				selectedItem.use(this);
				inventory.remove(itemIndex);
			}
			
		}
	}
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		int tempScreenX = screenX;
		int tempScreenY = screenY;
		
		switch(direction) {
		case "up":
			if(attacking == false) {
				if(spriteNum ==1) {image = up1;}
				if(spriteNum ==2) {image = up2;}
			}
			if(attacking == true) {
				tempScreenY = screenY - gp.tileSize;
				if(spriteNum ==1) {image = attackUp1;}
				if(spriteNum ==2) {image = attackUp2;}
			}
			break;
		case "down":
			if(attacking == false) {
				if(spriteNum ==1) {image = down1;}
				if(spriteNum ==2) {image = down2;}
			}			
			if(attacking == true) {
				if(spriteNum ==1) {image = attackDown1;}
				if(spriteNum ==2) {image = attackDown2;}
			}
			break;
		case "left":
			if(attacking == false) {
				if(spriteNum ==1) {image = left1;}
				if(spriteNum ==2) {image = left2;}
			}			
			if(attacking == true) {
				tempScreenX = screenX - gp.tileSize;
				if(spriteNum ==1) {image = attackLeft1;}
				if(spriteNum ==2) {image = attackLeft2;}
			}
			break;
		case "right":
			if(attacking == false) {
				if(spriteNum ==1) {image = right1;}
				if(spriteNum ==2) {image = right2;}
			}			
			if(attacking == true) {
				if(spriteNum ==1) {image = attackRight1;}
				if(spriteNum ==2) {image = attackRight2;}
			}
			break;
			 
		}
		if(invincible == true) {//當角色受到攻擊，會呈現半透明狀態
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
		}
		
		g2.drawImage(image, tempScreenX, tempScreenY, null);
		
		// Reset alpha
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		// 檢查在哪個地方發生碰撞
		// DEBUG
//		g2.setFont(new Font("Arial", Font.PLAIN, 26));
//		g2.setColor(Color.white);
//		g2.drawString("Invincible:"+invincibleCounter, 10, 400);
		
		
	}

}
