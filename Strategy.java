public interface Strategy
{
  
  void setPlayer(Player player);
  
  int[][] getSetUp();
  
  Move getMove();
  
  void moved(Move move, Piece movingPiece, Piece attackedPiece);
}
