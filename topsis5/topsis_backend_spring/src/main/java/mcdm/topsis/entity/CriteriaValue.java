package mcdm.topsis.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "Values")
public class CriteriaValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "alternative_id", nullable = false)
    private Alternative alternative;

    @ManyToOne
    @JoinColumn(name = "criteria_id", nullable = false)
    private Criteria criteria;

    @Column(name = "value", nullable = false)
    private Double value;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Alternative getAlternative() {
        return alternative;
    }

    public void setAlternative(Alternative alternative) {
        this.alternative = alternative;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public void setCriteria(Criteria criteria) {
        this.criteria = criteria;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
