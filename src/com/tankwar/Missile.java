package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JOptionPane;

 
public class Missile {
	int x,y;//子弹的位置
	public static final int XSPEED=10;//子弹的横向速度
	public static final int YSPEED=10;
	public static final int MWIDTH=10;//子弹宽度
	public static final int MHEIGHT=10;
	private boolean good;//用来代表子弹的类型，是我方还是敌方！
	private boolean mlive=true;//定义子弹的状态，
	public static int score=0;//得分~
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
		 
		 //设定子弹的颜色，我方位红色，敌方为蓝色
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
		//设置子弹的活动范围，超过范围设置为死亡状态
		if(x<0||y<0||x>TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT){
			mlive=false;
		}
	}
	 //获取子弹所在的矩形位置和大小
	public Rectangle getRect(){
		return new Rectangle(x,y,MWIDTH,MHEIGHT);
	}
	
	//打坦克
	public boolean hitTank(Tank t){
	 
	   if(this.isMlive()&&this.getRect().intersects(t.getRect())&&t.isTlive()&&this.good!=t.isGood()){
		   if(t.isGood()){//如果是打到我方坦克，生命值减少20；
			   t.setLife(t.getLife()-20);
			  if(t.getLife()<=0){//如果我方生命值小于0，设置我方坦克为死亡，即tlive=false；
				  t.setTlive(false);
				  int re=JOptionPane.showConfirmDialog(null,"你挂了，确定退出");
					//JOptionPane.showMessageDialog(null, "成功!")
					if(re==JOptionPane.YES_OPTION)
					{
						System.exit(0);
					}
		   }
		   }else{//否则是敌方坦克的话，直接设置为死亡状态
			   t.setTlive(false);
			   score++;
		   }
		   if(score>=20){
			   int re=JOptionPane.showConfirmDialog(null,"成功,确定退出!");
				if(re==JOptionPane.YES_OPTION)
				{
					System.exit(0);
				}
			   t.setTlive(false);
		   }
		   this.mlive=false;//设置子弹的状态为死亡，
		   
		   Explode e=new Explode(x,y,tc);//实例化爆炸
		   tc.explode.add(e);//打到了坦克就给一个爆炸的状态
		   return true;
	   }
	   else {
		   return false;
	   }
	}
 //敌方坦克之间的攻击无效！
	 public boolean hitTanks(List<Tank> tanks){
		 for(int i=0;i<tanks.size();i++){
			 if(hitTank(tanks.get(i))){
				 return true;
			 }
		 }
		 return false;
	 }
	
	 //子弹撞墙测试，撞到墙后消失
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