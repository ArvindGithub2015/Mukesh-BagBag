
package zuzusoft.com.bagbag.closet.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response implements Serializable
{

    @SerializedName("Bag")
    @Expose
    private List<Bag> bag = null;
    private final static long serialVersionUID = -1345485104618319925L;

    public List<Bag> getBag() {
        return bag;
    }

    public void setBag(List<Bag> bag) {
        this.bag = bag;
    }

}
