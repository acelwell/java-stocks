//import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
//import weka.core.*;
//import weka.core.converters.ConverterUtils;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.util.*;
import java.io.*;
import java.net.*;

public class ParseData {

    List<String> syms;

    String preURL = "https://cloud.iexapis.com/stable/stock/";
    //String preURL = "https://sandbox.iexapis.com/stable/stock/";
    // krytality token
    //String postURL = "...";
    // kryrality token
    //String postURL = "...";
    // gmail token

    //String postURL = "...";

    //fenix token
    String curToken = "...";
    String postURL = "...";

    //String postURL = "/chart/1d?token=Tpk_b17df93a8a3044a7abc1731cd1cafe01";

    String averageValue;

    String[] firstSplit;

    public ParseData(List<String> syms){
        this.syms = syms;
    }

    public List<String> getAttributes(){
        String line = firstSplit[0];
        List<String> attributes = new ArrayList<String>();
        String[] att = line.split(",");

        for(String s:att){
            String[] a = s.split(":", 2);
            if(!a[0].equals("date") && !a[0].equals("label")  && !a[0].equals("high")
                    && !a[0].equals("low") && !a[0].equals("average")  && !a[0].equals("open")
                    && !a[0].equals("close")){
                //System.out.println(a[0]);
                attributes.add(a[0]);
            }
        }

        return attributes;
    }

    public List<String> getParsedDataSingle(String sym) throws Exception
    {
        try
        {
            URL url = new URL(this.preURL + sym + this.postURL);
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;

            while((line = br.readLine()) != null){
                //System.out.println("#########  " + line);

                line = line.replace("},{", "}@{");
                line = line.replace("[", "");
                line = line.replace("]", "");
                line = line.replace("{", "");
                line = line.replace("}", "");
                line = line.replace("\"", "");
                System.out.println("!!!!!!!!!!  " + line);
                this.firstSplit = line.split("@");
//
                for (String s:firstSplit) {
                    System.out.println(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return getSecondParse(this.firstSplit);
    }

    private List<String> getSecondParse(String[] firstPass) throws Exception
    {

        int highIndex = 0, lowIndex = 0;
        double max = 0.0, low = 100000000.0;

        List<String> returnPass = new ArrayList<>();

        try {
            for (int i = 17; i < firstPass.length; i++) {
                String[] split = firstPass[i].split(",");
                //System.out.println((split[5]));
                try{
                    String[] moneySplit = split[11].split(":");

                    if(!moneySplit[1].equals("null"))
                    {
                        if (max < Double.parseDouble(moneySplit[1])) {
                            max = Double.parseDouble(moneySplit[1]);
                            highIndex = i;
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            try
            {
                for (int i = 17; i < highIndex - 20; i++)
                {
                    String[] split = firstPass[i].split(",");
                    //System.out.println((split[5]));
                    String[] moneySplit = split[5].split(":");

                    if ((low > Double.parseDouble(moneySplit[1]) && Double.parseDouble(moneySplit[1]) > 0)) {
                        low = Double.parseDouble(moneySplit[1]);
                        lowIndex = i;
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            String temp = "";

            for (int i = 0; i < firstPass.length; i++) {

                String[] split = firstPass[i].split(",");
                for (String s : split) {
                    String[] secondSplit = s.split(":", 2);
                    /// plave into string for arff file
                    if (secondSplit[0].equals("minute")) {
                        temp += secondSplit[1].replace(":", "") + ",";
                        if (temp.charAt(0) == '0') {
                            temp = temp.substring(1);
                        }
                    } else if (!secondSplit[0].equals("date") && !secondSplit[0].equals("minute")
                            && !secondSplit[0].equals("label") && !secondSplit[0].equals("high")
                            && !secondSplit[0].equals("low") && !secondSplit[0].equals("average")
                            && !secondSplit[0].equals("open") && !secondSplit[0].equals("close")) {
                        //System.out.println(secondSplit[0]);
                        try {
                            if (secondSplit[1].equals("null")) {
                                //System.out.println("its null!");
                                temp += "?,";
                            } else {
                                temp += secondSplit[1] + ",";
                            }
                        }
                        catch (Exception e){
                            temp += "?,";
                        }
                    }
                }

//                System.out.println("low " + low + " index " + lowIndex);
//                System.out.println("high " + max + " highindex " + highIndex);



                if (lowIndex - 11 < i && i < lowIndex + 11) {
                    //copyPass[i] += ",buy";
                    temp += "buy";
                }
                else if (highIndex - 11 < i && i < highIndex + 11)
                {
                    //copyPass[i] += ",sell";
                    temp += "sell";
                }
                else {
                    //copyPass[i] += ",wait";
                    temp += "wait";
                }

                //System.out.println(temp);
                //if(highIndex != 0 && lowIndex != 0)
                if(temp.split(",").length == 15)
                {
                    returnPass.add(temp);
                }


                temp = "";
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return returnPass;

    }

    public void getParsedDataList(){

        for (String sym: this.syms) {
            try{
                URL url = new URL(this.preURL + sym + this.postURL);
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;

                while((line = br.readLine()) != null){
                    //System.out.println("#########  " + line);

                    line = line.replace("},{", "}|{");
                    line = line.replace("[", "");
                    line = line.replace("]", "");
                    System.out.println("!!!!!!!!!!  " + line);
                    String[] firstSplit = line.split("|");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public String getLastInstance(String sym) throws Exception
    {

        String[] fPass = new String[390];
        List<String> attrsVals = new ArrayList<>();
        List<String> attrs = new ArrayList<>();
        MoneyMaker mm = new MoneyMaker();

        try{
            URL url = new URL(this.preURL + sym + this.postURL);
            //System.out.println("getting url");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            //System.out.println("got url");
            String line;

            while((line = br.readLine()) != null){
                //System.out.println("#########  " + line);

                line = line.replace("},{", "}@{");
                line = line.replace("[", "");
                line = line.replace("]", "");
                line = line.replace("{", "");
                line = line.replace("}", "");
                line = line.replace("\"", "");
                //System.out.println("!!!!!!!!!!  " + line);
                fPass = line.split("@");
//

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try
        {
            if (fPass.length > 0) {
                String[] sPass = fPass[fPass.length - 1].split(",");
                for (String s : sPass) {
                    String[] line = s.split(":", 2);
                    if (!line[0].equals("date") && !line[0].equals("minute")
                            && !line[0].equals("label") && !line[0].equals("high")
                            && !line[0].equals("low") && !line[0].equals("average")
                            && !line[0].equals("open") && !line[0].equals("close")) {

                        attrs.add(line[0]);

                        String s1 = line[1].replace(":", "");

                        if (s1.equals("null")) {
                            attrsVals.add("?");
                        } else {
                            attrsVals.add(s1);
                        }
                    }
                    if(line[0].equals("marketaverage"))
                    {
                        this.averageValue = line[1];
                    }
                }
            }
        }
        catch (Exception e){

        }


        String line = "";

        for(String s: attrsVals){
            line += s + ",";
        }
        line += "?";



        return line;
    }

    public void modArffFile(String path) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(path));

        boolean inData = false;
        String line, prev = "";
        String[] split;
        Double dVolume = 0.0, dNotional = 0.0, dNumberOfTrades = 0.0, dChangeOverTime = 0.0;
        String saveClass, temp = "";
        List<String> returnToFile = new ArrayList<String>();

        while((line = br.readLine()) != null)
        {

           if(inData)
           {
                split = line.split(",");
                //System.out.println(line);
                if(split[0].equals("930"))
                {
                    for(int i = 0; i < split.length - 1; i++)
                    {
                        temp += split[i] + ",";
                    }

                    temp += dVolume.toString() + "," + dNotional.toString() + "," + dNumberOfTrades.toString() + "," + dChangeOverTime.toString() + "," + split[split.length - 1];
                    prev = temp;
                    //System.out.println("1 prev " + prev);
                    returnToFile.add(temp);
                    temp = "";
                }
                else
                {
                    for(int i = 0; i < split.length - 1; i++)
                    {
                        temp += split[i] + ",";
                    }
                    String[] pSplit = prev.split(",");
                    //System.out.println(pSplit.length);
                    //System.out.println(prev);

                    if(split[1].equals("?"))
                    {
                        temp += pSplit[14] + ",";
                    }
                    else
                    {
                        temp += Math.abs(Double.parseDouble(split[1]) - Double.parseDouble(pSplit[14])) + ",";
                    }
                    if(split[2].equals("?"))
                    {
                        temp += pSplit[15] + ",";
                    }
                    else
                    {
                        temp += Math.abs(Double.parseDouble(split[2]) - Double.parseDouble(pSplit[15])) + ",";
                    }
                    if(split[3].equals("?"))
                    {
                        temp += pSplit[16] + ",";
                    }
                    else
                    {
                        temp += Math.abs(Double.parseDouble(split[3]) - Double.parseDouble(pSplit[16])) + ",";
                    }
                    if(split[12].equals("?"))
                    {
                        temp += pSplit[17] + ",";
                    }
                    else
                    {
                        temp += Math.abs(Double.parseDouble(split[12]) - Double.parseDouble(pSplit[17])) + ",";
                    }

                    temp += split[split.length - 1];

                    //System.out.println(temp);
                    prev = temp;
                    returnToFile.add(temp);
                    temp = "";
                }
           }

            if(line.equals("@DATA"))
            {
                inData = true;
            }

        }

        System.out.println("modified file");
        BufferedWriter bw = new BufferedWriter(new FileWriter("redo.txt"));

        for(String s : returnToFile)
        {
            System.out.println(s);


            bw.write(s + "\n");
            bw.flush();
        }
    }

    public void removeMinute(String path) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(path + "Data.arff"));
        //BufferedWriter bw = new BufferedWriter(new FileWriter(path + "Data.txt"));

        boolean inData = false;
        List<String> l = new ArrayList<>();

        String line = "", temp = "";

        while((line = br.readLine()) != null)
        {
            String[] split = line.split(",");

            if(line.contains("minute"))
            {
            }

            else if(inData)
            {
                for (int i = 1; i < split.length - 1; i++)
                {
                    temp += split[i] + ",";
                }
                temp += split[split.length - 1];
                l.add(temp);
            }
            else
            {
                l.add(line);
            }


            if(line.contains("DATA"))
            {
                inData = true;
            }

//            bw.write(temp + "\n");
//            bw.flush();
            temp = "";
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(path + "Data.arff"));

        for(String s: l)
        {
            bw.write(s + "\n");
            bw.flush();
        }

    }

    public void getDataFortheDay(String sym)
    {
        try
        {
            //URL url = new URL(this.preURL + sym + this.postURL);

            //kryrality token
            URL url = new URL("https://cloud.iexapis.com/stable/stock/" + sym + "/chart/date/20190712?token=pk_98abe081b0f445b78a961b06dc300680");
            // gmail token
            //pk_81d47e5bd7094b48aa297324e358bb7e
            //URL url = new URL("https://sandbox.iexapis.com/stable/stock/" + sym + "/chart/date/20190709?token=Tpk_b17df93a8a3044a7abc1731cd1cafe01");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String title = sym + "_JULY-12-2019.txt";
            String line = "";
            BufferedWriter bw = new BufferedWriter(new FileWriter(title));

            while((line = br.readLine()) != null)
            {
                bw.write(line + "\n");
                bw.flush();
            }



            System.out.println(title);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void makeIntoArff(String sym) throws Exception
    {
        String dataPath = sym + "_JULY-12-2019.txt";
        BufferedReader br = new BufferedReader(new FileReader(dataPath));
        BufferedWriter bw = new BufferedWriter(new FileWriter(sym + "Data.arff", true));

        try
        {
            String line;

            while((line = br.readLine()) != null){
                //System.out.println("#########  " + line);

                line = line.replace("},{", "}@{");
                line = line.replace("[", "");
                line = line.replace("]", "");
                line = line.replace("{", "");
                line = line.replace("}", "");
                line = line.replace("\"", "");
                System.out.println("!!!!!!!!!!  " + line);
                this.firstSplit = line.split("@");
//
                for (String s:firstSplit) {
                    //System.out.println(s);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String s: getSecondParse(this.firstSplit))
        {
            bw.write(s + "\n");
            bw.flush();
        }



    }

    public void removeExtraMinute(String sym) throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(sym + "Data.arff"));

        String line, temp = "";
        List<String> l = new ArrayList<>();

        while((line = br.readLine()) != null)
        {
            String[] split = line.split(",");
            if(split.length == 15)
            {
                for(int i = 1; i < split.length -1; i++)
                {
                    temp += split[i] + ",";
                }
                temp += split[split.length - 1];
                l.add(temp);
                temp = "";
            }
            else
            {
                l.add(line);
            }
            temp = "";
        }

        br.close();

        BufferedWriter bw = new BufferedWriter(new FileWriter(sym + "Data.arff"));

        for(String s: l)
        {
            bw.write(s + "\n");
            bw.flush();
        }

        bw.close();
    }



}
