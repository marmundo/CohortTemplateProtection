package main;

import java.io.IOException;

import cohort.Cohort;

import loader.ArffLoader;
import template.CohortTemplate;
import utils.Progress;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 * Main Class of this project
 * @author marcelo
 *
 */
public class Main {

	//the ClassLoader object associated with this Class
	private ClassLoader cLoader;

	//Path of dataset.arff
	private String path;

	//Creating loader which load the dataset
	ArffLoader loader;



	public void executeExample(){
		//Creating a new labeled object which will receive the predicted class
		Instances instances = loader.getInstances();
		Instances labeled = new Instances(instances);

		//Decision Tree
		J48 tree = new J48();

		//Classification of labeled instances
		System.out.println("Real                   Predicted");
		try {
			//Training the classifier
			tree.buildClassifier(instances);
			for (int i = 0; i < instances.numInstances(); i++) {
				double clsLabel = tree.classifyInstance(labeled.instance(i));
				labeled.instance(i).setClassValue(clsLabel);
				System.out.print("Real: " + instances.classAttribute().value((int) instances.instance(i).classValue()));
				System.out.println(", Predicted: " + labeled.classAttribute().value((int) clsLabel));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(labeled.toSummaryString());


	}

	public void executecohortExample(double percentage){    	
		ArffLoader loader= ArffLoader.getInstance();	
		loader.setClassIndex(0);
		Instances instances=loader.getInstances();

		Cohort cohort=new Cohort(instances);
		//double percentage=0.1;
		Instances cohortInstances=cohort.createCohortInstances(instances, percentage);

		CohortTemplate cohortTemplateDataSet=new CohortTemplate(instances, cohortInstances, percentage);
		Instances cohortTemplate=cohortTemplateDataSet.createCohortTemplateDataSet();

		int numberOfCohortClasses=(int)(percentage*instances.numClasses());

		loader.writeToAFile(cohortTemplate, "CohortTemplate-"+numberOfCohortClasses+".arff");
	}




	//    public Instances getInstances(){
	//	return loader.getInstances();
	//    }




	public Main(){

		cLoader= Main.class.getClassLoader();

		path = cLoader.getResource("data/dataset.arff").getFile();

		loader=new ArffLoader(path);


	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Main main=new Main();	
		//main.executeExample();
		Progress p=new Progress();
		try {

			for(double i=0.35;i<=0.5;i=i+0.05){
				p.start("Starting..."+i+" percentage");
				main.executecohortExample(i);
				p.stop();
				System.out.println("Finished..."+i+" percentage");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//for(double i=0.4;i<=0.5;i=i+0.05)
		//	main.executecohortExample(i);

		//main.executecohortExample(0.1);
	}

}
