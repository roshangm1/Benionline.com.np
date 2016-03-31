package np.info.roshan.benionlinecomnp.fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.adapters.NewsAdapter;
import np.info.roshan.benionlinecomnp.helper.Singleton;
import np.info.roshan.benionlinecomnp.networking.GCMIdUploader;

/**
 * A simple {@link Fragment} subclass.
 */
public class News extends Fragment {

    private int itemId;
    private RecyclerView recyclerView;
    private Context context;
    private String url;
    private final ArrayList<String> mTitles = new ArrayList<>();
    private final ArrayList<String> mDates = new ArrayList<>();
    private final ArrayList<String> mImages = new ArrayList<>();
    private final ArrayList<String> mContents = new ArrayList<>();
    private final ArrayList<String> mCategories = new ArrayList<>();
    private final ArrayList<String> mWriters = new ArrayList<>();
    private final ArrayList<Integer> mIds = new ArrayList<>();

    public static String tableName;
    private SwipeRefreshLayout swipeNews;
    private ProgressBar progressBar;
    private LinearLayout errorMsg;
    private View mainView;
    private TextView noFavNews;

    public News() {
        // Required empty public constructor
    }

    public static News newInstance(int id) {
        News news = new News();
        Bundle bundle = new Bundle();
        bundle.putInt("itemId", id);
        news.setArguments(bundle);
        return news;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);
        itemId = getArguments().getInt("itemId");
        return rootView;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (itemId) {
            case R.id.all_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=news&count=20";
                tableName = "all_news";
                break;
            case R.id.national_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=national-news&count=20";
                tableName = "national_news";
                break;
            case R.id.myagdi_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=myagdeli-news&count=20";
                tableName = "myagdi_news";
                break;
            case R.id.parbat_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=parbat-news&count=20";
                tableName = "parbat_news";
                break;
            case R.id.baglung_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=baglunge-news&count=20";
                tableName = "baglung_news";
                break;
            case R.id.mustang_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=mustangi-news&count=20";
                tableName = "mustang_news";
                break;
            case R.id.foreign_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=%E0%A4%AA%E0%A5%8D%E0%A4%B0%E0%A4%B5%E0%A4%BE%E0%A4%B8&count=20";
                tableName = "foreign_news";
                break;
            case R.id.sport_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=sport-news&count=20";
                tableName = "sport_news";
                break;

            case R.id.eco_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=%E0%A4%85%E0%A4%BE%E0%A4%B0%E0%A5%8D%E0%A4%A5%E0%A4%BF%E0%A4%95&count=20";
                tableName = "eco_news";
                break;
            case R.id.misc_news:
                url = "http://myagdikali.com/api/get_category_posts/?slug=%e0%a4%b5%e0%a4%bf%e0%a4%b5%e0%a4%bf%e0%a4%a7&count=20";
                tableName = "misc_news";
                break;
            case R.id.read_later:
                tableName = "fav_news";
                break;

        }

        if (tableName.equals("fav_news")) loadFavouritePosts();
        else loadFromDatabase();


    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recy);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbarFb);
        noFavNews = (TextView) view.findViewById(R.id.noFav);

        errorMsg = (LinearLayout) view.findViewById(R.id.errorMessage);
        this.mainView = view;

        swipeNews = (SwipeRefreshLayout) view.findViewById(R.id.swipeNews);

        swipeNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                fetchFromInternet(false);
                swipeNews.setRefreshing(true);
            }
        });

        errorMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchFromInternet(true);

            }
        });

        context = getActivity();


    }


    private void fetchFromInternet(final boolean isFirst) {
        boolean uploaded = context.getSharedPreferences("status",Context.MODE_PRIVATE).getBoolean("uploaded",false);

        if(!uploaded)
            new GCMIdUploader().RegisterApp();

        if (isFirst) {

            if (Singleton.isConnected() != 0) {
                errorMsg.setVisibility(View.GONE);
            }
            swipeNews.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
        Snackbar.make(mainView.findViewById(R.id.newsLayout), "कृपया पर्खनु होस् !! नयाँ समाचार अपडेट गरिदै छ ", Snackbar.LENGTH_LONG).show();


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
                    loadFromDatabase();
                    Snackbar.make(mainView.findViewById(R.id.newsLayout), "समाचार अपडेट गरियो। ", Snackbar.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "No data error ", Toast.LENGTH_SHORT).show();
                }


            }
        }

                , new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeNews.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                if (isFirst) errorMsg.setVisibility(View.VISIBLE);
                Snackbar.make(mainView, "इन्टरनेट छैन।  फेरी प्रयास गर्नु होस्। । । ", Snackbar.LENGTH_SHORT).show();
                Log.e("ERror///", error.toString());
            }
        }

        );

        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Singleton.getmInstance().addToRequestQueue(stringRequest);
    }


    private void fillRecy() {

        swipeNews.setVisibility(View.VISIBLE);
        swipeNews.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        errorMsg.setVisibility(View.GONE);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        NewsAdapter adapter = new NewsAdapter(context, mIds, mTitles, mDates, mImages, mContents, mCategories, mWriters);
        recyclerView.setAdapter(adapter);


    }

    private void loadFavouritePosts() {
        SQLiteDatabase database = Singleton.getmInstance().getmDatabase();
        Cursor cursor = database.query("fav_news", new String[]{"news_id", "title", "date", "image", "content", "category", "author"}, null, null, null, null, null);
        int i = 0;

        mIds.clear();
        mTitles.clear();
        mDates.clear();
        mImages.clear();
        mContents.clear();
        mCategories.clear();
        mWriters.clear();

        while (cursor.moveToNext()) {
            i++;
            mIds.add(cursor.getInt(cursor.getColumnIndex("news_id")));
            mTitles.add(cursor.getString(cursor.getColumnIndex("title")));
            mDates.add(cursor.getString(cursor.getColumnIndex("date")));
            mImages.add(cursor.getString(cursor.getColumnIndex("image")));
            mContents.add(cursor.getString(cursor.getColumnIndex("content")));
            mCategories.add(cursor.getString(cursor.getColumnIndex("category")));
            mWriters.add(cursor.getString(cursor.getColumnIndex("author")));
        }
        if (tableName.equals("fav_news")) {
            progressBar.setVisibility(View.GONE);
            swipeNews.setEnabled(false);

        }

        if (i == 0) {
            if (tableName.equals("fav_news")) {
                progressBar.setVisibility(View.GONE);
                swipeNews.setEnabled(false);
                noFavNews.setVisibility(View.VISIBLE);

            }
        } else {
            fillRecy();
        }

        cursor.close();
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

    }

    private void loadFromDatabase() {

        SQLiteDatabase database = Singleton.getmInstance().getmDatabase();
        Cursor cursor = database.query(tableName, new String[]{"id", "title", "date", "image", "content", "category", "author"}, null, null, null, null, null);
        int i = 0;
        mIds.clear();
        mTitles.clear();
        mDates.clear();
        mImages.clear();
        mContents.clear();
        mCategories.clear();
        mWriters.clear();
        while (cursor.moveToNext()) {
            i++;
            mIds.add(cursor.getInt(cursor.getColumnIndex("id")));
            mTitles.add(cursor.getString(cursor.getColumnIndex("title")));
            mDates.add(cursor.getString(cursor.getColumnIndex("date")));
            mImages.add(cursor.getString(cursor.getColumnIndex("image")));
            mContents.add(cursor.getString(cursor.getColumnIndex("content")));
            mCategories.add(cursor.getString(cursor.getColumnIndex("category")));
            mWriters.add(cursor.getString(cursor.getColumnIndex("author")));
        }

        if (i == 0) {
            if (Singleton.isConnected() == 0) {
                errorMsg.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            } else {

                fetchFromInternet(true);
            }
        } else {

            fillRecy();


        }
        cursor.close();

    }
}
