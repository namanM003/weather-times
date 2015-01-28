package com.example.naman_pc.weathertimes_zappos;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.browse.MediaBrowser;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class MainActivity extends ActionBarActivity {
    private LocationManager locationmanager;
    TextView textView;
   // private LocationListener locationlistener;
   // private String location;

    public void getWeather(View view){
        //This method is responsible to search location as specified in text box and display weather of that location

        textView = (TextView)this.findViewById(R.id.textview);
        String text="You are trying to find by search";
        this.textView.setText(text);
        JSONObject data=WeatherData.getdata();
        if(data == null){
            Toast.makeText(this,"City Not Found",Toast.LENGTH_LONG).show();
        }
        else{
            data.
        }
    }

    public void getLocation(View view){
        //This method is responsible to search current location of the user and display weather of that location
        try {
            locationmanager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            Location location = locationmanager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                textView = (TextView) this.findViewById(R.id.textview);
                String text = "Unable to find location";
                this.textView.setText(text);

            }
        }
        catch(Exception e){
            textView = (TextView) this.findViewById(R.id.textview);
            String text="Location ERROR";
            this.textView.setText(text);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // locationmanager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
       // locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationlistener);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
