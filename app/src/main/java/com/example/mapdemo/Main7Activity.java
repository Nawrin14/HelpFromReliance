package com.example.mapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main7Activity extends AppCompatActivity {

    private TextView profileName, profileEmail, profileAddress;
    String UID;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        this.setTitle("Profile");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        user = auth.getCurrentUser();
        UID = user.getUid();

        profileName = (TextView) findViewById(R.id.currentName);
        profileEmail = (TextView) findViewById(R.id.currentEmail);
        profileAddress = (TextView) findViewById(R.id.curretAddress);

        reference.child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String username = String.valueOf(dataSnapshot.child("fullname").getValue());
                String useremail = String.valueOf(dataSnapshot.child("email").getValue());
                String userHomeAddress = String.valueOf(dataSnapshot.child("address").getValue());

                profileName.setText(username);
                profileEmail.setText(useremail);
                profileAddress.setText(userHomeAddress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
