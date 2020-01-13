import java.io.*;
import java.util.*;

public class FetchData {
    String preURL = "https://cloud.iexapis.com/stable/stock/";
    String postURL = "...";

    List<String> symList = new ArrayList<String>();


    public FetchData(String pathToSym) throws IOException {

        FileReader fr = new FileReader(pathToSym);
        BufferedReader br = new BufferedReader(fr);

        String line;
        while(( line = br.readLine()) != null){
            symList.add(line);
        }
    }

    public List<String>getSymList(){
        return symList;
    }

}
