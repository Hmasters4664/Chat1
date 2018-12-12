package com.example.olivier.businessapp.Objects.Activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.olivier.businessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.Charset;

//https://www.sitepoint.com/learn-android-nfc-basics-building-a-simple-messenger/
public class TinnectActivity extends Base implements NfcAdapter.OnNdefPushCompleteCallback,
        NfcAdapter.CreateNdefMessageCallback {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference storageReference;
    private Button request;
    private Button recieve;
    private TextView message;
    private NfcAdapter mNfcAdapter;
    String words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinnect);
        message=(TextView)findViewById(R.id.message);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null) {
            //Handle some NFC initialization here
        }
        else {
            Toast.makeText(this, "NFC not available on this device",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNdefPushComplete(NfcEvent event) {
        //This is called when the system detects that our NdefMessage was
        //Successfully sent.
        //messagesToSendArray.clear();
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        //This will be called when another NFC capable device is detected.
       /* if (messagesToSendArray.size() == 0) {
            return null;
        }*/
        //We'll write the createRecords() method in just a moment
        NdefRecord[] recordsToAttach = createRecords();
        //When creating an NdefMessage we need to provide an NdefRecord[]
        return new NdefMessage(recordsToAttach);
    }

    public NdefRecord[] createRecords() {

        NdefRecord[] records = new NdefRecord[1];

       // for (int i = 0; i < messagesToSendArray.size(); i++){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            byte[] payload = mFirebaseUser.getUid().getBytes(Charset.forName("UTF-8"));

            NdefRecord record = new NdefRecord(
                    NdefRecord.TNF_WELL_KNOWN,  //Our 3-bit Type name format
                    NdefRecord.RTD_TEXT,        //Description of our payload
                    new byte[0],                //The optional id for our Record
                    payload);                   //Our payload for the Record

            records[0] = record;
        }else {

                byte[] payload = mFirebaseUser.getUid().getBytes(Charset.forName("UTF-8"));

                NdefRecord record = NdefRecord.createMime("text/plain",payload);
                records[0] = record;

        }
        records[0] = NdefRecord.createApplicationRecord(getPackageName());
        return records;

    }


    private void handleNfcIntent(Intent NfcIntent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcIntent.getAction())) {
            Parcelable[] receivedArray =
                    NfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if(receivedArray != null) {

                NdefMessage receivedMessage = (NdefMessage) receivedArray[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (NdefRecord record:attachedRecords) {
                    String string = new String(record.getPayload());
                    //Make sure we don't pass along our AAR (Android Application Record)
                    if (string.equals(getPackageName())) { continue; }
                    message.setText(string);

                }
            }
            else {
                Toast.makeText(this, "Received Blank Parcel", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }


}



