package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
/**
 * 墙类
 * @author WanHung
 */

public class Wall {
	int x,y,w,h;//墙的基本属性
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
	//获得墙体所在的矩形
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
}
