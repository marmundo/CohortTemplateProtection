package experiments;

import java.util.Iterator;
import java.util.Random;

import loader.ArffLoader;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class DecisionTree {
	
	private Instances data;
	private String[] options;
	private J48 tree;
	private Evaluation eval;
	
	public DecisionTree(String[] options, Instances data){
		this.options=options;
		this.data=data;
	}
	
	public void training(){		 
		 tree = new J48();         // new instance of tree
		 try {
			tree.setOptions(options);
			tree.buildClassifier(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     // set the options		
	}
	
	public void evaluation(int folds){		
		try {
			eval=new Evaluation(data);
			eval.crossValidateModel(tree, data, folds, new Random(10));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printStatistics(){
		System.out.println("Summary: "+eval.toSummaryString());
		System.out.println("Percentage Correct: "+eval.pctCorrect());
		System.out.println("Percentage Incorrect: "+eval.pctIncorrect());
		System.out.println("Root Mean Squared Error: "+eval.rootMeanSquaredError());
		System.out.println("Kappa: "+eval.kappa());				
	}
	
	public static void main(String[]args){
		ClassLoader cLoader = DecisionTree.class.getClassLoader();

		String path;
		ArffLoader loader;
		String[] options = null;

		//Classifier Settings
		try {
			options = weka.core.Utils.splitOptions("-C 0.25 -M 2");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		path = cLoader.getResource("data/dataset.arff").getFile();
		loader = new ArffLoader(path);
		loader.setClassIndex(loader.getInstances().numAttributes()-1);
		
		
		DecisionTree tree=new DecisionTree(options,loader.getInstances());
		tree.training();
		tree.evaluation(3);
		System.out.println("\n******3-Fold -- Tree for Original Data*****\n\n");
		tree.printStatistics();
		
		for(int index=2;index<=20;index=index+2){
			path = cLoader.getResource("data/CohortTemplate-"+index+".arff").getFile();
			loader = new ArffLoader(path);
			loader.setClassIndex(loader.getInstances().numAttributes()-1);
			
			
			tree=new DecisionTree(options,loader.getInstances());
			tree.training();
			tree.evaluation(3);
			System.out.println("\n******3-Fold -- Tree for CohortTemplate-"+index+".arff*****\n\n");
			tree.printStatistics();
		}
		
	}
}
