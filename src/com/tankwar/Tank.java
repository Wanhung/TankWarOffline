package com.tankwar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

/**
 * ̹����
 * @author WanHung
 *
 */
public class Tank {
	private int x,y;//λ������
	private int oldX,oldY;//��¼��һ����λ��
	public static final int XSPEED=5;//̹�˺����ٶ�
	public static final int YSPEED=5;//̹��������
	public static final int WIDTH=30;//̹�˿��
	public static final int HEIGHT=30;//̹�˸߶�
	private   boolean tlive=true;//����̹�˵Ĵ��
	private int life=100;//̹�˵�����ֵ
	
	private BloodBar bb=new BloodBar();//ʵ�����ڲ���BlooadBar
	
	//ȷ�������ã�~
	private boolean bL=false;
	private boolean bR=false;
	private boolean bU=false;
	private boolean bD=false;
	TankClient tc=null;
	
	private boolean good;//������ʾ̹�˵����ͣ��ҷ�̹�˻��ǵз�̹��
	 
	private Direction dir=Direction.STOP;//̹�˳�ʼ����Ϊֹͣ
	private Direction ptDir=Direction.U;//��Ͳ��ʼ�������¡�
	private static Random r=new Random();//���
	private int step=r.nextInt(10)+3;//�����¼̹���ƶ��Ĳ���
	
	//���췽����
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX=x;//��ʱ��¼��ǰλ��
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
		//��ʼ�����˸��ǵ����⣬ԭ����û����һ��draw()�ͻ�ˢ��һ�α���ɫ�����ԣ�����ɫ��ˢ��һ�ξ����ˣ����ڴ����Լ���̹�˵�ʱ��ˢ�±���һ�ξ�OK
		
		//������ҷ�̹�ˣ�����Ϊ��ɫ���з�Ϊ��ɫ
		if(good){ 
			 g.setColor(Color.RED);
		 } 
		 else{
			 g.setColor(Color.BLUE);
		 }
		 g.fillOval(x,y,WIDTH,HEIGHT);
		 
		if(good){
			bb.draw(g);//���ҷ�̹�˻��Ϻ�ɫѪ��
		}
		 g.setColor(Color.BLACK);//��Ͳ����ɫ����
		 
		 
		 //switch()�ø���̹�˵�λ��x,y��̹�˵Ĵ�СWIDTH,HEIGHTȷ����Ͳ��λ��
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
		this.oldX=x;//��¼���ƶ���һ��ǰ��λ��
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
		
		//����״̬�����µ�����Ͳ����
		if(this.dir!=Direction.STOP){
			this.ptDir=this.dir;
		}
		
		
		//��ֹ̹�˳���
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
		
		//����ǵз�̹�ˣ�
		if(!good){
			Direction[] dirs=Direction.values();//��enum�ķ����D�Q�ɔ��M�����ɰ˸�����������ͬ�ķ���
			if(step==0){
			step=r.nextInt(10)+8;
			int rn=r.nextInt(dirs.length);//�������һ����
			dir=dirs[rn];
			}
			
			step--;
			//�趨�������
			if(r.nextInt(40)>33){
				this.fire();
			}
		}
		
	}
	
	public void keyPressed(KeyEvent e){

		int key=e.getKeyCode();
		switch (key) {
		//ʹ̹�˶����������÷���
		//�����F2
		case KeyEvent.VK_F2:
		//�ж�̹���Ƿ��
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
		case KeyEvent.VK_CONTROL://����ť��
			 fire();
			break;
		case KeyEvent.VK_SPACE://���а�ť�� ��������
			 superFire();
			break;
		//ʹ̹�˶����������÷���
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
		//�󣬷��ϣ����ң�����====����ȷ������Ϊ��
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
	//��������
	  public void superFire(){
		  Direction [] dirs=Direction.values();
		  for(int i=0;i<8;i++){
			 fire(dirs[i]);
		  }
		  }
	//���̹�����ڵľ���λ�úʹ�С
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}

	private void stay(){
		x=oldX;
		y=oldY;
	}
	
	//��ײ�����жϣ��Ƿ���ǽײ��
	 public boolean collidesWithWall(Wall w){
		 if(this.tlive&&this.getRect().intersects(w.getRect())){
			 this.stay();
			 return true;
		 };
		 return false;
	 }
	 
	//��ײ�����жϣ��Ƿ�������̹��ײ��
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
	
	//�ڲ��࣬���������ҷ�̹�˵�Ѫ��
	private class BloodBar{
		public void draw(Graphics g){
			g.setColor(Color.RED);
			g.drawRect(x, y-10, 30, 10);//�����ⲿ����Ѫ���Ŀ�
			//g.setColor(Color);
			int w=30*life/100;//�������ʵ��Ѫ���Ŀ�Ŀ��
			g.fillRect(x, y-10, w, 10);
		}
	}
	//��Ѫ���жϣ�
	public boolean eat(Blood b){
		 if(this.tlive&&b.isLive()&&this.getRect().intersects(b.getRect())){
			 this.life=100;
			 b.setLive(false);
			 return true;
		 };
		 return false;
	}
}
