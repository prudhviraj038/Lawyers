package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignupLawyersActivity extends Activity {
    EditText fname,lname,uname,pass,phone,about;
    LinearLayout register,areas_pop,area_ll,banner_ll,cal,pass_ll;
    TextView email,register_tv,areas_tv,areas_title_tv,banner_tv,licenced,location;
    ListView listview;
    ImageView close,banner;
    CircleImageView law_img;
    HashMap<String, String> areas;
    HashMap<String, String> areas_t;
    AreasListAdapter areasListAdapter;
    ArrayList<String> area_id;
    ArrayList<String> area_title;
    private int mYear, mMonth, mDay;
    String date,ar;
    String lat="",lon="",adr="";
    Boolean is_edit=false;
    Boolean is_location_added=false;
    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.signup_lawyers_activity);
        email=(TextView)findViewById(R.id.law_signup_email);
        pass_ll=(LinearLayout)findViewById(R.id.pass_ll_law_signup);
        pass_ll.setVisibility(View.VISIBLE);
        Uri data = getIntent().getData();
        if(getIntent().getData()==null){
            is_edit=true;
            get_lawyer_details();
            pass_ll.setVisibility(View.GONE);
        }else {
            String scheme = data.getScheme(); // "http"
            String host = data.getHost(); // "twitter.com"
            List<String> params = data.getPathSegments();

            for (int i = 0; i < params.size(); i++) {
                Log.e("prams_size", params.get(i));
            }
            String first = params.get(0); // "status"
            String second = params.get(1);
            email.setText(second);
            pass_ll.setVisibility(View.VISIBLE);
        }
        areas = new HashMap<>();
        areas_t = new HashMap<>();
        area_id=new ArrayList<>();
        area_title=new ArrayList<>();
        get_areas();
        law_img=(CircleImageView)findViewById(R.id.lawyer_image);
        fname=(EditText)findViewById(R.id.law_signup_fname);
        fname.setHint(Settings.getword(this,"First Name"));
        lname=(EditText)findViewById(R.id.law_signup_lname);
        lname.setHint(Settings.getword(this,"Last Name"));
        uname=(EditText)findViewById(R.id.law_signup_uname);
        uname.setHint(Settings.getword(this,"Name"));
        pass=(EditText)findViewById(R.id.law_signup_pass);
        pass.setHint(Settings.getword(this,"Password"));
        phone=(EditText)findViewById(R.id.law_signup_phone);
        phone.setHint(Settings.getword(this,"Phone Number"));
        about=(EditText)findViewById(R.id.law_signup_about);
        about.setHint(Settings.getword(this, "About"));
        location=(TextView)findViewById(R.id.law_signup_location);
        location.setText(Settings.getword(this, "Location"));


        licenced=(TextView)findViewById(R.id.law_signup_licenced);
        licenced.setText(Settings.getword(this, "Licensed"));
        register_tv=(TextView)findViewById(R.id.law_register_tv);
        register_tv.setText(Settings.getword(this, "register"));
        areas_tv=(TextView)findViewById(R.id.signup_pr_area_tv);
        areas_tv.setText(Settings.getword(this, "Practice Areas"));
        areas_title_tv=(TextView)findViewById(R.id.areas_title);
        areas_title_tv.setText(Settings.getword(this, "Practice Areas"));
        banner_tv=(TextView)findViewById(R.id.law_banner_tv);
        banner_tv.setText(Settings.getword(this, "Banner Image"));

        register=(LinearLayout)findViewById(R.id.law_register_ll);
        area_ll=(LinearLayout)findViewById(R.id.signup_pr_area_ll);
        areas_pop=(LinearLayout)findViewById(R.id.areas_pop);
        banner_ll=(LinearLayout)findViewById(R.id.law_banner_ll);
        cal=(LinearLayout)findViewById(R.id.calender_ll);

        banner=(ImageView)findViewById(R.id.law_banner_img);
        close=(ImageView)findViewById(R.id.law_sign_pop_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String csv = "-1";
                areas_pop.setVisibility(View.GONE);
                for (Map.Entry<String, String> entry : areas_t.entrySet()) {
                    if (csv.equals("-1"))
                        csv = entry.getValue();
                    else
                        csv = csv + "," + entry.getValue();
                }
                areas_tv.setText(csv);
            }
        });
        area_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areas_pop.setVisibility(View.VISIBLE);
            }
        });
        banner_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        law_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectphotos();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupLawyersActivity.this,MapActivity.class);
                startActivityForResult(intent, 201);
            }
        });
        if(is_edit){
            register_tv.setText(Settings.getword(this, "update"));
        }else{
            register_tv.setText(Settings.getword(this, "register"));
        }
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(SignupLawyersActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String temp = String.valueOf(monthOfYear + 1);
                        if (temp.length() < 2)
                            temp = "0" + temp;
                        String temp1 = String.valueOf(dayOfMonth);
                        if (temp1.length() < 2)
                            temp1 = "0" + temp1;
                        date = temp1 + "-" + temp + "-" + year;
//                        date1 = temp1 + "-" + temp + "-" + year;
                        licenced.setText(date);
//                        time_ll.performClick();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });
        listview=(ListView)findViewById(R.id.area_listview);
        areasListAdapter=new AreasListAdapter(this,area_id,area_title,areas);
        listview.setAdapter(areasListAdapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setItemsCanFocus(false);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ar = area_id.get(position);
//                areas_pop.setVisibility(View.GONE);
//                areas_tv.setText(ar);
                if (areas.containsKey(area_id.get(position))) {
                    areas.remove(area_id.get(position));
                    areas_t.remove(area_id.get(position));
                }
                else {
                    areas.put(area_id.get(position), area_id.get(position));
                    areas_t.put(area_id.get(position), area_title.get(position));
                }
                JSONObject jsonObject = new JSONObject(areas);
                Log.e("areas", jsonObject.toString());
                areasListAdapter.notifyDataSetChanged();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!is_edit) {
                  if (uname.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_name"), Toast.LENGTH_SHORT).show();
                  } else if (email.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this,Settings.getword(SignupLawyersActivity.this,"empty_email"), Toast.LENGTH_SHORT).show();
                  } else if (!email.getText().toString().matches(emailPattern)) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_email_valid"), Toast.LENGTH_SHORT).show();
                  } else if (pass.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_password"), Toast.LENGTH_SHORT).show();
                  } else if (phone.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_phone"), Toast.LENGTH_SHORT).show();
                  } else if (date.equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_licence"), Toast.LENGTH_SHORT).show();
                  } else if (location.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_location"), Toast.LENGTH_SHORT).show();
                  } else if (about.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_about"), Toast.LENGTH_SHORT).show();
//                } else if (areas.size()==0) {
//                    Toast.makeText(SignupLawyersActivity.this,Settings.getword(SignupLawyersActivity.this,"empty_area"), Toast.LENGTH_SHORT).show();
                  } else {
//                    viewFlipper.setDisplayedChild(0);
                      register();
                  }
              }else{
                  if (uname.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_name"), Toast.LENGTH_SHORT).show();
                  } else if (email.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this,Settings.getword(SignupLawyersActivity.this,"empty_email"), Toast.LENGTH_SHORT).show();
                  } else if (!email.getText().toString().matches(emailPattern)) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_email_valid"), Toast.LENGTH_SHORT).show();
                  } else if (phone.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_phone"), Toast.LENGTH_SHORT).show();
                  } else if (date.equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_licence"), Toast.LENGTH_SHORT).show();
                  } else if (location.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_location"), Toast.LENGTH_SHORT).show();
                  } else if (about.getText().toString().equals("")) {
                      Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"empty_about"), Toast.LENGTH_SHORT).show();
//                } else if (areas.size()==0) {
//                    Toast.makeText(SignupLawyersActivity.this,Settings.getword(SignupLawyersActivity.this,"empty_area"), Toast.LENGTH_SHORT).show();
                  } else {
//                    viewFlipper.setDisplayedChild(0);
                      register();
                  }
              }
            }

        });
        Log.e(uname.getText().toString()+1, email.getText().toString() + pass.getText().toString()
                + phone.getText().toString() + licenced.getText().toString() + location.getText().toString() + about.getText().toString()
                + ar);
    }public  void register(){
        String url="";
        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage(Settings.getword(this, "please_wait"));
        progressDialog.setMessage(Settings.getword(SignupLawyersActivity.this,"please_wait"));
        progressDialog.show();
        progressDialog.setCancelable(false);
        if(is_edit){
             url=Settings.law_edit_profile_url;
        }else{
             url=Settings.law_signup_url;
        }
            Log.e("url",url);
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
                        Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(is_edit) {
                            String msg = jsonObject.getString("message");
                            Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (imgPath.equals("-1")) {
                                if (!isimgchoosen) {
                                    finish();
                                } else {
                                    encodeImagetoString_banner();
                                }
                            } else {
                                encodeImagetoString_lawyer();
                            }

                        } else{
                            String mem_id = jsonObject.getString("lawyer_id");
                            String msg = jsonObject.getString("message");
                            Settings.setLawyerUserid(SignupLawyersActivity.this, mem_id, "law");
                            Settings.setUserid(SignupLawyersActivity.this, mem_id, "law");
                            Log.e("l_id", mem_id);
                            Log.e("imgPath", imgPath);
                            Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (imgPath.equals("-1")) {
                                if (!isimgchoosen) {
                                    Intent intent = new Intent(SignupLawyersActivity.this, TimelineActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    encodeImagetoString_banner();
                                }
                            } else {
                                encodeImagetoString_lawyer();
                            }
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
//                        Toast.makeText(SignupLawyersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e(uname.getText().toString(), email.getText().toString() + pass.getText().toString()
                        + phone.getText().toString() + licenced.getText().toString() + location.getText().toString() + about.getText().toString()
                        + ar);
                params.put("name", uname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("licenced", licenced.getText().toString());
                params.put("location", location.getText().toString());
                params.put("latitude", lat);
                params.put("longitude", lon);
                params.put("about", about.getText().toString());
                String csv = "-1";
                for (Map.Entry<String, String> entry : areas.entrySet()) {
                    if (csv.equals("-1"))
                        csv = entry.getValue();
                    else
                        csv = csv + "," + entry.getValue();
                }
                params.put("areas", csv);
                if (is_edit) {
                    params.put("lawyer_id", Settings.getUserid(SignupLawyersActivity.this));
                }else{
                    params.put("password", pass.getText().toString());
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void get_areas() {
        String url = Settings.Areas_url;
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(SignupLawyersActivity.this,"please_wait"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    JSONObject sub = jsonObject.getJSONObject(0);
                    for (int i = 0; i < sub.getJSONArray("areas").length(); i++) {
                        area_id.add(sub.getJSONArray("areas").getJSONObject(i).getString("id"));
                        area_title.add(sub.getJSONArray("areas").getJSONObject(i).getString("title"+Settings.get_lan(SignupLawyersActivity.this)));
                    }
                    areasListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(SignupLawyersActivity.this,Settings.getword(SignupLawyersActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
    Bitmap bitmap;
    String encodedString;
    String imgDecodableString;
    final int RESULT_LOAD_IMAGE = 3;
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

        }
        else if (requestCode == 4) {
            String file_path = data.getStringExtra("image_path");
            Log.e("ile_path", file_path);
            sample = BitmapFactory.decodeFile(file_path);

            //Picasso.with(this).load(new File(file_path)).rotate(getCameraPhotoOrientation(this,mImageCaptureUri,file_path))
            //  .into(profile_image);
            imgPath = file_path;
            law_img.setImageBitmap(sample);
        }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
//            ImageView imgView = (ImageView) findViewById(R.id.ic_upload_logo);
            // Set the Image in ImageView after decoding the String
            banner.setImageBitmap(BitmapFactory
                    .decodeFile(imgDecodableString));
//            Picasso.with(SignupLawyersActivity.this).load(BitmapFactory.decodeFile(imgDecodableString)).into(banner);
            isimgchoosen = true;
        } else if(requestCode == 201) {
            if(data!=null) {
                is_location_added = true;
                lat = data.getStringExtra("lat");
                lon = data.getStringExtra("lon");
                adr = data.getStringExtra("adr");
                Log.e("addrerss", adr);
                location.setText(adr);
            }
        }else{
            Log.e("activity","not returned");
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
    public void encodeImagetoString_lawyer() {
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

                    law_image();


            }
        }.execute(null, null, null);
    }
    public void encodeImagetoString_banner() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

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

                    law_Banner_image();


            }
        }.execute(null, null, null);
    }

    public  void law_image(){
        final ProgressDialog progressDialog = new ProgressDialog(SignupLawyersActivity.this);
        progressDialog.setMessage("please wait.. uploading image");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.law_Add_image_url;
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
                        String msg = jsonObject.getString("message");
                        Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(isimgchoosen) {
                            encodeImagetoString_banner();
                        }else{
                            if(is_edit){
                                finish();
                            }else {
                                Intent intent = new Intent(SignupLawyersActivity.this, TimelineActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SignupLawyersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("lawyer_id",Settings. getLawyerUserid(SignupLawyersActivity.this));
                Log.e("l_id1", Settings.getLawyerUserid(SignupLawyersActivity.this));
                params.put("file",encodedString);
                params.put("ext_str", "jpg");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public  void law_Banner_image(){
        final ProgressDialog progressDialog = new ProgressDialog(SignupLawyersActivity.this);
        progressDialog.setMessage("please wait.. uploading image");
        progressDialog.show();
        progressDialog.setCancelable(false);
        String url = Settings.law_Add_Banner_image_url;
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
                        String msg = jsonObject.getString("message");
                        Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if(is_edit){
                            finish();
                        }else {
                            Intent intent = new Intent(SignupLawyersActivity.this, TimelineActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else {
                        String msg=jsonObject.getString("message");
                        Toast.makeText(SignupLawyersActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SignupLawyersActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("lawyer_id",Settings. getLawyerUserid(SignupLawyersActivity.this));
                Log.e("l_id2", Settings. getLawyerUserid(SignupLawyersActivity.this));
                params.put("file",encodedString);
                params.put("ext_str", "jpg");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    private void get_lawyer_details() {
        String url = Settings.Lawyers_url+"?&lawyer_id="+Settings.getUserid(SignupLawyersActivity.this);
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(SignupLawyersActivity.this,"please_wait"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        uname.setText(sub.getString("name"));
                        email.setText(sub.getString("email"));
                        phone.setText(sub.getString("phone"));
                        licenced.setText(sub.getString("licenced"));
                        date=sub.getString("licenced");
                        location.setText(sub.getString("location"));
                        lat=sub.getString("latitude");
                        lon=sub.getString("longitude");
                        about.setText(sub.getString("about"));
                        areas_tv.setText(sub.getJSONArray("areas").getJSONObject(0).getString("title" + Settings.get_lan(SignupLawyersActivity.this)));
                        ar=sub.getJSONArray("areas").getJSONObject(0).getString("id");
                        Picasso.with(SignupLawyersActivity.this).load(sub.getString("image")).into(law_img);
                        Picasso.with(SignupLawyersActivity.this).load(sub.getString("background_image")).into(banner);
                        for(int j=0;j<sub.getJSONArray("areas").length();j++){
                            areas.put(sub.getJSONArray("areas").getJSONObject(0).getString("id"),sub.getJSONArray("areas").getJSONObject(0).getString("id"));
                            areas_t.put(sub.getJSONArray("areas").getJSONObject(0).getString("id"),sub.getJSONArray("areas").getJSONObject(0).getString("title" + Settings.get_lan(SignupLawyersActivity.this)));
                        }
                        areasListAdapter.notifyDataSetChanged();
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
                Toast.makeText(SignupLawyersActivity.this, Settings.getword(SignupLawyersActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
