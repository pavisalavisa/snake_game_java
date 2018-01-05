package game;

import java.awt.Dimension;

import javax.swing.JFrame;

public class MainClass {
	public static void main(String[]args){
		JFrame frame=new JFrame("Snake");
		frame.setContentPane(new MainPanel());
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setPreferredSize(new Dimension(MainPanel.WIDTH,MainPanel.HEIGHT));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
	}

}
