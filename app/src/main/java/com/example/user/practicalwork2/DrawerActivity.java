package com.example.user.practicalwork2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Models.ModelPreOrder;
import com.example.user.practicalwork2.Models.ModelUser;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.IOException;
import java.util.ArrayList;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String nameHeader = "";
    String userName = "";
    String userEmail = null;

    protected DrawerLayout drawer;
    NavigationView navigationView;

    TextView txtLoggedInStatus;
    TextView txtHeaderTitle;
    TextView txtHeaderEmail;

    protected ImageView imgCart;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReferenceUser;
    FirebaseAuth firebaseAuth;


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //toolbar.setBackgroundColor(getColor(R.color.colorPurple));
        getWindow().setStatusBarColor(getColor(R.color.colorPurpleGiven));

        //toolbar.getBackground().setAlpha(0.8);

        imgCart = toolbar.findViewById(R.id.imgCart);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceUser = firebaseDatabase.getReference("USERS");
        firebaseAuth = FirebaseAuth.getInstance();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtHeaderTitle = headerView.findViewById(R.id.txtHeaderTitle);
        txtHeaderEmail = headerView.findViewById(R.id.textHeaderEmail);

        nameHeader = txtHeaderTitle.getText().toString();

        txtLoggedInStatus = findViewById(R.id.txtLoggedInStatus);

        txtLoggedInStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(DrawerActivity.this, SignInActivity.class));

            }
        });

        progressDialog = new ProgressDialog(this);

        if (CheckInternet.isInternetAvailable(this)) {
            visibleItems();

        } else {

            FancyToast.makeText(this, "No internet Access Drawer.", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
            //DrawerActivity.this.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            //Toast.makeText(this, "No internet Access", Toast.LENGTH_SHORT).show();
        }

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ModelPreOrder> cartList = new ArrayList<>();
                Intent intent = new Intent(DrawerActivity.this, CartOrderActivity.class);
                intent.putExtra("cartList", cartList);
                startActivity(intent);
            }
        });
        //final ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.list_view_item, R.id.tvItems, mobileArray);

        /*final ArrayAdapter adapter1 = new ArrayAdapter<String>(this, R.layout.list_view_item, R.id.tvItems, mobileArray) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Get the current item from ListView
                View view = super.getView(position, convertView, parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 100;
                //params.width = 100;
                view.setLayoutParams(params);

                return view;
            }
        };

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    public void visibleItems() {
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        final Menu menu = navigationView.getMenu();

        if (firebaseAuth.getCurrentUser() != null) {
            dbReferenceUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        ModelUser modelUser = userSnapshot.getValue(ModelUser.class);
                        //get username and email from DataBases
                        userName = modelUser.getUSER_NAME();
                        userEmail = modelUser.getUSER_mEMAIL();

                        //get email  of current user at runtime
                        String currentUserEmail = firebaseAuth.getCurrentUser().getEmail();

                        if (currentUserEmail.equals(userEmail)) {
                            txtHeaderTitle.setText(userName);
                            txtHeaderEmail.setText(userEmail);

                            txtLoggedInStatus.setText("Logout");

                            menu.findItem(R.id.nav_basket).setVisible(true);
                            menu.findItem(R.id.nav_track_order).setVisible(true);

                            menu.findItem(R.id.nav_favourite).setVisible(true);
                            menu.findItem(R.id.nav_order_history).setVisible(true);
                            menu.findItem(R.id.nav_refresh_search).setVisible(true);
                            //menu.findItem(R.id.nav_need_help).setVisible(true);
                            menu.findItem(R.id.nav_contact_us).setVisible(true);
                            menu.findItem(R.id.nav_tell_us_what_you_think).setVisible(true);
                            menu.findItem(R.id.nav_about_us).setVisible(true);

                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            //Toast.makeText(this, "User Not Logged In", Toast.LENGTH_SHORT).show();
            txtLoggedInStatus.setText("Login");

            menu.findItem(R.id.nav_basket).setVisible(true);
            menu.findItem(R.id.nav_refresh_search).setVisible(true);
            // menu.findItem(R.id.nav_need_help).setVisible(true);
            menu.findItem(R.id.nav_contact_us).setVisible(true);
            menu.findItem(R.id.nav_tell_us_what_you_think).setVisible(true);
            menu.findItem(R.id.nav_about_us).setVisible(true);
            progressDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            //startActivity(new Intent(DrawerActivity.this, CardViewActivity.class));
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);

        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_basket) {
            // Handle the camera action
            Toast.makeText(this, "This is message", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_refresh_search) {

        } else if (id == R.id.nav_track_order) {

        } else if (id == R.id.nav_order_history) {
            startActivity(new Intent(this, OrderHistoryActivity.class));

        } else if (id == R.id.nav_contact_us) {
            startActivity(new Intent(this, ChatActivity.class));

        } else if (id == R.id.nav_about_us) {

        }

        /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
