package object;

import entity.Entity;
import entity.Projectile;
import main.GamePanel;

public class OBJ_Fireball extends Projectile{
	
	GamePanel gp;
	
	public OBJ_Fireball(GamePanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Fireball";
		speed = 10;
		maxLife = 80; //龜派氣功的存在時間
		life = maxLife;
		attack = 2;
		useCost =1; // 使出火球攻擊時的能量耗費量
		alive = false;
		getImage();
	}
	public void getImage() {
		up1 = setup("/projectile/fireball_up_1",gp.tileSize,gp.tileSize);
		up2 = setup("/projectile/fireball_up_2",gp.tileSize,gp.tileSize);
		down1 = setup("/projectile/fireball_down_1",gp.tileSize,gp.tileSize);
		down2 = setup("/projectile/fireball_down_2",gp.tileSize,gp.tileSize);
		left1 = setup("/projectile/fireball_left_1",gp.tileSize,gp.tileSize);
		left2 = setup("/projectile/fireball_left_2",gp.tileSize,gp.tileSize);
		right1 = setup("/projectile/fireball_right_1",gp.tileSize,gp.tileSize);
		right2 = setup("/projectile/fireball_right_2",gp.tileSize,gp.tileSize);
		
	}
	public boolean haveResource(Entity user) {
		
		boolean haveResource = false;
		if(user.mana >= useCost) {
			haveResource = true;			
		}
		return haveResource;
	}
	public void subtractResource(Entity user) {
		user.mana -= useCost;
	}

}
