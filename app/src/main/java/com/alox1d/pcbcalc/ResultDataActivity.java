package com.alox1d.pcbcalc;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alox1d.pcbcalc.models.CalcRowItem;
import com.alox1d.pcbcalc.models.ChartItem;
import com.alox1d.pcbcalc.models.StringWithTag;
import com.alox1d.pcbcalc.models.PCBCalculator;
import com.alox1d.pcbcalc.models.WordItem;
import com.alox1d.pcbcalc.ui.CalculateUIHelper;
import com.alox1d.pcbcalc.ui.DialogHelper;
import com.alox1d.pcbcalc.utils.ApachePOIHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ResultDataActivity extends AppCompatActivity implements View.OnClickListener {
    private CalculateUIHelper calculateUIHelper;
    private HashMap<Integer, String> inputRowValuesAndNamesId;
    private HashMap<Integer, StringWithTag> inputSpinnerValuesAndNamesId;
    private HashMap<String, HashMap<Integer, String>> inputExpandedRows;
    private ArrayList<String> outputNames;
    private ArrayList<Double> outputValues;
    private ArrayList<WordItem> wordItems;
    private ArrayList<String> outputUnitsOfMeasurement;
    PCBCalculator PCBCalculator;
//    private boolean hasError;
//    private boolean isVibrationDurable;
//    private boolean isVibrationStable;

    private int selectedCalculationMethod;

    public ApachePOIHelper apachePOIHelper;
    public DialogHelper dialogHelper;
    public String wordFileName;
    private HashMap<String, HashMap<Integer, StringWithTag>> inputExpandedSpinnerRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data); // activity_vibration_result


        initializeData();

        initializeUI();

        initializeListeners();
    }

    private void initializeData() {
        selectedCalculationMethod = this.getIntent().getIntExtra(getString(R.string.mechanical_impacts_method_name), 0);
        ArrayList<StringWithTag> dialogItemChooserItems = (ArrayList<StringWithTag>) this.getIntent().getSerializableExtra(getString(R.string.dialogItemChooserItems));

        inputRowValuesAndNamesId = (HashMap<Integer, String>) this.getIntent().getSerializableExtra(getString(R.string.inputRowValuesAndNamesId));
        inputSpinnerValuesAndNamesId = (HashMap<Integer, StringWithTag>) this.getIntent().getSerializableExtra(getString(R.string.inputSpinnerValuesAndNamesId));
        inputExpandedRows = (HashMap<String, HashMap<Integer, String>>) this.getIntent().getSerializableExtra(getString(R.string.inputExpandedRows));
        inputExpandedSpinnerRows = (HashMap<String, HashMap<Integer, StringWithTag>>) this.getIntent().getSerializableExtra(getString(R.string.inputExpandedSpinnerRows));

        PCBCalculator = new PCBCalculator(inputRowValuesAndNamesId, outputNames, inputSpinnerValuesAndNamesId, inputExpandedRows, dialogItemChooserItems, inputExpandedSpinnerRows);

        switch (selectedCalculationMethod) {
            case R.string.vibration_durable_name:
                outputNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.vibration_result_et_names)));
                outputUnitsOfMeasurement = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.vibration_result_et_units_of_measurement)));

                PCBCalculator.calculateVibrationDurable();
                wordFileName = getString(R.string.mechanical_impacts_vdurable_word_file_name);

                break;
            case R.string.vibration_stable_name:
                outputNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.vibration_result_et_names)));
                outputUnitsOfMeasurement = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.vibration_result_et_units_of_measurement)));

                PCBCalculator.calculateVibrationStable();
                wordFileName = getString(R.string.mechanical_impacts_vstable_word_file_name);
                break;
            case R.string.failure_rate_name:
                outputNames = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.failure_rate_result_et_names)));
                outputUnitsOfMeasurement = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.failure_rate_result_et_units_of_measurement)));

                 PCBCalculator.calculateFailureRate();

                wordFileName = getString(R.string.dependability_failure_rate_word_file_name);
                break;
        }

        outputValues = PCBCalculator.getOutputValues();
        wordItems = PCBCalculator.getWordStringValues();
    }

    private void initializeUI() {
        calculateUIHelper = new CalculateUIHelper(this);

        calculateUIHelper.initializeToolbar();

        calculateUIHelper.initializeResultButton(getResources().getString(R.string.save_to_microsoft_word));

        if (selectedCalculationMethod == R.string.failure_rate_name){

            if (PCBCalculator.hasAnyError()){
                calculateUIHelper.initializeResultText(getString(PCBCalculator.getResultTextId()),
                        PCBCalculator.isSuccessResult(), PCBCalculator.hasAnyError());
            }

        }else {
            calculateUIHelper.initializeResultText(getString(PCBCalculator.getResultTextId()),
                    PCBCalculator.isSuccessResult(), PCBCalculator.hasAnyError());
        }


        //for (int i = 0; i < outputNames.length; i++) {
        //ArrayList<CalcRowItem> resultArray = new ArrayList<>();
        for (int i = 0; i < outputValues.size(); i++) {
            //resultArray.add();
            calculateUIHelper.initializeRow(new CalcRowItem(outputNames.get(i), outputValues.get(i), outputUnitsOfMeasurement.get(i), true));
        }
        if (PCBCalculator.getChartItems() != null){
            calculateUIHelper.initializeChart(PCBCalculator.getChartItems());

        }
        //}
    }


    private void initializeListeners() {

        Button btnCalc = (Button) findViewById(R.id.footer_button);
        btnCalc.setOnClickListener(this);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android misc_toolbar.
                onBackPressed();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.footer_button:
                saveToWordDoc();
                break;
        }
    }

    private void saveToWordDoc() {
        dialogHelper = new DialogHelper(this);
        dialogHelper.initializeProgressDialog(getString(R.string.saving_in_progress));

        try {
            SaveTask saveTask = new SaveTask();
            saveTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
            dialogHelper.closeDialogWithProgress(getString(R.string.error) + e.toString());
        }
    }

    private class SaveTask extends AsyncTask<Void, Integer, Void> {
        private final String FOLDER_NAME = getString(R.string.save_folder_name);
        private String folderPath;


        @Override
        protected Void doInBackground(Void... params) {
            try {
                InputStream inputStream = getAssets().open(wordFileName);
                apachePOIHelper = new ApachePOIHelper();
                apachePOIHelper.openDocument(inputStream);

                for (WordItem item : wordItems) {
                    if (item.getValue() == null && item.getValueResourceId() != 0) {
                        apachePOIHelper.findAndReplace(getString(item.getKeyResourceId()), getString(item.getValueResourceId()));
                    } else {
                        if (item.getValue() != null) {
                            apachePOIHelper.findAndReplace(getString(item.getKeyResourceId()), item.getValue());

                        }
                    }
                }

                folderPath = apachePOIHelper.saveCurrentDocument(FOLDER_NAME, wordFileName);
                inputStream.close();
//               showToast(STATUS_SAVED);
//             publishProgress(STATUS_SAVED);
            } catch (Exception e) {
                e.printStackTrace();
                dialogHelper.closeDialogWithProgress(getString(R.string.error) + " " + e.toString());

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dialogHelper.closeDialogWithProgress(getString(R.string.saved_to_folder) + " " + folderPath
                    + "\n\n" + getString(R.string.file_name) + " " + wordFileName
                    , getString(R.string.open_file)
                    , folderPath + wordFileName);

        }
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            showToast(values[0]);
//
//        }

        private void showToast(final Integer status) {
            ResultDataActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ResultDataActivity.this, status, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void showToast(final String status) {
            ResultDataActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(ResultDataActivity.this, status, Toast.LENGTH_LONG).show();
                }
            });
        }


    }
}


