package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ViewLeagueActivity extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_manage_league) {

            Intent intent2 = getIntent();
            final String league_name = intent2.getStringExtra(EXTRA_MESSAGE);
            // String leagueName = leagueNameField.getText().toString().trim();

         /*   TextView LeagueName=(TextView) findViewById(R.id.textViewLeagueName2);*/
            String leagueName=league_name;
           // intent2.putExtra(EXTRA_MESSAGE, leagueName);

            Intent intent = new Intent(ViewLeagueActivity.this, ManageLectureActivity.class);
            Toast.makeText(getApplicationContext(), "Welcome to your League Management!", Toast.LENGTH_SHORT).show();
            intent.putExtra(EXTRA_MESSAGE, leagueName);

           // Intent intent = new Intent(ViewLeagueActivity.this, ManageLectureActivity.class);

           // String leagueName = leagueNameField.getText().toString().trim();

            startActivity(intent);
        } else if(id == R.id.action_leave_league) {
            Toast.makeText(getApplicationContext(), "You have left the league!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LecturesActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    //***Added Glenn's menu code end***


    //********GLENN's ADDON CODE V2 start**********
    private static final String TAG = "ViewLeague";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    boolean isInLeague=false;//a check to see if player is in league

    GenericTypeIndicator <List<String>> t=new GenericTypeIndicator<List<String>>() {};

    public static final String EXTRA_MESSAGE = "andrew.zach.eyeTableP1.MESSAGE";
    //********GLENN's ADDON CODE V2 end**********

    //to store value entered from edit text field
    String leagueName="Test League";
    String league_name="Default";
    String admin,UserID="User ID";
    private ListView mListView; //a list view referenced by id

    //a checklist used to see if background colours have been set to displayNames or not
    final List <String> myCheckListOfDisplayNames=new ArrayList<String>();


    //*generic type indicator creation to pull down list from database*
    //GenericTypeIndicator <List<String>> t= new GenericTypeIndicator<List<String>>() {};

    Boolean adminCheck=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_league);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();



        final TextView deadlineField = (TextView) findViewById(R.id.deadlineField);
        final TextView selectedTeamView = (TextView) findViewById(R.id.selectedTeamField);
        final ImageView teamBadgeView = (ImageView) findViewById(R.id.teamBadgeView);


        //*Get User ID* START
       final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // User is signed in
          //  admin = user.getEmail();

            // Username="'"+user.getEmail()+"'";
            UserID = user.getUid();
        } else {
            // No user is signed in
        }
        //*Get User ID* END

        //***** GLENN's GET EXTRA_MESSAGE CODE FROM 'LecturesActivity.java'  START**********
        Intent intent = getIntent();
        league_name = intent.getStringExtra(EXTRA_MESSAGE);

        //***team pick button ref***
        final Button mpickTeamBtn=(Button)findViewById(R.id.pickTeamButton);

        //***REMOVED:manage league button***
        //final Button manageLeague=(Button)findViewById(R.id.buttonManage);

        //**adding**
       /* if(league_name==null){
            league_name=intent.getStringExtra(PickTeamActivity.EXTRA_MESSAGE);
        }*/
        //**adding end**

        final Button teamPickHistory=(Button) findViewById(R.id.buttonTeamPickHistory); //a button that will allow user to view team pick history

        final TextView enterLeagueNameToView = (TextView) findViewById(R.id.textView50);
        //*Title of page, containing league_name being viewed*
        enterLeagueNameToView.setText(league_name);
        //***** GLENN's GET EXTRA_MESSAGE CODE FROM 'LecturesActivity.java'  END**********

        //*****Getting Title of previous page******
        final String title_ofPrevPage = intent.getStringExtra("title");//getting title to check whether to view personal/overall leagues

        //****End Of Getting title of previous page****


        //* listener check to see if the logged in user is the admin of the league or not*
        final DatabaseReference refToLeagueAdmin = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + league_name + "/admin");
        refToLeagueAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String adminNameToThisLeague=dataSnapshot.getValue(String.class);
                    adminCheck = user.getEmail().equals(adminNameToThisLeague);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        mListView = (ListView) findViewById(R.id.listView1); //a list view referenced by id

        //*Firebase Database refs*
       final DatabaseReference refForListener = database.getReference("Leagues").child(leagueName).child("participants");


        //*********************TEMP TEAM PICK HISTORY (using button, going to use onItemClick when user.getUid() for particpants is working **********start*******************
        /* #3teamPickHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ViewLeagueActivity.this, ParticipantTeampickHistoryActivity.class);
                intent.putExtra(EXTRA_MESSAGE,league_name);
            }
        });   #3*/

        //*********************TEMP TEAM PICK HISTORY (using button, going to use onItemClick when user.getUid() for particpants is working **********end*******************


        refForListener.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //*******************************FIREBASE LIST ADAPTER start***************************************
                //*ref using EXTRAMESSAGE PASSED FROM 'LecturesActivity.java' page*
                final DatabaseReference refToLeagueParticipants = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + league_name + "/participants");
                final DatabaseReference refToPersonalLeagueParticipants = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/" + UserID + "/LeagueEnteredIn/" + league_name + "/participants");

                //***
                final DatabaseReference refToDisplayNamesForParticipants=database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + league_name + "/displayNames");
                //***


                if (title_ofPrevPage == "All Leagues List") {
                    //*Firebase listadapter, used to make a list on the page using data on the database
                    //this adapter works with our list and displays whats on firebase

                    FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                            ViewLeagueActivity.this,
                            String.class,
                            android.R.layout.simple_list_item_1,
                            refToLeagueParticipants

                    ) {
                        @Override
                        //*Displays a list on the app, of leagues currently stored in database in realtime*
                        protected void populateView(View v, String model, int position) {
                            final TextView textView = (TextView) v.findViewById(android.R.id.text1);
                           // v.setBackgroundResource(R.drawable.winner_text_image);
                         //   textView.setBackgroundResource(R.drawable.standing_in_image2);
                            //  textView.setText("LeagueName: "+model.getLeagueName()+"\n\nParticipants:\n"+model.getParticipants()+"\n");

                            DatabaseReference refToPersonalStandings= database.getReference().child("Participants").child(league_name).child(user.getUid()).child("Standing");
                            refToPersonalStandings.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                        String s=dataSnapshot.getValue(String.class);
                                    if(s.equals("In")) {
                                        Drawable image = getDrawable(R.drawable.standing_in_image2);
                                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);
                                      }else {
                                        Drawable image = getDrawable(R.drawable.standing_out_image);
                                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            textView.setText(model);
                        }
                    };
                    mListView.setAdapter(firebaseListAdapter);
                    // viewForParticipants.setText(firebaseListAdapter.toString());
                    //  viewForParticipants.setText(firebaseListAdapter.getItem(0));

                    // *******************************FIREBASE LIST ADAPTER end****************************************
                } else {
                    //******If title is 'My Leagues List''*****
                    //*Firebase listadapter, used to make a list on the page using data on the database
                    //this adapter works with our list and displays whats on firebase

                    FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                            ViewLeagueActivity.this,
                            String.class,
                            android.R.layout.simple_list_item_1,
                            //refToPersonalLeagueParticipants
                            refToDisplayNamesForParticipants
                    ) {
                        @Override
                        //*Displays a list on the app, of leagues currently stored in database in realtime*
                            protected void populateView(View v, final String model, int position) {
                            final TextView textView = (TextView) v.findViewById(android.R.id.text1);
                           // v.setBackgroundResource(R.drawable.standing_in_image2);


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
                                            final String element = iter.next();
                                            // 1 - can call methods of element
                                            // 2 - can use iter.remove() to remove the current element from the list
                                            // ...

                                            //*ref to participants stnading*
                                            final DatabaseReference refToPersonalStandings= database.getReference().child("Participants").child(league_name).child(element).child("Standing");

                                            //*ref to users display names*
                                            DatabaseReference refToDisplayNames=database.getReference().child("Users").child(element).child("Name");


                                            //*get name listener*
                                            refToDisplayNames.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    //get name from datasnashot for a user
                                                    final String getDisplayNameTemp=dataSnapshot.getValue(String.class);
                                                    System.out.println("***********************************Value of 'getDisplayName':"+getDisplayNameTemp+"****************************************");
                                                    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                                    DatabaseReference selectedTeamRef = database.getReference("Participants").child(league_name).child(element).child("Selected Team");

                                                    selectedTeamRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.getValue() != null&&!dataSnapshot.getValue().equals("Empty")) {
                                                                // selectedTeamView.setText(dataSnapshot.getValue().toString());
                                                                String teamName = dataSnapshot.getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                                                                int teamBadge = ViewLeagueActivity.this.getResources().getIdentifier(teamName, "drawable", ViewLeagueActivity.this.getPackageName());
                                                                // Drawable image= ViewLeagueActivity.this.getResources().getIdentifier(teamName, "drawable", ViewLeagueActivity.this.getPackageName());
                                                                //teamBadgeView.setImageResource(teamBadge);
                                                                Drawable image = ContextCompat.getDrawable(getApplicationContext(),teamBadge);

                                                                //++
                                                                int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
                                                                Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
                                                                final Drawable scaledDrawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, px, px, true ));
                                                                //++

                                                               // textView.setCompoundDrawablesWithIntrinsicBounds(scaledDrawable, null,null,null);
                                                                //textView.setBackground(image);
                                                                //textView.setBackgroundResource(teamBadge);

                                                    refToPersonalStandings.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            if (dataSnapshot.exists()&&model.contains(getDisplayNameTemp)&&!myCheckListOfDisplayNames.contains(getDisplayNameTemp)) {
                                                                String s = dataSnapshot.getValue(String.class);

                                                                if (s.equals("In")) {
                                                                    Drawable image = getDrawable(R.drawable.standings_stillstanding_image);
                                                                    textView.setCompoundDrawablesWithIntrinsicBounds(scaledDrawable, null, image, null);
                                                                  //  textView.setTextColor(Color.GREEN);
                                                                    textView.setTextSize(15);

                                                                    textView.setBackgroundColor(Color.GREEN);
                                                                } else if (s.equals("Out")) {
                                                                    Drawable image = getDrawable(R.drawable.standing_out_knockedout_image);
                                                                    textView.setCompoundDrawablesWithIntrinsicBounds(scaledDrawable, null, image, null);
                                                                   // textView.setTextColor(Color.RED);
                                                                    textView.setTextSize(15);

                                                                     textView.setBackgroundColor(Color.RED);

                                                                }
                                                                else if(s.equals("Winner")){
                                                                    Drawable image = getDrawable(R.drawable.winner_small_simple);
                                                                    textView.setCompoundDrawablesWithIntrinsicBounds(scaledDrawable, null, image, null);
                                                                   // textView.setTextColor(Color.YELLOW);
                                                                    textView.setTextSize(15);

                                                                    textView.setBackgroundColor(Color.YELLOW);
                                                                }
                                                                //a check that this displayName background/colour/ image has already been set
                                                               /* if(!myCheckListOfDisplayNames.contains(getDisplayNameTemp)) {
                                                                    myCheckListOfDisplayNames.add(getDisplayNameTemp);
                                                                }*/



                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                                //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++}


                                                            }

                                                                if(model.contains("*EMPTY*")) {
                                                                    Drawable image = getDrawable(R.drawable.unknown_image);
                                                                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);
                                                                    textView.setBackgroundColor(Color.GRAY);
                                                                }
                                                               /* else{
                                                                    // /*
                                                                    //  Drawable image = getDrawable(R.drawable.unknown_image);
                                                                    textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                                                                    textView.setBackgroundColor(Color.TRANSPARENT);

                                                                }*/


                                                            System.out.println("**************************Contents of 'myCheckListOfDisplayNames':"+myCheckListOfDisplayNames+"**************************");
                                                            System.out.println("**************************'String.valueOf(myCheckListOfDisplayNames')"+String.valueOf(myCheckListOfDisplayNames)+"**************************************************");

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });

                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
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



                            //*try to implement firebase ref listener, to listen to stnadings to effect images (in/out/winner)*
                            //******************************************test start****************************************

                          //-----------final DatabaseReference refForSettingImagesFromPaticpantStandings=FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Participants/G/");
                          /*  final DatabaseReference refForSettingImagesFromPaticpantStandings=database.getReference().child("Participants").child(league_name);

                            FirebaseListAdapter <String> listOfUsers=new FirebaseListAdapter<String>(
                                    ViewLeagueActivity.this,
                                    String.class,
                                    android.R.layout.simple_list_item_1,
                                    refForSettingImagesFromPaticpantStandings

                            ) {
                                @Override
                                protected void populateView(View v, String model, int position) {
                                   // final TextView textView= (TextView) v.findViewById(R.id.text1);
                                    List <String> myStringList;
                                    System.out.println("**************************************[position,model]:["+position+model+"]*************************************************");

                                }
                            };*/


                            // String userkey=refForSettingImagesFromPaticpantStandings.getKey();
                           // System.out.println("***************************** userkey:"+userkey+"******************************");
                          /*  refForSettingImagesFromPaticpantStandings.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                    Object obj=dataSnapshot;
                                    System.out.println("******************************obj:"+obj+"******************************************");

                                    String key=refForSettingImagesFromPaticpantStandings.getKey();
                                   // String userkey=s;
                                    System.out.println("***************************** userkey:"+key+"******************************");
                               //     if(refForSettingImagesFromPaticpantStandings.child(userkey).child("Standing").equals("In"))
                                    if(obj.equals("In"))

                                        {
                                        //player should have 'in' image beside name
                                        Drawable image =getDrawable(R.drawable.standing_in_image2);
                                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);
                                    }else {
                                        //player should have 'out' image beside name
                                        Drawable image= getDrawable(R.drawable.standing_out_image);
                                        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, image, null);

                                    }

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                         //***************************test end***************************
                         */

                            //  textView.setText("LeagueName: "+model.getLeagueName()+"\n\nParticipants:\n"+model.getParticipants()+"\n");
                            textView.setText(model);
                        }
                    };
                    mListView.setAdapter(firebaseListAdapter);

                    //*******************************
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

     /*   MOVED TO THE TOP SO IMAGE CAN BE USED IN LIST TOO ^^^
        final TextView deadlineField = (TextView) findViewById(R.id.deadlineField);
        final TextView selectedTeamView = (TextView) findViewById(R.id.selectedTeamField);
        final ImageView teamBadgeView = (ImageView) findViewById(R.id.teamBadgeView);*/
/*
        DatabaseReference currentGameweekRef = database.getReference("Premier League").child("Current Gameweek");

        currentGameweekRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String currentGameweek = dataSnapshot.getValue().toString();
                DatabaseReference deadlineRef = database.getReference("Premier League").child(currentGameweek).child("Deadline");

                deadlineRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        deadlineField.setText(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        //final FirebaseUser userRef2 = FirebaseAuth.getInstance().getCurrentUser();
        //final FirebaseDatabase databaseRef2 = FirebaseDatabase.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // User is signed in

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(ViewLeagueActivity.this, WelcomePageActivity.class);
                    startActivity(intent);
                }
            }
        };

       // DatabaseReference participantsExistRef=database.getReference("Participants");
       /* participantsExistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //*if participants ref exists*
                if (dataSnapshot.exists()) {*/
                    //then try to read value from 'selectedTeamRef'
        /*
                    if (user != null&&league_name!=null) {
                        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        DatabaseReference selectedTeamRef = database.getReference("Participants").child(league_name).child(user.getUid()).child("Selected Team");

                        selectedTeamRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    selectedTeamView.setText(dataSnapshot.getValue().toString());
                                    String teamName = dataSnapshot.getValue().toString().toLowerCase().replace(" ", "_") + "_badge";
                                    int teamBadge = ViewLeagueActivity.this.getResources().getIdentifier(teamName, "drawable", ViewLeagueActivity.this.getPackageName());
                                    teamBadgeView.setImageResource(teamBadge);

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++}

                }
                */
         //   }

         /*   @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        //***************GLENN's ADDON CODE V2 end******************
/*
        //disable team pick function if player is not in league
        DatabaseReference leagueParticipantsRef=database.getReference("Leagues").child(league_name).child("participants");
        leagueParticipantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot!=null) {
                    List temp=dataSnapshot.getValue(t); //get list, check if it is/contains '*EMPTY*'

                    if (!temp.contains("*EMPTY*")&&temp.contains(user.getEmail())) //if participant list is not empty, and you are in it
                    {
                        isInLeague=true;
                        mpickTeamBtn.setEnabled(true);
                    } else {
                        isInLeague=false;
                        mpickTeamBtn.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //disable team pick function if standing is 'out', Glenn's code
        DatabaseReference standingRef=database.getReference("Participants").child(league_name).child(user.getUid()).child("Standing");

        standingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot!=null) {
                    if (dataSnapshot.getValue().toString().equals("In")&&isInLeague==true) {
                        mpickTeamBtn.setEnabled(true);


                    } else {
                        mpickTeamBtn.setEnabled(false);
                       // mpickTeamBtn.setBackgroundColor(Color.TRANSPARENT);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        /*    24/04/2017  *REMOVED magnage league button, for menu dropdown*
        //disable manage league if current user is not the admin
        DatabaseReference adminRef=database.getReference("Leagues").child(league_name).child("admin");

        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()&&dataSnapshot!=null) {
                    String adminRef=dataSnapshot.getValue(String.class);
                    System.out.println("************* (in 1st if) adminRef:"+adminRef+"********************");
                    System.out.println("************* (in 1st if) userId:"+user.getEmail()+"********************");

                    System.out.println("************* (in 1st if) datasnapshot:"+dataSnapshot+"********************");


                    if (adminRef.equals(user.getEmail())) {
                        System.out.println("************* (in 2nd if) datasnapshot:"+dataSnapshot+"********************");
                        manageLeague.setEnabled(true);


                        manageLeague.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView leagueNameField = (TextView) findViewById(R.id.textView50);

                                Toast.makeText(getApplicationContext(), "Welcome to your League Management!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewLeagueActivity.this, ManageLectureActivity.class);
                                String leagueName = leagueNameField.getText().toString().trim();
                                intent.putExtra(EXTRA_MESSAGE, leagueName);
                                startActivity(intent);
                            }
                        });

                    } else {
                        manageLeague.setEnabled(false);
                        System.out.println("************* (in else of 2nd if)datasnapshot:"+dataSnapshot+"********************");
                      /*  manageLeague.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getApplicationContext(), "*ACCESS DENIED*: Only the admin of the league can manage it!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        */
    } //oustside onCreate
    //***Added Glenn's menu code start***
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //boolean admin = true;

        if(adminCheck) {
            getMenuInflater().inflate(R.menu.view_league_admin, menu);
        } else {
            getMenuInflater().inflate(R.menu.view_league_participant, menu);
        }
        return true;
    }
/*
    public void pickTeam(View view) {
        //***PICKED TEAM start***

        TextView leagueNameField = (TextView) findViewById(R.id.textView50);
        String leagueName = leagueNameField.getText().toString().trim();

        Intent intent = new Intent(this, PickTeamActivity.class);
        intent.putExtra(EXTRA_MESSAGE, leagueName);
        startActivity(intent);
        //***PICKED TEAM end***
    }
    */
/*
    public void usersTeams(View view) {
        int usersFieldId = view.getId();
        TextView usersNameField = (TextView) findViewById(usersFieldId);
        String usersName = usersNameField.getText().toString().trim();
        Intent intent = new Intent(this, UsersTeamsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, usersName);
        startActivity(intent);
    }*/

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


    //****OBSELETE*****
  /*  public void manageLeague(View view) {
        Intent intent = new Intent(this, ManageLectureActivity.class);
        startActivity(intent);
    }

    public void pickTeam(View view) {
        Intent intent = new Intent(this, PickTeamActivity.class);
        startActivity(intent);
    }*/

/*   public void manageLeague(View view) {
        TextView leagueNameField = (TextView) findViewById(R.id.textView50);

        //*Get User ID* START
       // final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       /* if (user != null) {
            // User is signed in
            admin = user.getEmail();

            // Username="'"+user.getEmail()+"'";
            UserID = user.getUid();
        } else {
            // No user is signed in
        }*/
      //  final DatabaseReference refToAdminOfLeague=FirebaseDatabase.getInstance().getReferenceFromUrl();

        //*Get User ID* END
       // if(user.getUid()!=leagueAdmin) {
      /*       Intent intent = new Intent(this, ManageLectureActivity.class);
            String leagueName = leagueNameField.getText().toString().trim();
            intent.putExtra(EXTRA_MESSAGE, leagueName);
            startActivity(intent);*/
      //  }
   // }
}
/*
 *******************************FIREBASE LIST ADAPTER start***************************************

        final DatabaseReference refToLeagueParticipants2= database.getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/"+leagueName+"/participants");

        //*Firebase listadapter, used to make a list on the page using data on the database
        //this adapter works with our list and displays whats on firebase
        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                this,
                String.class,
                android.R.layout.simple_list_item_1,
                refToLeagueParticipants2
        ) {
            @Override
            //*Displays a list on the app, of leagues currently stored in database in realtime*
            protected void populateView(View v, String model, int position) {
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                //textView.setText("LeagueName: "+model.getLeagueName()+"\n\nParticipants:\n"+model.getParticipants()+"\n");
                textView.setText(model);
            }
        };
       mListView.setAdapter(firebaseListAdapter);
       // *******************************FIREBASE LIST ADAPTER end****************************************
       */