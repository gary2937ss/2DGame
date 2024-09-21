package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Axe extends Entity{

	public OBJ_Axe(GamePanel gp) {
		super(gp);
		
		type = type_axe;
		name = "我叫斧頭";
		down1 = setup("/objects/axe", gp.tileSize,gp.tileSize);
		description = "[" + name + "]\n+99斧頭。";		
		attackValue = 2;
		attackArea.width = 30;
		attackArea.height = 30;
	}

}
