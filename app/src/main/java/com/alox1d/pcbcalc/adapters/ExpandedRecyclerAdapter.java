package com.alox1d.pcbcalc.adapters;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alox1d.pcbcalc.R;
import com.alox1d.pcbcalc.models.ComponentCorrectionCoeffItem;
import com.alox1d.pcbcalc.models.ExpandedRecyclerChildItem;
import com.alox1d.pcbcalc.models.ExpandedRecyclerParentItem;
import com.alox1d.pcbcalc.models.StringWithTag;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandedRecyclerAdapter extends RecyclerView.Adapter<ExpandedRecyclerAdapter.MyViewHolder> {
    private HashMap<String, HashMap<Integer, EditText>> expandedEditTextRows;
    private HashMap<String, HashMap<Integer, Spinner>> expandedSpinnerRows;
    private ArrayList<ExpandedRecyclerParentItem> expandedRecyclerParentItems;
    private HashMap<Integer, EditText> expandedEditTexts;
    private HashMap<Integer, Spinner> expandedSpinners;
    private HashMap<String, HashMap<Integer, Spinner>> expandedParentWithChildsSpinners;

    private static final String NOT_EXISTING_SIGNED_NUMBER = "-1.0";

    public ExpandedRecyclerAdapter(ArrayList<ExpandedRecyclerParentItem> expandedRecyclerParentItems, HashMap<String, HashMap<Integer, EditText>> expandedEditTextRows, HashMap<String, HashMap<Integer, Spinner>> expandedSpinnerRows) {
        this.expandedRecyclerParentItems = expandedRecyclerParentItems;
        this.expandedEditTexts = new HashMap<>();
        this.expandedParentWithChildsSpinners = new HashMap<>();
        this.expandedEditTextRows = expandedEditTextRows;
        this.expandedSpinnerRows = expandedSpinnerRows;
    }

    @Override
    public ExpandedRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_child_listing, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExpandedRecyclerAdapter.MyViewHolder holder, int position) {
        ExpandedRecyclerParentItem expandedRecyclerParentItem = expandedRecyclerParentItems.get(position);
        holder.textView_parentName.setText(expandedRecyclerParentItem.getParentName());
        //
        int noOfChildTextViews = holder.linearLayout_childItems.getChildCount();
        int noOfChild = expandedRecyclerParentItem.getChildDataItems().size();
        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {
                LinearLayout currentLL = (LinearLayout) holder.linearLayout_childItems.getChildAt(index);
                currentLL.setVisibility(View.GONE);
            }
        }
        expandedSpinners =  new HashMap<>();
        for (int viewIndex = 0; viewIndex < noOfChild; viewIndex++) {
            LinearLayout currentLL = (LinearLayout) holder.linearLayout_childItems.getChildAt(viewIndex);

                if (currentLL.getChildAt(1) instanceof TextInputLayout) {
//                    for (int childIndex = 0; childIndex<noOfChild; childIndex++) {
                    ExpandedRecyclerChildItem child = expandedRecyclerParentItem.getChildDataItems().get(viewIndex);
                    int childNameId = child.getChildNameId();

                        TextView tv = (TextView) currentLL.getChildAt(0);
                        tv.setText(childNameId);
                        TextInputLayout textInputLayout = (TextInputLayout) currentLL.getChildAt(1);
                        EditText editText = textInputLayout.getEditText();
                        editText.setText(child.getChildValue().toString());
                        expandedEditTexts.put(childNameId, editText);


                } else if (currentLL.getChildAt(1) instanceof Spinner) {

                        ExpandedRecyclerChildItem child = expandedRecyclerParentItem.getChildDataItems().get(viewIndex);
                        int childNameId = child.getChildNameId();
                        if (child.getChildValues() != null) {
                            TextView tv = (TextView) currentLL.getChildAt(0);
                            tv.setText(childNameId);

                            Spinner spinner = (Spinner) currentLL.getChildAt(1);
                            ArrayList<StringWithTag> values = child.getChildValues();
                            boolean hasImages = false;
                            boolean needListener = false;
                            for (StringWithTag item : values) {
                                if (item.getDrawableId() != 0) {
                                    hasImages = true;
                                }

                            }

                            int string = child.getChildNameId();
                            if (string == (R.string.component_type)){
                                needListener = true;
                            }

                            BaseAdapter adapter;
                            if (hasImages) {
                                adapter = new VibrationSpinnerAdapter(holder.context, values);
                            } else {
                                adapter = (ArrayAdapter) new ArrayAdapter<StringWithTag>
                                        (holder.context, android.R.layout.simple_spinner_dropdown_item, values); // uses toString method on every StringWithTag item (idk why)
                            }
                            if (needListener){
                                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            ArrayList<ComponentCorrectionCoeffItem> coeffItems = // TODO !!! 05 06 18
                                                    (ArrayList<ComponentCorrectionCoeffItem>) values.get(position).getTag();
                                            ArrayList<StringWithTag> stringWithTags = new ArrayList<>();
                                            for (ComponentCorrectionCoeffItem array : coeffItems) {
                                                if (array.getTemperature().toString().equals(NOT_EXISTING_SIGNED_NUMBER)){
                                                    stringWithTags.add(new StringWithTag(array, holder.context.getString(R.string.not_required)));
                                                } else {
                                                    stringWithTags.add(new StringWithTag(array, array.getTemperature().toString()));
                                                }
                                            }
                                            HashMap<Integer, Spinner> parentWithChilds = expandedParentWithChildsSpinners.get(expandedRecyclerParentItem.getParentName());
                                            Spinner tempSpinner = parentWithChilds.get(R.string.surface_temperature_of_the_components);
                                            ArrayAdapter adapter = new ArrayAdapter<StringWithTag>
                                                    (holder.context, android.R.layout.simple_spinner_dropdown_item, stringWithTags);
                                            tempSpinner.setAdapter(adapter);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                            spinner.setAdapter(adapter);
                            spinner.setPromptId(childNameId);
                            expandedSpinners.put(childNameId, spinner);
                    }}

        }
        expandedParentWithChildsSpinners.put(expandedRecyclerParentItem.getParentName(),expandedSpinners);
    }

    @Override
    public int getItemCount() {
        return expandedRecyclerParentItems.size();
    }

    public void removeAt(int position) {
        expandedRecyclerParentItems.remove(position);
        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, expandedRecyclerParentItems.size());
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Context context;
        private TextView textView_parentName;
        private ImageButton imageButton_closeParent;
        private LinearLayout linearLayout_childItems;

        MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            textView_parentName = itemView.findViewById(R.id.tv_parentName);
            imageButton_closeParent = itemView.findViewById(R.id.ib_closeParent);
            linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);
//            linearLayout_childItems.setVisibility(View.GONE);
            int intMaxNoOfChild = 0;
            for (int index = 0; index < expandedRecyclerParentItems.size(); index++) {
                int intMaxSizeTemp = expandedRecyclerParentItems.get(index).getChildDataItems().size();
                if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) { // TODO сделать if (for?) для создание tved и spinner отдельно
//                TextView textView = new TextView(context);
//                textView.setId(indexView);
//                textView.setPadding(0, 20, 0, 20);
//                textView.setGravity(Gravity.CENTER);
//                textView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.background_sub_module_text));
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                textView.setOnClickListener(this);
//                linearLayout_childItems.addView(textView, layoutParams);
                    ExpandedRecyclerChildItem child = expandedRecyclerParentItems.get(0).getChildDataItems().get(indexView); // TODO ???

                    LinearLayout linearRow = new LinearLayout(context);
                    LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    int px = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            16,
                            context.getResources().getDisplayMetrics()
                    ); // перевод dp в px
                    parentParams.setMargins(px, px, px, px); // принимают только в пикселях, поэтому нужен перевод
                if (child.getChildValue() != null){
                        linearRow.setLayoutParams(parentParams);
                        linearRow.setWeightSum(1f);
                        linearRow.setOrientation(LinearLayout.HORIZONTAL);
                        linearRow.setGravity(Gravity.CENTER_VERTICAL);
                        TextView tv = new TextView(context);
                        LinearLayout.LayoutParams tv_etParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
                        tv.setLayoutParams(tv_etParams);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                context.getResources().getDimension(R.dimen.input_font));
                        linearRow.addView(tv);
                        final TextInputLayout textInputLayout = new TextInputLayout(context);
                        final EditText etPCB = new EditText(context);
                        LinearLayout.LayoutParams etParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
                        textInputLayout.setLayoutParams(etParams);
                        etPCB.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                        TextWatcherPCB inputTextWatcher = new TextWatcherPCB(etPCB);
                        etPCB.addTextChangedListener(inputTextWatcher);
                        textInputLayout.addView(etPCB);
                        linearRow.addView(textInputLayout);
                        linearLayout_childItems.addView(linearRow);
                    } else if (child.getChildValues() != null){
                         linearRow = new LinearLayout(context);
                        linearRow.setLayoutParams(parentParams);
                        linearRow.setWeightSum(1f);
                        linearRow.setOrientation(LinearLayout.HORIZONTAL);
                        linearRow.setGravity(Gravity.CENTER_VERTICAL);
                        TextView tv = new TextView(context);
                        LinearLayout.LayoutParams tv_spinnerParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
                        tv.setLayoutParams(tv_spinnerParams);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                context.getResources().getDimension(R.dimen.input_font));
                        linearRow.addView(tv);
                        Spinner spinner = new Spinner(context);
                        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // a view of spinner: with radio buttons or without
                        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
                        spinner.setLayoutParams(spinnerParams);
                        linearRow.addView(spinner);
                        linearLayout_childItems.addView(linearRow);
                    }

            }
            textView_parentName.setOnClickListener(this);
            imageButton_closeParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.tv_parentName) {
                if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                    linearLayout_childItems.setVisibility(View.GONE);
                } else {
                    linearLayout_childItems.setVisibility(View.VISIBLE);
                }
            } else if (view.getId() == R.id.ib_closeParent) {
                int currentPos = getAdapterPosition();
                String parentName = expandedRecyclerParentItems.get(currentPos).getParentName();
                removeExpandedEditTexts(parentName);
                removeExpandedSpinners(parentName);
                removeAt(currentPos);
//                TextView tv = view.findViewById(R.id.tv_parentName);
            } else {
                TextView textViewClicked = (TextView) view;
                Toast.makeText(context, "" + textViewClicked.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void removeExpandedEditTexts(String parentName) {
        expandedEditTextRows.remove(parentName);
    }

    private void removeExpandedSpinners(String parentName) {
        expandedParentWithChildsSpinners.remove(parentName);
    }

    public HashMap<Integer, EditText> getExpandedEditTexts() {
        return expandedEditTexts;
    }

    public HashMap<String,HashMap<Integer, Spinner>> getExpandedParentWithChildsSpinners() {
        return expandedParentWithChildsSpinners;

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
//            String newInput = input.replaceFirst("(^[0.]+(?!$))|(^\\.)", "");
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
            //Log.d("calc",String.valueOf(Double.parseDouble(newInputAllCondition)));
        }
    }
}
