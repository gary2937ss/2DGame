package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Shield_Blue extends Entity{
	
	public OBJ_Shield_Blue(GamePanel gp) {
		super(gp);
		
		type = type_shield;
		name = "鑽石盾牌";
		down1 = setup("/objects/shield_blue",gp.tileSize,gp.tileSize);
		defenseValue = 1;
		description = "[" + name + "]\n一個被掉在地上的盾牌\n或許藏有什麼秘密?";
	}

}
