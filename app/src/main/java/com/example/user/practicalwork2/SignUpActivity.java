package com.example.user.practicalwork2;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.practicalwork2.Models.ModelUser;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtSignInHere;

    EditText etNameSignUp, etEmailSignUp, etPhoneSignUp, etPasswordSignUp;
    Button btnRegister, btnRegisterGmail, btnRegisterFB;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    DatabaseReference dbReferenceUsers;
    FirebaseDatabase firebaseDatabase;

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 100;

    String statusNormal = "normal";
    String statusGmail = "gmail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbReferenceUsers = firebaseDatabase.getReference("USERS");

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, CardViewActivity.class));
        }

        txtSignInHere = findViewById(R.id.txtSignInHere);

        etNameSignUp = findViewById(R.id.etNameSignUp);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPhoneSignUp = findViewById(R.id.etPhoneSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegisterGmail = findViewById(R.id.btnRegisterGmail);

        btnRegister.setOnClickListener(this);
        btnRegisterGmail.setOnClickListener(this);
        txtSignInHere.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public void registerUser() {
        String name = etNameSignUp.getText().toString().trim();
        String email = etEmailSignUp.getText().toString().trim();
        final String phone = etPhoneSignUp.getText().toString().trim();
        String password = etPasswordSignUp.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            etNameSignUp.setError("Field required.");
        } else if (TextUtils.isEmpty(email)) {
            etEmailSignUp.setError("Field required.");
        } else if (TextUtils.isEmpty(phone)) {
            etPhoneSignUp.setError("Field required.");
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPasswordSignUp.setError("Six digit password required.");
        } else {
            progressDialog.setTitle("Registering");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            if (CheckInternet.isInternetAvailable(SignUpActivity.this)) {

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            addUserDB();
                            FancyToast.makeText(SignUpActivity.this, "Registered Successfully.", Toast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                            //Toast.makeText(SignUpActivity.this, "Registered Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this, CardViewActivity.class));
                            progressDialog.dismiss();
                            finish();
                        } else {
                            progressDialog.dismiss();
                            FancyToast.makeText(SignUpActivity.this, "User Already Registered with this email", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
                        }
                    }
                });

            } else {
                progressDialog.dismiss();
                FancyToast.makeText(SignUpActivity.this, "Check Internet Connection", Toast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        }
    }

    public void registerUserWithGmail() {
        //Intent intent1 = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    public void addUserDB() {
        final String name = etNameSignUp.getText().toString().trim();
        final String email = etEmailSignUp.getText().toString().trim();
        final String phone = etPhoneSignUp.getText().toString().trim();
        final String password = etPasswordSignUp.getText().toString();

        if (TextUtils.isEmpty(name)) {
            etNameSignUp.setError("Field required.");
        } else if (TextUtils.isEmpty(email)) {
            etEmailSignUp.setError("Field required.");
        } else if (TextUtils.isEmpty(phone)) {
            etPhoneSignUp.setError("Field required.");
        } else if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPasswordSignUp.setError("Six digit password required.");
        } else {

            dbReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int i = 0;
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        i++;
                        userSnapshot.getKey();
                        ModelUser modelUser = userSnapshot.getValue(ModelUser.class);

                        String mUserPhoneNumber = modelUser.getUSER_PHONE();
                        String mUserEmail = modelUser.getUSER_mEMAIL();

                        if (mUserPhoneNumber.equals(phone) && modelUser.getUSER_mEMAIL().isEmpty()) {

                            modelUser.setUSER_mEMAIL(email);
                            modelUser.setUSER_NAME(name);
                            modelUser.setUSER_PASSWORD(password);
                            modelUser.setUSER_STATUS("normal");

                            dbReferenceUsers.child(modelUser.getUSER_ID()).setValue(modelUser);
                            break;

                        }
                        if (i == dataSnapshot.getChildrenCount()) {

                            //create unique id inside "USERS"
                            String id = dbReferenceUsers.push().getKey();

                            ModelUser mu = new ModelUser(id, name, phone, password, email, statusNormal);
                            dbReferenceUsers.child(id).setValue(mu);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(intent);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                //Toast.makeText(this, "Data Got" + result.getSignInAccount().getDisplayName(), Toast.LENGTH_SHORT).show();
                final GoogleSignInAccount account = result.getSignInAccount();

                dbReferenceUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        int i = 0;
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                            i++;
                            ModelUser modelUser = userSnapshot.getValue(ModelUser.class);
                            String email = modelUser.getUSER_mEMAIL();

                            if (account.getEmail().equals(email) && modelUser.getUSER_STATUS().equalsIgnoreCase("normal")) {
                                //match selected user with saved user email one by one in USER Table in firebase
                                //and its status i.e. whether user registered normally or using gmail
                                //if gmail then this if statement will not work and leads to sign in the user in second if statement
                                // other wise show the message that user already exist
                                FancyToast.makeText(SignUpActivity.this, "User Already Registered with this email.", Toast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                                mGoogleSignInClient.signOut();
                                break;
                            }
                            if (i == dataSnapshot.getChildrenCount()) {
                                // this if statement will run when last record is not matched in above if statement
                                // if the record matches the loop will break due to above if statement
                                // and control will not come here
                                //Sign in to selected account if loop is at end of table and record is not matched in first if statement
                                firebaseSignInWithGoogleAuth(account);
                                mGoogleSignInClient.signOut();
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                Toast.makeText(this, "User Not Selected", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Result Code did Not match", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseSignInWithGoogleAuth(final GoogleSignInAccount account) {
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AuthCredential credentials = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth.signInWithCredential(credentials)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if login auth successful then save data in database also in the last if it does not exist
                            saveDataFromGoogleSignIn();

                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(SignUpActivity.this, CardViewActivity.class));

                            //region AlertDialog with CustomView to get specific fields i.e. password, confirmPassword, phoneNumber

/*                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpActivity.this);
                            dialogBuilder.setMessage("Enter following fields.");

                            LayoutInflater inflater = SignUpActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.row_sign_up_gmail, null);
                            dialogBuilder.setView(dialogView);

                            final EditText etPassword = dialogView.findViewById(R.id.txtPassword);
                            final EditText etConfirmPassword = dialogView.findViewById(R.id.txtConfirmPassword);
                            final EditText etPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);

                            dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String password = etPassword.getText().toString();
                                    final String confirmPassword = etConfirmPassword.getText().toString();
                                    final String phoneNumber = etPhoneNumber.getText().toString().trim();

                                    //if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phoneNumber)) {

                                    Toast.makeText(SignUpActivity.this, "Password is: " + password + " Confirm Password is: " + confirmPassword + " phone No. is: " + phoneNumber, Toast.LENGTH_LONG).show();
//                                    Toast.makeText(SignUpActivity.this, "First: " + etPassword.getText().toString() + "second: " + etConfirmPassword.getText().toString() + "phone: " + etPhoneNumber.getText().toString(), Toast.LENGTH_LONG).show();
                                    if (TextUtils.isEmpty(password)) {
                                        Toast.makeText(SignUpActivity.this, "Empty password", Toast.LENGTH_SHORT).show();
                                        etPassword.setError("Field Required");

                                    } else if (TextUtils.isEmpty(confirmPassword)) {
                                        Toast.makeText(SignUpActivity.this, "Empty confirmPassword", Toast.LENGTH_SHORT).show();
                                        etConfirmPassword.setError("Field Required");

                                    } else if (TextUtils.isEmpty(phoneNumber)) {
                                        Toast.makeText(SignUpActivity.this, "Empty phoneNumber", Toast.LENGTH_SHORT).show();
                                        etPhoneNumber.setError("Field Required");

                                    } else {
                                        Toast.makeText(SignUpActivity.this, "in else", Toast.LENGTH_SHORT).show();
                                        String id = dbReference.push().getKey();

                                        ModelUser modelUser = new ModelUser(id, user.getDisplayName(), phoneNumber, password, user.getEmail());
                                        dbReference.child(id).setValue(modelUser);

                                        dialogInterface.dismiss();
                                        finish();
                                        startActivity(new Intent(SignUpActivity.this, CardViewActivity.class));
                                    }

                                }
                            });

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.show();*/
//endregion

                        } else {
                            progressDialog.dismiss();
                            //Not successful
                            Toast.makeText(SignUpActivity.this, "Google Sign in Task is not successful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void saveDataFromGoogleSignIn() {

        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        dbReferenceUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    i++;
                    ModelUser modelUser = userSnapshot.getValue(ModelUser.class);
                    String email = modelUser.getUSER_mEMAIL();

                    if (currentUser.getEmail().equals(email)) {
                        //match selected user with saved user email one by one in USER Table in firebase
                        break;
                    }
                    if (i == dataSnapshot.getChildrenCount()) {
                        //Sign in to selected account if loop is at end of table and record is not matched in first if statement
                        String id = dbReferenceUsers.push().getKey();
                        ModelUser modelUser1 = new ModelUser(id, currentUser.getDisplayName(), "00", "password", currentUser.getEmail(), statusGmail);
                        dbReferenceUsers.child(id).setValue(modelUser1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View view) {
        long id = view.getId();

        if (id == R.id.btnRegister) {
            registerUser();
        }
        if (id == R.id.btnRegisterGmail) {
            registerUserWithGmail();
        }
        if (id == R.id.txtSignInHere) {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut();
        }
    }
}
