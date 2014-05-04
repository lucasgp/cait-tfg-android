package es.lucasgp.cait.tfg.competition.tracker;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class TrackingJavascriptInterface {

    private Context context;

    public TrackingJavascriptInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void startTracking(String trackingId, long interval, float minDisplacement) {
        Intent intent = new Intent(context, TrackingService.class);
        intent.putExtra("trackingId", trackingId);
        intent.putExtra("interval", interval);
        intent.putExtra("minDisplacement", minDisplacement);
        context.startService(intent);
        Log.i("ParticipantTracker", "Tracking service started!");
    }

    @JavascriptInterface
    public void stopTracking() {
        context.stopService(new Intent(context, TrackingService.class));
        Log.i("ParticipantTracker", "Tracking service stopped!");
    }
}
