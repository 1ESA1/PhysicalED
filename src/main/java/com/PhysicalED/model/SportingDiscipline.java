package com.PhysicalED.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sporting_discipline")

public class SportingDiscipline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String description;

    // Constructor
    public SportingDiscipline() {}
    public SportingDiscipline(Long id, String description) {
        this.id = id;
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
