package np.info.roshan.benionlinecomnp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.helper.Singleton;

public class NewsDetails extends AppCompatActivity {

    private TextView txtTitle, txtDate,txtCategory,txtAuthor;
    private WebView webContent;
    private int id;
    private Toolbar toolbar;
    private ArrayList<String> mTitles = new ArrayList<>(),
            mDates = new ArrayList<>(),
            mImages = new ArrayList<>(),
            mContents = new ArrayList<>(),
            mCategories = new ArrayList<>(),
            mWriters = new ArrayList<>();
    private int fontSize;

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

        id = getIntent().getIntExtra("id", 0);


        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        mTitles = getIntent().getStringArrayListExtra("newsTitle");
        mDates = getIntent().getStringArrayListExtra("newsDate");
        mContents = getIntent().getStringArrayListExtra("newsContent");
        mImages = getIntent().getStringArrayListExtra("newsImage");
        mCategories = getIntent().getStringArrayListExtra("newsCategory");
        mWriters = getIntent().getStringArrayListExtra("newsAuthor");

        String html = mContents.get(id);
        Document doc = Jsoup.parse(html);
        Elements images = doc.getElementsByTag("img");
        for(Element image : images ) {
            image.attr("style","pointer-events:none; display:inline; height:auto; max-width:100%;");
            image.appendElement("br");

           // image.attr("onerror","this.src='file:///android_res/drawable/" + "placeholder.png'" );

        }

        Log.v("Data::",doc.toString());
        Log.v("Data::",html.toString());

        txtTitle.setText(mTitles.get(id));
        txtDate.setText(Singleton.convertDate(mDates.get(id)));
        txtCategory.setText(mCategories.get(id));
        txtAuthor.setText(mWriters.get(id));


        webContent.loadDataWithBaseURL("", "<body style=\"text-align:justify\">" + doc.toString() + "</body>", mimeType, encoding, "");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("बेनीअनलाइन ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fontSize = (int) getResources().getDimension(R.dimen.textSize);
        WebSettings settings = webContent.getSettings();


        settings.setDefaultFontSize(fontSize);
        settings.setJavaScriptEnabled(true);


        //webContent.setFocusableInTouchMode(false);
        //webContent.setFocusable(false);

        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.supportZoom();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else if(item.getItemId()==R.id.action_fav) {
            Toast.makeText(this,"Read this later //Todo",Toast.LENGTH_SHORT).show();
        } else if(item.getItemId()==R.id.action_night) {
            Toast.makeText(this,"NightMode //Todo",Toast.LENGTH_SHORT).show();
            setTheme(R.style.NightTheme);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
