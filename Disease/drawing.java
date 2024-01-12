package covid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Timer;

public class drawing extends covidSim implements ActionListener, MouseListener,KeyListener{
	Timer t = new Timer(10,this);
	public int pop = 0;
	public Boolean run = true;
	public int maxHos = 0;
	public int maxInfected = 0;
	public int totalInfected = 0;
	public ArrayList<person> hospital = new ArrayList<person>();
	public ArrayList<Integer> hbiGraph = new ArrayList<Integer>();	
	public ArrayList<Integer> ciGraph = new ArrayList<Integer>();
	public ArrayList<Integer> deadGraph = new ArrayList<Integer>();
	public int[] deathAG = new int[6];
	int count = 0,infected = 0,tempInfected = 0;
	public double maxSD = 6;
	public int width,height;
	public int blockNum = 0;
	public Boolean drawArc = true;
	public ArrayList<block> blocks = new ArrayList<block>();
	public static int bound = 50;
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		setBackground(Color.black);
		g2.setColor(Color.white);
		g2.drawString("Block#: " + Integer.toString(blockNum), 10, 10);
		g2.drawString("Hospital Occupacy: " + Integer.toString(hospital.size()) + " / " + Integer.toString(maxHos), 10, 30);
		if(ciGraph.size()>0) {
			g2.drawString("Infected: " + Integer.toString(ciGraph.get(ciGraph.size()-1)) + " / " + Integer.toString(pop), 10, 50);
			g2.drawString(">80:" + deathAG[0] + "   |79-70:" + deathAG[1] + "   |69-60:" + deathAG[2]
					+ "   |59-50:" + deathAG[3] + "   |49-40:" + deathAG[4] + "   |<39:"  + deathAG[5], 10, 70);
			g2.drawString("Total Dead: " + Integer.toString(deadGraph.get(deadGraph.size()-1)), 10, 90);
		}
		g2.drawString("Max Infected: " + Integer.toString(maxInfected), 10, 110);
		g2.drawString("Total Infected: " + Integer.toString(totalInfected), 10, 130);
		
		g2.drawRect(width/2, bound, width/2-bound, height-2*bound);
		
		g2.setColor(Color.green);
		g2.drawRect(width/4, bound, width/4, height-3*bound-(height-300));
		ArrayList<person> blockDraw = blocks.get(blockNum).block;
		for(int j = 0; j < blockDraw.size(); j++) {
			person p = blockDraw.get(j);
			g2.setColor(Color.white);
			if(p.infected) {
				g2.setColor(Color.green);				
			}
			if(p.recovered) {
				g2.setColor(Color.cyan);
			}
			if(p.dead) {
				g2.setColor(Color.red);
			}
			Ellipse2D.Double dot = new Ellipse2D.Double(p.x, p.y, 2*p.radius, 2*p.radius);
			g2.fill(dot);
			if(drawArc) {
				Ellipse2D.Double rad = new Ellipse2D.Double(p.x+p.radius-p.sdRad,p.y+p.radius-p.sdRad,p.sdRad*2,p.sdRad*2);
				g2.draw(rad);
			}
		}
		g2.setColor(Color.white);
		g2.drawLine(0, height-2*bound-(height-300), width/2, height-2*bound-(height-300));
		g2.drawLine(0, height-2*bound, width/2, height-2*bound);
		g2.setColor(Color.green);
		for(int i = 0; i < hbiGraph.size()-1; i++) {
			double w = (width/2.0)/hbiGraph.size();
			double x = ((width*i/2.0)/hbiGraph.size());
			double h1 = (((height-300)*hbiGraph.get(i)/pop));
			double h2 = (((height-300)*hbiGraph.get(i+1)/pop));
			//Rectangle2D.Double rect = new Rectangle2D.Double(x, height-2*bound-h, w, h);
			Line2D.Double line = new Line2D.Double(x,height-2*bound-h1, x+w,height-2*bound-h2);
			g2.draw(line);
			//g2.fill(rect);
		}	
		
		g2.setColor(Color.cyan);
		for(int i = 0; i < ciGraph.size()-1; i++) {
			double w = (width/2.0)/ciGraph.size();
			double x = ((width*i/2.0)/ciGraph.size());
			double h1 = Math.ceil((((double)(height-300)*(double)(ciGraph.get(i))/(double)(pop))));
			double h2 = Math.ceil((((double)(height-300)*(double)(ciGraph.get(i+1))/(double)(pop))));
			//Rectangle2D.Double rect = new Rectangle2D.Double(x, height-2*bound-h, w, h);
			//g2.fill(rect);
			Line2D.Double line = new Line2D.Double(x,height-2*bound-h1, x+w,height-2*bound-h2);
			g2.draw(line);
		}
		g2.setColor(Color.red);
		for(int i = 0; i < deadGraph.size()-1; i++) {
			double w = (width/2.0)/deadGraph.size();
			double x = ((width*i/2.0)/deadGraph.size());
			double h1 = (((height-300)*deadGraph.get(i)/pop));
			double h2 = (((height-300)*deadGraph.get(i+1)/pop));
			//Rectangle2D.Double rect = new Rectangle2D.Double(x, height-2*bound-h, w, h);
			//g2.fill(rect);
			Line2D.Double line = new Line2D.Double(x,height-2*bound-h1, x+w,height-2*bound-h2);
			g2.draw(line);
		}

	}
	
	
	
	public drawing() {
		width = gui.getWidth();
		height = gui.getHeight();
		t.start();
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);	
		creation();
		int count = 0;
		for(int i = 0; i < blocks.size(); i++) {
			person p1 = blocks.get(i).block.get(0);
			person p2 = blocks.get(i).block.get(1);
			p1.infected = true;
			p1.DTR = 270 * (int)((0.0008*Math.pow((p1.age-20),2)) + 2);
			p2.infected = true;
			p2.DTR = 270 * (int)((0.0008*Math.pow((p2.age-20),2)) + 2);
			//System.out.println(p.DTR);
			pop += blocks.get(i).block.size();
			count++;
		}
		System.out.println(pop);
		maxHos = (int)(pop*0.1);
		System.out.println(maxHos);
	}
	
	
	
	private void creation() {
		for(int i = 0; i < 200; i++) {
			ArrayList<person> people = new ArrayList<person>(); 
			for(int j = 0; j < 75; j++) {
				int rad = 5;
				double x = width/2 + Math.random()*(width/2-bound-2*rad);
				double y = bound + Math.random()*(height-2*bound-2*rad);
				double vel = 1;
				double velY = 0;
				double velX = 2*(Math.random()-0.5)*vel;
				double SD = 0;
				if(Math.random() < 1) {
					SD = ((2*rad)/2)*(maxSD/2 + maxSD*Math.random()*3/4);
				}
				if(Math.random() < 0.5) {
					velY = Math.pow(Math.pow(vel, 2.0) - Math.pow(velX, 2.0), 0.5);
				}else {
					velY = -Math.pow(Math.pow(vel, 2.0) - Math.pow(velX, 2.0), 0.5);
				}
				Boolean add = true;
				for(int k = 0; k < people.size(); k++) {
					double dist = Math.pow(Math.pow(x-people.get(k).x, 2) + Math.pow(y-people.get(k).y, 2), 0.5);
					if(dist < SD || dist < people.get(k).sdRad) {
						add = false;
					}
				}
				if(add) {
					int age = (int)(10 + Math.random()*Math.random()*90);
					person p = new person(x,y,rad,SD,true,velX,velY,age);
					people.add(p);
				}
			}
			blocks.add(new block(people));
		}
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if(run) {
			int dead = 0;
			infected = 0;
			int firstInfected = 0;
			count++;
			if(count%5 == 0 && run) {
				count = 1;
				for(int i = 0; i < blocks.size(); i++) {
					for(int j = 0; j < blocks.get(i).block.size(); j++) {
						if(blocks.get(i).block.get(j).infected) {
							if(j != 0 && j != 1) {
								infected++;
							}else {
								firstInfected++;
							}
						}
						if(blocks.get(i).block.get(j).dead) {
							dead++;
						}
					}
				}
				deadGraph.add(dead);
				ciGraph.add(infected);
				if(hbiGraph.size() == 0) {
					hbiGraph.add(infected);
				}else {
					if(infected - tempInfected > 0) {
						hbiGraph.add(hbiGraph.get(hbiGraph.size()-1) + (infected - tempInfected));	
					}else {
						hbiGraph.add(hbiGraph.get(hbiGraph.size()-1));
					}
				}
	
				tempInfected = infected;
				if(infected > maxInfected) {
					maxInfected = infected;
				}
				if(infected == 0 && firstInfected == 0) {
					run = false;
					System.out.println((double)((double)(dead)/(double)(totalInfected)));
				}
			}
	
			for(int i = 0; i < blocks.size(); i++) {
				Boolean[] swapped = new Boolean[blocks.get(i).block.size()];
				Arrays.fill(swapped, Boolean.FALSE);
				for(int j = 0; j < blocks.get(i).block.size(); j++) {
					person p = blocks.get(i).block.get(j);
					
					
					if(!p.isSD && p.recovered) {
						p.isSD = true;
						Boolean add = false;
						while(!add) {
						add = true;
						double x = width/2 + Math.random()*(width/2-bound-2*p.radius);
						double y = bound + Math.random()*(height-2*bound-2*p.radius);					
						for(int k = 0; k < blocks.get(i).block.size(); k++) {
							person p2 = blocks.get(i).block.get(k);
							double dist = Math.pow(Math.pow(x-p2.x, 2) + Math.pow(y-p2.y, 2), 0.5);
							if(dist < p.sdRad || dist < p2.sdRad) {
								add = false;
							}
						}
							p.x = x;
							p.y = y;
						}
						
						
						
						
						
					}
					
					
					if(p.dead) {
						p.velX = 0;
						p.velY = 0;
					}
					if(p.infected) {
						p.DTR -= 1;
						if(p.DTR <= 0) {
							p.infected = false;
							p.recovered = true;
							hospital.remove(p);
						}
					}
					if(p.x < width/2 && p.x + p.radius*2 > width/2) {
						p.velX *= -1;
					}else if(p.x +2*p.radius > width/2 + (width/2-bound) && p.x < width/2 + (width/2-bound)) {
						p.velX *= -1;
					}else if(p.x + p.radius*2  > width/2 && p.x  < width/2) {
						p.velX *= -1;
					}else if(p.x  < width/4) {
						p.velX *= -1;
					}
					
					if(!p.isSD) {
						if(p.y + p.radius*2 > bound + height-3*bound-(height-300)){
							p.velY *= -1;
						}
					}
					
					if(p.y < bound) {
						p.velY = Math.abs(p.velY);
					}	else if(p.y +2*p.radius > height - bound) {
						p.velY = -Math.abs(p.velY);
					}
					for(int k = 0; k < blocks.get(i).block.size(); k++) {
						person p2 = blocks.get(i).block.get(k);
						if(k != j && !swapped[k] && !swapped[j] && p.isSD) {
							double dist = Math.pow(Math.pow((p.x+p.velX)-(p2.x+p2.velX), 2) + Math.pow((p.y+p.velY)-(p2.y+p2.velY), 2), 0.5);
							if(p2.infected && !p.recovered && !p.infected && !p.dead) {
								double m = (2*p2.radius/2)*4;
								double percent = 0.10;
								double l = (2*Math.log(percent))/(p.radius-m);
								double chance = Math.pow(Math.E, -l*(dist-p.radius)/2);
								if(Math.random() < chance) {
									p.infected = true;
									totalInfected++;
									p.DTR = 270 * (int)((0.0008*Math.pow((p.age-20),2)) + 2);
									if(hospital.size() >= maxHos || Math.random() < 0.2) {
										int age = p.age;
										double dc = Math.random();
										if(age > 80) {
											if(dc < 0.3) {
												p.dead = true;
												p.infected = false;
												p.velX = 0;
												p.velY = 0;
												System.out.println(p.toString());
												deathAG[0]++;
											}
										}else if(age >=70) {
											if(dc < 0.2) {
												p.dead = true;
												p.infected = false;
												p.velX = 0;
												p.velY = 0;
												System.out.println(p.toString());
												deathAG[1]++;
											}										
										}else if(age >=60) {
											if(dc < 0.08) {
												p.dead = true;
												p.infected = false;
												p.velX = 0;
												p.velY = 0;
												System.out.println(p.toString());
												deathAG[2]++;
											}										
										}else if(age >=50) {
											if(dc < 0.04) {
												p.dead = true;
												p.infected = false;
												p.velX = 0;
												p.velY = 0;
												System.out.println(p.toString());
												deathAG[3]++;
											}										
										}else if(age > 40) {
											if(dc < 0.008) {
												p.dead = true;
												p.infected = false;
												p.velX = 0;
												p.velY = 0;
												System.out.println(p.toString());
												deathAG[4]++;
											}										
										}else {
											if(dc < 0.004) {
												p.dead = true;
												p.infected = false;
												p.velX = 0;
												p.velY = 0;
												System.out.println(p.toString());
												deathAG[5]++;
											}										
										}
									}else {
										if(!p.hospital) {
											p.hospital = true;
											hospital.add(p);
											int age = p.age;
											double dc = Math.random();
											if(age > 80) {
												if(dc < 0.15) {
													p.dead = true;
													p.infected = false;
													p.velX = 0;
													p.velY = 0;
													hospital.remove(p);
													System.out.println(p.toString());
													deathAG[0]++;
												}
											}else if(age >=70) {
												if(dc < 0.1) {
													p.dead = true;
													p.infected = false;
													p.velX = 0;
													p.velY = 0;
													hospital.remove(p);
													System.out.println(p.toString());
													deathAG[1]++;
												}
											}else if(age >=60) {
												if(dc < 0.04) {
													p.dead = true;
													p.infected = false;
													p.velX = 0;
													p.velY = 0;
													hospital.remove(p);
													System.out.println(p.toString());
													deathAG[2]++;
												}
											}else if(age >=50) {
												if(dc < 0.02) {
													p.dead = true;
													p.infected = false;
													p.velX = 0;
													p.velY = 0;
													hospital.remove(p);
													System.out.println(p.toString());
													deathAG[3]++;
												}
											}else if(age > 40) {
												if(dc < 0.004) {
													p.dead = true;
													p.infected = false;
													p.velX = 0;
													p.velY = 0;
													hospital.remove(p);
													System.out.println(p.toString());
													deathAG[4]++;
												}
											}else {
												if(dc < 0.002) {
													p.dead = true;
													p.infected = false;
													p.velX = 0;
													p.velY = 0;
													hospital.remove(p);
													System.out.println(p.toString());
													deathAG[5]++;
												}
											}
											if(!p.dead) {
												p.isSD = false;
												p.x = (width/4+width/8);
												p.y	= (bound*2);
											}
											
										}
									}
								}
							}
							if(dist < p.sdRad && !p2.dead && !p.dead) {
								swapped[k] = true;
								swapped[j] = true;
								double tx = p.velX;
								double ty = p.velY;
								p.velX = p2.velX;
								p.velY = p2.velY;
								p2.velX = tx;
								p2.velY = ty;
							}
						}
					}
					p.x += p.velX;
					p.y += p.velY;
				}
				
			}
			repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if(code == KeyEvent.VK_K) {
			blockNum++;
			if(blockNum >= blocks.size()) {
				blockNum = 0;
			}
		}else if(code == KeyEvent.VK_J) {
			blockNum--;
			if(blockNum < 0) {
				blockNum = blocks.size()-1;
			}			
		}else if(code == KeyEvent.VK_L) {
			if(drawArc) {
				drawArc = false;
			}else {
				drawArc = true;
			}
		}else if(code == KeyEvent.VK_S) {
			run = !run;
		}
		
	}



	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}





	@Override
	public void mouseClicked(MouseEvent e) {
	}



	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}




		
}

class block{
	ArrayList<person> block;
	public block(ArrayList<person> block) {
			this.block = block;
	}
}
