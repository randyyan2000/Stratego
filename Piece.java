public class Piece
{
  public static final boolean RED = true;
  public static final boolean BLUE = false;
  
  public static final int UNKNOWN = -1;
  public static final int SPY = 1;
  public static final int SCOUT = 2;
  public static final int MINER = 3;
  public static final int FLAG = 0;
  public static final int BOMB = 11;
  
  private int rank; //-1 = unknown, 0 = flag, 1 = spy, ..., 11 = bomb
  private boolean color;
  private boolean revealed; //automatically reveal scout when moved far
  private boolean moved;
  
  
  public static int getCount(int rank)
  {
    if(rank==0){return 1;}
    else if (rank==1){return 1;}
    else if(rank==2){return 8;}
    else if (rank==3){return 5;}
    else if (rank==4){return 4;}
    else if(rank==5){return 4;}
    else if (rank==6){return 4;}
    else if (rank==7){return 3;}
    else if(rank==8){return 2;}
    else if (rank==9){return 1;}
    else if (rank==10){return 1;}
    else if(rank==11){return 6;}
    throw new RuntimeException("invalid rank:  " + rank);
  }
  public Piece(boolean color, int rank)
  { 
    this.color = color;
    this.rank = rank;
  }
  
  private Piece(int rank, boolean color, boolean revealed, boolean moved)
  {
    this.rank = rank;
    this.color = color;
    this.revealed = revealed;
    this.moved = moved;
  }
  
  
  public int getRank()
  {
    return rank;
  }
  
  public boolean getColor()
  {
    return color;
  }
  
  public boolean hasRevealed()
  {
    return revealed;
  }
  
  public boolean hasMoved()
  {
    return moved;
  }
  
  public int canCapture(Piece other)
  {
    if (this.getRank() == 1 && other.getRank() == 10)
    {
      return 1;
    }
    else if (this.getRank()== 3 && other.getRank() == 11)
    {
      return 1;
      
    }
    else 
    {
      return (this.getRank() - other.getRank());
    }
    
  }
  public Piece moved() //maybe new Piece(this, false);
  { 
    return new Piece (getRank(), getColor(), hasRevealed(), true);
  }
  
  public Piece revealed()
  {
    return new Piece (getRank(), getColor(), true, hasMoved());
    
  }
  
  public Piece unknown() //maybe new Piece with type = -1 and color
  {
    return new Piece(-1, getColor(),false, hasMoved());
  }
  
  
//returns [color, rank, revealed, moved]
  public String toString()
  {
    String c;
    if (color==Piece.RED){c="red";}
    else{c="blue";}
    
    return "[" + c + ", rank: " + rank + ", revealed: " +    revealed + ", moved: " + moved +"]";
  }
}
