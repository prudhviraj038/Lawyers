package in.yellowsoft.lawyers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by HP on 05-Nov-16.
 */
public class Chats implements Serializable {
    String id,send_id,sender,receive_id,receiver,msg,date;
    Chats(JSONObject jsonObject){
        try {
            id=jsonObject.getString("id");
            send_id=jsonObject.getString("sender_id");
            sender=jsonObject.getString("sender");
            receive_id=jsonObject.getString("receiver_id");
            receiver=jsonObject.getString("message");
            msg=jsonObject.getString("message");
            date=jsonObject.getString("date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
