public class HumanStrategy implements Strategy
{
  private Player player;
  private Display display;
  
  public HumanStrategy(Display d) //makes a HumanStrategy w/ a display
  {
    display = d;
  }
  
  public int[][] getSetUp() //prompts the display for a setup
  {
    return display.getSetUp();
  }
  
  public void setPlayer(Player p) //gives us a player boi
  {
    player = p;
  }
  
  public Move getMove() //returns the player's move
  {
    Move m = display.getMove(); //prompts the display for a move
    //System.out.println("move from display:  " + m);
    if(player.isValidMove(m))
    {
      return m; //if the move is valid, returns it
    }
    else
    {
      return this.getMove(); //otherwise, ask for another one
    }
  }
  
  public void moved(Move move, Piece movingPiece, Piece attackedPiece)
  {
    //ignored for now
  }
}
