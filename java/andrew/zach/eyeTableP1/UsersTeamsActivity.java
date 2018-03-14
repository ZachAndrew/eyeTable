package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UsersTeamsActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "andrew.zach.eyeTableP1.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_teams);

        Intent intent = getIntent();
        String users_name = intent.getStringExtra(EXTRA_MESSAGE);

        TextView usersName = (TextView) findViewById(R.id.usersName);
        usersName.setText(users_name);
    }
}
