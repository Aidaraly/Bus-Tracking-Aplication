package com.example.blta_driver;

import static com.example.blta_driver.R.id.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    TextView logOutBtn, verifyTXT;
    Button btnVRF;
    FirebaseAuth auth;
    Switch gpsSwitch;

    private String userID;

    DatabaseReference databaseReference;
    FirebaseUser user;

    private GpsTracker gpsTracker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = FirebaseAuth.getInstance();
        logOutBtn = findViewById(log_out_btn);
        verifyTXT =findViewById(R.id.verifyTXT);
        btnVRF = findViewById(btnVerify);
        gpsSwitch = findViewById(GPS_switch);


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance("https://blta-driver-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Users");
        userID = user.getUid();
        final TextView userNameTV = findViewById(txtDriverName);
        final TextView busNumTV = findViewById(txtBusNumber);
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelper userProfile = snapshot.getValue(UserHelper.class);
                if (userProfile !=  null){
                    String userName = userProfile.userName;
                    String busNum = userProfile.busNum;
                    userNameTV.setText(userName);
                    busNumTV.setText(busNum);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







        if(!auth.getCurrentUser().isEmailVerified()){
            verifyTXT.setVisibility(View.VISIBLE);
            btnVRF.setVisibility(View.VISIBLE);
        }
        btnVRF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                        verifyTXT.setVisibility(View.GONE);
                        btnVRF.setVisibility(View.GONE);
                    }
                });
            }
        });



        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LogIn.class));
            }
        });

        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b == true){
                    Toast.makeText(getBaseContext(), "On", Toast.LENGTH_SHORT).show();
                    gpsTracker = new GpsTracker(MainActivity.this);


                    if(gpsTracker.canGetLocation()){

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();
                        HashMap hashMap = new HashMap();
                        hashMap.put("latitude", String.valueOf(latitude));
                        hashMap.put("longitude", String.valueOf(longitude));
                        databaseReference.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MainActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed update", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        gpsTracker.showSettingsAlert();

                    }

                }else {

                    Toast.makeText(getBaseContext(), "Off", Toast.LENGTH_SHORT).show();
                    if(gpsTracker.canGetLocation()){

                        double latitude = gpsTracker.getLatitude();
                        double longitude = gpsTracker.getLongitude();
                        HashMap hashMap = new HashMap();
                        hashMap.put("latitude", "0.00");
                        hashMap.put("longitude", "0.00");
                        databaseReference.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(MainActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed update", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }else{
                        gpsTracker.showSettingsAlert();

                    }





                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == resetUserPassword){
            startActivity( new Intent(getApplicationContext(), ResetPassword.class));

        }
        return super.onOptionsItemSelected(item);
    }



}