package com.example.olivier.businessapp.Objects.Activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivier.businessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;

//https://www.sitepoint.com/learn-android-nfc-basics-building-a-simple-messenger/
public class TinnectActivity extends AppCompatActivity implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference storageReference;
    private Button send;
    private Button recieve;
    private TextView message;
    private NfcAdapter mNfcAdapter;
    private NdefMessage mNdefMessage;
    String words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinnect);
        message=(TextView)findViewById(R.id.message);
        message.setText("waiting");
        send = (Button) findViewById(R.id.send);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null) {
            byte[] payload = mFirebaseUser.getUid().getBytes(Charset.forName("UTF-8"));
            NdefRecord record = NdefRecord.createMime("text/plain",payload);
            mNdefMessage= new NdefMessage(record);
            mNfcAdapter.setNdefPushMessage(mNdefMessage, this);
            mNfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }
        else {
            Toast.makeText(this, "NFC not available on this device",
                    Toast.LENGTH_SHORT).show();
        }
        handleNfcIntent(getIntent());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send();
            }
        });
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        message.setText("complete");
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        byte[] payload = mFirebaseUser.getUid().getBytes(Charset.forName("UTF-8"));
        NdefRecord record = NdefRecord.createMime("text/plain",payload);
        NdefRecord.createApplicationRecord(getPackageName());
        return new NdefMessage(record);
    }

    private void handleNfcIntent(Intent NfcIntent) {
        if ((NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction()))) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(receivedArray != null) {

                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    //if (string.equals(getPackageName())) { continue; }
                    message.setText("sent");

                }
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }
     public void send()
     {
         mNfcAdapter.setNdefPushMessage(mNdefMessage, this,this);
     }

    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
       // updateTextViews();
        if (mNfcAdapter != null)
           mNfcAdapter.setNdefPushMessage(mNdefMessage, this);
            handleNfcIntent(getIntent());
    }

}


