package com.alox1d.pcbcalc.ui;

import android.content.res.Resources;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alox1d.pcbcalc.R;
import com.alox1d.pcbcalc.adapters.ExpandedRecyclerAdapter;
import com.alox1d.pcbcalc.adapters.VibrationSpinnerAdapter;
import com.alox1d.pcbcalc.animations.AppearanceAnimation;
import com.alox1d.pcbcalc.models.CalcRowItem;
import com.alox1d.pcbcalc.models.ChartItem;
import com.alox1d.pcbcalc.models.ExpandedRecyclerChildItem;
import com.alox1d.pcbcalc.models.ExpandedRecyclerParentItem;
import com.alox1d.pcbcalc.models.SpinnerRowItem;
import com.alox1d.pcbcalc.models.StringWithTag;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alox1d on 06.04.2018.
 */

public class CalculateUIHelper {

    //    private static CalculateUIHelper instance;
    private Resources r;
    private AppCompatActivity appCompatActivity;

    private ExpandedRecyclerAdapter expandedRecyclerAdapter;
    private ArrayList<ExpandedRecyclerParentItem> expandedRecyclerParentItems;

    //    public static CalculateUIHelper getInstance(AppCompatActivity appCompatActivity) {
//        if (instance == null) {
//            instance = new CalculateUIHelper(appCompatActivity);
//        }
//        return instance;
//    }
    public CalculateUIHelper(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        r = appCompatActivity.getResources();
    }


    public void initializeToolbar() {

        Toolbar toolbar = (Toolbar) appCompatActivity.findViewById(R.id.toolbar);
        appCompatActivity.setSupportActionBar(toolbar); // for support?
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private Spinner initializeRow(int nameId, ArrayList<StringWithTag> rows) {

        LinearLayout linearRow = new LinearLayout(appCompatActivity);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                r.getDisplayMetrics()
        );
        parentParams.setMargins(px, px, px, px);
        linearRow.setLayoutParams(parentParams);
        linearRow.setWeightSum(1f);
        linearRow.setOrientation(LinearLayout.HORIZONTAL);
        linearRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView tvPCB = new TextView(appCompatActivity);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        tvPCB.setLayoutParams(tvParams);
        tvPCB.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                appCompatActivity.getResources().getDimension(R.dimen.input_font));
        tvPCB.setText(nameId);
        linearRow.addView(tvPCB);

//        List<StringWithTag> itemList = new ArrayList<StringWithTag>();
//        for (Map.Entry<Integer, String> entry : rows.entrySet()) {
//            Integer key = entry.getKey();
//            String value = entry.getValue();
//
//  /* Build the StringWithTag List using these keys and values. */
//            itemList.add(new StringWithTag(value, key));
//        }
        boolean hasImages = false;
        for (StringWithTag item : rows) {
            if (item.getDrawableId() != 0) {
                hasImages = true;
            }
        }
        BaseAdapter adapter;
        if (hasImages) {
            adapter = new VibrationSpinnerAdapter(appCompatActivity, rows); // todo единый адаптер?
        } else {
            adapter = (ArrayAdapter) new ArrayAdapter<StringWithTag>
                    (appCompatActivity, android.R.layout.simple_spinner_dropdown_item, rows); // uses toString method on every StringWithTag item (idk why)
        }
        Spinner spinner = new Spinner(appCompatActivity);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // a view of spinner: with radio buttons or without
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        spinner.setLayoutParams(spinnerParams);
        spinner.setAdapter(adapter);
        spinner.setPromptId(nameId);
        linearRow.addView(spinner);

        LinearLayout inner = (LinearLayout) appCompatActivity.findViewById(R.id.layout_inner);
        inner.addView(linearRow);
        return spinner;
    }

    private EditText initializeRow(int nameId, double defaultValue, String hint) {

        LinearLayout linearRow = new LinearLayout(appCompatActivity);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                r.getDisplayMetrics()
        ); // перевод dp в px
        parentParams.setMargins(px, px, px, px); // принимают только в пикселях, поэтому нужен перевод
        linearRow.setLayoutParams(parentParams);
//        if (helpImageId != 0){ // help-circle
//            linearRow.setWeightSum(1.1f);
//        } else {
//            linearRow.setWeightSum(1f);
//        }
        linearRow.setOrientation(LinearLayout.HORIZONTAL);
        linearRow.setGravity(Gravity.CENTER_VERTICAL);

        //linearRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider_in_width)); // программный разделитель
        //linearRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE); // разделитель между каждым item'ом в группе

        TextView tvPCB = new TextView(appCompatActivity);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.6f);
        //tvParams.gravity = Gravity.CENTER_VERTICAL; // разница: setGravity = gravity, tv.gravity = layout_gravity
        //countPCB.setGravity(Gravity.CENTER_VERTICAL); // разница: setGravity = gravity, tv.gravity = layout_gravity
        tvPCB.setLayoutParams(tvParams);
        tvPCB.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                appCompatActivity.getResources().getDimension(R.dimen.input_font));
        tvPCB.setText(nameId);
        linearRow.addView(tvPCB);

//        if (helpImageId != 0){ // help-circle
//            ImageButton helpImage = new ImageButton(appCompatActivity);
//            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            helpImage.setLayoutParams(imageParams);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                helpImage.setBackground(null);
//            } else {
//                helpImage.setBackgroundDrawable(null);
//            }
//            helpImage.setImageResource(helpImageId);
//            linearRow.addView(helpImage);
//        }

        final TextInputLayout textInputLayout = new TextInputLayout(appCompatActivity);
        final EditText etPCB = new EditText(appCompatActivity);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        textInputLayout.setLayoutParams(etParams);
        etPCB.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        etPCB.setText((Double.toString(defaultValue)));
        textInputLayout.setHint(Html.fromHtml(hint));
        TextWatcherPCB inputTextWatcher = new TextWatcherPCB(etPCB);
        etPCB.addTextChangedListener(inputTextWatcher);
        textInputLayout.addView(etPCB);
        linearRow.addView(textInputLayout);

        LinearLayout inner = (LinearLayout) appCompatActivity.findViewById(R.id.layout_inner);
        inner.addView(linearRow);
        return etPCB;
    }

    private void initializeRow(String rowName, double defaultValue, String hint, boolean isEditTextBlocked) {


        LinearLayout linearRow = new LinearLayout(appCompatActivity);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                r.getDisplayMetrics()
        ); // перевод dp в px
        parentParams.setMargins(px, px, px, px); // принимают только в пикселях, поэтому нужен перевод
        linearRow.setLayoutParams(parentParams);
        linearRow.setWeightSum(1f);
        linearRow.setOrientation(LinearLayout.HORIZONTAL);
        linearRow.setGravity(Gravity.CENTER_VERTICAL);

        //linearRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider_in_width)); // программный разделитель
        //linearRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE); // разделитель между каждым item'ом в группе

        TextView tvPCB = new TextView(appCompatActivity);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
        //tvParams.gravity = Gravity.CENTER_VERTICAL; // разница: setGravity = gravity, tv.gravity = layout_gravity
        //countPCB.setGravity(Gravity.CENTER_VERTICAL); // разница: setGravity = gravity, tv.gravity = layout_gravity
        tvPCB.setLayoutParams(tvParams);
        tvPCB.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                appCompatActivity.getResources().getDimension(R.dimen.input_font));
        tvPCB.setText(rowName);
        linearRow.addView(tvPCB);

        final TextInputLayout textInputLayout = new TextInputLayout(appCompatActivity);
        final EditText etPCB = new EditText(appCompatActivity);
        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
        textInputLayout.setLayoutParams(etParams);
        etPCB.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        etPCB.setText((Double.toString(defaultValue)));
        textInputLayout.setHint(Html.fromHtml(hint));
        TextWatcherPCB inputTextWatcher = new TextWatcherPCB(etPCB);
        etPCB.addTextChangedListener(inputTextWatcher);
        if (isEditTextBlocked) {
            etPCB.setInputType(InputType.TYPE_NULL);
            etPCB.setTextIsSelectable(true);
            etPCB.setKeyListener(null);
        }
        textInputLayout.addView(etPCB);
        linearRow.addView(textInputLayout);

        LinearLayout inner = (LinearLayout) appCompatActivity.findViewById(R.id.layout_inner);
        inner.addView(linearRow);
    }

    public void initializeResultButton(String text) {
        Button footerButton;
        footerButton = (Button) appCompatActivity.findViewById(R.id.footer_button);

        footerButton.setText(text);

    }

    public void initializeResultText(String message, boolean isSuccessResult, boolean error) { //todo вывод текста для каждого

        LinearLayout footer = (LinearLayout) appCompatActivity.findViewById(R.id.footer_rl);
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                r.getDisplayMetrics());
        tvParams.setMargins(0, 0, 0, px + px); // принимают только в пикселях, поэтому нужен перевод

        TextView tvAboveFooterButton = (TextView) LayoutInflater.from(appCompatActivity).inflate(R.layout.misc_textview_result, null);
        tvAboveFooterButton.setText(message);

        if (error) {
            tvAboveFooterButton.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorError));
        } else {
            if (isSuccessResult) {
                tvAboveFooterButton.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorPrimary));
            } else {
                tvAboveFooterButton.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorError));
            }
        }

        footer.addView(tvAboveFooterButton, 0, tvParams);
        appearanceAnimate(tvAboveFooterButton, 2);

    }

    private void appearanceAnimate(View view, int countOfTimes) {
//        AnimationSet as = new AnimationSet(true);
//        AlphaAnimation animation1 = new AlphaAnimation(1, 0); // анимация появления футера (кнопки)
//        animation1.setDuration(1000);
//        AlphaAnimation animation2 = new AlphaAnimation(0, 1); // анимация появления футера (кнопки)
//        animation2.setStartOffset(1000);
//        animation2.setDuration(1000);

        AppearanceAnimation animation = new AppearanceAnimation(countOfTimes);
        animation.animate(1, 0, view);
    }

    public HashMap<Integer, EditText> initializeTvEdRows(ArrayList<CalcRowItem> calcRowItems) {
        HashMap<Integer, EditText> etPCBs = new HashMap<>();
        for (CalcRowItem calcRowItem : calcRowItems) {
            EditText et = initializeRow(calcRowItem.getNameId(), calcRowItem.getValue(), calcRowItem.getUnitOfMeasurement());
            etPCBs.put(calcRowItem.getNameId(), et);

        }
        return etPCBs;
    }

    public HashMap<Integer, Spinner> initializeSpinnerRows(ArrayList<SpinnerRowItem> spinnerRowItems) {
        HashMap<Integer, Spinner> spinnerPCBs = new HashMap<>();

        for (SpinnerRowItem spinnerRowItem : spinnerRowItems) {
            Spinner spinner = initializeRow(spinnerRowItem.getNameId(), spinnerRowItem.getValues());
            spinnerPCBs.put(spinnerRowItem.getNameId(), spinner);
        }
        return spinnerPCBs;
    }


    public void initializeRow(CalcRowItem calcRowItem) {
        initializeRow(calcRowItem.getName(), calcRowItem.getValue(), calcRowItem.getUnitOfMeasurement(), calcRowItem.isEditTextBlocked());

    }

    public void initializeDialogItemChooserButton(int dialogButtonNameId, final View.OnClickListener onItemClickListener) {

        LinearLayout linearRow = new LinearLayout(appCompatActivity);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                r.getDisplayMetrics()
        ); // перевод dp в px
        parentParams.setMargins(px, px, px, px); // принимают только в пикселях, поэтому нужен перевод
        linearRow.setLayoutParams(parentParams);
        linearRow.setOrientation(LinearLayout.HORIZONTAL);
        linearRow.setGravity(Gravity.CENTER_VERTICAL);

        //linearRow.setDividerDrawable(getResources().getDrawable(R.drawable.divider_in_width)); // программный разделитель
        //linearRow.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE); // разделитель между каждым item'ом в группе
        int buttonStyle = R.style.Widget_AppCompat_Button_Colored;
        Button button = new Button(new ContextThemeWrapper(appCompatActivity, buttonStyle), null, buttonStyle);
        LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        button.setLayoutParams(btnParams);
//                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, appCompatActivity.getResources().getDimension(R.dimen.input_font));
        button.setText(dialogButtonNameId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClick(view);
//                DialogHelper alertHelper = new DialogHelper(appCompatActivity);
//                alertHelper.initializeItemChooserDialog(dialogItemChooserItems, new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    }
//                });
            }
        });
        linearRow.addView(button);

        LinearLayout inner = (LinearLayout) appCompatActivity.findViewById(R.id.layout_inner);
        inner.addView(linearRow);


    }

    public void initializeExpandedRecycler(ArrayList<ExpandedRecyclerParentItem> parentItems, HashMap<String, HashMap<Integer, EditText>> expandedEditTextRows, HashMap<String, HashMap<Integer, Spinner>> expandedSpinnerRows) {
//        LinearLayout linearRow = new LinearLayout(appCompatActivity);
//        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        int px = (int) TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                16,
//                r.getDisplayMetrics()
//        ); // перевод dp в px
//        parentParams.setMargins(px, px, px, px); // принимают только в пикселях, поэтому нужен перевод
//        linearRow.setLayoutParams(parentParams);
//        linearRow.setWeightSum(1f);
//        linearRow.setOrientation(LinearLayout.HORIZONTAL);

//        ExpandableListView expandableView = new ExpandableListView(appCompatActivity);
//        LinearLayout.LayoutParams elvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        expandableView.setLayoutParams(elvParams);
//    expandableView.setVerticalScrollBarEnabled(true);
//        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
//        ArrayList<String> children1 = new ArrayList<String>();
//        ArrayList<String> children2 = new ArrayList<String>();
//        children1.add("Child_1");
//        children1.add("Child_2");
//        groups.add(children1);
//        children2.add("Child_1");
//        children2.add("Child_2");
//        children2.add("Child_3");
//        groups.add(children2);
//        //Создаем адаптер и передаем context и список с данными
//        del_ExpandedListAdapter adapter = new del_ExpandedListAdapter(appCompatActivity, groups);
//        expandableView.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(expandableView);

//        linearRow.addView(expandableListView);

//        RecyclerView mRecyclerView = new RecyclerView(appCompatActivity);
//        ExpandedRecyclerAdapter expandedRecyclerAdapter = new ExpandedRecyclerAdapter(getDummyDataToPass());
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));
//        mRecyclerView.setAdapter(expandedRecyclerAdapter);
//        mRecyclerView.setHasFixedSize(true);
//        LinearLayout inner = (LinearLayout) appCompatActivity.findViewById(R.id.layout_inner);
//        inner.addView(mRecyclerView);

//        parentItems = new ArrayList<>();
        this.expandedRecyclerParentItems = parentItems;
        RecyclerView mRecyclerView = appCompatActivity.findViewById(R.id.inner_rv_input_data_layout);
        expandedRecyclerAdapter = new ExpandedRecyclerAdapter(expandedRecyclerParentItems, expandedEditTextRows, expandedSpinnerRows);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));
        mRecyclerView.setAdapter(expandedRecyclerAdapter);

    }

    public void insertItemToExpandedRecycler(
            String parentName,
            HashMap<Integer, Double> tvedChilds, ArrayList<SpinnerRowItem> spinnerChilds) {
        ArrayList<ExpandedRecyclerChildItem> childItems;
        ExpandedRecyclerParentItem expandedRecyclerParentItem;
        ExpandedRecyclerChildItem expandedRecyclerChildItem = null;
        /////////
        expandedRecyclerParentItem = new ExpandedRecyclerParentItem();
        expandedRecyclerParentItem.setParentName(parentName);
//        expandedRecyclerParentItem.setParentName(stringWithTagItems.get(position).toString());
        childItems = new ArrayList<>();
        //
        for (Map.Entry<Integer, Double> entry : tvedChilds.entrySet()) {
            expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
            expandedRecyclerChildItem.setChildNameId(entry.getKey());
            expandedRecyclerChildItem.setChildValue(entry.getValue());

            childItems.add(expandedRecyclerChildItem);
//                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//                    expandedRecyclerChildItem.setChildNameId("Количество");
//                    expandedRecyclerChildItem.setChildValue(1.0);
//                    childItems.add(expandedRecyclerChildItem);
//                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//                    expandedRecyclerChildItem.setChildNameId("Поправочный коэффициент");
//                    expandedRecyclerChildItem.setChildValue(55.0);
        }
        for (SpinnerRowItem item : spinnerChilds) { // todo распарсить childs и добавить в childSpinnerы
            expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
            expandedRecyclerChildItem.setChildNameId(item.getNameId());
            expandedRecyclerChildItem.setChildValues(item.getValues());
            childItems.add(expandedRecyclerChildItem);
//                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//                    expandedRecyclerChildItem.setChildNameId("Количество");
//                    expandedRecyclerChildItem.setChildValue(1.0);
//                    childItems.add(expandedRecyclerChildItem);
//                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//                    expandedRecyclerChildItem.setChildNameId("Поправочный коэффициент");
//                    expandedRecyclerChildItem.setChildValue(55.0);
        }
        //
        expandedRecyclerParentItem.setChildDataItems(childItems);
        expandedRecyclerParentItems.add(expandedRecyclerParentItem);
        expandedRecyclerAdapter.notifyItemInserted(expandedRecyclerParentItems.size() - 1);


    }
//    public void insertItemToExpandedRecycler(
//            String parentName, ArrayList<SpinnerRowItem> childs) {
//        ArrayList<ExpandedRecyclerChildItem> childItems;
//        ExpandedRecyclerParentItem expandedRecyclerParentItem;
//        ExpandedRecyclerChildItem expandedRecyclerChildItem = null;
//        /////////
//        expandedRecyclerParentItem = new ExpandedRecyclerParentItem();
//        expandedRecyclerParentItem.setParentName(parentName);
////        expandedRecyclerParentItem.setParentName(stringWithTagItems.get(position).toString());
//        childItems = new ArrayList<>();
//        //
//        for (SpinnerRowItem item : childs) { // todo распарсить childs и добавить в childSpinnerы
//            expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//            expandedRecyclerChildItem.setChildNameId(item.getNameId());
//            expandedRecyclerChildItem.setChildValues(item.getValues());
//            childItems.add(expandedRecyclerChildItem);
////                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
////                    expandedRecyclerChildItem.setChildNameId("Количество");
////                    expandedRecyclerChildItem.setChildValue(1.0);
////                    childItems.add(expandedRecyclerChildItem);
////                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
////                    expandedRecyclerChildItem.setChildNameId("Поправочный коэффициент");
////                    expandedRecyclerChildItem.setChildValue(55.0);
//        }
//        //
//        expandedRecyclerParentItem.setChildDataItems(childItems);
//        expandedRecyclerParentItems.add(expandedRecyclerParentItem);
//
//    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public HashMap<Integer, EditText> getExpandedEditTexts() {
        return expandedRecyclerAdapter.getExpandedEditTexts();
    }

    public HashMap<String,HashMap<Integer,Spinner>> getExpandedSpinners() {
        return expandedRecyclerAdapter.getExpandedParentWithChildsSpinners();

    }


    public void initializeChart(ArrayList<ChartItem> chartItems) {
        int height = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                300,
                r.getDisplayMetrics()
        ); // перевод dp в px
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        int margin = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                16,
                r.getDisplayMetrics()
        ); // перевод dp в px
        parentParams.setMargins(margin, margin, margin, margin); // принимают только в пикселях, поэтому нужен перевод



        LineChart chart = new LineChart(appCompatActivity);
        chart.setLayoutParams(parentParams);





        List<Entry> entries = new ArrayList<Entry>();

        for (ChartItem data : chartItems) {

            // turn your data into Entry objects
            entries.add(new Entry((float)data.getX(), (float)data.getY()));
        }

        LineDataSet dataSet = new LineDataSet(entries, appCompatActivity.getString(R.string.chart_probability_of_failure_free_work)); // add entries to dataset
        dataSet.setColor(r.getColor(R.color.colorAccent));
//        dataSet.setValueTextColor(R.color.colorAccent); // styling, ...
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setLabelRotationAngle(45f);

        chart.getDescription().setEnabled(false);

        chart.getLegend().setWordWrapEnabled(true);

        chart.animateX(3000);
        chart.invalidate(); // refresh

        LinearLayout inner = (LinearLayout) appCompatActivity.findViewById(R.id.layout_inner);
        inner.addView(chart);
    }

    private class TextWatcherPCB implements TextWatcher {

        private EditText editText;

        private TextWatcherPCB(EditText et) {
            super();
            editText = et;
        }

        public void afterTextChanged(Editable s) {


        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String input = s.toString();
            String inputCheckAllConditions = input.replaceFirst("(^[0.]+(?!$))|(^\\.)", "");
            if (!inputCheckAllConditions.equals(input)) {
                String inputCheckZeroDotCond = input.replaceFirst("^(0\\.)", "");
                if (inputCheckZeroDotCond.equals(input)) {
                    editText.removeTextChangedListener(this);
                    editText.setText(inputCheckAllConditions);
                    editText.addTextChangedListener(this);
                }
            }
            try {
                double number = Double.parseDouble(inputCheckAllConditions);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                editText.setError("Введено неверное значение");
            }
        }
        //            String newInput = input.replaceFirst("(^[0.]+(?!$))|(^\\.)", "");
        //Log.d("calc",String.valueOf(Double.parseDouble(newInputAllCondition)));

    }


}
//BACKUP RESULT TEXT
//boolean error = vibrationCalculator.hasAnyError();
//    boolean isVibrationDurable = vibrationCalculator.isVibrationDurable();
//    boolean isVibrationStable = vibrationCalculator.isVibrationStable();
//    LinkedHashMap<LinearLayout.LayoutParams, TextView> textViews = new LinkedHashMap<>();
//    //footerButton = (Button) appCompatActivity.findViewById(R.id.footer_button);
//
////        TextView tvAboveFooterButton = new TextView(appCompatActivity);
////        //tvParams.addRule(RelativeLayout.CENTER_VERTICAL, footerButton.getId()); // НЕ ХОЧЕТ ЛЕЗТЬ НАВЕРХ - решилось использованием linearlayout
////        tvAboveFooterButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,
////                r.getDimension(R.dimen.input_font));
////        tvAboveFooterButton.setTypeface(null, Typeface.BOLD);
////        tvAboveFooterButton.setGravity(Gravity.CENTER_HORIZONTAL);
//
//    LinearLayout footer = (LinearLayout) appCompatActivity.findViewById(R.id.footer_rl);
//    LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//    int px = (int) TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            16,
//            r.getDisplayMetrics());
//
//
//
//    TextView tvAboveFooterButton = (TextView) LayoutInflater.from(appCompatActivity).inflate(R.layout.misc_textview_result, null);
//    TextView tvVibrationDurable = (TextView) LayoutInflater.from(appCompatActivity).inflate(R.layout.misc_textview_result, null);
//
//
//        if (error) {
//
//                tvAboveFooterButton.setText(R.string.error_result);
//                tvAboveFooterButton.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorError));
//
//                tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                tvParams.setMargins(0, 0, 0, px); // принимают только в пикселях, поэтому нужен перевод
//
//                textViews.put(tvParams, tvAboveFooterButton);
//
//
//                } else {
//
//
//                if (isVibrationDurable) {
//                tvAboveFooterButton.setText(R.string.successful_vibration_durable);
//                tvAboveFooterButton.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorPrimary));
//
//                tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                tvParams.setMargins(0, px, 0, px+px); // view from bottom need marginTop and marginBottom (this is 2nd view, because method addView adds 1st elemnt of LinkedHashMap to zero position)
//
//                textViews.put(tvParams, tvAboveFooterButton);
//                } else {
//                tvAboveFooterButton.setText(R.string.unsuccessful_vibration_durable);
//                tvAboveFooterButton.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorError));
//
//                tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                tvParams.setMargins(0, px, 0, px+px); // idk why, but just px do nothing with marginBottom
//
//                textViews.put(tvParams, tvAboveFooterButton);
//                }
//                if (isVibrationStable) {
//                tvVibrationDurable.setText(R.string.sucessful_vibration_stable);
//                tvVibrationDurable.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorPrimary));
//
//                tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                tvParams.setMargins(0, 0, 0, px); // view from top need only marginBottom (this is 1st view, because method addView adds 1st elemnt of LinkedHashMap to zero position)
//
//                textViews.put(tvParams, tvVibrationDurable);
//                } else {
//                tvVibrationDurable.setText(R.string.unsuccessful_vibration_stable);
//                tvVibrationDurable.setTextColor(ContextCompat.getColor(appCompatActivity, R.color.colorError));
//
//                tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                tvParams.setMargins(0, 0, 0, px); // принимают только в пикселях, поэтому нужен перевод
//
//                textViews.put(tvParams, tvVibrationDurable);
//                }
//                }
//
//
//                for (Map.Entry entry : textViews.entrySet()){
//                tvParams = (LinearLayout.LayoutParams) entry.getKey();
//                TextView textView = (TextView) entry.getValue();
//                footer.addView(textView, 0, tvParams);
//                appearanceAnimate(textView, 2);
//                }