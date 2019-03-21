package zuzusoft.com.bagbag.home.models;

import com.glide.slider.library.SliderTypes.TextSliderView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import zuzusoft.com.bagbag.home.models.search.BagAllimage;
import zuzusoft.com.bagbag.home.models.search.BagImage;

/**
 * Created by mukeshnarayan on 07/11/18.
 */

public class DataBags implements Serializable{

    private String ownerUserId;
    private String ownerBagId;

    private String brandName;
    private String bagTitle;
    private List<BagImage> adBagImages;
    private List<TextSliderView> adSliderData;
    private String brandId;
    private String adId;

    private int bagImage;

    private String bagFileName;

    private List<BagAllimage> bagImages;

    private String bagOwnerName;
    private String distance;
    private String ownerImage;
    private boolean isBagBagAd;
    private String bagOwnerImage;

    private boolean isSelect;

    private String bagMaterial;
    private String bagColour;
    private String bagSize;
    private String bagType;
    private String bagDescription;


    private String exchangeUserId;
    private String exchangeBagId;
    private String exchangeBagImage;

    private String goldWebUrl;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getGoldWebUrl() {
        return goldWebUrl;
    }

    public void setGoldWebUrl(String goldWebUrl) {
        this.goldWebUrl = goldWebUrl;
    }

    public List<BagImage> getAdBagImages() {
        return adBagImages;
    }

    public void setAdBagImages(List<BagImage> adBagImages) {
        this.adBagImages = adBagImages;
    }

    public List<TextSliderView> getAdSliderData() {
        return adSliderData;
    }

    public void setAdSliderData(List<TextSliderView> adSliderData) {
        this.adSliderData = adSliderData;
    }

    public String getBagTitle() {
        return bagTitle;
    }

    public void setBagTitle(String bagTitle) {
        this.bagTitle = bagTitle;
    }

    public String getExchangeUserId() {
        return exchangeUserId;
    }

    public void setExchangeUserId(String exchangeUserId) {
        this.exchangeUserId = exchangeUserId;
    }

    public String getExchangeBagId() {
        return exchangeBagId;
    }

    public void setExchangeBagId(String exchangeBagId) {
        this.exchangeBagId = exchangeBagId;
    }

    public String getExchangeBagImage() {
        return exchangeBagImage;
    }

    public void setExchangeBagImage(String exchangeBagImage) {
        this.exchangeBagImage = exchangeBagImage;
    }

    public String getBagDescription() {
        return bagDescription;
    }

    public void setBagDescription(String bagDescription) {
        this.bagDescription = bagDescription;
    }

    public String getBagOwnerImage() {
        return bagOwnerImage;
    }

    public void setBagOwnerImage(String bagOwnerImage) {
        this.bagOwnerImage = bagOwnerImage;
    }

    public String getBagMaterial() {
        return bagMaterial;
    }

    public void setBagMaterial(String bagMaterial) {
        this.bagMaterial = bagMaterial;
    }

    public String getBagColour() {
        return bagColour;
    }

    public void setBagColour(String bagColour) {
        this.bagColour = bagColour;
    }

    public String getBagSize() {
        return bagSize;
    }

    public void setBagSize(String bagSize) {
        this.bagSize = bagSize;
    }

    public String getBagType() {
        return bagType;
    }

    public void setBagType(String bagType) {
        this.bagType = bagType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<BagAllimage> getBagImages() {
        return bagImages;
    }

    public void setBagImages(List<BagAllimage> bagImages) {
        this.bagImages = bagImages;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(String ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public String getOwnerBagId() {
        return ownerBagId;
    }

    public void setOwnerBagId(String ownerBagId) {
        this.ownerBagId = ownerBagId;
    }

    public boolean isBagBagAd() {
        return isBagBagAd;
    }

    public void setBagBagAd(boolean bagBagAd) {
        isBagBagAd = bagBagAd;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getBagOwnerName() {
        return bagOwnerName;
    }

    public void setBagOwnerName(String bagOwnerName) {
        this.bagOwnerName = bagOwnerName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getBagFileName() {
        return bagFileName;
    }

    public void setBagFileName(String bagFileName) {
        this.bagFileName = bagFileName;
    }

    public int getBagImage() {
        return bagImage;
    }

    public void setBagImage(int bagImage) {
        this.bagImage = bagImage;
    }
}
