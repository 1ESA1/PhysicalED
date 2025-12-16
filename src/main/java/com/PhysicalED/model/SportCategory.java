package com.PhysicalED.model;
import jakarta.persistence.*;
/**
 * Entity representing a sport category.
 */
@Entity
@Table(name = "sport_category")

public class SportCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String description;

    // Constructor
    public SportCategory() {}
    public SportCategory(String description) {
        this.description = description;
    }

    // Getter and Setter
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
