package com.PhysicalED.model;
import jakarta.persistence.*;

/**
 * Score entity representing a student's score in a specific test discipline.
 */
@Entity
@Table(name = "score") // Table name in the db

public class Score {
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_discipline_id", nullable = false)
    private TestDiscipline testDiscipline;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Double value;

    // Constructor
    public Score() {}
    public Score(TestDiscipline testDiscipline, Student student, Double value) {
        this.testDiscipline = testDiscipline;
        this.student = student;
        this.value = value;
    }

    // Getter and Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public TestDiscipline getTestDiscipline() {
        return testDiscipline;
    }
    public void setTestDiscipline(TestDiscipline testDiscipline) {
        this.testDiscipline = testDiscipline;
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
}
