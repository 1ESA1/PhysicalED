package com.PhysicalED.model;
import jakarta.persistence.*;
/**
 * Entity representing a Test Discipline in the Physical Education system.
 * It includes details about the sport category, class section,
 * test date, and description.
 */
@Entity
@Table(name = "test_discipline")

public class PhysicalTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sport_category_id", nullable = false)
    private SportCategory sportCategory;

    @ManyToOne
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;

    @Column(name = "test_date")
    private java.sql.Date testDate;

    @Column(nullable = false, length = 100)
    private String description;

    // Contructor
    public PhysicalTest() {}
    public PhysicalTest(SportCategory sportCategory,
                        ClassSection classSection,
                        java.sql.Date testDate,
                        String description) {
        this.sportCategory = sportCategory;
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

    public SportCategory getSportCategory() {
        return sportCategory;
    }
    public void setSportCategory(SportCategory sportCategory) {
        this.sportCategory = sportCategory;
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
