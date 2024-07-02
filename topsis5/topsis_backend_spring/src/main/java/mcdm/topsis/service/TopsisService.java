package mcdm.topsis.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class TopsisService {

	private List<Double> criteria;
    private List<Double> alternatives;
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

    public TopsisService(List<Double> weights, List<Double> criteria, List<Double> alternatives, List<List<Double>> criteriaValues, List<Boolean> benefitCriteria) {
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
        double weightSum = weights.stream().mapToDouble(Double::doubleValue).sum();
        return Math.abs(weightSum - 1.0) < 1e-10 && !alternatives.isEmpty() && !criteria.isEmpty();
    }

    private void calculateNormalizedMatrix() {
        divisor = new ArrayList<>(Collections.nCopies(numOfCriteria, 0.0));
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
    }

    private void calculateWeightedNormalizedMatrix() {
        weightedNormalizedDecisionMatrix = new ArrayList<>();
        for (int i = 0; i < numOfAlternatives; i++) {
            List<Double> row = new ArrayList<>();
            for (int j = 0; j < numOfCriteria; j++) {
                row.add(normalizedDecisionMatrix.get(i).get(j) * weights.get(j));
            }
            weightedNormalizedDecisionMatrix.add(row);
        }
    }

    private void calculateIdealValues(List<Boolean> benefitCriteria) {
        idealBest = new ArrayList<>();
        idealWorst = new ArrayList<>();

        for (int j = 0; j < numOfCriteria; j++) {
            List<Double> column = new ArrayList<>();
            for (int i = 0; i < numOfAlternatives; i++) {
                column.add(weightedNormalizedDecisionMatrix.get(i).get(j));
            }
            
            if (benefitCriteria.get(j)) {
                idealBest.add(Collections.max(column));
                idealWorst.add(Collections.min(column));
            } else {
                idealBest.add(Collections.min(column));
                idealWorst.add(Collections.max(column));
            }
        }
    }

    private void calculateEuclidianDistances() {
        euclidianDistFromIdealBest = new ArrayList<>();
        euclidianDistFromIdealWorst = new ArrayList<>();

        for (int i = 0; i < numOfAlternatives; i++) {
            double distBest = 0;
            double distWorst = 0;
            for (int j = 0; j < numOfCriteria; j++) {
                distBest += Math.pow(weightedNormalizedDecisionMatrix.get(i).get(j) - idealBest.get(j), 2);
                distWorst += Math.pow(weightedNormalizedDecisionMatrix.get(i).get(j) - idealWorst.get(j), 2);
            }
            euclidianDistFromIdealBest.add(Math.sqrt(distBest));
            euclidianDistFromIdealWorst.add(Math.sqrt(distWorst));
        }
    }

    private void calculatePerformanceScore() {
        performanceScore = new ArrayList<>();
        for (int i = 0; i < numOfAlternatives; i++) {
            double score = euclidianDistFromIdealWorst.get(i) / (euclidianDistFromIdealBest.get(i) + euclidianDistFromIdealWorst.get(i));
            performanceScore.add(score);
        }
    }

    private void calculateRanks() {
        ranks = new ArrayList<>(Collections.nCopies(numOfAlternatives, 0));
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < numOfAlternatives; i++) {
            indices.add(i);
        }
        indices.sort((a, b) -> Double.compare(performanceScore.get(b), performanceScore.get(a)));
        for (int i = 0; i < numOfAlternatives; i++) {
            ranks.set(indices.get(i), i + 1);
        }
    }

    public List<Double> getPerformanceScore() {
        return performanceScore;
    }

    public List<Integer> getRanks() {
        return ranks;
    }

}
