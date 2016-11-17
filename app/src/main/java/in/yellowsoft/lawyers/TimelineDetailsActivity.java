package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class TimelineDetailsActivity extends Activity {
    TextView title,date,descr,like_tv,share_tv,comm_tv;
    LinearLayout like,share,comment,like_ll,comm_ll,share_ll;
    ImageView tl_img,like_img,share_img,ply_btn,back,comm_img;
    int temp=0;
    String cnt;
    Posts posts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.timeline_details_screen);
        posts=(Posts)getIntent().getSerializableExtra("posts");
        like_status();
        title=(TextView)findViewById(R.id.tl_det_title);
        title.setText(posts.title);
        date=(TextView)findViewById(R.id.tl_det_date);
        date.setText(posts.date);
        descr=(TextView)findViewById(R.id.tl_det_des);
        descr.setText(posts.message);
        like_tv=(TextView)findViewById(R.id.tl_det_like_tv);
        like_tv.setText(Settings.getword(TimelineDetailsActivity.this,"Like"));
        share_tv=(TextView)findViewById(R.id.tl_det_share_tv);
        share_tv.setText(Settings.getword(TimelineDetailsActivity.this,"Share"));
        comm_tv=(TextView)findViewById(R.id.tl_det_comment_tv);
        comm_tv.setText(Settings.getword(TimelineDetailsActivity.this,"Comment"));

        like=(LinearLayout)findViewById(R.id.tl_det_like);
        share=(LinearLayout)findViewById(R.id.tl_det_share);
        comment=(LinearLayout)findViewById(R.id.tl_det_comment);
        like_ll=(LinearLayout)findViewById(R.id.like_lll);
        share_ll=(LinearLayout)findViewById(R.id.share_lll);
        comm_ll=(LinearLayout)findViewById(R.id.comment_lll);

        back=(ImageView)findViewById(R.id.tl_des_back);
        tl_img=(ImageView)findViewById(R.id.tl_det_image);
        like_img=(ImageView)findViewById(R.id.tl_det_like_img);
        share_img=(ImageView)findViewById(R.id.tl_det_share_img);
        comm_img=(ImageView)findViewById(R.id.tl_det_comment_img);
        ply_btn=(ImageView)findViewById(R.id.ply_btn_det_tl);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(posts.video.equals("")){
            ply_btn.setVisibility(View.GONE);
        }else{
            ply_btn.setVisibility(View.VISIBLE);
        }
        Picasso.with(TimelineDetailsActivity.this).load(posts.image).into(tl_img);
        ply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TimelineDetailsActivity.this, AndroidVideoPlayerActivity.class);
                intent.putExtra("video", posts.video);
                startActivity(intent);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(cnt.equals("0")){
                   like();
               }else{
                   unlike();
               }

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareTextUrl();
            }
        });


    }
    public  void shareTextUrl() {
        Picasso.with(getApplicationContext()).load(posts.image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.putExtra(Intent.EXTRA_SUBJECT, posts.title);
                i.putExtra(Intent.EXTRA_TEXT, posts.message);
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
    public  void like(){
        String url = Settings.like_url + "member_id="+ Settings.getUserid(TimelineDetailsActivity.this)+
                "&post_id="+posts.id;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(TimelineDetailsActivity.this,"please_wait"));
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
                        Toast.makeText(TimelineDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String like_id=jsonObject.getString("like_id");
                        String msg=jsonObject.getString("message");
                        Toast.makeText(TimelineDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        like_status();
                        like_img.setImageResource(R.drawable.like);
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
                Toast.makeText(TimelineDetailsActivity.this, Settings.getword(TimelineDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public  void unlike(){
        String url = Settings.Unlike_url + "member_id="+ Settings.getUserid(TimelineDetailsActivity.this)+
                "&post_id="+posts.id;
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
                        Toast.makeText(TimelineDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(TimelineDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        like_status();
                        like_img.setImageResource(R.drawable.unlike);
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
                Toast.makeText(TimelineDetailsActivity.this, Settings.getword(TimelineDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public  void like_status(){
        String url = Settings.like_status_url + "member_id="+ Settings.getUserid(TimelineDetailsActivity.this)+
                "&post_id="+posts.id;
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
                        Toast.makeText(TimelineDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        cnt=jsonObject.getString("cnt");
                        String msg=jsonObject.getString("message");
                        Toast.makeText(TimelineDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(cnt.equals("0")){
                            like_img.setImageResource(R.drawable.unlike);
                        }else{
                            like_img.setImageResource(R.drawable.like);
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
                Toast.makeText(TimelineDetailsActivity.this, Settings.getword(TimelineDetailsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
