import java.io.*;
import java.net.*;
import java.util.*;

public class ConfigurationStore 
{
    ArrayList<Configuration> configs = new ArrayList<>();

    public ArrayList<Configuration> getConfigurationsSorted() 
    {
        ArrayList<Configuration> copy = new ArrayList<Configuration>(configs);
        Collections.sort(copy);
        return copy;
    }


    public ConfigurationStore(String source) throws IOException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            loadFromURL(source);
        }
        else {
            loadFromDisk(source);
        }
    }
    
    public ConfigurationStore(Reader source) throws IOException 
    {
        load(source);
    }
    private void load(Reader r) throws IOException
    {
        BufferedReader b = new BufferedReader(r);
        String line;
        while ( (line = b.readLine()) != null) {
            //System.out.println(line);
            configs.add(new Configuration(line));
        }
    }
    private void loadFromURL(String url) throws IOException {
        URL destination = new URL(url);
		URLConnection conn = destination.openConnection();
		Reader r = new InputStreamReader(conn.getInputStream());
		load(r);
    }
    private void loadFromDisk(String filename) throws IOException {
        Reader r = new FileReader(filename);
        load(r);
    }
}

