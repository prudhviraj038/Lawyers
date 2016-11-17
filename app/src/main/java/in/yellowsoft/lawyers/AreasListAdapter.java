package in.yellowsoft.lawyers;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class AreasListAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<String> titles;
    ArrayList<String> s_ids;
//    ArrayList<String> ids;
    HashMap<String, String> areas;
    private static LayoutInflater inflater=null;
    public AreasListAdapter(Context mainActivity, ArrayList<String> s_ids, ArrayList<String> titles, HashMap<String, String> areas) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        this.titles = titles;
        this.areas = areas;
        this.s_ids = s_ids;
        //  imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;


    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_item, null);
        holder.tv=(TextView) rowView.findViewById(R.id.checkedTextView2);
        Log.e("title_area", titles.get(position));
        holder.img=(ImageView)rowView.findViewById(R.id.tick_area);
        holder.tv.setText(titles.get(position));

            if (areas.containsKey(s_ids.get(position))) {

                holder.img.setImageResource(R.drawable.tick);
            }
            else {

                holder.img.setImageResource(R.drawable.tick_empty);
            }
//        for(int i=0;i<ids.size();i++){
//                if(ids.get(i).equals(s_ids.get(position))){
//                    Log.e("id",ids.get(i));
//                    ((ListView) parent).setItemChecked(position, true);
////                     holder.tv.setEnabled(true);
//            }
//        }
        return rowView;
    }

}