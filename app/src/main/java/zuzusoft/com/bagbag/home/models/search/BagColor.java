package zuzusoft.com.bagbag.home.models.search;

public class BagColor {

    private String bagColor;
    private int colorCode;

    public BagColor(String bagColor, int colorCode){

        this.bagColor = bagColor;
        this.colorCode = colorCode;

    }

    public String getBagColor() {
        return bagColor;
    }

    public void setBagColor(String bagColor) {
        this.bagColor = bagColor;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }
}
