
package zuzusoft.com.bagbag.home.models.gold;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoldListResponse implements Serializable
{

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("gold_bags")
    @Expose
    private List<GoldBag> goldBags = null;
    private final static long serialVersionUID = 5495059984271557445L;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GoldBag> getGoldBags() {
        return goldBags;
    }

    public void setGoldBags(List<GoldBag> goldBags) {
        this.goldBags = goldBags;
    }

}
