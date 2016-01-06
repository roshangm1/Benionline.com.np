package np.info.roshan.benionlinecomnp.helper;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Singleton extends Application {
    public static final String TAG = Singleton.class.getSimpleName();
    private RequestQueue requestQueue;
    private static Singleton mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }

    public static int isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getmInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo!=null) {
            if(networkInfo.getType()==ConnectivityManager.TYPE_MOBILE)
                return 1;
            else
                return 2;
        } else
            return 0;
    }

    public static synchronized Singleton getmInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);

        getRequestQueue().add(req);
    }


    public static CharSequence convertDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            Date finalDate = simpleDateFormat.parse(date);
            return DateUtils.getRelativeTimeSpanString(finalDate.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE);


        } catch (ParseException e) {
            e.printStackTrace();
            return "Unknown Time";
        }
    }

}
