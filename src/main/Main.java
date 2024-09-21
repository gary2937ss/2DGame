package main;

import javax.swing.JFrame;

public class Main {
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //讓使用者可以正確關閉視窗
		window.setResizable(false);          //視窗大小固定
		window.setTitle("2D Adventure");     //名稱"2D冒險"
		
		GamePanel gamePanel =new GamePanel();
		window.add(gamePanel); //由於GamePanel是JPanel的子類別，他就像具有附加功能的JPanel
		
		window.pack();
		
		window.setLocationRelativeTo(null);  //視窗位於正中間
		window.setVisible(true);			 //視窗出現
		
		gamePanel.setupGame();
		gamePanel.startGameThread();
		
		
		
		
	}

}
