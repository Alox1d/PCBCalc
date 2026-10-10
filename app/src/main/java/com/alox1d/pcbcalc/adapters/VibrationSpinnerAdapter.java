package com.alox1d.pcbcalc.adapters;

/**
 * Created by Alox1d on 17.04.2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alox1d.pcbcalc.R;
import com.alox1d.pcbcalc.models.StringWithTag;

import java.util.ArrayList;

public class VibrationSpinnerAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater mInflater;
    private Integer[] imagesIds;
    private ArrayList<StringWithTag> rows;


    public VibrationSpinnerAdapter(Context context, ArrayList<StringWithTag> rows) {
        mInflater = LayoutInflater.from(context);

        int iteratorImage = 0;
        Integer[] imagesIds = new Integer[rows.size()];
        for (StringWithTag item : rows) {
            if (item.getDrawableId() != 0) {
                imagesIds[iteratorImage++] = item.getDrawableId();
                //hasImages = true;
            }
        }
//        if (hasImages) this.imagesIds = imagesIds;
        this.imagesIds = imagesIds;
        this.context = context;
        this.rows = rows;
    }

    public int getCount() {
        return imagesIds.length;
    }

    @Override
    public StringWithTag getItem(int position) {
        return rows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder imageViewHolder;
//            do we have a view
        if (convertView == null) {
//              we don't have a view so create one by inflating the layout
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.misc_spinner_image, parent, false);

            imageViewHolder = new ViewHolder();
            imageViewHolder.imageView
                    = (ImageView) itemView.findViewById(R.id.spinnerImage);
//              set the tag for this view to the current image view holder
            itemView.setTag(imageViewHolder);

        } else {
//              we have a view so get the tagged view
            imageViewHolder = (ViewHolder) itemView.getTag();
        }

//          display the current  image
        imageViewHolder.imageView.setImageResource(imagesIds[position]);

        return itemView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}