package com.csy.vquest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private Button btn;
    FirebaseAuth firebaseAuth;
    private static final int DELAY = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;

                        if (currentUser == null) {
                            intent = new Intent(MainActivity.this, SignInActivity.class);
                        } else {
                            intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
                        }

                        startActivity(intent);
                        finish();
                    }
                },
                DELAY
        );


    }

}
