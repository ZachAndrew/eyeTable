package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Zach on 17/11/2017.
 */

public class ClubsAndSocsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Leagues";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private TextView mUser_name;
    private TextView mUser_email;
    //   private TextView mProfile_name;
    //   private TextView mProfile_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lect_and_clubs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //*ListView*
        ListView myList=(ListView)findViewById(R.id.listViewLeagueCodes);// a view for LeagueCodes codes, stored on league creation


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
                    //     mProfile_name = (TextView) findViewById(R.id.profile_name);
                    //     mProfile_email = (TextView) findViewById(R.id.profile_email);

                    mUser_name.setText(user.getDisplayName());
                    mUser_email.setText(user.getEmail());
                    //    mProfile_name.setText(user.getDisplayName());
                    //    mProfile_email.setText(user.getEmail());
                } else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(andrew.zach.eyeTableP1.ClubsAndSocsActivity.this, WelcomePageActivity.class);
                    startActivity(intent);
                }
            }
        };

    /*    DatabaseReference refToLeagueCodes= FirebaseDatabase.getInstance().getReferenceFromUrl("");


        FirebaseListAdapter <List> listAdapter=new FirebaseListAdapter<List>(

                LectureCodesActivity.this,
                List.class,
                android.R.layout.simple_list_item_1,
                refToLeagueCodes


        ) {
            @Override
            protected void populateView(View v, List model, int position) {

            }
        };*/


    }//*outside onCreate*

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
        getMenuInflater().inflate(R.menu.league_codes, menu);
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
            Intent intent = new Intent(this, ClubsAndSocsActivity.class);
            startActivity(intent);
            //end
        } else if (id == R.id.nav_fixtures_and_results) {
            Intent intent = new Intent(this, EyeTableActivity.class);
            startActivity(intent);
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

    public void changeName(View v) {
        Intent intent = new Intent(this, ChangeNameActivity.class);
        startActivity(intent);
    }

  /*  public void changeEmail(View v) {
        Intent intent = new Intent(this, ChangeEmailActivity.class);
        startActivity(intent);
    }

    public void changePassword(View v) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        startActivity(intent);
    }

    public void deleteAccount(View v) {
        Intent intent = new Intent(this, DeleteAccountActivity.class);
        startActivity(intent);
    }*/

    public void logOut(View v) {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onStart() {
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
