package com.alox1d.pcbcalc.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alox1d.pcbcalc.R;
import com.alox1d.pcbcalc.adapters.ExpandedRecyclerAdapter;
import com.alox1d.pcbcalc.models.ExpandedRecyclerChildItem;
import com.alox1d.pcbcalc.models.ExpandedRecyclerParentItem;
import com.alox1d.pcbcalc.models.StringWithTag;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Alox1d on 16.04.2018.
 */

public class DialogHelper {
    private Context context;

    private LayoutInflater factory;
    private AlertDialog.Builder builder;
    private View view;
    private Dialog dialog;

    ArrayList<ExpandedRecyclerParentItem> parentItems;
    ExpandedRecyclerAdapter expandedRecyclerAdapter;

    public DialogHelper(Context context) {
        this.context = context;
    }

    public void initializeAboutDialog() {
        builder = new AlertDialog.Builder(context);
        LayoutInflater factory = LayoutInflater.from(context);
        final View view = factory.inflate(R.layout.dialog_about_program, null);
        builder.setView(view);
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dlg, int sumthin) {

            }
        });
        builder.show();
    }

    public void initializeProgressDialog(String text) {
//        dialog = new ProgressDialog(context); // this = YourActivity
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage(text);
//        dialog.setIndeterminate(true);
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
        builder = new AlertDialog.Builder(context);

        factory = LayoutInflater.from(context);
        view = factory.inflate(R.layout.dialog_progress, null);
        TextView textView = view.findViewById(R.id.progress_bar_tv);
        textView.setText(text);
        builder.setView(view);

        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

    }

    public void closeDialogWithProgress(final String text, final String openFolderTitle, final String filePath) {

        view = factory.inflate(R.layout.dialog_progress, null);
        TextView textView = view.findViewById(R.id.progress_bar_tv); // todo add checkbox animation (replace progressbar)
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        Drawable drawable = context.getResources().getDrawable(R.drawable.check_black_192x192);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setIndeterminateDrawable(drawable);

        textView.setText(text);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.close, null);

//        builder.setPositiveButton(R.string.open_file, new DialogInterface.OnClickListener() { // TODO открыть папку? или Save As?
//            public void onClick(DialogInterface dlg, int sumthin) {
//                openFile(filePath, openFolderTitle);
//            }
//        });

        dialog.dismiss();
        dialog = builder.create();
        dialog.show();
    }

    private void openFile(String filePath, String openFolderTitle) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File wordFile = new File(filePath);
        Uri uri = Uri.fromFile(wordFile);
        intent.setDataAndType(uri, "application/msword");
        context.startActivity(Intent.createChooser(intent, openFolderTitle));
    }

    public void closeDialogWithProgress(final String text) {

        view = factory.inflate(R.layout.dialog_progress, null);
        TextView textView = view.findViewById(R.id.progress_bar_tv);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        Drawable drawable = context.getResources().getDrawable(R.drawable.check_black_192x192);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setIndeterminateDrawable(drawable);

        textView.setText(text);

        builder.setView(view);
        builder.setCancelable(true);
        builder.setNegativeButton(R.string.close, null);
        dialog.dismiss();
        dialog = builder.create();
        dialog.show();
    }

    public void initializeItemChooserDialog(ArrayList<StringWithTag> items, final AdapterView.OnItemClickListener onItemClickListener) {
        builder = new AlertDialog.Builder(context);
        factory = LayoutInflater.from(context);
        view = factory.inflate(R.layout.dialog_item_chooser, null);
        ListView listView = view.findViewById(R.id.lvComponentChooser);
        SearchView searchView = view.findViewById(R.id.svItemChooser);
        ArrayAdapter<StringWithTag> listViewAdapter = new ArrayAdapter<StringWithTag>(context,
                android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listViewAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listViewAdapter.getFilter().filter(newText);

                return false;
            }
        });
//        if (parentItems == null) {
//            parentItems = new ArrayList<>();
//            RecyclerView mRecyclerView = ((Activity) context).findViewById(R.id.inner_rv_input_data_layout);
//            expandedRecyclerAdapter = new ExpandedRecyclerAdapter(parentItems);
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
//            mRecyclerView.setAdapter(expandedRecyclerAdapter);
//        }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        onItemClickListener.onItemClick(parent, view, position, id);

////                ArrayList<ExpandedRecyclerChildItem> childItems;
////                ExpandedRecyclerParentItem expandedRecyclerParentItem;
////                ExpandedRecyclerChildItem expandedRecyclerChildItem = null;
////                /////////
////                expandedRecyclerParentItem = new ExpandedRecyclerParentItem();
////                expandedRecyclerParentItem.setParentName(items.get(position).toString());
////                childItems = new ArrayList<>();
////                //
////                for (Map.Entry<String, Double> entry : childs.entrySet()) {
////                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
////                    expandedRecyclerChildItem.setChildNameId(entry.getKey());
////                    expandedRecyclerChildItem.setChildValue(entry.getValue());
////                    childItems.add(expandedRecyclerChildItem);
//////                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//////                    expandedRecyclerChildItem.setChildNameId("Количество");
//////                    expandedRecyclerChildItem.setChildValue(1.0);
//////                    childItems.add(expandedRecyclerChildItem);
//////                    expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//////                    expandedRecyclerChildItem.setChildNameId("Поправочный коэффициент");
//////                    expandedRecyclerChildItem.setChildValue(55.0);
////                }
////                childItems.add(expandedRecyclerChildItem);
////                //
////                expandedRecyclerParentItem.setChildDataItems(childItems);
////                parentItems.add(expandedRecyclerParentItem);
////                expandedRecyclerAdapter.notifyItemInserted(parentItems.size() - 1);
//
                dialog.dismiss();
//            }
        }});
        builder.setView(view);

        dialog = builder.create();
        dialog.show();
    }

//    private ArrayList<ExpandedRecyclerParentItem> getDataToPassExtendedRecyclerAdapter() {
//        ArrayList<ExpandedRecyclerParentItem> parentItems = new ArrayList<>();
//        ArrayList<ExpandedRecyclerChildItem> childItems;
//        ExpandedRecyclerParentItem expandedRecyclerParentItem;
//        ExpandedRecyclerChildItem expandedRecyclerChildItem;
//        /////////
//        expandedRecyclerParentItem = new ExpandedRecyclerParentItem();
//        expandedRecyclerParentItem.setParentName("Parent 1");
//        childItems = new ArrayList<>();
//        //
//        expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//        expandedRecyclerChildItem.setChildNameId("Количество");
//        expandedRecyclerChildItem.setChildValue(1.0);
//        childItems.add(expandedRecyclerChildItem);
//        expandedRecyclerChildItem = new ExpandedRecyclerChildItem();
//        expandedRecyclerChildItem.setChildNameId("Поправочный коэффициент");
//        expandedRecyclerChildItem.setChildValue(55.0);
//        childItems.add(expandedRecyclerChildItem);
//        //
//        expandedRecyclerParentItem.setChildDataItems(childItems);
//        parentItems.add(expandedRecyclerParentItem);
//
//        return parentItems;
//    }
}
