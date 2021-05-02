package ies.projeto.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
@Entity // This tells Hibernate to make a table out of this class

@Table(name="Client")
public class Client {
    @Id
    @Column(name="email")
    private String email;

    @Column(name="username")
    private String username;

    @Column(name="name")
    private String name;

    @Column(name="gender")
    private String gender;

    @Column(name="sexual_orientation")
    private String sexual_orientation;

    @Column(name="bio")
    private String bio;

    @Column(name="description")
    private String description;

    @Column(name="interests")
    private String interests;

    @Column(name="state")
    private String state;

    @Column(name="profile_picture")
    private String profile_picture;

    @Column(name="age")
    private int age;


    @Column(name="birthday")
    private String birthday;

    


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSexual_orientation() {
        return sexual_orientation;
    }

    public void setSexual_orientation(String sexual_orientation) {
        this.sexual_orientation = sexual_orientation;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    
}