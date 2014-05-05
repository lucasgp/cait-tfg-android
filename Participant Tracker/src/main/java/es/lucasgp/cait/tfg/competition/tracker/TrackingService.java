package es.lucasgp.cait.tfg.competition.tracker;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class TrackingService extends Service implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private long interval = 15000;
    private float minDisplacement = 50;
    private String trackingId;
    private HttpClient httpClient = new DefaultHttpClient();
    private HttpPost httpPost;
    private LocationClient locationClient;
    private LocationRequest locationRequest;

    @Override
    public void onConnected(Bundle bundle) {
        this.locationClient.requestLocationUpdates(this.locationRequest, this);
    }

    @Override
    public void onDisconnected() {
        Log.e("ParticipantTracker", "Location service disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("ParticipantTracker", connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        new UpdateTrackingLocationTask(this.httpClient, this.httpPost).execute(location);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent != null) {
            this.interval = intent.getLongExtra("interval", this.interval);
            this.minDisplacement = intent.getFloatExtra("minDisplacement", this.minDisplacement);
            this.trackingId = intent.getStringExtra("trackingId");
            this.httpPost = new HttpPost(AppResources.getFeaturesURL(this.trackingId));
            this.httpPost.setHeader("content-type", "application/json");

            Log.i("ParticipantTracker", "Interval: " + this.interval + " | Tracking ID: " + this.trackingId + " | URL: " + this.httpPost.getURI());

            this.locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(this.interval)
                    .setFastestInterval(this.interval);
                    //.setSmallestDisplacement(this.minDisplacement);

            this.locationClient = new LocationClient(this, this, this);
            this.locationClient.connect();
        }
        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        this.locationClient.removeLocationUpdates(this);
        this.locationClient.disconnect();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}