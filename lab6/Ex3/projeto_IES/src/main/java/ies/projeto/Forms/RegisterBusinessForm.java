package ies.projeto.Forms;


public class RegisterBusinessForm {
    private String email;
    private String password;
    private String name;
    private String address;
    private String business_type;


    public String getAddress(){
        return this.address;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public String getBusinessType() {
        return business_type;
    }

    public void setBusinessType(String business_type) {
        this.business_type = business_type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(String business_type) {
        this.business_type = business_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
