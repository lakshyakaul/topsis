package mcdm.topsis.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Criteria")
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "benefit", nullable = false)
    private boolean benefit;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBenefit() {
        return benefit;
    }

    public void setBenefit(boolean benefit) {
        this.benefit = benefit;
    }
}