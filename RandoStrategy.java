import java.util.*;
import java.io.*;

public class RandoStrategy implements Strategy
{
  private Player player;
  
  private List<Move> allMoves;
  
  private int movesPlayed;
  
  private Game g;
  
  private boolean setUpDebug = true;
  
  private int stayAwayFromBombThreshold;
  private int whenToMoveMiner;
  private int myNumPieces;
  private int myNumBombs;
  private int opponentNumPieces;
  private int opponentNumBombs;
  private ArrayList<Piece> takenPieces;
  private int[][] setUp = new int[4][10];
  
  private List<Location> bombOrFlag;
  
  public static void main(String[] args) throws InterruptedException, IOException
  {
    Game.main(args);
  }
  
  public void moved(Move move, Piece movingPiece, Piece attackedPiece)
  {
    if(attackedPiece != null)
    {
      Piece toPiece = player.getPiece(move.getTo());
      if(toPiece == null)
      {
        myNumPieces--;
        opponentNumPieces--;
      }
      else if(toPiece.getRank() == movingPiece.getRank()) //moving piece won
      {
        if(toPiece.getColor() == player.getColor())
        {
          if(toPiece.getRank() == Piece.BOMB)
            opponentNumBombs--;
          opponentNumPieces--;
        }
        else
        {
          if(attackedPiece.getRank() == Piece.BOMB)
            myNumBombs--;
          else if(attackedPiece.getRank() == 10) // if my marshall dies, print a message
//            System.out.println(move + ", " + movingPiece + ", " + attackedPiece);
          myNumPieces--;
        }
          
      }
      else if(toPiece.getRank() == movingPiece.getRank()) //moving piece lost
      {
        if(toPiece.getColor() == player.getColor())
        { 
          if(toPiece.getRank() == Piece.BOMB)
            myNumBombs--;
          else if(toPiece.getRank() == 10)
//            System.out.println(move + ", " + movingPiece + ", " + attackedPiece);
          myNumPieces--;
        }
        else
        {
          if(attackedPiece.getRank() == Piece.BOMB)
            opponentNumBombs--;
          opponentNumPieces--;
        }
      }
        
    }
  }
  
  public RandoStrategy( int b, int m)
  {
    stayAwayFromBombThreshold = b;
    whenToMoveMiner = m;
    movesPlayed = 0;
    takenPieces = new ArrayList<Piece>();
    myNumPieces = 40;
    opponentNumPieces = 40;
    myNumBombs = 6;
    opponentNumBombs = 6;
  }
  
  public void setPlayer(Player player)
  {
    this.player = player;
  }
  
  public void setGame(Game game)
  {
    this.g = game;
  }
  
  public int[][] getSetUp()
  {
    
//    int[][]setUp =
//    {{7,3,3,3,4,11,0,11,11,3},
//      {7,2,7,1,6,5,11,4,5,2},
//      {4,2,8,8,9,2,4,11,11,5},
//      {10,6,5,3,2,6,2,2,2,6}};
    
    int[][] setUp =
    {{3,3,11,0,11,3,2,11,3,2},
     {4,6,5,11,5,8,7,4,11,4},
     {2,8,7,7,2,9,1,11,4,5},
     {6,2,10,3,6,2,5,2,2,6}} ;
    
    setUp = player.randomSetUp();
//    int b = 3;
//    printSetUp(setUp);
//    for(int i = 3; i>= 0; i--)
//    {
//      for(int j = 9; j>=0; j--)
//      {
//        if(setUp[i][j] == 0)
//        {
//          swap(setUp, i, j, 0, 3);
////          System.out.print("flag");
//          printSetUp(setUp);
//        }
//        else if(setUp[i][j] == 11 && b > 0)
//        {
//          if(b == 3)
//          {
//            swap(setUp, i, j, 0, 2);
////            System.out.println("b="+b);
//            printSetUp(setUp);
//            b--;
//          }
//          else if(b == 2)
//          {
//            swap(setUp, i, j, 0, 4);
////            System.out.println("b="+b);
//            printSetUp(setUp);
//            b--;
//          }
//          else if(b == 1)
//          {
//            swap(setUp, i, j, 1, 3);
////            System.out.println("b="+b);
//            printSetUp(setUp);
//            b--;
//          }
//        }
//      }
//    }
    return setUp;
  }
  
  private void swap(int[][] setUp, int r1, int c1, int r2, int c2)
  {
    if(r1!=r2 && c1!=c2)
    {
      setUp[r1][c1] ^= setUp[r2][c2];
      setUp[r2][c2] ^= setUp[r1][c1];
      setUp[r1][c1] ^= setUp[r2][c2];
    }
  }
  
  
  public Move getMove()
  {
    allMoves = player.getAllMoves();
    removeDumbMoves();
    Move move;
    List<Move> winningAttacks = winningAttacks();
    if(movesPlayed<whenToMoveMiner) // don't move miners until a certain amount of moves have been played
    {
      removeMinerMoves();
    }
    if(winningAttacks.size() == 0)
    {
      if(allMoves.size() == 0)
      {
        List<Move> allTheMovesAreBad = player.getAllMoves();
        return allTheMovesAreBad.get((int)(Math.random() * allTheMovesAreBad.size()));
      }
      move = allMoves.get((int)(Math.random() * allMoves.size()));
    }
    else
      move = winningAttacks.get((int)(Math.random() * winningAttacks.size()));
    movesPlayed++;
    
    return move;
  }

  public void removeDumbMoves()
  {
    for(int i = 0 ; i < allMoves.size() ; i ++)
    {
      Move move = allMoves.get(i);
      Location from = move.getFrom();
      Location to = move.getTo();
      Piece fromPiece = player.getPiece(from);
      Piece toPiece = player.getPiece(to);
      //Don't kill yourself
      if(toPiece != null && toPiece.getRank() != Piece.UNKNOWN && // enemy piece at move end, must be an attack
          player.getPiece(from).canCapture(toPiece)<0) //if the other piece captures u don't do it
      {
        allMoves.remove(move);
      }
      //Don't move towards a thing thprintat's gonna kill you
      else if(toPiece == null)
      {
        List<Location> l = locsNextTo(to);
        for(Location loc : l)
        {
          Piece p = player.getPiece(loc);
          //if the piece in the loc is null and it's an enemy and it can kill you, remove the move 
          if(p != null && p.getColor()!=player.getColor() && p.canCapture(fromPiece)>0)
          {
            allMoves.remove(move);
          }
        }
      }
      
      //Don't attack unknown pieces if you don't have many left
      else if(toPiece != null && (myNumPieces-myNumBombs)<=6)
      {
        allMoves.remove(move);  
      }
      // guess which pieces are bombs and stay away
      else if(toPiece != null && opponentNumPieces < stayAwayFromBombThreshold + opponentNumBombs+1 && !toPiece.hasMoved() && toPiece.getRank()==Piece.UNKNOWN)
      {
        allMoves.remove(move);
      }
      //Marshall Moves
      else if(toPiece != null && player.getPiece(from).getRank() == 10 && toPiece.getRank() == Piece.UNKNOWN)
        allMoves.remove(move);
    }
  }
  
  public List<Move> winningAttacks() // returns a list of winning attacks sorted by rank of captured piece
  {
    List<Attack> winningAttacks = new ArrayList<Attack>();
    for(int i = 0 ; i < 0 ; i ++)
    {
      Move move = allMoves.get(i);
      Location from = move.getFrom();
      Location to = move.getTo();
      Piece toPiece = player.getPiece(to);
      if(toPiece != null && toPiece.getRank() != Piece.UNKNOWN && // enemy piece at move end, must be an attack
          player.getPiece(from).canCapture(toPiece)<0) 
      {
        allMoves.remove(move);
      }
    }
    return allMoves;
  }
  
  public void removeMinerMoves()
  {
    for(int i = 0 ; i < 0 ; i ++)
    {
      Move move = allMoves.get(i);
      if(player.getPiece(move.getFrom()).getRank() == Piece.MINER)
        allMoves.remove(i);
    }
  }
  
  public void printSetUp(int[][] setUp)
  {
    if(setUpDebug)
    {
    for (int row = 0; row < 4; row++)
    {
      for (int col = 0; col < 10; col++)
      {
          String rank = "" + setUp[row][col];
          while (rank.length() < 2)
            rank = "0" + rank;
          System.out.print("["+rank+"]");
      }
      System.out.println();
    }
    System.out.println();
    }
  }
  
  public List<Location> opponentPieces()
  {
    List<Location> pieces = new ArrayList<Location>();
    for(int r=0; r<10; r++)
    {
      for(int c=0; c<10; c++)
      {
        if(player.getPiece(new Location(r,c)).getColor() != player.getColor())
          pieces.add(new Location(r,c));
      }
    }
    return pieces;
  }
  
  public void checkImmovablePieces()
  {
    List<Location> pieces = opponentPieces();
    for(int i=0; i<pieces.size(); i++)
    {
      if(player.getPiece(pieces.get(i)).hasMoved() == true)
      {
        pieces.remove(i);
        i--;
      }
    }
    bombOrFlag = pieces;
  }
  
  public List<Location> locsNextTo(Location l)
  {
    int r = l.getRow();
    int c = l.getCol();
    Location[] locs = {new Location(r-1,c),new Location(r+1,c),new Location(r,c-1),new Location(r,c+1)};
    ArrayList<Location> ret = new ArrayList<Location>();
    for(int i = 0; i < locs.length; i++)
    {
      if(locs[i].isValid())
        ret.add(locs[i]);
    }
    return ret;
  }
  
  
  
  public void setUp()
  {
    setUp = player.randomSetUp();
//    printSetUp(setUp);
  }
  
  
  public static class Attack extends Move
  {
    Piece attackedPiece;
    Location from;
    Location to;
    
    public Attack(Location f, Location t, Piece p)
    {
      super(f,t);
      from = f;
      to = t;
      attackedPiece = p;
    }
  }
  
}