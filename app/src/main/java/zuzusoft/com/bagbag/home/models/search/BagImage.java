package zuzusoft.com.bagbag.home.models.search;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BagImage implements Serializable
{

    @SerializedName("img_id")
    @Expose
    private Integer imgId;
    @SerializedName("bag_id")
    @Expose
    private Integer bagId;
    @SerializedName("bag_url")
    @Expose
    private String bagUrl;
    private final static long serialVersionUID = 6463317581517363105L;

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public Integer getBagId() {
        return bagId;
    }

    public void setBagId(Integer bagId) {
        this.bagId = bagId;
    }

    public String getBagUrl() {
        return bagUrl;
    }

    public void setBagUrl(String bagUrl) {
        this.bagUrl = bagUrl;
    }

}
