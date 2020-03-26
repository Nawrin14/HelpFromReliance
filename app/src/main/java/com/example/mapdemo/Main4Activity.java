package com.example.mapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

public class Main4Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private Button confirmFirst, send;
    private ImageView profile, logOut;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private boolean isPermission;
    private String msgAddress1, msgAddress2, userCurrentAddress, userHomeAddress, userAddress, sentMsg, UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userCurrentAddress = null;

        confirmFirst = (Button) findViewById(R.id.confirmFirstButton);
        send = (Button) findViewById(R.id.sendMsgButton);
        profile = (ImageView) findViewById(R.id.currentProfile);
        logOut = (ImageView) findViewById(R.id.logout);

        reference = database.getReference();
        user = auth.getCurrentUser();
        UID = user.getUid();

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null){
            String currentHome = bundle.getString("address");
            userCurrentAddress = currentHome;
        }

        reference.child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userHomeAddress = String.valueOf(dataSnapshot.child("address").getValue());
                msgAddress1 = String.valueOf(dataSnapshot.child("number1").getValue());
                msgAddress2 = String.valueOf(dataSnapshot.child("number2").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main4Activity.this,"SMS send failed", Toast.LENGTH_SHORT).show();
            }
        });

        send.setOnClickListener(this);
        confirmFirst.setOnClickListener(this);
        profile.setOnClickListener(this);
        logOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==send){
            if (ContextCompat.checkSelfPermission(Main4Activity.this,Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Main4Activity.this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            } else if(userCurrentAddress != null) {
                userAddress = userCurrentAddress;
                sentMsg = "I need your help. My current location is " + userAddress;
                sendMessage();
            }

            else{
                userAddress = userHomeAddress;
                sentMsg = "I need your help. My current location is " + userAddress;
                sendMessage();
            }

        }

        else if(v==confirmFirst){
            Intent newIntent = new Intent(Main4Activity.this,Main6Activity.class);
            startActivity(newIntent);
        }
        else if(v==profile){
            Intent newIntent = new Intent(Main4Activity.this,Main7Activity.class);
            startActivity(newIntent);
        }

        else{
            Toast.makeText(Main4Activity.this,"Log Out Successful",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent newIntent = new Intent(Main4Activity.this,Main3Activity.class);
            startActivity(newIntent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                } else {
                    Toast.makeText(this,"SMS send failed",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void sendMessage() {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(msgAddress1, null, sentMsg, null, null);
        smsManager.sendTextMessage(msgAddress2, null, sentMsg, null, null);
        Toast.makeText(getApplicationContext(), "SMS sent.",
                Toast.LENGTH_SHORT).show();
    }

}