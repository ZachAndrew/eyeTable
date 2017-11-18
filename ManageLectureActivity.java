package andrew.zach.eyeTableP1;

import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.List;

import static andrew.zach.eyeTableP1.ViewLeagueActivity.EXTRA_MESSAGE;


public class ManageLectureActivity extends AppCompatActivity {


    String username="NULL",userID="NULL",leaguename="NULL",leagueCode="Nothing yet";

    private ListView mListView; //a list view referenced by id

    //*Generic type indicator for pulling down list obj from database*
    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};

    List <String>participantsList;
    List <String>displayNameslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_league);


        //*Get User ID* START
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            username=user.getEmail();
            // Username="'"+user.getEmail()+"'";
            userID=user.getUid();
        } else {
            // No user is signed in
        }

        Intent intent = getIntent();
        final String league_name = intent.getStringExtra(EXTRA_MESSAGE);


        //*Firebase database reference paths*


        //*Buttons*
        Button deleteLeague=(Button)findViewById(R.id.button5);//intialise the the delete button

        //*EditText fields*
        final TextView leagueToDeleteName=(TextView)findViewById(R.id.textViewLeagueName2);//text field for removing leagues by leaguename (top text field)
     //REMOVED AND REPLACED WITH TEXTVIEW   final EditText playerNameToDelete=(EditText)findViewById(R.id.editText13);//text field for entering the number of player to be removed in the league (3rd text field from top)
        final TextView playerNameToDelete=(TextView)findViewById(R.id.textView70);

     //REMOVED AND REPLACED WITH TEXTVIEW   final EditText leagueToDeletePlayerFrom=(EditText)findViewById(R.id.editText14);//text field to acquire league to delete player from (4th text field from top)
       // final TextView leagueToDeletePlayerFrom=(TextView) findViewById(R.id.textView14);//text field to acquire league to delete player from (4th text field from top)
        final TextView leagueCodeTextView=(TextView)findViewById(R.id.textView83);//text view to display 'leagueCode' for the league

        final TextView leagueToDeletePlayerFrom=(TextView)findViewById(R.id.textViewLeagueName2);//text field for removing leagues by leaguename (top text field)

       //************************ leagueToDeletePlayerFrom.setText(league_name);
        leagueToDeleteName.setText(league_name);
        //*ImageViews*
        ImageView xImageToDeletePlayer=(ImageView)findViewById(R.id.imageView);//X image to press to delete player

        //*ListView*
        final ListView mListView=(ListView)findViewById(R.id.listViewParticipants);//list view for participants

        //get league code and set it to 'leagueCodeTextView'
        DatabaseReference datbase=FirebaseDatabase.getInstance().getReference();
        DatabaseReference leagueCodeRef=datbase.getDatabase().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + league_name);

        leagueCodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get leagueCode from the league
                LectureClass tempLeague=dataSnapshot.getValue(LectureClass.class);
                leagueCode=tempLeague.getLeagueCode().toString();
                //leagueCode=dataSnapshot.getValue(String.class);
                System.out.println("*****************VALUE OF 'leagueCode' is:"+leagueCode+"**********************");
                //set league code to view
                leagueCodeTextView.setText(leagueCode);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        deleteLeague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!leagueToDeleteName.getText().toString().isEmpty()) {
                    leaguename = leagueToDeleteName.getText().toString();//get leaguename from text field
                    //*Ref paths to delete the typed in league by user*
                    DatabaseReference deleteLeagueFromGeneralData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leaguename);
                    DatabaseReference deleteLeagueFromPersonalData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/" + userID + "/LeagueEnteredIn/" + leaguename);
                    final DatabaseReference deleteLeagueFromLeagueListData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//LeagueList/");
                    final DatabaseReference deleteLeagueFromParticipantsData=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Participants/"+leaguename);

                    //*Delete leagues from these paths*
                    deleteLeagueFromGeneralData.removeValue();
                    deleteLeagueFromPersonalData.removeValue();
                    deleteLeagueFromParticipantsData.removeValue();

                    deleteLeagueFromLeagueListData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List <String> list=dataSnapshot.getValue(t);

                            System.out.println("***************LEAGUELIST BEFORE DELETE:"+list+"********************");
                            if(list.contains(leaguename)){

                                list.remove(leaguename);
                                deleteLeagueFromLeagueListData.setValue(list);
                                System.out.println("***************LEAGUELIST AFTER DELETE:"+list+"********************");

                                //leave page after deleting league from database
                                Toast.makeText(getApplicationContext(), "League '"+leaguename+"' has been deleted from the database!", Toast.LENGTH_SHORT).show();


                                Intent intent=new Intent(ManageLectureActivity.this,LecturesActivity.class);
                                startActivity(intent);


                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });
         xImageToDeletePlayer.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {


                 if( (!playerNameToDelete.getText().toString().isEmpty() )&&(!leagueToDeletePlayerFrom.getText().toString().isEmpty()) ) //if user has typed in something in both text fields, allow delete
                 {
                     final String playerName=playerNameToDelete.getText().toString();
                     final String leagueToDeletePlayer = leagueToDeletePlayerFrom.getText().toString();

                     final DatabaseReference refToDisplayNamesList=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leagueToDeletePlayer + "/displayNames/");


                     final DatabaseReference deletePlayerFromGeneralData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leagueToDeletePlayer + "/participants/");

                     deletePlayerFromGeneralData.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             participantsList=dataSnapshot.getValue(t);
                             //if size of list is 1, replace, don't remove
                             if(participantsList.size()>1&&!playerName.equals("*EMPTY*")) {
                                 participantsList.contains(playerName);

                                 //***get position of participant, to remove from displaynames list
                                 final int tempPositionOfUser;
                                 tempPositionOfUser=participantsList.lastIndexOf(playerName);

                                 System.out.println("*****************VALUE OF 'tempPositionOfUser':"+tempPositionOfUser+"***********************");
                                 final DatabaseReference deleteDisplayNameFromGeneralData=FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + leagueToDeletePlayer + "/displayNames/"+tempPositionOfUser);

                                 deleteDisplayNameFromGeneralData.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         if(dataSnapshot.exists()&&!playerNameToDelete.getText().toString().equals("*EMPTY*")) {
                                             // dataSnapshot.getRef().removeValue();

                                             refToDisplayNamesList.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                                     //pull down list from database
                                                     displayNameslist=dataSnapshot.getValue(t);//pulls down displayNames list
                                                   if(displayNameslist.size()>1&&!playerName.equals("*EMPTY*")) {
                                                       displayNameslist.remove(tempPositionOfUser); //remove the value of display names at the appropiate list position
                                                       refToDisplayNamesList.setValue(displayNameslist);
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
                                 //***


                              //  displayNameslist.remove(playerName);
                              //   refToDisplayNamesList.setValue(displayNameslist);


                                 participantsList.remove(playerName);
                                 deletePlayerFromGeneralData.setValue(participantsList);
                             }
                             else{
                                 //***get position of participant, to remove from displaynames list
                                 final int tempPositionOfUser;
                                 tempPositionOfUser=participantsList.lastIndexOf(playerName);
                                 System.out.println("***********************VALUE OF 'temPositionOfUser':"+tempPositionOfUser+"*************************");


                                 refToDisplayNamesList.addListenerForSingleValueEvent(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(DataSnapshot dataSnapshot) {
                                         //pull down list from database
                                         displayNameslist=dataSnapshot.getValue(t);//pulls down displayNames list
                                         if(displayNameslist.size()>1&&!playerName.equals("*EMPTY*")) {
                                             displayNameslist.remove(tempPositionOfUser); //remove the value of display names at the appropiate list position
                                             refToDisplayNamesList.setValue(displayNameslist);
                                         }else {
                                            // displayNameslist.remove(tempPositionOfUser);
                                             displayNameslist.clear();
                                             displayNameslist.add("*EMPTY*");
                                             refToDisplayNamesList.setValue(displayNameslist);
                                         }
                                     }

                                     @Override
                                     public void onCancelled(DatabaseError databaseError) {

                                     }
                                 });
                                 if(!participantsList.contains("*EMPTY*")) {
                                     participantsList.remove(playerName);
                                     participantsList.add("*EMPTY*");

                                 }
                                  /*   if(displayNameslist!=null) {
                                         if (!displayNameslist.contains("*EMPTY*")) {
                                             displayNameslist.clear();
                                             displayNameslist.add("*EMPTY*");
                                         }*/

                                         /*else if(!playerNameToDelete.getText().toString().equals("*EMPTY*")&&!displayNameslist.contains("*EMPTY*")){
                                             //get position ofn ame being deleted in 'particpantsList' (to use to delete the right ' displayName')
                                             int tempPos=participantsList.indexOf(playerName);
                                             displayNameslist.remove(tempPos);
                                         }*/
                                     }

                              /*   if(!playerNameToDelete.getText().toString().equals("*EMPTY*")) {
                                     System.out.println("************************ TEST 1*******************************");
                                     System.out.println("************'tempPositionOfUser':"+tempPositionOfUser+"*******************");

                                     System.out.println("*********** BEFORE DELETE:participantList.size():"+participantsList.size()+"*************");
                                     System.out.println("***********BEFORE DELETE:displayNamesList.size():"+displayNameslist.size()+"*************\n");

                                     participantsList.remove(playerName);
                                     displayNameslist.remove(tempPositionOfUser);

                                     System.out.println("*********** AFTER DELETE:participantList.size():"+participantsList.size()+"*************");
                                     System.out.println("***********AFTER DELETE:displayNamesList.size():"+displayNameslist.size()+"*************");

                                 }*/

                                  /*   if(displayNameslist!=null&&!displayNameslist.contains("*EMPTY*")&&!playerNameToDelete.getText().toString().equals("*EMPTY*")){
                                     int tempPos=participantsList.indexOf(playerName);
                                     displayNameslist.remove(tempPos);
                                     }*/

                                 /*   if(displayNameslist!=null&&displayNameslist.size()==0&&!playerNameToDelete.getText().toString().equals("*EMPTY*")){
                                        displayNameslist.remove(0);
                                    }*/


                                 deletePlayerFromGeneralData.setValue(participantsList);
                                 if(displayNameslist!=null) {
                                     refToDisplayNamesList.setValue(displayNameslist);
                                 }
                             }


                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });

                     //*Delete players attempt2 start*
                    final DatabaseReference deletePlayerFromPersonalData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Users/" + userID + "/LeagueEnteredIn/" + leagueToDeletePlayerFrom.getText().toString() + "/participants");
                     deletePlayerFromPersonalData.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             participantsList=dataSnapshot.getValue(t);
                             if(participantsList!=null) {
                                 //if size of list is 1, replace, don't remove
                                 if (participantsList.size() > 1&&!playerName.equals("*EMPTY*")) {

                                     participantsList.contains(playerName);
                                     participantsList.remove(playerName);
                                     deletePlayerFromPersonalData.setValue(participantsList);
                                 } else {
                                     if (!participantsList.contains("*EMPTY*")) {
                                         participantsList.add("*EMPTY*");
                                     }
                                     if(!playerName.equals("*EMPTY*")) {
                                         participantsList.remove(playerName);
                                     }

                                     deletePlayerFromPersonalData.setValue(participantsList);
                                 }
                             }
                         }
                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });
                     //*Delete players attempt 2 end*


                     //*Delete players attempt 1*
                     /*
                     String playerNo = playerNumberToDelete.getText().toString();
                     final String leagueToDeletePlayer = leagueToDeletePlayerFrom.getText().toString();
                     //*Database references to delete player from*
                     DatabaseReference deletePlayerFromGeneralData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Leagues/" + leagueToDeletePlayer + "/participants/" + playerNo);
                     DatabaseReference deletePlayerFromPersonalData = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Users/" + userID + "/LeagueEnteredIn/" + leagueToDeletePlayer + "/participant");

                     //*Delete player from the above references*
                     deletePlayerFromGeneralData.removeValue();
                     deletePlayerFromPersonalData.removeValue();
                     */
                     //*Delete players attempt 1 end*
                 }
             }
         });
        //*********ADDING PARTICIPANT LIST attemt 1 start**************
        //create a list view for participants
        final FirebaseDatabase database=FirebaseDatabase.getInstance();

        final DatabaseReference refForListener = database.getReference("Leagues").child(league_name).child("participants");


        refForListener.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //*******************************FIREBASE LIST ADAPTER start***************************************
                //*ref using EXTRAMESSAGE PASSED FROM 'LecturesActivity.java' page*
                final DatabaseReference refToLeagueParticipants = database.getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/" + league_name + "/participants");
              //---  final DatabaseReference refToPersonalLeagueParticipants = database.getReferenceFromUrl("https://androidapplms-database.firebaseio.com/Users/" + UserID + "/LeagueEnteredIn/" + league_name + "/participants");


                //*Firebase listadapter, used to make a list on the page using data on the database
                //this adapter works with our list and displays whats on firebase

                FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                        ManageLectureActivity.this,
                        String.class,
                        android.R.layout.simple_list_item_1,
                        refToLeagueParticipants
                ) {
                    @Override
                    //*Displays a list on the app, of leagues currently stored in database in realtime*
                    protected void populateView(View v, String model, int position) {
                        TextView textView = (TextView) v.findViewById(android.R.id.text1);
                        //  textView.setText("LeagueName: "+model.getLeagueName()+"\n\nParticipants:\n"+model.getParticipants()+"\n");
                        textView.setText(model);
                    }
                };
                mListView.setAdapter(firebaseListAdapter);
                // viewForParticipants.setText(firebaseListAdapter.toString());
                //  viewForParticipants.setText(firebaseListAdapter.getItem(0));

                // *******************************FIREBASE LIST ADAPTER end****************************************
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });
        //*********ADDING PARTICIPANT LIST attemt 1 end**************

        //Trying to implement clickable list START
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {

                int x=position;
                Object obj=arg0.getItemAtPosition(x);
               // LectureClass myObj=(LectureClass) leagueObj;

                String participantName=(String) obj; //get leaguename for ViewLeagueActivity page

                //now set textView with particpant name to name clicked from list
                playerNameToDelete.setText(participantName);

            }
        });
        //clickable list implementation END





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

    //to copy league code to clipboard
    public void copyCode(View view) {
        TextView leagueCodeField = (TextView) findViewById(R.id.textView83);
        String leagueCode = leagueCodeField.getText().toString().trim();
        ClipboardManager _clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(leagueCode, leagueCode);
        _clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Code has been copied to clipboard!", Toast.LENGTH_SHORT).show();
    }


}
