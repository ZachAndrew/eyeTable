package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static andrew.zach.eyeTableP1.LecturesActivity.EXTRA_MESSAGE;
import static andrew.zach.eyeTableP1.Pop.EXTRA_MESSAGE2;

import static andrew.zach.eyeTableP1.Pop.EXTRA_MESSAGE_BUTTON_ID;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;

public class EyeTableActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static final String EXTRA_MESSAGE_ID_OF_BUTTON ="andrew.zach.eyeTableP1.MESSAGE_BUTTON_ID"; //a key to contain the name of the button id
    public static final String EXTRAMESSAGE = "andrew.zach.eyeTableP1.MESSAGE";


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
    Button updateButton;
    Button updateButton2;


    //start:get item from pop class
    //*****GET EXTRA_MESSAGE CODE FROM 'LecturesActivity.java'  START**********
    final Intent intent = getIntent();
//    final String nameOfValueReceivedFromPOP = intent.getStringExtra(EXTRA_MESSAGE2);
    String nameOfValueReceivedFromPOP ="EMPTY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eye_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Initialise Firebase Database
        final FirebaseDatabase database=FirebaseDatabase.getInstance();


        final Intent intent=getIntent();
        String tempStorageForPopValue=intent.getStringExtra(EXTRA_MESSAGE2);
        int tempButtonIDFromPop=intent.getIntExtra(EXTRA_MESSAGE_BUTTON_ID,0);
        nameOfValueReceivedFromPOP=tempStorageForPopValue;

        //check value received from pop
        System.out.println("***************Value of 'nameOfValueReceivedFromPOP':" + nameOfValueReceivedFromPOP + "*******************");
        System.out.println("**************INITIAL Value of 'tempButtonIDFromPop':" + tempButtonIDFromPop + "*******************");


        //%%%%%%%%%%%%%TRY TO SET BUTTONS USING INFO FROM POP %%%%%%%%%%%%%%%%%
        updateButton=(Button)findViewById(tempButtonIDFromPop);
        if(tempStorageForPopValue!=null) {
            updateButton.setText("TESTING");
            updateButton.setTextColor(YELLOW);

        }
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


        //%%%%%%%%%%%%%%%%%%%TRYING TO SETUP A TABLE PAGE SETUP%%%%%%%%%%%%%%%
        //get 'Buttons' node ref on DB
        DatabaseReference mRefDBButtons=database.getReference().child("Buttons");

        //if values exist, try to pull them down and instantiate them into view
        mRefDBButtons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Log.v(TAG,"£££ CHECKING CHILD KEY £££"+ childDataSnapshot.getKey()); //displays the key for the node
                        Log.v(TAG,"£££ CHECKING CHILD VALUE £££"+ childDataSnapshot.getValue());   //gives the value for given keyname
                        System.out.println("KEY BUTTON:"+childDataSnapshot.getKey()+"\n Value:"+childDataSnapshot.getValue());
                      //  System.out.println("$$$$$$$$VALUE OF CHILD IN 'BUTTONS node on DB are:"+childDataSnapshot.getValue());

                        int tempKeyStorage=Integer.parseInt(childDataSnapshot.getKey()); //get value of current button key
                        String tempValStorage=childDataSnapshot.getValue(String.class);//get value of current button value

                        if(childDataSnapshot.exists()) //if a button key has been stored
                        {
                            System.out.println("++++++++++++++++++ DATA EXISTS, PROCEEDING TO UPDATE BUTTON CODE  ++++++++++++++++++");
                            //update table view interface
                            final Button updateButton2 = (Button) findViewById(tempKeyStorage);
                            updateButton2.findViewById(tempKeyStorage);
                            updateButton2.setText(tempValStorage);
                            updateButton2.setTextColor(GREEN);

                        }
                        else {
                            System.out.println("++++++++++++++++++ HAVE NOT BEEN THROUGH BUTTON UPDATE PROCEDURE +++++++++++++++++++++++++++++++++");
                        }

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

//  #f      Bundle  extras=getIntent().getExtras();
//        {
//            if (extras != null) {
//                String value = extras.getString("EXTRA_MESSAGE2");
//                System.out.println("***********Value of 'value' is: "+value+" **************************");
//                //The key argument here must match that used in the other activity
//            }
//  -   }

// #f   Intent intent2 = getIntent();
// -  textItemClickedFromPOP = intent2.getStringExtra(EXTRA_MESSAGE);
        //start:get item from pop class
        //:end

      /*  mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE); */

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
                    Intent intent = new Intent(EyeTableActivity.this, WelcomePageActivity.class);
                    startActivity(intent);
                }
            }
        };


        //*Buttons*
        final Button buttonL1 = (Button) findViewById(R.id.button_slot_rc11); //test button to see if view can be implemented on after intent
        final Button buttonL2 = (Button) findViewById(R.id.button_slot_rc12);
        final Button buttonL3 = (Button) findViewById(R.id.button_slot_rc13);
        final Button buttonL4 = (Button) findViewById(R.id.button_slot_rc14);
        final Button buttonL5 = (Button) findViewById(R.id.button_slot_rc15);
        //*Name of Buttons* (i.e. lecture names)
        final String lectureButtonName1 = buttonL1.getText().toString();
        final String lectureButtonName2 = buttonL2.getText().toString();
        final String lectureButtonName3 = buttonL3.getText().toString();
        final String lectureButtonName4 = buttonL4.getText().toString();
        final String lectureButtonName5 = buttonL5.getText().toString();

        //textViews
        TextView textView_time=(TextView)findViewById(R.id.textView_time);//a text view for the time box

        textView_time.setText("Time:\n\n07.00\n\n08.00\n\n09.00\n\n10.00\n\n11.00\n\n12.00\n\n13.00\n\n14.00\n\n15.00\n\n16.00\n\n17.00\n\n18.00\n\n19.00\n\n20.00\n\n21.00\n\n22.00\n\n");


        if (nameOfValueReceivedFromPOP != null && !nameOfValueReceivedFromPOP.equals("Slot")) {
            buttonL2.setText(nameOfValueReceivedFromPOP);//trying to set the value received from pop.class into button text name
        }
        System.out.println("***************Value of 'nameOfValueReceivedFromPOP':" + nameOfValueReceivedFromPOP + "*******************");

        //(Removal 1)start:********************* REMOVING LARGE CHUNK OF REDUNDENT CODE ************************
        /*
        //final String lectureButtonName=getResources().getResourceEntryName(R.id.button_monday_l1);

        buttonL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("$$$$$$$$$$$$$$$$$$$$$VALUE of CONONCIAL NAME:"+this.getClass().getCanonicalName()+"$$$$$$$$$$$$$$$$$$$$");
                //System.out.println("$$$$$$$$$$$$$$$$$$$$$VALUE of get NAME:"+this.getClass().getName()+"$$$$$$$$$$$$$$$$$$$$$$$$$$$");

                if (!buttonL1.getText().toString().equals("Slot")) {
                    System.out.println("***********************Value of 'lectureButtonName':" + lectureButtonName1 + "*******************************");
                    Intent intent = new Intent(EyeTableActivity.this, ViewLectureInfo.class);//take info from here and pass into view
                    intent.putExtra(EXTRA_MESSAGE, lectureButtonName1);
                    //intent.putExtra("title",titleForLeaguesActivity);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EyeTableActivity.this, Pop.class);
                    startActivity(intent);
                }
            }
        });
        buttonL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //***ADD A DB LISTENER ***
               DatabaseReference mRefB_rc12=database.getReference().child("Buttons/"+buttonL2.getId());

               mRefB_rc12.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {

                       //listener to check if current value of button has been changed or not
                       //if button has already been changed...
                       if(dataSnapshot.exists()){
                           //get button name from DB, store it in view
                           String tempButtonNameStorage=dataSnapshot.getValue(String.class);
                           //if user already gave button a name, then keep it
                           if(!tempButtonNameStorage.equals("Slot")){
                           }
                       }
                            //****TESTING THIS BUTTON WITH THE POP CLASS****
                            if (!buttonL2.getText().toString().equals("Slot")) {
                                System.out.println("***********************Value of 'lectureButtonName':" + lectureButtonName2 + "*******************************");
                                Intent intent = new Intent(EyeTableActivity.this, ViewLectureInfo.class);//take info from here and pass into view

                                //get lecture button name
                                String tempButtonName = buttonL2.getText().toString();
                                intent.putExtra(EXTRA_MESSAGE, tempButtonName);

                                //intent.putExtra("title",titleForLeaguesActivity);

                                //***Implementation of button ID passing to pop***
                                //***TRYING TO INPUT NAME OF BUTTON ID*** start:
                                Integer tempButtonID = buttonL2.getId();
                                //intent.putExtra("EXTRA_MESSAGE_NAME_OF_BUTTON_key",EXTRA_MESSAGE_NAME_OF_BUTTON );
                                System.out.println("************************ EyeTableActivity:Getting 'buttonL2' id, which is:" + buttonL2.getId() + "********************************");
                                intent.putExtra(EXTRA_MESSAGE_ID_OF_BUTTON, tempButtonID); //pass the name of the button to the pop class

                                //*******


                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(EyeTableActivity.this, Pop.class);
                                //***TRYING TO INPUT NAME OF BUTTON ID*** start:
                                Integer buttonId = buttonL2.getId();
                                //intent.putExtra("EXTRA_MESSAGE_NAME_OF_BUTTON_key",EXTRA_MESSAGE_NAME_OF_BUTTON );
                                System.out.println("************************ EyeTableActivity:Getting 'buttonL2' id, which is:" + buttonL2.getId() + "********************************");
                                intent.putExtra(EXTRA_MESSAGE_ID_OF_BUTTON, buttonL2.getId()); //pass the id of the button to the pop class

                                //try get intent from pop if message exists
                                //@@@@nameOfValueReceivedFromPOP = intent.getStringExtra(EXTRA_MESSAGE2);
                                if (nameOfValueReceivedFromPOP != null && !nameOfValueReceivedFromPOP.equals("Slot")) {
                                    buttonL2.setText(nameOfValueReceivedFromPOP);//trying to set the value received from pop.class into button text name
                                }
                                System.out.println("***************Value of 'nameOfValueReceivedFromPOP':" + nameOfValueReceivedFromPOP + "*******************");

                                //:end
                                startActivity(intent);
                            }

                   }

                   //if listener has error
                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });

            }
        });
        buttonL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("***********************Value of 'lectureButtonName':" + lectureButtonName3 + "*******************************");
                Intent intent = new Intent(EyeTableActivity.this, ViewLectureInfo.class);//take info from here and pass into view
                intent.putExtra(EXTRA_MESSAGE, lectureButtonName3);
                //intent.putExtra("title",titleForLeaguesActivity);
                startActivity(intent);
            }
        });
        buttonL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("***********************Value of 'lectureButtonName':" + lectureButtonName4 + "*******************************");
                Intent intent = new Intent(EyeTableActivity.this, ViewLectureInfo.class);//take info from here and pass into view
                intent.putExtra(EXTRA_MESSAGE, lectureButtonName4);
                //intent.putExtra("title",titleForLeaguesActivity);
                startActivity(intent);
            }
        });
        buttonL5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("***********************Value of 'lectureButtonName':" + lectureButtonName5 + "*******************************");
                Intent intent = new Intent(EyeTableActivity.this, ViewLectureInfo.class);//take info from here and pass into view
                intent.putExtra(EXTRA_MESSAGE, lectureButtonName5);
                //intent.putExtra("title",titleForLeaguesActivity);
                startActivity(intent);
            }
        });
        */
        //(Removal 1):end

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Intent intent=new Intent(this, LecturesActivity.class);
            startActivity(intent);
            //super.onBackPressed();
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

    public void onClick (View v)
    {
        this.findViewById(v.getId());
        System.out.println("******************************CURRENT BUTTON ID:"+v.getId()+"******************************");

        Integer tempButtonID=v.getId();
        final Button mRefButton=(Button)findViewById(tempButtonID);

        /*start:****BUTTON DEBUG CHECKING ***** #s
        Toast.makeText(this, "The ID of the Pressed button is:"+v.getId(), Toast.LENGTH_SHORT).show();

        Button tempTestButton=(Button)findViewById(v.getId());
        tempTestButton.setText("SUCCESS");
        tempTestButton.setTextColor(GREEN);
        /:end */

        //start:*IMPLEMENTING a GENERAL BUTTON ONCLICK FUNCTIONALITY*
        DatabaseReference mRefGeneralButton=database.getReference().child("Buttons/"+mRefButton.getId());


        mRefGeneralButton.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //listener to check if current value of button has been changed or not
                //if button has already been changed...
                if(dataSnapshot.exists()){
                    //get button name from DB, store it in view
                    String tempButtonNameStorage=dataSnapshot.getValue(String.class);
                    //if user already gave button a name, then keep it
                    if(!tempButtonNameStorage.equals("Slot")){
                    }
                }
                //****TESTING THIS BUTTON WITH THE POP CLASS****
                if (!mRefButton.getText().toString().equals("Slot")) {
                    System.out.println("***********************Value of 'lectureButtonName':" + mRefButton.getText() + "*******************************");
                    Intent intent = new Intent(EyeTableActivity.this, ViewLectureInfo.class);//take info from here and pass into view

                    //get lecture button name
                    String tempButtonName = mRefButton.getText().toString();
                    intent.putExtra(EXTRA_MESSAGE, tempButtonName);

                    //intent.putExtra("title",titleForLeaguesActivity);

                    //***Implementation of button ID passing to pop***
                    //***TRYING TO INPUT NAME OF BUTTON ID*** start:
                    Integer tempButtonID = mRefButton.getId();
                    //intent.putExtra("EXTRA_MESSAGE_NAME_OF_BUTTON_key",EXTRA_MESSAGE_NAME_OF_BUTTON );
                    System.out.println("************************ EyeTableActivity:Getting ^^^"+mRefButton+"^^^ id, which is:" + mRefButton.getId() + "********************************");
                    intent.putExtra(EXTRA_MESSAGE_ID_OF_BUTTON, tempButtonID); //pass the name of the button to the pop class

                    //*******
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EyeTableActivity.this, Pop.class);
                    //***TRYING TO INPUT NAME OF BUTTON ID*** start:
                    Integer buttonId = mRefButton.getId();
                    //intent.putExtra("EXTRA_MESSAGE_NAME_OF_BUTTON_key",EXTRA_MESSAGE_NAME_OF_BUTTON );
                    System.out.println("************************ EyeTableActivity:Getting ^^^"+mRefButton+"^^^ id, which is:" + mRefButton.getId() + "********************************");
                    intent.putExtra(EXTRA_MESSAGE_ID_OF_BUTTON, buttonId); //pass the id of the button to the pop class

                    //try get intent from pop if message exists
                    //@@@@nameOfValueReceivedFromPOP = intent.getStringExtra(EXTRA_MESSAGE2);

                    //start: **************** Obsolete  code ************
                    /*
                    if (nameOfValueReceivedFromPOP != null && !nameOfValueReceivedFromPOP.equals("Slot")) {
                        mRefButton.setText(nameOfValueReceivedFromPOP);//trying to set the value received from pop.class into button text name
                        //set colour
                        mRefButton.setTextColor(GREEN);
                    }
                    */
                    //:end*******************************************
                    System.out.println("***************Value of 'nameOfValueReceivedFromPOP':" + nameOfValueReceivedFromPOP + "*******************");

                    //:end
                    startActivity(intent);
                }

            }

            //if listener has error
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //*******************************************





        //:end


    }
}