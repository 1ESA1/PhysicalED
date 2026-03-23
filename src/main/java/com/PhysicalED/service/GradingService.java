package com.PhysicalED.service;

import com.PhysicalED.model.Gender;
import com.PhysicalED.model.GradingScale;
import com.PhysicalED.model.SpecialtyEntity;
import com.PhysicalED.repo.GradingScaleRepository;

import java.util.List;

public class GradingService {

    private final GradingScaleRepository gradingScaleRepository;

    public GradingService(GradingScaleRepository gradingScaleRepository) {
        this.gradingScaleRepository = gradingScaleRepository;
    }

    /**
     * Calcola e restituisce il voto sulla base delle fasce salvate per la specialità, sesso e punteggio.
     */
    public int calcolaVoto(SpecialtyEntity specialty, Gender gender, double valore) {
        List<GradingScale> fasce = gradingScaleRepository.findBySpecialtyAndGender(specialty, gender);
        return calcolaVotoDaFasce(fasce, valore);
    }

    /**
     * Variante pura (senza DB) per riuso e test unitari.
     */
    int calcolaVotoDaFasce(List<GradingScale> fasce, double valore) {
        if (fasce == null || fasce.isEmpty()) {
            throw new IllegalStateException("Nessuna fascia di valutazione disponibile.");
        }
        for (GradingScale gs : fasce) {
            if (valore >= gs.getMinValue() && valore < gs.getMaxValue()) {
                return gs.getVoto();
            }
        }
        throw new IllegalStateException("Il valore inserito non rientra in nessuna fascia di valutazione.");
    }
}
