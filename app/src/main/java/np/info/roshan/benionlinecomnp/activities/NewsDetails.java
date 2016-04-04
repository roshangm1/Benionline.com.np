package np.info.roshan.benionlinecomnp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.fragments.News;
import np.info.roshan.benionlinecomnp.helper.SQLiteHandler;
import np.info.roshan.benionlinecomnp.helper.Singleton;

public class NewsDetails extends AppCompatActivity {

    private int fontSize = 9;
    private String mTitles, mDates, mImages, mContents, mCategories, mWriters, from;

    private int mIds;
    private boolean saved;
    private Menu menu;
    private WebSettings settings;
    SeekBar seekBar;
    private FloatingActionButton fabShare;
    private LinearLayout linearLayout;

    WebView webContent;
    TextView txtTitle, txtDate, txtAuthor, txtCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_details);

        from = getIntent().getStringExtra("from");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarNews);
        linearLayout = (LinearLayout) findViewById(R.id.newsDetailLayout);

        txtTitle = (TextView) findViewById(R.id.newsTitle);
        txtDate = (TextView) findViewById(R.id.newsDate);
        txtAuthor = (TextView) findViewById(R.id.newsWriter);
        txtCategory = (TextView) findViewById(R.id.newsCategory);
        webContent = (WebView) findViewById(R.id.newsContents);

        fabShare = (FloatingActionButton) findViewById(R.id.share);
        settings = webContent.getSettings();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("बेनीअनलाइन ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        seekBar = new SeekBar(this);

        if (from != null && from.equals("notification")) {
            mIds = getIntent().getIntExtra("post_id", 0);
            final MaterialDialog dialog = new MaterialDialog.Builder(this).title("Please wait").progress(true, 0).content("Extracting news").build();
            dialog.show();

            StringRequest request = new StringRequest("http://myagdikali.com/api/get_post/?post_id=" + mIds, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject post = object.getJSONObject("post");
                        JSONArray categories = post.getJSONArray("categories");
                        JSONObject author = post.getJSONObject("author");

                        mTitles = post.getString("title");
                        mDates = post.getString("date");
                        mContents = post.getString("content");
                        mWriters = author.getString("name");

                        JSONObject categoryObj = categories.getJSONObject(0);
                        mCategories = categoryObj.getString("title");

                        fillNews();
                        dialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    getSupportFragmentManager().beginTransaction().replace(R.id.contentHolder, News.newInstance(R.id.all_news)).commit();
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Singleton.getmInstance().addToRequestQueue(request);
        } else

        {
            mIds = getIntent().getExtras().getInt("newsId");
            mTitles = getIntent().getStringExtra("newsTitle");
            mDates = getIntent().getStringExtra("newsDate");
            mContents = getIntent().getStringExtra("newsContent");
            mImages = getIntent().getStringExtra("newsImage");
            mCategories = getIntent().getStringExtra("newsCategory");
            mWriters = getIntent().getStringExtra("newsAuthor");
            fillNews();

        }


        fabShare.setOnClickListener(new View.OnClickListener()

                                    {
                                        @Override
                                        public void onClick(View v) {
                                            fabShare.setRippleColor(Color.parseColor("#ff5722"));
                                            Intent intent = new Intent(Intent.ACTION_SEND);
                                            intent.setType("text/plain");

                                            intent.putExtra(Intent.EXTRA_SUBJECT, "http://www.myagdikali.com/" + mIds + ".html");
                                            intent.putExtra(Intent.EXTRA_TEXT, "http://www.myagdikali.com/" + mIds + ".html");
                                            startActivity(Intent.createChooser(intent, "How do you want to share ?"));
                                        }
                                    }

        );
    }

    private void fillNews() {
        linearLayout.setVisibility(View.VISIBLE);
        String html = mContents;
        Document doc = Jsoup.parse(html);
        Elements images = doc.getElementsByTag("img");
        for (Element image : images) {
            image.attr("alt", "");
            image.attr("style", "pointer-events:none; display:inline; height:auto; max-width:100%;");
            image.appendElement("br");

        }

        txtTitle.setText(mTitles);
        txtDate.setText(Singleton.convertDate(mDates));
        txtCategory.setText(mCategories);
        txtAuthor.setText(mWriters);

        assert webContent != null;
        webContent.loadDataWithBaseURL("", "<body style=\"text-align:justify\">" + doc.toString() + "</body>", "text/html", "UTF-8", "");

        fontSize = (int) getResources().getDimension(R.dimen.textSize);


        settings.setDefaultFontSize(getSharedPreferences("progressNow", MODE_PRIVATE).getInt("fontsize", fontSize));
        settings.setJavaScriptEnabled(true);

        seekBar.setMax(10);
        seekBar.setProgress(getSharedPreferences("progressNow", MODE_PRIVATE).getInt("progress", 2));

        settings.setLoadsImagesAutomatically(getSharedPreferences("settings", MODE_PRIVATE).getBoolean("loadImages", true));


        webContent.setFocusableInTouchMode(false);
        webContent.setFocusable(false);

        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.supportZoom();

        saved = isNewsSaved();
    }


    private void changeIcon() {
        if (saved) menu.findItem(R.id.action_fav).setIcon(R.drawable.fav_black);
        else menu.findItem(R.id.action_fav).setIcon(R.drawable.fav);


    }

    private boolean isNewsSaved() {
        SQLiteDatabase database = Singleton.getmInstance().getmDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM fav_news WHERE news_id=" + mIds + ";", null);
        return cursor.moveToNext();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();

        } else if (item.getItemId() == R.id.action_fav) {
            if (saved) {
                deleteFromDb();
                Snackbar.make(findViewById(R.id.newsDetailsCore), "फेबरोइट समाचार हटाइयो। ", Snackbar.LENGTH_SHORT).show();
            } else {
                storeFav();
                Snackbar.make(findViewById(R.id.newsDetailsCore), "समाचार फेब्रोईट गरियो।  ", Snackbar.LENGTH_SHORT).show();
            }

            changeIcon();


        } else if (item.getItemId() == R.id.action_font) {

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int prevProgress;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int dff = progress - prevProgress;
                    if (dff < 0) fontSize -= 2;
                    else fontSize += 2;
                    prevProgress = progress;


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    settings.setDefaultFontSize(fontSize);
                    getSharedPreferences("progressNow", MODE_PRIVATE).edit().putInt("progress", prevProgress).putInt("fontsize", fontSize).apply();

                }
            });
            new MaterialDialog.Builder(NewsDetails.this).title("Change font size").customView(seekBar, false).show();

        } else if (item.getItemId() == R.id.action_setting)
            startActivity(new Intent(NewsDetails.this, Settings.class));


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        this.menu = menu;
        if (from != null && from.equals("notification"))
            menu.getItem(0).setVisible(false);
        changeIcon();


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onResume() {
        super.onResume();
        settings.setDefaultFontSize(getSharedPreferences("progressNow", MODE_PRIVATE).getInt("fontsize", 9));
        seekBar.setProgress(getSharedPreferences("progressNow", MODE_PRIVATE).getInt("progress", 2));

    }

    private void storeFav() {
        SQLiteDatabase database = new SQLiteHandler(this).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.clear();
        values.put("news_id", mIds);
        values.put("title", mTitles);
        values.put("date", mDates);
        values.put("image", mImages);
        values.put("content", mContents);
        values.put("category", mCategories);
        values.put("author", mWriters);
        database.insert("fav_news", null, values);
        saved = true;
        database.close();
    }

    private void deleteFromDb() {

        SQLiteDatabase database = new SQLiteHandler(this).getWritableDatabase();
        database.execSQL("DELETE FROM fav_news WHERE news_id=" + mIds + ";");
        saved = false;
        database.close();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


}
