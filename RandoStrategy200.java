import java.util.*;
import java.io.*;

public class RandoStrategy200 implements Strategy
{
  Player player;
  
  private MoveV[] allMoves;
  
//  private Piece[][] opponentPieces;
  private int[] opponentPieces;
  private int[] myPieces;
  
  private boolean myMarshallRevealed;
  
  private boolean killMarshall;
  private Location opMarshallLoc;
  
  private int possibleBombs;
  
  private int myNumPieces = 40;
  private int opNumPieces = 40;
  
  private int bombThreshold = 11;
  
  private Location flagLoc;
  
  private boolean autoWin;
  private ArrayList<Location> attacProtecList;
  
  private int myBestPiece;
  private int opBestPiece;
  
  private int[][] setUp;
  
  
  //various params
  private int spyWorth = 9;
  
  public RandoStrategy200()
  {
//    opponentPieces = new Piece[12][];
//    opponentPieces[0] = new Piece[1];
//    opponentPieces[1] = new Piece[1];
//    opponentPieces[2] = new Piece[8];
//    opponentPieces[3] = new Piece[5];
//    opponentPieces[4] = new Piece[4];
//    opponentPieces[5] = new Piece[4];
//    opponentPieces[6] = new Piece[4];
//    opponentPieces[7] = new Piece[3];
//    opponentPieces[8] = new Piece[2];
//    opponentPieces[9] = new Piece[1];
//    opponentPieces[10] = new Piece[1];
//    opponentPieces[11] = new Piece[6];
    opponentPieces = new int[12];
    opponentPieces[0] = 1;
    opponentPieces[1] = 1;
    opponentPieces[2] = 8;
    opponentPieces[3] = 5;
    opponentPieces[4] = 4;
    opponentPieces[5] = 4;
    opponentPieces[6] = 4;
    opponentPieces[7] = 3;
    opponentPieces[8] = 2;
    opponentPieces[9] = 1;
    opponentPieces[10] = 1;
    opponentPieces[11] = 6;
    myPieces = new int[12];
    myPieces[0] = 1;
    myPieces[1] = 1;
    myPieces[2] = 8;
    myPieces[3] = 5;
    myPieces[4] = 4;
    myPieces[5] = 4;
    myPieces[6] = 4;
    myPieces[7] = 3;
    myPieces[8] = 2;
    myPieces[9] = 1;
    myPieces[10] = 1;
    myPieces[11] = 6;
  }
  
  public void reset()
  {
    opponentPieces = new int[12];
    opponentPieces[0] = 1;
    opponentPieces[1] = 1;
    opponentPieces[2] = 8;
    opponentPieces[3] = 5;
    opponentPieces[4] = 4;
    opponentPieces[5] = 4;
    opponentPieces[6] = 4;
    opponentPieces[7] = 3;
    opponentPieces[8] = 2;
    opponentPieces[9] = 1;
    opponentPieces[10] = 1;
    opponentPieces[11] = 6;
    myPieces = new int[12];
    myPieces[0] = 1;
    myPieces[1] = 1;
    myPieces[2] = 8;
    myPieces[3] = 5;
    myPieces[4] = 4;
    myPieces[5] = 4;
    myPieces[6] = 4;
    myPieces[7] = 3;
    myPieces[8] = 2;
    myPieces[9] = 1;
    myPieces[10] = 1;
    myPieces[11] = 6;
  }
  
  public int[][] getSetUp()
  {
//    int[][] set =
//    {{3,3,11,0,11,3,2,11,3,2},
//     {4,5,5,11,1,8,7,4,11,4},
//     {2,8,7,7,2,9,6,11,4,5},
//     {6,2,10,3,6,2,5,2,2,6}};
//    setUp = set;
//    setUp = player.randomSetUp();
    
    int[][] set = 
    {{2,2,3,7,7,5,2,11,0,11},
      {2,6,11,8,2,11,5,11,11,4},
      {6,2,3,3,6,9,2,6,1,5},
      {4,2,5,3,4,10,4,7,8,3}};

//wins: 94 0.94
    
    
//    [02][11][02][11][08][04][11][00][11][02]
//[09][05][03][02][06][03][11][11][02][06]
//[05][02][04][07][03][03][06][02][04][07]
//[07][05][04][03][10][02][06][05][08][01]
//
//wins: 88 0.88
    
//    [03][02][05][07][04][11][05][11][00][11]
//[02][05][02][11][06][07][05][08][11][02]
//[02][09][03][03][10][04][04][01][06][06]
//[03][02][03][02][02][04][07][08][06][11]
//
//wins: 87 0.87
    
    setUp = set;
    
    findFlag(setUp);
    return setUp;
  }
  
  public void setUp()
  {
    setUp = player.randomSetUp();
    int flagC = (int)(Math.random()*10);
    findFlag(setUp);
    swap(setUp, flagLoc.getRow(), flagLoc.getCol(), 0, flagC);
    ArrayList<Location> bombsN = new ArrayList<Location>();
    if(flagC != 0)
      bombsN.add(new Location(0, flagC-1));
    bombsN.add(new Location(1, flagC));
    if(flagC != 9)
      bombsN.add(new Location(0, flagC+1));
    
    ArrayList<Location> bombsC = findBombs(setUp);
    
    for(int i = bombsC.size()-1; i >= 0; i--)
    {
      for(int j = 0; j < bombsN.size(); j++)
      {
        if(bombsC.get(i).equals(bombsN.get(j)))
        {
          bombsC.remove(i);
          break;
        }
      }
    }
    System.out.println("dupes removed");
    
    for(int j = 0; j < bombsN.size(); j++)
    {
      Location l1 = bombsN.get(j);
      Location l2 = bombsC.get(j);
      swap(setUp, l1.getRow(),l1.getCol(),l2.getRow(),l2.getCol());
    }
    System.out.println("setup complete");
  }
  
  public ArrayList<Location> findBombs(int[][] setUp)
  {
    ArrayList<Location> ret = new ArrayList<Location>();
    for(int r = 3; r >= 0; r--)
    {
      for(int c = 0; c < 10; c++)
      {
        if(setUp[r][c] == 11)
          ret.add(new Location(r,c));
      }
    }
    return ret;
  }
  
  private void swap(int[][] setUp, int r1, int c1, int r2, int c2)
  {
    int temp = setUp[r1][c1];
    setUp[r1][c1] = setUp[r2][c2];
    setUp[r2][c2] = temp;
  }
  
  public void findFlag(int[][] setUp)
  {
    for(int r = 0; r < 4; r++)
    {
      for(int c = 0; c < 10; c++)
      {
        if(setUp[r][c] == 0)
        {
          flagLoc = new Location(r,c);
//          if(player.getColor() == Piece.RED)
//            flagLoc = flagLoc.rotated();
          return;
        }
      }
    }
  }
  
  public void findFlag()
  {
    for(int r = 0; r < 10; r++)
    {
      for(int c = 0; c < 10; c++)
      {
        Location loc = new Location(r,c);
        if(player.getPiece(loc) != null &&
           player.getPiece(loc).getColor() == player.getColor() &&
           player.getPiece(loc).getRank() == Piece.FLAG)
        {
          flagLoc = loc;
          return;
        }
      }
    }
  }
  
  public void setPlayer(Player player)
  {
    this.player = player;
    
  }
  
  public Move getMove()
  {
    if(flagLoc == null)
      findFlag();
    List<Move> l = player.getAllMoves();
    allMoves = new MoveV[l.size()];
    for(int i = 0; i < l.size(); i++)
    {
      allMoves[i] = (new MoveV(l.get(i),0));
    }
    possibleBombs();
    Collections.shuffle(Arrays.asList(allMoves));
    if(myBestPiece > opBestPiece)
    {
      if(!(myBestPiece == 10 && opponentPieces[1] == 1))
      {
        autoWin = true;
//        System.out.println(flagLoc);
        attacProtecList = attacProtec();
//        System.out.println(attacProtecList);
      }
    }
    dontKYS();
    Arrays.sort(allMoves);
//    System.out.print("autowin: " + autoWin);
//    System.out.println(Arrays.toString(allMoves));
    return allMoves[allMoves.length-1];
  }
  
  public void moved(Move move, Piece movingPiece, Piece attackedPiece)
  {
    if(attackedPiece != null) // move was an attack
    {
      Piece toPiece = player.getPiece(move.getTo()); //remaining piece at to loc
      if(toPiece == null) //to loc is empty, pieces traded
      {
        myNumPieces--;
        myPieces[attackedPiece.getRank()]--;
        opNumPieces--;
        opponentPieces[attackedPiece.getRank()]--;
      }
      else
      {
        if(toPiece.getColor() == player.getColor()) // i won attack
        {
          opNumPieces--;
          opponentPieces[attackedPiece.getRank()]--;
          if(toPiece.getRank() == 10)
            myMarshallRevealed = true;
        }
        else if(toPiece.getColor() != player.getColor()) // i lost
        {
          myPieces[attackedPiece.getRank()]--;
          myNumPieces--;
          if(toPiece.getRank() == 10)
          {
            killMarshall = true;
            opMarshallLoc = move.getTo();
          }
        }
        else // my code is ass
          throw new RuntimeException("i forfeit");
      }
    }
    else
    {
      if(movingPiece.getColor() != player.getColor() &&
         movingPiece.getRank() == 10)
      {
        opMarshallLoc = move.getTo();
      }
    }
  }
  
  public void dontKYS()
  {
    for(int i = 0; i < allMoves.length; i++)
    {
      MoveV move = allMoves[i];
      Piece fromPiece = player.getPiece(move.getFrom());
      Piece toPiece = player.getPiece(move.getTo());
      
      if(toPiece != null) // enemy piece at move end, must be an attack
      {
         if(toPiece.getRank() != Piece.UNKNOWN) //attacking known piece
         {
           int result = fromPiece.canCapture(toPiece);
           if(result < 0) //if the other piece captures u don't do it
             move.value = -getValue(fromPiece);
           else if(result == 0)
           {
             if(fromPiece.getRank() == 10 && hasBestPiece(true)) // trading tens
             {
               move.value = 10;
             }
             else
             {
               move.value = fromPiece.getRank()/2.0;
             }
           }
           else if(result > 0) // if you capture the other piece
           {
             move.value = getValue(toPiece) + 1 - ((double)getValue(fromPiece))/10.0;
             int distanceToFlag = move.getTo().distanceTo(flagLoc);
             if( distanceToFlag == 1 )
             {
               move.value += 10;
             }
             if(fromPiece.getRank() == 10 && opponentPieces[1] == 1)
             {
               if(surrounded(move.getTo(), true) > 0)
                 move.value = -9;
             }
           }
         }
         else
         {
           if(fromPiece.getRank() == 2) // attacking unknown with Scout (2)
           {
             move.value = 5;
           }
           else if(fromPiece.getRank() == 10) // attacking unknown with Marshall (10)
           {
             if(opponentPieces[0] == 1) // no spy to worry about
             {
               if(toPiece.hasMoved()) // now counts as winning attack
               {
                 move.value = 6;
                 if( move.getTo().distanceTo(flagLoc) == 1 )
                 {
                   move.value += 10;
                 }
               }
               else
               {
                 move.value = -9;
               }
             }
             else // spy is alive be careful
             {
               if(toPiece.hasMoved()) // now counts as winning attack
               {
                 move.value = 6;
                 if( move.getTo().distanceTo(flagLoc) == 1 )
                 {
                   move.value += 10;
                 }
               }
               else if(possibleBombs < ( 1 + opponentPieces[11] + 7 ) )
               {
                 move.value = -9;
               }
               move.value -= 6 + surrounded(move.getTo(), true) * 3;
             }
           }
           else if(fromPiece.getRank() == 9) // attacking with 9
           {
             if(opponentPieces[10] == 0 && toPiece.hasMoved())
             {
               move.value = 7;
             }
             else if (killMarshall && toPiece.hasMoved())
             {
               move.value = 7;
             }
           }
           else if(fromPiece.getRank() == 3)
           {
             if(possibleBombs < ( 1 + opponentPieces[11] + 5 ) )
             {
               move.value = 6;
             }
           }
           else
           {
             if(fromPiece.getRank() > opBestPiece)
               move.value = 6;
             else if(fromPiece.getRank() == myBestPiece)
               move.value = -10;
             else
               move.value = 10 - getValue(fromPiece);
           }
         }
      }
      else
      {
        if(!fromPiece.hasMoved())
        {
          if(move.getFrom().getRow()<4 && move.getTo().getRow() < move.getFrom().getRow())
            move.value--;
        }
        if(fromPiece.getRank() == 1)
        {
          if(surrounded(move.getFrom())>0 && opponentPieces[10]==1)
          {
            if(surrounded(move.getTo()) == 0)
              move.value = 9;
          }
          move.value = -9.5;
        }
        if(killMarshall)
        {
          if(fromPiece.getRank() == 1)
          {
            Location from = move.getFrom();
            Location to = move.getTo();
            if(!(surrounded(to) == 0))
            {
              if(to.distanceTo(opMarshallLoc)<from.distanceTo(opMarshallLoc))
                move.value = 7;
            }
            if(to.distanceTo(opMarshallLoc) == 1)
              move.value = -10;
          }
        }
        if(fromPiece.getRank() == 10 && opponentPieces[1] == 1)
        {
          if(surrounded(move.getTo(), true) > 0)
            move.value = -9;
        }
        if(autoWin && fromPiece.getRank() > opBestPiece)
        {
          Location from = move.getFrom();
          Location to = move.getTo();
          if(attacProtecList.size()!= 0)
          {
            if(to.distanceTo(attacProtecList.get(0)) < from.distanceTo(attacProtecList.get(0)))
            {
              move.value = 10;
            }
          }
        }
      }
    }
  }
  
  public int getValue(Piece p)
  {
    if(opponentPieces[10] == 1 && p.getColor() == player.getColor() && p.getRank() == 1)
    {
      return spyWorth;
    }
    if(p.getRank() == 11)
      return 7;
      
    
    return p.getRank();
  }

  public int surrounded(Location loc)
  {
    int r = loc.getRow();
    int c = loc.getCol();
    Location up = new Location(r-1, c);
    Location down = new Location(r+1, c);
    Location left = new Location(r, c-1);
    Location right = new Location(r, c+1);
    int ret = 0;
    if(occupiedByEnemy(up))
      ret++;
    if(occupiedByEnemy(down))
      ret++;
    if(occupiedByEnemy(left))
      ret++;
    if(occupiedByEnemy(right))
      ret++;
    return ret;
  }
  
  public int surrounded(Location loc, boolean unknown)
  {
    int r = loc.getRow();
    int c = loc.getCol();
    Location up = new Location(r-1, c);
    Location down = new Location(r+1, c);
    Location left = new Location(r, c-1);
    Location right = new Location(r, c+1);
    int ret = 0;
    if(occupiedByEnemy(up, unknown))
      ret++;
    if(occupiedByEnemy(down, unknown))
      ret++;
    if(occupiedByEnemy(left, unknown))
      ret++;
    if(occupiedByEnemy(right, unknown))
      ret++;
    return ret;
  }

  public boolean occupiedByEnemy(Location loc)
  {
    return loc.isValid() && player.getPiece(loc) != null && player.getPiece(loc).getColor()!=player.getColor();
  }
  
  public boolean occupiedByEnemy(Location loc, boolean unknown)
  {
    if(unknown)
    {
      return loc.isValid() && 
      player.getPiece(loc) != null && 
      player.getPiece(loc).getColor()!=player.getColor() && 
      player.getPiece(loc).getRank() == Piece.UNKNOWN;
    }
    else
      return occupiedByEnemy(loc);
  }
  
  public void possibleBombs()
  {
    int ret = 0;
    myBestPiece = 0;
    opBestPiece = 0;
    for(int r = 0; r < 10; r++)
    {
      for(int c = 0; c < 10; c++)
      {
        Piece p = player.getPiece(new Location(r,c));
        if(p != null && p.getColor() != player.getColor())
        {
          if(!p.hasMoved())
          {
            ret++;
          }
          if(p.getRank() < 11 && p.getRank() > opBestPiece)
            opBestPiece = p.getRank();
        }
        else if(p != null && p.getColor() == player.getColor())
        {
          if(p.getRank() < 11 && p.getRank() > myBestPiece)
            myBestPiece = p.getRank();
        }
      }
    }
    possibleBombs = ret;
  }
  
  public ArrayList<Location> attacProtec()
  {
    ArrayList<Location> ret = new ArrayList<Location>();
    int minD = 20;
    for(int r = 0; r < 10; r++)
    {
      for(int c = 0; c < 10; c++)
      {
        Location loc = new Location(r,c);
        if(loc.isValid() && player.getPiece(loc) != null && player.getPiece(loc).getColor() != player.getColor())
        {
          if(player.getPiece(loc).hasMoved())
          {
            int d = loc.distanceTo(flagLoc);
//            System.out.print(d+ ",");
            if(d < minD)
            {
              minD = d;
              ret = new ArrayList<Location>();
              ret.add(loc);
            }
            else if(d == minD)
            {
              ret.add(loc);
            }
          }
        }
      }
    }
    return ret;
  }
  
  public boolean hasBestPiece()
  {
    if(myPieces[10] == 1)
      return true;
    else if(opponentPieces[10] == 1)
      return false;
    else
    {
      if(myPieces[9] == 1)
        return true;
      else if(myPieces[8] == 1 && opponentPieces[9] == 0)
        return true;
    }
    return false;
  }
  
  public boolean hasBestPiece(boolean trade10)
  {
    if(trade10)
    {
      myPieces[10] = 0;
      opponentPieces[10] = 0;
      boolean ret = hasBestPiece();
      myPieces[10] = 1;
      opponentPieces[10] = 1;
      return ret;
    }
    else
      return hasBestPiece();
  }
  
  public void updateBestPiece()
  {
    myBestPiece = 0;
    opBestPiece = 0;
    for(int i = 10; i >= 1; i++)
    {
      if(opponentPieces[i] > opBestPiece)
        opBestPiece = i;
      if(myPieces[i] > myBestPiece)
        myBestPiece = i;
    }
  }
  
  public void printSetUp(PrintWriter out)
  {
    for (int row = 0; row < 4; row++)
    {
      for (int col = 0; col < 10; col++)
      {
          String rank = "" + setUp[row][col];
          while (rank.length() < 2)
            rank = "0" + rank;
          out.print("["+rank+"]");
      }
      out.println();
    }
    out.println();
  }
  
  public void printSetUp(PrintStream out)
  {
    for (int row = 0; row < 4; row++)
    {
      for (int col = 0; col < 10; col++)
      {
          String rank = "" + setUp[row][col];
          while (rank.length() < 2)
            rank = "0" + rank;
          out.print("["+rank+"]");
      }
      out.println();
    }
    out.println();
  }
  
  public static class MoveV extends Move implements Comparable<Object>
  {
    double value;
    Location from;
    Location to;
    
    public MoveV(Move m, int v)
    {
      super(m.getFrom(),m.getTo());
      value = v;
    }
    
    public int compareTo(Object o)
    {
      return (int)Math.signum(value - ((MoveV)o).value);
    }
    
    public String toString()
    {
      return super.toString() + "val:" + value;
    }
  }
}