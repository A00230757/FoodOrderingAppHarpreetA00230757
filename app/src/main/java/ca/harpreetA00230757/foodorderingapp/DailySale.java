package ca.harpreetA00230757.foodorderingapp;

//class where daily sale data members and member functions and getter setter declared

public class DailySale
{
    private String itemname;
    private  int itemprice;

    public DailySale()
    {
    }

    public DailySale(String itemname, int itemprice)
    {
        this.itemname = itemname;
        this.itemprice = itemprice;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public int getItemprice() {
        return itemprice;
    }

    public void setItemprice(int itemprice) {
        this.itemprice = itemprice;
    }
}
