package np.info.roshan.benionlinecomnp.helper;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.iid.InstanceIDListenerService;

import java.io.IOException;

import np.info.roshan.benionlinecomnp.R;
import np.info.roshan.benionlinecomnp.networking.GCMIdUploader;

/**
 * Created by roshan on 3/30/16.
 */
public class GCMIdListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String token;
                InstanceID instanceID = InstanceID.getInstance(GCMIdListenerService.this);
                try {
                    token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
                } catch (IOException e) {
                    token = "Failed" + e;
                }
                new GCMIdUploader().RegisterApp();
                return token;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(GCMIdListenerService.this,s,Toast.LENGTH_SHORT).show();
            }
        }.execute();


    }
}
