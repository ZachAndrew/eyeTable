package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import static andrew.zach.eyeTableP1.R.id.nav_view;

import com.firebase.ui.database.FirebaseListAdapter;

import java.util.Iterator;
import java.util.List;


public class LecturesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    //NavigationView menu=(NavigationView) findViewById(nav_view);//navigation view on activity_leagues page
    private ListView mListView;
    String admin,UserID;

    //*************DEMO ADDON start**********
    private static final String TAG = "Leagues";
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private TextView mUser_name;
    private TextView mUser_email;
    private String textItemClickedFromPOP;
    //*************************end*********




    //type tries
    GenericTypeIndicator <List<String>> t=new GenericTypeIndicator<List<String>>() {};

    //*TO PASS MESSAGE TO A DIFFERENT ACTIVITY*
    public static final String EXTRA_MESSAGE = "andrew.zach.eyeTableP1.MESSAGE";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(Bundle.EMPTY);
        setContentView(R.layout.activity_leagues);


        //:end

        //*Get User ID* START
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            admin = user.getEmail();
            // Username="'"+user.getEmail()+"'";
            UserID = user.getUid();
        } else {
            // No user is signed in
        }

        //*Get User ID* END
        //*Buttons*
        final Button viewMyLeagues = (Button) findViewById(R.id.button17); // 'My Activities' button

        //*ImageViews*
        //final ImageView eyeImage=(ImageView)findViewById(R.id.eye_imageTest);

        //*textView*
        final TextView title = (TextView) findViewById(R.id.textView22);//Title of page

        mListView = (ListView) findViewById(R.id.leaguesList); //a list view referenced by id



        //*scrollView*
       // final HorizontalScrollView scrollView=(HorizontalScrollView) findViewById(R.id.scrollViewPendingLeagues);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //*******************DEMO ADDON start*********************

        mAuth=FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // User is signed in

                    final String uid = user.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference userInfoRef = database.getReference("Users");

                    userInfoRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(uid).child("Name").getValue() == null) {
                                FirebaseAuth.getInstance().signOut();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    View header = navigationView.getHeaderView(0);
                    mUser_name = (TextView) header.findViewById(R.id.user_name);
                    mUser_email = (TextView) header.findViewById(R.id.user_email);

                    mUser_name.setText(user.getDisplayName());
                    mUser_email.setText(user.getEmail());
                } else {
                    //User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(LecturesActivity.this, WelcomePageActivity.class);
                 //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //**************TO PREVENT OTHER ACTIVITIES FROM OVERLAPPING ON LOGOUT**********
                 //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    //******************************************************************************
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                   // finish();
                    startActivity(intent);

                }
            }
        };

        //************************-*end**********************

        //***********************
       // getActionBar().setTitle("***Testing***");
        //***********************
        //*Adding listeners to pull data fromFirebase*
        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       // final DatabaseReference ref = database.getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/");
        final DatabaseReference ref1 = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/");
        final DatabaseReference ref2 = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Clubs/");

        final DatabaseReference personalLeagueData=database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/"+UserID+"/LeagueEnteredIn");
        //DatabaseReference ref1=database.getReference();

        //******************************TEST USER IDS RETRIEVAL start**********************************
       // FirebaseDatabase database=FirebaseDatabase.getInstance().getReference().getDatabase();
        DatabaseReference refToUserIds=database.getReference().child("UserIDs");

        //try to ready data a 'Users' node
        refToUserIds.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()) {
                  List<String> temp2;

                  temp2 = dataSnapshot.getValue(t);

                  for (Iterator<String> iter = temp2.iterator(); iter.hasNext(); ) {
                      String element = iter.next();
                      // 1 - can call methods of element
                      // 2 - can use iter.remove() to remove the current element from the list
                      // ...
                      System.out.println("----------------------" + element + "----------------------------");

                  }
              }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //check values in 'UserIDs' list

        //******************************TEST USER IDS RETREIVEAL end**********************************


//*******************************************************************
        // REMOVED start
      //  final TextView firstLeagueName=(TextView)findViewById(R.id.textView29); //1st League name textfield to set

        // Attach a listener to read the data at our posts reference
        /*
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LectureClass posted = dataSnapshot.getValue(LectureClass.class);
                //*Set TextView 29 to a league name on database*
                if(ref.equals(LectureClass.class)) {
                    firstLeagueName.setText("[LeagueName: " + posted.getLeagueName() + "]" + "\n" + "Admin: " + posted.getAdmin() + "\nLeagueType: " + posted.getTypeOfLeague() + "\nEntry Fee: " + posted.getEntryFee());
               }
                //**********************************************

                System.out.println("************"+posted+"***************");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
*/  //  REMOVED end
        //***********************************************************

        //start:**onClickListener error fix attempt #1**

        //end:** attempt #1**


        //*Firebase listadapter, used to make a list on the page using data on the database
        //this adapter works with our list and displays whats on firebase

ref1.addListenerForSingleValueEvent(new ValueEventListener() {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(dataSnapshot.exists()) {


            FirebaseListAdapter<LectureClass> firebaseListAdapter = new FirebaseListAdapter<LectureClass>(
                    LecturesActivity.this,
                    LectureClass.class,
                    android.R.layout.simple_list_item_1,
                    ref1 //$$$ previously was 'personalLeagueData'

            ) {
                @Override
                //*Displays a list on the app, of leagues currently stored in database in realtime*
                protected void populateView(View v, LectureClass model, int position) {
                    TextView textView = (TextView) v.findViewById(android.R.id.text1);
                    textView.setText("Lecture Name: " + model.getLeagueName() + "\n\n" + "Lecture Admin: " +"\n"
                            + model.getAdmin() + "\n" + "Type Of Activity: "
                            + model.typeOfLeague + "\n" + "Credits: " + model.credits + "\n");
                    //RelativeLayout layoutRoot=(RelativeLayout)findViewById(R.id.content_leagues);
                   // ListView listViewRoot=(ListView) findViewById(R.id.leaguesList);
                   // v.setBackgroundResource(R.drawable.gold_flowers_test1);
                    //v.getResources().getColor(R.color.primary_color);
                    //mListView.setBackgroundColor(getResources().getColor(R.color.primary_color));
                    mListView.setBackgroundColor(getResources().getColor(R.color.goldCustom));


                    //registerForContextMenu(v);  ****'onClick' ERROR SOLVED! This line of code prevented the list from being clickable on opening

                }



            };
            mListView.setAdapter(firebaseListAdapter);

            //Trying to implement clickable list START
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                {

                    int x=position;
                    Object leagueObjWithName=arg0.getItemAtPosition(x);
                    //create if statement to handle whether 'LeagueClass'/'ClubClass' in pressed

                    if(leagueObjWithName.getClass().equals(LectureClass.class)) {
                        LectureClass myObj = (LectureClass) leagueObjWithName;
                        String leagueName = myObj.getLeagueName(); //get leaguename for ViewLeagueActivity page
                        //****
                        String titleForLeaguesActivity = title.getText().toString();

                        System.out.println("*************CONTENTS OF 'leagueName' is:" + leagueName + "**********************************");

                        Intent intent = new Intent(LecturesActivity.this, ViewLectureInfo.class);
                        intent.putExtra(EXTRA_MESSAGE, leagueName);
                        intent.putExtra("title", titleForLeaguesActivity);
                        startActivity(intent);
                    }
                    else if (leagueObjWithName.getClass().equals(ClubClass.class))// if it is of type 'ClubClass'...
                    {
                        ClubClass myObj = (ClubClass) leagueObjWithName;
                        String leagueName = myObj.getClubName(); //get leaguename for ViewLeagueActivity page
                        //****
                        String titleForLeaguesActivity = title.getText().toString();

                        System.out.println("*************CONTENTS OF 'leagueName' is:" + leagueName + "**********************************");

                        Intent intent = new Intent(LecturesActivity.this, ViewClubs.class);
                        intent.putExtra(EXTRA_MESSAGE, leagueName);
                        intent.putExtra("title", titleForLeaguesActivity);
                        startActivity(intent);

                    }




                    //*******2nd attempt end**********
                }

            });


            //start:**CLICKABLE list moved from here**

            //clickable list implementation END
            //end:**CLICKABLE list moved from here**

        }
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});


        // ***********************************************************************


        //*CHECK IF LEAGUES EXIST (generally), IF NOT REMOVE FROM PERSONAL LIST start (in personal user info)*
        //ref to 'LeagueList'
        DatabaseReference refToLeagueList=database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//LeagueList");
        //ref to 'PersonalLeagueList'
        final DatabaseReference refToPersonalLeagueList=database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//"+UserID+"/PersonalLeagueList");

        refToLeagueList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)//if 'LeagueList' exists
                {
                    //get 'LeagueList'
                    final List temp = dataSnapshot.getValue(t);
                    System.out.println("*******************CONTENTS OF 'temp' list ('LeagueList'):" + temp);

                    //compare with 'PersonalLeagueList'
                    //+change personal leagues list based the genereal leagues list
                    refToPersonalLeagueList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                  if(dataSnapshot.exists()) {
                                      //get 'PersonalLeagueList'
                                      List temp2 = dataSnapshot.getValue(t);
                                      System.out.println("*******************CONTENTS OF 'temp2' list ('PersonalLeagueList'):" + temp2);

                                      //now compare temp1 with temp2 i.e. change personal list based if it exist in general list

                                      temp2.retainAll(temp); //*CHANGES PERSONAL LIST('PersonalLeagueList'), ONLY KEEPS WHAT IS IN GENERAL LIST('LeagueList')

                                      //now update 'PersonalLeagueList' for the user before they view it
                                      DatabaseReference updateRefForPersonalLeagueList = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/" + UserID + "/PersonalLeagueList");
                                      updateRefForPersonalLeagueList.setValue(temp2); //update 'PersonalLeagueList' with temp2

                                      //*Now update actual league data ATTEMPT 1 start*
                           /*           DatabaseReference refToPersonalLeagueToGetName=database.getReference("Users/"+UserID+"/LeagueEnteredIn/");

                                      refToPersonalLeagueToGetName.addValueEventListener(new ValueEventListener() {
                                          @Override
                                          public void onDataChange(DataSnapshot dataSnapshot) {
                                              LectureClass tempLeagueValue=dataSnapshot.getValue(LectureClass.class);
                                              String currentLeagueNameAcquired=tempLeagueValue.getLeagueName(); //get leagueName
                                              

                                          }

                                          @Override
                                          public void onCancelled(DatabaseError databaseError) {

                                          }
                                      });*/
                                      //ATTEMPT 1 end
                                  }

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
        });

        //*CHECK IF LEAGUES EXIST, IF NOT REMOVE FROM PERSONAL LIST end*





        //*Trying to implement 'viewMyLeagues' function*
        viewMyLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //*Trying to implement a switch view on the FirebaseListAdapter*
                if(title.getText().toString()=="My Clubs") //check button to see if name is "My Lectures"
                {
                    FirebaseListAdapter<LectureClass> firebaseListAdapter = new FirebaseListAdapter<LectureClass>(
                            LecturesActivity.this,
                            LectureClass.class,
                            android.R.layout.simple_list_item_1,
                            ref1

                    ) {
                        @Override
                        //*Displays a list on the app, of leagues currently stored in database in realtime*
                        protected void populateView(View v, LectureClass model, int position) {
                            TextView textView = (TextView) v.findViewById(android.R.id.text1);
                            textView.setText("Lecture Name: " + model.getLeagueName() + "\n\n" + "Lecture Admin: "+"\n"
                                    + model.getAdmin() + "\n" + "Type Of Activity: "
                                    + model.typeOfLeague + "\n" + "Credits: " + model.credits + "\n");

                            //v.setBackgroundColor(R.color.colorPrimary);
                            //v.getResources().getColor(R.color.primary_color);
                            mListView.setBackgroundColor(getResources().getColor(R.color.goldCustom));


                            //ListView listViewRoot=(ListView) findViewById(R.id.leaguesList);
                            //listViewRoot.setBackgroundResource(R.drawable.gold_flowers_resize1);


                        }

                    };
                    mListView.setAdapter(firebaseListAdapter);
                    viewMyLeagues.setText("My Clubs");
                    title.setText("My Lectures");
                    title.setTextColor(getResources().getColor(R.color.goldCustom));


                } else
                {
                    //*Firebase listadapter, used to make a list on the page using data on the database
                    //this adapter works with our list and displays whats on firebase

                    FirebaseListAdapter<ClubClass> firebaseListAdapter = new FirebaseListAdapter<ClubClass>(
                            LecturesActivity.this,
                            ClubClass.class,
                            android.R.layout.simple_list_item_1,
                            ref2

                    ) {
                        @Override
                        //*Displays a list on the app, of activities currently stored in database in realtime*
                        protected void populateView(View v, ClubClass model, int position) {
                            TextView textView = (TextView) v.findViewById(android.R.id.text1);
                            textView.setText("Club Name: "+model.getClubName()+"\n\n"+"Club President: " +"\n"
                                    +model.getPresident()+"\n"+"Type Of Activity: "
                                    +model.getActivityType()+"\n"+"Description: "+model.getDescription()+"\n");

                              mListView.setBackgroundColor(getResources().getColor(R.color.primary_color));


                        }
                    };

                    mListView.setAdapter(firebaseListAdapter);
                    viewMyLeagues.setText("My Lectures");
                    title.setText("My Clubs");
                    title.setTextColor(getResources().getColor(R.color.primary_color));
                    // ***********************************************************************
                }

            }

        });





        //************************************************
    }
    //*Outside of onCreate*

    //start :addon for extra functionality for listViews  ###ATTEMPT 1###
    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Item Operations");
        menu.add(0, v.getId(), 0, "Edit Item");
        menu.add(0, v.getId(), 0, "Delete Item");
    } */
   /* @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
                .getMenuInfo();
        if (item.getTitle() == "Edit Item") {
            mRowId = info.id;
            DialogFragment_Item idFragment = new DialogFragment_Item();
            idFragment.show(getFragmentManager(), "dialog");
        } else if (item.getTitle() == "Delete Item") {
            mDbHelper.deleteItem(info.id);
            return true;
        }
        return super.onContextItemSelected(item);
    }*/

    //end: addon for extra functionality for listViews

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
        getMenuInflater().inflate(R.menu.leagues, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
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


    public void createLeague(View view) {
        Intent intent = new Intent(this, CreateLectureActivity.class);
        startActivity(intent);
    }
    public void createClub(View view){
        Intent intent=new Intent(this,CreateClubActivity.class);
        startActivity(intent);
    }

    public void joinLeague(View view) {
        Intent intent = new Intent(this, CreateLectureActivity.class);
        startActivity(intent);
    }

    public void manageLeagues(View view){
        Intent intent=new Intent(this, ManageLectureActivity.class);
        startActivity(intent);
    }

    //******DEMO VERSION addon start*****

    public void logOut (View v) {

        FirebaseAuth.getInstance().signOut();

        /*
        Intent intent = new Intent(LecturesActivity.this, LogInActivity.class);
        //***
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //***
        //**************TO PREVENT OTHER ACTIVITIES FROM OVERLAPPING ON LOGOUT**********
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        //******************************************************************************
        */
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

    //*******DEMO VERSION addon end

    public void viewLeague(View view) {
        int leagueFieldId = view.getId();
        TextView leagueNameField = (TextView) findViewById(leagueFieldId);
        String leagueName = leagueNameField.getText().toString().trim();
        Intent intent = new Intent(this, ViewLectureInfo.class);
        intent.putExtra(EXTRA_MESSAGE, leagueName);
        startActivity(intent);
    }
    //******************end*****

}
