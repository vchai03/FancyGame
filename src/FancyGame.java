import java.awt.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;

import java.awt.event.*;
import java.util.*;

public class FancyGame extends JFrame implements ActionListener{	

	private final int CANVAS_WIDTH = 300;
	private final int CANVAS_HEIGHT = 575;
	private final int SPLIT_DIST = 40;			//how far to shoot the split piece away
	private final int STEP = 5;				//how far points moves per iteration
	private final int POINT_SPEED = 25;			//how fast point moves in milliseconds

	private DrawPanel canvas;
	private Timer timer;
	private int numClicks;

	public FancyGame(){
		setSize(315,635);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		canvas = new DrawPanel();
		canvas.setBounds(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);
		canvas.setBackground(Color.white);
		canvas.setBorder(BorderFactory.createLineBorder(Color.black));
		
		JMenuBar bar = new JMenuBar();
		JMenu difficulty = new JMenu("Difficulty");
		
		JMenuItem baby = new JMenuItem("Baby");
		JMenuItem normal = new JMenuItem("Normal");
		JMenuItem nightmare = new JMenuItem("Nightmare");
		
		difficulty.add(baby);
		difficulty.add(normal);
		difficulty.add(nightmare);
		
		bar.add(difficulty);
		setJMenuBar(bar);
		
		baby.addActionListener(this);
		normal.addActionListener(this);
		nightmare.addActionListener(this);

		add(canvas);
		
		setVisible(true);
		
		timer = new Timer(POINT_SPEED, new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				for(int i=0; i<canvas.allPoints.size(); i++) {
					canvas.allPoints.get(i).move();
				}
				canvas.repaint();							
			}
		});

		timer.start();
	}

	public void actionPerformed(ActionEvent ae){
		
		int number = 0;
		int size = 0;
		
		if(ae.getActionCommand().equals("Baby")) {
			number = 1;
			size = 40;
		}
		else if(ae.getActionCommand().equals("Normal")) {
			number = 2;
			size = 50;
		}
		else if(ae.getActionCommand().equals("Nightmare")) {
			number = 3;
			size = 75;
		}
		
		while(canvas.allPoints.size() > 0)
			canvas.allPoints.remove(0);
		
		for(int i=0; i<number; i++)
			canvas.allPoints.add(new Point(size));			
	}

	
	public class DrawPanel extends JPanel implements MouseListener{

		private ArrayList<Point> allPoints;

		public DrawPanel(){
			allPoints = new ArrayList<Point>();
			this.addMouseListener(this);
		}

		public void paintComponent(Graphics g){
			super.paintComponent(g);

			for(Point next: allPoints){
				
				g.setColor(next.pColor);
				g.fillOval(next.xLoc, next.yLoc, next.size, next.size);

			}
		}

		public void mousePressed(MouseEvent me) {
			
			numClicks ++;
			int[] vertDisp = {1,1,-1,-1};
			int[] horzDisp = {1,-1,-1,1};
			
			int x = me.getX();
			int y = me.getY();
			int index=-1;
			
			for(int i=0; i<allPoints.size(); i++) {
				Point next = allPoints.get(i);
				if(x>next.xLoc && x<next.xLoc+next.size && y>next.yLoc && y<next.yLoc+next.size)
					index = i;
			}
						
			if (index != -1) {
				int nextSize = allPoints.get(index).size / 2;

				if (nextSize >= 15) {

					for (int i = 0; i < vertDisp.length; i++) {

						int nextX = x + horzDisp[i] * SPLIT_DIST;
						int nextY = y + vertDisp[i] * SPLIT_DIST;

						if (nextX >= 0 && nextY >= 0 && nextX + nextSize <= CANVAS_WIDTH && nextY + nextSize <= CANVAS_HEIGHT)
							allPoints.add(new Point(x + horzDisp[i] * SPLIT_DIST, y + vertDisp[i] * SPLIT_DIST, nextSize));
					}
				}
				allPoints.remove(index);
			}
			
			if(allPoints.size() == 0) {
				JOptionPane.showMessageDialog(null, "Number of clicks: "+numClicks);
			}
		}

		public void mouseReleased(MouseEvent arg0) {
			// do not implement

		}

		public void mouseClicked(MouseEvent arg0) {
			// do not implement
			
		}		


		public void mouseEntered(MouseEvent arg0) {
			// do not implement

		}

		
		public void mouseExited(MouseEvent arg0) {
			// do not implement

		}

		
		

		
		
	}

	public class Point{
		private int xLoc;	//this is the top left corner of the "box" that the point is drawn in.
		private int yLoc;
		private Color pColor;
		private int size;
		private int xDir;	//direction it's currently moving.
		private int yDir;

		//randomly makes a point of size s
		public Point(int s){
			this((int)(Math.random()*(CANVAS_WIDTH-s)),(int)(Math.random()*(CANVAS_HEIGHT-s)),s);			
		}
		
		//makes a point of size s at a given location
		public Point(int xL,int yL, int s){

			pColor =  new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			size = s;
			xLoc = xL;
			yLoc = yL;
			int[] directions = genDirection();
			xDir = directions[0];
			yDir = directions[1];		
		}

		private int[] genDirection(){

			int xVal;
			int yVal;

			do{
				xVal = (int)(Math.random()*3-1);
				yVal = (int)(Math.random()*3-1);
			}while(xVal == 0 && yVal == 0);

			int[] toRet = {xVal,yVal};
			
			return toRet;
		}

		public boolean equals(Object other){
			Point op = (Point)other;
			return this.xLoc == op.xLoc && this.yLoc == op.yLoc && pColor.equals(op.pColor);
		}
		
		public boolean hitVertBounds() {
			return xLoc<=0 || xLoc+size>=CANVAS_WIDTH;
		}
		
		public boolean hitHorzBounds() {
			return yLoc<=0 || yLoc+size>=CANVAS_HEIGHT;
		}
		
		public void move() {
			
			if(hitHorzBounds()) 				
				yDir *= -1;
			if(hitVertBounds())
				xDir *= -1;	
			
			xLoc += xDir*STEP;
			yLoc += yDir*STEP;				
		}
	}



	public static void main(String[] args){
		new FancyGame();
	}


}
