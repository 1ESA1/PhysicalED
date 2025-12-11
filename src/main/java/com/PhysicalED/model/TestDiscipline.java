package com.PhysicalED.model;
import jakarta.persistence.*;
/**
 * Entity representing a Test Discipline in the Physical Education system.
 * It includes details about the sporting discipline, class section,
 * test date, and description.
 */
@Entity
@Table(name = "test_discipline")

public class TestDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sporting_discipline_id", nullable = false)
    private SportingDiscipline sportingDiscipline;

    @ManyToOne
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;

    @Column(name = "test_date")
    private java.sql.Date testDate;

    @Column(nullable = false, length = 100)
    private String description;

    // Contructor
    public TestDiscipline() {}
    public TestDiscipline(SportingDiscipline sportingDiscipline,
                          ClassSection classSection,
                          java.sql.Date testDate,
                          String description) {
        this.sportingDiscipline = sportingDiscipline;
        this.classSection = classSection;
        this.testDate = testDate;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public SportingDiscipline getSportingDiscipline() {
        return sportingDiscipline;
    }
    public void setSportingDiscipline(SportingDiscipline sportingDiscipline) {
        this.sportingDiscipline = sportingDiscipline;
    }

    public ClassSection getClassSection() {
        return classSection;
    }
    public void setClassSection(ClassSection classSection) {
        this.classSection = classSection;
    }

    public java.sql.Date getTestDate() {
        return testDate;
    }
    public void setTestDate(java.sql.Date testDate) {
        this.testDate = testDate;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
