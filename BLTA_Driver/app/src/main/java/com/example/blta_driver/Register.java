package com.example.blta_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blta_driver.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText userEmailET, userNameET, busNumET, userPasswordET, passwordConfirmET;
    TextView registerBtn;
    String userName, userEmail, busNum, userPassword, passwordConfirm, latitude, longitude;

    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        userEmailET = findViewById(R.id.register_Email_editText);
        userNameET = findViewById(R.id.register_Name_editText);
        busNumET = findViewById(R.id.register_busNum_editText);
        userPasswordET = findViewById(R.id.register_passwordChoose_editText);
        passwordConfirmET = findViewById(R.id.register_passwordConfirm_editText);

        registerBtn = findViewById(R.id.register_btn);

        mAuth =FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Data Extraction
                 userName = userNameET.getText().toString();
                 userEmail = userEmailET.getText().toString();
                 busNum = busNumET.getText().toString();
                 userPassword = userPasswordET.getText().toString();
                 passwordConfirm = passwordConfirmET.getText().toString();
                 latitude = "0.0";
                 longitude = "0.0";



                //Validation
                if(userName.isEmpty()){
                    userNameET.setError("Name is required!");
                    userNameET.requestFocus();
                    return;
                }
                if (busNum.isEmpty()){
                    busNumET.setError("Bus num is required!");
                    busNumET.requestFocus();
                    return;
                }
                if (userEmail.isEmpty()){
                    userEmailET.setError("Email is required!");
                    userEmailET.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                    userEmailET.setError("Please enter a valid email!");
                    userEmailET.requestFocus();
                    return;
                }
                if (userPassword.isEmpty()){
                    userPasswordET.setError("Password is required!");
                    userPasswordET.requestFocus();
                    return;
                }
                if (!userPassword.equals(passwordConfirm)){
                    passwordConfirmET.setError("Password Do not match!");
                    passwordConfirmET.requestFocus();
                    return;
                }
                Toast.makeText(Register.this, "Data validated", Toast.LENGTH_LONG).show();
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserHelper userHelper = new UserHelper(userName, busNum, userEmail, latitude, longitude);
                        FirebaseDatabase.getInstance("https://blta-driver-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(userHelper);


                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });








            }


        });



    }


}