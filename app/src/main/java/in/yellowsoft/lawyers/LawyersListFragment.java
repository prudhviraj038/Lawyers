package in.yellowsoft.lawyers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Chinni on 04-05-2016.
 */
public class LawyersListFragment extends Fragment {
    String pos;
    ListView listView;
    LawyersAdapter lawyersAdapter;
    ProgressBar progressBar;
    ArrayList<Lawyers> lawyers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.lawyers_list_screen, container, false);
        return rootview;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getView();
        lawyers=new ArrayList<>();

        get_lawyers();
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        pos=(String)getArguments().getSerializable("id");
        listView=(ListView)view.findViewById(R.id.company_list);
//        progressBar.setVisibility(View.VISIBLE);
        lawyersAdapter=new LawyersAdapter(getActivity(),lawyers);
        listView.setAdapter(lawyersAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mainIntent = new Intent(getActivity(), LawyerDetailsActivity.class);
                mainIntent.putExtra("lawyers", lawyers.get(position));
                startActivity(mainIntent);

            }
        });

    }

    public static LawyersListFragment newInstance(String id) {
        LawyersListFragment companyListFragment = new LawyersListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            companyListFragment.setArguments(bundle);
                    return companyListFragment;
    }
    private void get_lawyers() {
        String url = Settings.Lawyers_url;
        Log.e("url--->", url);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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

                        if(pos.equals("0")){
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
                Toast.makeText(getActivity(), "Server not connected", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });
        AppController.getInstance().addToRequestQueue(jsObjRequest);

    }
}