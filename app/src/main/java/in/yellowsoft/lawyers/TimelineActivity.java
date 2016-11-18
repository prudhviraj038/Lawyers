package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class TimelineActivity extends Activity {
    ListView listView;
    TimelineAdapter timelineAdapter;
    ImageView lawyers,books,settings,filter;
    ArrayList<Posts> posts;
    String type;
    LinearLayout post_pop_ll,post_img_ll,submit_ll,cancel_ll,main_post_ll,des_pop_ll,post_video_ll;
    RelativeLayout video_rl;
    TextView des_tv,post_video_tv,post_img_tv,write_post_title;
    EditText et_post,et_post_title;
    ImageView post_img,close,ply_btn,thumb_img;
    String post_id;
    String lat,lon,adr;
    private Uri fileUri;
    Boolean is_video_added=false;
    Boolean is_location_added=false;
     Bitmap thumb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.timeline_screen);
        posts=new ArrayList<>();
        type=Settings.getType(TimelineActivity.this);
        get_lawyer_details();
        get_posts();
        if(!Settings.getUserid(this).equals("-1")&&Settings.getType(this).equals("law"))
            launchTestService();
        lawyers=(ImageView)findViewById(R.id.lawyers_img_1);
        books=(ImageView)findViewById(R.id.books_image_1);
        settings=(ImageView)findViewById(R.id.settings_img_1);
        post_img=(ImageView)findViewById(R.id.post_image);
        close=(ImageView)findViewById(R.id.des_pop_close);
        ply_btn=(ImageView)findViewById(R.id.video_ply_btn);
        thumb_img=(ImageView)findViewById(R.id.video_thumb);
        video_rl=(RelativeLayout)findViewById(R.id.rl_video_ll);
        main_post_ll=(LinearLayout)findViewById(R.id.main_post_ll);
        post_pop_ll=(LinearLayout)findViewById(R.id.post_pop_ll);
        post_img_ll=(LinearLayout)findViewById(R.id.post_photo_ll);
        submit_ll=(LinearLayout)findViewById(R.id.submit_post_ll);
        cancel_ll=(LinearLayout)findViewById(R.id.cancel_post_ll);
        des_pop_ll=(LinearLayout)findViewById(R.id.des_popup);
        post_video_ll=(LinearLayout)findViewById(R.id.post_video_ll);
        des_tv=(TextView)findViewById(R.id.timeline_des_pop_tv);
        write_post_title=(TextView)findViewById(R.id.write_your_post);
        write_post_title.setText(Settings.getword(TimelineActivity.this,"write_your_post"));
        post_video_tv=(TextView)findViewById(R.id.post_video_tv);
        post_video_tv.setText(Settings.getword(TimelineActivity.this,"Post Video"));
        post_img_tv=(TextView)findViewById(R.id.post_photo_tv);
        post_img_tv.setText(Settings.getword(TimelineActivity.this,"Post Image"));
        et_post=(EditText)findViewById(R.id.et_post);
        et_post.setHint(Settings.getword(TimelineActivity.this, "Post title"));
        et_post_title=(EditText)findViewById(R.id.et_post_title);
        et_post_title.setHint(Settings.getword(TimelineActivity.this, "Write your comments"));
        post_img.setVisibility(View.GONE);
        post_pop_ll.setVisibility(View.GONE);
        video_rl.setVisibility(View.GONE);
        listView=(ListView)findViewById(R.id.timeline_list);
        timelineAdapter=new TimelineAdapter(this,posts,TimelineActivity.this);
        listView.setAdapter(timelineAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(posts.get(position).id.equals("0")){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(posts.get(position).message));
                    startActivity(i);
                }else {
                    Intent mainIntent = new Intent(getApplicationContext(), TimelineDetailsActivity.class);
                    mainIntent.putExtra("posts", posts.get(position));
                    startActivity(mainIntent);
                }
            }
        });

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


            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.getUserid(TimelineActivity.this).equals("-1")){
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainIntent);
                }else {
                    Intent mainIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(mainIntent);
                }
            }
        });
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), BooksActivity.class);
                startActivity(mainIntent);

            }
        });
        if(Settings.getUserid(TimelineActivity.this).equals("-1")||!type.equals("law")) {
            main_post_ll.setVisibility(View.GONE);
        }else{
            main_post_ll.setVisibility(View.VISIBLE);
        }
        main_post_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (type.equals("law") && Settings.getLawTl(TimelineActivity.this).equals("Yes")) {
//                    main_post_ll.setVisibility(View.VISIBLE);
                        Log.e("tl_status", Settings.getLawTl(TimelineActivity.this));
                        post_pop_ll.setVisibility(View.VISIBLE);
                    } else {
//                    main_post_ll.setVisibility(View.GONE);
                        post_pop_ll.setVisibility(View.GONE);
                        Toast.makeText(TimelineActivity.this, Settings.getword(TimelineActivity.this, "cant_post"), Toast.LENGTH_SHORT).show();
                    }


            }
        });
        post_video_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectvideos();
            }
        });
        ply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                intent.setDataAndType(fileUri, "video/mp4");
                startActivity(intent);
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
                    Toast.makeText(TimelineActivity.this, Settings.getword(TimelineActivity.this,"empty_post_title"), Toast.LENGTH_SHORT).show();
                } else if(et_post.getText().toString().equals("")){
                    Toast.makeText(TimelineActivity.this, Settings.getword(TimelineActivity.this,"empty_post_comments"), Toast.LENGTH_SHORT).show();
                }else{
                    post_comments();
                }
            }
        });
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post_pop_ll.setVisibility(View.GONE);
                isimgchoosen = false;
                is_video_added=false;
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
//            if(Settings.getType(TimelineActivity.this).equals("law"))
//            get_status();
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
    public void launchTestService() {
        // Construct our Intent specifying the Service
        Intent i = new Intent(this, MyTestService.class);
        // Add extras to the bundle
        Log.e("m_id", Settings.getUserid(TimelineActivity.this));
        i.putExtra("foo", Settings.getUserid(TimelineActivity.this));
        // Start the service
        startService(i);
    }

    private void get_status() {
        String url = Settings.Add_Lawyer_status_url+"member_id="+Settings.getUserid(TimelineActivity.this);
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
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(TimelineActivity.this,Settings.getword(TimelineActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public  void dis_des(int posi){
        des_tv.setText(posts.get(posi).message);
        des_pop_ll.setVisibility(View.VISIBLE);
    }
    public  void play_video(int posi){
        Intent intent = new Intent(TimelineActivity.this, AndroidVideoPlayerActivity.class);
        Log.e("video",posts.get(posi).video);
        intent.putExtra("video", posts.get(posi).video);
        startActivity(intent);
    }
    public  void shareTextUrl(final int posi) {
        Picasso.with(getApplicationContext()).load(posts.get(posi).image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                i.putExtra(Intent.EXTRA_SUBJECT, posts.get(posi).title);
                i.putExtra(Intent.EXTRA_TEXT, posts.get(posi).message);
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
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    boolean isimgchoosen = false;
    final int RESULT_LOAD_IMAGE = 1;
    String imgDecodableString;
    String encodedString;
    Bitmap bitmap;

    private Uri mImageCaptureUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Toast.makeText(this, "image selected", Toast.LENGTH_LONG)
//                .show();
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
            } else if(requestCode == 200 && resultCode == RESULT_OK){
                fileUri=data.getData();
                String path = getRealPathFromURI(fileUri);
                if (path == null)
                    path = fileUri.getPath();
                thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                thumb_img.setImageBitmap(thumb);
                video_rl.setVisibility(View.VISIBLE);
                is_video_added = true;

//                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(fileUri, MediaStore.Video.Thumbnails.MINI_KIND);

            }else if (requestCode == 5 && resultCode == RESULT_OK) {
                is_video_added = true;

                mImageCaptureUri = data.getData();
                String path = getRealPathFromURI(mImageCaptureUri); //from Gallery

                if (path == null)
                    path = mImageCaptureUri.getPath();

                thumb = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);
                thumb_img.setImageBitmap(thumb);
                video_rl.setVisibility(View.VISIBLE);
                fileUri = Uri.fromFile(new File(path));

            }else {
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
                if(!isimgchoosen){
                    bitmap=thumb;
                }else{
                    bitmap = BitmapFactory.decodeFile(imgDecodableString, options);
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, Base64.NO_WRAP);
                Log.e("image",encodedString);

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

    public void selectvideos() {
        final String[] items = new String[]{
                Settings.getword(TimelineActivity.this,"camera"),
                Settings.getword(TimelineActivity.this, "gallery")};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(Settings.getword(TimelineActivity.this, "Select Video"));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    recordVideo();
                } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, 5);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri();
        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, 200);
    }
    public Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Lawyers_images");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("errror", "Oops! Failed create "
                        + "Lawyers_images" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
//        thumb = ThumbnailUtils.createVideoThumbnail(mediaStorageDir.getPath(), MediaStore.Video.Thumbnails.MINI_KIND);

        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");

        return mediaFile;
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
                        if(is_video_added){
                            upload_with_ion();
                            get_posts();
                        }else {
                            post_pop_ll.setVisibility(View.GONE);
                            get_posts();
                        }
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
        try {
            url = Settings.Post_comm_url + "lawyer_id="+ Settings.getLawyerUserid(TimelineActivity.this)+
                    "&title="+ URLEncoder.encode(et_post_title.getText().toString(), "utf-8")+
                "&message="+URLEncoder.encode(et_post.getText().toString(),"utf-8");
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
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        post_id=jsonObject.getString("post_id");
                        String msg=jsonObject.getString("message");
                        Toast.makeText(TimelineActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(isimgchoosen){
                            encodeImagetoString();
                        }else{
                            if(is_video_added) {
                                encodeImagetoString();
//                                upload_with_ion();
//                                get_posts();
                            }else{
                                post_pop_ll.setVisibility(View.GONE);
                                get_posts();
                            }
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
                Toast.makeText(TimelineActivity.this, Settings.getword(TimelineActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
        progressDialog.setMessage(Settings.getword(TimelineActivity.this, "please_wait"));
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
                Toast.makeText(TimelineActivity.this, Settings.getword(TimelineActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    private void upload_with_ion(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(TimelineActivity.this,"please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        Ion.with(this)
                .load(Settings.Upload_video_url)
                .setMultipartParameter("post_id", post_id)
                .setMultipartFile("video", "video/mp4", new File(fileUri.getPath())).asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String s) {
                        Log.e("res_ion", s);
                        if (progressDialog != null)
                            progressDialog.dismiss();

                        try {
                            JSONArray jsonArray = new JSONArray(s);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            if (jsonObject.getString("status").equals("Success")) {
                                Toast.makeText(TimelineActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                                finish();
                                post_pop_ll.setVisibility(View.GONE);
                                get_posts();
                            } else {
                                Toast.makeText(TimelineActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }
    private void get_lawyer_details() {
        String url = Settings.Lawyers_url+"?lawyer_id="+Settings.getUserid(TimelineActivity.this);
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(TimelineActivity.this, "please_wait"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        if(sub.getJSONObject("package").getString("timeline").equals("Yes"))
                            Settings.setLawTl(TimelineActivity.this,"Yes");
                        else
                            Settings.setLawTl(TimelineActivity.this,"No");
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
                Toast.makeText(TimelineActivity.this, Settings.getword(TimelineActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
