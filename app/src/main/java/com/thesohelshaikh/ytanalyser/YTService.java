package com.thesohelshaikh.ytanalyser;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class YTService {
    public static final String QUERY_GET_DURATION = "https://www.googleapis.com/youtube/v3/videos?part=contentDetails&key=AIzaSyBJ-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk&id=";
    Context context;

    public YTService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String errorMessage);

        void onResponse(String response);
    }

    public void getDuration(String id, final VolleyResponseListener listener) {
        String url = QUERY_GET_DURATION + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String duration = response.getJSONArray("items").getJSONObject(0).getJSONObject("contentDetails").getString("duration");
                    listener.onResponse(duration);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });

        // Add the request to the RequestQueue.
        RequestManager.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
