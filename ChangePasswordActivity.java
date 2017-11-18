package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.support.annotation.NonNull;
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

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePassword";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText mOldPasswordField;
    private EditText mNewPasswordField;
    private EditText mConfirmPasswordField;
    private Button msaveChangesBtn;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar4);
        mProgressBar.setVisibility(View.GONE);
        setProgressBarIndeterminateVisibility(true);

        mAuth = FirebaseAuth.getInstance();

        mOldPasswordField = (EditText) findViewById(R.id.oldPasswordField);
        mNewPasswordField = (EditText) findViewById(R.id.newPasswordField);
        mConfirmPasswordField = (EditText) findViewById(R.id.passwordField);
        msaveChangesBtn = (Button) findViewById(R.id.saveChanges);

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
                    Intent intent = new Intent(ChangePasswordActivity.this, EmailVerificationActivity.class);
                    startActivity(intent);
                }
            }
        };

        msaveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                msaveChangesBtn.setEnabled(false);
                hideKeyboard();
                mProgressBar.setVisibility(View.VISIBLE);

                final String oldPassword = mOldPasswordField.getText().toString().trim();
                final String newPassword = mNewPasswordField.getText().toString().trim();
                final String confirmPassword = mConfirmPasswordField.getText().toString().trim();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (TextUtils.isEmpty(oldPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    msaveChangesBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter old password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    msaveChangesBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Enter new password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    msaveChangesBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Confirm new password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length() < 6) {
                    mProgressBar.setVisibility(View.GONE);
                    msaveChangesBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    mProgressBar.setVisibility(View.GONE);
                    msaveChangesBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User re-authenticated.");
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User password updated.");
                                                mOldPasswordField.setText("");
                                                mNewPasswordField.setText("");
                                                mConfirmPasswordField.setText("");
                                                mProgressBar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(), "Change password was successful!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(ChangePasswordActivity.this, ProfileActivity.class));
                                                msaveChangesBtn.setEnabled(true);
                                            } else {
                                                Log.d(TAG, "User password was not updated.");
                                                mProgressBar.setVisibility(View.GONE);
                                                msaveChangesBtn.setEnabled(true);
                                                Toast.makeText(getApplicationContext(), "Failed to change password!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            mProgressBar.setVisibility(View.GONE);
                            msaveChangesBtn.setEnabled(true);
                            Toast.makeText(ChangePasswordActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
    public void onStart() {
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