package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;


public class LawyersActivity extends FragmentActivity {
    ListView listView;
    LinearLayout all,online;
    TextView all_tv,online_tv;
    LawyersAdapter lawyersAdapter;
    ImageView load,books,settings,filter,back;
    ArrayList<Lawyers> lawyers;
    ArrayList<String> name;
    PagerSlidingTabStrip pagerSlidingTabStrip;
    ViewPager viewPager;
    int temp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.lawyers_list);
        lawyers=new ArrayList<>();
//        get_lawyers();
        FragmentManager fragmentManager = getSupportFragmentManager();
        pagerSlidingTabStrip=(PagerSlidingTabStrip)findViewById(R.id.view3);
        viewPager=(ViewPager)findViewById(R.id.view2);
        pagerSlidingTabStrip.setTextColor(Color.parseColor("#ffffff"));
//        pagerSlidingTabStrip.setFillViewport(true);
        filter=(ImageView)findViewById(R.id.lawyers_filter);
        load=(ImageView)findViewById(R.id.load_img_2);
        books=(ImageView)findViewById(R.id.books_image_2);
        settings=(ImageView)findViewById(R.id.settings_img_2);
        back=(ImageView)findViewById(R.id.law_list_back);
        listView=(ListView)findViewById(R.id.law_list);
        all=(LinearLayout)findViewById(R.id.all_law_list_ll);
        online=(LinearLayout)findViewById(R.id.online_law_list_ll);
        all_tv=(TextView)findViewById(R.id.all_law_list_tv);
        online_tv=(TextView)findViewById(R.id.online_law_list_tv);
        lawyersAdapter=new LawyersAdapter(this,lawyers);
        listView.setAdapter(lawyersAdapter);
        all.setBackgroundResource(R.drawable.filter_empty_lawyer_rounded_corners);
        all_tv.setTextColor(Color.parseColor("#000000"));
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = 0;
                Log.e("tmp", String.valueOf(temp));
                all.setBackgroundResource(R.drawable.filter_empty_lawyer_rounded_corners);
                all_tv.setTextColor(Color.parseColor("#000000"));
                online.setBackgroundResource(R.drawable.signup_lawyer_rounded_corners);
                online_tv.setTextColor(Color.parseColor("#ffffff"));
                get_lawyers();
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = 1;
                Log.e("tmp", String.valueOf(temp));
                online.setBackgroundResource(R.drawable.filter_empty_lawyer_rounded_corners);
                online_tv.setTextColor(Color.parseColor("#000000"));
                all.setBackgroundResource(R.drawable.signup_lawyer_rounded_corners);
                all_tv.setTextColor(Color.parseColor("#ffffff"));
                get_lawyers();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(lawyers.get(position).p_tl.equals("Yes")){
//                    Settings.setLawTl(LawyersActivity.this,"Yes");
//                }else{
//                   Settings.setLawTl(LawyersActivity.this,"No");
//                }
                Intent mainIntent = new Intent(getApplicationContext(), LawyerDetailsActivity.class);
                mainIntent.putExtra("lawyers", lawyers.get(position));
                startActivity(mainIntent);

            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), TimelineActivity.class);
                startActivity(mainIntent);
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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Settings.getUserid(LawyersActivity.this).equals("-1")){
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(mainIntent);
            }
        });
         name=new ArrayList<>();
        name.add("All");
        name.add("Online");

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        pagerSlidingTabStrip.setViewPager(viewPager);


    }
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private final Map<Integer,LawyersListFragment> frags = new Hashtable<>();
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return name.get(position);
        }

        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public Fragment getItem(int position) {
            frags.put(position,LawyersListFragment.newInstance(String.valueOf(position)));
            //frags.put(position,new TestFragment());
            return frags.get(position);
        }

        public LawyersListFragment getthisfrag(int pos){

            return    frags.get(pos);
        }

    }

    private void get_lawyers() {
        String url = Settings.Lawyers_url;
        Log.e("url--->", url);
        Log.e("tmp",String.valueOf(temp));
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonObject) {
                lawyers.clear();
                progressDialog.dismiss();
                Log.e("response is: ", jsonObject.toString());
                try {
                    Lawyers pack;
                    for (int i = 0; i < jsonObject.length(); i++) {
                        JSONObject sub = jsonObject.getJSONObject(i);

                        if(temp==0){
                            pack=new Lawyers(sub);
                            lawyers.add(pack);
                        }else{
                            Log.e("status",sub.getJSONObject("status").getString("seconds"));
                            if(Integer.parseInt(sub.getJSONObject("status").getString("seconds"))<60) {
                                pack = new Lawyers(sub);
                                lawyers.add(pack);
                            }
                        }

                    }
                    listView.setAdapter(lawyersAdapter);
                    lawyersAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("response is:", error.toString());
                Toast.makeText(LawyersActivity.this, "Server not connected", Toast.LENGTH_SHORT).show();
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
