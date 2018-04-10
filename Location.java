public class Location implements Comparable
{
  private int row;
  private int column;
  
  public Location(int row, int column)
  {
    this.row = row;
    this.column = column;
  }
  
  public int getRow()
  {
    return row;
  }
  
  public int getCol()
  {
    return column;
  }
  
  public boolean isValid()
  {
    boolean ret = (row >= 0) && (column >= 0) && (row <= 9) && (column <= 9);
    if(ret)
    {
      ret = !((row>=4 && row<= 5) && ((column>=2 && column<=3) || (column>=6 && column<=7)));
    }
    return ret;
  }
  
  public Location rotated()  // returns location for opposite side of board
  {
    return new Location(9-row, 9-column);
  }
  
  public String toString()
  {
    return "(" + row + ", " + column + ")";
  }
  
  public boolean equals(Object obj)
  {
    return (((Location)obj).getRow() == row) && (((Location)obj).getCol() == column);
  }
  
  public int compareTo(Object obj)
  {
    int ret = row-(((Location)obj).getRow());
    if(ret == 0)
      ret = column-(((Location)obj).getCol());
    return ret;
  }
  
  public int hashCode()
  {
    return 10 * row + column;
  }
  
  public int distanceTo(Location other)  //returns taxicab distance
  {
    return (Math.abs(other.getRow()-row)+Math.abs(other.getCol()-column));
  }
}

