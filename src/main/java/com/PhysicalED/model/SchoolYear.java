package com.PhysicalED.model;
import jakarta.persistence.*;
/**
 * Entity class representing a School Year.
 */
@Entity
@Table(name = "school_year") // Table name in the database

public class SchoolYear {
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String description;

    // Constructor
    public SchoolYear() {}
    public SchoolYear(String description) {
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
