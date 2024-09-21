package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound() {
		
		// 如果沒有設置成wav音檔不僅無法開啟，甚至連角色都會無法動彈
		soundURL[0] = getClass().getResource("/sound/Adventure.wav");
		soundURL[1] = getClass().getResource("/sound/coin.wav");
		soundURL[2] = getClass().getResource("/sound/powerup.wav");
		soundURL[3] = getClass().getResource("/sound/unlock.wav");
		soundURL[4] = getClass().getResource("/sound/fanfare.wav");
		soundURL[5] = getClass().getResource("/sound/hitmonster.wav");
		soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
		soundURL[7] = getClass().getResource("/sound/swingweapon.wav");
		soundURL[8] = getClass().getResource("/sound/levelup.wav");
		soundURL[9] = getClass().getResource("/sound/cursor.wav");
		soundURL[10] = getClass().getResource("/sound/burning.wav");
	}
	
	public void setFile(int i) {
		
		try {
			//在java中打開音樂的方法
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();
			clip.open(ais);
			
		}catch(Exception e) {
			
		}
		
	}
	public void play() { //需要調用我們播放聲音file時的方法
		
		clip.start(); 
		
	}
	public void loop() { //循環播放
		
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	public void stop() { //停止聲音時
		
		clip.stop(); 
		
	}
	
}
