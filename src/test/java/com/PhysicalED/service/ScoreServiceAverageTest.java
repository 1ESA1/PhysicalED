package com.PhysicalED.service;

import com.PhysicalED.model.Student;
import com.PhysicalED.repo.ScoreRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test unitario puro: verifica la media usando un repository stub.
 * Non richiede DB.
 */
public class ScoreServiceAverageTest {

    private static class StubScoreRepository extends ScoreRepository {
        private final Double avg;
        private final Long count;

        StubScoreRepository(Double avg, Long count) {
            // em non usato nello stub
            super(null);
            this.avg = avg;
            this.count = count;
        }

        @Override
        public Double averageVotoByStudentId(Long studentId) {
            return avg;
        }

        @Override
        public Long countByStudentId(Long studentId) {
            return count;
        }
    }

    @Test
    public void mediaVotiStudente_quandoNonCiSonoScore_restituisceNull_e_countZero() {
        ScoreService service = new ScoreService(new StubScoreRepository(null, 0L), null);

        Student s = new Student();
        s.setId(1L);

        assertEquals(null, service.mediaVotiStudente(s));
        assertEquals(0L, service.numeroTestValutati(s));
    }

    @Test
    public void mediaVotiStudente_quandoCiSonoScore_restituisceMedia_e_count() {
        ScoreService service = new ScoreService(new StubScoreRepository(7.5, 4L), null);

        Student s = new Student();
        s.setId(1L);

        assertEquals(Double.valueOf(7.5), service.mediaVotiStudente(s));
        assertEquals(4L, service.numeroTestValutati(s));
    }
}

