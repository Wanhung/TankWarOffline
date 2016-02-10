package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

/**
 * 坦克类
 * @author WanHung
 *
 */
public class Tank {
	private int x,y;//位置坐標
	private int oldX,oldY;//记录上一步的位置
	public static final int XSPEED=5;//坦克横向速度
	public static final int YSPEED=5;//坦克纵向方向
	public static final int WIDTH=30;//坦克宽度
	public static final int HEIGHT=30;//坦克高度
	private   boolean tlive=true;//代表坦克的存活
	private int life=100;//坦克的生命值
	
	private BloodBar bb=new BloodBar();//实例化内部类BlooadBar
	
	//确定方向用！~
	private boolean bL=false;
	private boolean bR=false;
	private boolean bU=false;
	private boolean bD=false;
	TankClient tc=null;
	
	private boolean good;//用来表示坦克的类型，我方坦克还是敌方坦克
	 
	private Direction dir=Direction.STOP;//坦克初始方向为停止
	private Direction ptDir=Direction.U;//炮筒初始方向向下。
	private static Random r=new Random();//随机
	private int step=r.nextInt(10)+3;//随机记录坦克移动的步数
	
	//构造方法！
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX=x;//此时记录当前位置
		this.oldY=y;
		this.good=good;
		
	}
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
		this(x,y,good);
		this.dir=dir;
		this.tc=tc;
	}
	
	
	public void draw(Graphics g){
		if(!tlive) {
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		//开始遇到了覆盖的问题，原因是没调用一次draw()就会刷新一次背景色，所以，背景色的刷新一次就行了，用在创造自己的坦克的时候刷新背景一次就OK
		
		//如果是我方坦克，设置为红色！敌方为蓝色
		if(good){ 
			 g.setColor(Color.RED);
		 } 
		 else{
			 g.setColor(Color.BLUE);
		 }
		 g.fillOval(x,y,WIDTH,HEIGHT);
		 
		if(good){
			bb.draw(g);//给我方坦克画上红色血条
		}
		 g.setColor(Color.BLACK);//炮筒的颜色设置
		 
		 
		 //switch()用根据坦克的位置x,y和坦克的大小WIDTH,HEIGHT确定炮筒的位置
		 switch(ptDir){
	case L:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT/2);
		break;
	case LU:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y);
		break;
	case U:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y);
		break;
	case RU:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH,y);
		break;
	case R:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT/2);
		break;
	case RD:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT);
		break;
	case D:	
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y+Tank.HEIGHT);
		break;
	case LD:
		g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT);
		break;
	case STOP:
		break;
	default:
		break;
		 }
		 
		 
		 move();
	}
	
	
	public void move(){
		this.oldX=x;//记录下移动下一步前的位置
		this.oldY=y;
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
		case STOP:
			break;
		default:
			break;
		}
		
		//根据状态，重新调整炮筒方向
		if(this.dir!=Direction.STOP){
			this.ptDir=this.dir;
		}
		
		
		//防止坦克出界
		if(x<0 ){
			x=0;
			}
		if(y<30) {
			y=30;
			}
		if(x+Tank.WIDTH>TankClient.GAME_WIDTH){
			x=TankClient.GAME_WIDTH-Tank.WIDTH;
		}
		if(y+Tank.HEIGHT>TankClient.GAME_HEIGHT) {
			y=TankClient.GAME_HEIGHT-Tank.HEIGHT;
		}
		
		//如果是敌方坦克！
		if(!good){
			Direction[] dirs=Direction.values();//将enum的方向轉換成數組，生成八个整数，代表不同的方向
			if(step==0){
			step=r.nextInt(10)+8;
			int rn=r.nextInt(dirs.length);//随机产生一个数
			dir=dirs[rn];
			}
			
			step--;
			//设定开火火力
			if(r.nextInt(40)>33){
				this.fire();
			}
		}
		
	}
	
	public void keyPressed(KeyEvent e){

		int key=e.getKeyCode();
		switch (key) {
		//使坦克动起来，设置方向！
		//复活按键F2
		case KeyEvent.VK_F2:
		//判断坦克是否存活！
		if(!this.tlive){
			this.tlive=true;
			this.life=100;
		}
		break;
		 
		case KeyEvent.VK_LEFT:
			 bL=true;
			break;
		case KeyEvent.VK_RIGHT:
			bR=true;
			break;
		case KeyEvent.VK_UP:
			bU=true;
			break;
		case KeyEvent.VK_DOWN:
			bD=true;
			break;
	 
		default:
			break;
		}
	
		locateDirection();
	}
	public void keyReleased(KeyEvent e) {

		int key=e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_CONTROL://开火按钮！
			 fire();
			break;
		case KeyEvent.VK_SPACE://绝招按钮！ 火力大增
			 superFire();
			break;
		//使坦克动起来，设置方向！
		case KeyEvent.VK_LEFT:
			 bL=false;
			break;
		case KeyEvent.VK_RIGHT:
			bR=false;
			break;
		case KeyEvent.VK_UP:
			bU=false;
			break;
		case KeyEvent.VK_DOWN:
			bD=false;
			break;
	 
		default:
			break;
		}
		locateDirection();
	}
	public void locateDirection(){
		//左，非上，非右，非下====》》确定方向为左！
		if(bL && !bU && !bR && !bD) dir=Direction.L;
		else if(bL && bU && !bR && !bD) dir=Direction.LU;
		else if(!bL && bU && !bR && !bD) dir=Direction.U;
		else if(!bL && bU && bR && !bD) dir=Direction.RU;
		else if(!bL && !bU && bR && !bD) dir=Direction.R;
		else if(!bL && !bU && bR && bD) dir=Direction.RD;
		else if(!bL && !bU && !bR && bD) dir=Direction.D;
		else if(bL && !bU && !bR && bD) dir=Direction.LD;
		else if (!bL && !bU && !bR && !bD) dir=Direction.STOP;
	}
	public Missile fire(){
		if(!tlive){
			return null ;
		}
		int x=this.x+Tank.WIDTH/2-Missile.MWIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.MHEIGHT/2;
		Missile m=new Missile(x,y,good,ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	public Missile fire(Direction dir){
		if(!tlive){
			return null ;
		}
		int x=this.x+Tank.WIDTH/2-Missile.MWIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.MHEIGHT/2;
		Missile m=new Missile(x,y,good,dir,this.tc);
		tc.missiles.add(m);
		return m;
		
	}
	//超级开火
	  public void superFire(){
		  Direction [] dirs=Direction.values();
		  for(int i=0;i<8;i++){
			 fire(dirs[i]);
		  }
		  }
	//获得坦克所在的矩形位置和大小
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}

	private void stay(){
		x=oldX;
		y=oldY;
	}
	
	//碰撞测试判断，是否与墙撞了
	 public boolean collidesWithWall(Wall w){
		 if(this.tlive&&this.getRect().intersects(w.getRect())){
			 this.stay();
			 return true;
		 };
		 return false;
	 }
	 
	//碰撞测试判断，是否与其他坦克撞了
	  public boolean collidesWithTanks(List<Tank> tanks){
		 
		  for(int i=0;i<tanks.size();i++){
			  Tank t=tanks.get(i);
			  if(this!=t){
				  if(this.tlive&&t.isTlive()&&this.getRect().intersects(t.getRect())){
					  this.stay();
					  t.stay();
					  return true;
				  };
			  }
		  }
		  return false;
	  }
	
	 
	public int getLife() {
		return life;
	}
	 
	public void setLife(int life) {
		this.life = life;
	}
	
	 
	public   boolean isTlive() {
		return tlive;
	}
	 
	public void setTlive(boolean tlive) {
		this.tlive = tlive;
	}
	 
	public boolean isGood() {
		return good;
	}
	
	//内部类，用于设置我方坦克的血条
	private class BloodBar{
		public void draw(Graphics g){
			g.setColor(Color.RED);
			g.drawRect(x, y-10, 30, 10);//设置外部虚心血条的框
			//g.setColor(Color);
			int w=30*life/100;//算出里面实心血条的框的宽高
			g.fillRect(x, y-10, w, 10);
		}
	}
	//吃血块判断！
	public boolean eat(Blood b){
		 if(this.tlive&&b.isLive()&&this.getRect().intersects(b.getRect())){
			 this.life=100;
			 b.setLive(false);
			 return true;
		 };
		 return false;
	}
}
