package com.example.user.practicalwork2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.user.practicalwork2.Adapters.RecycleAdapter;

public class RecyclerViewTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_test);


        RecyclerView rv = (RecyclerView) findViewById(R.id.rvList);
        rv.setLayoutManager(new LinearLayoutManager(this));

        String[] mobiles = {"Android", "IPhone", "WindowsMobile", "Blackberry",
                "WebOS", "Ubuntu", "Windows7", "Max OS X"};

        rv.setAdapter(new RecycleAdapter(mobiles));

    }
}
