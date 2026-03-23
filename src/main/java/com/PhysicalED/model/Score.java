package com.PhysicalED.model;
import jakarta.persistence.*;

/**
 * Score entity representing a student's score in a specific specialty.
 */
@Entity
@Table(name = "score") // Table name in the db

public class Score {
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "speciality_id", nullable = false)
    private SpecialtyEntity specialty;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private Integer voto;

    // Constructor
    public Score() {}
    public Score(SpecialtyEntity specialty, Student student, Double value, Integer voto) {
        this.specialty = specialty;
        this.student = student;
        this.value = value;
        this.voto = voto;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public SpecialtyEntity getSpecialty() {
        return specialty;
    }
    public void setSpecialty(SpecialtyEntity specialty) {
        this.specialty = specialty;
    }

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    public Double getValue() {
        return value;
    }
    public void setValue(Double value) {
        this.value = value;
    }

    public Integer getVoto() {
        return voto;
    }
    public void setVoto(Integer voto) {
        this.voto = voto;
    }
}
