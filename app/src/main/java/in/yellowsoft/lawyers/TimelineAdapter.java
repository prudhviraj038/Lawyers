package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TimelineAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<Posts> posts;
    TimelineActivity timelineActivity;
    private static LayoutInflater inflater=null;
    public TimelineAdapter(Activity mainActivity, ArrayList<Posts> posts, TimelineActivity timelineActivity) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        //  imageId=prgmImages;
        this.posts=posts;
        this.timelineActivity=timelineActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  posts.size();
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
        TextView head,description,time,date,read_tv;
        LinearLayout read_ll,ads_ll,tl_ll;
        ImageView share,timeline_img,ply_btn,ads_img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        if (convertView == null)
            rowView = inflater.inflate(R.layout.timeline_item, null);
        else
            rowView = convertView;
        holder.head=(TextView) rowView.findViewById(R.id.tim_lin_header);
        holder.description=(TextView) rowView.findViewById(R.id.time_line_des);
        holder.time=(TextView) rowView.findViewById(R.id.time_line_time);
        holder.date=(TextView) rowView.findViewById(R.id.time_line_date);
        holder.read_tv=(TextView) rowView.findViewById(R.id.read_more);
        holder.read_tv.setText(Settings.getword(context,"Read More"));
        holder.read_ll=(LinearLayout) rowView.findViewById(R.id.read_more_ll);
        holder.ads_ll=(LinearLayout) rowView.findViewById(R.id.ads_ll);
        holder.tl_ll=(LinearLayout) rowView.findViewById(R.id.tl_item_ll);
        holder.share=(ImageView) rowView.findViewById(R.id.time_line_share);
        holder.ads_img=(ImageView) rowView.findViewById(R.id.ads_img);
        holder.timeline_img=(ImageView) rowView.findViewById(R.id.time_line_img);
        holder.ply_btn=(ImageView) rowView.findViewById(R.id.ply_btn_tl_list);
        if(posts.get(position).id.equals("0")){
            holder.tl_ll.setVisibility(View.GONE);
            holder.ads_ll.setVisibility(View.VISIBLE);
            Picasso.with(context).load(posts.get(position).image).into(holder.ads_img);

        }else{
            holder.tl_ll.setVisibility(View.VISIBLE);
            holder.ads_ll.setVisibility(View.GONE);
        }
        if(posts.get(position).video.equals("")){
            holder.ply_btn.setVisibility(View.GONE);
        }else{
            holder.ply_btn.setVisibility(View.VISIBLE);
        }
        Picasso.with(context).load(posts.get(position).image).into(holder.timeline_img);
        holder.ply_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelineActivity.play_video(position);
            }
        });

        holder.head.setText(posts.get(position).title);
        holder.description.setText(posts.get(position).message);
        holder.date.setText(posts.get(position).date);
        holder.read_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelineActivity.dis_des(position);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timelineActivity.shareTextUrl(position);
            }
        });
        return rowView;
    }

}