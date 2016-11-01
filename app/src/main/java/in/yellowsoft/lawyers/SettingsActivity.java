package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SettingsActivity extends Activity {
    ImageView back,load,user,books,close;
    TextView edit_profile,c_pass,c_lang,logout,submit_tv,cp_title,update_tv;
    LinearLayout ep_ll,c_pass_ll,cl_ll,logout_ll,submit_ll,cp_pop_ll,update_ll;
    EditText op,np,cp,fname,lname,uname,email,phone;
    ViewFlipper viewFlipper;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.settings_screen);
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper2);
        get_mem_details();
        back=(ImageView)findViewById(R.id.sett_back);
        load=(ImageView)findViewById(R.id.load_img_4);
        user=(ImageView)findViewById(R.id.lawyers_img_4);
        books=(ImageView)findViewById(R.id.books_image_4);
        close=(ImageView)findViewById(R.id.cp_pop_close);

        edit_profile=(TextView)findViewById(R.id.ef_tv);
//        edit_profile.setText(Settings.getword(this,"edit_profile"));
        c_pass=(TextView)findViewById(R.id.cp_tv);
//        c_pass.setText(Settings.getword(this,"change_password"));
        c_lang=(TextView)findViewById(R.id.cl_tv);
//        c_lang.setText(Settings.getword(this,"change_language"));
        logout=(TextView)findViewById(R.id.logout_tv);
//        logout.setText(Settings.getword(this,"logout"));
        submit_tv=(TextView)findViewById(R.id.cp_submit_tv);
//        submit_tv.setText(Settings.getword(this,"submit"));
        cp_title=(TextView)findViewById(R.id.cp_title);
//        cp_title.setText(Settings.getword(this,"change_password"));
        update_tv=(TextView)findViewById(R.id.ep_update_tv);
//        update_tv.setText(Settings.getword(this,"update"));

        ep_ll=(LinearLayout)findViewById(R.id.ef_ll);
        c_pass_ll=(LinearLayout)findViewById(R.id.cp_ll);
        cl_ll=(LinearLayout)findViewById(R.id.cl_ll);
        logout_ll=(LinearLayout)findViewById(R.id.logout_ll);
        submit_ll=(LinearLayout)findViewById(R.id.cp_submit_ll);
        cp_pop_ll=(LinearLayout)findViewById(R.id.cp_pop_ll);
        update_ll=(LinearLayout)findViewById(R.id.ep_update_ll);

        op=(EditText)findViewById(R.id.et_op);
//        op.setHint(Settings.getword(this,"old_pass"));
        np=(EditText)findViewById(R.id.et_np);
//        np.setHint(Settings.getword(this,"new_pass"));
        cp=(EditText)findViewById(R.id.et_cp);
//        cp.setHint(Settings.getword(this,"confirm_pass"));
        fname=(EditText)findViewById(R.id.ep_fname);
//        fname.setHint(Settings.getword(this,"fname"));
        lname=(EditText)findViewById(R.id.ep_lname);
//        lname.setHint(Settings.getword(this,"lname"));
        uname=(EditText)findViewById(R.id.ep_uname);
//        uname.setHint(Settings.getword(this,"uname"));
        email=(EditText)findViewById(R.id.ep_email);
//        email.setHint(Settings.getword(this,"email"));
        phone=(EditText)findViewById(R.id.ep_phone);
//        phone.setHint(Settings.getword(this,"phone"));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_pop_ll.setVisibility(View.GONE);
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), LawyersActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), BooksActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });

        ep_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(1);
            }
        });
        c_pass_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp_pop_ll.setVisibility(View.VISIBLE);

            }
        });
        cl_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), LanguageActvity.class);
                startActivity(mainIntent);
            }
        });
        logout_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Your logout", Toast.LENGTH_SHORT).show();
                Settings.setUserid(SettingsActivity.this, "-1", "");
            }
        });
        submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(op.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, "please enter Old password", Toast.LENGTH_SHORT).show();
                }else if(np.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, "please enter new password", Toast.LENGTH_SHORT).show();
                }else if(!cp.getText().toString().matches(np.getText().toString())) {
                    Toast.makeText(SettingsActivity.this, "please enter same password", Toast.LENGTH_SHORT).show();
                    cp.setText("");
                }else {
//                    cp_pop_ll.setVisibility(View.GONE);
                    change_password();
                }
            }
        });
        update_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fname.getText().toString().equals("")){
                    Toast.makeText(SettingsActivity.this, "please enter first name", Toast.LENGTH_SHORT).show();
                } else if(lname.getText().toString().equals("")){
                    Toast.makeText(SettingsActivity.this, "please enter last name", Toast.LENGTH_SHORT).show();
                }else if(uname.getText().toString().equals("")){
                    Toast.makeText(SettingsActivity.this, "please enter user name", Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, "please enter email id", Toast.LENGTH_SHORT).show();
                } else if (!email.getText().toString().matches(emailPattern)){
                    Toast.makeText(SettingsActivity.this, "Please Enter Valid Email id", Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, "please enter phone number", Toast.LENGTH_SHORT).show();
                }else {
//                    viewFlipper.setDisplayedChild(0);
                    edit_profile();
                }
            }
        });

    }
    @Override
    public void onBackPressed()
    {
        if(viewFlipper.getDisplayedChild()==1){
            viewFlipper.setDisplayedChild(0);
        }else {
            super.onBackPressed();
        }
    }
    public void change_password(){
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
//                    progressDialog.setMessage(Settings.getword(getActivity(),"please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.change_password_url;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Success")) {
                        String msg = jsonObject.getString("message");
                        Log.e("msg", msg);
                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        cp.setText("");
                        np.setText("");
                        cp_pop_ll.setVisibility(View.GONE);
                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("member_id",Settings.getUserid(SettingsActivity.this));
                params.put("opassword",op.getText().toString());
                params.put("password",np.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void edit_profile(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.edit_profile_url;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("res", response);
                if(progressDialog!=null)
                    progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Failed")) {
                        String msg = jsonObject.getString("message");

//                        alert.showAlertDialog(getActivity(), "Info", msg, true);
                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
//                        String mem_id=jsonObject.getString("member_id");
//                        String code=jsonObject.getString("code");
//                        Log.e("code", code);
                        String msg=jsonObject.getString("message");
//                                Settings.setUserid(getActivity(), mem_id, "name");
                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        viewFlipper.setDisplayedChild(0);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(progressDialog!=null)
                            progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fname",fname.getText().toString());
                params.put("lname",lname.getText().toString());
                params.put("username",uname.getText().toString());
                params.put("email",email.getText().toString());
                params.put("phone",phone.getText().toString());
                params.put("member_id",Settings.getUserid(SettingsActivity.this));
//                params.put("street",street);
//                params.put("house",house);
//                params.put("floor",floor);
//                params.put("flat",flat);
//                params.put("android_token",flat);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void get_mem_details() {
        String url = Settings.MemberDetails_url+"member_id="+Settings.getUserid(SettingsActivity.this);
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
                        fname.setText(sub.getString("fname"));
                        lname.setText(sub.getString("lname"));
                        uname.setText(sub.getString("username"));
                        email.setText(sub.getString("email"));
                        phone.setText(sub.getString("phone"));
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
                Toast.makeText(SettingsActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
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
