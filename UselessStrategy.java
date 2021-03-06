import java.util.*;

public class UselessStrategy implements Strategy
{
  private Player player;
  
  public UselessStrategy() //this exists
  {}
  
  public void setPlayer(Player p) //sets player equal to p
  {
    player = p;
  }
  
  public int[][] getSetUp() //returns a random set up
  {
    int[][] a = new int[3][9];
//    int random = (int)(100 * Math.random());
//    if (random < 25)
    {
   a = new int[][] 
   {{3, 3, 11, 0, 11, 3,2, 11, 3, 2},
      {4, 6, 5, 11, 5, 8, 7, 4, 11, 4},
      {2, 8, 7, 7, 2, 9, 1, 11, 4, 5},
      {6, 2, 10, 3, 6, 2, 5, 2, 2, 6}};
    }
//    else if (random > 25 && random < 50)
//    {
//   a = new int[][]
//   {{3, 11, 11, 0, 11, 4, 3, 3, 3, 7},
//     {2, 5, 4, 11, 5, 6, 1, 7, 2, 7},
//     {5, 11, 11, 4, 2, 9, 8, 8, 2, 4},
//     {6, 2, 2, 2, 6, 2, 3, 5, 6, 10}};
//    }
//   else if (random > 50 && random < 75)
//   {
//   a = new int[][]
//   {{0, 11, 3, 3, 2, 4, 11, 4, 11, 3},
//     {11, 5, 5, 6, 2, 11, 3, 1, 8, 4},
//     {3, 8, 7, 7, 2, 11, 5, 7, 2, 5},
//     {6, 2, 10, 2, 2, 6, 9, 4, 2, 6}};
//   }
//   else
//   {
//   a = new int[][]
//   {{2, 3, 11, 0, 11, 4, 3, 3, 2, 3},
//     {4, 11, 4, 11, 5, 7, 1, 7, 4, 6},
//     {5, 11, 5, 11, 6, 2, 8, 7, 2, 10},
//     {6, 2, 3, 9, 2, 6, 2, 5, 8, 2}};
//   }
    a = player.randomSetUp();
    return a;
    }
  
  public Move getMove()
  {
    List<Move> allMoves = player.getAllMoves(); //finds all legal moves
    ArrayList<Move> attack = new ArrayList<Move>();
    
    for (int i = 0; i < allMoves.size(); i++)
    {
      if (player.getPiece(allMoves.get(i).getTo()) != null)
      {
        if (player.getPiece(allMoves.get(i).getFrom()).canCapture(player.getPiece(allMoves.get(i).getTo())) > 0)
          attack.add(new Move(allMoves.get(i).getFrom(), allMoves.get(i).getTo()));
      }
    }
    if (attack.size() == 0)
    {
      for (int i = 0; i < allMoves.size(); i++)
      {
        if (player.getPiece(allMoves.get(i).getTo()) != null)
        {
          if (player.getPiece(allMoves.get(i).getFrom()).canCapture(player.getPiece(allMoves.get(i).getTo())) == 0)
            attack.add(new Move(allMoves.get(i).getFrom(), allMoves.get(i).getTo()));
          if (player.getPiece(allMoves.get(i).getFrom()).getRank() == 2 && player.getPiece(allMoves.get(i).getTo()).hasRevealed() == false)
          {
            attack.add(new Move(allMoves.get(i).getFrom(), allMoves.get(i).getTo()));
          }
          if (player.getPiece(allMoves.get(i).getFrom()).getRank() == 4 && player.getPiece(allMoves.get(i).getTo()).hasRevealed() == false)
          {
            attack.add(new Move(allMoves.get(i).getFrom(), allMoves.get(i).getTo()));
          }
          if (player.getPiece(allMoves.get(i).getTo()).hasRevealed() == false && player.getPiece(allMoves.get(i).getTo()).hasMoved() == true)
            attack.add(new Move(allMoves.get(i).getFrom(), allMoves.get(i).getTo()));
        }
      }
    }
//    if (attack.size() == 0)
//    {
//      for (int i = 0; i < allMoves.size(); i++)
//      {
//        if (player.getPiece(allMoves.get(i).getFrom()).canCapture(player.getPiece(allMoves.get(i).getTo())) < 0)
//        {
//        }
//      }
//    }
    if (attack.size() == 0)
    {
      for (int i = 0; i < allMoves.size(); i++)
      {
        if (player.getPiece(allMoves.get(i).getTo()) == null)
        {
          attack.add(new Move(allMoves.get(i).getFrom(), allMoves.get(i).getTo()));
        }
      }
    }
    if (attack.size() == 0)
    {
      return allMoves.get((int)(Math.random() * allMoves.size()));
    }
    return attack.get((int)(Math.random() * attack.size())); //returns the legal move at the index
  }
  
  public void moved(Move move, Piece movingPiece, Piece attackedPiece)
  {
    //ignored
  }
}

