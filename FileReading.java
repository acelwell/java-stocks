import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileReading {

    public FileReading(){

    }

    public List<String> getStockList(String file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));

        List<String> list = new ArrayList<>();
        String line;

        while((line = br.readLine()) != null){
            list.add(line);
        }

        return list;
    }
}
