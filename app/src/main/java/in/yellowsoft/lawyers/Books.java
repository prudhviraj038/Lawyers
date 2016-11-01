package in.yellowsoft.lawyers;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HP on 29-Oct-16.
 */
public class Books implements Serializable{
    String id,title,title_ar,pdf;
    Books(JSONObject jsonObject){
        try {
            id=jsonObject.getString("id");
            title=jsonObject.getString("title");
            title_ar=jsonObject.getString("title_ar");
            pdf=jsonObject.getString("pdf");
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
