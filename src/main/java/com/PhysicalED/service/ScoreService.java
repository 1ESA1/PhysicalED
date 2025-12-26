package com.PhysicalED.service;

import com.PhysicalED.model.*;
import com.PhysicalED.repo.*;

import jakarta.persistence.*;

public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final GradingService gradingService;

    public ScoreService(ScoreRepository scoreRepository, GradingService gradingService) {
        this.scoreRepository = scoreRepository;
        this.gradingService = gradingService;
    }

    public Score aggiungiScore(Student student, PhysicalTest test, double valore, EntityManager em) {
        int voto = gradingService.calcolaVoto(test, student.getGender(), valore, em);
        Score score = new Score(test, student, valore, voto);
        scoreRepository.save(score);
        return score;
    }
}
