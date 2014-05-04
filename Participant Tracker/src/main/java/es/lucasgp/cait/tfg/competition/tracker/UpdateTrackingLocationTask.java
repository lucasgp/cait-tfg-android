package es.lucasgp.cait.tfg.competition.tracker;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.drive.internal.h;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.Executor;

public class UpdateTrackingLocationTask extends AsyncTask<Location, Void, Void> {

    private final HttpPost httpPost;
    private final HttpClient httpClient;

    public UpdateTrackingLocationTask(final HttpClient httpClient, final HttpPost httpPost) {
        this.httpClient = httpClient;
        this.httpPost = httpPost;
    }

    @Override
    protected Void doInBackground(Location[] locations) {
        try {

            Location location = locations[0];

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

            this.httpPost.setEntity(new StringEntity(feature.toString()));
            HttpResponse resp = this.httpClient.execute(this.httpPost);

            Log.i("ParticipantTracker", "Server returned status: " + Integer.toString(resp.getStatusLine().getStatusCode()));

        } catch (Exception e) {
            Log.e("ParticipantTracker", "Error while tracking", e);
        }

        return null;
    }
}
