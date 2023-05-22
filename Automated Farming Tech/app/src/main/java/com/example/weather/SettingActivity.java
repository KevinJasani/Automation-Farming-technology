package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {
    private EditText editText8;
    private Button save;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText8 = (EditText) findViewById(R.id.editText8);
        save = (Button) findViewById(R.id.button);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("TRIGGERED_TEMP");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String Sdata8 = dataSnapshot.getValue().toString();
                    //Integer data8 = Integer.parseInt(Sdata8);
                    editText8.setText(Sdata8);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data8 = editText8.getText().toString();
                databaseReference.setValue(Integer.parseInt(data8));
                Toast.makeText(SettingActivity.this,"Triggere Successfully Update!",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
