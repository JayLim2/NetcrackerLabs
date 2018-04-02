/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.service.AuthorService;
import database.service.BookService;
import database.service.PublisherService;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Author;
import entity.Book;
import entity.Publisher;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @Autowired
    BookService bookService;
    
    @Autowired
    AuthorService authorService;
    
    @Autowired
    PublisherService publisherService;
    
    @GetMapping("/books")
    public String greeting(Model model) {
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "books";
    }
    
    @GetMapping("/publishers")
    public String publishers(Model model) {
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(name="id", required=true) String id, Model model) {
        Book book = bookService.getByID(Integer.parseInt(id));
        for(Author author:book.getAuthors()){
            author.getBooks().remove(book);
            authorService.editAuthor(author);
        }
        bookService.delete(book);
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "books";
    }
    
    @PostMapping("/deletePublisher")
    public String deletePublisher(@RequestParam(name="id", required=true) String id, Model model) {
        Publisher publisher = publisherService.getByID(Integer.parseInt(id));
        publisherService.delete(publisher);
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }
    
    @PostMapping("/edit")
    public String edit(@RequestParam(name="id", required=true) String id, Model model) {
            Book b = bookService.getByID(Integer.parseInt(id));
            model.addAttribute("book", b);
        return "edit";
    }
    
    @PostMapping("/add")
    public String add( Model model) {
        return "add";
    }
    
    @PostMapping("/addAuthor")
    public String addAuthor( Model model) {
        return "addAuthor";
    }
    
    @PostMapping("/addPublisher")
    public String addPublisher( Model model) {
        return "addPublisher";
    }
    
    @PostMapping("/editPublisher")
    public String editPublisher(@RequestParam(name="id", required=true) String id, Model model) {
        Publisher p = publisherService.getByID(Integer.parseInt(id));
        model.addAttribute("publisher", p);
        return "editPublisher";
    }
    
    @PostMapping("/authors")
    public String submitAddAuthor(@RequestParam Map<String,String> params, Model model) {
        byte status = 0;
        try {
            Author a = new Author();
            a.setAuthorName(params.get("authorname"));
            authorService.addAuthor(a);
        } catch (JpaSystemException ex) {
            System.out.println("Нарушение целостности.");
            status = 1;
        }
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        model.addAttribute("submitStatus", status);
        return "authors";
    }
    
    @PostMapping("/submitAddPublisher")
    public String submitAddPublisher(@RequestParam Map<String,String> params, Model model) {
        Publisher p = new Publisher();
        p.setPublisherName(params.get("publishername"));
        publisherService.addPublisher(p);
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }
    
    @PostMapping("/editAuthor")
    public String editAuthor(@RequestParam(name="id", required=true) String id, Model model) {
        Author a = authorService.getByID(Integer.parseInt(id));
        model.addAttribute("author", a);
        return "editAuthor";
    }
    
    @PostMapping("/submitEditAuthor")
    public String submitEditAuthor(@RequestParam Map<String,String> params, Model model) {
        Author a = authorService.getByID(Integer.parseInt(params.get("id")));
        a.setAuthorName(params.get("authorname"));
        authorService.addAuthor(a);
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "authors";
    }
    
    @PostMapping("/submitEditPublisher")
    public String submitEditPublisher(@RequestParam Map<String,String> params, Model model) {
        Publisher p = publisherService.getByID(Integer.parseInt(params.get("id")));
        p.setPublisherName(params.get("publishername"));
        publisherService.addPublisher(p);
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }
    
    @PostMapping("/submitEdit")
    public String submitEdit(@RequestParam Map<String,String> params, Model model) {
        Book b = bookService.getByID(Integer.parseInt(params.get("id")));
        for(Author author:b.getAuthors()){
            author.getBooks().remove(b);
            authorService.editAuthor(author);
        }
        String[] aNames = params.get("authors").split(",");
        List<Author> newAuthors = new LinkedList<>();
        for(String aname:aNames){
            newAuthors.add(authorService.getByName(aname));
        }
        Publisher p = publisherService.getByName(params.get("publisher"));
        b.setBookName(params.get("booktitle"));
        b.setBrief(params.get("brief"));
        b.setPublishYear(Integer.parseInt(params.get("publishYear")));
        b.setPublisher(p);
        b.setAuthors(newAuthors);
        bookService.editBook(b);
        for(int i = 0; i < newAuthors.size(); i++){
            newAuthors.get(i).getBooks().add(b);
            authorService.editAuthor(newAuthors.get(i));
        }
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "books";
    }
    
    @PostMapping("/submitAdd")
    public String submitAdd(@RequestParam Map<String,String> params, Model model) {
        Book b = new Book();
        String[] aNames = params.get("authors").split(",");
        List<Author> newAuthors = new LinkedList<>();
        for(String aname:aNames){
            newAuthors.add(authorService.getByName(aname));
        }
        Publisher p = publisherService.getByName(params.get("publisher"));
        b.setBookName(params.get("booktitle"));
        b.setBrief(params.get("brief"));
        b.setPublishYear(Integer.parseInt(params.get("publishYear")));
        b.setPublisher(p);
        b.setAuthors(newAuthors);
        Book nb = bookService.addBook(b);
        for(int i = 0; i < newAuthors.size(); i++){
            newAuthors.get(i).getBooks().add(nb);
            authorService.editAuthor(newAuthors.get(i));
        }
        //if (b.getAuthors().isEmpty()) bookService.addBook(b);
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/authors")
    public String authors(Model model) {
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam(name="id", required=true) String id, Model model) {
        Author author = authorService.getByID(Integer.parseInt(id));
        List<Book> books = author.getBooks();
        for(int i = 0; i < books.size(); i++){
            books.get(i).getAuthors().remove(author);
            bookService.editBook(books.get(i));
        }
//        authorService.editAuthor(author);
//        author = authorService.getByID(Integer.parseInt(id));
        authorService.delete(author);
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "authors";
    }
}
