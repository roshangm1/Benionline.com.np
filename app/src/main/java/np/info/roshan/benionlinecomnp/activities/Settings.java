package np.info.roshan.benionlinecomnp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import np.info.roshan.benionlinecomnp.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CheckBox loadImages = (CheckBox) findViewById(R.id.load_images);
        loadImages.setChecked(getSharedPreferences("settings",MODE_PRIVATE).getBoolean("loadImages",true));

        loadImages.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean("loadImages",isChecked).apply();
            }
        });

        setSupportActionBar((Toolbar) findViewById(R.id.toolbarSettings));
        setTitle("सेटिङ्ग ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }
}
