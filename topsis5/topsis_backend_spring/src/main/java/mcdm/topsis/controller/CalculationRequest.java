package mcdm.topsis.controller;

import java.util.List;

public class CalculationRequest {

    private List<String> alternatives;
    private List<String> criteria;
    private List<List<Double>> criteriaValues;
    private List<Double> weights;
    private List<Boolean> benefitCriteria;

    // Constructors, getters, and setters

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public List<String> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<String> criteria) {
        this.criteria = criteria;
    }

    public List<List<Double>> getCriteriaValues() {
        return criteriaValues;
    }

    public void setCriteriaValues(List<List<Double>> criteriaValues) {
        this.criteriaValues = criteriaValues;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
    }

	public List<Boolean> getBenefitCriteria() {
		return benefitCriteria;
	}

	public void setBenefitCriteria(List<Boolean> benefitCriteria) {
		this.benefitCriteria = benefitCriteria;
	}
	
}
