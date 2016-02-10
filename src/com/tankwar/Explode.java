package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
/**
 * ��ը��
 * @author WanHung
 *
 */

public class Explode {
	  int x,y;//��ը�@ʾ��λ��
	  int step=0;//�����ڼ����ͻ��ڼ�����ֱ��Բ
	  int [] diameter={10,15,20,25,40,60,30,20,10,6,3};//����ըԲ��ֱ��
	  private boolean live=true;//��ը����״̬
	  TankClient tc;
	  
	  //���췽��
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
		  if(step==diameter.length){//�жϻ���diameter��������ô�����жϲ���
			  live=false;//��ը״̬����Ϊfalse��
			  step=0;//�����0
			  return;//����
		  }
		  g.setColor(Color.ORANGE);
		  g.fillOval(x, y, diameter[step], diameter[step]);
		  step++;
	  }
}
