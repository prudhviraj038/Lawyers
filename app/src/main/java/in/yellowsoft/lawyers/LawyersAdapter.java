package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LawyersAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<Lawyers> lawyers;
    private static LayoutInflater inflater=null;
    public LawyersAdapter(Activity mainActivity, ArrayList<Lawyers> lawyers) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        //  imageId=prgmImages;
        this.lawyers=lawyers;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  lawyers.size();
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
        TextView law_name,law_descri,review;
        CircleImageView img;
        LinearLayout rating_ll;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        if (convertView == null)
            rowView = inflater.inflate(R.layout.lawyers_item, null);
        else
            rowView = convertView;
        holder.law_name=(TextView) rowView.findViewById(R.id.law_name);
        holder.law_descri=(TextView) rowView.findViewById(R.id.law_descri);
        holder.review=(TextView) rowView.findViewById(R.id.law_reviws);
        holder.img=(CircleImageView) rowView.findViewById(R.id.lawyer_img);
        holder.rating_ll=(LinearLayout)rowView.findViewById(R.id.law_rating);
        Settings.set_rating_yellow(context, lawyers.get(position).rating, holder.rating_ll);

        holder.law_name.setText(lawyers.get(position).name);
        holder.law_descri.setText(Html.fromHtml(lawyers.get(position).about));
        holder.review.setText(Settings.getword(context,"reviews")+" ("+lawyers.get(position).reviews+")");
//        Log.e("image",lawyers.get(position).image);
        Picasso.with(context).load(lawyers.get(position).image).into(holder.img);
        return rowView;
    }

}