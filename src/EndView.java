import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class EndView extends JPanel implements MouseListener, MouseMotionListener{

	//variables
	private int playerScore;
	private int[] newTopTen;
	private boolean mouseHovered;
	private JPanel panel;

	EndView(int score, BackendEngine engine) {

		//	actual code for the constructor:
		playerScore = score;
		BackendEngine game = engine;
		updateLeaderboard(playerScore);
		panel = new PaintEndView();
		panel.setLayout(null);
		panel.setBackground(Color.WHITE);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
	
	}
	//	method to read the csv file and edit it if necessary
	private void updateLeaderboard(int score) {
		int place = 0;
		int[] topTen = new int[10];
		newTopTen = new int[10];
		Scanner scan = null;
		//	read the csv file and make it an array
		try {
			scan = new Scanner(new File("/3D-Maze/src/assets/3D-Maze-Scores.csv"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		scan.useDelimiter(",");
		for(int l=0; l<10; l++) {
			topTen[place] = Integer.parseInt(scan.next());
			place++;
		}
		scan.close();

		//	find where score goes on leaderboard and adjust array as necessary
		if(score>=topTen[9]) { //if score in top 10
			int index = 0;
			int temp = 0;
			boolean isDone = false;
			do {
				if((score>=topTen[index])&&(!isDone)) {
					temp = topTen[index];
					newTopTen[index] = score;
					isDone = true;
				} else if(!isDone) {
					newTopTen[index]=topTen[index];
				} else {
					newTopTen[index] = topTen[index-1];
				}
				index++;
			} while(index<10);
		} else {  //if not in top 10
			for(int i=0; i<10; i++) {
				newTopTen[i]=topTen[i];
			}
		}

		//	copy correct array back to csv
		CharSequence tempSeq;
		String tempStr;
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File("/3D-Maze/src/assets/3D-Maze-Scores.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			for (int i=0; i<10; i++) {
				tempStr = ""+newTopTen[i];
				tempSeq = (CharSequence)tempStr;
				fw.append(tempSeq);
				if(i<9)
					fw.append(",");
			}
			fw.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//paint component method to do all the painting required
	private class PaintEndView extends JPanel {

		public void paintComponent(Graphics g) {

			super.paintComponent(g);

			//	Creates background and title
			Image bgImage = readImage("/3D-Maze/src/assets/endbg.jpg");
			Image bgScaled = bgImage.getScaledInstance(1151, 720, Image.SCALE_DEFAULT);
			g.drawImage(bgScaled,-215,0,null);
			Image titleImage = readImage("/3D-Maze/src/assets/endtitle.png");
			Image titleScaled = titleImage.getScaledInstance(680, 177, Image.SCALE_DEFAULT);
			g.drawImage(titleScaled,20,10,null);

			//	Displays the player's score (called multiple times to create black outline around white text)
			String displayScore; //assumes the highest playerScore possible is 4 digits (max score = 9999)
			displayScore = adjustScore(playerScore);
			g.setColor(Color.BLACK);
			g.setFont(new Font("OCR A Extended", Font.BOLD, 40));
			g.drawString("Your Score: " + displayScore, 168, 203);
			g.drawString("Your Score: " + displayScore, 168, 205);
			g.drawString("Your Score: " + displayScore, 168, 207);
			g.drawString("Your Score: " + displayScore, 170, 203);
			g.drawString("Your Score: " + displayScore, 170, 205);
			g.drawString("Your Score: " + displayScore, 170, 207);
			g.drawString("Your Score: " + displayScore, 172, 203);
			g.drawString("Your Score: " + displayScore, 172, 205);
			g.drawString("Your Score: " + displayScore, 172, 207);
			g.setColor(Color.WHITE);
			g.drawString("Your Score: " + displayScore, 170, 205);

			//	creates the transparent black background for the leaderboard
			g.setColor(new Color(0,0,0,150));
			g.fillRect(80,230,560,350);

			//	creates the image that has numbers 1-10 for the leaderboard
			Image leaderImage = readImage("/3D-Maze/src/assets/endleaderboard.png");
			Image leaderScaled = leaderImage.getScaledInstance(300, 317, Image.SCALE_DEFAULT);
			g.drawImage(leaderScaled,115,245,null);
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(new Font("OCR A Extended", Font.BOLD, 40));
			
			//	reads from the newly altered file and displays the scores on the leaderboard (your score is yellow if on leaderboard)
			Scanner scanner = null;
			try {
				scanner = new Scanner(new File("/3D-Maze/src/assets/3D-Maze-Scores.csv"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			scanner.useDelimiter(",");
			boolean yellowed = false;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 175, 283, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 175, 351, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 175, 418, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 175, 486, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 175, 552, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 430, 283, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 430, 351, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 430, 418, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 430, 486, yellowed, g) ;
			yellowed = drawLBScore(Integer.parseInt(scanner.next()), 430, 552, yellowed, g) ;
			scanner.close();
			
			//end button
			Image endButton;
			if(mouseHovered) {
				endButton = readImage("/3D-Maze/src/assets/closeOutlined.png");
				g.drawImage(endButton,208,598,null);
			}else{ 
				endButton = readImage("/3D-Maze/src/assets/close.png");
				g.drawImage(endButton,210,600,null);
			}
		}
	
		//	method to read the image (simplify code above)
		private Image readImage(String imgStr) {
			try {
				return ImageIO.read(getClass().getResource(imgStr));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return null;
		}

		//	method to adjust string of numbers displayed (simplify code above)
		private String adjustScore(int theScore) {
			String scoreToReturn;
			if(theScore<10) {
				scoreToReturn = "000" + theScore;
			} else if(theScore<100) {
				scoreToReturn = "00" + theScore;
			} else if(theScore<1000) {
				scoreToReturn = "0" + theScore;
			} else {
				scoreToReturn = "" + theScore;
			}
			return scoreToReturn;
		}

		//	method to display number on leaderboard (simplify code above)
		private boolean drawLBScore(int score, int x, int y,boolean yellowed, Graphics g) {
			boolean yello = yellowed;
			if((score==playerScore)&&(!yello)) {
				g.setColor(Color.YELLOW);
				yello = true;
			}
			g.drawString("= "+adjustScore(score), x, y);
			g.setColor(Color.LIGHT_GRAY);
			return yello;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//these coordinates will probably change once integrated with Raghav's code
		if(e.getX()>=210 && e.getX()<=510 && e.getY()>=600 && e.getY()<=660)
			mouseHovered=true;
		else
			mouseHovered=false;
		panel.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//these coordinates will probably change once integrated with Raghav's code
		if(e.getX()>=210 && e.getX()<=510 && e.getY()>=600 && e.getY()<=660)
			game.changeView("close");
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
