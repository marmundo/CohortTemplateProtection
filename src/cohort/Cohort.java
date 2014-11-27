package cohort;

import loader.ArffLoader;
import utils.Utils;
import weka.core.Instances;

public class Cohort {
    
    Instances cohort;
    
    /**
     * Constructor. Cohort instances will be based on @param instances. 
     * @param instances
     */
    public Cohort(Instances instances){
	cohort=new Instances(instances);
	cohort.clear();
    }
    
    
    public Instances createCohortInstances(Instances userInstances, double cohortPercentage){
    	int nClasses=(int)(cohortPercentage*userInstances.numClasses());
    	return createCohortInstances(userInstances, nClasses);
    }
    
    /**
     * Methods to create cohort instances
     * @param instances Instances whose cohort will be created
     * @param nClasses Number of classes whose cohort instances will be create
     * @return cohort instances
     */
    public Instances createCohortInstances(Instances instances, int nClasses){
	ArffLoader loader=ArffLoader.getInstance();		
	int numberOfAttributes=	instances.classAttribute().numValues();	
	int random;
	Instances cut;
	while(nClasses>0){
	    random=Utils.generateRandomNumber(1, numberOfAttributes);
	    cut=loader.cutAndPasteInstancesWithClassValue(String.valueOf(random));
	    //This if was did because the random function can generate the same number different times
	    if(cut.numInstances()>0){
		cohort.addAll(cut);
		nClasses--;
	    }	    
	}
	return cohort;
    }
	
	
	
	    
    
//    /**
//     * Append @param instances to cohort dataset
//     * @param instances
//     */
//    private void append(Instances merge){
//	cohort.addAll(merge);	
//    }
    
    

    /**
     * Verify if the @param values there is in @param instances
     * @param instances
     * @param value
     * @return
     */
//    private boolean classValueExists(Instances instances,Object value){
//	instances.classAttribute().enumerateValues()
//	return true;
//    }
    
    
}
