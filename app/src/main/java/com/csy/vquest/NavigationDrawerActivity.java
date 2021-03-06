package com.csy.vquest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Time;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static String searchKey = null;
    public static String score = null;
    public static String magnitude = null;

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
    public static String current_uname, current_dept, current_deg, current_year;
    private long nFeeds = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseMessaging.getInstance().subscribeToTopic("pushNotifications");

        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.child("member").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                current_uname = dataSnapshot.child("username").getValue().toString();
                current_dept = dataSnapshot.child("department").getValue().toString();
                current_deg = dataSnapshot.child("degree").getValue().toString();
                current_year = dataSnapshot.child("year").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final FragmentManager fm = getSupportFragmentManager();
        if (getApiFragment() == null) {
            fm.beginTransaction().add(new ApiFragment(), FRAGMENT_API).commit();
        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

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
        Bundle bundle = new Bundle();
        bundle.putString("fragment", "home");
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "home_page_fragment")
                .commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
        /*else {

            HomePageFragment homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("home_page_fragment");
            if (homePageFragment != null && homePageFragment.isVisible())
                moveTaskToBack(true);
            else
                super.onBackPressed();
        }*/
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (searchKey != null && !searchKey.equals("")) {
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
        Bundle bundle;

        switch (item.getItemId()) {

            case R.id.nav_home:
                HomePageFragment homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("home_page_fragment");
                if (homePageFragment != null && homePageFragment.isVisible()) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new HomePageFragment();
                bundle = new Bundle();
                bundle.putString("fragment", "home");
                fragment.setArguments(bundle);
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
                fragment = new QuestionFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "question_fragment")
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.nav_my_que:
                HomePageFragment homePageFragment1 = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("my_question_fragment");
                if (homePageFragment1 != null && homePageFragment1.isVisible()) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new HomePageFragment();
                bundle = new Bundle();
                bundle.putString("fragment", "myquestion");
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "my_question_fragment")
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.nav_new_ann:
                AnnouncementFragment announcementFragment = (AnnouncementFragment) getSupportFragmentManager().findFragmentByTag("announcement_fragment");
                if(announcementFragment != null && announcementFragment.isVisible()) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new AnnouncementFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "announcement_fragment")
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.nav_feedback:
                DatabaseReference feedRef = FirebaseDatabase.getInstance().getReference().child("feedback");
                feedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null)
                            nFeeds = dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                AlertDialog.Builder feedbackDialog = new AlertDialog.Builder(this);
                View feedView = LayoutInflater.from(this).inflate(R.layout.feedback_dialog, null);
                final EditText feedText = (EditText) feedView.findViewById(R.id.input_feedback);
                feedbackDialog.setView(feedView);
                feedbackDialog.setTitle("Help & Feedback");
                feedbackDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String feedString = feedText.getText().toString();
                        if(feedString == null || feedString.equals("")){
                            feedText.setError("Required");
                        }
                        else {
                            nFeeds++;
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference newFeedRef = rootRef.child("feedback").child(String.valueOf(nFeeds));
                            newFeedRef.child("username").setValue(current_uname);
                            newFeedRef.child("fstring").setValue(feedString);
                            newFeedRef.child("time").setValue(ServerValue.TIMESTAMP);
                            Toast.makeText(NavigationDrawerActivity.this, "Thankyou for your views", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                feedbackDialog.create().show();

                break;

            case R.id.nav_create_survey:
                CreateSurveyFragment createSurveyFragment = (CreateSurveyFragment) getSupportFragmentManager().findFragmentByTag("create_survey_fragment");
                if (createSurveyFragment != null && createSurveyFragment.isVisible()) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new CreateSurveyFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "create_survey_fragment")
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.nav_my_surveys:
                MySurveyCreatedFragment mySurveyCreatedFragment = (MySurveyCreatedFragment) getSupportFragmentManager().findFragmentByTag("my_survey_created_fragment");
                if(mySurveyCreatedFragment != null && mySurveyCreatedFragment.isVisible()){
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new MySurveyCreatedFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "my_survey_created_fragment")
                        .addToBackStack(null)
                        .commit();

                break;

            case R.id.nav_my_timetable:
              Timetable timetable = (Timetable) getSupportFragmentManager().findFragmentByTag("my_timetable_fragment");
              if(timetable != null && timetable.isVisible())
              {
                  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                  drawer.closeDrawer(GravityCompat.START);
                  break;
              }
                fragment = new Timetable();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "my_timetable_fragment")
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.nav_logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Logout")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                Toast.makeText(NavigationDrawerActivity.this, "You have been successfully logged out.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(NavigationDrawerActivity.this, SignInActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create().show();

                break;

            case R.id.nav_aboutus:
                AboutUsFragment aboutUsFragment = (AboutUsFragment) getSupportFragmentManager().findFragmentByTag("about_us_fragment");
                if(aboutUsFragment != null && aboutUsFragment.isVisible())
                {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new AboutUsFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "about_us_fragment")
                        .addToBackStack(null)
                        .commit();
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void searchQuery(String key) {
        Bundle bundle = new Bundle();
        bundle.putString("key", key);

        Fragment fragment = new AnsFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, "ans_fragment")
                .addToBackStack(null)
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

