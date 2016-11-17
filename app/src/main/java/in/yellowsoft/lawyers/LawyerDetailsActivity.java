package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class LawyerDetailsActivity extends Activity {
    ListView listView;
    GridView gridView;
    ImageView back,status,bg,calling,mail,map,share,rev_close;
    TextView name,about,about_des,p_area,reviews_tv,add_review,pop_title,rating_tv,review_tv,submit_tv,follow_tv,temp_follow;
    CircleImageView law_img;
    LinearLayout rating_ll,add_review_ll,rev_pop_ll,submit_ll,pop_rating_ll,calling_ll,mes_ll,map_ll,share_ll,follow_ll;
    ReviewsAdapter reviewsAdapter;
    PracticeAreasAdapter practiceAreasAdapter;
    Lawyers lawyers;
    public ImageLoader imageLoader;
    EditText rev_et;
    String write;
    String cnt="0";
    Animation animation,animation2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.lawyers_details_screen);
        lawyers=(Lawyers)getIntent().getSerializableExtra("lawyers");
//        imageLoader=new ImageLoader(LawyerDetailsActivity.this);

        back=(ImageView)findViewById(R.id.law_det_back);
        status=(ImageView)findViewById(R.id.law_status);
        bg=(ImageView)findViewById(R.id.bg_img);
        status.setImageResource(R.drawable.green_icon);
        calling=(ImageView)findViewById(R.id.calling_img);
        map=(ImageView)findViewById(R.id.map_img);
        mail=(ImageView)findViewById(R.id.mail_img);
        share=(ImageView)findViewById(R.id.share_img);
        rev_close=(ImageView)findViewById(R.id.giv_rev_close);
        law_img=(CircleImageView)findViewById(R.id.lawyer_image);
        Picasso.with(LawyerDetailsActivity.this).load(lawyers.image).into(law_img);
        Picasso.with(LawyerDetailsActivity.this).load(lawyers.bg_image).into(bg);

        rev_pop_ll=(LinearLayout)findViewById(R.id.giv_rev_popup);
        submit_ll=(LinearLayout)findViewById(R.id.giv_rev_submit_ll);
        pop_rating_ll=(LinearLayout)findViewById(R.id.giv_rat_ll);
        calling_ll=(LinearLayout)findViewById(R.id.calling_ll);
        mes_ll=(LinearLayout)findViewById(R.id.mes_ll);
        map_ll=(LinearLayout)findViewById(R.id.map_ll);
        share_ll=(LinearLayout)findViewById(R.id.l_det_share_ll);
        follow_ll=(LinearLayout)findViewById(R.id.follow_ll);

        temp_follow=(TextView)findViewById(R.id.temp_follow);
        follow_tv=(TextView)findViewById(R.id.follow_tv);
        follow_tv.setText(Settings.getword(LawyerDetailsActivity.this,"follow"));
        pop_title=(TextView)findViewById(R.id.giv_rev_title);
        pop_title.setText(Settings.getword(LawyerDetailsActivity.this,"give_review"));
        rating_tv=(TextView)findViewById(R.id.giv_rev_sta_rating);
        rating_tv.setText(Settings.getword(LawyerDetailsActivity.this,"rating"));
        review_tv=(TextView)findViewById(R.id.giv_rev_sta_review);
        review_tv.setText(Settings.getword(LawyerDetailsActivity.this,"review"));
        submit_tv=(TextView)findViewById(R.id.giv_rev_submit_tv);
        submit_tv.setText(Settings.getword(LawyerDetailsActivity.this,"submit"));
        rev_et=(EditText)findViewById(R.id.giv_rev_et);
        rev_et.setHint(Settings.getword(LawyerDetailsActivity.this,"enter_review"));

        if(!Settings.getUserid(LawyerDetailsActivity.this).equals("-1"))
            follow_status();
        else{
            follow_ll.setBackgroundResource(R.drawable.signup_lawyer_rounded_corners);
            follow_tv.setTextColor(Color.parseColor("#ffffff"));
            follow_tv.setText(Settings.getword(LawyerDetailsActivity.this, "follow"));
            temp_follow.setText(Settings.getword(LawyerDetailsActivity.this, "follow"));
        }
        name=(TextView)findViewById(R.id.lawyer_name_deta);
        name.setText(lawyers.name);
        about=(TextView)findViewById(R.id.about_company);
        about.setText(Settings.getword(this,"about_company"));
        about_des=(TextView)findViewById(R.id.about_company_description);
        about_des.setText(Html.fromHtml(lawyers.about));
        p_area=(TextView)findViewById(R.id.practice_area);
        p_area.setText(Settings.getword(this,"Practice Areas"));
        reviews_tv=(TextView)findViewById(R.id.reviews_tv);
        reviews_tv.setText(Settings.getword(this,"reviews"));
        add_review=(TextView)findViewById(R.id.add_review_tv);
        add_review.setText(Settings.getword(this,"Add Review"));

        add_review_ll=(LinearLayout)findViewById(R.id.add_review_ll);
        rating_ll=(LinearLayout)findViewById(R.id.law_rating_ll);
        Settings.set_rating(this,lawyers.rating,rating_ll);
        listView=(ListView)findViewById(R.id.reviews_list);
        reviewsAdapter=new ReviewsAdapter(this,lawyers);
        listView.setAdapter(reviewsAdapter);
        gridView=(GridView)findViewById(R.id.p_area_grid);
        practiceAreasAdapter=new PracticeAreasAdapter(this,lawyers);
        gridView.setAdapter(practiceAreasAdapter);
        if(Integer.parseInt(lawyers.status_sec)>=60){
            status.setImageResource(R.drawable.red_icon);
        }else{
            status.setImageResource(R.drawable.green_icon);
        }
        if(lawyers.p_mes.equals("Yes")){
            mes_ll.setVisibility(View.VISIBLE);
        }else{
            mes_ll.setVisibility(View.GONE);
        }
        if(lawyers.p_go.equals("Yes")){
            map_ll.setVisibility(View.VISIBLE);
        }else{
            map_ll.setVisibility(View.GONE);
        }
        follow_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.getUserid(LawyerDetailsActivity.this).equals("-1")){
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainIntent);
                }else {
                    if (cnt.equals("0")) {
                        follow();
                    } else {
                        unfollow();
                    }
                }

            }
        });
//        if(lawyers.p_social.equals("Yes")){
//            share_ll.setVisibility(View.VISIBLE);
//        }else{
//            share_ll.setVisibility(View.GONE);
//        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        get_status();
        setListViewHeightBasedOnItems(listView);
        setGridViewHeightBasedOnItems(gridView);
        set_refresh_timer();

        calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + lawyers.phone));
                startActivity(callIntent);
            }
        });
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.getUserid(LawyerDetailsActivity.this).equals("-1")){
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainIntent);
                }else {
                    Intent mainIntent = new Intent(getApplicationContext(), ChatingActivity.class);
                    mainIntent.putExtra("law_id", lawyers.id);
                    mainIntent.putExtra("law_img", lawyers.image);
                    mainIntent.putExtra("law_name", lawyers.name);
                    startActivity(mainIntent);
                }
            }
        });
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), MapDisplayActivity.class);
                mainIntent.putExtra("lawyers",lawyers);
                startActivity(mainIntent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl();
            }
        });
        rev_pop_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        add_review_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.getUserid(LawyerDetailsActivity.this).equals("-1")) {
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainIntent);
                }else{
                    set__give_rating(LawyerDetailsActivity.this, "0", pop_rating_ll);
                    rev_pop_ll.setVisibility(View.VISIBLE);
                    animation = AnimationUtils.loadAnimation(LawyerDetailsActivity.this, R.anim.slide_up);
                    animation.reset();
                    rev_pop_ll.setAnimation(animation);
                }

            }
        });

        rev_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rev_pop_ll.setVisibility(View.GONE);
                animation = AnimationUtils.loadAnimation(LawyerDetailsActivity.this, R.anim.slide_down);
                animation.reset();
                rev_pop_ll.setAnimation(animation);
            }
        });
        submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write="";
                write=rev_et.getText().toString();
                if(rating_user.equals(""))
//                    alert.showAlertDialog(getActivity(), "Info", Settings.getword(getActivity(), "empty_rating"), false);
                    Toast.makeText(LawyerDetailsActivity.this, Settings.getword(LawyerDetailsActivity.this,"empty_rating"), Toast.LENGTH_SHORT).show();
                else  if(write.equals(""))
//                   / alert.showAlertDialog(getActivity(), "Info", Settings.getword(getActivity(), "empty_comments"), false);
                    Toast.makeText(LawyerDetailsActivity.this, Settings.getword(LawyerDetailsActivity.this,"empty_review"), Toast.LENGTH_SHORT).show();
                else
                    send_rating();

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
            get_status();
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
    String rating_user="";
    public   void set__give_rating(final Context context,String value, final LinearLayout rating_ll){
//        rating_user="";
        rating_ll.removeAllViews();
        for(float i=1;i<=5;i++) {
            ImageView star = new ImageView(context);
//            star.setMaxWidth(50);
//            star.setMaxHeight(50);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(2,0,2,0);
            star.setLayoutParams(lp);
            star.setAdjustViewBounds(true);
            final float finalI = i;
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rating_user=String.valueOf(finalI);
                    set__give_rating(context,String.valueOf(finalI),pop_rating_ll);
                }
            });
            if(i<=Float.parseFloat(value))
                star.setImageResource(R.drawable.black_full_star);
            else if(i-Float.parseFloat(value)<1)
                star.setImageResource(R.drawable.black_half_star);
            else
                star.setImageResource(R.drawable.empty_star);
            rating_ll.addView(star);
        }
    }
    private void shareTextUrl() {
        Picasso.with(getApplicationContext()).load(lawyers.image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.putExtra(Intent.EXTRA_SUBJECT, lawyers.name);
//                i.putExtra(Intent.EXTRA_TEXT, html2text(lawyers.name) + html2text(lawyers.name) + "   " + Settings.getSettings(getApplicationContext(), "play_store"));
                startActivity(Intent.createChooser(i, "Share Image"));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }
//    public static String html2text(String html) {
//        return Jsoup.parse(html).text();
//    }


    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
    public  void send_rating(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.Add_rating_url;
        Log.e("ratingggg",rating_user);
        Log.e("review", write);
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
//                        String address_id = jsonObject.getString("address_id");
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        rev_pop_ll.setVisibility(View.GONE);
                        animation = AnimationUtils.loadAnimation(LawyerDetailsActivity.this, R.anim.slide_down);
                        animation.reset();
                        rev_pop_ll.setAnimation(animation);
                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LawyerDetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("member_id", Settings.getUserid(LawyerDetailsActivity.this));
                params.put("lawyer_id", lawyers.id);
                params.put("rating", rating_user);
                params.put("review",write);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void get_status() {
        String url = Settings.Lawyer_status_url+"member_id="+lawyers.id;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        String time=sub.getString("time");
                        String sta=sub.getString("seconds");
                        if(Integer.parseInt(sta)>=60){
                            status.setImageResource(R.drawable.red_icon);
                        }else{
                            status.setImageResource(R.drawable.green_icon);
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
                Toast.makeText(LawyerDetailsActivity.this, Settings.getword(LawyerDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
    public static boolean setGridViewHeightBasedOnItems(GridView gridView) {

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            int lastItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, gridView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
                lastItemsHeight=item.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            if(numberOfItems%2==1)
                totalItemsHeight=totalItemsHeight+lastItemsHeight;

            params.height = totalItemsHeight/2;
            gridView.setLayoutParams(params);
            gridView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
    public  void follow(){
        String url = Settings.follow_url + "member_id="+ Settings.getUserid(LawyerDetailsActivity.this)+
                "&lawyer_id="+lawyers.id;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(LawyerDetailsActivity.this,"please_wait"));
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
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String follow_id=jsonObject.getString("follow_id");
                        String msg=jsonObject.getString("message");
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        follow_status();
//                        like_img.setImageResource(R.drawable.like);
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
                Toast.makeText(LawyerDetailsActivity.this, Settings.getword(LawyerDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public  void unfollow(){
        String url = Settings.Unfollow_url + "member_id="+ Settings.getUserid(LawyerDetailsActivity.this)+
                "&lawyer_id="+lawyers.id;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait....");
        progressDialog.setMessage(Settings.getword(this, "please_wait"));
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
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        follow_status();
//                        like_img.setImageResource(R.drawable.unlike);
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
                Toast.makeText(LawyerDetailsActivity.this, Settings.getword(LawyerDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public  void follow_status(){
        String url = Settings.follow_status_url + "member_id="+ Settings.getUserid(LawyerDetailsActivity.this)+
                "&lawyer_id="+lawyers.id;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please wait....");
        progressDialog.setMessage(Settings.getword(this, "please_wait"));
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
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cnt=jsonObject.getString("cnt");
                        String msg=jsonObject.getString("message");
                        Toast.makeText(LawyerDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(!cnt.equals("0")){
                            follow_ll.setBackgroundResource(R.drawable.white_rounded_corners);
                            follow_tv.setTextColor(Color.parseColor("#c69a32"));
                            follow_tv.setText(Settings.getword(LawyerDetailsActivity.this, "unfollow"));
                            temp_follow.setText(Settings.getword(LawyerDetailsActivity.this,"unfollow"));
                        }else{
                            follow_ll.setBackgroundResource(R.drawable.signup_lawyer_rounded_corners);
                            follow_tv.setTextColor(Color.parseColor("#ffffff"));
                            follow_tv.setText(Settings.getword(LawyerDetailsActivity.this, "follow"));
                            temp_follow.setText(Settings.getword(LawyerDetailsActivity.this, "follow"));
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
                Toast.makeText(LawyerDetailsActivity.this, Settings.getword(LawyerDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
