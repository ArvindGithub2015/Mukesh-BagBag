
package zuzusoft.com.bagbag.home.models.gold;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoldBag implements Serializable
{

    @SerializedName("ad_id")
    @Expose
    private String adId;
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
    @SerializedName("ad_url")
    @Expose
    private String adUrl;
    @SerializedName("bag_allimage")
    @Expose
    private List<BagAllimage> bagAllimage = null;
    @SerializedName("brand_id")
    @Expose
    private String brandId;
    private final static long serialVersionUID = 8814766303128901326L;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
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

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public List<BagAllimage> getBagAllimage() {
        return bagAllimage;
    }

    public void setBagAllimage(List<BagAllimage> bagAllimage) {
        this.bagAllimage = bagAllimage;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

}
