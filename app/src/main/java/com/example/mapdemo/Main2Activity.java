package com.example.mapdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private EditText mail, pass, fullName, number1, number2, homeAddress;
    private Button register;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        this.setTitle("Sign Up");

        mail = (EditText) findViewById(R.id.setEmail);
        pass = (EditText) findViewById(R.id.setPassword);
        register = (Button) findViewById(R.id.registerButton);
        fullName = (EditText) findViewById(R.id.setName);
        number1 = (EditText) findViewById(R.id.emergencyContact1);
        number2 = (EditText) findViewById(R.id.emergencyContact2);
        homeAddress = (EditText) findViewById(R.id.setHomeAddress);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        final String name = fullName.getText().toString();
        final String email = mail.getText().toString();
        final String contact1 = number1.getText().toString();
        final String contact2 = number2.getText().toString();
        final String area = homeAddress.getText().toString();
        final String password = pass.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) || TextUtils.isEmpty(contact1) || TextUtils.isEmpty(contact2) || TextUtils.isEmpty(area)) {
            Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            credentials info = new credentials(name, email, contact1, contact2, area, password);

                            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    Intent intent = new Intent(Main2Activity.this, Main4Activity.class);
                                    startActivity(intent);
                                    finish();
                                    Toast.makeText(Main2Activity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            Toast.makeText(Main2Activity.this, "Sign Up failed!  Please try again later", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(Main2Activity.this, Main3Activity.class);
        startActivity(newIntent);
        finish();
    }
}
