package np.info.roshan.benionlinecomnp.helper;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import np.info.roshan.benionlinecomnp.networking.NewsDownloader;

public class TimeAlarm extends WakefulBroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("AppTest", "Hello done");

        NewsDownloader downloader = new NewsDownloader("http://myagdikali.com/api/get_category_posts/?slug=news&count=20", "all_news");
        downloader.doInBackground();
    }
}