package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BooksActivity extends Activity {
    ListView listView;
    BooksAdapter booksAdapter;
    ImageView back,back1;
    ViewFlipper viewFlipper;
    WebView webView;
    String url;
    ImageView load,settings,lawyer;
    ArrayList<Books> books;
//    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.books_activity);
        books=new ArrayList<>();
        get_books();
        webView=(WebView)findViewById(R.id.books_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "app");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new MyWebViewClient());
        viewFlipper=(ViewFlipper)findViewById(R.id.viewFlipper3);
        load=(ImageView)findViewById(R.id.load_img_3);
        lawyer=(ImageView)findViewById(R.id.lawyers_img_3);
        settings=(ImageView)findViewById(R.id.settings_img_3);
        back=(ImageView)findViewById(R.id.books_back);
        back1=(ImageView)findViewById(R.id.books_back1);
        listView=(ListView)findViewById(R.id.books_list);
        booksAdapter=new BooksAdapter(this, books, BooksActivity.this);
        listView.setAdapter(booksAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewFlipper.setDisplayedChild(1);
                Log.e("pdf",books.get(position).pdf);
                webView.loadUrl(books.get(position).pdf);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.setDisplayedChild(0);
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
        lawyer.setOnClickListener(new View.OnClickListener() {
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
                if(Settings.getUserid(BooksActivity.this).equals("-1")){
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }else {
                    Intent mainIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        });
    }
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void send_message(String toast,Boolean success) {

        }
    }
    private class MyWebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            BooksActivity.this.setValue(newProgress);
            super.onProgressChanged(view, newProgress);
        }
    }
    public void setValue(int progress) {
//        this.progress.setProgress(progress);
    }

    @Override
    public void onBackPressed() {
        if(viewFlipper.getDisplayedChild()==1){
            viewFlipper.setDisplayedChild(0);
        }else{
            super.onBackPressed();
        }

    }
    private void get_books() {
        String url;
//        if(type.equals("law"))
//            url = Settings.Posts_url+ "lawyer_id="+Settings.getLawyerUserid(this);
//        else
        url = Settings.Books_url;
//                    + "lawyer_id="+Settings.getUserid(this);
        Log.e("url--->", url);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(Settings.getword(this,"please_wait"));
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                progressDialog.dismiss();
                books.clear();
                Log.e("response is: ", jsonObject.toString());
                try {

                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);
                        Books pack=new Books(sub);
                        books.add(pack);
                    }
                    booksAdapter=new BooksAdapter(BooksActivity.this,books, BooksActivity.this);
                    listView.setAdapter(booksAdapter);
                    booksAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(BooksActivity.this, Settings.getword(BooksActivity.this,"server_not_connected"), Toast.LENGTH_SHORT).show();
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
