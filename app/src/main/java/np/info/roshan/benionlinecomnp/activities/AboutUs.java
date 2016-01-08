package np.info.roshan.benionlinecomnp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import mehdi.sakout.fancybuttons.FancyButton;
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

        String aboutContent = "<body style=\"text-align:justify;\"><p>बेनीअनलाइन सन 8/29/2006 देखी बेनीबजारबाट संचालित वेवसाइट हो । राष्ट्रिय समाचार पत्रिका तथा वेवहरुमा म्याग्देली/धौलागिरी अंचलका विशेष समाचारहरुले मात्र स्थान पाउने र अन्य समाचारहरुको चर्चा कम हुनु तथा प्रवासमा रहेका धौलागिरीबासीहरुका लागि गाँउघरका समाचारहरु पस्कनका लागि बेनीअनलाइन सुरु भएको थियो । यसको सुरुवात बेनीबजारका केहि युवाहरुको प्रयासमा भएको हो र हालसम्म बेनीअनलाइन टिम तपाईहरु सामु स्थानिय हालखवरहरु पस्किरहेको छ ।</p>\n" +
                "<p>बेनीअनलाइन एउटा नाफारहित सेवा हो जसलाई संचालन गर्नका लागि आर्थिक लगायतका विभिन्न समस्याहरु आइपर्ने गर्छन । हामीहरु ति समस्याहरुलाई झेल्दै यसलाई निरन्तरता दिइरहने छौं । यदि तपाईपनि हामीलाई सहयोग गर्न चाहनुहुन्छ भने हाम्रो इमेल ठेगानामा सम्पर्क राख्न सक्नुहुनेछ ।</p> \n" +
                "<b><u>हाम्रो टिम:</u> <br><br>" +
                "हरि कार्की <br>" +
                "हरि गिरि <br>" +
                "हरि कृष्ण गौतम </body>";


        TextView aboutUs = (TextView) findViewById(R.id.about_web);

        aboutUs.setText(Html.fromHtml(aboutContent));

        FancyButton facebook = (FancyButton) findViewById(R.id.facebookButton);
        FancyButton twitter = (FancyButton) findViewById(R.id.twitterButton);
        FancyButton web = (FancyButton) findViewById(R.id.webButton);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =null;
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/160590230974977"));
                } catch (Exception e) {
                    i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/brainants"));
                } finally {
                    startActivity(i);
                }

            }
        });


        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                try {
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    i = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=4720415052"));
                } catch (Exception e) {
                    i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/brainants"));
                } finally {
                   startActivity(i);
                }

            }
        });


        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.brainants.com/")));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            super.onBackPressed();

        return true;
    }
}
