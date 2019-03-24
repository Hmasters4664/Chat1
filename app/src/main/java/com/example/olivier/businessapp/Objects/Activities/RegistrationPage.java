package com.example.olivier.businessapp.Objects.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.olivier.businessapp.MainActivity;
import com.example.olivier.businessapp.Objects.ChatS;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends AppCompatActivity {
    String name,gender,age, professionalf, JobPosition,email,password,username;
    Spinner spinner;
    EditText mName,mAge,mGender,mProf,mJob;
    ArrayAdapter<CharSequence> adapter ;
    BubblePicker picker;
    String [] likes={"","","","",""};
    int count=0;
    private static final String TAG="FireLog";
    private FirebaseFirestore mFire = FirebaseFirestore.getInstance();
    private Button mSignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_registration_page);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mSignUp = findViewById(R.id.btn_register);
        mName = findViewById(R.id.input_name);
        mAge = findViewById(R.id.input_age);
        mGender = findViewById(R.id.input_gender);
        mProf = findViewById(R.id.input_profession);
        mJob = findViewById(R.id.input_Job);
        picker = findViewById(R.id.picker);

        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            email = extras.getString("email");
            password = extras.getString("password");
            username = extras.getString("username");
        }
        /////////////////////sign up button/////////////////////////////////////////////////////////
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mName.getText().toString().trim();
                age= mAge.getText().toString().trim();
                professionalf = mProf.getText().toString().trim();
                JobPosition =mJob.getText().toString().trim();
                gender = mGender.getText().toString().trim();
                if ((!name.isEmpty()) && (!age.isEmpty()) && (!professionalf.isEmpty()) && (!JobPosition.isEmpty()) && (!gender.isEmpty()) && (count>1)) {
                    signUP();
                } else {
                    Toast.makeText(RegistrationPage.this, "You must fill in " +
                            "all the required information", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////

        ////////////////////////////Bublle Adapter/////////////////////////////////////////////////////////
        final String[] titles = getResources().getStringArray(R.array.countries);
        final TypedArray colors = getResources().obtainTypedArray(R.array.color);
        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(RegistrationPage.this, android.R.color.white));
                return item;
            }
        });
            ///////////////////Buble Listerner/////////////////////////////////////////////////
        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                if(count<5)
                {
                    likes[count] = item.getTitle();
                    count++;
                }
                else{
                    Toast.makeText(RegistrationPage.this, "Maximum selections reached", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {
                int p;
                if(count>0)
                {
                    for(p=0;p<5;p++) {
                        if (likes[p] == item.getTitle())
                        {
                            likes[p]="";
                        }
                    }
                    count--;
                }

            }
        });

        //////////////////////////////////////////////////////////////////////////////
    }

    /* @Override
     public void onItemSelected(AdapterView<?> parent, View view,
                                    int pos, long id) {
             // An item was selected. You can retrieve the selected item using
             // parent.getItemAtPosition(pos)
         gender=Genders[pos];
         }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }*/
    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }

    public void signUP()
    {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            final FirebaseUser newUser = task.getResult().getUser();
                            //success creating user, now set display name as name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            newUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // mDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Log.d(SignIn.class.getName(), "User profile updated.");
                                                /***CREATE USER IN FIREBASE DB AND REDIRECT ON SUCCESS**/
                                                createUserInDb(newUser.getUid(), newUser.getDisplayName(), newUser.getEmail());

                                            }else{
                                                //error
                                                Toast.makeText(RegistrationPage.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            Intent i = new Intent(getBaseContext(), MainActivity.class );
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationPage.this, "Authentication failed please " +
                                    "try again after a few seconds.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
    }

    private void createUserInDb(String userId, String displayName, String email){
        //mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, displayName, email);
        Map<String, Object> userz = new HashMap<>();
        userz.put("UserID",userId );
        userz.put("DisplayName", name);
        userz.put("email", email);
        userz.put("age",age);
        userz.put("profession",professionalf);
        userz.put("JobPosition",JobPosition);
        userz.put("Interest1",likes[0]);
        userz.put("Interest2",likes[1]);
        userz.put("Interest3",likes[2]);
        userz.put("Interest4",likes[3]);
        userz.put("Interest5",likes[4]);
        userz.put("profile","x.jpg");

        //mDatabase.child("users").child(userId).child("username").setValue(name);
        //mDatabase.child("users").child(userId).child("GID").setValue(userId);

        mFire.collection("Users").document(userId)
                .set(userz)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        String roomID = userId + "_" + userId;
        //ChatS room1= new ChatS(name,name,roomID);

        Map<String, Object> chatz = new HashMap<>();
        chatz.put("user1", name);
        chatz.put("user2", name);
        chatz.put("ChatID", roomID);

        mFire.collection("Chats").document(userId).collection("OneToOne").document(roomID)
                .set(chatz)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }
}
