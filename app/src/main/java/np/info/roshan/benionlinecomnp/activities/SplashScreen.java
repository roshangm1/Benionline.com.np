package np.info.roshan.benionlinecomnp.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import np.info.roshan.benionlinecomnp.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                overridePendingTransition(R.anim.slide_in_up,R.anim.nothing);
                finish();
            }
        },1000);
    }
}
