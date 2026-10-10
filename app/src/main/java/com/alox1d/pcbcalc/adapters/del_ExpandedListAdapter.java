package com.alox1d.pcbcalc.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.alox1d.pcbcalc.R;

import java.util.ArrayList;

public class del_ExpandedListAdapter extends BaseExpandableListAdapter {
    private ArrayList<ArrayList<String>> mGroups;
    private Context mContext;

    public del_ExpandedListAdapter(Context context, ArrayList<ArrayList<String>> groups) {
        mContext = context;
        mGroups = groups;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.del_misc_exp_listview_group, null);
        }

        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView tvGroup = (TextView) convertView.findViewById(R.id.tvExpListViewGroup);
        tvGroup.setText("Group " + Integer.toString(groupPosition));

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.del_misc_exp_listview_child, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.tvExpListViewChild);
        textChild.setText(mGroups.get(groupPosition).get(childPosition));


        EditText et = (EditText) convertView.findViewById(R.id.etExpListViewChild);
        TextWatcherPCB inputTextWatcher = new TextWatcherPCB(et);
        et.addTextChangedListener(inputTextWatcher);



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
