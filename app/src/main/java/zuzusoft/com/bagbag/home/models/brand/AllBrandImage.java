
package zuzusoft.com.bagbag.home.models.brand;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllBrandImage implements Serializable
{

    @SerializedName("img_id")
    @Expose
    private String imgId;
    @SerializedName("bag_url")
    @Expose
    private String bagUrl;
    private final static long serialVersionUID = -6821534351289102741L;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getBagUrl() {
        return bagUrl;
    }

    public void setBagUrl(String bagUrl) {
        this.bagUrl = bagUrl;
    }

}
