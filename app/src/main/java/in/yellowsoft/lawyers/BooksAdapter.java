package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    ArrayList<Books> books;
    BooksActivity booksActivity;
    private static LayoutInflater inflater=null;
    public BooksAdapter(Activity mainActivity, ArrayList<Books> books, BooksActivity booksActivity) {
        // TODO Auto-generated constructor stub
        //  result=prgmNameList;
        context=mainActivity;
        //  imageId=prgmImages;
        this.booksActivity=booksActivity;
        this.books=books;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//        return place.reviewses.size();
        return  books.size();
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
        TextView name;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.books_item, null);
        holder.name=(TextView) rowView.findViewById(R.id.book_name);
        holder.name.setText(position+1+"."+books.get(position).getTitle(context));
        return rowView;
    }

}