package com.csy.vquest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
        } else {

            HomePageFragment homePageFragment = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("home_page_fragment");
            if (homePageFragment != null && homePageFragment.isVisible())
                moveTaskToBack(true);
            else
                super.onBackPressed();
        }
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
                        .addToBackStack("questionFragment")
                        .commit();
                break;

            case R.id.nav_my_que:
                HomePageFragment homePageFragment1 = (HomePageFragment) getSupportFragmentManager().findFragmentByTag("my_question_fragment");
                if(homePageFragment1 != null && homePageFragment1.isVisible()) {
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
                        .addToBackStack("myQuestionFragment")
                        .commit();

                break;

            case R.id.nav_feedback:
                AlertDialog.Builder feedbackDialog = new AlertDialog.Builder(this);
                View feedView = LayoutInflater.from(this).inflate(R.layout.feedback_dialog, null);
                final EditText feedText = (EditText) feedView.findViewById(R.id.input_feedback);
                feedbackDialog.setView(feedView);
                feedbackDialog.setTitle("Help & Feedback");
                feedbackDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference newFeedRef = rootRef.child("feedback").child(firebaseAuth.getCurrentUser().getUid());
                        newFeedRef.child("fstring").setValue(feedText.getText().toString());
                        newFeedRef.child("time").setValue(ServerValue.TIMESTAMP);
                        Toast.makeText(NavigationDrawerActivity.this, "Thankyou for your views", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                feedbackDialog.create().show();

                break;

            case R.id.nav_my_ans:
                CreateSurveyFragment createSurveyFragment = (CreateSurveyFragment) getSupportFragmentManager().findFragmentByTag("create_survey_fragment");
                if(createSurveyFragment != null && createSurveyFragment.isVisible()){
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    break;
                }
                fragment = new CreateSurveyFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment, fragment, "create_survey_fragment")
                        .addToBackStack("createsurveyfragment")
                        .commit();

                break;

            case R.id.nav_logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                break;


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
