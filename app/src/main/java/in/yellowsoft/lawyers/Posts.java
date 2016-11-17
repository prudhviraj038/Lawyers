package in.yellowsoft.lawyers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HP on 29-Oct-16.
 */
public class Posts implements Serializable{
    String id,title,message,image,video,date;
    Posts(JSONObject jsonObject){
        try {
            id=jsonObject.getString("id");
            title=jsonObject.getString("title");
            message=jsonObject.getString("message");
            image=jsonObject.getString("image");
            video=jsonObject.getString("video");
            date=jsonObject.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
