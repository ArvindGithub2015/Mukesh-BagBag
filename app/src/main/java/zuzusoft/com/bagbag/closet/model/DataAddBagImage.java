package zuzusoft.com.bagbag.closet.model;

import java.io.Serializable;

/**
 * Created by mukeshnarayan on 17/01/19.
 */

public class DataAddBagImage implements Serializable {

    private String bagImageId;
    private String bagImageUrl;
    private String bagId;
    private boolean hasBagData;

    public String getBagImageId() {
        return bagImageId;
    }

    public void setBagImageId(String bagImageId) {
        this.bagImageId = bagImageId;
    }

    public String getBagImageUrl() {
        return bagImageUrl;
    }

    public void setBagImageUrl(String bagImageUrl) {
        this.bagImageUrl = bagImageUrl;
    }

    public String getBagId() {
        return bagId;
    }

    public void setBagId(String bagId) {
        this.bagId = bagId;
    }

    public boolean isHasBagData() {
        return hasBagData;
    }

    public void setHasBagData(boolean hasBagData) {
        this.hasBagData = hasBagData;
    }
}
