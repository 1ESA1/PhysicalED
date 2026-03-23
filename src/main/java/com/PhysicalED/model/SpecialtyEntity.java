package com.PhysicalED.model;

import jakarta.persistence.*;

/**
 * Entity che rappresenta una Speciality (UI: "Specialità").
 */
@Entity
@Table(name = "speciality")
public class SpecialtyEntity {
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

    public SpecialtyEntity() {
    }

    public SpecialtyEntity(SportCategory sportCategory,
                           ClassSection classSection,
                           java.sql.Date testDate,
                           String description) {
        this.sportCategory = sportCategory;
        this.classSection = classSection;
        this.testDate = testDate;
        this.description = description;
    }

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

    @Override
    public String toString() {
        String cls = classSection != null ? classSection.getName() : "";
        return cls.isBlank() ? description : description + " (" + cls + ")";
    }
}
