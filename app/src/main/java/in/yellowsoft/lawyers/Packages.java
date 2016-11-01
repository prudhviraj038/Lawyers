package in.yellowsoft.lawyers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HP on 27-Oct-16.
 */
public class Packages implements Serializable {
    String id,title,title_ar,price,validity,messages,go_icon,timeline,social,description,description_ar;
    Packages(JSONObject jsonObject){
        try {
            id=jsonObject.getString("id");
            title=jsonObject.getString("title");
            title_ar=jsonObject.getString("title_ar");
            price=jsonObject.getString("price");
            validity=jsonObject.getString("validity");
            messages=jsonObject.getString("messages");
            go_icon=jsonObject.getString("go_icon");
            timeline=jsonObject.getString("timeline");
            social=jsonObject.getString("social");
            description=jsonObject.getString("description");
            description_ar=jsonObject.getString("description_ar");
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
    public String getDescription(Context context) {
        if(Settings.get_user_language(context).equals("ar"))
            return description_ar;
        else
            return  description;
    }
}
