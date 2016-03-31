package np.info.roshan.benionlinecomnp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Singleton {
    public static final String TAG = Singleton.class.getSimpleName();
    private RequestQueue requestQueue;
    private final SQLiteHandler mDatabase;


    public Singleton() {
        mDatabase= new SQLiteHandler(MyApplication.getContext());
    }

    public static int isConnected() {
        ConnectivityManager cm = (ConnectivityManager) MyApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                return 1;
            else
                return 2;
        } else
            return 0;
    }

    public static Singleton getmInstance() {
        Singleton mInstance = new Singleton();
        return mInstance;
    }

    public SQLiteDatabase getmDatabase() {
        return mDatabase.getWritableDatabase();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(MyApplication.getContext());
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
