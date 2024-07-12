package mcdm.topsis.service;

import java.util.List;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class TopsisService {

	private List<String> criteria;
    private List<String> alternatives;
    private List<Double> weights;
    private int numOfCriteria; //columns or j
    private int numOfAlternatives; //rows or i
    private List<List<Double>> criteriaValues;
    private List<Double> divisor; //divide to normalize
    private List<List<Double>> normalizedDecisionMatrix;
    private List<List<Double>> weightedNormalizedDecisionMatrix;
    private List<Double> idealBest;
    private List<Double> idealWorst;
    private List<Double> euclidianDistFromIdealBest;
    private List<Double> euclidianDistFromIdealWorst;
//    private List<Double> euclidianSum;
    private List<Double> performanceScore;
    private List<Integer> ranks;

    public TopsisService(List<Double> weights, List<String> criteria, List<String> alternatives, List<List<Double>> criteriaValues, List<Boolean> benefitCriteria) {
        this.weights = weights;
        this.criteria = criteria;
        this.alternatives = alternatives;
        this.criteriaValues = criteriaValues;
        this.numOfCriteria = criteria.size();
        this.numOfAlternatives = alternatives.size();

        if (validateData()) {
            calculateNormalizedMatrix();
            calculateWeightedNormalizedMatrix();
            calculateIdealValues(benefitCriteria);
            calculateEuclidianDistances();
            calculatePerformanceScore();
            calculateRanks();
        } else {
            throw new IllegalArgumentException("Invalid input data");
        }
    }

    private boolean validateData() {
    	// 2ND CODE
    	if (weights == null || criteria == null || alternatives == null || criteriaValues == null) {
            return false;
        }
        // Additional validation as needed
    	System.out.println("\n^^^ valid data\n");
        return true;
        
        
        // 1ST CODE : GAVE EXCEPTIONS
//        double weightSum = weights.stream().mapToDouble(Double::doubleValue).sum();
//        return Math.abs(weightSum - 1.0) < 1e-10 && !alternatives.isEmpty() && !criteria.isEmpty();
    }
    
    private void calculateNormalizedMatrix() {
        divisor = new ArrayList<>(Collections.nCopies(numOfCriteria, 0.0)); 
        //no of elements in divisor = that of numOfCriteria and all values = 0
        for (int j = 0; j < numOfCriteria; j++) {
            double sum = 0;
            for (int i = 0; i < numOfAlternatives; i++) {
                sum += Math.pow(criteriaValues.get(i).get(j), 2);
            }
            divisor.set(j, Math.sqrt(sum));
        }

        normalizedDecisionMatrix = new ArrayList<>();
        for (int i = 0; i < numOfAlternatives; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < numOfCriteria; j++) {
                row.add(criteriaValues.get(i).get(j) / divisor.get(j));
            }
            normalizedDecisionMatrix.add(row);
        }
        System.out.println(normalizedDecisionMatrix);
        System.out.println("\n^^^ normalized\n");
    }

    private void calculateWeightedNormalizedMatrix() {
        weightedNormalizedDecisionMatrix = new ArrayList<>();
        for (int i = 0; i < numOfAlternatives; i++) {
        	//not needed but row makes it easy to add elements to weightedNormalizedDecisionMatrix
        	//by adding 1d list to the 2d list like we add elements to a 1d list
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < numOfCriteria; j++) {
                row.add(normalizedDecisionMatrix.get(i).get(j) * weights.get(j));
            }
            weightedNormalizedDecisionMatrix.add(row);
        }
        System.out.println(weightedNormalizedDecisionMatrix);
        System.out.println("\n^^^ weighted normalized\n");
    }

    private void calculateIdealValues(List<Boolean> benefitCriteria) {
        idealBest = new ArrayList<>();
        idealWorst = new ArrayList<>();

        for (int j = 0; j < numOfCriteria; j++) {
        	//column is a temp veriable to store all values for a criteria and get max and min elements from it
            List<Double> column = new ArrayList<>();
            for (int i = 0; i < numOfAlternatives; i++) {
                column.add(weightedNormalizedDecisionMatrix.get(i).get(j));
            }
            
            
            if (benefitCriteria.get(j)) { 
            	//if benefit
                idealBest.add(Collections.max(column));
                idealWorst.add(Collections.min(column));
            } else {
            	//if cost
                idealBest.add(Collections.min(column));
                idealWorst.add(Collections.max(column));
            }
        }
        System.out.println(idealBest);
        System.out.println(idealWorst);
        System.out.println("\n^^^ ideal values calculated\n");
    }
    
    private void calculateEuclidianDistances() {
        euclidianDistFromIdealBest = new ArrayList<>();
        euclidianDistFromIdealWorst = new ArrayList<>();

        for (int i = 0; i < numOfAlternatives; i++) {
            double distBest = 0;
            double distWorst = 0;
            // just to make readable, they store SUM of SQUARE of DIFFERENCES
            for (int j = 0; j < numOfCriteria; j++) {
                distBest += Math.pow(weightedNormalizedDecisionMatrix.get(i).get(j) - idealBest.get(j), 2);
                distWorst += Math.pow(weightedNormalizedDecisionMatrix.get(i).get(j) - idealWorst.get(j), 2);
            }
            euclidianDistFromIdealBest.add(Math.sqrt(distBest));
            euclidianDistFromIdealWorst.add(Math.sqrt(distWorst));
        }
        System.out.println(euclidianDistFromIdealBest);
        System.out.println(euclidianDistFromIdealWorst);
        System.out.println("\n^^^ euclidian distances calculated\n");
    }
    
    
    
    
    private void calculatePerformanceScore() {
        performanceScore = new ArrayList<>();
        for (int i = 0; i < numOfAlternatives; i++) {
            double score = euclidianDistFromIdealWorst.get(i) / (euclidianDistFromIdealBest.get(i) + euclidianDistFromIdealWorst.get(i));
            performanceScore.add(score);
            System.out.println(score);
        }
        System.out.println(performanceScore);
        System.out.println("\n^^^ score calculated\n");
    }

//    private void calculateRanks() {
//        ranks = new ArrayList<>(Collections.nCopies(numOfAlternatives, 0));
//        List<Integer> indices = new ArrayList<>();
//        for (int i = 0; i < numOfAlternatives; i++) {
//            indices.add(i);
//        }
//        indices.sort((a, b) -> Double.compare(performanceScore.get(b), performanceScore.get(a)));
//        for (int i = 0; i < numOfAlternatives; i++) {
//            ranks.set(indices.get(i), i + 1);
//        }
//    }

   
//    private void calculatePerformanceScore() {
//        performanceScore = new ArrayList<>();
//        for (int i = 0; i < numOfAlternatives; i++) {
//        	euclidianSum.add(euclidianDistFromIdealBest.get(i) + euclidianDistFromIdealWorst.get(i));
//            double score = euclidianDistFromIdealWorst.get(i) / euclidianSum.get(i);
//            performanceScore.add(score);
//        }
//        System.out.println("perf score");
//    }
//   
    
    //CODE WITH SIMPLIFIED LOGIC
    public void calculateRanks() {
    	  // Initialize ranks with 0s and indices with original order
    	  
    	  ranks = new ArrayList<>(Collections.nCopies(numOfAlternatives, 0));
    	  List<Integer> indices = new ArrayList<>();
    	  for (int i = 0; i < numOfAlternatives; i++) {
    	    indices.add(i);
    	  }

    	  // Sort indices based on performance score (descending order)
    	  for (int i = 0; i < numOfAlternatives - 1; i++) {
    	    for (int j = i + 1; j < numOfAlternatives; j++) {
    	      if (performanceScore.get(indices.get(i)) < performanceScore.get(indices.get(j))) {
    	        int temp = indices.get(i);
    	        indices.set(i, indices.get(j));
    	        indices.set(j, temp);
    	      }
    	    }
    	  }

    	  // Assign ranks based on the sorted order
    	  for (int i = 0; i < numOfAlternatives; i++) {
    	    ranks.set(indices.get(i), i + 1);
    	  }
    	  System.out.println(ranks);
    	  System.out.println("\n^^^ rank calculated\n");
    	}
//
////    //LAMBDA FUNCTION IMPLEMENTATION 
////    private void calculateRanks() {
////        ranks = new ArrayList<>(Collections.nCopies(numOfAlternatives, 0));
////        List<Integer> indices = new ArrayList<>();
////        //stores indices of elements to track
////        for (int i = 0; i < numOfAlternatives; i++) {
////            indices.add(i);
////        }
////        // lambda expression a.k.a. (parameters) -> expression
////        indices.sort((a, b) -> Double.compare(performanceScore.get(b), performanceScore.get(a)));
////        // if performanceScore.get(b), performanceScore.get(a) = d1, d2
////        // Double.compare(performanceScore.get(b), performanceScore.get(a)) = d1 - d2
////        
////        // The use of performanceScore.get(b) as the first parameter and performanceScore.get(a) as the second parameter
////        // ensures descending order because a higher value of performanceScore.get(b) results in a positive comparison result,
////        // thus placing b before a.
////        
////        // if (a,b) and a smaller (a,b)
////        // if (a,b) and a bigger (b,a) 
////        // basically makes it ascending but since we compare (2,1) instead of (1,2) its reversed
////        
////        for (int i = 0; i < numOfAlternatives; i++) {
////            ranks.set(indices.get(i), i + 1);
////        }
////    System.out.println("score");
////    }

    public List<Double> getPerformanceScore() {
        return performanceScore;
    }

    public List<Integer> getRanks() {
        return ranks;
    }

}
