package template;

import java.util.ArrayList;
import java.util.Vector;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;

public class CohortTemplate {

	Instances cohortInstances;
	Instances userInstances;
	int numberOfCohortClasses;
	ArrayList<Attribute> cohortTemplateAttributes;
	Instances cohortTemplate;
	
	public CohortTemplate(Instances user,Instances cohort,double cohortPercentage){		
		this(user,cohort,(int)(cohortPercentage*user.numClasses()));
	}

	private CohortTemplate(Instances user,Instances cohort,int numOfCohortModels){	
		userInstances=user;
		cohortInstances=cohort;
		numberOfCohortClasses=numOfCohortModels;
		modelTemplateDataSet();
	}

	private void modelTemplateDataSet(){
		//Modeling CohortTemplate DataSet
		int capacity=userInstances.numInstances();
		cohortTemplateAttributes=new ArrayList<Attribute>();
		for(int i=1;i<=numberOfCohortClasses;i++){
			Attribute attr=new Attribute("attr"+i,i-1);
			cohortTemplateAttributes.add(attr);
		}

		Attribute user=userInstances.classAttribute();
		cohortTemplateAttributes.add(user);
		
		cohortTemplate=new Instances("CohortTemplate",cohortTemplateAttributes,capacity);
	}

	public Instances createCohortTemplateDataSet(){
		Attribute user=userInstances.classAttribute();
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
				inst.setValue(cohortTemplateAttributes.get(k), scoreVector.get(k));
			}
			inst.setValue(user, userInstance.classValue());
			cohortTemplate.add(inst);
			classUsedVector.clear();
			scoreVector.clear();
		}		
		return cohortTemplate;
	}

	

}
