package es.lucasgp.cait.tfg.competition.tracker;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

public class TrackingService extends IntentService {

    private boolean run = true;
    private HttpClient httpClient;

    public TrackingService() {
        super("TrackingService");
        httpClient = new DefaultHttpClient();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int interval = intent.getIntExtra("interval", 10000);
        String trackingId = intent.getStringExtra("trackingId");
        GeolocationTracker tracker = new GeolocationTracker(this);

        Log.i("ParticipantTracker", "Interval: " + interval + " | Tracking ID: " + trackingId);

        HttpPost post = new HttpPost(AppResources.getFeaturesURL(trackingId));
        post.setHeader("content-type", "application/json");

        Log.i("ParticipantTracker", "POST URL: " + post.getURI());

        while (run) {
            try {

                Location location = tracker.getCurrentLocation();
                if(location != null) {

                    Log.i("ParticipantTracker", location.getLatitude() + ", " + location.getLongitude());

                    JSONObject feature = new JSONObject();
                    JSONObject point = new JSONObject();
                    feature.put("type", "Feature");
                    feature.put("geometry", point);
                    point.put("type", "Point");
                    JSONArray coordinates = new JSONArray();
                    point.put("coordinates", coordinates);
                    coordinates.put(location.getLongitude());
                    coordinates.put(location.getLatitude());


                    Log.i("ParticipantTracker", "Sending feature " + feature.toString() + " to server");

                    post.setEntity(new StringEntity(feature.toString()));
                    HttpResponse resp = httpClient.execute(post);

                    Log.i("ParticipantTracker", "Server returned status: " + Integer.toString(resp.getStatusLine().getStatusCode()));

                } else {
                    Log.i("ParticipantTracker", "Can't retrieve actual position");
                }

                SystemClock.sleep(interval);

            } catch (Exception e) {
                Log.e("ParticipantTracker", "Error while tracking", e);
            }
        }
    }

    @Override
    public void onDestroy() {
        this.run = false;
        super.onDestroy();
    }
}