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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {
    EditText login_uname,login_pass,fp_et;
    EditText reg_fname,reg_lname,reg_uname,reg_email,reg_pass,reg_phone,law_sign_email;
    LinearLayout login,signup_user,signup_law,register,fp_pop_ll,submit_ll,skip_ll,lan_ll,signup_pop_ll,sign_submit_ll,r_l_ll,r_u_ll;
    TextView login_tv,register_tv,signup_law_tv,signup_user_tv,fp_title,submit_tv,forgot_password,skip_tv,lan_tv,sign_submit_tv,
    radio_law_tv,radio_user_tv;
    ImageView back,close,sign_close,back2,radio_law_img,radio_user_img;
    ViewFlipper viewFlipper;
    String type="";
    Animation animation,animation2;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.login_screen);
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper);
        back=(ImageView)findViewById(R.id.login_back);
        back2=(ImageView)findViewById(R.id.back_sign2);
        close=(ImageView)findViewById(R.id.fp_close);
        sign_close=(ImageView)findViewById(R.id.signup_close);
        radio_law_img=(ImageView)findViewById(R.id.radio_law_img);
        radio_user_img=(ImageView)findViewById(R.id.radio_user_img);

        login_uname=(EditText)findViewById(R.id.login_email);
//        login_uname.setHint(Settings.getword(this,"uname"));
        login_pass=(EditText)findViewById(R.id.login_pass);
//        login_pass.setHint(Settings.getword(this,"password"));
        reg_fname=(EditText)findViewById(R.id.reg_fname);
//        reg_fname.setHint(Settings.getword(this,"fname"));
        reg_lname=(EditText)findViewById(R.id.reg_lname);
//        reg_lname.setHint(Settings.getword(this,"lname"));
        reg_uname=(EditText)findViewById(R.id.reg_uname);
//        reg_uname.setHint(Settings.getword(this,"uname"));
        reg_email=(EditText)findViewById(R.id.reg_email);
//        reg_email.setHint(Settings.getword(this,"email"));
        reg_pass=(EditText)findViewById(R.id.reg_pass);
//        reg_pass.setHint(Settings.getword(this,"password"));
        reg_phone=(EditText)findViewById(R.id.reg_phone);
//        reg_phone.setHint(Settings.getword(this,"phone"));
        fp_et=(EditText)findViewById(R.id.fp_et);
//        fp_et.setHint(Settings.getword(this,"enter_email"));
        law_sign_email=(EditText)findViewById(R.id.law_signup_email);
//        law_sign_email.setHint(Settings.getword(this,"enter_email"));


        login=(LinearLayout)findViewById(R.id.signin_ll);
        signup_user=(LinearLayout)findViewById(R.id.signup_user_ll);
        signup_law=(LinearLayout)findViewById(R.id.signup_law_ll);
        register=(LinearLayout)findViewById(R.id.register_ll);
        submit_ll=(LinearLayout)findViewById(R.id.fp_submit_ll);
        fp_pop_ll=(LinearLayout)findViewById(R.id.fp_pop_ll);
        skip_ll=(LinearLayout)findViewById(R.id.skip_ll);
        signup_pop_ll=(LinearLayout)findViewById(R.id.signup_pop_law);
        lan_ll=(LinearLayout)findViewById(R.id.lan_ll);
        sign_submit_ll=(LinearLayout)findViewById(R.id.law_signup_submit_ll);
        r_l_ll=(LinearLayout)findViewById(R.id.radio_law_ll);
        r_u_ll=(LinearLayout)findViewById(R.id.radio_user_ll);


        login_tv=(TextView)findViewById(R.id.signin_tv);
//        reg_phone.setText(Settings.getword(this,"login"));
        register_tv=(TextView)findViewById(R.id.register_tv);
//        reg_phone.setText(Settings.getword(this,"register"));
        signup_law_tv=(TextView)findViewById(R.id.signup_law_tv);
//        reg_phone.setText(Settings.getword(this,"sginup_lawyer"));
        signup_user_tv=(TextView)findViewById(R.id.signup_user_tv);
//        reg_phone.setText(Settings.getword(this,"signup_user"));
        submit_tv=(TextView)findViewById(R.id.fp_submit_tv);
//        submit_tv.setText(Settings.getword(this,"submit"));
        fp_title=(TextView)findViewById(R.id.fp_title);
//        fp_title.setText(Settings.getword(this,"forgot"));
        forgot_password=(TextView)findViewById(R.id.forgot_password);
//        forgot_password.setText(Settings.getword(this,"forgot"));
        skip_tv=(TextView)findViewById(R.id.skip_tv);
//        skip_tv.setText(Settings.getword(this,"skip"));
        radio_law_tv=(TextView)findViewById(R.id.radio_law_tv);
//        radio_law_tv.setText(Settings.getword(this,"skip"));
        radio_user_tv=(TextView)findViewById(R.id.radio_user_tv);
//        radio_user_tv.setText(Settings.getword(this,"skip"));
        lan_tv=(TextView)findViewById(R.id.lan_tv);
        sign_submit_tv=(TextView)findViewById(R.id.law_signup_submit_tv);
//        sign_submit_tv.setText(Settings.getword(this,"submit"));


        r_l_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="2";
                radio_law_img.setImageResource(R.drawable.radio_full);
                radio_user_img.setImageResource(R.drawable.radio_empty);

            }
        });
        r_u_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="1";
                radio_law_img.setImageResource(R.drawable.radio_empty);
                radio_user_img.setImageResource(R.drawable.radio_full);

            }
        });
        skip_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                viewFlipper.setDisplayedChild(1);
                Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        signup_law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//            viewFlipper.setDisplayedChild(1);
                signup_pop_ll.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_up);
                animation.reset();
                signup_pop_ll.setAnimation(animation);
            }
        });
        signup_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="1";
                viewFlipper.setDisplayedChild(1);
            }
        });
        sign_submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(law_sign_email.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter email", Toast.LENGTH_SHORT).show();
                }else if(!law_sign_email.getText().toString().matches(emailPattern)) {
                    Toast.makeText(LoginActivity.this, "please enter valid email", Toast.LENGTH_SHORT).show();
                }else{
                    signup_pop_ll.setVisibility(View.GONE);
                    animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_down);
                    animation.reset();
                    signup_pop_ll.setAnimation(animation);
                    Intent mainIntent = new Intent(getApplicationContext(), PackagesActivity.class);
                    mainIntent.putExtra("email",law_sign_email.getText().toString());
                    startActivity(mainIntent);
                }
            }
        });
        if(Settings.get_user_language(LoginActivity.this).equals("en")) {
            lan_tv.setText("Ar");
        }else{
            lan_tv.setText("En");
        }



        lan_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.get_user_language(LoginActivity.this).equals("en")){

                    Settings.set_user_language(LoginActivity.this, "ar");
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{

                    Settings.set_user_language(LoginActivity.this, "en");
                    Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login_uname.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter email", Toast.LENGTH_SHORT).show();
                }else if(!login_uname.getText().toString().matches(emailPattern)) {
                    Toast.makeText(LoginActivity.this, "please enter valid email", Toast.LENGTH_SHORT).show();
                }else if(login_pass.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
                }else if(type.equals("")){
                    Toast.makeText(LoginActivity.this, "please Select Login Type", Toast.LENGTH_SHORT).show();
                } else {
//                    Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
//                    startActivity(mainIntent);
                    login();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reg_fname.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter first name", Toast.LENGTH_SHORT).show();
                } else if(reg_lname.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter last name", Toast.LENGTH_SHORT).show();
                }else if(reg_uname.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter user name", Toast.LENGTH_SHORT).show();
                }else if(reg_pass.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
                }else if(reg_email.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "please enter email id", Toast.LENGTH_SHORT).show();
                } else if (!reg_email.getText().toString().matches(emailPattern)){
                        Toast.makeText(LoginActivity.this, "Please Enter Valid Email id", Toast.LENGTH_SHORT).show();
                }else if(reg_phone.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "please enter phone number", Toast.LENGTH_SHORT).show();
                }else {
//                    viewFlipper.setDisplayedChild(0);
                    register();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(0);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp_pop_ll.setVisibility(View.GONE);
                animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_up);
                animation.reset();
                fp_pop_ll.setAnimation(animation);
            }
        });
        sign_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_pop_ll.setVisibility(View.GONE);
                animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_down);
                animation.reset();
                signup_pop_ll.setAnimation(animation);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fp_pop_ll.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_up);
                animation.reset();
                fp_pop_ll.setAnimation(animation);
            }
        });
        submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fp_et.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "please enter email id", Toast.LENGTH_SHORT).show();
                } else if (!fp_et.getText().toString().matches(emailPattern)){
                    Toast.makeText(LoginActivity.this, "Please Enter Valid Email id", Toast.LENGTH_SHORT).show();
                }else{
//                    fp_pop_ll.setVisibility(View.GONE);
                    forgot_pass();
                }
            }
        });

    }
//    @Override
//    public void onBackPressed()
//    {
//        if(viewFlipper.getDisplayedChild()==1){
//            viewFlipper.setDisplayedChild(0);
//        }else {
//            super.onBackPressed();
//        }
//    }
    String url;
    public  void login(){
        try {
            url = Settings.login_url + "email="+ URLEncoder.encode(login_uname.getText().toString(), "utf-8")+
                    "&password="+URLEncoder.encode(login_pass.getText().toString(),"utf-8")+"&type="+URLEncoder.encode(type,"utf-8");
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
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String mem_id=jsonObject.getString("member_id");
                        String name=jsonObject.getString("name");
                        if(type.equals("1")){
                            Settings.setUserid(LoginActivity.this, mem_id, "user");
                            Toast.makeText(LoginActivity.this, name, Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }else{
                            Settings.setUserid(LoginActivity.this, mem_id, "law");
                            Toast.makeText(LoginActivity.this, name, Toast.LENGTH_SHORT).show();
                            Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                            startActivity(mainIntent);
                            finish();
                        }

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
                Toast.makeText(LoginActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    public  void register(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.signup_url;

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
                            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String mem_id=jsonObject.getString("member_id");
                        String msg=jsonObject.getString("message");
                        Settings.setUserid(LoginActivity.this, mem_id, "user");
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        viewFlipper.setDisplayedChild(0);
                        Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                        startActivity(mainIntent);
                        finish();
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
                        Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("fname",reg_fname.getText().toString());
                params.put("lname",reg_lname.getText().toString());
                params.put("username",reg_uname.getText().toString());
                params.put("email",reg_email.getText().toString());
                params.put("password",reg_pass.getText().toString());
                params.put("phone",reg_phone.getText().toString());
                params.put("type",type);
                params.put("android_token",type);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void forgot_pass(){
        String url = Settings.forgot_password_url+"email="+fp_et.getText().toString();
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.show();
        progressDialog.setCancelable(false);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Failed")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                        fp_pop_ll.setVisibility(View.GONE);
                        animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.slide_up);
                        animation.reset();
                        fp_pop_ll.setAnimation(animation);
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
                Toast.makeText(LoginActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
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
