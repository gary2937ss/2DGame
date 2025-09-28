package main;

import java.awt.Rectangle;

/**
 * 自訂事件區域矩形，額外記錄預設座標與事件觸發狀態，用於判斷特殊地圖事件。
 */
public class EventRect extends Rectangle{
	
	int eventRectDefaultX, eventRectDefaultY;
	boolean eventDone = false;

}
