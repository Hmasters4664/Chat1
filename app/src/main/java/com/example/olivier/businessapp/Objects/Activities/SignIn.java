package com.example.olivier.businessapp.Objects.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.olivier.businessapp.R;

public class SignIn extends AppCompatActivity {

    private EditText mName;
    private EditText mPassword;
    private Button mSignIn;
    private EditText mEmail;
    String uname;
    String password;
    String email;
    //String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mName=(EditText)findViewById(R.id.input_name);
        mEmail=(EditText)findViewById(R.id.input_email);
          mPassword= (EditText)findViewById(R.id.input_password);
          mSignIn= (Button)findViewById(R.id.btn_signup);

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname=mName.getText().toString().trim();
                password= mPassword.getText().toString().trim();
                email= mEmail.getText().toString().trim();
                if (!uname.isEmpty() && (!password.isEmpty()) && (!email.isEmpty())) {
                    Intent i = new Intent(getBaseContext(),RegistrationPage.class);
                    i.putExtra("email",email);
                    i.putExtra("password",password);
                    i.putExtra("username",uname);

                    startActivity(i);
                } else {
                    Toast.makeText(SignIn.this, "You must provide a username and " +
                            "password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
