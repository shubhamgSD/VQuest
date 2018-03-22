package com.csy.vquest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.widget.Toast.makeText;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passText;
    private Button loginBtn;
    private TextView signupView;
    private FirebaseAuth firebaseAuth;

    private String email;
    private String password;
    private TextView forgotpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        emailText = (EditText) findViewById(R.id.input_email);
        passText = (EditText) findViewById(R.id.input_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        signupView = (TextView) findViewById(R.id.link_signup);
        forgotpassword = (TextView)findViewById(R.id.link_forgotpassword);

        loginBtn.setOnClickListener(this);
        signupView.setOnClickListener(this);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this, PasswordActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_login:

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                email = emailText.getText().toString().trim();
                password = passText.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    if(user.isEmailVerified()){

                                        progressDialog.dismiss();

                                        Intent intent = new Intent(SignInActivity.this, NavigationDrawerActivity.class);
                                        startActivity(intent);
                                    }
                                    else{

                                        progressDialog.dismiss();

                                        Toast.makeText(SignInActivity.this, "Verify your email by following the link sent to your registered email address.",
                                                Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    // If sign in fails, display a message to the user.

                                    progressDialog.dismiss();

                                    makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
                break;

            case R.id.link_signup:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;

        }

    }
}