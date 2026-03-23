package com.PhysicalED.service;

import com.PhysicalED.model.Gender;
import com.PhysicalED.model.GradingScale;
import com.PhysicalED.repo.GradingScaleRepository;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GradingServiceTest {

    @Test
    public void calcolaVotoDaFasce_valoreNelRange_restituisceVoto() {
        // repo non usato in questo test
        GradingService service = new GradingService((GradingScaleRepository) null);

        GradingScale f1 = new GradingScale();
        f1.setGender(Gender.M);
        f1.setMinValue(0.0);
        f1.setMaxValue(10.0);
        f1.setVoto(6);

        GradingScale f2 = new GradingScale();
        f2.setGender(Gender.M);
        f2.setMinValue(10.0);
        f2.setMaxValue(20.0);
        f2.setVoto(8);

        List<GradingScale> fasce = Arrays.asList(f1, f2);

        assertEquals(6, service.calcolaVotoDaFasce(fasce, 5.0));
        assertEquals(8, service.calcolaVotoDaFasce(fasce, 10.0));
    }

    @Test(expected = IllegalStateException.class)
    public void calcolaVotoDaFasce_fasceVuote_lanciaEccezione() {
        GradingService service = new GradingService(null);
        service.calcolaVotoDaFasce(Collections.emptyList(), 5.0);
    }

    @Test(expected = IllegalStateException.class)
    public void calcolaVotoDaFasce_valoreFuoriRange_lanciaEccezione() {
        GradingService service = new GradingService(null);

        GradingScale f1 = new GradingScale();
        f1.setGender(Gender.F);
        f1.setMinValue(0.0);
        f1.setMaxValue(10.0);
        f1.setVoto(6);

        service.calcolaVotoDaFasce(List.of(f1), 10.0); // max esclusivo
    }
}
