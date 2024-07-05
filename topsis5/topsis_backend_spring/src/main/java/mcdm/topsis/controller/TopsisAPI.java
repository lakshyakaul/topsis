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


// 3RD CODE : DIDNT EVEN TEST

//@RestController
//@RequestMapping("/api/topsis")
//public class TopsisAPI {
//
//    private final TopsisService topsisService;
//
//    public TopsisAPI(TopsisService topsisService) {
//        this.topsisService = topsisService;
//    }
//
//    @PostMapping("/calculate")
//    public ResponseEntity<Object> calculateTopsis(@RequestBody CalculationRequest request) {
//        try {
//            // Extract data from the request object
//            List<String> alternatives = request.getAlternatives();
//            List<String> criteria = request.getCriteria();
//            List<List<Double>> criteriaValues = request.getCriteriaValues();
//            List<Double> weights = request.getWeights();
//
//            // Validate or process the data as needed
//            // Example validation:
//            if (alternatives.isEmpty() || criteria.isEmpty() || criteriaValues.isEmpty() || weights.isEmpty()) {
//                return ResponseEntity.badRequest().body("Invalid input data.");
//            }
//
//            // Call TOPSIS service to perform calculation
//            TopsisResult result = topsisService.calculateTopsis(alternatives, criteria, criteriaValues, weights);
//
//            // Return the result
//            return ResponseEntity.ok(result); // Assuming TopsisResult has appropriate fields
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing request.");
//        }
//    }
//}


// FIRST CODE : NOT WORKING

//@RestController
//public class TopsisAPI {
//
////	/calculate is not a URL in this context. It's the endpoint within your own application
////	When a POST request is sent to http://your_server_address/calculate,
////	the sendData method will be triggered to process the request.
//	
//	private String[] alternatives;  // Member variable to store alternatives
//    private String[] criteria;       // Member variable to store criteria
//    private double[][] criteriaValues; // Member variable to store decision matrix
//    private double[] weights;        // Member variable to store weights
//	
//    @PostMapping("/calculate")
//    public String sendData() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        // alternatives stores the URL not the data
//        // doubleArray stores the data that was sent
//        String alternativesURL = "http://localhost:8080/alternatives";
//        String[] alternatives = restTemplate.getForObject(alternativesURL, String[].class);
//
//        String criteriaURL = "http://localhost:8080/criteria";
//        String[] criteria = restTemplate.getForObject(criteriaURL, String[].class);
//
//        String criteriaValuesURL = "http://localhost:8080/criteriaValues";
//        double[][] criteriaValues = restTemplate.getForObject(criteriaValuesURL, double[][].class);
//        
//        String weightsURL = "http://api.example.com/weights";
//        double[] weights = restTemplate.getForObject(weightsURL, double[].class);
//        
//// alternate way to write above 8 lines :
////	alternatives = restTemplate.getForObject("http://localhost:8080/alternatives", String[].class);
////	criteria = restTemplate.getForObject("http://localhost:8080/criteria", String[].class);
////	criteriaValues = restTemplate.getForObject("http://localhost:8080/criteriaValues", double[][].class);
////	weights = restTemplate.getForObject("http://api.example.com/weights", double[].class);
//
//        // Process and return the fetched data
//        StringBuilder result = new StringBuilder();
//        result.append("Alternatives: ").append(Arrays.toString(alternatives)).append("\n");
//        result.append("Criteria: ").append(Arrays.toString(criteria)).append("\n");
//        result.append("Decision matrix: ").append(Arrays.deepToString(criteriaValues)).append("\n");
//        result.append("Weights array: ").append(Arrays.toString(weights)).append("\n");
//
//        return "Data sent successfully.";
//    }
//    
//    @GetMapping("/results")
//    public String fetchResults() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String rankURL = "http://localhost:8080/rank";
//        double[] rank = restTemplate.getForObject(rankURL, double[].class);
//
//        // return the results
//        StringBuilder result = new StringBuilder();
//        result.append("Double Array: ").append(Arrays.toString(rank)).append("\n");
//
//        return result.toString();
//    }
//
//	public String[] getAlternatives() {
//		return alternatives;
//	}
//
//	public void setAlternatives(String[] alternatives) {
//		this.alternatives = alternatives;
//	}
//
//	public String[] getCriteria() {
//		return criteria;
//	}
//
//	public void setCriteria(String[] criteria) {
//		this.criteria = criteria;
//	}
//
//	public double[][] getCriteriaValues() {
//		return criteriaValues;
//	}
//
//	public void setCriteriaValues(double[][] criteriaValues) {
//		this.criteriaValues = criteriaValues;
//	}
//
//	public double[] getWeights() {
//		return weights;
//	}
//
//	public void setWeights(double[] weights) {
//		this.weights = weights;
//	}
//    
//    
//}

// WORKING : 2ND CODE

//@RestController
//public class TopsisAPI {
//
//    private List<String> alternatives; // Member variable to store alternatives
//    private List<String> criteria; // Member variable to store criteria
//    private List<List<Double>> criteriaValues; // Member variable to store decision matrix
//    private List<Double> weights; // Member variable to store weights
//
//    @PostMapping("/calculate")
//    public String sendData() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String alternativesURL = "http://localhost:8080/alternatives";
//        alternatives = Arrays.asList(restTemplate.getForObject(alternativesURL, String[].class));
//
//        String criteriaURL = "http://localhost:8080/criteria";
//        criteria = Arrays.asList(restTemplate.getForObject(criteriaURL, String[].class));
//
//        String criteriaValuesURL = "http://localhost:8080/criteriaValues";
//        double[][] criteriaValuesArray = restTemplate.getForObject(criteriaValuesURL, double[][].class);
//        criteriaValues = new ArrayList<>();
//        for (double[] row : criteriaValuesArray) {
//            criteriaValues.add(Arrays.stream(row).boxed().toList());
//        }
//
//        String weightsURL = "http://localhost:8080/weights";
//        weights = Arrays.stream(restTemplate.getForObject(weightsURL, double[].class)).boxed().toList();
//
//        // Process and return the fetched data
//        StringBuilder result = new StringBuilder();
//        result.append("Alternatives: ").append(alternatives).append("\n");
//        result.append("Criteria: ").append(criteria).append("\n");
//        result.append("Decision matrix: ").append(criteriaValues).append("\n");
//        result.append("Weights array: ").append(weights).append("\n");
//
//        return "Data sent successfully.";
//    }
//
//    @GetMapping("/results")
//    public String fetchResults() {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String rankURL = "http://localhost:8080/rank";
//        List<Double> rank = Arrays.stream(restTemplate.getForObject(rankURL, double[].class)).boxed().toList();
//
//        // return the results
//        return "Double List: " + rank;
//    }
//
//    public List<String> getAlternatives() {
//        return alternatives;
//    }
//
//    public void setAlternatives(List<String> alternatives) {
//        this.alternatives = alternatives;
//    }
//
//    public List<String> getCriteria() {
//        return criteria;
//    }
//
//    public void setCriteria(List<String> criteria) {
//        this.criteria = criteria;
//    }
//
//    public List<List<Double>> getCriteriaValues() {
//        return criteriaValues;
//    }
//
//    public void setCriteriaValues(List<List<Double>> criteriaValues) {
//        this.criteriaValues = criteriaValues;
//    }
//
//    public List<Double> getWeights() {
//        return weights;
//    }
//
//    public void setWeights(List<Double> weights) {
//        this.weights = weights;
//    }
//}
