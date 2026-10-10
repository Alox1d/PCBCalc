package com.alox1d.pcbcalc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alox1d.pcbcalc.R;
import com.alox1d.pcbcalc.models.StringWithTag;

import java.util.ArrayList;

public class StringWithTagListViewAdapter extends BaseAdapter{
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<StringWithTag> stringsWithTag;

    StringWithTagListViewAdapter(Context context, ArrayList<StringWithTag> stringsWithTag) {
        ctx = context;
        this.stringsWithTag = stringsWithTag;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return stringsWithTag.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return stringsWithTag.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.misc_listview_item, parent, false);
        }

        StringWithTag stringWithTag = getStringWithTag(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.tvListViewItem)).setText(stringWithTag.toString());

        return view;
    }

    // товар по позиции
    StringWithTag getStringWithTag(int position) {
        return ((StringWithTag) getItem(position));
    }



}
