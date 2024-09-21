package object;


import entity.Entity;
import main.GamePanel;

public class OBJ_Boots extends Entity{
	
	GamePanel gp;
	
	public OBJ_Boots(GamePanel gp) {
		super(gp);
		name = "Boots";
		down1 = setup("/objects/Boots",gp.tileSize,gp.tileSize);
				
	}

}
