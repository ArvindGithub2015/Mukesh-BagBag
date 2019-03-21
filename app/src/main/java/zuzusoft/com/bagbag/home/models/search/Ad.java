package zuzusoft.com.bagbag.home.models.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Ad implements Serializable
{

    @SerializedName("ad_id")
    @Expose
    private String adId;

    @SerializedName("brand_id")
    @Expose
    private String brandId;

    @SerializedName("brand_name")
    @Expose
    private String brandName;

    @SerializedName("bag_title")
    @Expose
    private String bagTitle;

    @SerializedName("bag_description")
    @Expose
    private String description;

    @SerializedName("bag_allimage")
    @Expose
    private List<BagImage> bagImage = null;

    private final static long serialVersionUID = -6660067126242344354L;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBagTitle() {
        return bagTitle;
    }

    public void setBagTitle(String bagTitle) {
        this.bagTitle = bagTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BagImage> getBagImage() {
        return bagImage;
    }

    public void setBagImage(List<BagImage> bagImage) {
        this.bagImage = bagImage;
    }
}