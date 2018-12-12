package com.example.olivier.businessapp.Objects.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivier.businessapp.MainActivity;
import com.example.olivier.businessapp.Objects.User;
import com.example.olivier.businessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogIn;
    private TextView mLink;
    private FirebaseAuth mAuth;
    private static final String TAG="ERROR";
    private EditText mName;
    private FirebaseFirestore mFire;
    String email;
    String password;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail= (EditText)findViewById(R.id.input_email);
        mPassword= (EditText)findViewById(R.id.input_password);
        mLogIn = (Button)findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        mFire= FirebaseFirestore.getInstance();
        mLink= (TextView)findViewById(R.id.link_signup);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();
                if (!email.isEmpty() && (!password.isEmpty())) {
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "You must provide a " +
                            "username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SignIn.class);
                startActivity(i);
            }
        });
    }

    public void login(String email, String password){

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            progressDialog.dismiss();
                            Intent i = new Intent(getBaseContext(), MainActivity.class );
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication " +
                                    "failed check username or password.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }


}