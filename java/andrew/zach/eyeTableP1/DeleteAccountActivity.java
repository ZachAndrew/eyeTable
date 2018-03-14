package andrew.zach.eyeTableP1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DeleteAccountActivity extends AppCompatActivity {

    private static final String TAG = "DeleteAccount";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mConfirmPasswordField;
    private Button mdeleteAccountBtn;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar5);
        mProgressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.firstNameField);
        mConfirmPasswordField = (EditText) findViewById(R.id.passwordField);
        mdeleteAccountBtn = (Button) findViewById(R.id.deleteAccount);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(DeleteAccountActivity.this, WelcomePageActivity.class);
                    startActivity(intent);
                }
            }
        };

        mdeleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mdeleteAccountBtn.setEnabled(false);
                hideKeyboard();
                mProgressBar.setVisibility(View.VISIBLE);

                final String email = mEmailField.getText().toString().trim();
                final String password = mPasswordField.getText().toString().trim();
                final String confirmPassword = mConfirmPasswordField.getText().toString().trim();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (TextUtils.isEmpty(email)) {
                    mProgressBar.setVisibility(View.GONE);
                    mdeleteAccountBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mProgressBar.setVisibility(View.GONE);
                    mdeleteAccountBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    mdeleteAccountBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Confirm password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    mdeleteAccountBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(DeleteAccountActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to delete your account?");
                builder.setMessage("You will not be able to retrieve your account once it has been deleted.");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User re-authenticated.");

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference userInfoRef = database.getReference("Users");
                                            userInfoRef.child(user.getUid()).removeValue();

                                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(TAG, "User account deleted.");
                                                        mEmailField.setText("");
                                                        mPasswordField.setText("");
                                                        mConfirmPasswordField.setText("");
                                                        mProgressBar.setVisibility(View.GONE);
                                                        Toast.makeText(DeleteAccountActivity.this, "Account has been deleted.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        mProgressBar.setVisibility(View.GONE);
                                                        mdeleteAccountBtn.setEnabled(true);
                                                        Toast.makeText(DeleteAccountActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            mProgressBar.setVisibility(View.GONE);
                                            mdeleteAccountBtn.setEnabled(true);
                                            Toast.makeText(DeleteAccountActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void hideKeyboard() {
        try  {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}