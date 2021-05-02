package ies.projeto.Forms;

public class PromotionForm {

    private String title;
    private String promotion_code;
    private String description;
    //private int image;
    private String promotion_picture;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromotion_code() {
        return promotion_code;
    }

    public void setPromotion_code(String promotion_code) {
        this.promotion_code = promotion_code;
    }

    public String getPromotion_picture() {
        return promotion_picture;
    }

    public void setPromotion_picture(String promotion_picture) {
        this.promotion_picture = promotion_picture;
    }

    /*public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }*/
}
