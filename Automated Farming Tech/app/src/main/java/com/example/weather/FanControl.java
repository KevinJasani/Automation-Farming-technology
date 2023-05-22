package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FanControl extends AppCompatActivity {
    private TextView text5;
    private Switch switch1,switch2;
    public String fan_status;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_control);

        text5=(TextView) findViewById(R.id.textView7);
        switch1=(Switch) findViewById(R.id.switch1);
        switch2=(Switch) findViewById(R.id.switch2);

        getSupportActionBar().setTitle("Fan Control");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String data6 = dataSnapshot.child("FAN").getValue().toString();
                    text5.setText("Fan Status: "+data6);
                    fan_status = dataSnapshot.child("FAN_CONTROL").getValue().toString();
                    if(fan_status.equals("MANUAL")){
                        switch1.setChecked(true);
                        switch2.setVisibility(View.VISIBLE);
                    }
                    else {
                        switch1.setChecked(false);
                        switch2.setVisibility(View.INVISIBLE);
                    }
                    if(data6.equals("ON")){
                        switch2.setChecked(true);
                    }
                    else{
                        switch2.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch1.isChecked()){
                    databaseReference.child("FAN_CONTROL").setValue("MANUAL");
                }
                else {
                    databaseReference.child("FAN_CONTROL").setValue("AUTO");
                }
            }
        });
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("FAN");
        switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(switch2.isChecked()){
                    databaseReference.child("FAN").setValue("ON");
                }
                else {
                    databaseReference.child("FAN").setValue("OFF");
                }
            }
        });
    }
}
