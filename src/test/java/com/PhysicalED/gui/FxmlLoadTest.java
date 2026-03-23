package com.PhysicalED.gui;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Smoke test: verifica che le risorse FXML siano presenti nel classpath.
 */
public class FxmlLoadTest {

    @Test
    public void schoolYearFxmlPresenteNelClasspath() {
        assertNotNull(ViewLoader.class.getResource("/com/PhysicalED/gui/SchoolYearView.fxml"));
    }

    @Test
    public void scoreFxmlPresenteNelClasspath() {
        assertNotNull(ViewLoader.class.getResource("/com/PhysicalED/gui/ScoreView.fxml"));
    }

    @Test
    public void specialtyFxmlPresenteNelClasspath() {
        assertNotNull(ViewLoader.class.getResource("/com/PhysicalED/gui/SpecialtyView.fxml"));
    }
}
