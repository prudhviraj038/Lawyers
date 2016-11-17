package in.yellowsoft.lawyers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by HP on 27-Oct-16.
 */
public class Lawyers implements Serializable {
    String id,name,email,phone,licenced,latitude,longitude,location,about,image,p_id,p_title,p_title_ar,rating,reviews,bg_image,
    p_mes,p_go,p_tl,p_social,status_sec,status_time;
    ArrayList<Area> area;
    ArrayList<Review> review;
    Lawyers(JSONObject jsonObject){
        area=new ArrayList<>();
        review=new ArrayList<>();
        try {
            id=jsonObject.getString("id");
            name=jsonObject.getString("name");
            email=jsonObject.getString("email");
            phone=jsonObject.getString("phone");
            licenced=jsonObject.getString("licenced");
            latitude=jsonObject.getString("latitude");
            longitude=jsonObject.getString("longitude");
            location=jsonObject.getString("location");
            about=jsonObject.getString("about");
            rating=jsonObject.getString("rating");
            reviews=jsonObject.getString("reviews");
            image=jsonObject.getString("image");
            bg_image=jsonObject.getString("background_image");
            status_time=jsonObject.getJSONObject("status").getString("time");
            status_sec=jsonObject.getJSONObject("status").getString("seconds");
            p_id=jsonObject.getJSONObject("package").getString("id");
            p_title=jsonObject.getJSONObject("package").getString("title");
            p_title_ar=jsonObject.getJSONObject("package").getString("title_ar");
            p_mes=jsonObject.getJSONObject("package").getString("messages");
            p_go=jsonObject.getJSONObject("package").getString("go_icon");
            p_tl=jsonObject.getJSONObject("package").getString("timeline");
            p_social=jsonObject.getJSONObject("package").getString("social");
            for(int i=0;i<jsonObject.getJSONArray("areas").length();i++){
                JSONObject temp=jsonObject.getJSONArray("areas").getJSONObject(i);
                Area pro=new Area(temp);
                this.area.add(pro);
            }
            for(int i=0;i<jsonObject.getJSONArray("reviews_list").length();i++){
                JSONObject temp=jsonObject.getJSONArray("reviews_list").getJSONObject(i);
                Review pro1=new Review(temp);
                this.review.add(pro1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getPackagesTitle(Context context) {
        if(Settings.get_user_language(context).equals("ar"))
            return p_title_ar;
        else
            return  p_title;
    }
    public class Area implements Serializable  {
        String id,title,title_ar;
        Area(JSONObject jsonObject){
            try {
                id=jsonObject.getString("id");
                title=jsonObject.getString("title");
                title_ar=jsonObject.getString("title_ar");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        public String getTitle(Context context) {
            if(Settings.get_user_language(context).equals("ar"))
                return title_ar;
            else
                return  title;
        }
    }
    public class Review implements Serializable  {
        String review,rating,name,image,date;
        Review(JSONObject jsonObject){
            try {
                review=jsonObject.getString("review");
                rating=jsonObject.getString("rating");
                name=jsonObject.getString("name");
                image=jsonObject.getString("image");
                date=jsonObject.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
