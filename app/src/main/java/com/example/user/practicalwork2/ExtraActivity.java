package com.example.user.practicalwork2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.example.user.practicalwork2.Adapters.ExtraAdapter;
import com.example.user.practicalwork2.Models.ModelUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExtraActivity extends DrawerActivity {

    ListView listViewExtra;

    List<ModelUser> listmodelUserData;

    DatabaseReference dbReference;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_extra);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_extra, null, false);
        drawer.addView(contentView, 0);

        listmodelUserData = new ArrayList<>();
        listViewExtra = findViewById(R.id.listExtra);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference("USERS");

        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                listmodelUserData.clear();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    ModelUser modelUserData = userSnapshot.getValue(ModelUser.class);
                    listmodelUserData.add(modelUserData);

                    //Toast.makeText(ExtraActivity.this, "" + modelUserData.getUSER_mEMAIL(), Toast.LENGTH_SHORT).show();
                }

                ExtraAdapter adapter = new ExtraAdapter(ExtraActivity.this, listmodelUserData);
                listViewExtra.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
