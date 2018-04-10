import java.util.*;

public class Player
{
  private boolean color;
  private Strategy strategy;
  private Game game;
  
  public Player(Game game, Strategy strat, boolean color)
  {
    this.game = game;
    strategy = strat;
    this.color = color;
    strategy.setPlayer(this);
  }
  public boolean getColor()
  {
    return color; 
  }
  public int[][] getSetUp()
  {
    return strategy.getSetUp();
  }
  
  public Move getMove()
  {
    return rotatedMove(strategy.getMove());
  }
  
  private Location rotatedLoc(Location loc)
  {
    if (color == Piece.BLUE)
      return loc.rotated();
    else
      return loc;
  }
  
  private Move rotatedMove(Move move)
  {
    if (color == Piece.BLUE)
      return move.rotated();
    else
      return move;
  }
  
  private Piece hiddenPiece(Piece p)
  {
    if(p != null)
    {
      if(p.getColor() == color)
        return p;
      else if(p.hasRevealed())
        return p;
      else
        return p.unknown(); 
    }
    return null;
  }
  
  public Piece getPiece(Location loc)
  {
    return hiddenPiece(game.getPiece(rotatedLoc(loc)));
  }
  
  public boolean isValidMove(Move move)
  {
    return getAllMoves().contains(move);
  }
  
  public List<Move> getAllMoves()
  {
    List<Move> moves = new ArrayList<Move>();
    for(int row = 0; row < 10; row++)
    {
      for(int col = 0; col < 10; col++)
      {
        Location loc = new Location(row, col);
        if(!loc.isValid()) continue;
        Piece p = game.getPiece(new Location(row, col));
        if(p != null && p.getRank() != Piece.BOMB &&
           p.getRank() != Piece.FLAG && p.getColor() == color)
        {
          if(p.getRank() == Piece.SCOUT)
          {
            List<Move> scoutMoves = sweep(loc);
            for(Move m : scoutMoves)
              moves.add(m);
          }
          else
          {
            Location to = new Location(row + 1, col);
            if(to.isValid() && to.distanceTo(loc) == 1)
            {
              if(game.getPiece(to) == null)
                moves.add(new Move(loc, to));
              else if(game.getPiece(to).getColor() != color)
                moves.add(new Move(loc, to));     
            }
            to = new Location(row, col + 1);
            if(to.isValid() && to.distanceTo(loc) == 1)
            {
              if(game.getPiece(to) == null)
                moves.add(new Move(loc, to));
              else if(game.getPiece(to).getColor() != color)
                moves.add(new Move(loc, to));     
            }
            to = new Location(row - 1, col);
            if(to.isValid() && to.distanceTo(loc) == 1)
            {
              if(game.getPiece(to) == null)
                moves.add(new Move(loc, to));
              else if(game.getPiece(to).getColor() != color)
                moves.add(new Move(loc, to));     
            }
            to = new Location(row, col - 1);
            if(to.isValid() && to.distanceTo(loc) == 1)
            {
              if(game.getPiece(to) == null)
                moves.add(new Move(loc, to));
              else if(game.getPiece(to).getColor() != color)
                moves.add(new Move(loc, to));     
            }
          }
        }
      }
    }
    ArrayList<Move> rotatedMoves = new ArrayList<Move>();
    for (Move move : moves)
      rotatedMoves.add(rotatedMove(move));
    return rotatedMoves;
  }
  
  private List<Move> sweep(Location loc)
  {
    List<Move> moves = new ArrayList<Move>();
    for(int row = loc.getRow() - 1; row <= 9 && row >= 0; row--) {
      Location newLoc = new Location(row, loc.getCol());
      if(newLoc.isValid()) {
        if (game.getPiece(newLoc) == null)
          moves.add(new Move(loc, newLoc));
        else if (game.getPiece(newLoc).getColor() == color)
          break;
        else {
          moves.add(new Move(loc, newLoc));
          break;
        }
      }
      else break;
    }
    for(int row = loc.getRow() + 1; row <= 9 && row >= 0; row++) {
      Location newLoc = new Location(row, loc.getCol());
      if(newLoc.isValid()) {
        if (game.getPiece(newLoc) == null)
          moves.add(new Move(loc, newLoc));
        else if (game.getPiece(newLoc).getColor() == color)
          break;
        else {
          moves.add(new Move(loc, newLoc));
          break;
        }
      }
      else break;
    }
    for(int col = loc.getCol() - 1; col <= 9 && col >= 0; col--) {
      Location newLoc = new Location(loc.getRow(), col);
      if(newLoc.isValid()) {
        if (game.getPiece(newLoc) == null)
          moves.add(new Move(loc, newLoc));
        else if (game.getPiece(newLoc).getColor() == color)
          break;
        else {
          moves.add(new Move(loc, newLoc));
          break;
        }
      }
      else break;
    }
    for(int col = loc.getCol() + 1; col <= 9 && col >= 0; col++) {
      Location newLoc = new Location(loc.getRow(), col);
      if(newLoc.isValid()) {
        if (game.getPiece(newLoc) == null)
          moves.add(new Move(loc, newLoc));
        else if (game.getPiece(newLoc).getColor() == color)
          break;
        else {
          moves.add(new Move(loc, newLoc));
          break;
        }
      }
      else break;
    }
    return moves;
  }
  
  public static int[][] randomSetUp()
  {
    //board is the final product of the random process
    int[][] board = new int[4][10];
    //pieces is the list of all pieces that are in the game
    int[] pieces = new int[40];
    //indices indicate the places the pieces should go
    int[] indices = new int[40];
    //fills indices with numbers 1-40
    for (int i = 0; i < 40; i++)
      indices[i] = i;
    //randomizes indices
    for (int i = 0; i < 40; i++)
    {
      int newLoc = (int)(40*Math.random());
      int temp = indices[i];
      indices[i] = indices[newLoc];
      indices[newLoc] = temp;
    }
    //adds pieces
    for (int i = 0; i < 40; i++)
    {
      if (i == 0)
        pieces[i] = 0;
      else if (i == 1)
        pieces[i] = 1;
      else if (i <= 9)
        pieces[i] = 2;
      else if (i <= 14)
        pieces[i] = 3;
      else if (i <= 18)
        pieces[i] = 4;
      else if (i <= 22)
        pieces[i] = 5;
      else if (i <= 26)
        pieces[i] = 6;
      else if (i <= 29)
        pieces[i] = 7;
      else if (i <= 31)
        pieces[i] = 8;
      else if (i == 32)
        pieces[i] = 9;
      else if (i == 33)
        pieces[i] = 10;
      else if (i <= 39)
        pieces[i] = 11;
    }
    //arranges pieces on board based on index
    for (int i = 0; i < 40; i++)
    {
      int row = indices[i] / 10;
      int col = indices[i] % 10;
      board[row][col] = pieces[i];
    }
    
    return board;
  }
  
  
  public static boolean isValidSetUp(int[][] setUp)
  {
    //checks dimensions of board to ensure they're right
    if (setUp.length != 4 || setUp[0].length != 10)
      return false;
    else
    {
      //dimensions are right - tally sort of pieces
      int[] pieces = new int[12];
      for (int row = 0; row < 4; row++)
      {
        for (int col = 0; col < 10; col++)
        {
          pieces[setUp[row][col]]++;
        }
      }
      //makes sure the number of pieces of each rank is correct and you have 40 pieces on the board
      return sumOfArray(pieces) == 40 &&pieces[0] == 1 && pieces[1] == 1 && pieces[2] == 8 && pieces[3] == 5 && pieces[4] == 4 && pieces[5] == 4
        && pieces[6] == 4 && pieces[7] == 3 && pieces[8] == 2 && pieces[9] == 1 && pieces[10] == 1 && pieces[11] == 6;
    }
  }
  
  //this sums the ints in an array
  private static int sumOfArray(int[] arr)
  {
    int sum = 0;
    for (int i = 0; i < arr.length; i++)
      sum += arr[i];
    return sum;
  }
  
  public int[] getRemainingRanks(boolean color)
  {
    int[] remainingRanks = new int[12];
    for (int row = 0; row < 10; row++)
    {
      for (int col = 0; col < 10; col++)
      {
        Piece piece = game.getPiece(new Location(row, col));
        if (piece != null && piece.getColor() == color)
          remainingRanks[piece.getRank()]++;
      }
    }
    return remainingRanks;
  }
  
  public void moved(Move move, Piece movingPiece, Piece attackedPiece)
  {
    strategy.moved(rotatedMove(move), hiddenPiece(movingPiece), hiddenPiece(attackedPiece));
  }
}
