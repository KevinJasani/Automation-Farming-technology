#include "DHT.h"        // including the library of DHT11 temperature and humidity sensor
#define DHTPIN D1   // DHT 11
#define DHTTYPE DHT11
#include <ArduinoJson.h>
#include <ESP8266WiFi.h>                                               
#include <FirebaseArduino.h>                                        
 
#define FIREBASE_HOST "weatherforecast-410d8.firebaseio.com"              // the project name address from firebase id
#define FIREBASE_AUTH "kptYspcg6ZziZ3YBPZDtMEnamb1dCgsOCMvTc91Q"       // the secret key generated from firebase
#define WIFI_SSID "SHREY"                                          
#define WIFI_PASSWORD "1234567890"                                  

String fireStatus = ""; 
//int resval = 0;
//int respin = D2;
DHT dht(DHTPIN, DHTTYPE); 
#include <TinyGPS++.h>
#include <SoftwareSerial.h>
#include <ESP8266WiFi.h>

TinyGPSPlus gps;  // The TinyGPS++ object

SoftwareSerial ss(2,0); // The serial connection to the GPS device

const char* ssid = "SHREY";
const char* password = "1234567890";

float latitude , longitude;
int year , month , date, hour , minute , second;
String date_str , time_str , lat_str , lng_str;
int pm;

WiFiServer server(80);

void setup(void)
{ 
  ss.begin(9600);
  dht.begin();
  Serial.println("Humidity and temperature\n\n");
  Serial.begin(9600);
  delay(1000);    
  pinMode(D2, OUTPUT);                 
  pinMode(D5, OUTPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);                               
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);
  while (WiFi.status() != WL_CONNECTED) 
  {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  
  server.begin();
  Serial.println("Server started");

  // Print the IP address
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);                  // connect to firebase
}
void loop() {

    
    float h = dht.readHumidity();
    float t = dht.readTemperature();
    int w = analogRead(A0);          
    Serial.print("Current humidity = ");
    Serial.print(h);
    Serial.print("%  ");
    Serial.print("temperature = ");
    Serial.print(t); 
    Serial.print("C  ");
    Serial.print("Water = ");
    Serial.println(w);
    Firebase.setFloat("TEMPERATURE", t);
    Firebase.setFloat("HUMIDITY", h);

    if(w>90){
      Firebase.setString("WATER_LVL", "Raining..."); 
    }
    else{
      Firebase.setString("WATER_LVL", "Clear");
    }
    
    //Firebase.setFloat("WATER_LVL", w);   
    String fan_control = Firebase.getString("FAN_CONTROL");
    int trig_temp= Firebase.getInt("TRIGGERED_TEMP");
    Serial.print("Trigger Temperature: ");
    Serial.println(trig_temp);


    if(fan_control.equals("AUTO")){
      Serial.print("Fan Control: ");
      Serial.println(fan_control);
      if(t>trig_temp){
        Firebase.setString("FAN","ON");
        digitalWrite(D5, HIGH);   // turn the LED on (HIGH is the voltage level)
      }
      else{
        Firebase.setString("FAN","OFF");
        digitalWrite(D5, LOW);
      }
    }
    else{
      Serial.print("Fan Control: ");
      Serial.print(fan_control);
      String fan = Firebase.getString("FAN");
      Serial.print("  Fan Status: ");
      Serial.println(fan);
      if(fan.equals("ON")){
        digitalWrite(D5, HIGH);   // turn the LED on (HIGH is the voltage level)
      }
      else{
        digitalWrite(D5, LOW);
      }
    }

    if(t>trig_temp){
        Firebase.setString("BUZZER_STATUS","ON");
        digitalWrite(D2, HIGH);   // turn the LED on (HIGH is the voltage level)
        delay(500);                       // wait for a second
        digitalWrite(D2, LOW);    // turn the LED off by making the voltage LOW
        delay(500);
    }
    else{Firebase.setString("BUZZER_STATUS","OFF");}
    
  while (ss.available() > 0)
    if (gps.encode(ss.read()))
    {
      if (gps.location.isValid())
      {
        latitude = gps.location.lat();
        lat_str = String(latitude , 8);
        Firebase.setString("LATITUDE",lat_str);
        longitude = gps.location.lng();
        lng_str = String(longitude , 8);
        Firebase.setString("LONGITUDE",lng_str);
      }

      if (gps.date.isValid())
      {
        date_str = "";
        date = gps.date.day();
        month = gps.date.month();
        year = gps.date.year();

        if (date < 10)
          date_str = '0';
        date_str += String(date);

        date_str += " / ";

        if (month < 10)
          date_str += '0';
        date_str += String(month);

        date_str += " / ";

        if (year < 10)
          date_str += '0';
        date_str += String(year);
      }

      if (gps.time.isValid())
      {
        time_str = "";
        hour = gps.time.hour();
        minute = gps.time.minute();
        second = gps.time.second();

        minute = (minute + 30);
        if (minute > 59)
        {
          minute = minute - 60;
          hour = hour + 1;
        }
        hour = (hour + 5) ;
        if (hour > 23)
          hour = hour - 24;

        if (hour >= 12)
          pm = 1;
        else
          pm = 0;

        hour = hour % 12;

        if (hour < 10)
          time_str = '0';
        time_str += String(hour);

        time_str += " : ";

        if (minute < 10)
          time_str += '0';
        time_str += String(minute);

        time_str += " : ";

        if (second < 10)
          time_str += '0';
        time_str += String(second);

        if (pm == 1)
          time_str += " PM ";
        else
          time_str += " AM ";

      }

    }
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client)
  {
    return;
  }

  // Prepare the response
  String s = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n <!DOCTYPE html> <html> <head> <title>GPS Interfacing with NodeMCU</title> <style>";
  s += "a:link {background-color: YELLOW;text-decoration: none;}";
  s += "table, th, td {border: 1px solid black;} </style> </head> <body> <h1  style=";
  s += "font-size:300%;";
  s += " ALIGN=CENTER> GPS Interfacing with NodeMCU</h1>";
  s += "<p ALIGN=CENTER style=""font-size:150%;""";
  s += "> <b>Location Details</b></p> <table ALIGN=CENTER style=";
  s += "width:50%";
  s += "> <tr> <th>Latitude</th>";
  s += "<td ALIGN=CENTER >";
  s += lat_str;
  s += "</td> </tr> <tr> <th>Longitude</th> <td ALIGN=CENTER >";
  s += lng_str;
  s += "</td> </tr> <tr>  <th>Date</th> <td ALIGN=CENTER >";
  s += date_str;
  s += "</td></tr> <tr> <th>Time</th> <td ALIGN=CENTER >";
  s += time_str;
  s += "</td>  </tr> </table> ";
 
  
  if (gps.location.isValid())
  {
     s += "<p align=center><a style=""color:RED;font-size:125%;"" href=""http://maps.google.com/maps?&z=15&mrt=yp&t=k&q=";
    s += lat_str;
    s += "+";
    s += lng_str;
    s += """ target=""_top"">Click here!</a> To check the location in Google maps.</p>";
  }

  s += "</body> </html> \n";

  client.print(s);
   
    
  delay(5000);
}
