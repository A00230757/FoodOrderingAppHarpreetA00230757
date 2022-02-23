package ca.harpreetA00230757.foodorderingapp;
//


//all data members and member functions with getter setter are present in this class
public class RatingClass
{
    private long orderno;
    private long ratingdate;
    private String reviewText;
    private int ratingvalue;
    private String mobileno;
    private String reviewpushkey;

    public RatingClass()
    {

    }

    public RatingClass(long orderno, long ratingdate, String reviewText, int ratingvalue, String mobileno, String reviewpushkey) {
        this.orderno = orderno;
        this.ratingdate = ratingdate;
        this.reviewText = reviewText;
        this.ratingvalue = ratingvalue;
        this.mobileno = mobileno;
        this.reviewpushkey = reviewpushkey;
    }

    public long getOrderno()
    {
        return orderno;
    }

    public void setOrderno(long orderno) {
        this.orderno = orderno;
    }

    public long getRatingdate() {
        return ratingdate;
    }

    public void setRatingdate(long ratingdate) {
        this.ratingdate = ratingdate;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRatingvalue() {
        return ratingvalue;
    }

    public void setRatingvalue(int ratingvalue) {
        this.ratingvalue = ratingvalue;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getReviewpushkey() {
        return reviewpushkey;
    }

    public void setReviewpushkey(String reviewpushkey) {
        this.reviewpushkey = reviewpushkey;
    }


}

