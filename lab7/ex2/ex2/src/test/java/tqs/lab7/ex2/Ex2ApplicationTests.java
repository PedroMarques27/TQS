package tqs.lab7.ex2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

@Testcontainers
@SpringBootTest
class ApplicationTests {
    Book book = new Book();
    Book book2 = new Book();

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
            .withUsername("postgres")
            .withPassword("user")
            .withDatabaseName("testdb");

    @Autowired
    private BookRepository bookRepository;

    // requires Spring Boot >= 2.2.6
    @DynamicPropertySource
    @Order(1)
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }
    @BeforeEach
    void startup(){
        book.setTitle("Testcontainers");
        book.setId(16);
        book.setRelease("12/05");
        book.setAuthor("Author 1");


        book2.setTitle("Book Inserted 2");
        book2.setId(15);
        book2.setRelease("12/05");
        book2.setAuthor("Author 1");
    }
    @Test
    @Order(2)
    void contextLoads() {
        bookRepository.save(book);
        bookRepository.save(book2);

        System.out.println("Context loads!");
    }

    @Test
    @Order(3)
    void getData() {
        ArrayList<Book> books = (ArrayList<Book>) bookRepository.findAll();
        assertTrue(books.contains(book));
        assertTrue(books.contains(book2));
    }

}