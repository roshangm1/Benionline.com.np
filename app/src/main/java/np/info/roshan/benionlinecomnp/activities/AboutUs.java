package np.info.roshan.benionlinecomnp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import np.info.roshan.benionlinecomnp.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarAbout));
        setTitle("हाम्रो बारे");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String mimeType = "text/html";
        final String encoding = "UTF-16";

        String aboutContent = "<body style = \"text-align:justify\"><p>बेनीअनलाइन सन 8/29/2006 देखी बेनीबजारबाट संचालित वेवसाइट हो । राष्ट्रिय समाचार पत्रिका तथा वेवहरुमा म्याग्देली/धौलागिरी अंचलका विशेष समाचारहरुले मात्र स्थान पाउने र अन्य समाचारहरुको चर्चा कम हुनु तथा प्रवासमा रहेका धौलागिरीबासीहरुका लागि गाँउघरका समाचारहरु पस्कनका लागि बेनीअनलाइन सुरु भएको थियो । यसको सुरुवात बेनीबजारका केहि युवाहरुको प्रयासमा भएको हो र हालसम्म बेनीअनलाइन टिम तपाईहरु सामु स्थानिय हालखवरहरु पस्किरहेको छ ।</p>\n" +
                "<p>बेनीअनलाइन एउटा नाफारहित सेवा हो जसलाई संचालन गर्नका लागि आर्थिक लगायतका विभिन्न समस्याहरु आइपर्ने गर्छन । हामीहरु ति समस्याहरुलाई झेल्दै यसलाई निरन्तरता दिइरहने छौं । यदि तपाईपनि हामीलाई सहयोग गर्न चाहनुहुन्छ भने हाम्रो इमेल ठेगानामा सम्पर्क राख्न सक्नुहुनेछ ।</p>\n" +
                "<p><b>हाम्रो टिम</p> Kramasha: Hari Karki, Hari Giri, Harikrishna Gautam</body>";

        WebView webView = (WebView) findViewById(R.id.about_web);
        webView.loadDataWithBaseURL("file:///android_asset",aboutContent,mimeType,encoding,"");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            super.onBackPressed();

        return true;
    }
}
