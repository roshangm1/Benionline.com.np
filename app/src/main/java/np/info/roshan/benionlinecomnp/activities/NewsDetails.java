package np.info.roshan.benionlinecomnp.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.helper.SQLiteHandler;
import np.info.roshan.benionlinecomnp.helper.Singleton;

public class NewsDetails extends AppCompatActivity {

    private TextView txtTitle, txtDate, txtCategory, txtAuthor;
    private WebView webContent;
    private int fontSize;
    private Toolbar toolbar;
    private String mTitles;
    private String mDates;
    private String mImages;
    private String mContents;
    private String mCategories;
    private int mIds;
    private String mWriters;
    private boolean saved;
    private Menu menu;
    private  WebSettings settings;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_details);

        toolbar = (Toolbar) findViewById(R.id.toolbarNews);

        txtTitle = (TextView) findViewById(R.id.newsTitle);
        txtDate = (TextView) findViewById(R.id.newsDate);
        txtAuthor = (TextView) findViewById(R.id.newsWriter);
        txtCategory = (TextView) findViewById(R.id.newsCategory);

        webContent = (WebView) findViewById(R.id.newsContents);



        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        seekBar = new SeekBar(this);


        mIds = getIntent().getExtras().getInt("newsId");
        mTitles = getIntent().getStringExtra("newsTitle");
        mDates = getIntent().getStringExtra("newsDate");
        mContents = getIntent().getStringExtra("newsContent");
        mImages = getIntent().getStringExtra("newsImage");
        mCategories = getIntent().getStringExtra("newsCategory");
        mWriters = getIntent().getStringExtra("newsAuthor");

        String html = mContents;
        Document doc = Jsoup.parse(html);
        Elements images = doc.getElementsByTag("img");
        for (Element image : images) {
            image.attr("alt", "");
            image.attr("style", "pointer-events:none; display:inline; height:auto; max-width:100%;");
            image.appendElement("br");


            // image.attr("onerror","this.src='file:///android_res/drawable/" + "header'" );

        }

        Log.v("Data::", doc.toString());
        Log.v("Data::", html.toString());

        txtTitle.setText(mTitles);
        txtDate.setText(Singleton.convertDate(mDates));
        txtCategory.setText(mCategories);
        txtAuthor.setText(mWriters);


        webContent.loadDataWithBaseURL("", "<body style=\"text-align:justify\">" + doc.toString() + "</body>", mimeType, encoding, "");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("बेनीअनलाइन ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fontSize = (int) getResources().getDimension(R.dimen.textSize);

        settings = webContent.getSettings();

        settings.setDefaultFontSize(getSharedPreferences("progressNow",MODE_PRIVATE).getInt("fontsize",fontSize));
        settings.setJavaScriptEnabled(true);

        seekBar.setMax(10);
        seekBar.setProgress(getSharedPreferences("progressNow",MODE_PRIVATE).getInt("progress",2));


        webContent.setFocusableInTouchMode(false);
        webContent.setFocusable(false);

        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.supportZoom();

        saved = isNewsSaved();
    }

    private void changeIcon() {
        if(saved)
            menu.findItem(R.id.action_fav).setIcon(R.drawable.fav_black);
        else
            menu.findItem(R.id.action_fav).setIcon(R.drawable.fav);


    }
    private boolean isNewsSaved() {
        SQLiteDatabase database = new SQLiteHandler(this).getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM fav_news WHERE news_id=" + mIds + ";",null);
        if(cursor.moveToNext())
            return true;
        return false;


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();

        } else if (item.getItemId() == R.id.action_fav) {
            if(saved) {
                deleteFromDb();
                Snackbar.make(findViewById(R.id.newsDetailsCore), "फेबरोइट समाचार हटाइयो। ", Snackbar.LENGTH_SHORT).show();
            } else {
                storeFav();
                Snackbar.make(findViewById(R.id.newsDetailsCore), "समाचार फेब्रोईट गरियो।  ", Snackbar.LENGTH_SHORT).show();
            }

            changeIcon();

        } else if (item.getItemId() == R.id.action_night) {
            Toast.makeText(this, "NightMode //Todo", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId()==R.id.action_font) {

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int prevProgress;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int dff = progress-prevProgress;
                    if(dff<0)
                        fontSize-=2;
                    else
                        fontSize+=2;
                    prevProgress = progress;


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    settings.setDefaultFontSize(fontSize);
                    getSharedPreferences("progressNow",MODE_PRIVATE).edit().putInt("progress",prevProgress).putInt("fontsize",fontSize);

                }
            });
            new MaterialDialog.Builder(NewsDetails.this)
                    .title("Change font size")
                    .customView(seekBar,false).show();

        }

        else if (item.getItemId()==R.id.action_setting)
            startActivity(new Intent(NewsDetails.this,Settings.class));



        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);

        this.menu  = menu;
        changeIcon();

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onResume() {
        super.onResume();
        settings.setDefaultFontSize(getSharedPreferences("progressNow",MODE_PRIVATE).getInt("fontsize",fontSize));
        seekBar.setProgress(getSharedPreferences("progressNow",MODE_PRIVATE).getInt("progress",2));

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


}
