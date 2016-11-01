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

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewsAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
//    ArrayList<Review> comments;
        Lawyers lawyers;
    private static LayoutInflater inflater=null;
    public ReviewsAdapter(Activity mainActivity, Lawyers lawyers) {
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
        return  lawyers.review.size();
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
        TextView rev_name,rev_date,rev_description;
        CircleImageView img;
        LinearLayout rating_ll;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.review_item, null);
        holder.rating_ll=(LinearLayout)rowView.findViewById(R.id.review_rating_ll);
        Settings.set_rating_yellow(context,lawyers.review.get(position).rating,holder.rating_ll);
        holder.rev_name=(TextView) rowView.findViewById(R.id.rev_name);
        holder.rev_date=(TextView) rowView.findViewById(R.id.rev_date);
        holder.rev_description=(TextView) rowView.findViewById(R.id.rev_description);
        holder.img=(CircleImageView) rowView.findViewById(R.id.rev_img);
        Picasso.with(context).load(lawyers.review.get(position).image).into(holder.img);
        holder.rev_name.setText(lawyers.review.get(position).name);
        holder.rev_date.setText(lawyers.review.get(position).date);
        holder.rev_description.setText(lawyers.review.get(position).review);
        return rowView;
    }

}