package tqs.lab7.ex2;


import javax.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private String author;
    private String release;

}

