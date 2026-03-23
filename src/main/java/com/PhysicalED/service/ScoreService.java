package com.PhysicalED.service;

import com.PhysicalED.model.Score;
import com.PhysicalED.model.SpecialtyEntity;
import com.PhysicalED.model.Student;
import com.PhysicalED.repo.ScoreRepository;

public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final GradingService gradingService;

    public ScoreService(ScoreRepository scoreRepository, GradingService gradingService) {
        this.scoreRepository = scoreRepository;
        this.gradingService = gradingService;
    }

    public Score aggiungiScore(Student student, SpecialtyEntity specialty, double valore) {
        int voto = gradingService.calcolaVoto(specialty, student.getGender(), valore);
        Score score = new Score(specialty, student, valore, voto);
        scoreRepository.save(score);
        return score;
    }

    /**
     * Media voti dello studente su tutte le specialità disponibili (basata sui record Score).
     *
     * @return media, oppure null se lo studente non ha ancora score.
     */
    public Double mediaVotiStudente(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Studente non valido.");
        }
        return scoreRepository.averageVotoByStudentId(student.getId());
    }

    public long numeroSpecialtyValutate(Student student) {
        if (student == null || student.getId() == null) {
            throw new IllegalArgumentException("Studente non valido.");
        }
        Long c = scoreRepository.countByStudentId(student.getId());
        return c == null ? 0L : c;
    }
}
