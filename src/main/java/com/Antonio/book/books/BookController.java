package com.Antonio.book.books;

import java.lang.StackWalker.Option;
import java.util.List;
import java.util.Optional;

import javax.naming.NameNotFoundException;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> GetAllBooks(){
        return new ResponseEntity<>(bookService.FindMany(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity GetOneBook(@PathVariable(value = "id") Long id)throws NotFoundException{
        return new ResponseEntity<>(bookService.FindOne(id).get(),HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<?> SaveOne(@RequestBody @Validated Book book){
        return new ResponseEntity<>(bookService.SaveOne(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> UpdateBook(
        @RequestBody @Validated Book book,
        @PathVariable(value = "id") Long id
    ) throws NotFoundException{
        return new ResponseEntity<>(bookService.UpdateBook(book, id),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> DeleteBook(@PathVariable(value = "id") Long id) throws NotFoundException{
        return new ResponseEntity<>(bookService.DeleteBook(id),HttpStatus.ACCEPTED);
    }
}
