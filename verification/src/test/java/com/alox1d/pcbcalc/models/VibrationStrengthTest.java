package com.alox1d.pcbcalc.models;
import org.junit.Test;
import static org.junit.Assert.*;

public class VibrationStrengthTest {
    @Test public void convertsPascalsToMegapascalsExactlyOnce() {
        // 1 N / (1 mm)^2 with factor 6 = 6,000,000 Pa = 6 MPa.
        assertEquals(6.0, VibrationStrength.bendingStressMPa(1, 0.001), 1e-10);
    }
    @Test public void doublingThicknessQuartersStress() {
        assertEquals(1.5, VibrationStrength.bendingStressMPa(1, 0.002), 1e-10);
    }
    @Test public void rejectsLoadAboveFiberglassAllowableStress() {
        double stress = VibrationStrength.bendingStressMPa(10, 0.001);
        assertEquals(60, stress, 1e-10);
        assertFalse(VibrationStrength.isBelowAllowable(stress, 105.0 / 2));
    }
    @Test public void acceptsLoadBelowAllowableStress() {
        assertTrue(VibrationStrength.isBelowAllowable(50, 52.5));
    }
    @Test public void equalityDoesNotMeetStrictReserveCriterion() {
        assertFalse(VibrationStrength.isBelowAllowable(52.5, 52.5));
    }
    @Test public void nonFiniteStressDoesNotPass() {
        assertFalse(VibrationStrength.isBelowAllowable(Double.NaN, 52.5));
        assertFalse(VibrationStrength.isBelowAllowable(Double.POSITIVE_INFINITY, 52.5));
    }
}
