package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatingAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    Lawyers lawyers;
    private static LayoutInflater inflater=null;
    public ChatingAdapter(Activity mainActivity) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        //  imageId=prgmImages;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  10;
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
        TextView chating_tv;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.chating_item, null);
        holder.chating_tv=(TextView) rowView.findViewById(R.id.chating_tv);
//        holder.name.setText(position+1+"."+lawyers.area.get(position).getTitle(context));
        if(position%2==0){
            holder.chating_tv.setGravity(Gravity.RIGHT);
            holder.chating_tv.setText("outgoing");
        }else{
            holder.chating_tv.setGravity(Gravity.LEFT);
            holder.chating_tv.setText("incoming");
        }
        return rowView;
    }

}