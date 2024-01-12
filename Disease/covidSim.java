package covid;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class covidSim extends JPanel{
	public static JFrame gui = new JFrame();
	public static void main(String[]args){
		gui.setSize(1200,800);
		drawing DC = new drawing();
		gui.add(DC);
		gui.setVisible(true);
		gui.setTitle("Covid Sim");
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
