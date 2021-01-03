package com.thesohelshaikh.ytanalyser;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class YTService {
    public static final String QUERY_GET_DURATION = "https://www.googleapis" +
            ".com/youtube/v3/videos?part=contentDetails&key=AIzaSyBJ" +
            "-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk&id=";
    public static final String QUERY_GET_VIDEO_DETAILS = "https://youtube.googleapis" +
            ".com/youtube/v3/videos?part=snippet&key=AIzaSyBJ-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk&id=";
    public static final String QUERY_PLAYLIST_DETAILS = "https://www.googleapis" +
            ".com/youtube/v3/playlists?part=snippet&key=AIzaSyBJ-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk" +
            "&id=";
    public static final String QUERY_PLAYLIST_VIDEO_IDS = "https://www.googleapis" +
            ".com/youtube/v3/playlistItems?part=contentDetails&maxResults=50&fields=items" +
            "/contentDetails/videoId,nextPageToken&key=AIzaSyBJ-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk";
    public static final String QUERY_PLAYLIST_VIDEO_DETAILS = "https://www.googleapis" +
            ".com/youtube/v3/videos?part=contentDetails&key=AIzaSyBJ" +
            "-YGFNIDYeGo5pTvexZ2fJgsM6Erenkk&fields=items/contentDetails/duration&id=";
    private static final String TAG = "YTService";

    private String nextPageToken1 = null;

    Context context;

    public YTService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener {
        void onError(String errorMessage);

        void onResponse(VideoModel videoModel);
    }

    public void getDuration(String id, final VolleyResponseListener listener) {
        String url = QUERY_GET_DURATION + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String duration =
                            response.getJSONArray("items").getJSONObject(0).getJSONObject(
                                    "contentDetails").getString("duration");
                    String id = response.getJSONArray("items").getJSONObject(0).getString("id");
                    VideoModel video = new VideoModel();
                    video.setId(id);
                    video.setDuration(duration);
                    listener.onResponse(video);
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

    public interface VideoDetailsListener {
        void onError(String errorMessage);

        void onResponse(VideoModel videoModel);
    }

    public void getVideoDetails(final String id, final VideoModel video,
                                final VideoDetailsListener listener) {
        String url = QUERY_GET_VIDEO_DETAILS + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    JSONObject snippet = items.getJSONObject(0).getJSONObject("snippet");

                    String title = snippet.getString("title");
                    String channelTitle = snippet.getString("channelTitle");
                    String url =
                            snippet.getJSONObject("thumbnails").getJSONObject("maxres").getString("url");

                    video.setTitle(title);
                    video.setChannelTitle(channelTitle);
                    video.setThumbnailURL(url);

                    listener.onResponse(video);
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

    public interface PlaylistDetailsListener {
        void onError(String errorMessage);

        void onResponse(PlaylistModel playlistModel);
    }

    /**
     * Fetches all required details for a playlist such as title thumbnail and the author of the
     * playlist. Makes successive call to fetch the duration of the playlist.
     *
     * @param id       id of the playlist
     * @param listener listener to be called when response is received
     */
    public void getPlaylistDetails(String id, final PlaylistDetailsListener listener) {
        String url = QUERY_PLAYLIST_DETAILS + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray items = response.getJSONArray("items");
                    JSONObject snippet = items.getJSONObject(0).getJSONObject("snippet");

                    String playlistID = items.getJSONObject(0).getString("id");
                    String playlistTitle = snippet.getString("title");
                    String author = snippet.getString("channelTitle");
                    String thumbnailURL =
                            snippet.getJSONObject("thumbnails").getJSONObject("maxres").getString("url");

                    PlaylistModel playlist = new PlaylistModel();
                    playlist.setTitle(playlistTitle);
                    playlist.setCreatedBy(author);
                    playlist.setId(playlistID);
                    playlist.setThumbnailURL(thumbnailURL);

                    getPlaylistVideoIDs(playlistID, playlist, null,
                            new PlaylistVideoDetailsListener() {
                                @Override
                                public void onError(String errorMessage) {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(PlaylistModel playlistModel) {
                                    // Fetched all idsto
                                    listener.onResponse(playlistModel);

                                }
                            });


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

    public interface PlaylistVideoDetailsListener {
        void onError(String errorMessage);

        void onResponse(PlaylistModel playlist);
    }

    /**
     * Makes recursive call and fetches all the video ids from a playlist. For each page of video
     * ids, their duration is fetched.
     *
     * @param playlistID id of playlist
     * @param playlist   playlist model
     * @param pageToken  pageToken present in the page
     * @param listener   listener to be call after getting response
     */
    public void getPlaylistVideoIDs(final String playlistID, final PlaylistModel playlist,
                                    String pageToken,
                                    final PlaylistVideoDetailsListener listener) {
        String oldURL = QUERY_PLAYLIST_VIDEO_IDS + "&playlistId=" + playlistID;
        String newURL = oldURL;
        if (!(pageToken == null)) {
            newURL = oldURL + "&pageToken=" + pageToken;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, newURL, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray videosInPlaylist =
                            response.getJSONArray("items");

                    // if it last page, pageToken will be null
                    nextPageToken1 = null;
                    try {
                        nextPageToken1 = response.getString("nextPageToken");
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }

                    // build a string of comma separated video ids
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < videosInPlaylist.length(); i++) {
                        JSONObject obj = (JSONObject) videosInPlaylist.get(i);
                        String id = obj.getJSONObject("contentDetails").getString("videoId");
                        sb.append(id).append(",");
                    }

                    String commaSeparatedIds = sb.toString();

                    // remove the last comma
                    if (commaSeparatedIds.length() > 0) {
                        commaSeparatedIds = commaSeparatedIds.substring(0,
                                commaSeparatedIds.length() - 1);
                    }

                    getPlaylistVideoDuration(commaSeparatedIds, playlist,
                            new PlaylistVideoDurationsListener() {
                                @Override
                                public void onError(String errorMessage) {

                                }

                                @Override
                                public void onResponse(PlaylistModel playlistModel) {
                                    if (nextPageToken1 == null) {
                                        // break out of here, duration for all the videos has
                                        // been calculated
                                        listener.onResponse(playlist);
                                    } else {
                                        //query again for next page
                                        getPlaylistVideoIDs(playlistID, playlist, nextPageToken1,
                                                listener);
                                    }
                                }
                            });

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

    public interface PlaylistVideoDurationsListener {
        void onError(String errorMessage);

        void onResponse(PlaylistModel playlistModel);
    }

    /**
     * Fetch duration for multiple video ids and keep track of it by adding it to playlist.
     *
     * @param ids      comma separated ids
     * @param playlist playlist for which duration is to be calculated
     * @param listener listener call upon receiving response
     */
    public void getPlaylistVideoDuration(String ids, final PlaylistModel playlist,
                                         final PlaylistVideoDurationsListener listener) {

        String url = QUERY_PLAYLIST_VIDEO_DETAILS + ids;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray videosInPlaylist =
                            response.getJSONArray("items");

                    // get durations of each of the videos
                    ArrayList<String> durations = new ArrayList<>();
                    for (int i = 0; i < videosInPlaylist.length(); i++) {
                        JSONObject obj = (JSONObject) videosInPlaylist.get(i);
                        String id = obj.getJSONObject("contentDetails").getString("duration");
                        durations.add(id);
                    }

                    long totalPlaylistDuration = UtilitiesManger.parsePlaylistDurations(durations);
                    playlist.addDuration(totalPlaylistDuration);
                    listener.onResponse(playlist);
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
