public class Move
{
  private Location from;
  private Location to;
  
  public Move(Location from, Location to)
  {
    this.from = from;
    this.to = to;
  }
  public Location getFrom()
  {
    return from;
  }
  public Location getTo()
  {
    return to;
  }
  public String toString()
  {
    return from.toString() + " ==> " + to.toString();
    
  }
  public boolean equals(Object obj)
  {
    if (((Move)obj).getFrom().equals(from)&&((Move)obj).getTo().equals(to))
    {
      return true;
    }
    return false;
  }
  
  public int compareTo(Object obj)
  {
    int ret = from.compareTo(((Move)obj).getFrom());
    if(ret == 0)
      ret = to.compareTo(((Move)obj).getTo());
    return ret;
  }
  public int hashCode()
  {
    return from.hashCode() * 100 + to.hashCode();
  }
  
  public Move rotated()
  {
    return new Move(from.rotated(), to.rotated());
  }
}

