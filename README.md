# TOPSIS

Implementation of
Technique for Order Preference by Similarity to Ideal Solution (TOPSIS)
for Multiple-Criteria Decision Making (MCDM)

### versions used :

postgres 16 <br>
spring tool suite 4 <br>
spring boot 3.3.1 <br>
angular 15 <br>
java 21 <br>
npm 10.8.1 <br>
node.js 18 (latest compatible with angular15)




temp:

The provided code looks generally correct in implementing the TOPSIS method. However, there are a few areas that could be optimized or clarified:

1. **Initialization and Constructor:**
   - The constructor does not initialize `criteria` and `alternatives`. Since they are passed as arguments, it should initialize these variables.

2. **Validation:**
   - The `validateData` method checks for an empty `criteria` list, but it doesn't check if `criteriaValues` has the correct dimensions.

3. **Performance and Memory:**
   - Using `ArrayList` for all data structures may lead to higher memory usage. Consider using more efficient data structures if performance becomes an issue.

4. **Code Readability:**
   - The code could benefit from comments explaining the logic, especially for complex calculations.

5. **Division by Zero:**
   - There should be a check to ensure that the divisor is not zero before normalization to avoid division by zero errors.

Here are some suggested changes to address these points:

### Improved Code

```java
package mcdm.topsis.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class TopsisService {

    private List<Double> criteria;
    private List<Double> alternatives;
    private List<Double> weights;
    private int numOfCriteria;
    private int numOfAlternatives;
    private List<List<Double>> criteriaValues;
    private List<Double> divisor;
    private List<List<Double>> normalizedDecisionMatrix;
    private List<List<Double>> weightedNormalizedDecisionMatrix;
    private List<Double> idealBest;
    private List<Double> idealWorst;
    private List<Double> euclidianDistFromIdealBest;
    private List<Double> euclidianDistFromIdealWorst;
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
        if (Math.abs(weightSum - 1.0) >= 1e-10) return false;
        if (alternatives.isEmpty() || criteria.isEmpty()) return false;
        if (criteriaValues.size() != numOfAlternatives) return false;
        for (List<Double> row : criteriaValues) {
            if (row.size() != numOfCriteria) return false;
        }
        return true;
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
                if (divisor.get(j) != 0) {
                    row.add(criteriaValues.get(i).get(j) / divisor.get(j));
                } else {
                    row.add(0.0);
                }
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
```

### Changes Made:
1. **Constructor Initialization:** Added initialization of `criteria` and `alternatives`.
2. **Validation Method:** Improved to check the dimensions of `criteriaValues`.
3. **Division by Zero:** Added a check in `calculateNormalizedMatrix` to avoid division by zero.
4. **Code Comments:** It's beneficial to add comments to explain the logic further for better readability (though not shown here to keep the code clean).

With these changes, the code should be more robust and easier to understand and maintain.
