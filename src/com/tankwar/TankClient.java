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
 * ��Ϸ������
 * 
 * @author WanHung
 *
 * 
 */
public class TankClient extends JFrame implements Serializable {
	/**
	 * �趨��Ϸ����ĳ���� �����ݿ��ȡ��
	 * */
	FrameDao fd = new FrameDao();
	FrameBean fb = new FrameBean();
	// ��ȡ��
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

	// ��ȡ�ߣ�
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

	// �����ȣ�
	public static final int GAME_WIDTH = getWFrame();
	public static final int GAME_HEIGHT = getHFrame();

	List<Missile> missiles = new ArrayList<Missile>();// �ӵ����飬��List<>����
	List<Explode> explode = new ArrayList<Explode>();// ��ը���飬��List<>����
	List<Tank> tanks = new ArrayList<Tank>();// �з�̹������
	Image offScreenImage = null;// ���残��
	Wall w1 = new Wall(100, 100, 100, 25, this);
	Wall w2 = new Wall(400, 100, 100, 25, this);
	Wall w3 = new Wall(675, 160, 25, 100, this);
	Wall w4 = new Wall(140, 180, 50, 25, this);
	Tank myTank = new Tank(400, 600, true, Direction.STOP, this);// �ҷ�̹��
	Blood b = new Blood();// ʵ����Ѫ��

	public void lauchFrame() {
		// �������ļ����ȡ�з�̹�˿�ʼ����
		int initTankCount = Integer.parseInt(PropertyMgr
				.getProperty("initTankCount"));
		// ѭ����ӵз�̹��
		for (int i = 0; i < initTankCount; i++) {
			tanks.add(new Tank(50 + 40 * (i + 1), 50, false, Direction.D, this));
		}

		this.setTitle("̹�˴�ս ");
		this.setSize(GAME_WIDTH, GAME_HEIGHT);
		this.setLocation(100, 100);
		this.setResizable(false);// ���ô��ڲ�����󻯣������û��Լ�������Ϸ�����С
		this.setVisible(true);
		// �������setDefaultCloseOperation();��swing����еķ�����awtû��
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// ������Ĳ��������ر�
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		this.addKeyListener(new KeyMonitor());
		// �̣߳�
		new Thread(new PaintThread()).start();
	}

	private class PaintThread implements Runnable {
		// �߳��ڲ���ֻΪ�ⲿ��װ����� �������ڲ���Ϳ����ˣ�
		@Override
		public void run() {
			while (true) {
				repaint();// ÿ����һ���߳̾��ػ�һ�Σ�ʹ��̹�˶�������
				try {
					Thread.sleep(100);// ����ػ�ʱ��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.GREEN);// ֱ���������ñ���ɫ
		g.fillRect(0, 0, TankClient.GAME_WIDTH, TankClient.GAME_WIDTH);// ��䡣

		/*
		 * ���µ���ʾ����д�Ĺ����������������õĿ���ע�͵��� g.setColor(Color.BLACK);//��ʾ�ӵ�����Ϸ�����ڵ���Ŀ
		 * g.drawString("Tank Life"+myTank.getLife(), 100, 90); //��ʾ̹�˵�Ѫ��
		 * g.drawString("missiles count:"+ missiles.size(), 100, 100); //��ʾ�ӵ�����Ŀ
		 * g.drawString("explode count:"+ explode.size(), 100, 120); //��ʾ��ը����Ŀ��
		 * g.drawString("tanks count:"+ tanks.size(), 100, 140); //��ʾ�з�̹�˵���Ŀ
		*/
		//g.setColor(Color.RED);
		//g.drawString("Socre"+Missile.score, 700, 500);
		myTank.draw(g);// ���ҷ�̹��
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithWall(w3);
		myTank.collidesWithWall(w4);

		// �����жϵз�̹�˵����������ս����û�ез�̹�ˣ��Զ���������̹�ˣ����ɿ������ó���Ϸ������XD
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
	 * �ڲ��࣬���ڼ��������¼�
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
