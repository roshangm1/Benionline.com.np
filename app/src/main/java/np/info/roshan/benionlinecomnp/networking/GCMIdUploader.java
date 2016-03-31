package np.info.roshan.benionlinecomnp.networking;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.helper.MyApplication;
import np.info.roshan.benionlinecomnp.helper.Singleton;

public class GCMIdUploader {
    String token;

    public void RegisterApp() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                InstanceID instanceID = InstanceID.getInstance(MyApplication.getContext());
                try {
                    token = instanceID.getToken(MyApplication.getContext().getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE);

                    Singleton.getmInstance().addToRequestQueue(
                            (new StringRequest(Request.Method.POST, "http://myagdikali.com/pnfw/register/", new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    MyApplication.getContext().getSharedPreferences("status", Context.MODE_PRIVATE).edit().putBoolean("uploaded",true).apply();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }) {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("token", token);
                                    params.put("os", "Android");
                                    return params;
                                }
                            }));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }
        }.execute();
    }
}
