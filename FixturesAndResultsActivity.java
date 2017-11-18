package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FixturesAndResultsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Leagues";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private ArrayList<FixturesModel> fixturesProperties = new ArrayList<>();

    private TextView mUser_name;
    private TextView mUser_email;
    private String mGameweek;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    TextView mGameweek_Field;
    ListView mListView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixtures_and_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    View header = navigationView.getHeaderView(0);
                    mUser_name = (TextView) header.findViewById(R.id.user_name);
                    mUser_email = (TextView) header.findViewById(R.id.user_email);

                    mUser_name.setText(user.getDisplayName());
                    mUser_email.setText(user.getEmail());
                } else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(FixturesAndResultsActivity.this, eye.class);
                    startActivity(intent);
                }
            }
        };

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference gameweekRef = database.getReference("Premier League");
        mGameweek_Field = (TextView) findViewById(R.id.textView89);

        /* #4gameweekRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mGameweek == null || mGameweek == dataSnapshot.child("Current Gameweek").getValue().toString()) {
                    mGameweek_Field.setText("Week " + dataSnapshot.child("Current Gameweek").getValue().toString());
                    mGameweek = dataSnapshot.child("Current Gameweek").getValue().toString();
                    DatabaseReference myRef = database.getReference("Premier League").child(mGameweek).child("Matches");

                    // List Views
                    mListView = (ListView) findViewById(R.id.listView);
                    final ArrayAdapter<FixturesModel> fixturesAdapter = new fixturesArrayAdapter(FixturesAndResultsActivity.this, 0, fixturesProperties);
                    mListView.setAdapter(fixturesAdapter);
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            fixturesAdapter.clear();
                            fixturesAdapter.notifyDataSetChanged();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String homeBadge = child.child("Home").getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                                String homeTeam = child.child("Home").getValue().toString();
                                String homeGoals = child.child("Home Goals").getValue().toString();
                                String awayGoals = child.child("Away Goals").getValue().toString();
                                String awayTeam = child.child("Away").getValue().toString();
                                String awayBadge = child.child("Away").getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                                fixturesAdapter.add(new FixturesModel(homeBadge, homeTeam, homeGoals, awayGoals, awayTeam, awayBadge));
                                fixturesAdapter.notifyDataSetChanged();
                            }
                            mProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }

                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        }); */


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fixtures_and_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_leagues) {
            Intent intent = new Intent(this, LecturesActivity.class);
            startActivity(intent);
            //*Adding League Codes function* start
        } else if (id == R.id.nav_league_codes) {
            Intent intent = new Intent(this, LectureCodesActivity.class);
            startActivity(intent);
            //end
        } else if (id == R.id.nav_fixtures_and_results) {
            onBackPressed();
        } else if (id == R.id.nav_how_to_play) {
            Intent intent = new Intent(this, HowToPlayActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void previousWeek (View v) {
        int x = Integer.parseInt(mGameweek);
        if(x != 1) {
            x--;
            mGameweek = Integer.toString(x);
            mGameweek_Field.setText("Week " + mGameweek);

            DatabaseReference myRef = database.getReference("Premier League").child(mGameweek).child("Matches");

            // List Views
            mListView = (ListView) findViewById(R.id.listView);
            final ArrayAdapter<FixturesModel> previousFixturesAdapter = new fixturesArrayAdapter(FixturesAndResultsActivity.this, 0, fixturesProperties);
            mListView.setAdapter(previousFixturesAdapter);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    previousFixturesAdapter.clear();
                    previousFixturesAdapter.notifyDataSetChanged();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String homeBadge = child.child("Home").getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                        String homeTeam = child.child("Home").getValue().toString();
                        String homeGoals = child.child("Home Goals").getValue().toString();
                        String awayGoals = child.child("Away Goals").getValue().toString();
                        String awayTeam = child.child("Away").getValue().toString();
                        String awayBadge = child.child("Away").getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                        previousFixturesAdapter.add(new FixturesModel(homeBadge, homeTeam, homeGoals, awayGoals, awayTeam, awayBadge));
                        previousFixturesAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }

    public void nextWeek (View v) {
        int x = Integer.parseInt(mGameweek);
        if(x != 38) {
            x++;
            mGameweek = Integer.toString(x);
            mGameweek_Field.setText("Week " + mGameweek);

            DatabaseReference myRef = database.getReference("Premier League").child(mGameweek).child("Matches");

            // List Views
            mListView = (ListView) findViewById(R.id.listView);
            final ArrayAdapter<FixturesModel> fixturesAdapter = new fixturesArrayAdapter(FixturesAndResultsActivity.this, 0, fixturesProperties);
            mListView.setAdapter(fixturesAdapter);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    fixturesAdapter.clear();
                    fixturesAdapter.notifyDataSetChanged();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String homeBadge = child.child("Home").getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                        String homeTeam = child.child("Home").getValue().toString();
                        String homeGoals = child.child("Home Goals").getValue().toString();
                        String awayGoals = child.child("Away Goals").getValue().toString();
                        String awayTeam = child.child("Away").getValue().toString();
                        String awayBadge = child.child("Away").getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                        fixturesAdapter.add(new FixturesModel(homeBadge, homeTeam, homeGoals, awayGoals, awayTeam, awayBadge));
                        fixturesAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }

            });
        }
    }

    public void logOut (View v) {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}