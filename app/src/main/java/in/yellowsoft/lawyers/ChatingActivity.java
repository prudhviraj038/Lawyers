package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatingActivity extends Activity {
    ListView listView;
    EditText et_message;
    ChatingAdapter chatingAdapter;
    ImageView back,send_btn;
    ArrayList<Chats> chats;
    String law_id,law_im,law_n;
    CircleImageView law_img;
    TextView law_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.chating_screen);
        law_id=getIntent().getStringExtra("law_id");
        law_im=getIntent().getStringExtra("law_img");
        law_n=getIntent().getStringExtra("law_name");
        chats=new ArrayList<>();
        chat_list();
        et_message=(EditText)findViewById(R.id.et_chat_message);
        et_message.setHint(Settings.getword(this,"type_here"));
        back=(ImageView)findViewById(R.id.chating_back);
        law_img=(CircleImageView)findViewById(R.id.chat_law_img);
        Picasso.with(this).load(law_im).into(law_img);
        law_name=(TextView)findViewById(R.id.chat_law_name);
        law_name.setText(law_n);
        send_btn=(ImageView)findViewById(R.id.msg_send_img);
        listView=(ListView)findViewById(R.id.chating_list);
        chatingAdapter=new ChatingAdapter(this,chats);
        listView.setAdapter(chatingAdapter);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_message.getText().toString().equals(""))
                    Toast.makeText(ChatingActivity.this, Settings.getword(ChatingActivity.this,"empty_msg"), Toast.LENGTH_SHORT).show();
                else
                    add_chat();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    final Handler h = new Handler();
    final int delay = 1000*30; //milliseconds

    final Runnable r = new Runnable() {
        @Override
        public void run() {
            Log.e("time", "ticked");
            h.postDelayed(this, delay);
            // tabclicked(selected,true);
            chat_list();
        }
    };

    private void set_refresh_timer(){

        h.postDelayed(r, delay);
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        h.removeCallbacks(r);
        Log.e("timer", "removed");
    }

    @Override
    public void onResume() {
        super.onResume(); // Always call the superclass method first

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
        h.removeCallbacks(r);
        h.postDelayed(r, delay);
        Log.e("timer", "added");
    }
    public  void add_chat(){
        String url = null;
        try {
            url = Settings.Add_chat_url + "send_id="+ Settings.getUserid(ChatingActivity.this)+
                    "&receive_id="+law_id+"&message="+ URLEncoder.encode(et_message.getText().toString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait....");
////        progressDialog.setMessage(Settings.getword(this, "please_wait"));
//        progressDialog.show();
//        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
//                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Failure")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(ChatingActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg=jsonObject.getString("message");
//                        Toast.makeText(ChatingActivity.this, msg, Toast.LENGTH_SHORT).show();
                        et_message.setText("");
                        chat_list();
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
                Toast.makeText(ChatingActivity.this, Settings.getword(ChatingActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public  void chat_list(){
        String url = Settings.Chat_list_url + "send_id="+ Settings.getUserid(ChatingActivity.this)+
                "&receive_id="+law_id;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(ChatingActivity.this,"please_wait"));
//        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                chats.clear();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        Chats pack=new Chats(sub);
                        chats.add(pack);
                    }

                    chatingAdapter.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(ChatingActivity.this,Settings.getword(ChatingActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(chatingAdapter.getCount() - 1);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
