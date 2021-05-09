package tqs.lab7.ex2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository repository;

    @Override
    public ArrayList<Book> findAll() {
        var books = (ArrayList<Book>) repository.findAll();
        return books;
    }

    @Override
    public void save(Book b) {
        repository.save(b);
    }
}
