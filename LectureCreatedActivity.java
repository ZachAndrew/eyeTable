package andrew.zach.eyeTableP1;

        import android.content.ClipboardManager;
        import android.content.Context;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

        import java.util.UUID;

public class LectureCreatedActivity extends AppCompatActivity {
    String admin,UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_league_created);

    /*    //*Get User ID* START
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            admin=user.getEmail();
            // Username="'"+user.getEmail()+"'";
            UserID=user.getUid();
        } else {
            // No user is signed in
        }

        //*Get User ID* END*/



        TextView leagueCode = (TextView) findViewById(R.id.leagueCode);
        leagueCode.setText(UUID.randomUUID().toString());


        //get leaguename from 'CreateLeagues' page
        Intent intent=getIntent();
        final String leagueName=intent.getStringExtra(CreateLeagueActivity.EXTRA_MESSAGE);
        System.out.println("****************CONTENT OF 'leagueName' is:"+leagueName+"************************");


        //now set leagueCode to the league in the correct database path
        DatabaseReference database= FirebaseDatabase.getInstance().getReference();
        DatabaseReference addCodeToLeagueRef=database.getDatabase().getReferenceFromUrl("https://eyetabletestdb.firebaseio.com//Leagues/"+leagueName);
        addCodeToLeagueRef.child("lectureCode").setValue(leagueCode.getText().toString());

    }

    public void copyCode(View view) {
        TextView leagueCodeField = (TextView) findViewById(R.id.leagueCode);
        String leagueCode = leagueCodeField.getText().toString().trim();
        ClipboardManager _clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(leagueCode, leagueCode);
        _clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Code has been copied to clipboard!", Toast.LENGTH_SHORT).show();
    }
    public void leagues(View view) {
        Intent intent = new Intent(this, LecturesActivity.class);
        startActivity(intent);
    }
}
