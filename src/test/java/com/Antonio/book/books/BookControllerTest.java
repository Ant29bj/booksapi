package com.Antonio.book.books;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.print.attribute.standard.Media;

@WebMvcTest(controllers = BookController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book;

    @Autowired
    private ObjectMapper objectMapper ;

    @BeforeEach
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateStr = "2001-10-12";
        LocalDate localDate = LocalDate.parse(dateStr, formatter);
        java.sql.Date sDate = java.sql.Date.valueOf(localDate);
        book = Book.builder()
                .autor("test autor")
                .title("test tile")
                .isbm("1231-312413-41312")
                .publication_date(sDate)
                .build();
    }

    @Test
    void testDeleteBook() throws Exception {

        long bookId = 1;
        Book responseBook = Book.builder()
                .id(1L)
                .autor("Miguel de servantes")
                .title("El Quijote")
                .publication_date(new Date())
                .isbm("1231231231").build();
        when(bookService.DeleteBook(1L)).thenReturn(responseBook);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/books/1")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autor").value("Miguel de servantes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("El Quijote"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetAllBooks() throws Exception {
        List<Book> bookResponse = Arrays.asList(
                new Book(1L, "El Quijote", "Miguel de servantes", new Date(), "12312-12313-1231"),
                new Book(2L, "Don Juan Tenorio", "Jose Zorrilla", new Date(), "12312-12313-1231")
        );

        when(bookService.FindMany()).thenReturn(bookResponse);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]title",CoreMatchers.is("El Quijote")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]title",CoreMatchers.is("Don Juan Tenorio")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testGetOneBook() throws Exception {
       long bookId = 1;
       Book responseBook = Book.builder()
               .id(1L)
               .autor("Miguel de servantes")
               .title("El Quijote")
               .publication_date(new Date())
               .isbm("1231231231").build();
       when(bookService.FindOne(1L)).thenReturn(Optional.ofNullable(responseBook));

       ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books/1")
               .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"));

       response.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(bookId))
               .andExpect(MockMvcResultMatchers.jsonPath("$.autor").value("Miguel de servantes"))
               .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("El Quijote"))
               .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testSaveOne() throws JsonProcessingException, Exception {

        given(bookService.SaveOne(ArgumentMatchers.any()))
                .willAnswer((invocation -> invocation.getArgument(0)));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/books")
                .content(objectMapper.writeValueAsString(book)).characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(book.getTitle())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void testUpdateBook() throws Exception{
        long bookId = 1;
        Book responseBook = Book.builder()
                .id(bookId)
                .autor("Miguel de servantes")
                .title("El Quijote")
                .publication_date(new Date())
                .isbm("1231231231").build();
        when(bookService.UpdateBook(responseBook,1L)).thenReturn(responseBook);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/books/1")
                .content(objectMapper.writeValueAsString(responseBook)).characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(responseBook.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.autor").value(responseBook.getAutor()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(responseBook.getTitle()))
                .andDo(MockMvcResultHandlers.print());
    }
}
