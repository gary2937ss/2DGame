package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Beans_Hp extends Entity{
	
	GamePanel gp;
	int value = 5;
	
	public OBJ_Beans_Hp(GamePanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_consumable;
		name = "仙豆";
		down1 = setup("/objects/beans_hp",gp.tileSize,gp.tileSize);
		description = "[仙豆]\n神仙樹上所摘下來的" + name +"!";
		
	}	
	public void use(Entity entity) {
		gp.gameState = gp.dialogueState;
		gp.ui.currentDialogue = "吃下一顆" + name + "!\n"
				+ "血量回復" + value + "。";
		entity.life += value;
		if(gp.player.life > gp.player.maxLife) {
			gp.player.life = gp.player.maxLife;
		}
		gp.playSE(2);
	}

}
