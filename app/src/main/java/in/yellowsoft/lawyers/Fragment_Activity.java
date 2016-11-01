package in.yellowsoft.lawyers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class Fragment_Activity extends FragmentActivity {
    FragmentManager fragmentManager;
    ImageView lawyers,load,books,sett;
    LinearLayout cir1,cir2,cir3,cir4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Settings.forceRTLIfSupported(this);
        setContentView(R.layout.fragment_activity);
        fragmentManager = getSupportFragmentManager();

        lawyers=(ImageView)findViewById(R.id.frg_lawyer_img);
        load=(ImageView)findViewById(R.id.frg_load_img);
        books=(ImageView)findViewById(R.id.frg_books_img);
        sett=(ImageView)findViewById(R.id.frg_sett_img);

        cir1=(LinearLayout)findViewById(R.id.cir1);
        cir2=(LinearLayout)findViewById(R.id.cir2);
        cir3=(LinearLayout)findViewById(R.id.cir3);
        cir4=(LinearLayout)findViewById(R.id.cir4);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                cir1.setVisibility(View.VISIBLE);
                Intent mainIntent = new Intent(Fragment_Activity.this, TimelineActivity.class);
                startActivity(mainIntent);
            }
        });
        lawyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                cir2.setVisibility(View.VISIBLE);
                Intent mainIntent = new Intent(Fragment_Activity.this, LawyersActivity.class);
                startActivity(mainIntent);
            }
        });

        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                cir3.setVisibility(View.VISIBLE);
                Intent mainIntent = new Intent(Fragment_Activity.this, BooksActivity.class);
                startActivity(mainIntent);
            }
        });
        sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                cir4.setVisibility(View.VISIBLE);
                Intent mainIntent = new Intent(Fragment_Activity.this, SettingsActivity.class);
                startActivity(mainIntent);
            }
        });
    }
    public void reset(){
        cir1.setVisibility(View.GONE);
        cir2.setVisibility(View.GONE);
        cir3.setVisibility(View.GONE);
        cir4.setVisibility(View.GONE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
