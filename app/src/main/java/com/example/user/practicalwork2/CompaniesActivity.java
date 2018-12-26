package com.example.user.practicalwork2;

import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.practicalwork2.Adapters.CompaniesAdapter;
import com.example.user.practicalwork2.Models.ModelCompanies;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CompaniesActivity extends AppCompatActivity {

    ModelCompanies mc;

    ListView listViewCompanies;
    List<ModelCompanies> listmodelCompaniesData;
    CompaniesAdapter companiesAdapter;

    DatabaseReference dbReference;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog progressDialog;

    /*ArrayList<String> titles;
    ArrayList<String> descriptions;

    ArrayList<Integer> flags;
    ListView list;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies);

        /*LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_companies, null, false);
        drawer.addView(contentView, 0);*/

        listmodelCompaniesData = new ArrayList<>();
        listViewCompanies = findViewById(R.id.lvCompanies);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference("COMPANIES");

        progressDialog = new ProgressDialog(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.tool_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        actionBar.setDisplayHomeAsUpEnabled(true);


        getCompanies();
        onlistItemClicked();
        //progressDialog.dismiss();

        /*
        listmodelCompaniesData = new ArrayList<>();

        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_noodles, "The Noodle", "Min Delivery : $25/", "Open"));
        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_sub_way, "Subway", "Min Delivery : $15/", "Open"));
        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_donuts, "Dunkin Dounts", "Min Delivery : $55/", "Closed"));
        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_jack, "Jack in tha box", "Min Delivery : $55/", "Open"));
        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_noodles, "The Noodle", "Min Delivery : $25/", "Open"));
        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_sub_way, "Subway", "Min Delivery : $15/", "Open"));
        listmodelCompaniesData.add(new ModelCompanies(R.drawable.company_donuts, "Dunkin Donuts", "Min Delivery : $55/", "Closed"));

        final ListAdapter listAdapter = new CompaniesAdapter(CompaniesActivity.this, listmodelCompaniesData);

        listViewCompanies = (ListView) findViewById(R.id.lvCompanies);
        listViewCompanies.setAdapter(listAdapter);

        listViewCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    startActivity(new Intent(CompaniesActivity.this, ExpandMenuActivity.class));
                    finish();
                }
                if (i == 1) {
                }
                if (i == 2) {
                }
                if (i == 3) {
                }
            }
        });

        for (int i = 0; i < listmodelCompaniesData.size(); i++) {
            modelCompanies = listmodelCompaniesData.get(i);

            String id = dbReference.push().getKey();
            dbReference.child(id).setValue(modelCompanies);

        }
*/


        /*titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        flags = new ArrayList<>();

        titles.add("The Noodle");
        titles.add("SubWay");
        titles.add("Dunkin Donuts");
        titles.add("Jack in the box");
        titles.add("The Noodle");
        titles.add("SubWay");
        titles.add("Dunkin Donuts");
        titles.add("etc");

        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");
        descriptions.add("Min Delivery:$25/ Open");

        flags.add(R.drawable.company_noodles);
        flags.add(R.drawable.company_sub_way);
        flags.add(R.drawable.company_donuts);
        flags.add(R.drawable.company_jack);
        flags.add(R.drawable.company_noodles);
        flags.add(R.drawable.company_sub_way);
        flags.add(R.drawable.company_donuts);
        flags.add(R.mipmap.ic_launcher);

        final ListAdapter listAdapter = new CompaniesAdapter(this, '0', titles, descriptions, flags);

        list = (ListView) findViewById(R.id.lvMobiles);
        list.setAdapter(listAdapter);
*/
    }

    private void getCompanies() {

        //progressDialog.setTitle("Companies");
        progressDialog.setMessage("Refreshing data...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listmodelCompaniesData.clear();
                for (DataSnapshot companiesSnapshot : dataSnapshot.getChildren()) {
                    ModelCompanies modelData = companiesSnapshot.getValue(ModelCompanies.class);
                    listmodelCompaniesData.add(modelData);
                }

                companiesAdapter = new CompaniesAdapter(CompaniesActivity.this, listmodelCompaniesData);
                listViewCompanies.setAdapter(companiesAdapter);

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(CompaniesActivity.this, "" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onlistItemClicked() {

        listViewCompanies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    //finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    mc = (ModelCompanies) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent(CompaniesActivity.this, ExpandMenuActivity.class);
                    //Intent intent = new Intent(CompaniesActivity.this, ExpandAbleActivity.class);
                    intent.putExtra("companies", mc);
                    startActivity(intent);

                } else if (i == 1) {
                    //finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    mc = (ModelCompanies) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent(CompaniesActivity.this, ExpandMenuActivity.class);
                    intent.putExtra("companies", mc);
                    startActivity(intent);

                } else if (i == 2) {

                    //finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    mc = (ModelCompanies) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent(CompaniesActivity.this, ExpandMenuActivity.class);
                    intent.putExtra("companies", mc);
                    startActivity(intent);
                } else if (i == 3) {
                    //finish();
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    mc = (ModelCompanies) adapterView.getItemAtPosition(i);

                    Intent intent = new Intent(CompaniesActivity.this, ExpandMenuActivity.class);
                    intent.putExtra("companies", mc);
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        //startActivity(new Intent(this, CardViewActivity.class));
        //super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        //startActivity(new Intent(CompaniesActivity.this, CardViewActivity.class));
        return super.onSupportNavigateUp();
    }
}
