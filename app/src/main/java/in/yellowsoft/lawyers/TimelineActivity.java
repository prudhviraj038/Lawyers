package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TimelineActivity extends Activity {
    ListView listView;
    TimelineAdapter timelineAdapter;
    ImageView lawyers,books,settings,filter;
    ArrayList<Posts> posts;
    String type;
    LinearLayout post_pop_ll,post_img_ll,submit_ll,cancel_ll,main_post_ll,des_pop_ll;
    TextView des_tv;
    EditText et_post,et_post_title;
    ImageView post_img,close;
    String post_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.timeline_screen);
        posts=new ArrayList<>();
        type=Settings.getType(TimelineActivity.this);
        get_posts();
        lawyers=(ImageView)findViewById(R.id.lawyers_img_1);
        books=(ImageView)findViewById(R.id.books_image_1);
        settings=(ImageView)findViewById(R.id.settings_img_1);
        post_img=(ImageView)findViewById(R.id.post_image);
        close=(ImageView)findViewById(R.id.des_pop_close);
        main_post_ll=(LinearLayout)findViewById(R.id.main_post_ll);
        post_pop_ll=(LinearLayout)findViewById(R.id.post_pop_ll);
        post_img_ll=(LinearLayout)findViewById(R.id.post_photo_ll);
        submit_ll=(LinearLayout)findViewById(R.id.submit_post_ll);
        cancel_ll=(LinearLayout)findViewById(R.id.cancel_post_ll);
        des_pop_ll=(LinearLayout)findViewById(R.id.des_popup);
        des_tv=(TextView)findViewById(R.id.timeline_des_pop_tv);
        et_post=(EditText)findViewById(R.id.et_post);
        et_post_title=(EditText)findViewById(R.id.et_post_title);
        post_img.setVisibility(View.GONE);
        post_pop_ll.setVisibility(View.GONE);
        listView=(ListView)findViewById(R.id.timeline_list);
        timelineAdapter=new TimelineAdapter(this,posts,TimelineActivity.this);
        listView.setAdapter(timelineAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        if(type.equals("law")){
            main_post_ll.setVisibility(View.VISIBLE);
        }else{
            main_post_ll.setVisibility(View.GONE);
        }
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des_pop_ll.setVisibility(View.GONE);
            }
        });
        lawyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), LawyersActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), SettingsActivity.class);
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
        main_post_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_pop_ll.setVisibility(View.VISIBLE);
            }
        });

        post_img_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_post_title.getText().toString().equals("")){
                    Toast.makeText(TimelineActivity.this, "please enter title", Toast.LENGTH_SHORT).show();
                } else if(et_post.getText().toString().equals("")){
                    Toast.makeText(TimelineActivity.this, "please enter comments", Toast.LENGTH_SHORT).show();
                }else{
                    post_comments();
                }
            }
        });
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_pop_ll.setVisibility(View.GONE);
            }
        });
    }
    public  void dis_des(int posi){
        des_tv.setText(posts.get(posi).message);
        des_pop_ll.setVisibility(View.VISIBLE);
    }
    public  void shareTextUrl(final int posi) {
        Picasso.with(getApplicationContext()).load(posts.get(posi).image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.putExtra(Intent.EXTRA_SUBJECT, posts.get(posi).title);
                i.putExtra(Intent.EXTRA_TEXT,posts.get(posi).message);
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
    boolean isimgchoosen = false;
    final int RESULT_LOAD_IMAGE = 1;
    String imgDecodableString;
    String encodedString;
    Bitmap bitmap;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "image selected", Toast.LENGTH_LONG)
                .show();
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
//                ImageView imgView = (ImageView) findViewById(R.id.ic_upload_logo);
                // Set the Image in ImageView after decoding the String
                post_img.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
                post_img.setVisibility(View.VISIBLE);
                isimgchoosen=true;
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {
            final ProgressDialog progressDialog = new ProgressDialog(TimelineActivity.this);

            protected void onPreExecute() {
                progressDialog.setMessage("please wait.. encoding image");
                progressDialog.show();
                progressDialog.setCancelable(false);

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                encodedString = "";
                bitmap = BitmapFactory.decodeFile(imgDecodableString, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, Base64.NO_WRAP);


                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                // Put converted Image string into Async Http Post param
                // Trigger Image upload
                if(progressDialog!=null)
                    progressDialog.dismiss();
                company_logo();

            }
        }.execute(null, null, null);
    }
    public  void company_logo(){
        final ProgressDialog progressDialog = new ProgressDialog(TimelineActivity.this);
        progressDialog.setMessage("please wait.. uploading image");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.Post_img_url;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog!=null)
                    progressDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("response",jsonObject.toString());
                    String reply=jsonObject.getString("status");
                    if(reply.equals("Success")) {
                        String msg = jsonObject.getString("message");
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                        post_pop_ll.setVisibility(View.GONE);
                        get_posts();
                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(TimelineActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("post_id",post_id);
                params.put("file",encodedString);
                params.put("ext_str", "jpg");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    String url;
    public  void post_comments(){
            url = Settings.Post_comm_url + "lawyer_id="+ Settings.getLawyerUserid(TimelineActivity.this)+
                    "&title="+et_post_title.getText().toString()+"&message="+et_post.getText().toString();
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
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        post_id=jsonObject.getString("post_id");
                        String msg=jsonObject.getString("message");
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(isimgchoosen){
                            encodeImagetoString();
                        }else{
                            post_pop_ll.setVisibility(View.GONE);
                            get_posts();
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
                Toast.makeText(TimelineActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }

    private void get_posts() {
        String url;
//        if(type.equals("law"))
//            url = Settings.Posts_url+ "lawyer_id="+Settings.getLawyerUserid(this);
//        else
            url = Settings.Posts_url;
//                    + "lawyer_id="+Settings.getUserid(this);
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                posts.clear();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        Posts pack=new Posts(sub);
                        posts.add(pack);
                    }
                    timelineAdapter=new TimelineAdapter(TimelineActivity.this,posts, TimelineActivity.this);
                    listView.setAdapter(timelineAdapter);
                    timelineAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(TimelineActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
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
