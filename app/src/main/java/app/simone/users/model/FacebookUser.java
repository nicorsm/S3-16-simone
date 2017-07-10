package app.simone.users.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;

/**
 * Created by nicola on 11/07/2017.
 */

public class FacebookUser extends RealmObject implements Serializable {


    public static final String kNAME = "name";
    public static final String kPICTURE = "picture";
    public static final String kID = "id";
    public static final String kSCORE = "score";
    public static final String kDATA = "data";

    private String id;
    private String name;
    private FacebookPicture picture;

    private String score;

    public FacebookUser() {

    }

    public FacebookUser(JsonElement json) {

        JsonObject obj = json.getAsJsonObject();

        this.name = obj.get(kNAME).getAsString();
        this.picture = new FacebookPicture(obj.get(kPICTURE).getAsJsonObject().get(kDATA).getAsJsonObject());
        this.id = obj.get(kID).getAsString();
    }

    public FacebookUser(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<FacebookUser> listFromJson(JsonArray json) {

        List<FacebookUser> objectFriends = new ArrayList<FacebookUser>();

        for(JsonElement item : json) {
            objectFriends.add(new FacebookUser(item));
        }

        return objectFriends;
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty(FacebookUser.kID, id);
        json.addProperty(FacebookUser.kNAME, name);
        json.addProperty(FacebookUser.kSCORE, score);
        return json;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public FacebookPicture getPicture() {
        return picture;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

}