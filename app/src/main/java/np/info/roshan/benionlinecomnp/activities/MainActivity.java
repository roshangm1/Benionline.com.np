package np.info.roshan.benionlinecomnp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.fragments.News;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private int lastClicked;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentHolder,News.newInstance(R.id.all_news)).commit();
        setTitle("ताजा समाचार ");

        navigationView = (NavigationView) findViewById(R.id.navView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.Open,R.string.Close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
            }
        };

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int currentId = item.getItemId();
                String menuTitle = (String) item.getTitle();
                mDrawerLayout.closeDrawers();
                if(lastClicked != currentId) {
                    navigate(currentId,menuTitle);
                    lastClicked=currentId;
                    return true;
                }
                return false;
            }
        });

    }

    private void navigate(int currentId,String menuTitle) {
       if(currentId == R.id.aboutus) {
           Intent intent = new Intent(MainActivity.this,AboutUs.class);
           startActivity(intent);
       } else if(currentId==R.id.settings) {
           // Load settings activity
           Intent intent = new Intent(MainActivity.this,Settings.class);
           startActivity(intent);
       } else if(currentId==R.id.rateus) {
           new MaterialDialog.Builder(MainActivity.this)
                   .title("रेट गर्नुहोस् ")
                   .content("हामीलाई ५ तारा रेट गरेर एस विकासको लागि सहयोग गर्नुहोस।  ")
                   .positiveText("हुन्छ ")
                   .negativeText("पछि गर्छु ")
                   .onPositive(new MaterialDialog.SingleButtonCallback() {
                       @Override
                       public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                           //todo Google Playstore link
                       }


                   })
                   .build()
                   .show();
       } else {

           getSupportActionBar().setTitle(menuTitle);
           getSupportFragmentManager().beginTransaction().replace(R.id.contentHolder, News.newInstance(currentId)).commit();

       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (News.tableName.equals("fav_news"))
            getSupportFragmentManager().beginTransaction().replace(R.id.contentHolder, News.newInstance(R.id.read_later)).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            mDrawerLayout.openDrawer(navigationView);
        }
        return true;
    }
}
