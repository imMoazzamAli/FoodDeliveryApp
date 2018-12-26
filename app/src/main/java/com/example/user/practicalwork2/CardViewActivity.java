package com.example.user.practicalwork2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class CardViewActivity extends DrawerActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_card_view);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        View contentView = inflater.inflate(R.layout.activity_card_view, null, false);
        drawer.addView(contentView, 0);

        progressDialog.dismiss();

        Configuration configuration = getResources().getConfiguration();
        if (configuration.smallestScreenWidthDp >= 600) {
            Toast.makeText(this, "Tablet: " + configuration.smallestScreenWidthDp, Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(this, "Normal: " + configuration.smallestScreenWidthDp, Toast.LENGTH_LONG).show();

        imgCart.setVisibility(View.INVISIBLE);

        firebaseAuth = FirebaseAuth.getInstance();

        /*ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.tool_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );*/

        CardView cardFastFood = (CardView) findViewById(R.id.cardFastFood);
        CardView cardKebabs = (CardView) findViewById(R.id.cardKebabs);
        CardView cardChinese = (CardView) findViewById(R.id.cardChinese);
        CardView cardPizza = (CardView) findViewById(R.id.cardPizza);
        CardView cardThai = (CardView) findViewById(R.id.cardThai);
        CardView cardVegetarian = (CardView) findViewById(R.id.cardVegeterian);

        final AutoCompleteTextView acWhereAreYou = findViewById(R.id.actvWhereAreYou);
        AutoCompleteTextView acWhatYouWant = findViewById(R.id.actvWhatYouWant);

        //etWhatDoYouWant = findViewById(R.id.etWhatYouWant);

        cardFastFood.setOnClickListener(this);
        cardKebabs.setOnClickListener(this);
        cardChinese.setOnClickListener(this);
        cardPizza.setOnClickListener(this);
        cardThai.setOnClickListener(this);
        cardVegetarian.setOnClickListener(this);


        //whatDoYouWantForSimpleET();

        String[] cities = {
                "Gujranwala",
                "Gujrat",
                "Gujar Khan",
                "Gujra",
                "Gujranwala1",
                "Gujranwala2",
                "Gujranwala3",
                "Gujranwala4",
                "Gujranwala5",
                "Ahmedabad",
                "Chennai",
                "Kolkata",
                "Pune",
                "Jabalpur"
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);

        acWhereAreYou.setThreshold(2);
        acWhereAreYou.setAdapter(adapter);

        acWhereAreYou.setCursorVisible(false);
        acWhereAreYou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (acWhereAreYou.hasFocus() || acWhereAreYou.requestFocus()) {
                    acWhereAreYou.setCursorVisible(true);
                }
            }
        });
        acWhereAreYou.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CardViewActivity.this, "Item: " + acWhereAreYou.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        acWhatYouWant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {

                    Toast.makeText(CardViewActivity.this, "Searched will go here", Toast.LENGTH_SHORT).show();
                    return true;

                } else {
                    return false;
                }
            }
        });

    }


    private void whatDoYouWantForSimpleET() {

/*
        etWhatDoYouWant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    Toast.makeText(CardViewActivity.this, "Clicked here", Toast.LENGTH_SHORT).show();
                    //searchItemsYouWant();
                    return true;

                } else {
                    Toast.makeText(CardViewActivity.this, "else", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        });


        etWhatDoYouWant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Toast.makeText(CardViewActivity.this, "before change" + charSequence, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Toast.makeText(CardViewActivity.this, "on change" + charSequence, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Toast.makeText(CardViewActivity.this, "after change" + editable.toString(), Toast.LENGTH_SHORT).show();

            }
        });
*/


    }

    /*@Override
    public boolean onSupportNavigateUp() {

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        startActivity(new Intent(CardViewActivity.this, DrawerActivity.class));

        return super.onSupportNavigateUp();
    }*/


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {

        long id = view.getId();

        if (id == R.id.cardFastFood) {
            //finish();
            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            startActivity(new Intent(CardViewActivity.this, CompaniesActivity.class));
        }
        if (id == R.id.cardKebabs) {
        }
        if (id == R.id.cardChinese) {
        }
        if (id == R.id.cardPizza) {
        }
        if (id == R.id.cardThai) {
        }
        if (id == R.id.cardVegeterian) {
        }

    }


}
