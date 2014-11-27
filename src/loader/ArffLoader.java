package loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;


/**
 * Class to Load Arff File
 * @author marcelo
 *
 */

public class ArffLoader {

    private static ArffLoader instance = null;

    private Instances instances;

    private String filePath;
    
    RemoveWithValues remove;

    /**
     * Returns the absolute path of arff dataset
     * @return Absolute path of arff dataset
     */
    public String getFilePath() {
        return filePath;
    }


    /**
     * Returns the instances of arff dataset
     * @return Instances of arff dataset
     */
    public Instances getInstances() {
	return instances;
    }
    
   public void setInstances(Instances inst){
       instances=inst;
   }

  
    /**
     * Constructor. This constructor receives the {@link path} of arff dataset
     * @param path The full path of arff dataset
     */
    public ArffLoader(String path){
	filePath=path;
	load(filePath);
	instance=this;
	remove	=new RemoveWithValues();	
    }

    protected ArffLoader(){
	
    }
    
    /**
     * Method returns a ArffLoader instance. This object can be used to acess the arff dataset.
     * @return ArffLoader intances
     */
    public static ArffLoader getInstance() {
	if(instance == null) {
	    instance = new ArffLoader();
	}
	return instance;
    }

    /**
     * Method to cut and past instances with {@link classValueToBeCuted} from DataSet to {@link cohort} dataset
     * @param indexClassValueToBeCuted
     * @param cohort
     * @return
     */
    public Instances cutAndPasteInstancesWithClassValue(String indexClassValueToBeCuted){
	setClassIndex(0);
	Instances cohort=new Instances(getInstances());
	cohort.clear();
	String classValue="";
	//writeToAFile(getInstances(), "IntancesTemp-"+indexClassValueToBeCuted+".arff");
	for (int i = getInstances().numInstances() - 1; i >= 0; i--) {
	    Instance inst = getInstances().get(i);
	    classValue=inst.classAttribute().value((int)inst.classValue());		
	    if (classValue.equals(indexClassValueToBeCuted)) {
		cohort.add(inst);		
		getInstances().delete(i);		
	    }		    
	}	
	//if(!cohort.isEmpty())
	//    cleanClassWithZeroInstances(indexClassValueToBeCuted);
	return cohort;
    }
    
    
   public void cleanClassWithZeroInstances(Instances dataset){

   }
    
    public void cleanClassWithZeroInstances(String classValue){
	
	String[] options=new String[7];		
	try {
	    options = weka.core.Utils.splitOptions("-S 0.1 -C 1 -L "+classValue+ " -H");	    
	    remove.setInputFormat(instances);
	    remove.setOptions(options);	 
	    setInstances(Filter.useFilter(getInstances(), remove));		
	} catch (Exception e) {
	    e.printStackTrace();
	}		
	writeToAFile(getInstances(),"InstanceWithout-."+classValue+"-.arff");
    }    
    
    public void writeToAFile(Instances instances,String name){
  	BufferedWriter writer;
  	ClassLoader cLoader=ArffLoader.class.getClassLoader();
  	try {
  	    writer = new BufferedWriter(
  		    new FileWriter(cLoader.getResource("data").getPath()+"/"+name));
  	    writer.write(instances.toString());
  	    writer.newLine();
  	    writer.flush();
  	    writer.close();
  	} catch (IOException e) {
  	    // TODO Auto-generated catch block
  	    e.printStackTrace();
  	}
      }

    /**
     * Removes the instances with a specific class given by {@link classValueToBeRemoved}
     * @param classValueToBeRemoved class which instances will be remnoved
     */
    public void removeInstancesWithClassValue(String classValueToBeRemoved){		
	for (int i = getInstances().numInstances() - 1; i >= 0; i--) {
	    Instance inst = getInstances().get(i);
	    String classValue=inst.classAttribute().value((int)inst.classValue());
	    if (classValue.equals(classValueToBeRemoved)) {
		getInstances().delete(i);		
	    }	    
	}
    }

    /**
     * Dataset
     */
    DataSource source;

    /**
     * Load a dataset in arff Format.
     * @param path Full path + name of the file
     * @return Instances object
     */
    private Instances load(String path){
	try {
	    instances = new Instances(
		    new BufferedReader(
			    new FileReader(path)));
	    return instances;
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Sets the class of arff dataset
     * @param value index of attribute (starts in 0)
     */
    public void setClassIndex(int value){
	//Setting userid as class
	instances.setClassIndex(value);
    }

    /**
     * Load a dataset in arff Format. 
     * @return Instances object
     */
    public void load(){
	try {
	    instances = new Instances(
		    new BufferedReader(
			    new FileReader(filePath)));	    
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();	    
	}
    }

}
