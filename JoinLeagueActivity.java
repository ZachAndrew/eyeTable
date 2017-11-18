package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


public class JoinLeagueActivity extends AppCompatActivity {

    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};

    //*Firebase references*
    DatabaseReference ref=FirebaseDatabase.getInstance().getReference();//get database reference
    DatabaseReference refForListener=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/");

   // DatabaseReference leagueParticipantsRef=ref.child("Leagues");

    String leagueWantedToJoin="Null",username="Null",userID="Null",temp1,displayName="Empty";

   // List <String> currentParticipantList=new ArrayList<String>();
    final List <String>tempParticipantsHolder=new ArrayList<>();

    //A list for personal leagues for each user
    List <String> leagueEnteredInList=new ArrayList<>();

    //A list for the participants in the league
    List <String> participantsList;

    //A list for the display names of the participants in the league
    List <String> displayNamesOfParticipantsList;




    //*Temp League var*, to take in obj league from Firebase
    LectureClass tempLeague=new LectureClass();
    LectureClass test;

    String testOutput;

    //counter
    int x1=0; //a counter for the participants in the league
    //int x2=1;

    String tempCheck="NULL";//for if the string input for league changes


    boolean added=true;//to stop loop after one addition of a user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_league);

        //*TextView*
        final TextView notif= (TextView)findViewById(R.id.textView58);//notification

        //*Buttons*
        Button joinButton= (Button)findViewById(R.id.button5);

        //*Edit text fields*
       final EditText leagueToJoinID=(EditText)findViewById(R.id.editText10);//edit text field where user will enter league name/code to join league

        //*List for participants*


        //*Get User ID* START
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            username=user.getEmail();
            // Username="'"+user.getEmail()+"'";
            displayName=user.getDisplayName();


            userID=user.getUid();
        } else {
            // No user is signed in
        }

        //*Get User ID* END

        //when join button is clicked, make user join the league
   /*     joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp;
                temp=leagueToJoinID.getText().toString();
                final DatabaseReference leagueParticipantsRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/"+temp);
                final DatabaseReference leagueRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/"+temp);
                //*Tryin to implements adding to participants for each league*
                // leagueParticipantsRef.setValue(username);
                // Attach a listener to read the data at our posts reference
                //********************************************
            }
        });*/

        //***********START OF JOIN BUTTON LISTENER*********************
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //debug check
                System.out.println("***************DISPLAY name:"+displayName+"**********************");


               final String temp;//to store edit text input
                temp=leagueToJoinID.getText().toString();//gets league name entered by user

                //**************Ref to list of leagues start**************
                DatabaseReference refToLeaguesList= FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/LeagueList");
                //************************end**************************


                DatabaseReference refNumPlayersForListener=FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/"+temp+"/numPlayers");
                //*A listener to change read in  numPlayers when players/users are added*
                //+also used to change the value of counter, so adding players should not overwrite users
                //***********

                final DatabaseReference refForLeagueParticipants=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/"+leagueToJoinID.getText().toString()+"/participants");
               //*LeagueRef*
                DatabaseReference refForLeagueBeingJoined=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/"+leagueToJoinID.getText().toString());
                //A ref for a list of users own leagueNames
                final DatabaseReference personalLeagueListRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/"+user.getUid()+"/PersonalLeagueList");

                //*If list contains league trying to join*
                refToLeaguesList.addListenerForSingleValueEvent(new ValueEventListener() {


                        @Override
                        public void onDataChange (DataSnapshot dataSnapshot){
                            //*get list*
                            final List tempList=dataSnapshot.getValue(t);

                            if(dataSnapshot.exists()&&  tempList.contains(temp)) //if league exists in list, then allow listener for joining

                            //*check if list contains the league string being joined*
                            {
                        //a 'leagueEnteredInList' check
                        //+ if league already in list, don't add it again
                        personalLeagueListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //if a list is there...
                                if (dataSnapshot.exists()) {
                                    List temp = dataSnapshot.getValue(t); //get list
                                    //    System.out.println("**********************************CONTENTS OF 'temp' list BEFORE ADDING:"+temp+"**********************************");

                                    if (!temp.contains(leagueToJoinID.getText().toString())) //if list does not already have the league stored in it, add
                                    {
                                        leagueEnteredInList = temp;//store list from database locally first, before adding to it
                                        leagueEnteredInList.add(leagueToJoinID.getText().toString());//take in leagueName for a leagues checklist
                                        personalLeagueListRef.setValue(leagueEnteredInList); //store list onto database
                                        //     System.out.println("**********************************CONTENTS OF 'temp' list AFTER ADDING:"+temp+"**********************************");

                                    }
                                } else //...store the list initially
                                {
                                    List initialList = new ArrayList();
                                    initialList.add(leagueToJoinID.getText().toString());
                                    //  System.out.println("**********************************CONTENTS OF 'initialList' list:"+initialList+"**********************************");
                                    personalLeagueListRef.setValue(initialList);
                                }
                                notif.setText("League '"+temp+"' has been joined!");


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                                //  refNumPlayersForListener.addListenerForSingleValueEvent(new ValueEventListener() {
                                final DatabaseReference participantsExistRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leagueToJoinID.getText().toString() + "/participants/");



                                refForLeagueParticipants.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //*Ref to the participants of this selected League*

                                        final DatabaseReference leagueParticipantsRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leagueToJoinID.getText().toString() + "/participants/");

                                        final DatabaseReference leagueDisplayNamesRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leagueToJoinID.getText().toString() + "/displayNames/");


                                        // if(temp!=leagueToJoinID.getText().toString()) //if string name being accessed is not 'null', add to participant list

                                        if (tempList.contains(temp)&&dataSnapshot.exists()) //if string name being accessed is not 'null', add to participant list
                                        {

                                            participantsList = dataSnapshot.getValue(t);//get generic type of obj on database
                                            participantsList.add(username);

                                            if (participantsList.contains("*EMPTY*")) //when adding participants remove '*EMPTY*' from the league
                                            {
                                                participantsList.remove("*EMPTY*");
                                            }
                                              leagueParticipantsRef.setValue(participantsList);

                                            //**DISPLAY NAME ATTEMPT 1**
                                            leagueDisplayNamesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.exists()&&dataSnapshot!=null) {
                                                        List temp=dataSnapshot.getValue(t);
                                                        displayNamesOfParticipantsList=temp;
                                                        displayNamesOfParticipantsList.add(displayName);
                                                        if (displayNamesOfParticipantsList.contains("*EMPTY*")&&displayNamesOfParticipantsList.size()>1) //when adding participants remove players from the league
                                                        {
                                                            displayNamesOfParticipantsList.remove("*EMPTY*");
                                                        }
                                                        leagueDisplayNamesRef.setValue(displayNamesOfParticipantsList);
                                                    }


                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            //****

                                            //*Set league joined for user's own/participated leagues*
                                            final DatabaseReference leagueUserRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/" + user.getUid() + "/LeagueEnteredIn/" + leagueToJoinID.getText().toString() + "/participants/");

                                            leagueUserRef.child("" + x1).setValue(username);
                                        /*    Boolean knockedOut = false;
                                            Boolean leagueAlive = true;
                                            leagueUserRef.child("KnockedOutStatus").setValue(knockedOut);
                                            leagueUserRef.child("LeagueAlive").setValue(leagueAlive);*/

                                            //*****ADD TO 'Selected Team' node to database on join   start*****
                                            DatabaseReference refForSelectedTeam = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Participants/" + temp + "/" + user.getUid());
                                            refForSelectedTeam.child("Selected Team").setValue("Empty");
                                            //****end****

                                            //*********************GLENN's addon code start V2**********************
                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            final FirebaseDatabase database = FirebaseDatabase.getInstance();

                                            EditText leagueNameEditText = (EditText) findViewById(R.id.editText10);//edited
                                            String leagueName = leagueNameEditText.getText().toString();
                                            final DatabaseReference teamsRef = database.getReference("Teams");
                                            final DatabaseReference userRef = database.getReference("Participants").child(leagueName).child(user.getUid()).child("Available");
                                            //Zach's code ref:set select team node on database start
                                            final DatabaseReference selectedTeamRef = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Participants/" + leagueName + "/" + user.getUid());
                                            selectedTeamRef.child("Selected Team").setValue("Empty");

                                            // selectedTeamRef.setValue("Selected Team");
                                            //code end
                                            //**Glen's 'standings' addon code**
                                            final DatabaseReference standingRef=database.getReference("Participants").child(leagueName).child(user.getUid()).child("Standing");

                                            standingRef.setValue("In");
                                            //****

                                            teamsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                        DatabaseReference specificTeam = userRef.child(child.getKey());
                                                        specificTeam.setValue(true);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            //*********************GLENN's addon code V2 end**********************
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                //***********************************************

                    }//outside if statement
                            else{notif.setText("League Does not exist! Please join an existing league.");}
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                });


                refForLeagueBeingJoined.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //*Set league joined for user's own/participated leagues*
                        LectureClass temp=dataSnapshot.getValue(LectureClass.class);//get league object
                        final DatabaseReference leagueUserRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/"+user.getUid()+"/LeagueEnteredIn/"+leagueToJoinID.getText().toString());
                        leagueUserRef.setValue(temp);//set league object for personal usage for user on database
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //**************************************************
            /*
             //   if(tempCheck.equals(temp))//checks if the current league name is the same as the previous
                {
                    //don't reset counter
                }
               // else {
                    //reset counter

                    DatabaseReference refNumPlayersForListener = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/" + leagueToJoinID.getText().toString() + "/numPlayers");

                    //*A listener to change read in  numPlayers when players/users are added*
                    //+also used to change the value of counter, so adding players should not overwrite users
                    refNumPlayersForListener.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            int numPlayerRetrieved = dataSnapshot.getValue(int.class);//gets int obj from firebase
                            x1 = numPlayerRetrieved; //change the value of counter with numPlayers on database(prevents overwriting users at a child node)
                            x1++;
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

              //      tempCheck = temp;
              //  } */

                //*A listener to update numPlayers after a user joins the specific league*
              /*
                leagueRefForUpdatingNumPlayers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                            int numPlayerRetrieved = dataSnapshot.getValue(int.class);//gets int obj from firebase
                        if(x1<x2) {
                           // x2++;
                            x2++;//increment because the current user has just joined
                            leagueRefForUpdatingNumPlayers.setValue(numPlayerRetrieved);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/



              // x1++;

            /****List attempt for participants****
                //*List* to store username input in listener******************
                final List <String> tempParticipantsHolder1=tempParticipantsHolder;
                //************************************************************
                tempParticipantsHolder1.add(username);
                leagueParticipantsRef.setValue(tempParticipantsHolder1);

                tempParticipantsHolder1.clear();
            //*************************************
            */

              //  tempParticipantsHolder1.add(username);
              //  leagueParticipantsRef.setValue(tempParticipantsHolder1);
               // System.out.print("££££££££££££££££££££££££££"+testOutput+"£££££££££££££££££££££££££");
            }
        }); //***********END OF JOIN BUTTON LISTENER******************


        //****TRYING TO CHANGE LIST WITH CURRENT LIST ON DATABASE FOR PARTICIPANTS****

    }
    //*Outside onCreate*
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

    public void leagues(View view) {
        Intent intent = new Intent(this, LecturesActivity.class);
        startActivity(intent);
    }
}








//************************Issues withe code************************************
//********ADDING A DATA LISTENER FOR A LEAGUE*************
/*
leagueRefForListener.addValueEventListener(new ValueEventListener() {
@Override
public void onDataChange(DataSnapshot dataSnapshot) {


        if(dataSnapshot.getClass().equals(LectureClass.class)) {
        LectureClass posted = dataSnapshot.getValue(LectureClass.class);

        tempParticipantsHolder1 = posted.getParticipants();//get list of particpants from league, store as a temp list
        tempParticipantsHolder1.add(username);//add username to list
        System.out.println("************LEAGUE CURRENTLY LISTENED TO:" + posted.getLeagueName() + "***************");
        }
        System.out.print("\n$$$$$$$$$$$"+tempParticipantsHolder1+"$$$$$$$$$$$$$$$$\n");
        leagueParticipantsRef.setValue(tempParticipantsHolder1);
        }
@Override
public void onCancelled(DatabaseError databaseError) {
        System.out.println("The read failed: " + databaseError.getCode());
        }
        });
*/

//*************************END OF addValueEventListener*******************************
//set the participant of current league by new list of participants with added username

//******************************************************************


//********TRYING A SINGLE DATA LISTENER FOR A LEAGUE************
        /*
        leagueRefForListener.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                test=dataSnapshot.getValue(LectureClass.class);
               // $ $ $ LectureClass posted=dataSnapshot.getValue(LectureClass.class);
              //$ $ $   tempLeague=posted;
             //$ $ $    System.out.println("$$$$$$$$$$$$$$$$$$$$$$$"+posted.getLeagueName()+"$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.println("$$$$$$$$$$$$$$$$$$$$$$$"+test+"$$$$$$$$$$$$$$$$$$$$$$$$$$");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
//***********************SINGLE DATA LISTENER END***************************************

/*

refForListener.addValueEventListener(new ValueEventListener() {


@Override
public void onDataChange(DataSnapshot dataSnapshot) {

final String temp;
        temp=leagueToJoinID.getText().toString();
//+++   temp1=temp;//for listener to use too
final DatabaseReference listenerRef=FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/"+temp);


        //if (dataSnapshot.equals(LectureClass.class)){
        Iterable tempLeague=dataSnapshot.getChildren();
        System.out.print("****ADMIN OF CURRENT LEAGUE:"+tempLeague.iterator()+"****");
        // testOutput=dataSnapshot.();
        //}

        // List<String> updatedList=dataSnapshot.getValue(ArrayList.class);//pulls list obj from database
        // updatedList.add(username);//add username to current list acquired from database
        //  System.out.print("****$$$***"+updatedList+"****$$$***");
        //  leagueParticipantsRef.setValue(updatedList);//set list back to database

        }


@Override
public void onCancelled(DatabaseError databaseError) {

        }
        });//*************************************************************************
*/