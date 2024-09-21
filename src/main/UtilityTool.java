package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class UtilityTool { // 這裡是百寶箱，當有任何函數或功能，可以在此調用
	
	public BufferedImage scaleImage(BufferedImage original, int width, int height) {
		
		//1.創建一個空白的BufferedImage, 2.將g2要繪製的內容先保存在裡面, 3.第三行將它顯示出來
		BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
		Graphics2D g2 = scaledImage.createGraphics();
		g2.drawImage(original, 0, 0, width, height, null);
		g2.dispose();
		
		return scaledImage;
		
	}

}
