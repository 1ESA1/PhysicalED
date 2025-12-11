package com.PhysicalED.model;
import jakarta.persistence.*;
/**
 * ClassSection entity representing a class section in the school system.
 */
@Entity
@Table(name = "class_section") // Table name in the db

public class ClassSection {
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false, length = 20)
    private String name;

    @ManyToOne
    @JoinColumn(name = "school_year_id", nullable = false)
    private SchoolYear schoolYear;

    // Constructor
    public ClassSection() {}
    public ClassSection(String name, SchoolYear schoolYear) {
        this.name = name;
        this.schoolYear = schoolYear;
    }
    // Getters and Setters
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

    public SchoolYear getSchoolYear() {
        return schoolYear;
    }
    public void setSchoolYear(SchoolYear schoolYear) {
        this.schoolYear = schoolYear;
    }
}
