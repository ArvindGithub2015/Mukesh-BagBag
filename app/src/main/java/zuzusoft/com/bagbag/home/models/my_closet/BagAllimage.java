
package zuzusoft.com.bagbag.home.models.my_closet;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BagAllimage implements Serializable
{

    @SerializedName("img_id")
    @Expose
    private String imgId;
    @SerializedName("bag_id")
    @Expose
    private String bagId;
    @SerializedName("bag_url")
    @Expose
    private String bagUrl;
    private final static long serialVersionUID = -5875696894219331679L;

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public String getBagId() {
        return bagId;
    }

    public void setBagId(String bagId) {
        this.bagId = bagId;
    }

    public String getBagUrl() {
        return bagUrl;
    }

    public void setBagUrl(String bagUrl) {
        this.bagUrl = bagUrl;
    }

}
