package in.yellowsoft.lawyers;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Handler;

/**
 * Created by HP on 08-Nov-16.
 */
public class MyTestService extends IntentService {
    String m_id;
    // Must create a default constructor
    public MyTestService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate(); // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // This describes what will happen when service is triggered
        m_id=intent.getStringExtra("foo");

        h.postDelayed(r, delay);
    }

    final android.os.Handler h = new android.os.Handler();
    final int delay = 1000*30; //milliseconds

    final Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.e("time", "ticked");
            h.postDelayed(this, delay);
            get_status();

        }
    };
    private void get_status() {
        Log.e("m_iddd",m_id);
        String url = Settings.Add_Lawyer_status_url+"member_id="+m_id;
        Log.e("url--->", url);
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait....");
//        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
//                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Success")) {
                        String msg = jsonObject.getString("message");
//                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }else{
                        String msg = jsonObject.getString("message");
//                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
//                Toast.makeText(TimelineActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
}