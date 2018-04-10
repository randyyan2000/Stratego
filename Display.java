import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.event.*;

public class Display extends JComponent implements MouseListener, ActionListener {

	private static final int SET_UP_MODE = 1;  //letting user swap pieces to set up
	private static final int MOVE_MODE = 0;  //letting user move a piece during game

	//initially, both of these are null. then setGame or setPlayer is called.
	//then one of them should have a value, and one should be null, indicating which viewpoint to use
	private Game game;
	private Player player;

	public static final int CELL_SIZE = 64;
	public static final int BOARD_ROW = 0;
	public static final int BOARD_COL = 0;
	
	public static final int DEFAULT_PAUSE_TIME = 0;

	private static final BufferedImage RED_UNKNOWN_IMAGE = getImage("red-1.png");
	private static final BufferedImage BLUE_UNKNOWN_IMAGE = getImage("blue-1.png");
	private static final BufferedImage BACKGROUND_IMAGE = getImage("background.png");
	private static final BufferedImage[] RED_IMAGES = new BufferedImage[] {
			getImage("red0.png"), getImage("red1.png"), getImage("red2.png"),
			getImage("red3.png"), getImage("red4.png"), getImage("red5.png"),
			getImage("red6.png"), getImage("red7.png"), getImage("red8.png"),
			getImage("red9.png"), getImage("red10.png"), getImage("red11.png")
	};
	private static final BufferedImage[] BLUE_IMAGES = new BufferedImage[] {
			getImage("blue0.png"), getImage("blue1.png"), getImage("blue2.png"),
			getImage("blue3.png"), getImage("blue4.png"), getImage("blue5.png"),
			getImage("blue6.png"), getImage("blue7.png"), getImage("blue8.png"),
			getImage("blue9.png"), getImage("blue10.png"), getImage("blue11.png")
	};

	private JButton done;
	private int[][] setUp;
	private int mode;
	private boolean paused;
	private int pauseTime = DEFAULT_PAUSE_TIME;
	private Location selectedLoc1;
	private Location selectedLoc2;
	public Display() {
		this.game = null;
		this.player = null;
		done = new JButton("Pause");
		init();
	}
	public void setGame(Game g)
	{
		if (game != null || player != null)
			throw new RuntimeException("game = " + game + ", player = " + player);
		game = g;
		repaint();
		try{Thread.sleep(10);}catch(Exception e){}
	}

	public void setPlayer(Player p)
	{
		if (game != null || player != null)
			throw new RuntimeException("game = " + game + ", player = " + player);
		player = p;
		repaint();
		try{Thread.sleep(10);}catch(Exception e){}
	}

	public void init()
	{
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		done.addActionListener(this);
		setPreferredSize(new Dimension(CELL_SIZE * 10, CELL_SIZE * 10)); // 600, 600 is max (maybe)
		frame.getContentPane().add(this, BorderLayout.CENTER);
		frame.getContentPane().add(done, BorderLayout.PAGE_END);
		frame.pack();
		frame.setVisible(true);
		addMouseListener(this);
	}

	public Piece getPiece(Location loc) {
		if(player != null)
			return player.getPiece(loc.rotated());
		else
			return game.getPiece(loc);
	}
	public void paintComponent(Graphics g) {
		if (game == null && player == null)
			return;

		if(mode == MOVE_MODE) {
			g.drawImage(BACKGROUND_IMAGE, 0, 0, CELL_SIZE * 10, CELL_SIZE * 10, null);
			for(int r = 0; r < 10; ++r) {
				for(int c = 0; c < 10; ++c) {
					Piece p = getPiece(new Location(r, c));
					BufferedImage img;
					if (p == null)
						img = null;
					else if(p.getRank() == -1)
						img = p.getColor() ? RED_UNKNOWN_IMAGE : BLUE_UNKNOWN_IMAGE;
					else
						img = p.getColor() ? RED_IMAGES[p.getRank()] : BLUE_IMAGES[p.getRank()];
						if(img != null)
							g.drawImage(img, BOARD_COL + c * CELL_SIZE, BOARD_ROW + r * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
				}
			}
		}
		else if(mode == SET_UP_MODE) {
			for(int r = 0; r < 4; ++r) {
				for(int c = 0; c < 10; ++c) {
					BufferedImage img = RED_IMAGES[setUp[r][c]];
					g.drawImage(img, BOARD_COL + (9 - c) * CELL_SIZE, BOARD_ROW + (9 - r) * CELL_SIZE, CELL_SIZE, CELL_SIZE, null);
				}
			}
		}
		g.setColor(Color.WHITE);
		if(selectedLoc1 != null)
			g.drawRect(selectedLoc1.getCol() * CELL_SIZE, selectedLoc1.getRow() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
		g.setColor(Color.BLACK);
		if(selectedLoc2 != null)
			g.drawRect(selectedLoc2.getCol() * CELL_SIZE, selectedLoc2.getRow() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
	}
	public void setPauseTime(int pause) {
		pauseTime = pause;
	}
	public void update() {
		repaint();
		while(paused) {
			try {
				Thread.sleep(pauseTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public Move getMove()
	{
		//rotate coordinates for human, since human expects to be in rows 0 - 3, but we draw human in rows 6 - 9.
		return selectMove().rotated();
	}

	private Move selectMove() {
		selectedLoc1 = null;
		selectedLoc2 = null;
		while(selectedLoc1 == null || selectedLoc2 == null) {
			repaint();
			try{Thread.sleep(10);}catch(Exception e){}
		}
		return new Move(selectedLoc1, selectedLoc2);
	}
	public int[][] getSetUp() {
		done.setText("Done");
		setUp = Player.randomSetUp();
		mode = SET_UP_MODE;
		Move swap = selectMove().rotated();
		while(mode != MOVE_MODE) {
			int temp = setUp[swap.getFrom().getRow()][swap.getFrom().getCol()];
			setUp[swap.getFrom().getRow()][swap.getFrom().getCol()] = setUp[swap.getTo().getRow()][swap.getTo().getCol()];
			setUp[swap.getTo().getRow()][swap.getTo().getCol()] = temp;
			selectedLoc1 = null;
			selectedLoc2 = null;
			repaint();
			try{Thread.sleep(10);}catch(Exception e){}
			swap = selectMove().rotated();
		}
		repaint();
		try{Thread.sleep(10);}catch(Exception e){}
		done.setText("Close");
		return setUp;
	}
	public void showWinner(boolean color) {
		done.setText("Close");
		JOptionPane.showMessageDialog(null, (color ? "Red" : "Blue") + " team won!", "WINNER", 1);
	}
	public static BufferedImage getImage(String fileName) {
		//System.out.println("reading file: " + fileName);
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
	public void mouseExited(MouseEvent e) {

	}
	public void mouseEntered(MouseEvent e) {

	}
	public void mouseClicked(MouseEvent e) {

	}
	public void mouseReleased(MouseEvent e) {
		if(mode == MOVE_MODE && (e.getX() < 0 || e.getX() >= CELL_SIZE * 10 || e.getY() < 0 ||e.getY() >= CELL_SIZE * 10))
			return;
		else if(mode == SET_UP_MODE && (e.getX() < 0 || e.getX() >= CELL_SIZE * 10 || e.getY() < CELL_SIZE * 6 ||e.getY() >= CELL_SIZE * 10))
			return;
		Location loc = new Location(e.getY() / CELL_SIZE, e.getX() / CELL_SIZE);
		if(selectedLoc1 != null && selectedLoc1.equals(loc))
			selectedLoc1 = null;
		else if(selectedLoc2 != null && selectedLoc2.equals(loc))
			selectedLoc2 = null;
		else if(selectedLoc1 == null)
			selectedLoc1 = loc;
		else
			selectedLoc2 = loc;
		repaint();
	}
	public void mousePressed(MouseEvent e) {

	}
	public void actionPerformed(ActionEvent e) {
		if(done.getText().equals("Done")) {
			mode = MOVE_MODE;
			selectedLoc1 = new Location(0, 0);
			selectedLoc2 = new Location(0, 0);
			done.setText("Close");
		}
		else if(done.getText().equals("Pause")) {
			paused = true;
			done.setText("Unpause");
		}
		else if(done.getText().equals("Unpause")) {
			paused = false;
			done.setText("Pause");
		}
		else
			System.exit(0);
	}
}


