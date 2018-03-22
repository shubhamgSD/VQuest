package com.csy.vquest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csy.vquest.model.SentimentInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.language.v1.CloudNaturalLanguage;
import com.google.api.services.language.v1.CloudNaturalLanguageRequest;
import com.google.api.services.language.v1.CloudNaturalLanguageScopes;
import com.google.api.services.language.v1.model.AnalyzeSentimentRequest;
import com.google.api.services.language.v1.model.AnalyzeSentimentResponse;
import com.google.api.services.language.v1.model.Document;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public  static String searchKey = null;
    public static String score=null;
    private String magnitude="mag5";

//    public String getScore() {
//        return score;
//    }
//
//    public void setScore(String score) {
//        this.score = score;
//    }
//
//    public String getMagnitude() {
//        return magnitude;
//    }
//
//    public void setMagnitude(String magnitude) {
//        this.magnitude = magnitude;
//    }

    private static final String FRAGMENT_API = "api";
    private static final int LOADER_ACCESS_TOKEN = 1;

    FirebaseAuth firebaseAuth;
    public static String current_uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("member").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_uname = dataSnapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final FragmentManager fm = getSupportFragmentManager();
        if (getApiFragment() == null) {
            fm.beginTransaction().add(new ApiFragment(), FRAGMENT_API).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        TextView emailView = (TextView) headerView.findViewById(R.id.nav_emailView);
        emailView.setText(firebaseAuth.getCurrentUser().getEmail());

        TextView unameView = (TextView) headerView.findViewById(R.id.nav_unameView);
        unameView.setText(firebaseAuth.getCurrentUser().getDisplayName());

        ImageView imageView = (ImageView) headerView.findViewById(R.id.nav_imageView);
        imageView.setImageURI(firebaseAuth.getCurrentUser().getPhotoUrl());

        Fragment fragment = new HomePageFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "home_page_fragment")
                .commit();


       // prepareApi();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            HomePageFragment homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("home_page_fragment");
            if (homePageFragment != null && homePageFragment.isVisible())
                moveTaskToBack(true);
            else
                super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if(searchKey != null && !searchKey.equals(""))
        {
            searchQuery(searchKey);
        }
    }

    @Override
    protected void onPause() {
        searchKey = "";
        super.onPause();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment;

        switch (item.getItemId()) {

            case R.id.nav_home:
                HomePageFragment homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("home_page_fragment");
                if (homePageFragment != null && homePageFragment.isVisible()) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new HomePageFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "home_page_fragment")
                        .commit();
                break;

            case R.id.nav_new_que:
                QuestionFragment questionFragment = (QuestionFragment) getSupportFragmentManager().findFragmentByTag("question_fragment");
                if (questionFragment != null && questionFragment.isVisible()) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                startAnalyze("Bastard");
                fragment = new QuestionFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "question_fragment")
                        .addToBackStack("questionFragment")
                        .commit();
                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_feedback:
//                    Intent intent1 = new Intent(this,temp_activity.class);
//                    startActivity(intent1);
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        while(true)
//                        {
//                            if(score!=null)
//                            {
//                                Toast.makeText(NavigationDrawerActivity.this, score, Toast.LENGTH_SHORT).show();
//                                Log.d("Score",score);
//                                break;
//                            }
//                        }
//
//                    }
//                });
//                thread.start();
                startAnalyze("Bastard");

                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void searchQuery(String key)
    {
        Bundle bundle  = new Bundle();
        bundle.putString("key",key);

        Fragment fragment = new AnsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment, fragment, "ans_fragment")
            .addToBackStack("ansFragment")
            .commit();
    }


//    @Override
//    public void onSentimentReady(SentimentInfo sentiment) {
//
//        Toast.makeText(this, "OnReady", Toast.LENGTH_SHORT).show();
//        //score = String.valueOf(sentiment.score);
//
//
//    }




    public void startAnalyze(String text) {

        prepareApi();
        getApiFragment().analyzeSentiment(text);
    }
//
//    public void uiThread()
//    {
//        Toast.makeText(this, NavigationDrawerActivity.score, Toast.LENGTH_SHORT).show();
//    }

    private ApiFragment getApiFragment() {
        return (ApiFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_API);
    }



    private void prepareApi() {
        getSupportLoaderManager().initLoader(LOADER_ACCESS_TOKEN, null,
                new LoaderManager.LoaderCallbacks<String>() {
                    @Override
                    public Loader<String> onCreateLoader(int id, Bundle args) {
                        return new AccessTokenLoader(NavigationDrawerActivity.this);
                    }

                    @Override
                    public void onLoadFinished(Loader<String> loader, String token) {
                        getApiFragment().setAccessToken(token);
                    }

                    @Override
                    public void onLoaderReset(Loader<String> loader) {
                    }
                });
    }

}

