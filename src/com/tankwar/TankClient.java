package com.tankwar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 游戏界面类
 * 
 * @author WanHung
 *
 * 
 */
public class TankClient extends JFrame implements Serializable {
	/**
	 * 设定游戏界面的长与宽 从数据库调取！
	 * */
	FrameDao fd = new FrameDao();
	FrameBean fb = new FrameBean();
	// 调取宽！
	public static int getWFrame() {
		int w = 0;
		FrameDao fd = new FrameDao();
		ArrayList<?> frame = fd.Quety();
		for (int i = 0; i < frame.size(); i++) {
			FrameBean fb = (FrameBean) frame.get(i);
			w = fb.getGwidth();
		}
		return w;
	}

	// 调取高！
	public static int getHFrame() {
		int h = 0;
		FrameDao fd = new FrameDao();
		ArrayList<?> frame = fd.Quety();
		for (int i = 0; i < frame.size(); i++) {
			FrameBean fb = (FrameBean) frame.get(i);
			h = fb.getGheight();
		}
		return h;
	}

	// 长与宽度！
	public static final int GAME_WIDTH = getWFrame();
	public static final int GAME_HEIGHT = getHFrame();

	List<Missile> missiles = new ArrayList<Missile>();// 子弹数组，用List<>接收
	List<Explode> explode = new ArrayList<Explode>();// 爆炸数组，用List<>接收
	List<Tank> tanks = new ArrayList<Tank>();// 敌方坦克数组
	Image offScreenImage = null;// 背面畫板
	Wall w1 = new Wall(100, 100, 100, 25, this);
	Wall w2 = new Wall(400, 100, 100, 25, this);
	Wall w3 = new Wall(675, 160, 25, 100, this);
	Wall w4 = new Wall(140, 180, 50, 25, this);
	Tank myTank = new Tank(400, 600, true, Direction.STOP, this);// 我方坦克
	Blood b = new Blood();// 实例化血条

	public void lauchFrame() {
		// 从配置文件里读取敌方坦克开始数量
		int initTankCount = Integer.parseInt(PropertyMgr
				.getProperty("initTankCount"));
		// 循环添加敌方坦克
		for (int i = 0; i < initTankCount; i++) {
			tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Direction.D, this));
		}

		this.setTitle("坦克大战 ");
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(100, 100);
		this.setResizable(false);// 设置窗口不可最大化，避免用户自己跳动游戏界面大小
		this.setVisible(true);
		// 估计这个setDefaultCloseOperation();是swing里才有的方法，awt没有
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 匿名类的操作方法关闭
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.addKeyListener(new KeyMonitor());
		// 线程！
		new Thread(new PaintThread()).start();
	}

	private class PaintThread implements Runnable {
		// 线程内部类只为外部包装类服务！ 所以用内部类就可以了！
		@Override
		public void run() {
			while (true) {
				repaint();// 每调用一次线程就重画一次，使得坦克动起来！
				try {
					Thread.sleep(100);// 相隔重画时间
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GREEN);// 直接重新设置背景色
		g.fillRect(0, 0, TankClient.GAME_WIDTH, TankClient.GAME_WIDTH);// 填充。

		/*
		 * 以下的显示是在写的过程中留下来测试用的可以注释掉！ g.setColor(Color.BLACK);//显示子弹在游戏界面内的数目
		 * g.drawString("Tank Life"+myTank.getLife(), 100, 90); //显示坦克的血条
		 * g.drawString("missiles count:"+ missiles.size(), 100, 100); //显示子弹的数目
		 * g.drawString("explode count:"+ explode.size(), 100, 120); //显示爆炸的数目；
		 * g.drawString("tanks count:"+ tanks.size(), 100, 140); //显示敌方坦克的数目
		*/
		//g.setColor(Color.RED);
		//g.drawString("Socre"+Missile.score, 700, 500);
		myTank.draw(g);// 画我方坦克
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithWall(w3);
		myTank.collidesWithWall(w4);

		// 首先判断敌方坦克的数量，如果战场上没有敌方坦克，自动从新生成坦克！。可考虑设置成游戏结束！XD
		if (tanks.size() <= 0) {
//			for (int i = 0; i < Integer.parseInt(PropertyMgr
//					.getProperty("reProduceTankCount")); i++) {
//				tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Direction.D,
//						this));
//			}
//		
		}

		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.hitWall(w3);
			m.draw(g);
		}

		for (int i = 0; i < explode.size(); i++) {
			Explode e = explode.get(i);
			e.draw(g);
		}

		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			t.draw(g);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithWall(w3);
			t.collidesWithWall(w4);

			t.collidesWithTanks(tanks);
		}
		w1.draw(g);
		w2.draw(g);
		w3.draw(g);
		w4.draw(g);
		b.draw(g);
		myTank.eat(b);

	}

	@Override
	public void update(Graphics g) {
		if (offScreenImage == null) {
			offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();

		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}

	/**
	 * 内部类，用于监听键盘事件
	 * 
	 * @author WanHung
	 *
	 */
	private class KeyMonitor extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}
	}

}
