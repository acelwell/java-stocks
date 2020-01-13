import java.io.*;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileWriting {

    List<String> attributes = new ArrayList<String>();
    List<String> safeList = new ArrayList<>();
    String path;

    public FileWriting(){}

    public FileWriting(String path){
        this.path = path;
    }

    public void setAttributes(String path) throws  Exception{
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line = "";

        while((line = br.readLine()) != null){
            this.attributes.add(line);
        }
    }

    public void setSafeList(String path) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line = "";

        while((line = br.readLine()) != null){
            this.safeList.add(line);
        }
    }

    public void setupFile(String path) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));

        bw.write("@RELATION stocks\n\n\n");

        for(String s: attributes){
            //System.out.println(s);
            bw.write("@ATTRIBUTE " + s + " NUMERIC\n");
            bw.flush();
        }

        bw.write("@ATTRIBUTE action {buy, sell, wait}");
        bw.write("\n\n@DATA\n");

        bw.close();

        //setupTestFile();
        //setupLiveFile();
    }

    private void setupTestFile() throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter("test.arff"));

        bw.write("@RELATION stocks\n\n\n");

        for(String s: attributes){
            //System.out.println(s);
            bw.write("@ATTRIBUTE " + s + " NUMERIC\n");
            bw.flush();
        }

        bw.write("@ATTRIBUTE action {buy, sell, wait}");
        bw.write("\n\n@DATA\n");

        bw.close();
    }

    public void setupLiveFile() throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter("live.arff"));

        System.out.println("setting up live file");

        bw.write("@RELATION stocks\n\n\n");

        for(String s: attributes){
            //System.out.println(s);
            bw.write("@ATTRIBUTE " + s + " NUMERIC\n");
            bw.flush();
        }

        bw.write("@ATTRIBUTE action {buy, sell, wait}");
        bw.write("\n\n@DATA\n");

        bw.close();
    }



    public void addToFile(String line) throws Exception{
        BufferedWriter bw = new BufferedWriter(new FileWriter(path, true));
        BufferedWriter bwt = new BufferedWriter(new FileWriter("test.arff", true));

        int commaCount = 0;

        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == ','){
                commaCount++;
            }
        }
        if(commaCount == 14) {
            bw.write(line + "\n");
            Random rand = new Random();
            if(rand.nextInt() % 10 <= 1){
                bwt.write(line + "\n");
            }
        }
        //bw.write(line + "\n");

        bw.flush();
        bwt.flush();
    }

    public void addToSafeFile(String line) throws Exception
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("SafeData.arff", true));

        int commaCount = 0;

        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == ','){
                commaCount++;
            }
        }
        if(commaCount == 14) {
            bw.write(line + "\n");

        }
        //bw.write(line + "\n");

        bw.flush();
    }

    public void addToHopeFile(String line) throws Exception
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("HopeData.arff", true));

        int commaCount = 0;

        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == ','){
                commaCount++;
            }
        }
        if(commaCount == 14) {
            bw.write(line + "\n");

        }
        //bw.write(line + "\n");

        bw.flush();
    }

    public void addToLiveFile(String line) throws Exception{

        BufferedWriter bw = new BufferedWriter(new FileWriter("live.arff", true));

        int commaCount = 0;

        for(int i = 0; i < line.length(); i++){
            if(line.charAt(i) == ','){
                commaCount++;
            }
        }
        if(commaCount == 13) {
            bw.write(line + "\n");
            //System.out.println(line);

        }
        //bw.write(line + "\n");

        bw.flush();
    }

    public void addToBuySell(String line) throws Exception
    {
        BufferedWriter bw = new BufferedWriter(new FileWriter("happening.txt", true));

        //System.out.println("adding to buysell file");
        bw.write(line + "\n");
        bw.flush();
    }

}
