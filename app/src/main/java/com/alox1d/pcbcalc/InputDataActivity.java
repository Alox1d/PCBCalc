package com.alox1d.pcbcalc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alox1d.pcbcalc.models.AtmospherePressureItem;
import com.alox1d.pcbcalc.models.CalcRowItem;
import com.alox1d.pcbcalc.models.ComponentCorrectionCoeffItem;
import com.alox1d.pcbcalc.models.ComponentFailureRateItem;
import com.alox1d.pcbcalc.models.ComponentLoadingCoeffItem;
import com.alox1d.pcbcalc.models.ExpandedRecyclerParentItem;
import com.alox1d.pcbcalc.models.InfluenceOfMechanicalImpactsCoeffItem;
import com.alox1d.pcbcalc.models.InfluenceOfHumidityAndTempCoeffItem;
import com.alox1d.pcbcalc.models.SpinnerRowItem;
import com.alox1d.pcbcalc.models.StringWithTag;
import com.alox1d.pcbcalc.repositories.AtmospherePressureRep;
import com.alox1d.pcbcalc.repositories.ComponentsCorrectionCoeffRep;
import com.alox1d.pcbcalc.repositories.ComponentsFailureRateRep;
import com.alox1d.pcbcalc.repositories.ComponentsLoadingCoeffRep;
import com.alox1d.pcbcalc.repositories.InfluenceOfMechanicalImpactsCoeffsRep;
import com.alox1d.pcbcalc.repositories.InfluenceOfhumidityCoeffsRep;
import com.alox1d.pcbcalc.ui.CalculateUIHelper;
import com.alox1d.pcbcalc.ui.DialogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InputDataActivity extends AppCompatActivity implements View.OnClickListener {

    private CalculateUIHelper calculateUIHelper;
    private ArrayList<CalcRowItem> calcRowItems;
    private ArrayList<SpinnerRowItem> spinnerRowItems;
    private HashMap<Integer, EditText> etPCBs;
    private HashMap<Integer, Spinner> spinnerPCBs;
    private HashMap<String, HashMap<Integer, EditText>> expandedPCBEditTextRows;
    private HashMap<String, HashMap<Integer, Spinner>> expandedPCBSpinnerRows;
    private ArrayList<StringWithTag> stringWithTags;
    private ArrayList<CalcRowItem> expandableListViewItems;

    private int selectedCalculationMethod;

    private LinkedHashMap<String, String> componentsFailureRate;

    private int dialogButtonNameId;
    private ArrayList<StringWithTag> dialogItemChooserItems;
    private HashMap<Integer, Double> expandedRecyclerTvEtChilds;
    private ArrayList<SpinnerRowItem> expandedRecyclerSpinnerChilds;
    private ArrayList<ExpandedRecyclerParentItem> expandedParents;

    private static final String RECYCLER_STATE_KEY = "RECYCLER_STATE_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);

        initializeData();

        initializeUI();

        initializeListeners();

    }

    private void initializeData() {
        selectedCalculationMethod = this.getIntent().getIntExtra(getString(R.string.mechanical_impacts_method_name), 0);


        switch (selectedCalculationMethod) {
            case R.string.vibration_durable_name:

                calcRowItems = new ArrayList<>();
                calcRowItems.add(new CalcRowItem(R.string.PCB_length, Double.parseDouble(getString(R.string.moq_PCB_length)), getString(R.string.units_of_measurement_meter)));
                calcRowItems.add(new CalcRowItem(R.string.PCB_width, Double.parseDouble(getString(R.string.moq_PCB_width)), getString(R.string.units_of_measurement_meter)));
                calcRowItems.add(new CalcRowItem(R.string.PCB_thickness, Double.parseDouble(getString(R.string.moq_PCB_thickness)), getString(R.string.units_of_measurement_meter)));
                calcRowItems.add(new CalcRowItem(R.string.PCB_mass_all_elements, Double.parseDouble(getString(R.string.moq_PCB_mass_all_elements)), getString(R.string.units_of_measurement_kilogram)));

                spinnerRowItems = new ArrayList<>();
                stringWithTags = new ArrayList<>();
                stringWithTags.add(new StringWithTag(R.string.material_fiberglass_stef_1, getString(R.string.material_fiberglass_stef_1)));
                spinnerRowItems.add(new SpinnerRowItem(R.string.material_PCB, stringWithTags));
                stringWithTags = new ArrayList<>();
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_1, R.string.vibration_fixing_method_1, getString(R.string.vibration_fixing_method_1)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_2, R.string.vibration_fixing_method_2, getString(R.string.vibration_fixing_method_2)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_3, R.string.vibration_fixing_method_3, getString(R.string.vibration_fixing_method_3)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_4, R.string.vibration_fixing_method_4, getString(R.string.vibration_fixing_method_4)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_5, R.string.vibration_fixing_method_5, getString(R.string.vibration_fixing_method_5)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_6, R.string.vibration_fixing_method_6, getString(R.string.vibration_fixing_method_6)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_7, R.string.vibration_fixing_method_7, getString(R.string.vibration_fixing_method_7)));
                spinnerRowItems.add(new SpinnerRowItem(R.string.fixing_method, stringWithTags));

                calcRowItems.add(new CalcRowItem(R.string.PCB_exciting_frequency,
                        Double.parseDouble(getString(R.string.moq_PCB_exciting_frequency)),
                        getString(R.string.units_of_measurement_hertz)));

                break;

            case R.string.vibration_stable_name:

                calcRowItems = new ArrayList<>();
                calcRowItems.add(new CalcRowItem(R.string.PCB_length, Double.parseDouble(getString(R.string.moq_PCB_length)), getString(R.string.units_of_measurement_meter)));
                calcRowItems.add(new CalcRowItem(R.string.PCB_width, Double.parseDouble(getString(R.string.moq_PCB_width)), getString(R.string.units_of_measurement_meter)));
                calcRowItems.add(new CalcRowItem(R.string.PCB_thickness, Double.parseDouble(getString(R.string.moq_PCB_thickness)), getString(R.string.units_of_measurement_meter)));
                calcRowItems.add(new CalcRowItem(R.string.PCB_mass_all_elements, Double.parseDouble(getString(R.string.moq_PCB_mass_all_elements)), getString(R.string.units_of_measurement_kilogram)));

                spinnerRowItems = new ArrayList<>();
                stringWithTags = new ArrayList<>();
                stringWithTags.add(new StringWithTag(R.string.material_fiberglass_stef_1, getString(R.string.material_fiberglass_stef_1)));
                stringWithTags.add(new StringWithTag(R.string.material_fiberglass_ste, getString(R.string.material_fiberglass_ste)));
                stringWithTags.add(new StringWithTag(R.string.material_stef, getString(R.string.material_stef)));
                stringWithTags.add(new StringWithTag(R.string.material_sf_2, getString(R.string.material_sf_2)));
                stringWithTags.add(new StringWithTag(R.string.material_ndf, getString(R.string.material_ndf)));
                stringWithTags.add(new StringWithTag(R.string.material_getinax_gf_1, getString(R.string.material_getinax_gf_1)));
                spinnerRowItems.add(new SpinnerRowItem(R.string.material_PCB, stringWithTags));
                stringWithTags = new ArrayList<>();
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_1, R.string.vibration_fixing_method_1, getString(R.string.vibration_fixing_method_1)));
                ;
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_2, R.string.vibration_fixing_method_2, getString(R.string.vibration_fixing_method_2)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_3, R.string.vibration_fixing_method_3, getString(R.string.vibration_fixing_method_3)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_4, R.string.vibration_fixing_method_4, getString(R.string.vibration_fixing_method_4)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_5, R.string.vibration_fixing_method_5, getString(R.string.vibration_fixing_method_5)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_6, R.string.vibration_fixing_method_6, getString(R.string.vibration_fixing_method_6)));
                stringWithTags.add(new StringWithTag(R.drawable.vibration_fixing_method_7, R.string.vibration_fixing_method_7, getString(R.string.vibration_fixing_method_7)));
                spinnerRowItems.add(new SpinnerRowItem(R.string.fixing_method, stringWithTags));

                calcRowItems.add(new CalcRowItem(R.string.PCB_exciting_frequency, Double.parseDouble(getString(R.string.moq_PCB_exciting_frequency)), getString(R.string.units_of_measurement_hertz)));

                break;
            case R.string.failure_rate_name:

                calcRowItems = new ArrayList<>();
                calcRowItems.add(new CalcRowItem(R.string.required_time_of_work, Double.parseDouble(getString(R.string.moq_required_time_of_work)), getString(R.string.units_of_measurement_hour)));

                spinnerRowItems = new ArrayList<>();
                stringWithTags = new ArrayList<>();
                InfluenceOfMechanicalImpactsCoeffsRep influenceOfMechanicalImpactsCoeffsRep = new InfluenceOfMechanicalImpactsCoeffsRep(this);
                List<InfluenceOfMechanicalImpactsCoeffItem> influenceOfMechanicalImpactsCoeffItems = influenceOfMechanicalImpactsCoeffsRep.getItems();
                for (InfluenceOfMechanicalImpactsCoeffItem item : influenceOfMechanicalImpactsCoeffItems) {
                    stringWithTags.add(new StringWithTag(item, item.getConditionsName()));
                }
                spinnerRowItems.add(new SpinnerRowItem(R.string.working_conditions, stringWithTags));
                stringWithTags = new ArrayList<>();
                InfluenceOfhumidityCoeffsRep influenceOfhumidityCoeffsRep = new InfluenceOfhumidityCoeffsRep(this);
                List<InfluenceOfHumidityAndTempCoeffItem> influenceOfHumidityAndTempCoeffItems = influenceOfhumidityCoeffsRep.getItems();
                for (InfluenceOfHumidityAndTempCoeffItem item : influenceOfHumidityAndTempCoeffItems) {
                    stringWithTags.add(new StringWithTag(item, item.getHumidity() + " | " + item.getTemperature()));
                }
                spinnerRowItems.add(new SpinnerRowItem(R.string.humidity_and_temperature, stringWithTags));
                stringWithTags = new ArrayList<>();
                AtmospherePressureRep AtmospherePressureRep = new AtmospherePressureRep(this);
                List<AtmospherePressureItem> AtmospherePressureItems = AtmospherePressureRep.getItems();
                for (AtmospherePressureItem item : AtmospherePressureItems) {
                    stringWithTags.add(new StringWithTag(item, item.getPressure()));
                }
                spinnerRowItems.add(new SpinnerRowItem(R.string.atmosphere_pressure, stringWithTags));

//                calcRowItems.add(new CalcRowItem(R.string.surface_temperature_of_the_components, Double.parseDouble(getString(R.string.moq_surface_temperature_of_the_components)), getString(R.string.units_of_measurement_celsius)));

                dialogButtonNameId = R.string.add_components;
                dialogItemChooserItems = new ArrayList<>();
                ComponentsFailureRateRep componentsFailureRateRep = new ComponentsFailureRateRep(this);
                List<ComponentFailureRateItem> componentFailureRateItems = componentsFailureRateRep.getItems();
                for (ComponentFailureRateItem item : componentFailureRateItems) {
                    dialogItemChooserItems.add(new StringWithTag(item, item.getComponentName()));
                }
                expandedRecyclerTvEtChilds = new HashMap<>();
                expandedRecyclerTvEtChilds.put(R.string.number_of_components, Double.parseDouble(getString(R.string.moq_number_of_components)));


//                ComponentsLoadingCoeffRep componentsLoadingCoeffRep = new ComponentsLoadingCoeffRep(this);
//                List<ComponentLoadingCoeffItem> componentLoadingCoeffItems = componentsLoadingCoeffRep.getItems();
//                stringWithTags = new ArrayList<>();
//                for (ComponentLoadingCoeffItem item : componentLoadingCoeffItems) {
//                    stringWithTags.add(new StringWithTag(item, item.getComponentsName()));
//                }
//                expandedRecyclerSpinnerChilds.add(new SpinnerRowItem(R.string.component_type, stringWithTags));
                stringWithTags = new ArrayList<>();
                stringWithTags.add(new StringWithTag(R.string.impulse_mode, getString(R.string.impulse_mode)));
                stringWithTags.add(new StringWithTag(R.string.static_mode, getString(R.string.static_mode)));
                expandedRecyclerSpinnerChilds = new ArrayList<>();
                expandedRecyclerSpinnerChilds.add(new SpinnerRowItem(R.string.work_mode_name, stringWithTags));


                ComponentsCorrectionCoeffRep componentsCorrectionCoeffRep = new ComponentsCorrectionCoeffRep(this);
                List<ComponentCorrectionCoeffItem> componentCorrectionCoeffItems = componentsCorrectionCoeffRep.getItems();
                stringWithTags = new ArrayList<>();
                ArrayList<ArrayList<ComponentCorrectionCoeffItem>> arrayOfCorrectionCoeffItemsArray = new ArrayList<>();
                ArrayList<ComponentCorrectionCoeffItem> correctionCoeffItemsArray = new ArrayList<>();
                for (int correctionItemIterator = 0; correctionItemIterator < componentCorrectionCoeffItems.size(); correctionItemIterator++) {
                    ComponentCorrectionCoeffItem item = componentCorrectionCoeffItems.get(correctionItemIterator);
                    try {
                        if (item.getComponentsType().equals(componentCorrectionCoeffItems.get(correctionItemIterator+1).getComponentsType())) {
                            correctionCoeffItemsArray.add(item);
                        } else {
                            correctionCoeffItemsArray.add(item);
                            arrayOfCorrectionCoeffItemsArray.add(correctionCoeffItemsArray);
                            correctionCoeffItemsArray = new ArrayList<>();
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        correctionCoeffItemsArray.add(item);
                        arrayOfCorrectionCoeffItemsArray.add(correctionCoeffItemsArray);
                    }

                }
                for (ArrayList<ComponentCorrectionCoeffItem> array : arrayOfCorrectionCoeffItemsArray) {
                    stringWithTags.add(new StringWithTag(array, array.get(0).getComponentsType()));
                }
                expandedRecyclerSpinnerChilds.add(new SpinnerRowItem(R.string.component_type, stringWithTags));

                stringWithTags = new ArrayList<>();
                ArrayList<ComponentCorrectionCoeffItem> firstArray = arrayOfCorrectionCoeffItemsArray.get(0);
                for (ComponentCorrectionCoeffItem componentCorrectionCoeffItem : firstArray) {
                    stringWithTags.add(new StringWithTag(componentCorrectionCoeffItem, componentCorrectionCoeffItem.getTemperature().toString()));
                }
                Double firstValue = componentCorrectionCoeffItems.get(0).getImpulseModeValue();
                expandedRecyclerSpinnerChilds.add(new SpinnerRowItem(R.string.surface_temperature_of_the_components, stringWithTags));

//                ComponentsLoadingCoeffRep componentsLoadingCoeffRep = new ComponentsLoadingCoeffRep(this);
//                List<ComponentLoadingCoeffItem> componentLoadingCoeffItems = componentsLoadingCoeffRep.getItems();
//                stringWithTags = new ArrayList<>();
//                for (ComponentLoadingCoeffItem item : componentLoadingCoeffItems) {
//                    stringWithTags.add(new StringWithTag(item, item.getComponentsType()));
//                }
//                expandedRecyclerSpinnerChilds.add(new SpinnerRowItem(R.string.surface_temperature_of_the_components, stringWithTags));
//                expandedRecyclerTvEtChilds.put(R.string.correction_coeff_in_work, 1.0); // TODO что с ним делать?

                break;

        }

//        calcRowItems.add(new CalcRowItem(R.string.PCB_length, 0.16, unitsOfMeasurement[i++]));
//        calcRowItems.add(new CalcRowItem(R.string.PCB_width, 0.08, unitsOfMeasurement[i++]));
//        calcRowItems.add(new CalcRowItem(R.string.PCB_thickness, 0.0015, unitsOfMeasurement[i++]));
//        calcRowItems.add(new CalcRowItem(R.string.PCB_mass_all_elements, 0.038, unitsOfMeasurement[i++]));
//
//        spinnerRowItems = new ArrayList<>();
//        ArrayList<StringWithTag> values = new ArrayList<>();
//        values.add(new StringWithTag(R.string.material_fiberglass_stef_1, getString(R.string.material_fiberglass_stef_1)));
//        values.add(new StringWithTag(R.string.material_fiberglass_ste, getString(R.string.material_fiberglass_ste)));
//        values.add(new StringWithTag(R.string.material_stef, getString(R.string.material_stef)));
//        values.add(new StringWithTag(R.string.material_sf_2, getString(R.string.material_sf_2)));
//        values.add(new StringWithTag(R.string.material_ndf, getString(R.string.material_ndf)));
//        values.add(new StringWithTag(R.string.material_getinax_gf_1, getString(R.string.material_getinax_gf_1)));
//        spinnerRowItems.add(new SpinnerRowItem(R.string.material_PCB, values));
//        values = new ArrayList<>();
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_1, R.drawable.vibration_fixing_method_1, ""));
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_2, R.drawable.vibration_fixing_method_2, ""));
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_3, R.drawable.vibration_fixing_method_3, ""));
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_4, R.drawable.vibration_fixing_method_4, ""));
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_5, R.drawable.vibration_fixing_method_5, ""));
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_6, R.drawable.vibration_fixing_method_6, ""));
//        values.add(new StringWithTag(R.drawable.vibration_fixing_method_7, R.drawable.vibration_fixing_method_7, ""));
//        spinnerRowItems.add(new SpinnerRowItem(R.string.fixing_method, values));
//
//        calcRowItems.add(new CalcRowItem(R.string.PCB_exciting_frequency, 50, unitsOfMeasurement[i]));
    }


    private void initializeUI() {
        calculateUIHelper = new CalculateUIHelper(this);
        calculateUIHelper.initializeToolbar();

        spinnerPCBs = calculateUIHelper.initializeSpinnerRows(spinnerRowItems);
        etPCBs = calculateUIHelper.initializeTvEdRows(calcRowItems);

        if (dialogItemChooserItems != null) {
            expandedPCBEditTextRows = new HashMap<>();
            expandedPCBSpinnerRows = new HashMap<>();
            expandedParents = new ArrayList<>();

            calculateUIHelper.initializeExpandedRecycler(expandedParents, expandedPCBEditTextRows, expandedPCBSpinnerRows);
//            calculateUIHelper.initializeDialogItemChooserButton(dialogButtonNameId, dialogItemChooserItems, expandedRecyclerTvEtChilds);
            calculateUIHelper.initializeDialogItemChooserButton(dialogButtonNameId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogHelper alertHelper = new DialogHelper(InputDataActivity.this);
                    alertHelper.initializeItemChooserDialog(dialogItemChooserItems, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            String expandedRowName = dialogItemChooserItems.get(position).toString();
//                            TextView textView = (TextView) view.findViewById(R.id.text);
                            String expandedRowName = ((TextView) view).getText().toString();
                            for (StringWithTag dialogItemChooserItem : dialogItemChooserItems) {

                                if (dialogItemChooserItem.toString().equals(expandedRowName)) {
                                    calculateUIHelper.insertItemToExpandedRecycler(expandedRowName, expandedRecyclerTvEtChilds, expandedRecyclerSpinnerChilds);
//                                    calculateUIHelper.insertItemToExpandedRecycler(expandedRowName, expandedRecyclerSpinnerChilds);
                                    HashMap<Integer, EditText> expandedEditTexts = calculateUIHelper.getExpandedEditTexts();
//                                    HashMap<String,HashMap<Integer,Spinner>> expandedParentWithChildsSpinners = calculateUIHelper.getExpandedSpinners();
                                    expandedPCBEditTextRows.put(expandedRowName, expandedEditTexts);
                                    expandedPCBSpinnerRows = calculateUIHelper.getExpandedSpinners();
                                }
                            }

                        }


//        String[] etNames = getResources().getStringArray(R.array.et_input_names); // filling by array
//        int[] etValues = getResources().getIntArray(R.array.edittext_values);
//        for (int i = 0; i<etNames.length; i++){
//            initializeRow(etNames[i],etValues[i]);
//        }
                    });
                }
            });
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android misc_toolbar.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeListeners() {

        Button btnCalc = (Button) findViewById(R.id.footer_button);
        btnCalc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footer_button:

                HashMap<Integer, String> inputValuesAndNamesId = new HashMap<>();
                HashMap<String, HashMap<Integer, String>> inputExpandedRows = new HashMap<>();
                HashMap<String, HashMap<Integer, StringWithTag>> inputExpandedSpinnerRows = new HashMap<>();

                LinkedHashMap<Integer, StringWithTag> inputSpinnerValuesLinked = new LinkedHashMap<>();

                for (Map.Entry<Integer, EditText> entry : etPCBs.entrySet()) {
                    EditText editText = entry.getValue();
                    if (editText.getError() != null || editText.getText().toString().equals("")) {
                        Toast.makeText(this, R.string.please_type_correct_data, Toast.LENGTH_SHORT).show();
                        return;

                    }
                    inputValuesAndNamesId.put(entry.getKey(), entry.getValue().getText().toString());
                }
                for (Map.Entry<Integer, Spinner> entry : spinnerPCBs.entrySet()) {
                    int key = entry.getKey();
                    StringWithTag value = (StringWithTag) entry.getValue().getSelectedItem();
                    inputSpinnerValuesLinked.put(key, value);
                }
                if (expandedPCBEditTextRows != null) {
                    for (Map.Entry<String, HashMap<Integer, EditText>> parentEntry : expandedPCBEditTextRows.entrySet()) {
                        HashMap<Integer, String> etChilds = new HashMap<>();
                        for (Map.Entry<Integer, EditText> childsEntry : parentEntry.getValue().entrySet()) {
                            EditText editText = childsEntry.getValue();
                            if (editText.getError() != null || editText.getText().toString().equals("")) {
                                Toast.makeText(this, R.string.please_type_correct_data, Toast.LENGTH_SHORT).show();
                                return;

                            }
                            etChilds.put(childsEntry.getKey(), editText.getText().toString());
                        }
                        inputExpandedRows.put(parentEntry.getKey(), etChilds);

                    }
                }
                if (expandedPCBSpinnerRows != null) {
                    for (Map.Entry<String, HashMap<Integer, Spinner>> parentEntry : expandedPCBSpinnerRows.entrySet()) {
                        HashMap<Integer, StringWithTag> etChilds = new HashMap<>();
                        for (Map.Entry<Integer, Spinner> childsEntry : parentEntry.getValue().entrySet()) {
                            int key = childsEntry.getKey();
                            StringWithTag value = (StringWithTag) childsEntry.getValue().getSelectedItem();
//                            inputSpinnerValuesLinked.put(key, value);
                            etChilds.put(key, value);
                        }
                        inputExpandedSpinnerRows.put(parentEntry.getKey(), etChilds);

                    }
                }


                Intent myIntent = new Intent(this, ResultDataActivity.class);


                //etPCBs.forEach((key, value) -> inputValuesAndNamesId.put(key, value.getText().toString())); // Lambda is not working
                myIntent.putExtra(getString(R.string.inputRowValuesAndNamesId), inputValuesAndNamesId); //todo static final String
                myIntent.putExtra(getString(R.string.inputExpandedRows), inputExpandedRows);
                myIntent.putExtra(getString(R.string.inputExpandedSpinnerRows), inputExpandedSpinnerRows);
                myIntent.putExtra(getString(R.string.inputSpinnerValuesAndNamesId), inputSpinnerValuesLinked);
                myIntent.putExtra(getString(R.string.mechanical_impacts_method_name), selectedCalculationMethod);
                myIntent.putExtra(getString(R.string.dialogItemChooserItems), dialogItemChooserItems);
                this.startActivity(myIntent);
                break;
        }
    }

//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        expandedParents= (ArrayList<ExpandedRecyclerParentItem>) savedInstanceState.getSerializable(RECYCLER_STATE_KEY);
//    }
//
//    // invoked when the activity may be temporarily destroyed, save the instance state here
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putSerializable(RECYCLER_STATE_KEY, expandedParents);
//
//        // call superclass to save any view hierarchy
//        super.onSaveInstanceState(outState);
//    }
}

//    del_AppDatabase db = del_App.getInstance().getDatabase();
//    del_EmployeeDao employeeDao = db.employeeDao();
//    del_Employee employee = new del_Employee();
//                employee.id = 1;
//                        employee.name = "John Smith";
//                        employee.salary = 10000;
//
//                        employeeDao.insert(employee);
// MAY BE USEFUL
//private void initFirebase() {
//    //инициализируем наше приложение для Firebase согласно параметрам в google-services.json
//    // (google-services.json - файл, с настройками для firebase, кот. мы получили во время регистрации)
//    FirebaseApp.initializeApp(this);
//    //получаем точку входа для базы данных
//    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//    //получаем ссылку для работы с базой данных
//    DatabaseReference mDatabaseReference = mFirebaseDatabase.getReference();
//    mDatabaseReference.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            DataSnapshot failureSnapshot = dataSnapshot.child("интенсивности отказов компонентов");
//            Iterable<DataSnapshot> failureChild = failureSnapshot.getChildren();
//            ArrayList<ComponentsFailureRate> componentsFailureRates = new ArrayList<>();
//            for (DataSnapshot failure : failureChild) {
//                ComponentsFailureRate f = failure.getValue(ComponentsFailureRate.class);
//                Double v = f.getValue();
//                componentsFailureRates.add(f);
//            }
//
//        }
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//
//        }
//    });
//}