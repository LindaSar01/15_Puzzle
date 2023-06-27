public class MatrixTile extends Tiles implements Cloneable
{
    private byte[][] tiles;
    private int emptyCol;
    private int emptyRow;
    //private Configuration configuration;

    public MatrixTile(String format) throws ConfigurationFormatException, InvalidConfiguationException
    {
        super(format);
        tiles = new byte[SIZE][SIZE];
        getConfiguration().initialise(this);
        ensureValidity();
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                if(tiles[i][j] == EMPTY)
                {
                    emptyCol = j;
                    emptyRow = i;
                }
            }
        }
    }

    public MatrixTile(MatrixTile mt)
    {
        super(mt);
        tiles = new byte[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                tiles[i][j] = mt.tiles[i][j];
            }
        }
        emptyRow = mt.emptyRow;
        emptyCol = mt.emptyCol;
    }

    public MatrixTile clone()
    {
        MatrixTile mt = (MatrixTile) super.clone();
        mt.tiles = new byte[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
            {
                mt.tiles[i][j] = tiles[i][j];
            }
        }
        return mt;
    }

    public byte getTile(int row, int col)
    {
        if ((row < 0 || row >= SIZE) || (col < 0 || col >= SIZE))
        {
            System.out.println("Error: position out of the board");
            System.exit(0);
        }
        return tiles[row][col];
    }

    public void setTile(int row, int col, byte value)
    {
        tiles[row][col] = value;
    }

    public void moveImpl(Direction direction)
    {
        int row = 0, col = 0;
        if(direction == Direction.UP)
        {
            row = emptyRow;
            col = emptyCol;
            emptyRow += 1;
        }else if (direction == Direction.DOWN)
        {
            row = emptyRow;
            col = emptyCol;
            emptyRow -= 1;
        }else if (direction == Direction.LEFT)
        {
            row = emptyRow;
            col = emptyCol;
            emptyCol += 1;
        }else if (direction == Direction.RIGHT)
        {
            row = emptyRow;
            col = emptyCol;
            emptyCol -= 1;
        }
        
        setTile(row, col, getTile(emptyRow, emptyCol));
        setTile(emptyRow, emptyCol, Tiles.EMPTY);
    }

    protected boolean isSolved()
    {
        if (emptyRow + 1 != SIZE && emptyCol + 1 != SIZE)
        {
            return false;
        }
        for(int r = 0; r < SIZE; r++)
        {
            for(int c = 0; c < SIZE; c++)
            {
                if ((r + 1 < SIZE || c + 1 < SIZE) && 
                    tiles[r][c] != r * SIZE + c + 1)
                    {
                        return false;
                    }
            }
        }
        return true;
    }
}
