package com.example.user.practicalwork2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Models.ModelUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtRegisterHere, txtSkip;

    EditText etEmailSignIn, etPassSignIn;

    Button btnLogin, btnLoginGmail;

    FirebaseAuth firebaseAuth;
    GoogleSignInClient mGoogleSignInClient;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbReference;

    ProgressDialog progressDialog;

    TextView txtHeaderTitle;
    NavigationView navigationView;

    int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReference = firebaseDatabase.getReference("USERS");

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

//        final String emailCurrentUser = firebaseUser.getEmail();

        if (firebaseAuth.getCurrentUser() != null) {

            /*dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        ModelUser modelUser = userSnapshot.getValue(ModelUser.class);

                        String email = modelUser.getUSER_mEMAIL();

                        if (email.equals(emailCurrentUser)) {
                            txtHeaderTitle.setText("");
                            //txtHeaderTitle.setText(modelUser.getUSER_NAME());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

            startActivity(new Intent(this, CardViewActivity.class));
            finish();
        }

        txtRegisterHere = (TextView) findViewById(R.id.txtRegisterHere);
        txtSkip = (TextView) findViewById(R.id.txtSkip);

        etEmailSignIn = findViewById(R.id.etEmailSignIn);
        etPassSignIn = findViewById(R.id.etPassSignIn);

        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGmail = findViewById(R.id.btnLoginGmail);

        txtHeaderTitle = findViewById(R.id.txtHeaderTitle);
        navigationView = findViewById(R.id.nav_view);

        btnLogin.setOnClickListener(this);
        btnLoginGmail.setOnClickListener(this);
        txtRegisterHere.setOnClickListener(this);
        txtSkip.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    public void loginUser() {

        String email = etEmailSignIn.getText().toString().trim();
        String pass = etPassSignIn.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {

            if (TextUtils.isEmpty(email)) {
                etEmailSignIn.setError("Field required.");
            }
            if (TextUtils.isEmpty(pass)) {
                etPassSignIn.setError("Field required.");
            }

        } else {
            progressDialog.setMessage("Signing In...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

//                        View headerView = navigationView.getHeaderView(0);

                        dbReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                    ModelUser modelUser = userSnapshot.getValue(ModelUser.class);

                                    String email = modelUser.getUSER_mEMAIL();
                                    if (email.equals(etEmailSignIn.toString())) {
                                        txtHeaderTitle.setText(modelUser.getUSER_NAME());
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        startActivity(new Intent(SignInActivity.this, CardViewActivity.class));
                        progressDialog.dismiss();
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(SignInActivity.this, "Email or Password is not correct.", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }

    public void loginUserGmail() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Context context = SignInActivity.this;

                            AlertDialog.Builder dialog = new AlertDialog.Builder(SignInActivity.this);
                            dialog.setTitle("Please Enter required information");
                            //dialog.setMessage("Title: ");
                            dialog.setCancelable(false);

                            LinearLayout layout = new LinearLayout(SignInActivity.this);
                            layout.setOrientation(LinearLayout.VERTICAL);

                            // Add a TextView here for the "Title" label, as noted in the comments
                            final EditText txtPhoneNumber = new EditText(SignInActivity.this);
                            txtPhoneNumber.setHint("Phone Number");
                            layout.addView(txtPhoneNumber); // Notice this is an add method

                            // Add another TextView here for the "Description" label
                            final EditText txtPassword = new EditText(SignInActivity.this);
                            txtPassword.setHint("Password");
                            layout.addView(txtPassword); // Another add method

                            // Add another TextView here for the "Description" label
                            final EditText txtConfirmPassword = new EditText(SignInActivity.this);
                            txtConfirmPassword.setHint("Confirm Password");
                            layout.addView(txtConfirmPassword); // Another add method

                            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(SignInActivity.this, "Ok. pressed", Toast.LENGTH_SHORT).show();
                                }
                            });

                            dialog.setView(layout); // Again this is a set method, not add
                            dialog.show();

                            //startActivity(new Intent(SignInActivity.this, CardViewActivity.class));
                            //finish();
                            Toast.makeText(SignInActivity.this, "Name: " + user.getDisplayName() + " Email: " + user.getEmail(), Toast.LENGTH_LONG).show();


                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Not Logged in", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onClick(View view) {
        long id = view.getId();

        if (id == R.id.btnLogin) {
            loginUser();
        }

        if (id == R.id.btnLoginGmail) {
            //loginUserGmail();
        }

        if (id == R.id.txtRegisterHere) {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();
        }

        if (id == R.id.txtSkip) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();

            startActivity(new Intent(SignInActivity.this, CardViewActivity.class));
            finish();

            progressDialog.dismiss();
        }

    }
}
