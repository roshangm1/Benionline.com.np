package np.info.roshan.benionlinecomnp.activities;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

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
        setTitle("सबै समाचार");

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
        getSupportActionBar().setTitle(menuTitle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contentHolder,News.newInstance(currentId)).commit();
        mDrawerLayout.closeDrawers();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            mDrawerLayout.openDrawer(navigationView);
        }
        return true;
    }
}
