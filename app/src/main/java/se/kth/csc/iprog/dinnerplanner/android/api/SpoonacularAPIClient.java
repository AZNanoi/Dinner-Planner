package se.kth.csc.iprog.dinnerplanner.android.api;
import com.loopj.android.http.*;

/**
 * Created by AZN on 2017-03-11.
 */


public class SpoonacularAPIClient {
    private static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/";
    private static final String API_KEY = "";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("X-Mashape-Key",API_KEY);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}