package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
/**
 * 爆炸类
 * @author WanHung
 *
 */

public class Explode {
	  int x,y;//爆炸顯示的位置
	  int step=0;//画到第几步就话第几步的直径圆
	  int [] diameter={10,15,20,25,40,60,30,20,10,6,3};//代表爆炸圆的直径
	  private boolean live=true;//爆炸存在状态
	  TankClient tc;
	  
	  //构造方法
	  public Explode(int x,int y,TankClient tc){
		  this.x=x;
		  this.y=y;
		  this.tc=tc;
	  }
	  
	  
	  public void draw(Graphics g){
		  if(!live){
			  tc.explode.remove(this);
			  return;
		  }
		  if(step==diameter.length){//判断画到diameter的最后该怎么做的判断操作
			  live=false;//爆炸状态设置为false，
			  step=0;//步骤归0
			  return;//返回
		  }
		  g.setColor(Color.ORANGE);
		  g.fillOval(x, y, diameter[step], diameter[step]);
		  step++;
	  }
}
