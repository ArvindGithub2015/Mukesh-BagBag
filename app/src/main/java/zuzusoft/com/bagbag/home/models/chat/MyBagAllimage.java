
package zuzusoft.com.bagbag.home.models.chat;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyBagAllimage implements Serializable
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
    private final static long serialVersionUID = 1205000342540664L;

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
