
package zuzusoft.com.bagbag.home.models.my_closet;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("Bag")
    @Expose
    private List<Bag> bag = null;
    private final static long serialVersionUID = -3446895610113027275L;

    public List<Bag> getBag() {
        return bag;
    }

    public void setBag(List<Bag> bag) {
        this.bag = bag;
    }

}
