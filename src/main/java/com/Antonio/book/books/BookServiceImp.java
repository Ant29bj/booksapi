package com.Antonio.book.books;

import java.util.List;
import java.util.Optional;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.web.client.HttpClientErrorException.NotFound;


public interface BookServiceImp {
    Book SaveOne(Book book);
    List<Book> FindMany();
    Optional<Book> FindOne(Long id) throws NotFoundException;
    Book DeleteBook (Long id) throws NotFoundException;
    Book UpdateBook(Book book, Long id) throws NotFoundException;
}
