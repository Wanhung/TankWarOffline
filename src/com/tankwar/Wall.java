package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * ǽ��
 * @author WanHung
 */

public class Wall {
	int x,y,w,h;//ǽ�Ļ�������
	TankClient tc;
	public Wall(int x, int y, int w, int h, TankClient tc) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.tc = tc;
	}
	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
		
	}
	//���ǽ�����ڵľ���
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
}
