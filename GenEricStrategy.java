import java.util.*;

public class GenEricStrategy implements Strategy
{
    private int[][] wholeBoard;
    private Player player;
    private boolean color;
    public GenEricStrategy() //this exists
    {

    }

    public void setPlayer(Player p)
    {
        player=p;
        color=player.getColor();
    }

    public int[][] getSetUp()
    {
        return player.randomSetUp();
//        return new int[][] {
//            {3,3,11,0,11,3,2,11,3,2},
//            {4,6,5,11,5,8,1,4,11,4},
//            {2,8,7,7,2,9,7,11,4,6},
//            {6,2,3,10,6,2,5,2,2,5}
//        };
    }

    public Move getMove()
    {

        List<Move> allMoves = player.getAllMoves(); //finds all legal move
        Move bestMove = null;
        int bestVal = 0;

        for (Move m: allMoves)
        {
            Location from=m.getFrom();
            Location to=m.getTo();
            Piece myPiece=player.getPiece(from);
            Piece enemyPiece= player.getPiece(to);
            int myPieceRank=myPiece.getRank();
            int val = 0;

            if (enemyPiece!=null)
            {
                if (myPiece.getRank()==1)
                {
                    if (enemyPiece.getRank()==10)
                    {
                        //kill marshal
                        val=100;
                    }
                    else
                    {
                        Move runMove=runAway(from,to);
                        if (runMove!=null)
                        {

                            m=runMove;
                            val=95;
                        }
                        else
                        {
                            val=-1;
                        }
                    }
                }
                if (player.getPiece(to).getRank()!=-1&&player.getPiece(from).getRank()>player.getPiece(to).getRank())
                {
                    val=50;
                    //kill smaller piece
                }
                else if(enemyPiece.getRank()!=-1&&myPieceRank<enemyPiece.getRank())
                {

                    if(enemyPiece.getRank()==10)
                    {
                        Location spyLoc=spyLocation();
                        if (spyLoc!=null)
                        {
                            Move spyMove=runTowardsSpy(from,spyLoc);
                            if(spyMove!=null)
                            {
                                m=spyMove;
                                val=90;
                            }
                        }
                        // else
                        // {
                        // Move runMove=runAway(from,to);
                        // if (runMove!=null)
                        // {

                        // m=runMove;
                        // val=80;
                        // }
                        // }
                    }
                    // else
                    // {
                    // Move runMove=runAway(from,to);
                    // if (runMove!=null)
                    // {

                    // m=runMove;
                    // val=80;
                    // }

                    // }    
                }
                else if (myPieceRank==2||myPieceRank==4||myPieceRank==5&&enemyPiece.getRank()==-1)
                {
                    val=20;
                    //scout
                }
                else if (myPieceRank==6||myPieceRank==7&&enemyPiece.getRank()==-1)
                {
                    val=2;
                    //scout
                }
                else if (myPiece.getRank()==10&&enemyPiece.hasMoved()==true)
                {
                    val=40;
                    //marsahl kills moved piece
                }
            }
            else
            {
                if (myPieceRank==10)
                {

                    Location huntLoc=biggerThanEnemy(10);
                    Location nearestMoved=nearestUnkownMovedPiece(from);
                    if (huntLoc!=null&&huntDownSafe(from,huntLoc,10)!=null)
                    {
                        val=10;
                        m= huntDown(from,huntLoc);
                    }

                    else if(nearestMoved!=null)
                    {

                        Move b=huntDown(from,nearestMoved);
                        if (b!=null)
                        {
                            val=10;
                            m=b;
                        }
                    }
                }
                else if (myPieceRank==3)
                {
                    Location nearestBomb=nearestBomb(from);
                    
                    {

                        if(nearestBomb!=null) 
                        {
                            Move a=safetyHuntDown(from,nearestBomb);
                            if (a!=null)
                            {
//System.out.print(""+nearestBomb);
                                val=3;
                                m=a;
                            }
                        }
                    }
                }            
                else if(myPieceRank>=4&&myPieceRank<=9)
                {
                    Location huntLoc=biggerThanEnemy(myPieceRank);
                    if (huntLoc!=null&&huntDownSafe(from,huntLoc,myPieceRank)!=null)
                    {
                        val=11;
                        m= huntDown(from,huntLoc);
                    }
                    else if(myPieceRank==9)
                    {
                        Location nearestMoved=nearestUnkownMovedPiece(from);
                        if(nearestMoved!=null)
                        {
                            Move b=huntDown(from,nearestMoved);
                            if (b!=null)
                            {
                                val=9;
                                m=b;
                            }
                        }
                    }
                    else if(myPieceRank<9&&myPieceRank>5)
                    {
                        if(myPieceRank==6||myPieceRank==7)
                            {

                                Location nearest=nearestUnknownEnemy(from);
                                if(nearest!=null)
                                {
                                    if (huntDown(from,nearest)!=null);
                                    {
                                        Move a=huntDownSafe(from,nearest, myPieceRank);
                                        if(a!=null)
                                        {
                                            
                                            val=1;
                                            m=a;
                                        }

                                    }
                                    //fix some stuff
                                }
                            }
                        boolean temp=doIHaveBiggerPiece(myPieceRank);

                        if(temp==false)
                        {
                            
                            Location nearestMoved=nearestUnkownMovedPiece(from);
                            if(nearestMoved!=null)
                            {

                                Move b=huntDown(from,nearestMoved);
                                if (b!=null)
                                {

                                    val=7;
                                    m=b;
                                }

                            }
                        }
                    }
                    else if(myPieceRank==4||myPieceRank==5)
                    {

                        Location nearest=nearestUnknownEnemy(from);
                        if(nearest!=null)
                        {
                            if (huntDown(from,nearest)!=null);
                            {
                                Move a=huntDownSafe(from,nearest, myPieceRank);
                                if(a!=null)
                                {

                                    val=4;
                                    m=a;
                                }

                            }
                            //fix some stuff
                        }
                    }

                }
            }
            if(val > bestVal) {
                bestVal = val;
                bestMove = m;
            }
        }
        if (bestVal<=0)
        {
            Location maxPiece=maxPiece();
//System.out.println("random");
            int randomMove = (int)(Math.random() * allMoves.size());
      
            return allMoves.get(randomMove);
        }
        return bestMove;
        //

        //finds a random valid index
        //return allMoves.get(move); //returns the legal move at the index
    }

    // public Location smallerPieceAround(Location from)
    // {
    // else
    // {
    // return null;
    // }
    // } 
    public Location maxPiece()
    {
        int max=-1;
        Location l=null;
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);
                if (player.getPiece(a)!=null&&player.getPiece(a).getColor()==color)
                {
                    if(player.getPiece(a).getRank()!=11&&player.getPiece(a).getRank()>max)
                    {
                        l=a;
                        max=player.getPiece(a).getRank();
                    }
                }
            }
        }
        return l;
    }

    public Move runTowardsSpy(Location from, Location to)
    {
        Queue<Location> toVisit=new LinkedList<Location>();
        boolean[][]visited=new boolean[10][10];
        toVisit.add(from);
        visited[from.getRow()][from.getCol()]=true;
        Location[][] parent=new Location[10][10];
        while(!toVisit.isEmpty())
        {
            Location x=toVisit.remove();
            if (x.equals(to))
            {
                Location a=parent[to.getRow()][to.getCol()];
                if (a==null)
                {
                    return null;
                }
                if(a.equals(from))
                {
                    return new Move(from,x);
                }
                while(!parent[a.getRow()][a.getCol()].equals(from))
                {
                    a=parent[a.getRow()][a.getCol()];
                }
                return new Move(from,a);
            }
            Location[]children=new Location[4];
            children[0]=new Location (x.getRow()-1,x.getCol());
            children[1]=new Location (x.getRow()+1,x.getCol());
            children[2]=new Location (x.getRow(),x.getCol()-1);
            children[3]=new Location (x.getRow(),x.getCol()+1);
            boolean allChildrenBad=true;
            for(Location child: children)
            {

                if (child.isValid()==true&&visited[child.getRow()][child.getCol()]==false)
                {
                    if(player.getPiece(child)==null)
                    {
                        allChildrenBad=false;
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                    else if (child.equals(to))
                    {
                        allChildrenBad=false;
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;

                        if (parent[x.getRow()][x.getCol()]==null)
                        {
                            return null;
                        }

                        while(!parent[x.getRow()][x.getCol()].equals(from))
                        {
                            x=parent[x.getRow()][x.getCol()];
                        }
                        return new Move(from,x);
                    }
                }
            }
            if (allChildrenBad==true&&distance(x,to)<4&&x.equals(from)==false)
            {

                toVisit.add(x);
                visited[x.getRow()][x.getCol()]=true;

                while(!parent[x.getRow()][x.getCol()].equals(from))
                {
                    x=parent[x.getRow()][x.getCol()];
                }
                return new Move(from,x);

            }
        }

        return null;
    }

    public Move safetyHuntDown(Location from, Location to)
    {
        //System.out.print("reached");
        Queue<Location> toVisit=new LinkedList<Location>();
        boolean[][]visited=new boolean[10][10];
        toVisit.add(from);
        visited[from.getRow()][from.getCol()]=true;
        Location[][] parent=new Location[10][10];
        while(!toVisit.isEmpty())
        {
            Location x=toVisit.remove();
            if (x.equals(to))
            {
                //System.out.print("called");
                Location a=parent[to.getRow()][to.getCol()];
                if(a.equals(from))
                {
                    return new Move(from,x);
                }
                while(!parent[a.getRow()][a.getCol()].equals(from))
                {
                    a=parent[a.getRow()][a.getCol()];
                }
                return new Move(from,a);
            }
            Location[]children=new Location[4];
            children[0]=new Location (x.getRow()-1,x.getCol());
            children[1]=new Location (x.getRow()+1,x.getCol());
            children[2]=new Location (x.getRow(),x.getCol()-1);
            children[3]=new Location (x.getRow(),x.getCol()+1);
            for(Location child: children)
            {
                if (child.isValid()==true&&visited[child.getRow()][child.getCol()]==false)
                {
                    if (child.equals(to))
                    {
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                    else if(player.getPiece(child)==null&&isPieceAround(child)==false&&isLocationAround(child,to)==false)
                    {
                        
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                    else if(player.getPiece(child)==null&&isLocationAround(child,to)==true)
                    {
                        //System.out.print("works");
                        if (isPieceAroundException(child,to)==false){
                            toVisit.add(child);
                            visited[child.getRow()][child.getCol()]=true;
                            parent[child.getRow()][child.getCol()]=x;
                        }
                    }
                }
            }
        }

        return null;

    }

    public Move huntDown(Location from, Location to)
    {
        Queue<Location> toVisit=new LinkedList<Location>();
        boolean[][]visited=new boolean[10][10];
        toVisit.add(from);
        visited[from.getRow()][from.getCol()]=true;
        Location[][] parent=new Location[10][10];
        while(!toVisit.isEmpty())
        {
            Location x=toVisit.remove();
            if (x.equals(to))
            {
                Location a=parent[to.getRow()][to.getCol()];
                if(a.equals(from))
                {
                    return new Move(from,x);
                }
                while(!parent[a.getRow()][a.getCol()].equals(from))
                {
                    a=parent[a.getRow()][a.getCol()];
                }
                return new Move(from,a);
            }
            Location[]children=new Location[4];
            children[0]=new Location (x.getRow()-1,x.getCol());
            children[1]=new Location (x.getRow()+1,x.getCol());
            children[2]=new Location (x.getRow(),x.getCol()-1);
            children[3]=new Location (x.getRow(),x.getCol()+1);
            for(Location child: children)
            {
                if (child.isValid()==true&&visited[child.getRow()][child.getCol()]==false)
                {
                    if(player.getPiece(child)==null)
                    {
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                    else if (child.equals(to))
                    {
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                }
            }
        }

        return null;

    }

    public Move huntDownSafe(Location from, Location to, int rank)
    {
        Queue<Location> toVisit=new LinkedList<Location>();
        boolean[][]visited=new boolean[10][10];
        toVisit.add(from);
        visited[from.getRow()][from.getCol()]=true;
        Location[][] parent=new Location[10][10];
        while(!toVisit.isEmpty())
        {
            Location x=toVisit.remove();
            if (x.equals(to))
            {
                Location a=parent[to.getRow()][to.getCol()];
                if(a.equals(from))
                {
                    return new Move(from,x);
                }
                while(!parent[a.getRow()][a.getCol()].equals(from))
                {
                    a=parent[a.getRow()][a.getCol()];
                }
                return new Move(from,a);
            }
            Location[]children=new Location[4];
            children[0]=new Location (x.getRow()-1,x.getCol());
            children[1]=new Location (x.getRow()+1,x.getCol());
            children[2]=new Location (x.getRow(),x.getCol()-1);
            children[3]=new Location (x.getRow(),x.getCol()+1);
            for(Location child: children)
            {
                if (child.isValid()==true&&visited[child.getRow()][child.getCol()]==false)
                {
                    boolean temp=isBiggerPieceAround(child,rank);
                    if(player.getPiece(child)==null&&temp==false)
                    {
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                    else if (child.equals(to))
                    {
                        toVisit.add(child);
                        visited[child.getRow()][child.getCol()]=true;
                        parent[child.getRow()][child.getCol()]=x;
                    }
                }
            }
        }

        return null;

    }

    public Move runAway(Location x, Location enemy)
    {
        int row=x.getRow();
        int col=x.getCol();
        Location[]locs=new Location[4]; 
        locs[0]= new Location (row-1,col);
        locs[1]= new Location (row,col-1);
        locs[2]= new Location (row,col+1);
        locs[3] = new Location (row+1,col);
        Move temp=null;
        for(int i=0;i<4;i++)
        {

            if (locs[i].isValid() &&player.getPiece(locs[i])==null&& !locs[i].equals(enemy))
            {
                if (temp==null)
                {
                    temp=new Move(x,locs[i]);
                }
                else
                {
                    if (Math.random()>.5)
                    {
                        temp=new Move(x,locs[i]);
                    }
                }
            }

        }

        return temp;
    }

    public Location biggerThanEnemy(int x)
    {
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);

                if (player.getPiece(a)!=null&&player.getPiece(a).getColor()!=color)
                {
                    int y=player.getPiece(a).getRank();
                    if (x>y&&y!=-1)
                    {
                        if (x==1||y==1)
                        {
                        }
                        else
                        {
                            return a;
                        }
                    }
                }

            }
        }
        return null;
    }

    public Location spyLocation()
    {
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);
                if(player.getPiece(a)!=null&&player.getPiece(a).getRank()==1&&player.getPiece(a).getColor()==color)
                {
                    return a;
                }
            }
        }
        return null;
    }

    public boolean doIHaveBiggerPiece(int rank)
    {
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);
                if(player.getPiece(a)!=null&&player.getPiece(a).getColor()==color&&player.getPiece(a).getRank()<11&&player.getPiece(a).getRank()>rank)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public Location nearestUnkownMovedPiece(Location l)
    {
        Location closest=null;
        int minD=1000;
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);
                if (player.getPiece(a)!=null&&player.getPiece(a).getColor()!=color)
                {
                    if(distance(l,a)<minD&&player.getPiece(a).getRank()==-1&&player.getPiece(a).hasMoved()==true)
                    {
                        closest=a;
                        minD=distance(l,a);
                    }
                }

            }
        }
        return closest;
    }

    public Location nearestBomb(Location l)
    {
        Location closest=null;
        int minD=1000;
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);
                if (player.getPiece(a)!=null&&player.getPiece(a).getColor()!=color)
                {
                    if(distance(l,a)<minD&&player.getPiece(a).getRank()==11)
                    {
                        closest=a;
                        minD=distance(l,a);
                    }
                }

            }
        }
        return closest;
    }

    public Location nearestUnknownEnemy(Location l)
    {
        Location closest=null;
        int minD=1000;
        for (int i=0; i<10;i++)
        {
            for (int j=0; j<10; j++)
            {
                Location a=new Location(i,j);
                if (player.getPiece(a)!=null&&player.getPiece(a).getColor()!=color)
                {
                    if(distance(l,a)<minD&&player.getPiece(a).getRank()==-1)
                    {
                        closest=a;
                        minD=distance(l,a);
                    }
                }

            }
        }
        return closest;
    }

    public int distance(Location a, Location b)
    {
        int x=Math.abs(a.getRow()-b.getRow())+Math.abs(a.getCol()-b.getCol());
        return x;
    }

    public boolean isPieceAround(Location l)
    {
        int row=l.getRow();
        int col=l.getCol();
        Location[]locs=new Location[4]; 
        locs[0]= new Location (row-1,col);
        locs[1]= new Location (row,col-1);
        locs[2]= new Location (row,col+1);
        locs[3] = new Location (row+1,col);
        for(int i=0;i<4;i++)
        {
            if (locs[i].isValid() && player.getPiece(locs[i])!=null)
            {
                return false;
            }
        }
        return true;
    }

    public boolean isBiggerPieceAround(Location l, int rank)
    {
        int row=l.getRow();
        int col=l.getCol();
        Location[]locs=new Location[4]; 
        locs[0]= new Location (row-1,col);
        locs[1]= new Location (row,col-1);
        locs[2]= new Location (row,col+1);
        locs[3] = new Location (row+1,col);
        for(int i=0;i<4;i++)
        {
            if (locs[i].isValid() && player.getPiece(locs[i])!=null&&player.getPiece(locs[i]).getRank()>rank)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isPieceAroundException(Location l, Location to)
    {
        int row=l.getRow();
        int col=l.getCol();
        Location[]locs=new Location[4]; 
        locs[0]= new Location (row-1,col);
        locs[1]= new Location (row,col-1);
        locs[2]= new Location (row,col+1);
        locs[3] = new Location (row+1,col);
        for(int i=0;i<4;i++)
        {
            if (locs[i].isValid() && player.getPiece(locs[i])!=null)
            {
                if (locs[i].equals(to)==false)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLocationAround(Location l, Location to)
    {
        int row=l.getRow();
        int col=l.getCol();
        Location[]locs=new Location[4]; 
        locs[0]= new Location (row-1,col);
        locs[1]= new Location (row,col-1);
        locs[2]= new Location (row,col+1);
        locs[3] = new Location (row+1,col);
        for(int i=0;i<4;i++)
        {
            if (locs[i].isValid() && locs[i].equals(to))
            {
                return true;
            }
        }
        return false;
    }

    public void moved(Move move, Piece movingPiece, Piece attackedPiece)
    {
        //ignored
        //System.out.println("moved:  " + move + " " + movingPiece + " " + attackedPiece);
    }
}

