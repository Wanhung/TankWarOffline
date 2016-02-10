package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Ѫ���࣬���˿��Լ�Ѫ
 * @author WanHung
 *
 */
public class Blood {
	int x,y,w,h;//Ѫ��Ļ�������,x,yλ��.w,h�Ǵ�С
	int step=0;//���ڼ�¼ѭ��
	private boolean live=true;//����Ѫ��Ĵ���״̬��
	TankClient tc;
	
	//Ѫ���ķ�Χ���ֶ��趨���й��ɵ����趨�ķ�Χ�ڻ
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
		//ȷ��Ѫ��λ�ã�
		x=pos[step][0];
		y=pos[step][1];
	}
	
	//Ѫ����̹����ײ���Է���
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
