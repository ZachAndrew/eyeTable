package andrew.zach.eyeTableP1;

import android.app.Activity;
import android.support.annotation.Keep;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zach on 16/02/2017.
 */



public class LectureClass {

    public String credits; //entry fee for each player to join
    public String lectureName; //to store user defined string name
    public String admin;//the owner of the league
    public int numStudents; //to define the number of players allowed in the league
    public List <String>participants=new ArrayList<String>();
    public List <String> displayNames=new ArrayList<>();
    //public String participants;
    public String typeOfLeague; //Premier League, La Liga...etc
    public boolean activityAlive=true;

    public String leagueCode;


    //default constructor takes in name of league, admin and number of players
    public LectureClass(){

        this.lectureName="*NO LEAGUE ENTERED*";
        this.credits="None Entered";
        this.admin="No Admin Entered";
        this.numStudents=0;
        //this.typeOfLeague="No type";
        this.activityAlive=true;
        this.leagueCode="*No Code Created Yet*";
    }

    public LectureClass(String lectureName){
        this.lectureName=lectureName;

    }

    //alt constructor

    public LectureClass(String lectureName, String admin, int numStudents, String credits, String lectureCode, List <String>participants, List <String>displayNames)
     {
        this.lectureName=lectureName;
        this.admin=admin;
        this.numStudents=numStudents;
        this.credits=credits;
       // this.participants=participants;
       // this.displayNames=displayNames;

         //***clear lists***
       //  displayNames.clear();
       //  participants.clear();
         //*****************


        participants.add(numStudents,"*EMPTY*");
        displayNames.add(numStudents,"*EMPTY*");
        //participants="*EMPTY*";
        //this.typeOfLeague=typeOfLeague;
        this.activityAlive=true;
       // this.leagueCode="EMPTY";



    }

    //Getters
    public String getLeagueName(){
        return this.lectureName;
    }

    public String getAdmin(){
        return this.admin;
    }


   /*public int getNumPlayers()
    {
        return this.numStudents;
    }*/

    public String getCredits(){
        return this.credits;
    }

    public List<String> getParticipants()
    {
        return participants;
    }

    public String getLeagueCode(){return  this.leagueCode;}

    public List<String>getDisplayNames(){return displayNames;}


   /* public String getParticipants(){
        return this.participants;
    }*/

   /* public String getTypeOfLeague()
    {
        return typeOfLeague;
    }*/

    public boolean getLeagueAlive(){
        return this.activityAlive;
    }

    //Setters

    public void addToParticipants(String userToAdd){
        participants.add(numStudents,userToAdd);
        numStudents++;

    }

   /* public void setTypeOfLeague(String typeOfLeague){
        this.typeOfLeague=typeOfLeague;

    }*/

    public void setCredits(String credits){
        this.credits=credits;
    }

    public void setParticipants(){this.participants.clear(); this.participants.add("*EMPTY*");}

    public void setDisplayNames(){this.displayNames.clear(); this.displayNames.add("*EMPTY*");}





    // public void setLeagueType(String typeOfLeague){

}


    //...



