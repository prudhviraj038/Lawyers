package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatingAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<Chats> chats;
    private static LayoutInflater inflater=null;
    public ChatingAdapter(Activity mainActivity, ArrayList<Chats> chats) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        this.chats=chats;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  chats.size();
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
        TextView chating_tv_r,chating_tv_l,date_r,date_l;
        LinearLayout chat_ll_r,chat_ll_l,ch_ll_r,ch_ll_l;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.chating_item, null);
        holder.chat_ll_r=(LinearLayout) rowView.findViewById(R.id.chat_ll_right);
        holder.chat_ll_l=(LinearLayout) rowView.findViewById(R.id.chat_ll_left);
        holder.ch_ll_r=(LinearLayout) rowView.findViewById(R.id.ch_ll_r);
        holder.ch_ll_l=(LinearLayout) rowView.findViewById(R.id.ch_ll_l);
        holder.chating_tv_r=(TextView) rowView.findViewById(R.id.chating_tv_right);
        holder.chating_tv_l=(TextView) rowView.findViewById(R.id.chating_tv_left);
        holder.date_r=(TextView) rowView.findViewById(R.id.date_chating_right);
        holder.date_l=(TextView) rowView.findViewById(R.id.date_chating_left);
        String [] date_time=chats.get(position).date.split(" ");

//        holder.name.setText(position+1+"."+lawyers.area.get(position).getTitle(context));
        if(chats.get(position).send_id.equals(Settings.getUserid(context))) {
            holder.ch_ll_l.setVisibility(View.GONE);
            holder.ch_ll_r.setVisibility(View.VISIBLE);
            holder.chating_tv_r.setText(chats.get(position).msg);
            holder.date_r.setText(date_time[1]);
            holder.chat_ll_r.setBackgroundResource(R.drawable.yellow_rounded_corners_with_bo);
        } else {
            holder.ch_ll_l.setVisibility(View.VISIBLE);
            holder.ch_ll_r.setVisibility(View.GONE);
            holder.chating_tv_l.setText(chats.get(position).msg);
            holder.date_l.setText(date_time[1]);
            holder.chat_ll_l.setBackgroundResource(R.drawable.white_rounded_corners_with_bo);
        }
        return rowView;
    }

}