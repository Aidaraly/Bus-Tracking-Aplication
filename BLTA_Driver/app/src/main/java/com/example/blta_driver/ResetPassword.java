package com.example.blta_driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    EditText resetPasswordET, confirmPasswordET;
    TextView resetBTN;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        resetPasswordET = findViewById(R.id.resetPassword_editTextRS);
        confirmPasswordET = findViewById(R.id.confirm_Password_editTextRS);
        resetBTN= findViewById(R.id.reset_btnRS);

        user = FirebaseAuth.getInstance().getCurrentUser();



        resetBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(resetPasswordET.getText().toString().isEmpty()){
                    resetPasswordET.setError("Password is required!");
                    resetPasswordET.requestFocus();
                    return;
                }if(confirmPasswordET.getText().toString().isEmpty()){
                    confirmPasswordET.setError("Confirm password!");
                    confirmPasswordET.requestFocus();
                    return;
                }
                if(!resetPasswordET.getText().toString().equals(confirmPasswordET.getText().toString())){
                    confirmPasswordET.setError("Password doesn't match!");
                    confirmPasswordET.requestFocus();
                    return;
                }
                user.updatePassword(resetPasswordET.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ResetPassword.this, "Password is updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
}