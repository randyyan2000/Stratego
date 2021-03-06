import java.io.*;

public class Game
{
  public static void main(String[] args) throws IOException
  {
//    PrintWriter out = new PrintWriter(new FileWriter("setups.txt"), true);
//    Display display = new Display();
    int games = 10000;
    int wins = 0;
//    for(int s = 0; s < Integer.MAX_VALUE; s++)
//    {
//      RandoStrategy200 r = new RandoStrategy200();
//      r.setUp();
//      out.println("set up #" + s);
//      System.out.println("set up #" + s);
//      r.printSetUp(out);
//      r.printSetUp(System.out);
      for(int i = 0; i < games; i++)
      {
        Game g = new Game(new RandoStrategy200(), new RandomStrategy(), null);
        g.sleep = false;
        if(g.play() == Piece.RED)
          wins++;
//        r.reset();
      if(i%500 == 0)
      {
        System.out.println("wins: " + wins + " " + (wins+0.0)/(i+1));
      }
      }
      System.out.println("wins: " + wins + " " + (wins+0.0)/games);
//      System.out.println("wins: " + wins + " " + (wins+0.0)/games);
      wins = 0;
//    }
//    out.close();
  }
  
  
  
  private Piece[][] board;
  private Player p1; // RED Player
  private Player p2; // Blue Player
  private Display display;
  public boolean sleep;
  private long time;
  
  public Game(Strategy strat1, Strategy strat2, Display display)
  {
    this.p1 = new Player(this, strat1, Piece.RED);
    this.p2 = new Player(this, strat2, Piece.BLUE);
    this.display = display;
    board = new Piece[10][10];
    
    if (display != null)
    {
      if (strat1 instanceof HumanStrategy)
        display.setPlayer(p1);
      else if (strat2 instanceof HumanStrategy)
        display.setPlayer(p2);
      else
        display.setGame(this);
    }
    time = System.currentTimeMillis();
  }
  
  //returns winning color
  public boolean play()
  {
    int[][] p1SetUp = p1.getSetUp();
    int[][] p2SetUp = p2.getSetUp();
    
    if(!Player.isValidSetUp(p1SetUp))
      throw new RuntimeException("p1 returned invalid setup!");
    if(!Player.isValidSetUp(p2SetUp))
      throw new RuntimeException("p2 returned invalid setup!");
    
    for(int r = 0; r < 4; r++)
    {
      for(int c = 0; c < 10; c++)
      {
        Location loc = new Location(r,c);
        setPiece(new Piece(Piece.RED, p1SetUp[r][c]), loc);
        setPiece(new Piece(Piece.BLUE, p2SetUp[r][c]), loc.rotated());
      }
    }
    
    while(countFlags() == 2 && canMove(p1))
    {
      if((System.currentTimeMillis() - time) > 5000)
        return Piece.BLUE;
      //RED turn
      executeMove(p1);
      
      if (countFlags() == 2 && canMove(p2))
      {
        //BLUE turn
        executeMove(p2);
      }
      else
        return Piece.RED;
    }
//    printBoard();
    
    return Piece.BLUE;
  }
  
  
  private void executeMove(Player p)
  {
    if(sleep)
    {
      try
      {
        Thread.sleep(50);
      }
      catch(InterruptedException e)
      {
          e.printStackTrace();
      }
    }
    //printBoard();
    Move move = p.getMove();
    //System.out.println("Move:  " + move);
    
    //update piece move/reveal status
    Piece movePiece = getPiece(move.getFrom()).moved();
    if (move.getFrom().distanceTo(move.getTo()) > 1)
      movePiece = movePiece.revealed();  //must be a scout
    setPiece(movePiece, move.getFrom());
    
    //assuming move is valid:
    if(getPiece(move.getTo()) != null) //destination is occupied - attack
    {
      attack(move);  //attacks AND notifies that move took place
    }
    else // destination is not occupied - just move
    {
      setPiece(movePiece, move.getTo());
      setPiece(null, move.getFrom());
      p1.moved(move, movePiece, null);
      p2.moved(move, movePiece, null);
    }
    
    if(display != null)
      display.update();
  }
  
  private Piece setPiece(Piece p, Location loc)
  {
    Piece ret = board[loc.getRow()][loc.getCol()];
    board[loc.getRow()][loc.getCol()] = p;
    return ret;
  }
  
  private Piece setPiece(Piece p, int r, int c)
  {
    Piece ret = board[r][c];
    board[r][c] = p;
    return ret;
  }
  
  private boolean canMove(Player p)
  {
    return !p.getAllMoves().isEmpty();
  }
  
  private void attack(Move move)
  {
    //System.out.println("attack:  " + attacker + " --> " + attacked);
    Piece attackerP = getPiece(move.getFrom());
    Piece attackedP = getPiece(move.getTo());
    int canCapture = attackerP.canCapture(attackedP);
    attackerP = attackerP.revealed();
    attackedP = attackedP.revealed();
    
    //System.out.println("canCapture = " + canCapture);
    
    if(canCapture > 0)
    {
      //attacker can capture opponent
      setPiece(null, move.getFrom());
      setPiece(attackerP, move.getTo());
    }
    
    else if(canCapture == 0)
    {
      setPiece(null, move.getFrom());
      setPiece(null, move.getTo());
    }
    
    else // (canCapture < 0)
    {
      setPiece(null, move.getFrom());
      setPiece(attackedP, move.getTo());
    }

    p1.moved(move, attackerP, attackedP);
    p2.moved(move, attackerP, attackedP);
  }
  
  public Piece getPiece(Location loc) //returns null if empty, crash if invalid
  {
    return board[loc.getRow()][loc.getCol()];
  }
  
  public String toString()
  {
    return p1.getClass().getName() + " vs. " + p2.getClass().getName();
  }
  
  private void printBoard()
  {
    for (int row = 0; row < board.length; row++)
    {
      for (int col = 0; col < board[0].length; col++)
      {
        if (board[row][col] == null)
          System.out.print("---- ");
        else
        {
          
          String rank = "" + board[row][col].getRank();
          while (rank.length() < 2)
            rank = "0" + rank;
          System.out.print((board[row][col].getColor() == Piece.RED ? "R" : "B") + rank + 
                           (board[row][col].hasRevealed() ? "r" : (board[row][col].hasMoved() ? "m" : "?"))
                             + " ");
        }
      }
      System.out.println();
    }
    System.out.println();
  }
  
  private int countFlags()
  {
    int count = 0;
    for (int row = 0; row < 10; row++)
    {
      for (int col = 0; col < 10; col++)
      {
        if (board[row][col] != null && board[row][col].getRank() == Piece.FLAG)
          count++;
      }
    }
    return count;
  }
}


