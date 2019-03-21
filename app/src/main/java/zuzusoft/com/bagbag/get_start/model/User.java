
package zuzusoft.com.bagbag.get_start.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User implements Serializable
{

    @SerializedName("Member")
    @Expose
    private Member member;
    private final static long serialVersionUID = -3136590172286726181L;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
