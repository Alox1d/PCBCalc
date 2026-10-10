package com.alox1d.pcbcalc;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alox1d.pcbcalc.adapters.MainRecyclerViewAdapter;
import com.alox1d.pcbcalc.models.MainCalcItem;
import com.alox1d.pcbcalc.ui.DialogHelper;
import com.alox1d.pcbcalc.utils.PreferencesHelper;

import java.util.ArrayList;
import java.util.List;

import static com.alox1d.pcbcalc.utils.PreferencesHelper.getDefaults;
import static com.alox1d.pcbcalc.utils.PreferencesHelper.setDefaults;


public class MainActivity extends AppCompatActivity {
    private List<MainCalcItem> mainCalcItems;
    private List<MainCalcItem> mechanicalImpactItems;
    private List<MainCalcItem> dependabilityItems;
    private List<MainCalcItem> tempMainCalcItems;
    private RecyclerView rv;
    private MainRecyclerViewAdapter adapterRV;
    final private String DISABLE_SPLASH = PreferencesHelper.DISABLE_SPLASH;

    final private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initializeToolbar();

//        initializeTitle();
        initializeData();
        initializeAdapter();

        askPermissions();


//        RecyclerView  mRecyclerView = findViewById(R.id.recyclerView);
//        ExpandedRecyclerAdapter expandedRecyclerAdapter = new ExpandedRecyclerAdapter(getDummyDataToPass());
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(expandedRecyclerAdapter);
//        mRecyclerView.setHasFixedSize(true);

 /*       ExpandableListView expandableListView = findViewById(R.id.explistview);
        LinearLayout.LayoutParams elvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        expandableListView.setLayoutParams(elvParams);
        ArrayList<ArrayList<String>> groups = new ArrayList<ArrayList<String>>();
        ArrayList<String> children1 = new ArrayList<String>();
        ArrayList<String> children2 = new ArrayList<String>();
        children1.add("Child_1");
        children1.add("Child_2");
        groups.add(children1);
        children2.add("Child_1");
        children2.add("Child_2");
        children2.add("Child_3");
        groups.add(children2);
        //Создаем адаптер и передаем context и список с данными
        del_ExpandedListAdapter adapter = new del_ExpandedListAdapter(this, groups);
        expandableListView.setAdapter(adapter);*/
    }


    private void initializeTitle() {
        setTitle(R.string.design_calculations_name);
    }

    private void askPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, getString(R.string.requireWritePermission), Toast.LENGTH_SHORT).show();

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // for support?


    }

    private void initializeData() {
        mainCalcItems = new ArrayList<>();
        mainCalcItems.add(new MainCalcItem(R.string.mechanical_impacts_name, R.drawable.main_mechanical_impacts));
        mainCalcItems.add(new MainCalcItem(R.string.dependability_name, R.drawable.main_dependability));
        mainCalcItems.add(new MainCalcItem(R.string.about_app_name, R.drawable.main_about_program));

        mechanicalImpactItems = new ArrayList<>();
        mechanicalImpactItems.add(new MainCalcItem(R.string.vibration_durable_name, R.drawable.mechanical_impacts_vibration_durable));
        mechanicalImpactItems.add(new MainCalcItem(R.string.vibration_stable_name, R.drawable.mechanical_impacts_vibration_stable));
//        mechanicalImpactItems.add(new MainCalcItem(R.string.impact_resistance_name, R.drawable.mechanical_impacts_impact_resistance));
        mechanicalImpactItems.add(new MainCalcItem(R.string.backward, R.drawable.mechanical_impacts_back_arrow));

        dependabilityItems = new ArrayList<>();
        dependabilityItems.add(new MainCalcItem(R.string.failure_rate_name, R.drawable.dependability_failure_rate));
        dependabilityItems.add(new MainCalcItem(R.string.backward, R.drawable.mechanical_impacts_back_arrow));



    }

    private void initializeAdapter() {
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapterRV = new MainRecyclerViewAdapter(mainCalcItems,
                new MainRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, MainCalcItem item) {
                        Intent myIntent;

                        Integer calcNameId = item.getNameId();
                        if (calcNameId.equals(R.string.mechanical_impacts_name)) {
                            tempMainCalcItems = new ArrayList<>();
                            tempMainCalcItems.addAll(mainCalcItems);
                            int oldSize = mainCalcItems.size();
                            mainCalcItems.clear();
                            adapterRV.notifyItemRangeRemoved(0, oldSize);
                            mainCalcItems.addAll(mechanicalImpactItems);
                            adapterRV.notifyItemRangeInserted(0, mechanicalImpactItems.size());


                        } else if (calcNameId.equals(R.string.about_app_name)) {
                            DialogHelper alertHelper = new DialogHelper(v.getContext());
                            alertHelper.initializeAboutDialog();

                        } else if (calcNameId.equals(R.string.vibration_durable_name) || calcNameId.equals(R.string.vibration_stable_name)
                                || calcNameId.equals(R.string.impact_resistance_name)) {
                            myIntent = new Intent(v.getContext(), InputDataActivity.class);
                            myIntent.putExtra(getString(R.string.mechanical_impacts_method_name), calcNameId);
                            //myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(myIntent);
                        } else if (calcNameId.equals(R.string.failure_rate_name)) {
                            myIntent = new Intent(v.getContext(), InputDataActivity.class);
                            myIntent.putExtra(getString(R.string.mechanical_impacts_method_name), calcNameId);
                            //myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            v.getContext().startActivity(myIntent);
                        } else if (calcNameId.equals(R.string.backward)) {
                            int oldSize = mainCalcItems.size();
                            mainCalcItems.clear();
                            adapterRV.notifyItemRangeRemoved(0, oldSize);
                            mainCalcItems.addAll(tempMainCalcItems);
                            adapterRV.notifyItemRangeInserted(0, tempMainCalcItems.size());

                        } else if (calcNameId.equals(R.string.dependability_name)) {
                            tempMainCalcItems = new ArrayList<>();
                            tempMainCalcItems.addAll(mainCalcItems);
                            int oldSize = mainCalcItems.size();
                            mainCalcItems.clear();
                            adapterRV.notifyItemRangeRemoved(0, oldSize);
                            mainCalcItems.addAll(dependabilityItems);
                            adapterRV.notifyItemRangeInserted(0, dependabilityItems.size());

                        } else {
                            Toast.makeText(v.getContext(), item.getNameId(), Toast.LENGTH_LONG).show();
                        }


                    }
                });
        rv.setAdapter(adapterRV);
    }
//    private void runLayoutAnimation(final RecyclerView recyclerView) {
//        final Context context = recyclerView.getContext();
//        final LayoutAnimationController controller =
//                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
//
//        recyclerView.setLayoutAnimation(controller);
//        recyclerView.getAdapter().notifyDataSetChanged();
//        recyclerView.scheduleLayoutAnimation();
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        boolean isSplashDisabled = getDefaults(DISABLE_SPLASH, this);
        menu.findItem(R.id.menu_item_disable_splash_screen).setChecked(isSplashDisabled);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_disable_splash_screen:
                setDefaults(DISABLE_SPLASH, !item.isChecked(), this);
                item.setChecked(!item.isChecked());
                //Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this); // вместо android:name в manifest'е

    }



}
