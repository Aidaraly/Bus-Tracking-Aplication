package com.example.blta_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
     TextView registerBTn, logInBTN, forgotBtn;
     EditText userEmailET, userPasswordET;
     FirebaseAuth firebaseAuth;
     AlertDialog.Builder reset_alert;
     LayoutInflater inflater;
     FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        forgotBtn = findViewById(R.id.txt_forgotPassword);

        inflater = this.getLayoutInflater();

        reset_alert = new AlertDialog.Builder(this);
        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = inflater.inflate(R.layout.reset_pop, null);
                reset_alert.setTitle("Reset forgotten password?")
                        .setMessage("Enter your email to get password reset link.")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText email = view1.findViewById(R.id.reset_email_pop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Email is required");
                                    email.requestFocus();
                                    return;
                                }
                                firebaseAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LogIn.this, "Reset link sent to email", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setView(view1)
                        .create().show();

            }
        });



        registerBTn = findViewById(R.id.txt_Register);
        registerBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }

        });
        userEmailET = findViewById(R.id.logIn_Email_editText);
        userPasswordET = findViewById(R.id.logIn_Password_editText);
        logInBTN = findViewById(R.id.logIn_btn);

        logInBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //extract
                String userEmail = userEmailET.getText().toString();
                String userPassword = userPasswordET.getText().toString();
                //validation
                if (userEmail.isEmpty()){
                    userEmailET.setError("Email is required");
                    userEmailET.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    userEmailET.setError("Please enter a valid email!");
                    userEmailET.requestFocus();
                    return;
                }
                if (userPassword.isEmpty()){
                    userEmailET.setError("Please enter password!");
                }
                firebaseAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}