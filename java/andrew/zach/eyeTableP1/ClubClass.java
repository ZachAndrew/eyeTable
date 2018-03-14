package andrew.zach.eyeTableP1;

/**
 * Created by Zach on 05/12/2017.
 */

import java.util.ArrayList;

import android.app.Activity;
import android.support.annotation.Keep;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zach on 16/02/2017.
 */



public class ClubClass {

   // public String credits; //entry fee for each player to join
    public String clubName; //to store user defined string name
    public String president;//the owner of the league
    public String description;//a description of what the club does/is
    public String activityType;
    public int numStudents; //to define the number of players allowed in the league
    public List<String> participants=new ArrayList<String>();
    public List <String> displayNames=new ArrayList<>();
    //public String participants;
    public String clubTime; //the time in which the club/soc will be in session
    public String typeOfLeague; //Premier League, La Liga...etc
    public boolean activityAlive=true;


    //default constructor takes in name of league, president and number of players
    public ClubClass(){

        this.clubName="*NO LEAGUE ENTERED*";
        //this.credits="None Entered";
        this.president="No President Entered";
        this.numStudents=0;
        //this.typeOfLeague="No type";
        this.activityAlive=true;
        this.activityType="sport";
        this.clubTime="*NOT SET YET*";
    }

    public ClubClass(String clubName){
        this.clubName=clubName;

    }

    //alt constructor

    public ClubClass(String clubName, String president, String description, String activityType, String ClubTime)
    {
        this.clubName=clubName;
        this.president=president;
        this.numStudents=numStudents;
        this.description=description;
        this.activityType=activityType;
       // this.credits=credits;
        // this.participants=participants;
        // this.displayNames=displayNames;

        this.clubTime=ClubTime; //set the input of ClubTime from java project main class into clubtime

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
    public String getClubName(){
        return this.clubName;
    }

    public String getPresident(){
        return this.president;
    }

    public String getDescription() { return this.description;}

    public String getActivityType(){return this.activityType;}


   /*public int getNumPlayers()
    {
        return this.numStudents;
    }*/


    public List<String> getParticipants()
    {
        return participants;
    }

    public List<String>getDisplayNames(){return displayNames;}


   /* public String getParticipants(){
        return this.participants;
    }*/

   /* public String getTypeOfLeague()
    {
        return typeOfLeague;
    }*/

    public boolean getClubAlive(){
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


    public void setParticipants(){this.participants.clear(); this.participants.add("*EMPTY*");}

    public void setDisplayNames(){this.displayNames.clear(); this.displayNames.add("*EMPTY*");}





    // public void setLeagueType(String typeOfLeague){

}


//...




