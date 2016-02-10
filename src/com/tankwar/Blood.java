package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * 血块类，吃了可以加血
 * @author WanHung
 *
 */
public class Blood {
	int x,y,w,h;//血块的基本属性,x,y位置.w,h是大小
	int step=0;//用于记录循环
	private boolean live=true;//设置血块的存在状态。
	TankClient tc;
	
	//血块活动的范围，手动设定，有规律的在设定的范围内活动
	private int[][] pos={
			{250,214}, {250,225}, 
			{250,236},{250,246},
			{250,257},{250,268},
			{250,279},{250,300}
			};
	
	public Blood(){
		x=pos[0][0];
		y=pos[0][1];
		w=15;
		h=15;
	}
	
	public void draw(Graphics g){
		if(!live){
			return;
		}
		g.setColor(Color.YELLOW);
		g.fillRect(x, y, w, h);
		move();
	}
	
	private void move(){
		step++;
		if(step==pos.length){
			step=0;
		}
		//确定血块位置！
		x=pos[step][0];
		y=pos[step][1];
	}
	
	//血块与坦克碰撞测试方法
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}

	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
}
