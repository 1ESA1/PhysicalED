package com.PhysicalED.model;
import jakarta.persistence.*;
/**
 * Student Entity representing a student in the Physical Education system.
 */
@Entity
@Table(name = "student") // Table name in the db

public class Student {
    @Id // Primary Key
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "firstname", length = 30, nullable = false)
    private String firstName;

    @Column (name = "lastname",length = 30, nullable = false)
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "class_section_id", nullable = false)
    private ClassSection classSection;

    //Constructor
    public Student() {}
    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Student(String firstName, String lastName, ClassSection classSection) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.classSection = classSection;
    }

    //Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public ClassSection getClassSection() {
        return classSection;
    }
    public void setClassSection(ClassSection classSection) {
        this.classSection = classSection;
    }
}
