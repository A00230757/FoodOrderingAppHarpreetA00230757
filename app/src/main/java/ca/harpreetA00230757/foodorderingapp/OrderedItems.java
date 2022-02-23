package ca.harpreetA00230757.foodorderingapp;

//this class has data members and member functions and getter setter for ordered items
public class OrderedItems
{
    private String item_name,item_desc,item_type,item_photo;
    private int item_qty,item_price,totalprice;

    public OrderedItems()
    {
//       item_name = "";
//       item_desc = "";
//       item_type = "";
//       item_photo = "";
//       item_price = 0;
//       totalprice = 0;
    }


    public OrderedItems(String item_name, String item_desc, String item_type, String item_photo, int item_qty, int item_price, int totalprice) {
        this.item_name = item_name;
        this.item_desc = item_desc;
        this.item_type = item_type;
        this.item_photo = item_photo;
        this.item_qty = item_qty;
        this.item_price = item_price;
        this.totalprice = totalprice;
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

    public String getItem_photo() {
        return item_photo;
    }

    public void setItem_photo(String item_photo) {
        this.item_photo = item_photo;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(int item_qty) {
        this.item_qty = item_qty;
    }
}
