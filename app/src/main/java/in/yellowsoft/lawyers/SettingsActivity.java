package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.audiofx.BassBoost;
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
import android.widget.ArrayAdapter;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SettingsActivity extends Activity {
    ImageView back,load,user,books,close,u_img;
    TextView edit_profile,c_pass,c_lang,logout,submit_tv,cp_title,update_tv,mc_tv,u_name,u_mail,u_phone,title;
    LinearLayout ep_ll,c_pass_ll,cl_ll,logout_ll,submit_ll,cp_pop_ll,update_ll,mc_ll;
    EditText op,np,cp,fname,lname,uname,email,phone;
    ViewFlipper viewFlipper;
    CircleImageView user_img;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.settings_screen);
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper2);
        info();
        get_mem_details();
        back=(ImageView)findViewById(R.id.sett_back);
        load=(ImageView)findViewById(R.id.load_img_4);
        user=(ImageView)findViewById(R.id.lawyers_img_4);
        books=(ImageView)findViewById(R.id.books_image_4);
        close=(ImageView)findViewById(R.id.cp_pop_close);
        u_img=(ImageView)findViewById(R.id.u_img);
        user_img=(CircleImageView)findViewById(R.id.user_edit_img);


        edit_profile=(TextView)findViewById(R.id.ef_tv);
        edit_profile.setText(Settings.getword(this,"Edit Profile"));
        c_pass=(TextView)findViewById(R.id.cp_tv);
        c_pass.setText(Settings.getword(this,"Change Password"));
        c_lang=(TextView)findViewById(R.id.cl_tv);
        c_lang.setText(Settings.getword(this,"Change Language"));
        logout=(TextView)findViewById(R.id.logout_tv);
        logout.setText(Settings.getword(this,"Logout"));
        submit_tv=(TextView)findViewById(R.id.cp_submit_tv);
        submit_tv.setText(Settings.getword(this,"submit"));
        cp_title=(TextView)findViewById(R.id.cp_title);
        cp_title.setText(Settings.getword(this,"Change Password"));
        update_tv=(TextView)findViewById(R.id.ep_update_tv);
        update_tv.setText(Settings.getword(this,"update"));
        mc_tv=(TextView)findViewById(R.id.mc_tv);
        mc_tv.setText(Settings.getword(this,"My Chats"));
        u_name=(TextView)findViewById(R.id.sett_user_name);
        u_mail=(TextView)findViewById(R.id.sett_user_mail);
        u_phone=(TextView)findViewById(R.id.sett_user_phone);
        title=(TextView)findViewById(R.id.sett_title);
        title.setText(Settings.getword(this,"settings"));

        ep_ll=(LinearLayout)findViewById(R.id.ef_ll);
        c_pass_ll=(LinearLayout)findViewById(R.id.cp_ll);
        cl_ll=(LinearLayout)findViewById(R.id.cl_ll);
        logout_ll=(LinearLayout)findViewById(R.id.logout_ll);
        submit_ll=(LinearLayout)findViewById(R.id.cp_submit_ll);
        cp_pop_ll=(LinearLayout)findViewById(R.id.cp_pop_ll);
        update_ll=(LinearLayout)findViewById(R.id.ep_update_ll);
        mc_ll=(LinearLayout)findViewById(R.id.mc_ll);

        op=(EditText)findViewById(R.id.et_op);
        op.setHint(Settings.getword(this,"Old Password"));
        np=(EditText)findViewById(R.id.et_np);
        np.setHint(Settings.getword(this,"New Password"));
        cp=(EditText)findViewById(R.id.et_cp);
        cp.setHint(Settings.getword(this,"Re Enter New Password"));
        fname=(EditText)findViewById(R.id.ep_fname);
        fname.setHint(Settings.getword(this,"First Name"));
        lname=(EditText)findViewById(R.id.ep_lname);
        lname.setHint(Settings.getword(this,"Last Name"));
        uname=(EditText)findViewById(R.id.ep_uname);
        uname.setHint(Settings.getword(this,"User Name"));
        email=(EditText)findViewById(R.id.ep_email);
        email.setHint(Settings.getword(this,"Email"));
        phone=(EditText)findViewById(R.id.ep_phone);
        phone.setHint(Settings.getword(this,"Phone Number"));


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
        mc_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), MyChatListActivity.class);
                startActivity(mainIntent);

            }
        });
        cp_pop_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectphotos();
            }
        });
        ep_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.getType(SettingsActivity.this).equals("law")){
                    Intent mainIntent = new Intent(getApplicationContext(), SignupLawyersActivity.class);
                    startActivity(mainIntent);
                }else{
                    viewFlipper.setDisplayedChild(1);
                    title.setText(Settings.getword(SettingsActivity.this, "Edit Profile"));
                }
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
                Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
        submit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(op.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"Enter Old Password"), Toast.LENGTH_SHORT).show();
                }else if(np.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"Enter New Password"), Toast.LENGTH_SHORT).show();
                }else if(!cp.getText().toString().matches(np.getText().toString())) {
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"Enter Same Password"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"empty_first_name"), Toast.LENGTH_SHORT).show();
                } else if(lname.getText().toString().equals("")){
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"empty_last_name"), Toast.LENGTH_SHORT).show();
                }else if(uname.getText().toString().equals("")){
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"empty_username"), Toast.LENGTH_SHORT).show();
                }else if(email.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"empty_email"), Toast.LENGTH_SHORT).show();
                } else if (!email.getText().toString().matches(emailPattern)){
                    Toast.makeText(SettingsActivity.this,Settings.getword(SettingsActivity.this,"empty_email_valid"), Toast.LENGTH_SHORT).show();
                }else if(phone.getText().toString().equals("")) {
                    Toast.makeText(SettingsActivity.this, Settings.getword(SettingsActivity.this,"empty_phone"), Toast.LENGTH_SHORT).show();
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
            title.setText(Settings.getword(SettingsActivity.this, "settings"));
        }else {
            super.onBackPressed();
            title.setText(Settings.getword(SettingsActivity.this, "Edit Profile"));
        }
    }
    Bitmap bitmap;
    String encodedString;
    boolean isimgchoosen = false;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;
    Bitmap sample;
    String imgPath = "-1";

    public void selectphotos() {
        final String[] items = new String[]{Settings.getword(this,"camera"), Settings.getword(this,"gallery")};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(Settings.getword(this,"select_image"));
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(),
                            "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    mImageCaptureUri = Uri.fromFile(file);
                    try {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dialog.cancel();
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(galleryIntent, PICK_FROM_FILE);
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_FROM_FILE && resultCode == RESULT_OK ) {
            mImageCaptureUri = data.getData();
            String path = getRealPathFromURI(mImageCaptureUri); //from Gallery

            if (path == null)
                path = mImageCaptureUri.getPath();
            //from File Manag\\
            if (path != null)
                imgPath = path;
            Intent intent = new Intent(this, ImageEditActivity.class);
            intent.putExtra("image_path", imgPath);
            intent.putExtra("image_source", "gallery");
            intent.putExtra("rotation", String.valueOf(getCameraPhotoOrientation(this,mImageCaptureUri,imgPath)));
            startActivityForResult(intent, 4);

        } else if (requestCode == PICK_FROM_CAMERA && resultCode == RESULT_OK ) {
            String path = mImageCaptureUri.getPath();
            imgPath = path;
            Intent intent = new Intent(this, ImageEditActivity.class);
            intent.putExtra("image_path", imgPath);
            intent.putExtra("image_source", "device_cam");
            intent.putExtra("rotation", String.valueOf(getCameraPhotoOrientation(this,mImageCaptureUri,imgPath)));
            startActivityForResult(intent, 4);

        }else if (requestCode == 4) {
            String file_path = data.getStringExtra("image_path");
            Log.e("ile_path", file_path);
            sample = BitmapFactory.decodeFile(file_path);

            //Picasso.with(this).load(new File(file_path)).rotate(getCameraPhotoOrientation(this,mImageCaptureUri,file_path))
            //  .into(profile_image);
            imgPath = file_path;
            user_img.setImageBitmap(sample);
        }else{
            Log.e("activity", "not returned");
        }
    }
    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(column_index);
    }
    public int getCameraPhotoOrientation(Context context, Uri imageUri, String imagePath){
        int rotate = 0;
        try {
            //  context.getContentResolver().notifyChange(imageUri, null);
            File imageFile = new File(imagePath);

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }

            Log.i("RotateImage", "Exif orientation: " + orientation);
            Log.i("RotateImage", "Rotate value: " + rotate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }
    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            };

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                encodedString = "";
                bitmap = BitmapFactory.decodeFile(imgPath, options);
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

                user_image();


            }
        }.execute(null, null, null);
    }
    public  void user_image(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait.. uploading image");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.User_Add_image_url;
        Log.e("url", url);

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
                        viewFlipper.setDisplayedChild(0);
                        String msg=jsonObject.getString("message");
                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();

                    }else {
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
                params.put("member_id", Settings.getUserid(SettingsActivity.this));
                Log.e("l_id1", Settings.getUserid(SettingsActivity.this));
                params.put("file",encodedString);
                params.put("ext_str", "jpg");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public  void info(){
        String url = Settings.Member_info_url + "member_id="+ Settings.getUserid(this);

        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(SettingsActivity.this,"please_wait"));
//        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        u_name.setText(sub.getString("name"));
                        u_mail.setText(sub.getString("email"));
                        u_phone.setText(sub.getString("phone"));
                        Picasso.with(SettingsActivity.this).load(sub.getString("image")).into(u_img);
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
                Toast.makeText(SettingsActivity.this,Settings.getword(SettingsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    public void change_password(){
        final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
                    progressDialog.setMessage(Settings.getword(SettingsActivity.this,"please_wait"));
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
        progressDialog.setMessage(Settings.getword(SettingsActivity.this,"please_wait"));
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
                        String msg=jsonObject.getString("message");
                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (imgPath.equals("-1")) {
                            viewFlipper.setDisplayedChild(0);
                        }else{
                            encodeImagetoString();
                        }
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
        progressDialog.setMessage(Settings.getword(SettingsActivity.this,"please_wait"));
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
                        Picasso.with(SettingsActivity.this).load(sub.getString("image")).into(u_img);
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
                Toast.makeText(SettingsActivity.this,Settings.getword(SettingsActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
