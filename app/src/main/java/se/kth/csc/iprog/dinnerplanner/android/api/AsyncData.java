package se.kth.csc.iprog.dinnerplanner.android.api;

import org.json.JSONObject;

/**
 * Created by AZN on 2017-03-11.
 */

public interface AsyncData {
    public void onData(JSONObject data);
    public void onError(String errorMessage);
}
