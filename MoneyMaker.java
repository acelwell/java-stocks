import weka.*;

import weka.classifiers.Evaluation;
import weka.classifiers.meta.Bagging;
import weka.classifiers.meta.RandomCommittee;

import weka.classifiers.trees.RandomForest;
import weka.classifiers.trees.RandomTree;
import weka.core.DenseInstance;
import weka.core.converters.ConverterUtils.DataSource;

import weka.core.Instances;
import weka.core.Instance;
import javax.sound.sampled.*;
import java.util.*;


public class MoneyMaker {

    private String arffFile;
    private String testArffFile;
    private Bagging bags = new Bagging();
    private RandomCommittee rc = new RandomCommittee();
    private Instances trainInstances;
    private Instances testInstances;
    private DataSource ds;


    public MoneyMaker() throws Exception {

        this.arffFile = "SafeData.arff";
        this.testArffFile = "test.arff";
        this.ds = new DataSource(this.arffFile);
    }

    public void buildClassifier() throws Exception{

        DataSource source = new DataSource(this.arffFile);
        this.trainInstances = source.getDataSet();
        this.trainInstances.setClassIndex(trainInstances.numAttributes() - 1);

        this.bags.buildClassifier(trainInstances);
        this.rc.buildClassifier(trainInstances);

        //testClassifiers();

    }

    private void testClassifiers() throws Exception{
        DataSource source = new DataSource(this.testArffFile);

        this.testInstances = source.getDataSet();
        this.testInstances.setClassIndex(testInstances.numAttributes() - 1);



        Evaluation eval = new Evaluation(this.trainInstances);
        eval.evaluateModel(this.rc, this.testInstances);
        System.out.println(eval.toSummaryString());

        eval = new Evaluation(this.trainInstances);
        eval.evaluateModel(this.bags, this.testInstances);
        System.out.println(eval.toSummaryString() + "\n\n\n");

    }

    public double classifyInstance () throws Exception {


        DataSource source = new DataSource("live.arff");
        Instances live = source.getDataSet();
        live.setClassIndex(live.numAttributes() - 1);

        //System.out.println("classifying!!!");

        double bagval = this.bags.classifyInstance(live.lastInstance());
        double commval = this.rc.classifyInstance(live.lastInstance());

        if(commval == 0)
        {
            return 0;
        }
        else if(bagval == 1 || commval == 1)
        {
            return 1;
        }

        return 2;
    }

    public double classifyInstance(String sym) throws Exception
    {
        DataSource source = new DataSource("live.arff");
        Instances live = source.getDataSet();
        live.setClassIndex(live.numAttributes() - 1);



        Instances symInstances = new DataSource(sym + "Data.arff").getDataSet();
        symInstances.setClassIndex(symInstances.numAttributes() - 1);

        RandomCommittee liverc = new RandomCommittee();
        liverc.buildClassifier(symInstances);

        Bagging livebag = new Bagging();
        livebag.buildClassifier(symInstances);

        RandomTree livert = new RandomTree();
        livert.buildClassifier(symInstances);

        RandomForest liverf = new RandomForest();
        liverf.buildClassifier(symInstances);

        Instance insta = live.lastInstance();

        double commval = liverc.classifyInstance(insta);
        double bagval = livebag.classifyInstance(insta);
        double rtval = livert.classifyInstance(insta);
        double rfval = liverf.classifyInstance(insta);

        if(commval == 0 && bagval == 0 && rfval == 0)
        {
            return 0;
        }
        else if(bagval == 1 || commval == 1)
        {
            return 1;
        }

        return 2;
    }
}



