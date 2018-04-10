import java.util.*;

public class RandomStrategy implements Strategy
{
  private Player player;
  
  public RandomStrategy() //this exists
  {}
  
  public void setPlayer(Player p) //sets player equal to p
  {
    player = p;
  }
  
  public int[][] getSetUp() //returns a random set up
  {
    return player.randomSetUp();
  }
  
  public Move getMove()
  {
    List<Move> allMoves = player.getAllMoves(); //finds all legal moves
    int move = (int)(Math.random() * allMoves.size());
    //finds a random valid index
    return allMoves.get(move); //returns the legal move at the index
  }
  
  public void moved(Move move, Piece movingPiece, Piece attackedPiece)
  {
    //ignored
  }
}

