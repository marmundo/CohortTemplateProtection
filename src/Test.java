import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import cohort.Cohort;
import loader.ArffLoader;
import main.Main;
import utils.Utils;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SparseInstance;


public class Test {



	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Main main=new Main();
		ArffLoader loader= ArffLoader.getInstance();	
		loader.setClassIndex(0);
		Instances userInstances=loader.getInstances();


		Cohort cohort=new Cohort(userInstances);
		int numberOfCohortClasses=2;
		
		Instances cohortInstances=cohort.createCohortInstances(userInstances, numberOfCohortClasses);
		System.out.println("Num Classes: "+cohortInstances.numClasses());
		
		loader.writeToAFile(cohortInstances,"Cohort.arff");
		
		loader.writeToAFile(loader.getInstances(), "DataCuted.arff");

		//Modeling CohortTemplate DataSet
		int capacity=10000;
		ArrayList<Attribute> attributes=new ArrayList<Attribute>();
		for(int i=1;i<=numberOfCohortClasses;i++){
			Attribute attr=new Attribute("attr"+i,i-1);
			attributes.add(attr);
		}
		
		Attribute user=userInstances.classAttribute();
		attributes.add(user);
		
		Instances cohortTemplate=new Instances("CohortTemplate",attributes,capacity);


		Vector<String> classUsedVector=new Vector<String>();
		Vector<Double> scoreVector=new Vector<Double>();
		Instance inst= new DenseInstance(numberOfCohortClasses+1);

		for (int i = userInstances.numInstances() - 1; i >= 0; i--) {
			Instance userInstance = userInstances.get(i); // user instance
			for (int j = cohortInstances.numInstances() - 1; j >= 0; j--) {
				Instance cohortInstance = cohortInstances.get(j);
				String classValue=cohortInstance.classAttribute().value((int)cohortInstance.classValue());		

				//Just calculate euclidean distance of new instances whose class was not calculated
				if(!classUsedVector.contains(classValue)){
					classUsedVector.add(classValue);
					EuclideanDistance distance=new EuclideanDistance(userInstances);
					double score=distance.distance(userInstance, cohortInstance);
					scoreVector.add(score);
				}
			}

			//Filling the template instance for user i
			for(int k=0;k<numberOfCohortClasses;k++){
				inst.setValue(attributes.get(k), scoreVector.get(k));
			}
			inst.setValue(user, userInstance.classValue());
			cohortTemplate.add(inst);
			classUsedVector.clear();
			scoreVector.clear();
		}		

		loader.writeToAFile(cohortTemplate, "CohortTemplate-"+numberOfCohortClasses+".arff");
		

		//cohort=ArffLoader.getInstance().cutAndPasteInstancesWithClassValue("1", cohort);
		//main.WriteToAFile(cohort,"CohortClass1.arff");

		








	}

}
