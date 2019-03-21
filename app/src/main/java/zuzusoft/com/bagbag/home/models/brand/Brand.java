
package zuzusoft.com.bagbag.home.models.brand;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand implements Serializable
{

    @SerializedName("brand_id")
    @Expose
    private String brandId;
    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("brand_details")
    @Expose
    private String brandDetails;
    @SerializedName("all_brand_images")
    @Expose
    private List<AllBrandImage> allBrandImages = null;
    private final static long serialVersionUID = -4398976631521602782L;

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

    public String getBrandDetails() {
        return brandDetails;
    }

    public void setBrandDetails(String brandDetails) {
        this.brandDetails = brandDetails;
    }

    public List<AllBrandImage> getAllBrandImages() {
        return allBrandImages;
    }

    public void setAllBrandImages(List<AllBrandImage> allBrandImages) {
        this.allBrandImages = allBrandImages;
    }

}
