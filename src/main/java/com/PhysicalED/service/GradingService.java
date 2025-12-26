package com.PhysicalED.service;

import com.PhysicalED.model.Gender;
import com.PhysicalED.model.GradingScale;
import com.PhysicalED.model.PhysicalTest;
import com.PhysicalED.repo.GradingScaleRepository;

import jakarta.persistence.*;
import java.util.List;

public class GradingService {

    private final GradingScaleRepository gradingScaleRepository;

    public GradingService(GradingScaleRepository gradingScaleRepository) {
        this.gradingScaleRepository = gradingScaleRepository;
    }

    /**
     * Calcola e restituisce il voto sulla base delle fasce salvate per il test, sesso e punteggio.
     */
    public int calcolaVoto(PhysicalTest physicalTest, Gender gender, double valore, EntityManager em) {
        List<GradingScale> fasce = gradingScaleRepository.findByPhysicalTestAndGender(physicalTest, gender, em);
        if (fasce.isEmpty()) {
            throw new IllegalStateException("⚠️ Nessuna fascia di valutazione per questo test/genere.");
        }
        for (GradingScale gs : fasce) {
            if (valore >= gs.getMinValue() && valore < gs.getMaxValue()) {
                return gs.getVoto();
            }
        }
        throw new IllegalStateException("‼️ Il valore inserito non rientra in nessuna fascia di valutazione!");
    }
}
