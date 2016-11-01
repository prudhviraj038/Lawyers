package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class PackagesActivity extends Activity {
    LinearLayout bronze_ll,gold_ll,platinum_ll;
    ImageView back;
    ListView listView;
    PackagesAdapter packagesAdapter;
    ArrayList<Packages> packages;
    String law_sign_email,pack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.packages_screenn);
        packages=new ArrayList<>();
        get_packages();
        law_sign_email=getIntent().getStringExtra("email");
        listView=(ListView)findViewById(R.id.packages_list);
        packagesAdapter=new PackagesAdapter(this,packages);
        listView.setAdapter(packagesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pack=packages.get(position).id;
                Intent payment = new Intent(PackagesActivity.this, PaymentActivity.class);
                payment.putExtra("pack_price", packages.get(position).price);
                startActivityForResult(payment, 7);
            }
        });
//        bronze_ll=(LinearLayout)findViewById(R.id.bronze_ll);
//        gold_ll=(LinearLayout)findViewById(R.id.gold_ll);
//        platinum_ll=(LinearLayout)findViewById(R.id.platinum_ll);
        back=(ImageView)findViewById(R.id.pack_list_back);
//        bronze_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(getApplicationContext(), LawyersActivity.class);
//                startActivity(mainIntent);
//            }
//        });
//        gold_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(getApplicationContext(), LawyersActivity.class);
//                startActivity(mainIntent);
//            }
//        });
//        platinum_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent mainIntent = new Intent(getApplicationContext(), LawyersActivity.class);
//                startActivity(mainIntent);
//            }
//        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("pay--->", "Payment success");
        if (requestCode == 7) {
            if(data.getStringExtra("msg").equals("OK"))
            {
                Toast.makeText(PackagesActivity.this, "Payment success", Toast.LENGTH_SHORT).show();

                signup_lawyer();
            }
            else
                Toast.makeText(PackagesActivity.this,Settings.getword(PackagesActivity.this,"pay_failed"),Toast.LENGTH_SHORT).show();
        }
    }
    private void get_packages() {
        String url = Settings.Packages_url;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        Packages pack=new Packages(sub);
                        packages.add(pack);
                    }

                    packagesAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(PackagesActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    String url;
    public  void signup_lawyer(){
        try {
            url = Settings.law_Verify_url + "email="+ URLEncoder.encode(law_sign_email, "utf-8")+
                    "&package_id="+ URLEncoder.encode(pack, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
//        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Failure")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(PackagesActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg=jsonObject.getString("message");
//                        String name=jsonObject.getString("name");
//                        Settings.setUserid(LoginActivity.this, mem_id, name);
                        Toast.makeText(PackagesActivity.this, msg, Toast.LENGTH_SHORT).show();
                        finish();

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
                Toast.makeText(PackagesActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

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
