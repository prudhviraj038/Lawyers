package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyChatListAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
//    ArrayList<Review> comments;
ArrayList<Members_list> members_lists;
    private static LayoutInflater inflater=null;
    public MyChatListAdapter(Activity mainActivity, ArrayList<Members_list> members_lists) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        //  imageId=prgmImages;
        this.members_lists=members_lists;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  members_lists.size();
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
        TextView rev_name,rev_date,rev_description,cnt_tv;
        CircleImageView img;
        LinearLayout cnt_ll;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.mychat_item, null);
        holder.cnt_ll=(LinearLayout)rowView.findViewById(R.id.cnt_chat_list_ll);
        holder.cnt_tv=(TextView) rowView.findViewById(R.id.cnt_chat_list_tv);
        holder.rev_name=(TextView) rowView.findViewById(R.id.chat_mem_name);
        holder.rev_date=(TextView) rowView.findViewById(R.id.law_chat_list_date);
        holder.rev_description=(TextView) rowView.findViewById(R.id.law_msg_description);
        holder.img=(CircleImageView) rowView.findViewById(R.id.mychat_img);
        Picasso.with(context).load(members_lists.get(position).image).into(holder.img);
        holder.rev_name.setText(members_lists.get(position).name);
        holder.rev_date.setText(members_lists.get(position).date);
        holder.rev_description.setText(members_lists.get(position).msg);
        holder.cnt_tv.setText(members_lists.get(position).cnt);
        if(members_lists.get(position).cnt.equals("0"))
            holder.cnt_ll.setVisibility(View.GONE);
        else
            holder.cnt_ll.setVisibility(View.VISIBLE);
        return rowView;
    }

}