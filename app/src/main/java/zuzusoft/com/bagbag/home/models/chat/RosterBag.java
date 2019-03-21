
package zuzusoft.com.bagbag.home.models.chat;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RosterBag implements Serializable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("material")
    @Expose
    private String material;
    @SerializedName("colour")
    @Expose
    private String colour;
    @SerializedName("bag_type")
    @Expose
    private String bagType;
    @SerializedName("bag_size")
    @Expose
    private String bagSize;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("bag_description")
    @Expose
    private String bagDescription;
    @SerializedName("bag_image")
    @Expose
    private Object bagImage;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("bag_id")
    @Expose
    private String bagId;
    @SerializedName("my_bag_allimage")
    @Expose
    private List<MyBagAllimage> myBagAllimage = null;
    private final static long serialVersionUID = -85502772606249524L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getBagType() {
        return bagType;
    }

    public void setBagType(String bagType) {
        this.bagType = bagType;
    }

    public String getBagSize() {
        return bagSize;
    }

    public void setBagSize(String bagSize) {
        this.bagSize = bagSize;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBagDescription() {
        return bagDescription;
    }

    public void setBagDescription(String bagDescription) {
        this.bagDescription = bagDescription;
    }

    public Object getBagImage() {
        return bagImage;
    }

    public void setBagImage(Object bagImage) {
        this.bagImage = bagImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getBagId() {
        return bagId;
    }

    public void setBagId(String bagId) {
        this.bagId = bagId;
    }

    public List<MyBagAllimage> getMyBagAllimage() {
        return myBagAllimage;
    }

    public void setMyBagAllimage(List<MyBagAllimage> myBagAllimage) {
        this.myBagAllimage = myBagAllimage;
    }

}
