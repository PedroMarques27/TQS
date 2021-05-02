package ies.projeto.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import javax.persistence.Column;
import java.util.Date;
// This tells Hibernate to make a table out of this class
@Entity 
@Table(name="User")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="password")
    private String password;

    @Column(name="email")
    private String email;

    @Column(name="user_type")
    private String tipo;

    @Column(name="start_date")
    private Date data_de_adesao;

    @Column(name="local")
    private String local;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

   
    public Date getData_de_adesao() {
        return data_de_adesao;
    }

    public void setData_de_adesao(Date data_de_adesao) {
        this.data_de_adesao = data_de_adesao;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

}