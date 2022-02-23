package ca.harpreetA00230757.foodorderingapp;

// this class has all data members , member functions for user along with getter setter.
public class UserDetails
{
    private String mobileno;
    private String fullname;
    private String email;
    private String gender;
    public UserDetails()
    {
        mobileno = "";
        fullname = "";
        email = "";
        gender = "";
    }

    public UserDetails(String mobileno, String fullname, String email, String gender)
    {
        this.mobileno = mobileno;
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
