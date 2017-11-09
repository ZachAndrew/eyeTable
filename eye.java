package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Zach on 01/10/2017.
 */

public class eye extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState1){
        super.onCreate(savedInstanceState1);
        setContentView(R.layout.activity_eye);

    }

    public void proceed(View view){
        Intent intent= new Intent(this , LogInActivity.class);
        startActivity(intent);
    }

}
