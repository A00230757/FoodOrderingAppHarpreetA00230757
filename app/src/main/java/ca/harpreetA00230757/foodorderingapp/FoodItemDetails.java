package ca.harpreetA00230757.foodorderingapp;


public class FoodItemDetails
{

    private String item_name;
    private String item_desc;
    private String item_type ;
    private  int item_price;
    private String photo;


    public FoodItemDetails()
    {
        item_name = "";
        item_desc = "";
        item_type = "";
        item_price = 0;
        photo = "";

    }

    public FoodItemDetails(String item_name, String item_desc, String item_type, int item_price,String photo)
    {
        this.item_name = item_name;
        this.item_desc = item_desc;
        this.item_type = item_type;
        this.item_price = item_price;
        this.photo = photo;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }





}
