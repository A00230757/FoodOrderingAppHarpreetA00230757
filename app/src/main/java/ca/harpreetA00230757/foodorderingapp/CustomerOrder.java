package ca.harpreetA00230757.foodorderingapp;



import java.util.ArrayList;

public class CustomerOrder
{
    long orderno;
    String mobileno;
    String status;
    int rating;
    int payableamount;
    double tax;
    double totalamount;
    Long orderdate,deliverydate;
    ArrayList<OrderedItems> orderItems;
    String pushkey;
    String reviewtext;
    String reviewpushkey;
    String date_vise_sale = "";
    String date_month = "";

    public CustomerOrder() {
    }

    public CustomerOrder(long orderno, String mobileno, String status, int rating, int payableamount, double tax, double totalamount, Long orderdate, Long deliverydate, ArrayList<OrderedItems> orderItems, String pushkey, String reviewtext,String reviewpushkey,String date_vise_sale, String date_month)
    {
        this.orderno = orderno;
        this.mobileno = mobileno;
        this.status = status;
        this.rating = rating;
        this.payableamount = payableamount;
        this.tax = tax;
        this.totalamount = totalamount;
        this.orderdate = orderdate;
        this.deliverydate = deliverydate;
        this.orderItems = orderItems;
        this.pushkey = pushkey;
        this.reviewtext = reviewtext;
        this.reviewpushkey = reviewpushkey;
        this.date_vise_sale = date_vise_sale;
        this.date_month = date_month;
    }

    public long getOrderno() {
        return orderno;
    }

    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getPayableamount() {
        return payableamount;
    }

    public void setPayableamount(int payableamount) {
        this.payableamount = payableamount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }

    public Long getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(Long orderdate) {
        this.orderdate = orderdate;
    }

    public Long getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Long deliverydate) {
        this.deliverydate = deliverydate;
    }

    public ArrayList<OrderedItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<OrderedItems> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getReviewtext() {
        return reviewtext;
    }

    public void setReviewtext(String reviewtext) {
        this.reviewtext = reviewtext;
    }

    public String getReviewpushkey() {
        return reviewpushkey;
    }

    public void setReviewpushkey(String reviewpushkey) {
        this.reviewpushkey = reviewpushkey;
    }



    public String getDate_vise_sale() {
        return date_vise_sale;
    }

    public void setDate_vise_sale(String date_vise_sale) {
        this.date_vise_sale = date_vise_sale;
    }

    public String getDate_month() {
        return date_month;
    }

    public void setDate_month(String date_month) {
        this.date_month = date_month;
    }
}
