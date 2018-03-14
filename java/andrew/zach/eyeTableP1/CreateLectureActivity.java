package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

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

public class CreateLectureActivity extends AppCompatActivity {


    //***Variables***
    private ListView mListView;
   // private List <String>leaguesList=new ArrayList<String>();
   private ArrayList <String>leaguesList= new ArrayList<>();

    //***Boolean var, for league created***
    boolean leagueCreated=false;

    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};

    //*Variables needed for getting User UID implmentation*
    private static final String TAG = "Login";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String Username="No Username Yet";//default, for branching separate database
    String UserID="No UID yet";

    public static final String EXTRA_MESSAGE="andrew.zach.lmsprototype2_android_version.MESSAGE";
    //*****************************************************

    String leaguename="*Default*",admin="*DEFAULT*",paymentAmount="7.5",typeOfleague="Premier League",lectureCode="NULL";
    //*List*
    final List<LectureClass> list = new ArrayList<>();
    //displayNames,participants lists
    List <String> displayNames=new ArrayList<>();
    List <String> participants=new ArrayList<>();



    List<String> stringArray;

    //Counter for x
    int x=0,playernum;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference(); //get an instence of the database
    DatabaseReference mCreateLeague=mRootRef.child("Leagues");
    DatabaseReference players=mRootRef.child("'UserID'");
    DatabaseReference mLeagueWithMembersChild=mRootRef.child("LeagueAndMembers");


    //access to data in sub children
 //   DatabaseReference mleagueData1=FirebaseDatabase.getInstance().getReferenceFromUrl("https://lmsproto1.firebaseio.com/Leagues/"+x+"/"+leaguename);
//    final DatabaseReference mplayersData1= FirebaseDatabase.getInstance().getReferenceFromUrl("https://lmsproto1.firebaseio.com/Users/Players");
    // List l= (List) FirebaseDatabase.getInstance().getReferenceFromUrl("https://lmsproto1.firebaseio.com/Users");
    //*Extract List from 'Player Entries' on database*
    DatabaseReference myPlayerList= mRootRef.child("Users");

    //DatabaseReference mAddToExistingLeague=mCreateLeague.child(list);
    //**************



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_league);

        //***clear lists***
        displayNames.clear();
        participants.clear();
        //*****************

        //*Get User ID* START
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            admin=user.getEmail();
           // Username="'"+user.getEmail()+"'";
            UserID=user.getUid();
        } else {
            // No user is signed in
        }

        //*Get User ID* END

        //*League name entered by user*
      //  EditText getLeagueNameInput=(EditText)findViewById(R.id.editTextleagueNameEntry);
        final TextView notif=(TextView) findViewById(R.id.textView24);//notification for user


        //*Edit Text fields*
        final EditText editLeagueName=(EditText)findViewById(R.id.editTextleagueNameEntry);

        //*Buttons*
        final Button makeLeagueButton=(Button)findViewById(R.id.makeClub);
        //*RADIO BUTTONS*
        RadioButton radioButton5euro=(RadioButton)findViewById(R.id.radioButton3);//€5
        RadioButton radioButton10euro=(RadioButton)findViewById(R.id.radioButton5);//€10
        RadioButton radioButton20euro=(RadioButton)findViewById(R.id.radioButton4);//€20
        RadioButton radioButton50euro=(RadioButton)findViewById(R.id.radioButton7);//€50

     /*   RadioButton radioButtonLeagueType1=(RadioButton)findViewById(R.id.radioButton);//for "Premier League"
        RadioButton radioButtonLeagueType2=(RadioButton)findViewById(R.id.radioButton2);//for "La Liga"
*/


        /*
        //*********************GLENN's addon code start V2**********************
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        EditText leagueNameEditText = (EditText) findViewById(R.id.editTextleagueNameEntry);
        String leagueName = leagueNameEditText.getText().toString();
        final DatabaseReference teamsRef = database.getReference("Teams");
        final DatabaseReference userRef = database.getReference("Participants").child(leagueName).child(user.getUid()).child("Available");

        teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    DatabaseReference specificTeam = userRef.child(child.getKey());
                    specificTeam.setValue(true);
                }
                Intent intent = new Intent(CreateLectureActivity.this, LectureCreatedActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //*********************GLENN's addon code V2 end**********************
        */

      //++  final Button resetAllLeagues=(Button)findViewById(R.id.buttonResetLeagueData);
      //+  Button resetPlayersInLeague=(Button)findViewById(R.id.buttonEnrollAll);

        //**********Setting onclick listeners to just reset text when user clicks on text entry fields************

//*RADIO BUTTON LISTENERS* START
        radioButton5euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String credits=paymentAmount;
                paymentAmount="5";
              //  paymentAmount="Lecture";
            }
        });
        radioButton10euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String credits=paymentAmount;
                paymentAmount="10";
             //   paymentAmount="Club/Soc";
            }
        });

        radioButton20euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String credits=paymentAmount;
                paymentAmount="15";
                //paymentAmount="€20";
            }
        });
        radioButton50euro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  String credits=paymentAmount;
                paymentAmount="Other";
             //   paymentAmount="€50";
            }
        });


        //*RADIO BUTTONS: LEAGUE TYPE*

/*
        radioButtonLeagueType1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfleague="Premier League";

            }
        });

        radioButtonLeagueType2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeOfleague="La Liga";
            }
        });*/

        //****************************
        //*RADIO BUTTON LISTENERS* END


        makeLeagueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //***** CREATE A LIST (link it to the local 'list')********
                List <LectureClass> listOfCreatedLeagues=list;
                //***************************

                //***clear lists***
                displayNames.clear();
                participants.clear();
                //*****************

                if(editLeagueName.getText().toString().isEmpty() ){
                    leaguename="Default";
                }
                else {
                    leaguename = editLeagueName.getText().toString(); //get leaguename from user
                }
              //+  admin=editLeagueAdmin.getText().toString(); //get admin name from user
               //+ playernum=editPlayerNum.getText().toString();//to get player number max size from user

                //*Store into an obj of type 'LectureClass'
                final LectureClass leagueWithInfo=new LectureClass(leaguename,admin,playernum,paymentAmount,lectureCode,participants,displayNames);

                System.out.println("********************BEFORE '.setParticiapnts'& '.setDisplayNames' ==>'leagueWithInfo':"+leagueWithInfo+"************************");
                //****CHANGE PARTICIPANTS/DISPLAY NAMES attempt 1 start*****
                //reset names to empty on creation
                leagueWithInfo.setParticipants();
                leagueWithInfo.setDisplayNames();
                //***********************end*******************************
                System.out.println("********************AFTER '.setParticiapnts'& '.setDisplayNames' ==>'leagueWithInfo':"+leagueWithInfo+"************************");


                //*****STORE INTO ARRAY LIST of LEAGUES******
                //  listOfLeagueNames.add(leaguename);
                // ***Store into an array of LectureClass list***
                listOfCreatedLeagues.add(leagueWithInfo);
                //***********************************
               final DatabaseReference addOntoLeague=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/"+leaguename);
               final DatabaseReference personalLeagues=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//'UserID'/'"+UserID+ "'/"+leaguename);
                //  mCreateLeague.setValue(leagueWithInfo); //*set league name in database*
                // mCreateLeague.setValue(listOfCreatedLeagues); //*set league name in database*
                //mCreateLeague.setValue(listOfCreatedLeagues);

                //*********************GLENN's addon code start V2**********************
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                EditText leagueNameEditText = (EditText) findViewById(R.id.editTextleagueNameEntry);
                final String leagueName = leagueNameEditText.getText().toString();
                final DatabaseReference teamsRef = database.getReference("Teams");

                final DatabaseReference userRef = database.getReference("Participants").child(leagueName).child(user.getUid()).child("Available");
                  //Zach's code ref:set select team node on database start
                final  DatabaseReference selectedTeamRef= database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com/Participants/"+leagueName+"/"+UserID);
                selectedTeamRef.child("Selected Team").setValue("Empty");

                //**Glen's 'standings' addon code**
                final DatabaseReference standingRef=database.getReference("Participants").child(leaguename).child(user.getUid()).child("Standing");

                standingRef.setValue("In");
                //****

                // selectedTeamRef.setValue("Selected Team");
                //code end

                teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
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
                     final DatabaseReference leaguesListRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//LeagueList/");

                    leaguesListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             //  if(leaguesListRef.getClass().equals(t)||leaguesListRef.getClass().toString().isEmpty())//initialise on database if no arraylist present
                             // {
                             // leaguesList = dataSnapshot.getValue(ArrayList.class);
                             if (dataSnapshot.exists()) {
                                 stringArray = dataSnapshot.getValue(t);
                                 if (!stringArray.contains(leaguename)) //if stringArray doesn't have this leaguename added yet
                                 {
                                     //*Add to list*
                                     stringArray.add(leaguename);
                                     //*****Add LeagueWithInfo to database*****
                                     addOntoLeague.setValue(leagueWithInfo);
                                     personalLeagues.setValue(leagueWithInfo);


                                     notif.setText("*League '" + leaguename + "' has been added to the database!*");

                                     //***set 'leagueCreated' boolean to 'true'  (because a new league has been created)
                                     leagueCreated = true;

                                     if (leagueCreated == true) {
                                         Intent intent = new Intent(CreateLectureActivity.this, LectureCreatedActivity.class);
                                         intent.putExtra(EXTRA_MESSAGE, leagueName);
                                       startActivity(intent);
                                     }
                                 } else if (stringArray.contains(leaguename)) {

                                     notif.setText("*League '" + leaguename + "' Already exists! Please choose another name.*");

                                     //***set 'leagueCreated' boolean to 'false' (because no new league has been created)***
                                     leagueCreated = false;
                                 }


                                 leaguesListRef.setValue(stringArray);
                                 System.out.println("********IN IF********LeagueList:" + stringArray + "*********************");
                             } else //when there is no league data yet (then initialise it)
                             {
                                 leaguesList.add(leaguename);
                                 leaguesListRef.setValue(leaguesList);
                                 notif.setText("*League '" + leaguename + "' has been added to the database!*");

                                 System.out.println("********IN ELSE********LeagueList:" + leaguesList + "*********************");
                                 //$$$$$$$
                                 Intent intent = new Intent(CreateLectureActivity.this, LectureCreatedActivity.class);
                                 intent.putExtra(EXTRA_MESSAGE, leagueName);
                                 startActivity(intent);

                                 //*****Add LeagueWithInfo to database*****
                                 addOntoLeague.setValue(leagueWithInfo);
                                 personalLeagues.setValue(leagueWithInfo);


                             }
                         }


                           /*     }
                                else //take list down, add to, put it back to database
                                {
                                    //leaguesList = dataSnapshot.getValue(ArrayList.class);
                                    List<String> stringArray = dataSnapshot.getValue(t);
                                    //initialise list for leagues
                                    stringArray.add(leaguename);
                                    leaguesListRef.setValue(leaguesList);
                                    System.out.println("********IN ELSE********LeagueList:" + stringArray + "*********************");
                                }*/


                          /*   else
                             {
                                 //else, take down list of leagues, add to it, put it back to the database
                                 leaguesList=dataSnapshot.getValue(List.class);
                                 System.out.println("********IN ELSE********LeagueList:"+leaguesList+"*********************");


                                 System.out.println("\n==> Iterator Example...");
                                 Iterator<String> crunchifyIterator = leaguesList.iterator();
                                 while (crunchifyIterator.hasNext()) {
                                     System.out.println(crunchifyIterator.next());
                                 }
                                 leaguesList.add(leaguename);
                                 leaguesListRef.setValue(leaguesList);
                             }*/

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });
            }
        });

        //**CLEARS ALL THE LEAGUES DATA**
        /*
        resetAllLeagues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();//clear list
                mCreateLeague.setValue("***RESETTED***");
                mLeagueWithMembersChild.setValue("***RESETTED***");

                x=0;
            }
        });

     //*REMOVED 'resetPlayersInLeague.setOnclickListener(new View ...)...{}


        editLeagueNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editLeagueNum.setText("");
            }
        });
*/
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
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //on return button pressed on phone, take user to 'LecturesActivity' page
            Intent intent= new Intent(this, LecturesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //*After League has been created, switch to this page*
 /*   public void leagueCreated(View view) {

        if(leagueCreated==true) {
            Intent intent = new Intent(CreateLectureActivity.this, LectureCreatedActivity.class);
            startActivity(intent);
        }*/






        /* ATTEMPT 1 start
        boolean leagueCreatedCheck=false; //league not created yet
        //*Move to 'LectureCreatedActivity.class' only when it is created*
        DatabaseReference dbLeaguesListcheckRef=FirebaseDatabase.getInstance().getReference("https://androidapplms-database.firebaseio.com/LeagueList");
        dbLeaguesListcheckRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List leagueListOnDB=dataSnapshot.getValue(t);
                //*********
                Intent intent = new Intent(CreateLectureActivity.this, LectureCreatedActivity.class);
                startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
   ********end*******/

    }
    //*Starting and stopping User Auth observer*
/****Error*****
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
 *************
*/


