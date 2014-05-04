package es.lucasgp.cait.tfg.competition.tracker;

public final class AppResources {

    private static final String BASE_URL = "http://83.165.29.107";
    private static final String MAIN_PAGE = "/web/main.html";
    private static final String FEATURES = "/resources/trackings/%s/features";

    public static final String getMainPageURL() {
        return BASE_URL + MAIN_PAGE;
    }

    public static final String getFeaturesURL(final String trackingId) {
        return BASE_URL + String.format(FEATURES, trackingId);
    }

    private AppResources(){}
}
