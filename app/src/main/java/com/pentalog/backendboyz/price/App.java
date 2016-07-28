package com.pentalog.backendboyz.price;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class App extends AppCompatActivity {
    private LocationManager mLocationManager;
    private double mLatitude,mLongitude;
    private TextView mLocationLabel;
    private TextView mCityLabel;

    private  String mCity;

    String jsonData;

    private static final String TAG = "LOCATION STUFF PLZ WORK";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_app);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mLocationLabel = (TextView) findViewById(R.id.locationLabel);
        mCityLabel = (TextView) findViewById(R.id.cityLabel);

        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {


            @Override
            public void onLocationChanged(Location location) {
                Log.d(TAG, location.toString());

                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();
                mLocationLabel.setText(mLatitude+" , "+mLongitude);
                String mApiUrl="http://nominatim.openstreetmap.org/reverse?format=json&lat=" +mLatitude+ "&lon=" +mLongitude;


//
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
                                    mCity = getCurrentCity(jsonData);
                                    Log.d(TAG,mCity);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mCityLabel.setText(mCity);
                                        }
                                    });


                                }
                                else{
                                    alertUserAboutError();
                                }

                            } catch (IOException e) {
                                Log.e(TAG, "Exception Caught:", e);
                            }
                              catch (JSONException e){
                                Log.e(TAG, "Exception Caught:", e);
                              }

                        }
                    });

                }

                else {
                    Toast.makeText(App.this, R.string.network_unavailable, Toast.LENGTH_LONG).show();
                }
//
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

    private String getCurrentCity(String jsonData) throws JSONException{
        JSONObject response = new JSONObject(jsonData);
        JSONObject address = response.getJSONObject("address");
        String mCity;

        if (address.has("city")){
            mCity = address.getString("city");
        }
        else {
            mCity = address.getString("town");
        }
        return mCity;
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

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuItem1 :
                Toast.makeText(App.this, "TO BE IMPLEMENTED ...Someday (hopefully)", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuItem2 :
                Toast.makeText(App.this, "Made with <3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menuItem3 :
                Toast.makeText(App.this, "No ads till now. Blame the lazy developers", Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }
}
