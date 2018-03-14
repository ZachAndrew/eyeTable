package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import static andrew.zach.eyeTableP1.LecturesActivity.EXTRA_MESSAGE;
import static android.graphics.Color.GREEN;

/**
 * Created by Zach on 08/11/2017.
 */

public class ViewLectureInfo extends AppCompatActivity {

    GenericTypeIndicator<List<String>> t=new GenericTypeIndicator<List<String>>() {};



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_manage_league) {

            Intent intent2 = getIntent();
            final String activity_name = intent2.getStringExtra(EXTRA_MESSAGE);
            // String leagueName = leagueNameField.getText().toString().trim();

         /*   TextView LeagueName=(TextView) findViewById(R.id.textViewLeagueName2);*/
            String currentActiviity_name = activity_name;
            // intent2.putExtra(EXTRA_MESSAGE, leagueName);

            Intent intent = new Intent(this, ManageLectureActivity.class);
            Toast.makeText(getApplicationContext(), "Welcome to your League Management!", Toast.LENGTH_SHORT).show();
            intent.putExtra(EXTRA_MESSAGE, currentActiviity_name);

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
        setContentView(R.layout.activity_view_lecture);

        String UserID="Default",lect_name="Default";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();


/*
        final TextView deadlineField = (TextView) findViewById(R.id.deadlineField);
        final TextView selectedTeamView = (TextView) findViewById(R.id.selectedTeamField);
        final ImageView teamBadgeView = (ImageView) findViewById(R.id.teamBadgeView);*/
        final TextView lectNameViewed=(TextView)findViewById(R.id.textView_view_lecture_lectName);
        final TextView lectInfoDisplay=(TextView)findViewById(R.id.textView_displayLectInfo); //a text box to display lecture info from firebase
        final Button deleteLectureFromDB=(Button) findViewById(R.id.button_delete_lecture); // delete lecture from DB
        final Button clearFromTable=(Button) findViewById(R.id.button_clear_from_table);// clear from table
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
        final Intent intent = getIntent();
        lect_name = intent.getStringExtra(EXTRA_MESSAGE);
        final String lecture_name_stored=lect_name;

        System.out.println("\n\n\n*****************Lecture Name:"+lect_name+"**********************\n\n\n");

        lectNameViewed.setText(lect_name);

        //*Now pull down info on 'lect_name' lecture located in database*
        final DatabaseReference refToLectures=database.getReference().child("Leagues").child(lect_name);
        final DatabaseReference refToListOfActivities=database.getReference().child("LeagueList");
        final DatabaseReference refToButtonsNode=database.getReference().child("Buttons");

        refToLectures.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    //#3 test: (SUCCESS)
                    LectureClass obj=dataSnapshot.getValue(LectureClass.class);
                    System.out.println("***********************Value of dataSnapshot"+dataSnapshot.getValue()+"*******************************");

                    lectInfoDisplay.setText("Lecture name:"+obj.getLeagueName()+"\n\nLecture admin:\n"+obj.getAdmin()+"\n\nCredits:"+obj.getCredits()
                    +"\n\nDisplay names:"+obj.getDisplayNames()+"\n\nLecture live:"+obj.getLeagueAlive()+"\n\nLeague Code:"+obj.getLeagueCode());



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

        //(1)start:implement delete function
        deleteLectureFromDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //use firebase DB on current lecture
                refToLectures.removeValue(); //remove current value located on the firebase
                refToListOfActivities.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //if the list contains the name of the activity to be removed, remove it from the list as well
                        if(dataSnapshot!=null) {
                            List temp=dataSnapshot.getValue(t);//get list from DB
                            if (temp!=null)//if a list has actually been stored in temp
                            {
                                if (temp.contains(lecture_name_stored)) {
                                    temp.remove(lecture_name_stored);//remove from list acquired from DB
                                    refToListOfActivities.setValue(temp);//set te new list onto the DB
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                //remove value also from list of activities


                Toast.makeText(ViewLectureInfo.this,"The lecture '"+lecture_name_stored+"' has been successfully deleted from the Database",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(ViewLectureInfo.this, LecturesActivity.class);
                startActivity(intent);
            }
        });

        //:(1) end
        clearFromTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                refToButtonsNode.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                             //   Log.v(TAG,"£££ CHECKING CHILD KEY £££"+ childDataSnapshot.getKey()); //displays the key for the node
                             //   Log.v(TAG,"£££ CHECKING CHILD VALUE £££"+ childDataSnapshot.getValue());   //gives the value for given keyname
                                System.out.println("KEY BUTTON:"+childDataSnapshot.getKey()+"\n Value:"+childDataSnapshot.getValue());
                                //  System.out.println("$$$$$$$$VALUE OF CHILD IN 'BUTTONS node on DB are:"+childDataSnapshot.getValue());

                                int tempKeyStorage=Integer.parseInt(childDataSnapshot.getKey()); //get value of current button key
                                String tempValStorage=childDataSnapshot.getValue(String.class);//get value of current button value



                                if(childDataSnapshot.exists()) //if a button key has been stored
                                {

                                    System.out.println("++++++++++++++++++ DATA EXISTS, PROCEEDING TO CLEAR BUTTON DATA CODE  ++++++++++++++++++");
                                    //clear Button info in 'Buttons' ref
                                    if(childDataSnapshot.getValue()!=null) {
                                        //if lecture exists in buttons node ref on DB
                                        if (childDataSnapshot.getValue().equals(lecture_name_stored)) {
                                            //clear button info containing current lecture/activity name
                                            DatabaseReference currentButtonToClear=database.getReference().child("Buttons/"+tempKeyStorage);
                                            currentButtonToClear.removeValue();// remove data at this point to clear it from table


                                           Toast.makeText(ViewLectureInfo.this,"'"+lecture_name_stored+"' has been removed from the timeTable view successfully.",Toast.LENGTH_SHORT).show();
                                           //bring the user to the eyeTable viewing page
                                            Intent intent =new Intent(ViewLectureInfo.this, EyeTableActivity.class);
                                            startActivity(intent); //take user to eyeTable page

                                        }
                                    }
//                                    final Button updateButton2 = (Button) findViewById(tempKeyStorage);
//                                    updateButton2.findViewById(tempKeyStorage);
//                                    updateButton2.setText(tempValStorage);
//                                    updateButton2.setTextColor(GREEN);

                                }
                                else {
                                    System.out.println("++++++++++++++++++ HAVE NOT BEEN THROUGH BUTTON CLEAR DATA PROCEDURE +++++++++++++++++++++++++++++++++");
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        //(2)start:implement clear from table function


        //:(2)end



    }
}

