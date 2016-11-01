package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PackagesAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<Packages> packages;
//    ArrayList<Review> comments;
    private static LayoutInflater inflater=null;
    public PackagesAdapter(Activity mainActivity, ArrayList<Packages> packages) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        //  imageId=prgmImages;
        this.packages=packages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  packages.size();
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
        TextView pack_name,pack_description;
        CircleImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.packages_item, null);
        holder.pack_name=(TextView) rowView.findViewById(R.id.package_name);
        holder.pack_description=(TextView) rowView.findViewById(R.id.pack_description);
        holder.pack_name.setText(packages.get(position).getTitle(context));
        holder.pack_description.setText(Html.fromHtml(packages.get(position).getDescription(context)));
//        holder.img=(CircleImageView) rowView.findViewById(R.id.lawyer_img);
//        Picasso.with(context).load(comments.get(position).mem_img).into(holder.img);
        return rowView;
    }

}