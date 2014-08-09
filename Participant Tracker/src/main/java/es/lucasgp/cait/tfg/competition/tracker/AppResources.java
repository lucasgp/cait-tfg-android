package es.lucasgp.cait.tfg.competition.tracker;

public final class AppResources {

    private static String host = "";

    private static final String MAIN_PAGE = "/web/main.html";
    private static final String FEATURES = "/resources/trackings/%s/features";

    public static final String getMainPageURL(String host) {
        AppResources.host = host;
        return getBaseURL() + MAIN_PAGE;
    }

    public static final String getBaseURL() {
        return "http://" + AppResources.host;
    }

    public static final String getFeaturesURL(final String trackingId) {
        return getBaseURL() + String.format(FEATURES, trackingId);
    }

    private AppResources(){}
}
