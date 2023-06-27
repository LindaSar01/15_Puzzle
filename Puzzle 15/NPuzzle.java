import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NPuzzle 
{
    private Tiles tiles;
    private ConfigurationStore store;
    private ArrayList<Tiles> cachedTiles;
    public NPuzzle(ConfigurationStore cs) 
    {
        cachedTiles = new ArrayList<>();
        store = cs;
    }

    private Tiles copyTiles(boolean useCloning) throws ConfigurationFormatException, InvalidConfiguationException {
        if(useCloning)
        {
            return tiles.clone();
        }
        else
        {
            if(tiles instanceof ArrayTiles)
                return new ArrayTiles((ArrayTiles) tiles);
            else
                return new MatrixTile((MatrixTile) tiles);
        }
    }
    public void play() throws IOException, ConfigurationFormatException, InvalidConfiguationException
    {
        boolean started = false;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response = "";
        System.out.println("Please select a configuration to play (l to list):");
        while(!response.equals("q"))
        {
            response = br.readLine();
            if(response.equals("UP") || response.equals("DOWN") ||
                response.equals("LEFT") || response.equals("RIGHT"))
            {
                if(tiles == null)
                {
                    System.out.println("Please select a configuration to play (l to list):");
                }else 
                {
                    print();
                    if(!tiles.isSolved())
                    {
                        try {
                            tiles.move(Tiles.Direction.valueOf(response));
                        }catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                            System.exit(0);
                        }
                        print();
                        cachedTiles.add(tiles.getMoveCount(), copyTiles(true));
                        System.out.println("Please make a move by inputting UP, DOWN, LEFT, RIGHT;");
                        System.out.println("or stop the game by inputting q:");
                    }else
                    {
                        System.out.println("You Solved the Puzzle!");
                        tiles = null;
                        cachedTiles.clear();
                        System.out.println("Please select a configuration to play (l to list):");
                    }
                }
            }
            else if(response.equals("b"))
            {
                if(tiles == null)
                {
                    System.out.println("Please select a configuration to play (l to list):");
                }else
                {
                    int moveCount = tiles.getMoveCount();
                    if(moveCount != 0)
                    {
                        tiles = cachedTiles.get(moveCount - 1);
                    }
                    print();
                }
            }
            else if(response.equals("f"))
            {
                if(tiles == null)
                {
                    System.out.println("Please select a configuration to play (l to list):");
                }else
                {
                    int moveCount = tiles.getMoveCount();
                    if(moveCount + 1 < cachedTiles.size())
                        tiles = cachedTiles.get(moveCount + 1);
                    print();
                }
            }
            else if(response.equals("l"))
            {
                int i = 1;
                ArrayList<Configuration> list = store.getConfigurationsSorted();
                for (Configuration config : list) 
                {
                    System.out.println(i++ + " (" + config.getData() + ")");
                }
            }else if(response.startsWith("c"))
            {
                if(response.length() <= 1)
                {
                    if(started == true)
                        System.out.println("Invalid command, try again!");
                    else
                        System.out.println("Please select a configuration to play (l to list):");
                }else
                {
                    response = response.replaceAll(" ", "").replaceAll("c", "");
                    int c = Integer.valueOf(response);
                    ArrayList<Configuration> list = store.getConfigurationsSorted();
                    tiles = new ArrayTiles(list.get(c - 1).getData());
                    started = true;
                    if(!tiles.isSolvable())
                    {
                        System.out.println("The game is not solvable. Quitting.");
                        System.exit(0);
                    }
                    cachedTiles.add(tiles.getMoveCount(), copyTiles(true));
                    print();
                    System.out.println("Please make a move by inputting UP, DOWN, LEFT, RIGHT;");
                    System.out.println("or stop the game by inputting q:");
                }
            }else
            {
                if(response.equals("q"))
                    break;
                if(started == true)
                    System.out.println("Invalid command, try again!");
                else
                    System.out.println("Please select a configuration to play (l to list):");
            }
        }
        System.out.println("Ending the game.");
        tiles = null;
        cachedTiles.clear();
		return;
    }
    public void print() 
    {
        System.out.println("- " + tiles.getMoveCount() + " moves");
        for(int r = 0; r < Tiles.SIZE; r++)
        {
            System.out.print("-");
            for(int c = 0; c < Tiles.SIZE; c++)
            {
                System.out.print("-----");
            }
            System.out.println();
            System.out.print("|");
            for(int c = 0; c < Tiles.SIZE; c++)
            {
                if(tiles.getTile(r, c) == Tiles.EMPTY)
                    System.out.printf("%1$3s", " ");
                else
                    System.out.printf("%1$3s", tiles.getTile(r, c), "%1$3s");
                System.out.print(" |");
            }
            System.out.println();
        }
        System.out.print("-");
        for(int c = 0; c < Tiles.SIZE; c++)
        {
            System.out.print("-----");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        try {
            NPuzzle np = new NPuzzle(new ConfigurationStore("https://bit.ly/2VnEGqS"));
            np.play();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (ConfigurationFormatException e) {
            System.out.println(e.getMessage());
        }
        catch (InvalidConfiguationException e) {
            System.out.println(e.getMessage());
        }
    }
}
