package com.pentalog.backendboyz.price;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GpsTest extends AppCompatActivity {
    private LocationManager mLocationManager;
    private double mLatitude,mLongitude;
    private TextView mLocationLabel;

    private static final String TAG = "LOCATION STUFF PLZ WORK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_test);

        mLocationLabel = (TextView) findViewById(R.id.locationLabel);

        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, location.toString());

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mLocationLabel.setText(mLatitude+" , "+mLongitude);
                String mApiUrl=" http://nominatim.openstreetmap.org/reverse?format=json&lat=" +mLatitude+ "&lon=" +mLongitude;
/*
                if(isNetworkAvailable()) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(mApiUrl)
                            .build();

                    Call call = client.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String jsonData = response.body().string();
                                if (response.isSuccessful()){
                                    Log.d(TAG,jsonData);
                                }
                                else{

                                }

                            } catch (IOException e) {
                                Log.e(TAG, "Exception Caught:", e);

                            }
                        }
                    });

                }

                else {
                    Toast.makeText(GpsTest.this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
                }
*/
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return  isAvailable;
    }
}