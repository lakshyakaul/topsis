package mcdm.topsis.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import mcdm.topsis.service.TopsisService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class TopsisAPI {

    private final TopsisService topsisService;

    public TopsisAPI(TopsisService topsisService) {
        this.topsisService = topsisService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Object> calculateTopsis(@RequestBody CalculationRequest request) {
        try {
            // Extract data from the request object
            List<Double> weights = request.getWeights();
            List<String> criteria = request.getCriteria();
            List<String> alternatives = request.getAlternatives();
            List<List<Double>> criteriaValues = request.getCriteriaValues();
            List<Boolean> benefitCriteria = request.getBenefitCriteria();

            // Create TOPSIS service instance and perform calculation
            TopsisService topsis = new TopsisService(weights, criteria, alternatives, criteriaValues, benefitCriteria);

            // Get the calculated ranks
            List<Integer> ranks = topsis.getRanks();

            // Return the ranks as response
            return ResponseEntity.ok(ranks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request.");
        }
    }
}
