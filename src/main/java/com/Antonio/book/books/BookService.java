package com.Antonio.book.books;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.NotFound;

@Service
public class BookService implements BookServiceImp{
    @Autowired
    private BookRepository bookRepository;
    
    @Override
    public List<Book> FindMany(){
        return bookRepository.findAll();
    }

    @Override
    public Book SaveOne(Book book){
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> FindOne(Long id) throws NotFoundException {
        Optional<Book>  oBook = bookRepository.findById(id);
        if (!oBook.isPresent()){
            throw new NotFoundException();
        }
        return oBook;
    }

    @Override
    public Book DeleteBook(Long id) throws NotFoundException  {
        Optional<Book> oBook = bookRepository.findById(id);

        if(!oBook.isPresent()){
            System.out.println("ENTRO AQUI");
            throw new NotFoundException();
        }
        
        bookRepository.deleteById(id);
        return oBook.get();
    }

    @Override
    public Book UpdateBook(Book book, Long id) throws NotFoundException {
        Optional<Book> oBook = bookRepository.findById(id);
        if(!oBook.isPresent()){
            throw new NotFoundException();
        }

        Book updateBook = oBook.get();
        updateBook.setAutor(book.getAutor());
        updateBook.setTitle(book.getTitle());
        updateBook.setIsbm(book.getIsbm());
        updateBook.setPublication_date(book.getPublication_date());

        bookRepository.save(updateBook);
        
        return updateBook;
    }
}
