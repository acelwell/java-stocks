
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main (String[] args) throws Exception {

        FetchData fd = new FetchData("src/StockList.txt");
        ParseData pd = new ParseData(fd.getSymList());
        FileWriting fw = new FileWriting("data.arff");
        MoneyMaker mm = new MoneyMaker();
        FileReading fr = new FileReading();



        fw.setAttributes("Attributes.txt");
        fw.setSafeList("src/StockList.txt");

        //fw.setupFile("SafeData.arff");
        List<String> stocks = new ArrayList<>();
        stocks = fr.getStockList("src/watchlist.txt");

        //pd.removeMinute("aumn");
        //pd.removeExtraMinute("aapl");
//
        for(String s: stocks)
        {
            System.out.println("################## " + s);
            //pd.removeExtraMinute(s);
            //pd.removeMinute(s);
            //fw.setupFile(s + "Data.arff");
//            pd.getDataFortheDay(s);
//
//            pd.makeIntoArff(s);
//            List<String> instances = pd.getParsedDataSingle(s);
//            for(String s1 : instances)
//            {
//                fw.addToSafeFile(s1);
//                System.out.println(s1);
//            }
            //System.out.println(s + " ####################################################################");
        }

        //pd.modArffFile("SafeData.arff");
        //pd.removeMinute("SafeData.arff");


        System.out.println(LocalDateTime.now().getSecond());
        mm.buildClassifier();
        System.out.println(LocalDateTime.now().getSecond());



        fw.setupLiveFile();



        //System.out.println(LocalDateTime.now().getHour() + LocalDateTime.now().getMinute());

        stocks = fr.getStockList("src/shortlive.txt");

        while(true)
        {

            //System.out.println("waiting for market to open");
            if(LocalDateTime.now().getHour() >= 6 && LocalDateTime.now().getHour() < 13)
            {

                System.out.println("Time start: " + LocalDateTime.now().getHour() + " " + LocalDateTime.now().getMinute() + " " + LocalDateTime.now().getSecond());
                for (String s : stocks)
                {
                    String line = pd.getLastInstance(s);

                    //System.out.println(line);

                    fw.addToLiveFile(line);

                    String value = pd.averageValue;

                    double action = mm.classifyInstance(s);

                    if (action == 0)
                    {
                        new Thread(new ExtraTread(s, 0)).start();
                        //t.start();
                        fw.addToBuySell("Buy " + s + " at " + value + " at " + (LocalDateTime.now().getHour()+3) + ":" + LocalDateTime.now().getMinute());
                        //System.out.println("Buy " + s + " at " + pd.getAverageValue(s) + " at " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "\n");
                    }
                    else if(action == 1)
                    {
                        new Thread(new ExtraTread(s, 1)).start();
                        fw.addToBuySell("Sell " + s + " at " + value + " at " + (LocalDateTime.now().getHour()+3) + ":" + LocalDateTime.now().getMinute());
                    }
                    else
                    {
                        System.out.println("still running :)");
                        //fw.addToBuySell("Wait " + s + " at " + value + " at " + (LocalDateTime.now().getHour()+3) + ":" + LocalDateTime.now().getMinute());
                    }
                }


                System.out.println("Time end: " + LocalDateTime.now().getHour() + " " + LocalDateTime.now().getMinute() + " " + LocalDateTime.now().getSecond());
                System.out.println("##########################################");
                Thread.sleep(1000 * 110);

            }
            else if(5 <= LocalDateTime.now().getHour() && LocalDateTime.now().getHour() <= 6)
            {
                System.out.println("waiting for market to open");
                Thread.sleep(1000 * 60 * 60);
            }
            else
            {
                System.out.println("waiting for market to open long wait");
                Thread.sleep(1000 * 60 * 150);
            }
        }
//
    }


}
