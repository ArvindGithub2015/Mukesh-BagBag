package zuzusoft.com.bagbag.helper.notification;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FCMPayload implements Serializable {

    @SerializedName("team")
    @Expose
    private String team;
    @SerializedName("score")
    @Expose
    private String score;
    @SerializedName("in_user_id")
    @Expose
    private String inUserId;
    @SerializedName("in_user_name")
    @Expose
    private String inUserName;
    @SerializedName("in_bag_id")
    @Expose
    private String inBagId;
    @SerializedName("in_user_image")
    @Expose
    private String inUserImage;
    @SerializedName("my_id")
    @Expose
    private String myId;
    @SerializedName("my_bag_id")
    @Expose
    private String myBagId;
    @SerializedName("my_name")
    @Expose
    private String myName;
    @SerializedName("my_image")
    @Expose
    private String myImage;
    @SerializedName("in_bag_image")
    @Expose
    private String inBagImage;
    @SerializedName("my_bag_image")
    @Expose
    private String myBagImage;
    private final static long serialVersionUID = -8673404624200085060L;

    public String getInBagImage() {
        return inBagImage;
    }

    public void setInBagImage(String inBagImage) {
        this.inBagImage = inBagImage;
    }

    public String getMyBagImage() {
        return myBagImage;
    }

    public void setMyBagImage(String myBagImage) {
        this.myBagImage = myBagImage;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getInUserId() {
        return inUserId;
    }

    public void setInUserId(String inUserId) {
        this.inUserId = inUserId;
    }

    public String getInUserName() {
        return inUserName;
    }

    public void setInUserName(String inUserName) {
        this.inUserName = inUserName;
    }

    public String getInBagId() {
        return inBagId;
    }

    public void setInBagId(String inBagId) {
        this.inBagId = inBagId;
    }

    public String getInUserImage() {
        return inUserImage;
    }

    public void setInUserImage(String inUserImage) {
        this.inUserImage = inUserImage;
    }

    public String getMyId() {
        return myId;
    }

    public void setMyId(String myId) {
        this.myId = myId;
    }

    public String getMyBagId() {
        return myBagId;
    }

    public void setMyBagId(String myBagId) {
        this.myBagId = myBagId;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyImage() {
        return myImage;
    }

    public void setMyImage(String myImage) {
        this.myImage = myImage;
    }

}