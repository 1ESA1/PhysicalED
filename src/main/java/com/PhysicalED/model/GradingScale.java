package com.PhysicalED.model;
/*
 * Entity representing the grading scale for physical tests based
 */
import jakarta.persistence.*;

@Entity
@Table(name = "grading_scale")
public class GradingScale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "physical_test_id")
    private PhysicalTest physicalTest;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 1, nullable = false)
    private Gender gender;

    @Column(name = "min_value", nullable = false)
    private Double minValue;

    @Column(name = "max_value", nullable = false)
    private Double maxValue;

    @Column(name = "voto", nullable = false)
    private Integer voto;

    public GradingScale() {}

    public GradingScale(PhysicalTest physicalTest, Gender gender, Double minValue, Double maxValue, Integer voto) {
        this.physicalTest = physicalTest;
        this.gender = gender;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.voto = voto;
    }

    // Getters & Setters...
    public Long getId() { return id; }
    public PhysicalTest getPhysicalTest() { return physicalTest; }
    public void setPhysicalTest(PhysicalTest physicalTest) { this.physicalTest = physicalTest; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public Double getMinValue() { return minValue; }
    public void setMinValue(Double minValue) { this.minValue = minValue; }
    public Double getMaxValue() { return maxValue; }
    public void setMaxValue(Double maxValue) { this.maxValue = maxValue; }
    public Integer getVoto() { return voto; }
    public void setVoto(Integer voto) { this.voto = voto; }
}
