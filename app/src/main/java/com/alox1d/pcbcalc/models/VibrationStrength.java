package com.alox1d.pcbcalc.models;

/** Unit conversions and the strict bending-strength criterion used by PCBCalculator. */
public final class VibrationStrength {
    private VibrationStrength() {}

    /** Plate bending moment per unit width (N) and thickness (m) produce stress in MPa. */
    public static double bendingStressMPa(double momentPerWidth, double thicknessMeters) {
        return 6 * momentPerWidth / (thicknessMeters * thicknessMeters * 1_000_000);
    }

    public static boolean isBelowAllowable(double stressMPa, double allowableMPa) {
        return stressMPa >= 0 && stressMPa < allowableMPa;
    }
}
