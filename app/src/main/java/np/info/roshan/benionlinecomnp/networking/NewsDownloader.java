package np.info.roshan.benionlinecomnp.networking;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import np.info.roshan.benionlinecomnp.helper.Singleton;

public class NewsDownloader {
    String url, tableName;
    private ArrayList<String> mTitles = new ArrayList<>(), mDates = new ArrayList<>(), mImages = new ArrayList<>(), mContents = new ArrayList<>(), mCategories = new ArrayList<>(), mWriters = new ArrayList<>();

    private ArrayList<Integer> mIds = new ArrayList<>();

    private ClickListener listener;

    public NewsDownloader(String url, String tableName) {
        this.url = url;
        this.tableName = tableName;
    }

    public void doInBackground() {

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mTitles.clear();
                mDates.clear();
                mImages.clear();
                mContents.clear();
                mCategories.clear();
                mWriters.clear();
                mIds.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("ok")) {

                        JSONArray posts = jsonObject.getJSONArray("posts");


                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject object = posts.getJSONObject(i);

                            if (object.getString("status").equals("publish")) {
                                JSONArray images = object.getJSONArray("attachments");
                                JSONArray categories = object.getJSONArray("categories");
                                JSONObject author = object.getJSONObject("author");

                                mIds.add(object.getInt("id"));
                                mTitles.add(object.getString("title"));
                                mDates.add(object.getString("date"));
                                mContents.add(object.getString("content"));
                                mWriters.add(author.getString("name"));

                                try {
                                    JSONObject imageObject = images.getJSONObject(0);
                                    mImages.add(imageObject.getString("url"));

                                    JSONObject categoryObj = categories.getJSONObject(0);
                                    mCategories.add(categoryObj.getString("title"));
                                } catch (Exception e) {
                                    mImages.add("");
                                    mCategories.add("समाचार ");
                                }
                            }
                        }
                    }

                    storeToDb();

                } catch (JSONException e) {
                    listener.onTaskCompleted(false);
                }
            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onTaskCompleted(false);
            }
        }

        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Singleton.getmInstance().addToRequestQueue(stringRequest);
    }

    private void storeToDb() {
        SQLiteDatabase database = Singleton.getmInstance().getmDatabase();
        database.delete(tableName, null, null);
        ContentValues content = new ContentValues();
        for (int i = 0; i < mTitles.size(); i++) {
            content.clear();
            content.put("id", mIds.get(i));
            content.put("title", mTitles.get(i));
            content.put("date", mDates.get(i));
            content.put("image", mImages.get(i));
            content.put("content", mContents.get(i));
            content.put("category", mCategories.get(i));
            content.put("author", mWriters.get(i));
            database.insert(tableName, null, content);

        }
        listener.onTaskCompleted(true);
    }

    public void setOnTaskCompleteListener(ClickListener listener) {
        this.listener = listener;
    }

    public interface ClickListener {
        void onTaskCompleted(boolean success);
    }
}
