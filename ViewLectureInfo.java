package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static andrew.zach.eyeTableP1.LecturesActivity.EXTRA_MESSAGE;

/**
 * Created by Zach on 08/11/2017.
 */

public class ViewLectureInfo extends AppCompatActivity {



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
        setContentView(R.layout.activity_view_lecture);

        String UserID="Default",lect_name="Default";

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

/*
        final TextView deadlineField = (TextView) findViewById(R.id.deadlineField);
        final TextView selectedTeamView = (TextView) findViewById(R.id.selectedTeamField);
        final ImageView teamBadgeView = (ImageView) findViewById(R.id.teamBadgeView);*/
        final TextView lectNameViewed=(TextView)findViewById(R.id.textView_view_lecture_lectName);


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
        System.out.println("\n\n\n*****************Lecture Name:"+lect_name+"**********************\n\n\n");

        lectNameViewed.setText(lect_name);


    }
}

