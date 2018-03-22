package com.csy.vquest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText fnameText;
    private EditText lnameText;
    private EditText emailText;
    private EditText unameText;
    private EditText contactText;
    private EditText passText;
    private EditText conpassText;
    private CheckBox anonymCheck;
    private CheckBox termsCheck;
    private Button signupBtn;

    private int anonymity = 1;
    private int termsAccepted = 0;

    private boolean fname_valid = true;
    private boolean lname_valid = true;
    private boolean email_valid = true;
    private boolean uname_valid = true;
    private boolean contact_valid = true;
    private boolean pass_valid = true;
    private boolean conpass_valid = true;
    private boolean terms_valid = true;

    DatabaseReference rootRef;
    DatabaseReference memberRef;
    Query query;

    FirebaseAuth firebaseAuth;
    private String fname;
    private String uname;
    private String lname;
    private String email;
    private long contactNum;
    private String pass;
    private String conpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");

        firebaseAuth = FirebaseAuth.getInstance();

        fnameText = (EditText) findViewById(R.id.input_fname);
        lnameText = (EditText) findViewById(R.id.input_lname);
        emailText = (EditText) findViewById(R.id.input_new_email);
        unameText = (EditText) findViewById(R.id.input_uname);
        contactText = (EditText) findViewById(R.id.input_phone);
        passText = (EditText) findViewById(R.id.input_new_pass);
        conpassText = (EditText) findViewById(R.id.input_con_pass);
        anonymCheck = (CheckBox) findViewById(R.id.check_anonym);
        termsCheck = (CheckBox) findViewById(R.id.check_terms);
        signupBtn = (Button) findViewById(R.id.btn_signup);

        rootRef = FirebaseDatabase
                .getInstance()
                .getReference();

        memberRef = rootRef.child("member");

        anonymCheck.setOnCheckedChangeListener(this);
        termsCheck.setOnCheckedChangeListener(this);
        signupBtn.setOnClickListener(this);
        unameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                uname = unameText.getText().toString().trim();
                if (uname.isEmpty()){
                    Log.d("empty", "working");
                    unameText.setError("Required");
                    uname_valid = false;
                }
                else if(uname.contains(" ")){
                        unameText.setError("No space allowed");
                        uname_valid = false;
                }
                else if(uname.contains("@")){
                    unameText.setError("No @ allowed");
                    uname_valid = false;
                }
                else {
                    memberRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot data :
                                    dataSnapshot.getChildren()) {
                                unameText.setError(null);
                                uname_valid = true;
                                if (data.child("username").getValue(String.class).equalsIgnoreCase(uname)){
                                    unameText.setError("Already exists");
                                    uname_valid = false;
                                    break;
                                }

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }




            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View v) {

//        View view = this.getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }

        if(v.getId() == R.id.btn_signup) {

            fname = fnameText.getText().toString().trim();
            if (fname.isEmpty()) {
                fnameText.setError("Required");
                fname_valid = false;
            }
            else{
                fnameText.setError(null);
                fname_valid = true;
            }

            lname = lnameText.getText().toString().trim();
            if (lname.isEmpty()) {
                lnameText.setError("Required");
                lname_valid = false;
            }
            else{
                lnameText.setError(null);
                lname_valid = true;
            }

            email = emailText.getText().toString().trim();
            if (email.isEmpty()) {
                emailText.setError("Required");
                email_valid = false;
            } else if (!email.endsWith("@smvdu.ac.in") || email.length() < 13) {
                emailText.setError("Valid SMVDU email id");
                email_valid = false;
            }
            else{
                emailText.setError(null);
                email_valid = true;
            }

            uname = unameText.getText().toString().trim();
            if(uname.isEmpty()){
                unameText.setError("Required");
                uname_valid = false;
            }

            String contact = contactText.getText().toString().trim();
            contactNum = -1;
            if (!contact.isEmpty()) {

                try {
                    contactNum = Long.parseLong(contact);
                    contactText.setError(null);
                    contact_valid = true;
                    if (contact.length() != 10) {
                        contactText.setError("Exactly 10 digits");
                        contact_valid = false;
                    }
                } catch (Exception e) {
                    contactText.setError("Invalid");
                    contact_valid = false;
                }

            }
            else {
                contactText.setError(null);
                contact_valid = true;
            }

            pass = passText.getText().toString();
            if (pass.isEmpty() || pass.length() < 8) {
                passText.setError("Minimum 8 characters");
                pass_valid = false;
            }
            else {
                passText.setError(null);
                pass_valid = true;
            }

            conpass = conpassText.getText().toString().trim();
            if (!conpass.equals(pass)) {
                conpassText.setError("Mismatch");
                conpass_valid = false;
            }
            else{
                conpassText.setError(null);
                conpass_valid = true;
            }

            if (termsAccepted == 0) {
                termsCheck.setError("Required");
                terms_valid = false;
            }
            else {
                termsCheck.setError(null);
                terms_valid = true;
            }

            if (fname_valid && lname_valid && email_valid && uname_valid && contact_valid && pass_valid && conpass_valid && terms_valid) {

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information

//                                    updateUI(user);

                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    firebaseUser.sendEmailVerification();

                                    DatabaseReference newMemberRef = memberRef.child(firebaseUser.getUid());
                                    newMemberRef.child("aboutme").setValue(null);
                                    newMemberRef.child("email").setValue(email);
                                    newMemberRef.child("fname").setValue(fname);
                                    newMemberRef.child("lname").setValue(lname);
                                    newMemberRef.child("manonymity").setValue(anonymity);
                                    newMemberRef.child("phone").setValue(contactNum);
                                    newMemberRef.child("status").setValue("Student");
                                    newMemberRef.child("username").setValue(uname);

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fname + " " + lname)
//                                            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();

                                    firebaseUser.updateProfile(profileUpdates);

                                    progressDialog.dismiss();

                                    Toast.makeText(SignUpActivity.this, "Authentication successfull.",
                                            Toast.LENGTH_LONG).show();

                                    onBackPressed();

                                } else {
                                    // If sign in fails, display a message to the user.

                                    progressDialog.dismiss();

                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                                }

                                // ...
                            }
                        });

            }
            else {

                Toast.makeText(this, "Please enter the required details accurately",
                        Toast.LENGTH_LONG).show();

            }

        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {

            case R.id.check_anonym:
                anonymity = (anonymity+1)%2;
                break;

            case R.id.check_terms:
                termsAccepted = (termsAccepted+1)%2;
                break;

        }

    }
}
