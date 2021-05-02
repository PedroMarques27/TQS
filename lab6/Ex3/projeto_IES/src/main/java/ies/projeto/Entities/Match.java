package ies.projeto.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
@Entity // This tells Hibernate to make a table out of this class

@Table(name="Matches")
public class Match {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Integer id;
    
    @Column(name="email1")
    private String email1;

    @Column(name="email2")
    private String email2;

    @Column(name="swipe_email1", columnDefinition = "BOOLEAN default False")
    private boolean swipe_email1;

    @Column(name="swipe_email2", columnDefinition = "BOOLEAN default False")
    private boolean swipe_email2;

    @Column(name="talking", columnDefinition = "BOOLEAN default False")
    private boolean talking;

    public String getEmail1(){
        return this.email1;
    }
    public String getEmail2(){
        return this.email2;
    }

    public void setEmail1(String email1){
        this.email1=email1;
    }
    public void setEmail2(String email2){
        this.email2=email2;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSwipe_email1() {
        return swipe_email1;
    }

    public void setSwipe_email1(boolean swipe_email1) {
        this.swipe_email1 = swipe_email1;
    }

    public boolean isSwipe_email2() {
        return swipe_email2;
    }

    public void setSwipe_email2(boolean swipe_email2) {
        this.swipe_email2 = swipe_email2;
    }

    public boolean isTalking() {
        return talking;
    }

    public void setTalking(boolean talking) {
        this.talking = talking;
    }
}