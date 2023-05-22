package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private LinearLayout LO4;
    private LinearLayout LO5;
    private LinearLayout LO6;
    private DatabaseReference databaseReference;
    public String FAN = "";
    public String Lat,Lng;
    String CHANNEL_ID1  = "1";
    String CHANNEL_ID2  = "2";
    Integer notificationId1 = 1;
    Integer notificationId2 = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        text1=findViewById(R.id.textView1);
        text2=findViewById(R.id.textView2);
        text3=findViewById(R.id.textView3);
        text4=findViewById(R.id.textView4);
        text5=findViewById(R.id.textView5);
        text6=findViewById(R.id.textView6);
        LO4=findViewById(R.id.linearLayout4);
        LO5=findViewById(R.id.linearLayout5);
        LO6=findViewById(R.id.linearLayout6);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String data1 = dataSnapshot.child("TEMPERATURE").getValue().toString();
                    String data2 = dataSnapshot.child("HUMIDITY").getValue().toString();
                    String data3 = dataSnapshot.child("WATER_LVL").getValue().toString();
                    String data4 = dataSnapshot.child("LATITUDE").getValue().toString();
                    String data5 = dataSnapshot.child("LONGITUDE").getValue().toString();
                    String data6 = dataSnapshot.child("FAN").getValue().toString();
                    text1.setText(data1+"°C");
                    text2.setText(data2+"%");
                    text3.setText(data3);
                    text4.setText(data4 + "°N\n" + data5+"°W");
                    text5.setText(data6);
                    FAN = "& Fan is "+data6;
                    Lat=data4;
                    Lng=data5;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String data1 = dataSnapshot.child("TEMPERATURE").getValue().toString();
                    String Stemp_trigger = dataSnapshot.child("TRIGGERED_TEMP").getValue().toString();
                    Integer temp = Integer.parseInt(data1);
                    Integer temp_trigger = Integer.parseInt(Stemp_trigger);
                    if(temp>temp_trigger) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,0,intent,0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID1)
                                .setSmallIcon(R.drawable.ic_thermometer)
                                .setContentTitle("Weather Update")
                                .setContentText("Temperature is " +data1+"°C "+FAN)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true)
                                .setContentIntent(pendingIntent);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
                        notificationManagerCompat.notify(notificationId1, builder.build());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        LO4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("Lat", Lat);
                intent.putExtra("Lng", Lng);
                startActivity(intent);
            }
        });

        LO5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FanControl.class);
                startActivity(intent);
            }
        });
        LO6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Weather_Forecast.class);
                intent.putExtra("Lat", Lat);
                intent.putExtra("Lng", Lng);
                startActivity(intent);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.Setting){
            //Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel(){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name1 = "Channel1";
            String description1 = "Description1";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_ID1,name1, importance);
            channel1.setDescription(description1);

            /*CharSequence name2 = "Channel2";
            String description2 = "Description2";
            NotificationChannel channel2 = new NotificationChannel(CHANNEL_ID2,name2, importance);
            channel1.setDescription(description2);*/

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            //notificationManager.createNotificationChannel(channel2);
        }

    }

}
