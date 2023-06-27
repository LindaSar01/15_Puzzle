public class ArrayTiles extends Tiles implements Cloneable
{
    private byte[] tiles;
    private int emptyPos;

    public ArrayTiles(String format) throws ConfigurationFormatException, InvalidConfiguationException
    {
        super(format);
        tiles = new byte[SIZE * SIZE];
        getConfiguration().initialise(this);
        ensureValidity();
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            if(tiles[i] == EMPTY)
                emptyPos = i;
        }
    }

    public ArrayTiles(ArrayTiles at) throws ConfigurationFormatException, InvalidConfiguationException
    {
        super(at);
        tiles = new byte[SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            tiles[i] = at.getTile(i);
        }
        emptyPos = at.emptyPos;
    }

    public ArrayTiles clone()
    {
        ArrayTiles at = (ArrayTiles)super.clone();
        at.tiles = new byte[SIZE * SIZE];
        for (int i = 0; i < SIZE * SIZE; i++)
        {
            at.tiles[i] = tiles[i];
        }
        return at;
    }

    public byte getTile(int row, int col)
	{
		return getTile(row * SIZE + col);
	}
	private byte getTile(int pos)
	{
		if (pos < 0 || pos >= SIZE * SIZE)
		{
			System.out.println("Error: position out of the board");
			System.exit(0);			
		}
		return tiles[pos];
	}
	public void setTile(int row, int col, byte value)
	{
		setTile(row * SIZE + col, value);
	}
	public void setTile(int pos, byte value)
	{
		tiles[pos] = value;
	}

    public void moveImpl(Direction direction)
    {
        int pos = 0;
        if(direction == Direction.UP)
        {
            pos = emptyPos;
            emptyPos += 4;
        }else if (direction == Direction.DOWN)
        {
            pos = emptyPos;
            emptyPos -= 4;
        }else if (direction == Direction.LEFT)
        {
            pos = emptyPos;
            emptyPos += 1;
        }else if (direction == Direction.RIGHT)
        {
            pos = emptyPos;
            emptyPos -= 1;
        }
        setTile(pos, getTile(emptyPos));
        setTile(emptyPos, Tiles.EMPTY);
    }

    protected boolean isSolved()
    {
        if(emptyPos + 1 != SIZE * SIZE)
        {
            return false;
        }
        for(int i = 0; i < SIZE * SIZE; i++)
        {
            if(tiles[i] != i + 1)
            {
                return false;
            }
        }
        return true;
    }
}

