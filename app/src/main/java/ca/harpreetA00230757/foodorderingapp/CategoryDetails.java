package ca.harpreetA00230757.foodorderingapp;

//class where category data members and member functions declared

public class CategoryDetails
{
    private String category_name;
    private String category_desc;
    private String photo;

    public CategoryDetails()
    {
        category_name  = "";
        category_desc = " ";
        photo = " ";
    }

    public CategoryDetails(String category_name, String category_desc, String photo)
    {
        this.category_name = category_name;
        this.category_desc = category_desc;
        this.photo = photo;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



}
