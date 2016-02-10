package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JOptionPane;

 
public class Missile {
	int x,y;//�ӵ���λ��
	public static final int XSPEED=10;//�ӵ��ĺ����ٶ�
	public static final int YSPEED=10;
	public static final int MWIDTH=10;//�ӵ����
	public static final int MHEIGHT=10;
	private boolean good;//���������ӵ������ͣ����ҷ����ǵз���
	private boolean mlive=true;//�����ӵ���״̬��
	public static int score=0;//�÷�~
	Direction dir;
	TankClient tc;
 
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x,int y,boolean good,Direction dir,TankClient tc){
		 this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	
	
	public void draw(Graphics g){
		if(!mlive){
			tc.missiles.remove(this);
			return;
		}
		 
		 //�趨�ӵ�����ɫ���ҷ�λ��ɫ���з�Ϊ��ɫ
		if(good){
			g.setColor(Color.RED);
			
		}else{
			g.setColor(Color.BLUE);
		}
		  g.fillOval(x, y, MWIDTH, MHEIGHT);
		  move();
	}
	private void move() {
		switch (dir) {
		case L:
			x-=XSPEED;
			break;
		case LU:
			x-=XSPEED;
			y-=YSPEED;
			break;
		case U:
			y-=YSPEED;
			break;
		case RU:
			x+=XSPEED;
			y-=YSPEED;
			break;
		case R:
			x+=XSPEED;
			break;
		case RD:
			x+=XSPEED;
			y+=YSPEED;
			break;
		case D:	
			y+=YSPEED;
			break;
		case LD:
			x-=XSPEED;
			y+=YSPEED;
			break;
		 
		default:
			break;
		}
		//�����ӵ��Ļ��Χ��������Χ����Ϊ����״̬
		if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT){
			mlive=false;
		}
	}
	 //��ȡ�ӵ����ڵľ���λ�úʹ�С
	public Rectangle getRect(){
		return new Rectangle(x,y,MWIDTH,MHEIGHT);
	}
	
	//��̹��
	public boolean hitTank(Tank t){
	 
	   if(this.isMlive()&&this.getRect().intersects(t.getRect())&&t.isTlive()&&this.good!=t.isGood()){
		   if(t.isGood()){//����Ǵ��ҷ�̹�ˣ�����ֵ����20��
			   t.setLife(t.getLife()-20);
			  if(t.getLife()<=0){//����ҷ�����ֵС��0�������ҷ�̹��Ϊ��������tlive=false��
				  t.setTlive(false);
				  int re=JOptionPane.showConfirmDialog(null,"����ˣ�ȷ���˳�");
					//JOptionPane.showMessageDialog(null, "�ɹ�!")
					if(re==JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
		   }
		   }else{//�����ǵз�̹�˵Ļ���ֱ������Ϊ����״̬
			   t.setTlive(false);
			   score++;
		   }
		   if(score>=20){
			   int re=JOptionPane.showConfirmDialog(null,"�ɹ�,ȷ���˳�!");
				if(re==JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			   t.setTlive(false);
		   }
		   this.mlive=false;//�����ӵ���״̬Ϊ������
		   
		   Explode e=new Explode(x,y,tc);//ʵ������ը
		   tc.explode.add(e);//����̹�˾͸�һ����ը��״̬
		   return true;
	   }
	   else {
		   return false;
	   }
	}
 //�з�̹��֮��Ĺ�����Ч��
	 public boolean hitTanks(List<Tank> tanks){
		 for(int i=0;i<tanks.size();i++){
			 if(hitTank(tanks.get(i))){
				 return true;
			 }
		 }
		 return false;
	 }
	
	 //�ӵ�ײǽ���ԣ�ײ��ǽ����ʧ
	 public boolean	 hitWall(Wall w){
			 if(this.mlive&&this.getRect().intersects(w.getRect())){
				 this.mlive=false;
				 return true;
			 };
			 return false;
		 }
		
	public boolean isMlive() {
				return mlive;
			}
	public void setMlive(boolean mlive) {
				this.mlive = mlive;
			}
			 
	public boolean isGood() {
				return good;
			}
	 }