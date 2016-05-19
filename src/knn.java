import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * kNN class performs all steps involve to carry out the k nearest neighbour
 * algorithm. 
 * @author Krina Nagar
 *
 */
public class knn {
	
	private ArrayList<Instance> trainingList = new ArrayList<Instance>(); //list to store iris-training.txt
	private ArrayList<Instance> testList = new ArrayList<Instance>(); //list to store iris-test.txt
	
	/**
	 * Scans each file into corresponding array lists declared by reading each line
	 * in the file and defining its instances.
	 * @param trainingFile
	 * @param testFile
	 */
	public void loadData(String trainingFile, String testFile){
		try {
			Scanner sc1 = new Scanner(new File(trainingFile));
			Scanner sc2 = new Scanner(new File(testFile));
			while(sc1.hasNext()){
				
				double slength = sc1.nextDouble();
				double swidth = sc1.nextDouble();
				double plength = sc1.nextDouble();
				double pwidth = sc1.nextDouble();
				String t = sc1.next();
				trainingList.add(new Instance(slength, swidth, plength, pwidth, t));
			}
			
			while(sc2.hasNext()){
				double slength = sc2.nextDouble();
				double swidth = sc2.nextDouble();
				double plength = sc2.nextDouble();
				double pwidth = sc2.nextDouble();
				String t = sc2.next();
				testList.add(new Instance(slength, swidth, plength, pwidth, t));
			}
			sc1.close();
			sc2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Obtains ranges for each attribute of the flower and carries out the 
	 * euclidean distance calculation 
	 * @param instance1 - test item
	 * @param instance2 - training item
	 * @return distance
	 */
	public double calculateDistance(Instance instance1, Instance instance2){
		//get ranges
		double sLRange = range(trainingList, "sepalLength");
		double swRange = range(trainingList, "sepalWidth");
		double pLRange = range(trainingList, "petalLength");
		double pwRange = range(trainingList, "petalWidth");

		double petalL = Math.pow((instance1.getPetalLength() - instance2.getPetalLength())/pLRange,2);
		double petalW = Math.pow((instance1.getPetalWidth() - instance2.getPetalWidth())/pwRange,2);
		double sepalL = Math.pow((instance1.getSepalLength() - instance2.getSepalLength())/sLRange,2);
		double sepalW = Math.pow((instance1.getSepalWidth() - instance2.getSepalWidth())/swRange, 2);

		return Math.sqrt(petalL + petalW + sepalL + sepalW);
		
	}
	/**
	 * Calculate the range of each attribute passed into method
	 * @param list
	 * @param type
	 * @return range of specified type
	 */
	public static double range(ArrayList<Instance> list, String type) {
		double min = 0;
		double max = 0;
	    if (list.isEmpty()) {
	        return 0;
	    }
	    //range for sepal length
	    else if (type == "sepalLength") {
	        max = list.get(0).getSepalLength();
	        min = list.get(0).getSepalLength();
	        for (final Instance i : list) {
	            if (i.getSepalLength() > max) {
	                max = i.getSepalLength();
	            } else if (i.getSepalLength() < min) {
	                min = i.getSepalLength();
	            }
	        }
	    }
	    //range for sepal width
	    else if (type == "sepalWidth") {
	        max = list.get(0).getSepalWidth();
	        min = list.get(0).getSepalWidth();
	        for (final Instance i : list) {
	            if (i.getSepalWidth() > max) {
	                max = i.getSepalWidth();
	            } else if (i.getSepalWidth() < min) {
	                min = i.getSepalWidth();
	            }
	        }
	    }
	    //range for petal length
	    else if (type == "petalLength") {
	        max = list.get(0).getPetalLength();
	        min = list.get(0).getPetalLength();
	        for (final Instance i : list) {
	            if (i.getPetalLength() > max) {
	                max = i.getPetalLength();
	            } else if (i.getPetalLength() < min) {
	                min = i.getPetalLength();
	            }
	        }
	    }
	    //range for petal width
	    else if (type == "petalWidth") {
	        max = list.get(0).getPetalWidth();
	        min = list.get(0).getPetalWidth();
	        for (final Instance i : list) {
	            if (i.getPetalWidth() > max) {
	                max = i.getPetalWidth();
	            } else if (i.getPetalWidth() < min) {
	                min = i.getPetalWidth();
	            }
	        }
	    }
	    return max - min;
	}
	
	/**
	 * Get the nearest neighbours based on k. 
	 * @param training
	 * @param test
	 * @param k
	 * @return result
	 */
	public String performAlgorithm(ArrayList<Instance> training, Instance test, int k){
		//calculate all distances
		HashMap<Double, String> allDistances = getDistances(training, test);
		
		ArrayList<Double> distanceList = new ArrayList<Double>(allDistances.keySet()); //key set of doubles in map
		Collections.sort(distanceList); //sort all distances from lowest to highest

		HashMap<Double, String> kNeighbours = topKNeighbours(k, distanceList, allDistances);
		
		String result = rankNeighbours(k, kNeighbours);

		return result;
	}
	
	/**
	 * Calculates tally result of most occuring flower from k neighbours
	 * @param k
	 * @param kNeighbours
	 * @return highest ranked neighbour
	 */
	private String rankNeighbours(int k, HashMap<Double, String> kNeighbours) {
		HashMap<String,Integer> nNeighbours = new HashMap<String, Integer>(); //map to store k neighbours ranks
		String key; 
		int i = 0;
		for(Map.Entry<Double, String> entry :kNeighbours.entrySet()) {	
			while(i<k){
				key = entry.getValue();
				//if instance is already in map, add one to its value
				if(nNeighbours.containsKey(key)){
					int rank = nNeighbours.get(key);
					rank++ ;
					nNeighbours.put(entry.getValue(), rank);
				}
				//if instance is not in map, add instance and value of 1
				else{
					nNeighbours.put(entry.getValue(), 1);
				}
				i++;
				break;
			}
		}
		//most highly ranked flower
		return (String) (nNeighbours.keySet().toArray()[0]);
	}

	/**
	 * Calculate all the distances between test instance and training set
	 * @param training
	 * @param test
	 * @return map of distances and corresponding flower
	 */
	public HashMap<Double, String> getDistances(ArrayList<Instance> training, Instance test){
		HashMap<Double, String> distances = new HashMap<Double,String>(); //map to store each distance and its type
		for(Instance item: training){
			double dist = calculateDistance(test, item);
			distances.put(dist, item.getType());
			
		}
		return distances;
	}
	/**
	 * Get the top k neighbours from all distances
	 * @param k
	 * @param distanceList
	 * @param allDistances
	 * @return map of top k neighbours distances and corresponding flower
	 */
	public HashMap<Double, String> topKNeighbours(int k, ArrayList<Double> distanceList, HashMap<Double, String> allDistances){
		HashMap<Double, String> kNeighbours = new HashMap<Double,String>(); //map to store k neighbours
		int index = 0;
		//add top k neighbours to map
		while(index <k){
			kNeighbours.put(distanceList.get(index), allDistances.get(distanceList.get(index)));
			index++;
		}
		return kNeighbours;
	}
	
	/**
	 * Calcualte knn accuracy
	 * @param test
	 * @param predictions
	 * @return accuracy
	 */
	public Double getAccuracy(ArrayList<Instance> test, ArrayList<String> predictions){
		double correct = 0;
		for(int i = 0; i <= test.size()-1; i++){
			if(test.get(i).getType().equals(predictions.get(i))){ //if test instance matches predictions
				correct ++;
			}
		}
		double accuracy = (correct/test.size()) *100.00;
		return accuracy;
	}
	
	public static void main(String[] args){
		knn knn = new knn();
		knn.loadData(args[0],args[1]);
		ArrayList<String> predictions = new ArrayList<String>();
		for(Instance item: knn.testList){
			String result = knn.performAlgorithm(knn.trainingList, item, 3);
			predictions.add(result);
		}
		double accuracy = knn.getAccuracy(knn.testList, predictions);
		System.out.println("Accuracy: " + accuracy + '%');
		}
}
