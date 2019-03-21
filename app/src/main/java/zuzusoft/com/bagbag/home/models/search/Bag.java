
package zuzusoft.com.bagbag.home.models.search;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Bag implements Serializable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("bag_allimage")
    @Expose
    private List<BagAllimage> bagAllimage = null;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("bag_id")
    @Expose
    private String bagId;
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
    @SerializedName("bag_description")
    @Expose
    private String bagDescription;
    @SerializedName("in_exchange_user_id")
    @Expose
    private String exchangeUserId;

    @SerializedName("in_exchange_bag_id")
    @Expose
    private String exchangeBagId;

    @SerializedName("in_exchange_bag_image")
    @Expose
    private String exchangeBagImage;


    private final static long serialVersionUID = -3721312179092421524L;

    public String getExchangeUserId() {
        return exchangeUserId;
    }

    public void setExchangeUserId(String exchangeUserId) {
        this.exchangeUserId = exchangeUserId;
    }

    public String getExchangeBagId() {
        return exchangeBagId;
    }

    public void setExchangeBagId(String exchangeBagId) {
        this.exchangeBagId = exchangeBagId;
    }

    public String getExchangeBagImage() {
        return exchangeBagImage;
    }

    public void setExchangeBagImage(String exchangeBagImage) {
        this.exchangeBagImage = exchangeBagImage;
    }

    public String getBagDescription() {
        return bagDescription;
    }

    public void setBagDescription(String bagDescription) {
        this.bagDescription = bagDescription;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<BagAllimage> getBagAllimage() {
        return bagAllimage;
    }

    public void setBagAllimage(List<BagAllimage> bagAllimage) {
        this.bagAllimage = bagAllimage;
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

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getBagId() {
        return bagId;
    }

    public void setBagId(String bagId) {
        this.bagId = bagId;
    }

}
