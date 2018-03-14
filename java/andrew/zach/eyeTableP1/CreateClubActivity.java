package andrew.zach.eyeTableP1;

/**
 * Created by Zach on 05/12/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateClubActivity extends AppCompatActivity {


    //***Variables***
    private ListView mListView;
    // private List <String>leaguesList=new ArrayList<String>();
    private ArrayList<String> leaguesList = new ArrayList<>();

    //***Boolean var, for league created***
    boolean leagueCreated = false;

    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
    };

    //*Variables needed for getting User UID implmentation*
    private static final String TAG = "Login";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String Username = "No Username Yet";//default, for branching separate database
    String UserID = "No UID yet";

    public static final String EXTRA_MESSAGE = "andrew.zach.lmsprototype2_android_version.MESSAGE";
    //*****************************************************

    String clubname = "*Default*", admin = "*DEFAULT*", paymentAmount = "7.5", typeOfleague = "Premier League", lectureCode = "NULL", description="Nothing yet", activityType="*Not Specified*";
    //*List*
    final List<ClubClass> list = new ArrayList<>();
    //displayNames,participants lists
    List<String> displayNames = new ArrayList<>();
    List<String> participants = new ArrayList<>();

    //temp clubtime
    String clubTime="*DEFAULT*";


    List<String> stringArray;

    //Counter for x
    int x = 0, playernum;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); //get an instence of the database
    DatabaseReference mCreateClub = mRootRef.child("Clubs");
    DatabaseReference players = mRootRef.child("'UserID'");
    DatabaseReference mLeagueWithMembersChild = mRootRef.child("LeagueAndMembers");


    //access to data in sub children
    //   DatabaseReference mleagueData1=FirebaseDatabase.getInstance().getReferenceFromUrl("https://lmsproto1.firebaseio.com/Leagues/"+x+"/"+clubname);
//    final DatabaseReference mplayersData1= FirebaseDatabase.getInstance().getReferenceFromUrl("https://lmsproto1.firebaseio.com/Users/Players");
    // List l= (List) FirebaseDatabase.getInstance().getReferenceFromUrl("https://lmsproto1.firebaseio.com/Users");
    //*Extract List from 'Player Entries' on database*
    DatabaseReference myPlayerList = mRootRef.child("Users");

    //DatabaseReference mAddToExistingLeague=mCreateLeague.child(list);
    //**************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        //***clear lists***
        displayNames.clear();
        participants.clear();
        //*****************

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

        //*League name entered by user*
        //  EditText getLeagueNameInput=(EditText)findViewById(R.id.editTextleagueNameEntry);
       // final TextView notif = (TextView) findViewById(R.id.textView24);//notification for user


        //*Edit Text fields*
        final EditText editClubName = (EditText) findViewById(R.id.editText_clubName);
        final EditText editAdmin=(EditText)findViewById(R.id.editText_admin);
        final EditText editDescription=(EditText)findViewById(R.id.editText_Descrition);
        final EditText editActivityType=(EditText)findViewById(R.id.editText_activtyType_info);
        final EditText editExtraInfo2=(EditText)findViewById(R.id.editText_extraInfo2);

        //*Buttons*
        final Button makeClubButton = (Button) findViewById(R.id.makeClub);


        makeClubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //***** CREATE A LIST (link it to the local 'list')********
                List<ClubClass> listOfCreatedClubs = list;
                //***************************

                //***clear lists***
                displayNames.clear();
                participants.clear();
                //*****************

                if (editClubName.getText().toString().isEmpty()) {
                    clubname = "Default";
                } else {
                    clubname = editClubName.getText().toString(); //get clubname from user
                }
                //+  admin=editLeagueAdmin.getText().toString(); //get admin name from user
                //+ playernum=editPlayerNum.getText().toString();//to get player number max size from user

                //Start:******* GET INFO FROM EDIT TEXT BOXES and create a 'ClubClass' with it ********
                admin=editAdmin.getText().toString(); //get the admin of the club
                description=editDescription.getText().toString(); //get the description of the club
                activityType=editActivityType.getText().toString();//get activity type from textbox


                //*Store into an obj of type 'LectureClass'
                // $$$ final ClubClass ActivityWithInfo = new ClubClass(clubname, admin, playernum, participants, displayNames);
                final ClubClass ActivityWithInfo = new ClubClass(clubname, admin,description,activityType,clubTime);

                System.out.println("********************BEFORE '.setParticiapnts'& '.setDisplayNames' ==>'ActivityWithInfo':" + ActivityWithInfo + "************************");
                //****CHANGE PARTICIPANTS/DISPLAY NAMES attempt 1 start*****
                //reset names to empty on creation
                ActivityWithInfo.setParticipants();
                ActivityWithInfo.setDisplayNames();
                //***********************end*******************************
                System.out.println("********************AFTER '.setParticiapnts'& '.setDisplayNames' ==>'ActivityWithInfo':" + ActivityWithInfo + "************************");


                //*****STORE INTO ARRAY LIST of CLUBS******
                //  listOfLeagueNames.add(clubname);
                // ***Store into an array of LectureClass list***
                listOfCreatedClubs.add(ActivityWithInfo);
                //***********************************
                final DatabaseReference addOntoLeague = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Clubs/" + clubname);
                final DatabaseReference personalLeagues = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//'UserID'/'" + UserID + "'/" + clubname);
                //  mCreateLeague.setValue(ActivityWithInfo); //*set league name in database*
                // mCreateLeague.setValue(listOfCreatedLeagues); //*set league name in database*
                //mCreateLeague.setValue(listOfCreatedLeagues);

                //*********************GLENN's addon code start V2**********************
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                EditText leagueNameEditText = (EditText) findViewById(R.id.editText_clubName);
                final String clubname = leagueNameEditText.getText().toString();

                final DatabaseReference teamsRef = database.getReference("Teams");

                final DatabaseReference userRef = database.getReference("Participants").child(clubname).child(user.getUid()).child("Available");
                //Zach's code ref:set select team node on database start
                final DatabaseReference selectedTeamRef = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com/Participants/" + clubname + "/" + UserID);
                selectedTeamRef.child("Selected Team").setValue("Empty");

                //**Glen's 'standings' addon code**
                final DatabaseReference standingRef = database.getReference("Participants").child(clubname).child(user.getUid()).child("Standing");

                standingRef.setValue("In");
                //****

                // selectedTeamRef.setValue("Selected Team");
                //code end

                teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            DatabaseReference specificTeam = userRef.child(child.getKey());
                            specificTeam.setValue(true);
                        }
                        //=========== Intent intent = new Intent(CreateLectureActivity.this, LectureCreatedActivity.class);
                        //=========== startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //*********************GLENN's addon code V2 end**********************


                //*Put leagueNames into a list which can be checked before league creation*
                final DatabaseReference leaguesListRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//LeagueList/");

                leaguesListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //  if(leaguesListRef.getClass().equals(t)||leaguesListRef.getClass().toString().isEmpty())//initialise on database if no arraylist present
                        // {
                        // leaguesList = dataSnapshot.getValue(ArrayList.class);
                        if (dataSnapshot.exists()) {
                            stringArray = dataSnapshot.getValue(t);
                            if (!stringArray.contains(clubname)) //if stringArray doesn't have this clubname added yet
                            {
                                //*Add to list*
                                stringArray.add(clubname);
                                //*****Add ActivityWithInfo to database*****
                                addOntoLeague.setValue(ActivityWithInfo);
                                personalLeagues.setValue(ActivityWithInfo);


                              //  notif.setText("*League '" + clubname + "' has been added to the database!*");

                                //***set 'leagueCreated' boolean to 'true'  (because a new league has been created)
                                leagueCreated = true;

                                if (leagueCreated == true) {
                                   //**REMOVED ONLY NEEDED FOR LECTURES CLASS!** Intent intent = new Intent(CreateClubActivity.this, LectureCreatedActivity.class);
                                   //**REMOVED ONLY NEEDED FOR LECTURES CLASS!** Intent.putExtra(EXTRA_MESSAGE, clubname);
                                   //**REMOVED ONLY NEEDED FOR LECTURES CLASS!** startActivity(intent);

                                    //**REPLACED above 3 lines code with...
                                    Intent intent=new Intent(CreateClubActivity.this,LecturesActivity.class);
                                    Toast.makeText(CreateClubActivity.this,"Club created successfully.",Toast.LENGTH_SHORT).show();
                                    startActivity(intent);


                                }
                            } else if (stringArray.contains(clubname)) {
                                    Toast.makeText(CreateClubActivity.this,"Could not create. Club with the same name already exists.",Toast.LENGTH_LONG).show();


                                //   notif.setText("*Club '" + clubname + "' Already exists! Please choose another name.*");

                                //***set 'leagueCreated' boolean to 'false' (because no new league has been created)***
                                leagueCreated = false;
                            }


                            leaguesListRef.setValue(stringArray);
                            System.out.println("********IN IF********LeagueList:" + stringArray + "*********************");
                        } else //when there is no league data yet (then initialise it)
                        {
                            leaguesList.add(clubname);
                            leaguesListRef.setValue(leaguesList);
                          //  notif.setText("*Club '" + clubname + "' has been added to the database!*");

                            System.out.println("********IN ELSE********LeagueList:" + leaguesList + "*********************");
                            //$$$$$$$
                            Intent intent = new Intent(CreateClubActivity.this, LectureCreatedActivity.class);
                            intent.putExtra(EXTRA_MESSAGE, clubname);
                            startActivity(intent);

                            //*****Add ActivityWithInfo to database*****
                            addOntoLeague.setValue(ActivityWithInfo);
                            personalLeagues.setValue(ActivityWithInfo);


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        //*Get User UID implementation*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        //*****************************
    }//*outside onCreate*

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        intent.replaceExtras(new Bundle());
        intent.setAction("");
        intent.setData(null);
        intent.setFlags(0);
        //consumedIntent = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //on return button pressed on phone, take user to 'LecturesActivity' page
            Intent intent = new Intent(this, LecturesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

