package andrew.zach.eyeTableP1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static andrew.zach.eyeTableP1.EyeTableActivity.EXTRA_MESSAGE_ID_OF_BUTTON;

/**
 * Created by Zach on 09/02/2018.
 */

public class Pop extends Activity {
    private ListView mListView;

    List <String>listOfModules= new ArrayList<>(); //a string lst to only contain module names
    List <LectureClass> listOfLectures;// a list able to contain objs of type LectureClass
    private ArrayAdapter<String> listAdapter ;

    public static final String EXTRA_MESSAGE2 = "andrew.zach.eyeTableP1.MESSAGE2";
    public static final String EXTRA_MESSAGE_BUTTON_ID = "andrew.zach.eyeTableP1.MESSAGE3";
    Button buttonRefFromEyeTable;

    Boolean itemWasPressedInPopUpPage=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //(1) start: Setting up basic layout for popupwindow
        setContentView(R.layout.activity_pop);

        //*****GET EXTRA_MESSAGE CODE FROM 'EyeTableActivity.java'**********
        final Intent intent = getIntent();
        final Integer idFromEyeTable = intent.getIntExtra(EXTRA_MESSAGE_ID_OF_BUTTON,0);
        final int tempButtonIDStorage=intent.getIntExtra(EXTRA_MESSAGE_ID_OF_BUTTON,0);
        System.out.println("*$**************Value of 'nameOfValueReceivedFromPOP':" + idFromEyeTable+ "*******************");


        mListView= (ListView)findViewById(R.id.listView_popUp);//a pop up listView



        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));
        //:(1) end

        //(2) start: Setting up Firebase list from which list of names can be selected when clicked
        FirebaseDatabase database= FirebaseDatabase.getInstance();
        DatabaseReference dbLecturesRef=database.getReference().child("Leagues");
        DatabaseReference dbClubsRef=database.getReference().child("Club");
        final DatabaseReference DBrefToButtons=database.getReference().child("Buttons/"+idFromEyeTable);

        dbLecturesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String [] data=new String[]{};
                    ArrayList<String> dataList = new ArrayList<String>();
                    dataList.addAll( Arrays.asList(data) );

                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey();
                        System.out.println("***************************Name of  current Lecture:" + name + "********************************");

                        //List <String>tempListOfModules=listOfModules;
                        listOfModules.add(name);
                        //tempListOfModules.toArray();
                        System.out.println("***************************contents of 'tempListOfModules':" + listOfModules.toString() + "********************************");
                        dataList.add(name);
                        // Create ArrayAdapter using the planet list.
                        listAdapter = new ArrayAdapter<String>(Pop.this, R.layout.row,R.id.textView_popUpTextBox, dataList);

                    }
                    mListView.setAdapter(listAdapter);

                    //***Trying to implement clickable list start:
                    //Trying to implement clickable list START
                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {

                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
                        {

                            int x=position;
                                Object leagueObjWithName = arg0.getItemAtPosition(x);
                            //create if statement to handle whether 'LeagueClass'/'ClubClass' in pressed

                            if(leagueObjWithName.getClass().equals(LectureClass.class)) {
                                LectureClass myObj = (LectureClass) leagueObjWithName;
                                String leagueName = myObj.getLeagueName(); //get leaguename for ViewLeagueActivity page
                                System.out.println("*************VALUE OF LEAGUENAME (after myObj.getLeagueName()is called):"+leagueName+"************");
                                //****
                                //String titleForLeaguesActivity = title.getText().toString();

                                //set lecture name to the item button from pop up


                                System.out.println("*************(in first if)CONTENTS OF 'leagueName' is:" + leagueName + "**********************************");

                                Intent intent = new Intent(Pop.this, LecturesActivity.class);
                               // ---------------TEMP REMOVAL--------------------- intent.putExtra(EXTRA_MESSAGE2, leagueName);
                                //intent.putExtra("title", );
                                startActivity(intent);
                            }
                            else if (leagueObjWithName.getClass().equals(ClubClass.class))// if it is of type 'ClubClass'...
                            {
                                ClubClass myObj = (ClubClass) leagueObjWithName;
                                String leagueName = myObj.getClubName(); //get for ViewLeagueActivity page
                                //****
                                //String titleForLeaguesActivity = title.getText().toString();

                                System.out.println("*************(in 2nd if)CONTENTS OF 'leagueName' is:" + leagueName + "**********************************");

//                                Intent intent = new Intent(LecturesActivity.this, ViewClubs.class);
//                                intent.putExtra(EXTRA_MESSAGE, leagueName);
//                                intent.putExtra("title", titleForLeaguesActivity);
//                                startActivity(intent);

                            }
                            else if(leagueObjWithName.getClass().equals(String.class)){
                                String myObj=(String) leagueObjWithName;

                                System.out.println("*************(in 3rd if)CONTENTS OF 'leagueName' is:" + myObj + "**********************************");

                                Intent intent2=getIntent(); //get intent contents from previous page

//                                int tempButtonIDStorage=intent2.getIntExtra("EXTRA_MESSAGE_ID_OF_BUTTON",0);
                                System.out.println("&&&&&&&&&&&&&&& Value of 'EXTRAMESSAGE_BUTTON_ID'(tempButtonIDStorage):"+tempButtonIDStorage+"&&&&&&&&&&&&&&&&&&&&&");

                                Intent intent = new Intent(Pop.this, EyeTableActivity.class);
                                //start: trying to get button name, then set it to Table button name

                                //^^^^^^^^^^^^^^^^^TRYING TO SET myObj TO DB ^^^^^^^^^^^^^^^^^^
                                DBrefToButtons.setValue(myObj); //set value of new node 'Buttons' to store 'myObj' (which is the button name change)

                                //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
                               /* obsolete code:
                                intent.putExtra(EXTRA_MESSAGE2, myObj);
                                intent.putExtra(EXTRA_MESSAGE_BUTTON_ID,idFromEyeTable);
                                System.out.println("********value of 'myObj'(before starting new activity):"+myObj);
                                */


                              //#f  //%%%%%%%%%%%%%%%%%%%%%%TRYING TO SET BUTTON ON EYETABLE VIEW TEXT FROM HERE%%%%%%%%%%%%%%%%%%%%%%%%
                               // Button toChangeButtonOnEyeTable=(Button) findViewById(tempButtonIDStorage);
                               // toChangeButtonOnEyeTable.setText(myObj);
                                //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

                                //:end
                                //intent.putExtra("title", );
                                //*************OBESELETE: REMOVING startIntent function **************
                               // startActivity(intent);

                                finish();


                                 //#f ************ TRIED LAYOUT INFLATER METHOD TO CHANGE BUTTON NAME IN EYETABLE *************
//                                LayoutInflater inflater = (LayoutInflater)Pop.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                                View vi = inflater.inflate(R.layout.content_eye_table, null); //log.xml is your file.
//                                //use temp button name to create a generic button ref
//                                    buttonRefFromEyeTable = (Button) vi.findViewById(tempButtonNameStorage);
//                                System.out.println("*   *    *   *  'tempButtonNameStorage':" + tempButtonNameStorage + "*   *    *   * ");
//                                //set button ref to received name
//                                if(buttonRefFromEyeTable!=null) {
//                                    System.out.println("------------------BEFORE trying to get button name, name is:" + buttonRefFromEyeTable.getText().toString() + "-----------------");
//                                    buttonRefFromEyeTable.setText(leagueObjWithName.toString());
//                                    System.out.println("------------------AFTER trying to get button name, name is:" + buttonRefFromEyeTable.getText().toString() + "-----------------");
//                                }
//                                //if(buttonRefFromEyeTable!=null) {
//                                //    buttonRefFromEyeTable.setText(leagueObjWithName.toString());
//                               // }

                            }

                            else {
                                //debug check
                                System.out.println("$$$$$$$$$$$$$$ NOTHING IS HAPPENING ON ITEM LIST CLICK $$$$$$$$$$$$$$$");
                            }
                            //trying to implement close on itemPressed function
//                            itemWasPressedInPopUpPage=true;
//                            runItemWasPressedCheck();
                        }


                    });
                    //***:end

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






                        //dataSnapshot.getValue(LectureClass.class); //get lecture class obj from db
               // LectureClass temp=dataSnapshot.getValue(LectureClass.class);

                //String temp2=dataSnapshot.getKey().toString(); //testing to get names of children in this branch
                    //start:*testing*
                   // System.out.println("***************************Name of  current Lecture:"+temp.getLeagueCode() +"********************************");
                   // System.out.println("*************************Value of DataSnapshot:"+dataSnapshot.getValue(LectureClass.class).getLeagueCode()+"****************");
                   // System.out.println("*************************Value of temp2 (Trying to pull names of children down):"+temp2+"****************");
                    //:end

                }//outside onCreate
//    public void runItemWasPressedCheck(){
//        if(itemWasPressedInPopUpPage.equals(true)){
//
//        }
//    }



}
