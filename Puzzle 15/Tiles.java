public abstract class Tiles implements Cloneable
{
    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public static final int SIZE = 4;
    public static final byte EMPTY = 0;
    protected Configuration configuration;
    protected int moves;
    
    public abstract byte getTile(int row, int col);
    public abstract void setTile(int row, int col, byte value);
    protected abstract boolean isSolved();
    protected abstract void moveImpl(Direction direction);

    public Tiles(String format)
    {
        moves = 0;
        configuration = new Configuration(format);
    }

    public Tiles(Tiles t)
    {
        moves = t.moves;
        configuration = t.configuration;
    }

    public Tiles clone() 
    {
        try {
            return (Tiles)super.clone();
        } catch (CloneNotSupportedException e) {
            e.getMessage();
            return null;
        }
    }

    public void move(Direction direction)
    {
        moveImpl(direction);
        moves++;
    }

    protected int getMoveCount()
    {
        return moves;
    }

    protected void incrementMoveCount()
    {
        moves++;
    }

    Configuration getConfiguration()
    {
        return configuration;
    }

    public void ensureValidity() throws InvalidConfiguationException
    {
        boolean[] checkRepetition = new boolean[SIZE * SIZE];
        int empty = 0;
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                if(checkRepetition[getTile(i, j)])
                    throw new InvalidConfiguationException("Invalid configuration: multiple tiles with the value " +
                                                            getTile(i, j) + ".");
                else
                    checkRepetition[getTile(i, j)] = true;
                if(getTile(i, j) == EMPTY)
                    empty++;
                if(empty > 1)
                    throw new InvalidConfiguationException("Invalid configuration: multiple empty spaces.");
                if(getTile(i, j) > SIZE * SIZE - 1 || getTile(i, j) < 0)
                    throw new InvalidConfiguationException("Invalid configuration: incorrect tile value " +
                                                            getTile(i, j) + ".");
            }
        }
    }

    public boolean isSolvable()
    {
        int count = 0;
        int row = 0;
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                if(getTile(i, j) == EMPTY)
                {
                    row = i;
                    break;
                }
                byte value = getTile(i, j);
                for(int k = i; k < SIZE; k++)
                {
                    for(int m = j + 1; m < SIZE; m++)
                    {
                        if(value > getTile(k, m) && getTile(k, m) != EMPTY)
                            count++;
                    }
                }
            }
        }
        if((count % 2 == 0 && (SIZE - row) % 2 != 0) || (count % 2 != 0 && (SIZE - row) % 2 == 0))
            return true;
        else
            return false;
    }
}
