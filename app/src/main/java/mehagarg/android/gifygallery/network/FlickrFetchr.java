package mehagarg.android.gifygallery.network;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import mehagarg.android.gifygallery.model.GalleryItem;

public class FlickrFetchr {
    private static final String TAG = "FlickrFetchr";

    private static final String API_KEY = "dc6zaTOxFJmzC";
    private static final String LIMIT = "100";
    private static final String TRENDING_METHOD = "trending";
    private static final String SEARCH_METHOD = "search";

    private static final Uri ENDPOINT = Uri.parse("http://api.giphy.com/v1/gifs/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("limit", LIMIT)
            .build();

//    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
//    private static final String SEARCH_METHOD = "flickr.photos.search";
//    private static final Uri ENDPOINT = Uri
//            .parse("https://api.flickr.com/services/rest/")
//            .buildUpon()
//            .appendQueryParameter("api_key", API_KEY)
//            .appendQueryParameter("format", "json")
//            .appendQueryParameter("nojsoncallback", "1")
//            .appendQueryParameter("extras", "url_s")
//            .build();

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchTrendingPhotos() {
        String url = buildUrl(TRENDING_METHOD, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query) {
        String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
    }

    private List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();

        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    private String buildUrl(String method, String query) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendPath(method);
        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("q", query);
        }
        return uriBuilder.build().toString();
    }

//    private String buildUrl(String method, String query) {
//        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
//                .appendQueryParameter("method", method);
//
//        if (method.equals(SEARCH_METHOD)) {
//            uriBuilder.appendQueryParameter("text", query);
//        }
//
//        return uriBuilder.build().toString();
//    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray dataArray = jsonBody.getJSONArray("data");
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject giffyObject = dataArray.getJSONObject(i);

            JSONObject imageObject = giffyObject.getJSONObject("images");
            JSONObject downSizeImageObject = imageObject.getJSONObject("downsized_still");
            GalleryItem item = new GalleryItem();

            item.setUrl(downSizeImageObject.getString("url"));
            item.setCaption(giffyObject.getString("slug"));
            item.setId(giffyObject.getString("id"));
            items.add(item);
        }
//
//        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
//        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

//        for (int i = 0; i < photoJsonArray.length(); i++) {
//            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
//
//            GalleryItem item = new GalleryItem();
//            item.setId(photoJsonObject.getString("id"));
//            item.setCaption(photoJsonObject.getString("title"));
//
//            if (!photoJsonObject.has("url_s")) {
//                continue;
//            }
//
//            item.setUrl(photoJsonObject.getString("url_s"));
//            items.add(item);
//        }
    }

}
