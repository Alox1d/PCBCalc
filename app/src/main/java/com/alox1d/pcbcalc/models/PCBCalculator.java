package com.alox1d.pcbcalc.models;

import android.widget.Spinner;

import com.alox1d.pcbcalc.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alox1d on 10.04.2018.
 */

public class PCBCalculator {
    private final HashMap<String, HashMap<Integer, String>> inputExpandedRows;
    private final ArrayList<StringWithTag> dialogItemChooserItems;
    private final HashMap<String, HashMap<Integer, StringWithTag>> inputExpandedSpinnerRows;
    private ArrayList<String> outputNames;
    private HashMap<Integer, String> inputRowValuesAndNamesId;
    private HashMap<Integer, StringWithTag> inputSpinnerValuesAndNamesId;

    //    private boolean isVibrationDurable;
//    private boolean isVibrationStable;
//    private boolean isImpactResistance;
    private boolean successResult;
    private boolean error;
    private int resultTextId;
    private String resultText;

    private HashMap<Integer, Double> wordValues;
    private ArrayList<WordItem> wordStringValues = new ArrayList<>();
    private ArrayList<Double> outputValues;
    private ArrayList<ChartItem> chartItems;

    public PCBCalculator(HashMap<Integer, String> inputRowValuesAndNamesId, ArrayList<String> outputNames, HashMap<Integer, StringWithTag> inputSpinnerValuesAndNamesId, HashMap<String, HashMap<Integer, String>> inputExpandedRows, ArrayList<StringWithTag> dialogItemChooserItems, HashMap<String, HashMap<Integer, StringWithTag>> inputExpandedSpinnerRows) {
        this.inputRowValuesAndNamesId = inputRowValuesAndNamesId;
        this.outputNames = outputNames;
        this.inputSpinnerValuesAndNamesId = inputSpinnerValuesAndNamesId;
        this.inputExpandedRows = inputExpandedRows;
        this.dialogItemChooserItems = dialogItemChooserItems;
        this.inputExpandedSpinnerRows = inputExpandedSpinnerRows;
    }


    public void calculateVibrationDurable() {

        wordValues = new HashMap<>();
        outputValues = new ArrayList<>();

        double density = 0; // плотность материала
        double moduleElasticityOfMaterial = 0; // модуль упругости
        double moduleElasticityOfMaterialDividedByPow10_10 = 0; // модуль упругости
        double coeffPoisson = 0; // коэф. Пуассона
        StringWithTag materialStringWithTag = inputSpinnerValuesAndNamesId.get(R.string.material_PCB);
        int materialId = (int) materialStringWithTag.getTag();
        switch (materialId) {
            case R.string.material_fiberglass_stef_1:
                density = 2050;
                moduleElasticityOfMaterialDividedByPow10_10 = 3.02;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.22;
                break;
        }
        wordValues.put(R.string.mech_imp_var_density, density);
        wordValues.put(R.string.mech_imp_var_moduleElasticityOfMaterial, moduleElasticityOfMaterialDividedByPow10_10);
        wordValues.put(R.string.mech_imp_var_coeffPoisson, coeffPoisson);
        wordStringValues.add(new WordItem(R.string.mech_imp_var_material, materialStringWithTag.toString()));

        double length = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_length));
        double width = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_width));
        double thickness = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_thickness));
        wordValues.put(R.string.mech_imp_var_length, length);
        wordValues.put(R.string.mech_imp_var_width, width);
        wordValues.put(R.string.mech_imp_var_thickness, thickness);
        double massPCB = density * length * width * thickness;
        outputValues.add(massPCB);
        wordValues.put(R.string.mech_imp_var_massPCB, massPCB);

        double massofAllElements = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_mass_all_elements));
        wordValues.put(R.string.mech_imp_var_massofAllElements, massofAllElements);
        double coeffMassElements = 1 / Math.sqrt((massofAllElements / massPCB) + 1);
        outputValues.add(coeffMassElements);
        wordValues.put(R.string.mech_imp_var_coeffMassElements, coeffMassElements);

        double coeffFixingMethod = 0;
        double fixingFirstCoeff = 0;
        double fixingSecondCoeff = 0;
        double fixingThirdCoeff = 0;
        StringWithTag fixingMethodStringWithTag =  inputSpinnerValuesAndNamesId.get(R.string.fixing_method);
        int fixingMethod = (int) fixingMethodStringWithTag.getTag();
        switch (fixingMethod) {
            case R.string.vibration_fixing_method_1:
                fixingFirstCoeff = 9.87;
                fixingSecondCoeff = 2.33;
                fixingThirdCoeff = 2.44;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_2:
                fixingFirstCoeff = 9.87;
                fixingSecondCoeff = 2.57;
                fixingThirdCoeff = 5.14;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_3:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.48;
                fixingThirdCoeff = 0.19;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_4:
                fixingFirstCoeff = 15.42;
                fixingSecondCoeff = 1.11;
                fixingThirdCoeff = 1;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) +
                        fixingThirdCoeff * (Math.pow(length, 4) / Math.pow(width, 4))));
                break;
            case R.string.vibration_fixing_method_5:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.57;
                fixingThirdCoeff = 0.47;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_6:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.61;
                fixingThirdCoeff = 1;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) +
                        fixingThirdCoeff * (Math.pow(length, 4) / Math.pow(width, 4))));
                break;
            case R.string.vibration_fixing_method_7:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.14;
                fixingThirdCoeff = 0.02;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;

        }
        outputValues.add(coeffFixingMethod);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeffFixingMethodFirst, fixingFirstCoeff);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeffFixingMethodSecond, fixingSecondCoeff);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeffFixingMethodThird, fixingThirdCoeff);
        wordValues.put(R.string.mech_imp_var_coeffFixingMethod, coeffFixingMethod);
        wordStringValues.add(new WordItem(R.string.mech_imp_var_fixing_method,fixingMethodStringWithTag.toString()) );

        double rigidity = (moduleElasticityOfMaterial * Math.pow(thickness, 3)) / (12 * (1 - Math.pow(coeffPoisson, 2)));
        outputValues.add(rigidity);
        wordValues.put(R.string.mech_imp_var_rigidity, rigidity);

        final double accelerationOfGravity = 9.81;
        final double specificPCBWeight = 20500;
        double excitingFrequency = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_exciting_frequency));
        wordValues.put(R.string.mech_imp_var_excitingFrequency, excitingFrequency);
        double fundamentalFrequency = ((coeffMassElements * coeffFixingMethod) / (2 * Math.PI * Math.pow(length, 2))) *
                Math.sqrt((rigidity * accelerationOfGravity) / (specificPCBWeight * thickness));
        outputValues.add(fundamentalFrequency);
        wordValues.put(R.string.mech_imp_var_fundamentalFrequency, fundamentalFrequency);

        final double coeffOfOverload = 8; // const
        double amplitudeOfOscillation = (250 * coeffOfOverload) / Math.pow(fundamentalFrequency, 2);
        outputValues.add(formatAndCheckValue(amplitudeOfOscillation));
        wordValues.put(R.string.mech_imp_var_amplitudeOfOscillation, amplitudeOfOscillation);

        final double oscillationAttenuationIndex = 0.06; // для стеклотекстолита - const
        double coeffOfDynamic = 1 / Math.sqrt(Math.pow(1 - Math.pow(excitingFrequency / fundamentalFrequency, 2), 2) +
                (Math.pow(excitingFrequency / fundamentalFrequency, 2) * Math.pow(oscillationAttenuationIndex, 2)));
        outputValues.add(formatAndCheckValue(coeffOfDynamic));
        wordValues.put(R.string.mech_imp_var_coeffOfDynamic, coeffOfDynamic);


        double dynamicDeflection = coeffOfDynamic * amplitudeOfOscillation;
        outputValues.add(formatAndCheckValue(dynamicDeflection));
        wordValues.put(R.string.mech_imp_var_dynamicDeflection, dynamicDeflection);

        double auxiliaryFixingCoeff = 0.0012 + 0.04 * Math.log10(length / width); // для стационарного способа крепления
        wordValues.put(R.string.mech_imp_var_auxiliaryFixingCoeff, auxiliaryFixingCoeff);
        double dynamicLoad = (rigidity * dynamicDeflection) / (auxiliaryFixingCoeff * Math.pow(width, 4));
        outputValues.add(formatAndCheckValue(dynamicLoad));
        wordValues.put(R.string.mech_imp_var_dynamicLoad, dynamicLoad);


        double auxiliaryCoeff = 0.0513 + 0.108 * Math.log10(length / width);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeff, auxiliaryCoeff);
        double maxBendingMoment = auxiliaryCoeff * dynamicLoad * Math.pow(width, 2);
        outputValues.add(formatAndCheckValue(maxBendingMoment));
        wordValues.put(R.string.mech_imp_var_maxBendingMoment, maxBendingMoment);

        double maxDynamicTensionOfBending = VibrationStrength.bendingStressMPa(maxBendingMoment, thickness);
        outputValues.add(formatAndCheckValue(maxDynamicTensionOfBending));
        wordValues.put(R.string.mech_imp_var_maxDynamicTensionOfBending, maxDynamicTensionOfBending);

        final double fiberglassEnduranceLimit = 105; // для стеклотекстолита
        final double allowableDurabilityReserveForFiberglass = 2; // 1,8..2 // для стеклотекстолита
        double allowableTensionOfBending = fiberglassEnduranceLimit / allowableDurabilityReserveForFiberglass; // для стеклотекстолита
        outputValues.add(formatAndCheckValue(allowableTensionOfBending));
        wordValues.put(R.string.mech_imp_var_allowableTensionOfBending, allowableTensionOfBending);

        if (VibrationStrength.isBelowAllowable(maxDynamicTensionOfBending, allowableTensionOfBending)) { // Both values are MPa
            successResult = true;
            resultTextId = R.string.successful_vibration_durable;
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result, R.string.observed));
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result_condition, R.string.more));
        } else {
            successResult = false;
            resultTextId = R.string.unsuccessful_vibration_durable;
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result, R.string.unobserved));
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result_condition, R.string.less_or_equals));
        }

        formatValues();
    }

    public void calculateVibrationStable() {

        wordValues = new HashMap<>();
        outputValues = new ArrayList<>();

        double density = 0; // плотность материала
        double moduleElasticityOfMaterial = 0; // модуль упругости
        double moduleElasticityOfMaterialDividedByPow10_10 = 0; // модуль упругости
        double coeffPoisson = 0; // коэф. Пуассона
        StringWithTag materialStringWithTag = inputSpinnerValuesAndNamesId.get(R.string.material_PCB);
        int materialId = (int) materialStringWithTag.getTag();
        switch (materialId) {
            case R.string.material_fiberglass_stef_1:
                density = 2050;
                moduleElasticityOfMaterialDividedByPow10_10 = 3.02;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.22;
                break;
            case R.string.material_fiberglass_ste:
                density = 1980;
                moduleElasticityOfMaterialDividedByPow10_10 = 3.5;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.214;
                break;
            case R.string.material_stef:
                density = 2470;
                moduleElasticityOfMaterialDividedByPow10_10 = 3.3;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.279;
                break;
            case R.string.material_sf_2:
                density = 2670;
                moduleElasticityOfMaterialDividedByPow10_10 = 5.7;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.24;
                break;
            case R.string.material_ndf:
                density = 2320;
                moduleElasticityOfMaterialDividedByPow10_10 = 3.45;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.238;
                break;
            case R.string.material_getinax_gf_1:
                density = 1450;
                moduleElasticityOfMaterialDividedByPow10_10 = 2.7;
                moduleElasticityOfMaterial = moduleElasticityOfMaterialDividedByPow10_10 * Math.pow(10, 10);
                coeffPoisson = 0.21;
                break;
        }
        wordValues.put(R.string.mech_imp_var_density, density);
        wordValues.put(R.string.mech_imp_var_moduleElasticityOfMaterial, moduleElasticityOfMaterialDividedByPow10_10);
        wordValues.put(R.string.mech_imp_var_coeffPoisson, coeffPoisson);
        wordStringValues.add(new WordItem(R.string.mech_imp_var_material, materialStringWithTag.toString()));

        double length = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_length));
        double width = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_width));
        double thickness = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_thickness));
        wordValues.put(R.string.mech_imp_var_length, length);
        wordValues.put(R.string.mech_imp_var_width, width);
        wordValues.put(R.string.mech_imp_var_thickness, thickness);
        double massPCB = density * length * width * thickness;
        outputValues.add(massPCB);
        wordValues.put(R.string.mech_imp_var_massPCB, massPCB);

        double massofAllElements = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_mass_all_elements));
        wordValues.put(R.string.mech_imp_var_massofAllElements, massofAllElements);
        double coeffMassElements = 1 / Math.sqrt((massofAllElements / massPCB) + 1);
        outputValues.add(coeffMassElements);
        wordValues.put(R.string.mech_imp_var_coeffMassElements, coeffMassElements);

        double coeffFixingMethod = 0;
        double fixingFirstCoeff = 0;
        double fixingSecondCoeff = 0;
        double fixingThirdCoeff = 0;
        StringWithTag fixingMethodStringWithTag =  inputSpinnerValuesAndNamesId.get(R.string.fixing_method);
        int fixingMethod = (int) fixingMethodStringWithTag.getTag();
        switch (fixingMethod) {
            case R.string.vibration_fixing_method_1:
                fixingFirstCoeff = 9.87;
                fixingSecondCoeff = 2.33;
                fixingThirdCoeff = 2.44;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_2:
                fixingFirstCoeff = 9.87;
                fixingSecondCoeff = 2.57;
                fixingThirdCoeff = 5.14;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_3:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.48;
                fixingThirdCoeff = 0.19;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_4:
                fixingFirstCoeff = 15.42;
                fixingSecondCoeff = 1.11;
                fixingThirdCoeff = 1;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) +
                        fixingThirdCoeff * (Math.pow(length, 4) / Math.pow(width, 4))));
                break;
            case R.string.vibration_fixing_method_5:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.57;
                fixingThirdCoeff = 0.47;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
            case R.string.vibration_fixing_method_6:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.61;
                fixingThirdCoeff = 1;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) +
                        fixingThirdCoeff * (Math.pow(length, 4) / Math.pow(width, 4))));
                break;
            case R.string.vibration_fixing_method_7:
                fixingFirstCoeff = 22.37;
                fixingSecondCoeff = 0.14;
                fixingThirdCoeff = 0.02;
                coeffFixingMethod = fixingFirstCoeff * Math.sqrt(1 + (fixingSecondCoeff * (Math.pow(length, 2) / Math.pow(width, 2)) + (fixingThirdCoeff *
                        (Math.pow(length, 4) / Math.pow(width, 4)))));
                break;
        }
        outputValues.add(coeffFixingMethod);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeffFixingMethodFirst, fixingFirstCoeff);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeffFixingMethodSecond, fixingSecondCoeff);
        wordValues.put(R.string.mech_imp_var_auxiliaryCoeffFixingMethodThird, fixingThirdCoeff);
        wordValues.put(R.string.mech_imp_var_coeffFixingMethod, coeffFixingMethod);
        wordStringValues.add(new WordItem(R.string.mech_imp_var_fixing_method,fixingMethodStringWithTag.toString()) );

        double rigidity = (moduleElasticityOfMaterial * Math.pow(thickness, 3)) / (12 * (1 - Math.pow(coeffPoisson, 2)));
        outputValues.add(rigidity);
        wordValues.put(R.string.mech_imp_var_rigidity, rigidity);


        final double accelerationOfGravity = 9.81;
        final double specificPCBWeight = 20500;
        double excitingFrequency = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_exciting_frequency));
        wordValues.put(R.string.mech_imp_var_excitingFrequency, excitingFrequency);
        double fundamentalFrequency = ((coeffMassElements * coeffFixingMethod) / (2 * Math.PI * Math.pow(length, 2))) *
                Math.sqrt((rigidity * accelerationOfGravity) / (specificPCBWeight * thickness));
        outputValues.add(fundamentalFrequency);
        wordValues.put(R.string.mech_imp_var_fundamentalFrequency, fundamentalFrequency);

        double fundamentalDividedByExcitingFreq = fundamentalFrequency / excitingFrequency;
        if (fundamentalDividedByExcitingFreq > 2) {
            successResult = true;
            resultTextId = R.string.successful_vibration_stable;
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result, R.string.stable));
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result_condition, R.string.more));
        } else {
            successResult = false;
            resultTextId = R.string.unsuccessful_vibration_stable;
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result, R.string.unstable));
            wordStringValues.add(new WordItem(R.string.mech_imp_var_result_condition, R.string.less_or_equals));

        }
        wordValues.put(R.string.mech_imp_var_fundamentalDividedByExcitingFreq, fundamentalDividedByExcitingFreq);

        formatValues();
    }

    private void formatValues() {
        for (int i = 0; i<outputValues.size(); i++){
            Double formattedAndCheckedValue = formatAndCheckValue(outputValues.get(0));
            outputValues.add(formattedAndCheckedValue);
            outputValues.remove(outputValues.get(0));
        }

        List<Integer> keys = new ArrayList<Integer>(wordValues.keySet());
        List<Double> values = new ArrayList<Double>(wordValues.values());
        for (int i = 0; i<wordValues.size(); i++){
            Double formattedAndCheckedValue = formatAndCheckValue(values.get(i));
            String wordStringValue = formattedAndCheckedValue.toString().replace(".",",");
            wordStringValues.add(new WordItem(keys.get(i), wordStringValue));
        }
        wordValues = null;
    }

    private double formatAndCheckValue(double value) {
//        DecimalFormat df = new DecimalFormat("#.####"); // Another way to round
//        df.setRoundingMode(RoundingMode.HALF_EVEN);
////            for (Number n : Arrays.asList(12, 123.12345, 0.23, 0.1, 2341234.212431324)) { // EXAMPLE
////                Double d = n.doubleValue();
////                System.out.println(df.format(d));
////            }
        checkValueForError(value);

        return Math.round(value * 1e4) / 1e4; // округление до нужных знаков;

    }


    private void checkValueForError(double outputValue) {
        boolean nan = Double.isNaN(outputValue);
        if (nan) {
            //throw new NumberFormatException("One of values is NotANumber");
            error = true;
        }
    }

    public boolean hasAnyError() {
        return error;
    }

    public boolean isSuccessResult() {
        return successResult;
    }

    public int getResultTextId() {

        if (hasAnyError()) {
            return R.string.error_result;
        } else {
            return resultTextId;
        }
    }


    public ArrayList<WordItem> getWordStringValues() {
        return wordStringValues;
    }

    public ArrayList<Double> getOutputValues() {
        return outputValues;
    }


    public void calculateFailureRate() {
        wordValues = new HashMap<>();
        outputValues = new ArrayList<>();

        double systemFailureRate = 0;
        StringWithTag conditions = inputSpinnerValuesAndNamesId.get(R.string.working_conditions);
        InfluenceOfMechanicalImpactsCoeffItem influenceOfMechanicalImpactsCoeffItem =
                (InfluenceOfMechanicalImpactsCoeffItem) conditions.getTag();
        wordStringValues.add(new WordItem(R.string.dependab_fail_rate_workingConditions, conditions.toString()));

        StringWithTag humidityAndTemp = inputSpinnerValuesAndNamesId.get(R.string.humidity_and_temperature);
        InfluenceOfHumidityAndTempCoeffItem influenceOfHumidityAndTempCoeffItem =
                (InfluenceOfHumidityAndTempCoeffItem) humidityAndTemp.getTag();
        wordStringValues.add(new WordItem(R.string.dependab_fail_rate_humidityAndTemperature, humidityAndTemp.toString()));

        StringWithTag atmospherePressure = inputSpinnerValuesAndNamesId.get(R.string.atmosphere_pressure);
        AtmospherePressureItem atmospherePressureItem = (AtmospherePressureItem) atmospherePressure.getTag();
        wordStringValues.add(new WordItem(R.string.dependab_fail_rate_atmospherePressure, atmospherePressure.toString()));

        for (Map.Entry<String, HashMap<Integer, String>> entryExpandedRow : inputExpandedRows.entrySet()){
            HashMap<Integer, String> componentChilds = entryExpandedRow.getValue();
            for (StringWithTag stringWithTag : dialogItemChooserItems){
                String parentName = entryExpandedRow.getKey();
                if (stringWithTag.toString().equals(parentName)){
                    ComponentFailureRateItem componentFailureRateItem = (ComponentFailureRateItem) stringWithTag.getTag();
                    double normalFailureRate = componentFailureRateItem.getValue();
                    double K1 = influenceOfMechanicalImpactsCoeffItem.getK1();
                    double K2 = influenceOfMechanicalImpactsCoeffItem.getK2();
                    double K3 = influenceOfHumidityAndTempCoeffItem.getCorrectionCoeff();
                    double K4 = atmospherePressureItem.getCorrectionCoeff();
                    double groupFailureRateConsideringExternalImpacts = normalFailureRate * K1 * K2 * K3 * K4;

                    double correctionCoeffInWork = 0;
                    HashMap<Integer,StringWithTag> parent = inputExpandedSpinnerRows.get(parentName);
                    StringWithTag stringWithTag1Child = parent.get(R.string.surface_temperature_of_the_components);
//                    Double.parseDouble(stringWithTag1Child.toString());
                    ComponentCorrectionCoeffItem componentCorrectionCoeffItem = (ComponentCorrectionCoeffItem) stringWithTag1Child.getTag();
                    switch((int)parent.get(R.string.work_mode_name).getTag()){
                        case R.string.static_mode:

                            correctionCoeffInWork = componentCorrectionCoeffItem.getStaticModeValue();
                            break;
                        case R.string.impulse_mode:
                            correctionCoeffInWork = componentCorrectionCoeffItem.getImpulseModeValue();
                            break;


                    }
//                    double correctionCoeffInWork = Double.parseDouble(componentChilds.get(R.string.correction_coeff_in_work));
                    double groupFailureRateInWork = groupFailureRateConsideringExternalImpacts * correctionCoeffInWork;

                    double numberOfComponents = Double.parseDouble(componentChilds.get(R.string.number_of_components));
                    double groupFailureRateTotal = groupFailureRateInWork * numberOfComponents;

                    systemFailureRate+= groupFailureRateTotal;
                }
            }

        }

        outputValues.add(systemFailureRate);
        wordValues.put(R.string.dependab_fail_rate_systemFailureRate, systemFailureRate);
        double averageOperatingTime = 1 / systemFailureRate * Math.pow(10,6);
        outputValues.add(averageOperatingTime);
        wordValues.put(R.string.dependab_fail_rate_averageOperatingTime, averageOperatingTime);
        double requiredTimeOfWork = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.required_time_of_work));
        wordValues.put(R.string.dependab_fail_rate_requiredTimeOfWork, requiredTimeOfWork);
        double probabilityOfTroubleFreeWork = Math.exp(-requiredTimeOfWork/averageOperatingTime);
        double probabilityOfTroubleFreeWorkInPercents = probabilityOfTroubleFreeWork*100;
        outputValues.add(probabilityOfTroubleFreeWorkInPercents);
        wordValues.put(R.string.dependab_fail_rate_probabilityOfTroubleFreeWorkInPercents, probabilityOfTroubleFreeWorkInPercents);


//        if (probabilityOfTroubleFreeWorkInPercents > 50) {
            successResult = true;
            resultTextId = R.string.failure_rate_is;
//            resultTextId = R.string.successful_failure_rate;
//        } else {
//            successResult = false;
//            resultTextId = R.string.unsuccessful_failure_rate;
//        }

        formatValues();

        chartItems = new ArrayList<>();
        double x = 0;
        double plus = requiredTimeOfWork / 20;
        for (int i = 0; i<21; i++){
            double y = Math.exp(-(x/averageOperatingTime))*100;
            chartItems.add(new ChartItem(x,y));
            x += plus;
        }
    }

    public ArrayList<ChartItem> getChartItems() {
        return chartItems;
    }
}

//    public boolean isVibrationDurable() {
//        return isVibrationDurable;
//    }
//
//    public boolean isVibrationStable() {
//        return isVibrationStable;
//    }
//
//    public boolean isImpactResistance() {
//        return isImpactResistance;
//    }


//BACKUP!!!!!
//    ArrayList<Double> wordValues = new ArrayList<>();
//    //double[] wordValues = new double[12];
//    //int iterator = 0;
//    double density = 0; // плотность материала
//    double moduleElasticityOfMaterial = 0; // модуль упругости
//    double coeffPoisson = 0; // коэф. Пуассона
//        switch (inputSpinnerValuesAndNamesId.get(R.string.material_PCB)) {
//                case R.string.material_fiberglass_stef_1:
//                density = 2050;
//                moduleElasticityOfMaterial = 3.02 * Math.pow(10, 10);
//                coeffPoisson = 0.22;
//                break;
//                case R.string.material_fiberglass_ste:
//                density = 1980;
//                moduleElasticityOfMaterial = 3.5 * Math.pow(10, 10);
//                coeffPoisson = 0.214;
//                break;
//                case R.string.material_stef:
//                density = 2470;
//                moduleElasticityOfMaterial = 3.3 * Math.pow(10, 10);
//                coeffPoisson = 0.279;
//                break;
//                case R.string.material_sf_2:
//                density = 2670;
//                moduleElasticityOfMaterial = 5.7 * Math.pow(10, 10);
//                coeffPoisson = 0.24;
//                break;
//                case R.string.material_ndf:
//                density = 2320;
//                moduleElasticityOfMaterial = 3.45 * Math.pow(10, 10);
//                coeffPoisson = 0.238;
//                break;
//                case R.string.material_getinax_gf_1:
//                density = 1450;
//                moduleElasticityOfMaterial = 2.7 * Math.pow(10, 10);
//                coeffPoisson = 0.21;
//                break;
//                }
//                double length = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_length));
//                double width = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_width));
//                double thickness = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_thickness));
//                double massPCB = density * length * width * thickness;
//                wordValues.add(formatAndCheckValue(massPCB));
//
//                //double numberOfIC = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_number));
//                double massofAllElements = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_mass_all_elements));
//                double coeffMassElements = 1 / Math.sqrt((massofAllElements / massPCB) + 1);
//                wordValues.add(formatAndCheckValue(coeffMassElements));
//
//                double coeffFixingMethod = 0;
//                switch (inputSpinnerValuesAndNamesId.get(R.string.fixing_method)) {
//                case R.drawable.vibration_fixing_method_1:
//                coeffFixingMethod = 9.87 * Math.sqrt(1 + (2.33 * (Math.pow(length, 2) / Math.pow(width, 2)) + (2.44 *
//                (Math.pow(length, 4) / Math.pow(width, 4)))));
//                break;
//                case R.drawable.vibration_fixing_method_2:
//                coeffFixingMethod = 9.87 * Math.sqrt(1 + (2.57 * (Math.pow(length, 2) / Math.pow(width, 2)) + (5.14 *
//                (Math.pow(length, 4) / Math.pow(width, 4)))));
//                break;
//                case R.drawable.vibration_fixing_method_3:
//                coeffFixingMethod = 22.37 * Math.sqrt(1 + (0.48 * (Math.pow(length, 2) / Math.pow(width, 2)) + (0.19 *
//                (Math.pow(length, 4) / Math.pow(width, 4)))));
//                break;
//                case R.drawable.vibration_fixing_method_4:
//                coeffFixingMethod = 15.42 * Math.sqrt(1 + (1.11 * (Math.pow(length, 2) / Math.pow(width, 2)) +
//                (Math.pow(length, 4) / Math.pow(width, 4))));
//                break;
//                case R.drawable.vibration_fixing_method_5:
//                coeffFixingMethod = 22.37 * Math.sqrt(1 + (0.57 * (Math.pow(length, 2) / Math.pow(width, 2)) + (0.47 *
//                (Math.pow(length, 4) / Math.pow(width, 4)))));
//                break;
//                case R.drawable.vibration_fixing_method_6:
//                coeffFixingMethod = 22.37 * Math.sqrt(1 + (0.61 * (Math.pow(length, 2) / Math.pow(width, 2)) +
//                (Math.pow(length, 4) / Math.pow(width, 4))));
//                break;
//                case R.drawable.vibration_fixing_method_7:
//                coeffFixingMethod = 22.37 * Math.sqrt(1 + (0.14 * (Math.pow(length, 2) / Math.pow(width, 2)) + (0.02 *
//                (Math.pow(length, 4) / Math.pow(width, 4)))));
//                break;
//                }
//                wordValues.add(formatAndCheckValue(coeffFixingMethod));
//
//                //double moduleElasticityOfMaterial = 3.02 * Math.pow(10, 10);
//                //double coeffPoisson = 0.22;
//                double rigidity = (moduleElasticityOfMaterial * Math.pow(thickness, 3)) / (12 * (1 - Math.pow(coeffPoisson, 2)));
//                wordValues.add(formatAndCheckValue(rigidity));
//
//                double accelerationOfGravity = 9.81;
//                double specificPCBWeight = 20500;
//                double excitingFrequency = Double.parseDouble(inputRowValuesAndNamesId.get(R.string.PCB_exciting_frequency));
//                double fundamentalFrequency = ((coeffMassElements * coeffFixingMethod) / (2 * Math.PI * Math.pow(length, 2))) *
//                Math.sqrt((rigidity * accelerationOfGravity) / (specificPCBWeight * thickness));
//                wordValues.add(formatAndCheckValue(fundamentalFrequency));
//                if (fundamentalFrequency / excitingFrequency > 2) {
//                isVibrationStable = true;
//                }
//
//final double coeffOfOverload = 8; // const
//        double amplitudeOfOscillation = (250 * coeffOfOverload) / Math.pow(fundamentalFrequency, 2);
//        wordValues.add(formatAndCheckValue(amplitudeOfOscillation));
//
//        double oscillationAttenuationIndex = 0.06; // для стеклотекстолита
//        double coeffOfDynamic = 1 / Math.sqrt(Math.pow(1 - Math.pow(excitingFrequency / fundamentalFrequency, 2), 2) +
//        (Math.pow(excitingFrequency / fundamentalFrequency, 2) * Math.pow(oscillationAttenuationIndex, 2)));
//        wordValues.add(formatAndCheckValue(coeffOfDynamic));
//
//        double dynamicDeflection = coeffOfDynamic * amplitudeOfOscillation;
//        wordValues.add(formatAndCheckValue(dynamicDeflection));
//
//        double auxiliaryFixingCoeff = 0.0012 + 0.04 * Math.log10(length / width); // для стационарного способа крепления
//        double dynamicLoad = (rigidity * dynamicDeflection) / (auxiliaryFixingCoeff * Math.pow(width, 4));
//        wordValues.add(formatAndCheckValue(dynamicLoad));
//
//        double auxiliaryCoeff = 0.0513 + 0.108 * Math.log10(length / width);
//        double maxBendingMoment = auxiliaryCoeff * dynamicLoad * Math.pow(width, 2);
//        wordValues.add(formatAndCheckValue(maxBendingMoment));
//
//        double maxDynamicTensionOfBending = VibrationStrength.bendingStressMPa(maxBendingMoment, thickness);
//        wordValues.add(formatAndCheckValue(maxDynamicTensionOfBending));
//
//        double fiberglassEnduranceLimit = 105; // для стеклотекстолита
//        double allowableDurabilityReserveForFiberglass = 2; // 1,8..2 // для стеклотекстолита
//        double allowableTensionOfBending = fiberglassEnduranceLimit / allowableDurabilityReserveForFiberglass; // для стеклотекстолита
//        wordValues.add(formatAndCheckValue(allowableTensionOfBending));
//
//        if (VibrationStrength.isBelowAllowable(maxDynamicTensionOfBending, allowableTensionOfBending)) { // Both values are MPa
//        isVibrationDurable = true;
//        }
//
//        // УДАРОПРОЧНОСТЬ
//        double t = 0.01; // длительность ударного импульса
//        //1 Определяется условная частота ударного импульса
//        double w = Math.PI / t;
//
//        double v = w / (2*Math.PI*fundamentalFrequency);
//        // 2 Рассчитывается коэффициент передачи при ударе для прямоугольного импульса
//        double K = 2*Math.sin(Math.PI/(2*v));
//
//        double H = 147.0; // амплитуда ускорения ударного импульса, м/с2
//        // 3 Рассчитывается ударное ускорение
//        double a = H*K;
//
//        // 4 Определяется максимальное относительное перемещение
//        double Zmax = ((2*H)/(2*Math.PI*fundamentalFrequency))*Math.sin(Math.PI/(2*v));
//
//        //double b = ;
//        // 5 Проверяется выполнение условия ударопрочности по следующим критериям:
//        //if (Zmax<0.003*b){
//        isImpactResistance = true;
////}
//
//
////WTFКоэффициент передачи по ускорению рассчитывается
////        double coeffTransferOfAcceleration = Math.sqrt(1+)
////        //Определим виброперемещение:
////        double vibroMovement = amplitudeOfVibrationOffset * coeffTransferOfAcceleration;
////
////
////        //5. Определение максимального прогиба ПП.
////        double maxDeflection = Math.abs(vibroMovement - amplitudeOfVibrationOffset);
////        double parallelSide;
////        if (length>width){
////            parallelSide = length;
////        } else {
////            parallelSide = width;
////        }
////        if (maxDeflection<0.003*parallelSide) //