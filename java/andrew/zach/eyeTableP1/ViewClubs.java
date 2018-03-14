package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.util.List;

import static andrew.zach.eyeTableP1.LecturesActivity.EXTRA_MESSAGE;

/**
 * Created by Zach on 12/12/2017.
 */


    public class ViewClubs extends AppCompatActivity {

        GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
        };


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if (id == R.id.action_manage_league) {

                Intent intent2 = getIntent();
                final String league_name = intent2.getStringExtra(EXTRA_MESSAGE);
                // String leagueName = leagueNameField.getText().toString().trim();

         /*   TextView LeagueName=(TextView) findViewById(R.id.textViewLeagueName2);*/
                String leagueName = league_name;
                // intent2.putExtra(EXTRA_MESSAGE, leagueName);

                Intent intent = new Intent(this, ManageLectureActivity.class);
                Toast.makeText(getApplicationContext(), "Welcome to your League Management!", Toast.LENGTH_SHORT).show();
                intent.putExtra(EXTRA_MESSAGE, leagueName);

                // Intent intent = new Intent(ViewLeagueActivity.this, ManageLectureActivity.class);

                // String leagueName = leagueNameField.getText().toString().trim();

                startActivity(intent);
            }
    /*else if(id == R.id.action_leave_league) {
        Toast.makeText(getApplicationContext(), "You have left the league!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LecturesActivity.class);
        startActivity(intent);
    }*/
            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_clubs);

            String UserID = "Default", lect_name = "Default";

            final FirebaseDatabase database = FirebaseDatabase.getInstance();


/*
            final TextView deadlineField = (TextView) findViewById(R.id.deadlineField);
            final TextView selectedTeamView = (TextView) findViewById(R.id.selectedTeamField);
            final ImageView teamBadgeView = (ImageView) findViewById(R.id.teamBadgeView);*/
            final TextView clubNameViewed = (TextView) findViewById(R.id.textView_view_club_clubName);
            final TextView clubInfoDisplay = (TextView) findViewById(R.id.textView_displayClubInfo); //a text box to display lecture info from firebase

            final Button deleteClubFromDB=(Button)findViewById(R.id.button_delete_club); //delete club from the database
            //final ListView listViewForLectInfo=(ListView)findViewById(R.id.listView_displayInfoOnLect);// displays info based on lecture class on db

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

            //*****GET EXTRA_MESSAGE CODE FROM 'LecturesActivity.java'  START**********
            Intent intent = getIntent();
            lect_name = intent.getStringExtra(EXTRA_MESSAGE);
            final String lecture_name_stored = lect_name;

            System.out.println("\n\n\n*****************Lecture Name:" + lect_name + "**********************\n\n\n");

            clubNameViewed.setText(lect_name);

            //*Now pull down info on 'lect_name' lecture located in database*
            final DatabaseReference refToLectures = database.getReference().child("Clubs").child(lect_name);

            refToLectures.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        //#3 test: (SUCCESS)
                        ClubClass obj = dataSnapshot.getValue(ClubClass.class);
                        System.out.println("***********************Value of dataSnapshot" + dataSnapshot.getValue() + "*******************************");

                        clubInfoDisplay.setText("Club name:" + obj.getClubName() + "\n\nClub admin:\n" + obj.getPresident() + "\n\nActivity Type:" + obj.getActivityType()
                                + "\n\nDisplay names:" + obj.getDisplayNames() + "\n\nClub live:" + obj.getClubAlive() + "\n\nDescription:\n" + obj.getDescription());


                        //#3 end.
                        //#1 start: *TEST* final List temp = dataSnapshot.getValue(t);

                        //*Test 1: to see if info can be seen (SUCCESS)
                    /*
                    Object obj=dataSnapshot.getValue();
                    System.out.println("***********************Value of dataSnapshot"+dataSnapshot.getValue()+"*******************************");
                    String infoFromObj=obj.toString(); //cast obj into a string object
                    System.out.println("***************************TEST:Checking value of 'infoFromObj':"+infoFromObj+"**********************************");

                    lectInfoDisplay.setText(infoFromObj);
                    */
                        //*/#1 end


                        //#2 start: *implement a listView*
                    /*
                    FirebaseListAdapter<LectureClass> firebaseListAdapter = new FirebaseListAdapter<LectureClass>(
                            ViewLectureInfo.this,
                            LectureClass.class,
                            android.R.layout.simple_list_item_1,
                            refToLectures

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

                        }
                    };
                    listViewForLectInfo.setAdapter(firebaseListAdapter); //set acquired info into list view for user to see
                    */
                        //#2 end: list view implementation end

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            deleteClubFromDB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //use ref from the DB to remove the current club
                    refToLectures.removeValue(); //remove current value from DB, i.e. the club present at this node
                    //ref to the list of activities to remove club from if present
                    final DatabaseReference mRefToRemoveFromAcitivityList=database.getReference().child("LeagueList");
                    mRefToRemoveFromAcitivityList.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists())//is the league list exists
                            {
                                //get the league list
                                List tempList =dataSnapshot.getValue(t);
                                if(tempList.contains(lecture_name_stored)){
                                    tempList.remove(lecture_name_stored);//remove from tempList
                                    mRefToRemoveFromAcitivityList.setValue(tempList);//set the value of temp list back onto DB without club name
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //let user know
                    Toast.makeText(ViewClubs.this,"The club '"+lecture_name_stored+"' has been successfully removed from the database.",Toast.LENGTH_LONG).show();
                    //switch back to main page
                    Intent intent=new Intent(ViewClubs.this,LecturesActivity.class);
                    startActivity(intent);
                }
            });


        }
    }


