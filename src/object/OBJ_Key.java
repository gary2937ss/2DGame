package object;

import entity.Entity;
import main.GamePanel;

public class OBJ_Key extends Entity{
	
	
	public OBJ_Key(GamePanel gp) {
		super(gp);
	
		name = "龍珠";
		down1 = setup("/objects/Key",gp.tileSize,gp.tileSize);
		description = "[" + name + "]\n四星球\n蒐集七顆龍珠可以許願。";		
	}
}