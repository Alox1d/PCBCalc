package com.alox1d.pcbcalc.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alox1d.pcbcalc.R;
import com.alox1d.pcbcalc.models.MainCalcItem;

import java.util.List;


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.CalcViewHolder> {
    private List<MainCalcItem> mainCalcItems;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, MainCalcItem item);
    }

    static class CalcViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView calcName;
        ImageView calcImage;

        CalcViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.main_card_view);
            calcName = (TextView) itemView.findViewById(R.id.calc_name);
            calcImage = (ImageView) itemView.findViewById(R.id.calc_image);
        }
        private void bind(final CalcViewHolder holder, final MainCalcItem item, final OnItemClickListener listener) {

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(v, item);
                }
            });
        }
    }

    public MainRecyclerViewAdapter(List<MainCalcItem> mainCalcItems, OnItemClickListener listener) {
        this.mainCalcItems = mainCalcItems;
        this.listener = listener;

    }

    @Override
    public CalcViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.misc_main_cardview_item, parent, false);

        CalcViewHolder calcViewHolder = new CalcViewHolder(view);
        return calcViewHolder;
    }

    @Override
    public void onBindViewHolder(final CalcViewHolder holder, final int position) {
        holder.bind(holder, mainCalcItems.get(position),listener);

        holder.calcName.setText(mainCalcItems.get(position).getNameId());
        holder.calcImage.setImageResource(mainCalcItems.get(position).getImageId());
//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//
//                switch (v.getId()) {
//                    case R.id.main_card_view:
//                        //holder.calcName.getText().toString()
//                        Intent myIntent;
//
//
//                        //String calcName = holder.calcName.getResources().getResourceEntryName(nameId);
//                        //if (calcName.equals(v.getContext().getResources().getResourceEntryName(R.string.vibration_name))) {
//                        Integer calcNameId = mainCalcItems.get(position).getNameId();
//                        if (calcNameId.equals(R.string.mechanical_impacts_name)) {
//                            mainCalcItems.clear();
//                            mainCalcItems.addAll(mechanicalImpactItems);
//                            myIntent = new Intent(v.getContext(), MechanicalImpactsActivity.class);
////                            myIntent = new Intent(v.getContext(), MechanicalImpactsActivity.class);
//                            v.getContext().startActivity(myIntent);
//
//                        } else if (calcNameId.equals(R.string.about_program)) {
//                            AlertDialogHelper alertHelper = new AlertDialogHelper(v.getContext());
//                            alertHelper.initializeAboutDialog();
//
//                            //myIntent = new Intent(v.getContext(), DelAboutProgramActivity.class);
//                            //v.getContext().startActivity(myIntent);
//
//
//                        } else if (calcNameId.equals(R.string.vibration_durable_name)){
//                        } else {
//                            Toast.makeText(v.getContext(), mainCalcItems.get(position).getNameId(), Toast.LENGTH_LONG).show();
//
//                        }
//
//                        break;
//                }
//            }
//
//
//        });
    }



    @Override
    public int getItemCount() {
        return mainCalcItems.size();
    }


}