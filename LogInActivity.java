package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "Login";

    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mloginBtn;
    private Button mAlt_btnDataTest;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    GenericTypeIndicator <List<String>> t= new GenericTypeIndicator<List<String>>() {};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();


        mEmailField = (EditText) findViewById(R.id.editText6);
        mPasswordField = (EditText) findViewById(R.id.editText7);
        mloginBtn = (Button) findViewById(R.id.button2);
        mAlt_btnDataTest=(Button) findViewById(R.id.button_altPage);


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(LogInActivity.this, cellTables.class));
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        mloginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = mEmailField.getText().toString().trim();
                final String password = mPasswordField.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }



                //create user
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(LogInActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(LogInActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    boolean emailVerified = user.isEmailVerified();
                                    if(emailVerified) {
                                        //#########   startActivity(new Intent(LogInActivity.this, LecturesActivity.class));
                                        //##########   finish();
                                        //   finishAffinity();

                                        //****MAKE USER ID ACCESSIBLE start*****
                                        FirebaseDatabase database=FirebaseDatabase.getInstance().getReference().getDatabase();
                                        final DatabaseReference refUserIDaccess=database.getReference().child("UserIDs");

                                        refUserIDaccess.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    List temp=dataSnapshot.getValue(t); //pull down id list

                                                    if(!temp.contains(user.getUid())) //if id list does not contain this id
                                                    {
                                                        temp.add(user.getUid()); //add to list
                                                        refUserIDaccess.setValue(temp);//set the modified list to database
                                                    }

                                                }
                                                else  //set up the 'UserIDs' list initially to firebase
                                                {
                                                    List<String> temp2=new ArrayList<String>();
                                                    temp2.add(user.getUid());
                                                    refUserIDaccess.setValue(temp2);


                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        //****MAKE USER ID ACCESSIBLE end*****


                                        Intent intent = new Intent(LogInActivity.this, LecturesActivity.class);
                                        //***
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                                        //***
                                        //**************TO PREVENT OTHER ACTIVITIES FROM OVERLAPPING ON LOGOUT**********
                                        //   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //******************************************************************************

                                        startActivity(intent);
                                    }
                                    else {
                                        Toast.makeText(LogInActivity.this, "Email not verified!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //*DATA test page* (to check to see if Firebase connection/wifi is working)
        mAlt_btnDataTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,cellTables.class);
                startActivity(intent);
            }
        });

    }
    //*Outside of onCreate *

   /* public void leagues(View view) {
        Toast.makeText(getApplicationContext(), "Log in was successful!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ******MY_CLASS_AFTER_LOGIN).class);
        startActivity(intent);
    }*/

    public void passwordReset(View view) {
        Intent intent = new Intent(this, PasswordResetActivity.class);
        startActivity(intent);
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
