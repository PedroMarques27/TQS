package ies.projeto.Forms;


public class RegisterClientForm {
    private String email;
    private String password;
    private String username;
    private String birthday;

    private String gender;
    private String sexual_orientation;

    public String getGender(){
        return this.gender;
    }

    public void setGender(String gender){
        this.gender=gender;
    }

   
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSexual_orientation() {
        return sexual_orientation;
    }

    public void setSexual_orientation(String sexual_orientation) {
        this.sexual_orientation = sexual_orientation;
    }
}
