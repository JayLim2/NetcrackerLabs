/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.service.AuthorService;
import database.service.BookService;
import database.service.PublisherService;
import entity.Author;
import entity.Book;
import entity.Publisher;
import java.sql.SQLException;
import java.sql.Savepoint;
import models.YearOutOfBoundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import javax.annotation.Resource;
import javax.persistence.PersistenceException;
import javax.sql.DataSource;
import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class GreetingController {

    @Autowired
    DataSource dataSource;
    
    @Autowired
    BookService bookService;
    
    @Autowired
    AuthorService authorService;
    
    @Autowired
    PublisherService publisherService;
    
    @Autowired
    TransactionalStuff tstuff;

    //ОСНОВНЫЕ СПИСКИ
    @GetMapping("/books")
    public String greeting(Model model) {
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
    
    @GetMapping("/publishers")
    public String publishers(Model model) {
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    //АВТОРЫ
    @PostMapping("/addAuthor")
    public String addAuthor( Model model) {
        return "addAuthor";
    }

    @PostMapping("/submitAddAuthor")
    public String submitAddAuthor(@RequestParam Map<String,String> params, Model model) {
        byte status = 0;
        try {
            Author a = new Author();
            a.setAuthorName(params.get("authorname"));
            authorService.addAuthor(a);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение целостности.");
            status = 1;
        }catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение целостности.");
            status = 1;
        }
        model.addAttribute("submitAddStatus", status);
        if (status != 0){
            model.addAttribute("authorName", params.get("authorname"));
            return "addAuthor";
        }
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @PostMapping("/editAuthor")
    public String editAuthor(@RequestParam(name="id", required=true) String id, Model model) {
        Author a = authorService.getByID(Integer.parseInt(id));
        model.addAttribute("authorID", a.getAuthorID());
        model.addAttribute("authorName", a.getAuthorName());
        return "editAuthor";
    }

    @PostMapping("/submitEditAuthor")
    public String submitEditAuthor(@RequestParam Map<String,String> params, Model model) {
        byte status = 0;
        Author a = null;
        try {
            a = authorService.getByID(Integer.parseInt(params.get("id")));
            a.setAuthorName(params.get("authorname"));
            model.addAttribute("author", a);
            authorService.editAuthor(a);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение целостности.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Null pointer exception.");
            status = 2;
        }catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение целостности.");
            status = 1;
        }
        if (status != 0){
            model.addAttribute("submitEditStatus", status);
            model.addAttribute("authorID",params.get("id"));
            model.addAttribute("authorName",params.get("authorname"));
            return "editAuthor";
        }
        List<Author> alist = authorService.getAll();
        model.addAttribute("authors", alist);
        return "authors";
    }

    @PostMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam(name = "id", required = true) String id, Model model) {
        byte status = 0;
        try {
            Author author = authorService.getByID(Integer.parseInt(id));
            List<Book> books = author.getBooks();
            for (int i = 0; i < books.size(); i++) {
                books.get(i).getAuthors().remove(author);
                bookService.editBook(books.get(i));
            }
//        authorService.editAuthor(author);
//        author = authorService.getByID(Integer.parseInt(id));
            authorService.delete(author);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить автора.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого автора не существует. Удаление невозможно.");
            status = 2;
        }

        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        model.addAttribute("submitDelStatus", status);

        return "authors";
    }

    //ИЗДАТЕЛИ
    @PostMapping("/addPublisher")
    public String addPublisher(Model model) {
        return "addPublisher";
    }

    @PostMapping("/submitAddPublisher")
    public String submitAddPublisher(@RequestParam Map<String, String> params, Model model) {
        byte status = 0;
        try {
            Publisher p = new Publisher();
            p.setPublisherName(params.get("publishername"));
            publisherService.addPublisher(p);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение ограничений при добавлении издателя.");
            status = 1;
        }catch (Exception ex){
            System.out.println(ex.getCause());
            System.out.println("Такой издатель уже существует.");
            status = 1;
        }
        if(status != 0){
            model.addAttribute("submitAddStatus", status);
            model.addAttribute("publisherName", params.get("publishername"));
            return "addPublisher";
        }
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    @PostMapping("/editPublisher")
    public String editPublisher(@RequestParam(name = "id", required = true) String id, Model model) {
        Publisher p = publisherService.getByID(Integer.parseInt(id));
        model.addAttribute("publisherID", p.getPublisherID());
        model.addAttribute("publisherName", p.getPublisherName());
        return "editPublisher";
    }
    
    @PostMapping("/submitEditPublisher")
    public String submitEditPublisher(@RequestParam Map<String,String> params, Model model) {
        byte status = 0;
        try {
            Publisher p = publisherService.getByID(Integer.parseInt(params.get("id")));
            p.setPublisherName(params.get("publishername"));
            publisherService.editPublisher(p);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такой издатель уже существует.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого издателя не существует.");
            status = 2;
        } catch (Exception ex){
            System.out.println(ex.getCause());
            System.out.println("Такой издатель уже существует.");
            status = 1;
        }
        model.addAttribute("submitEditStatus", status);
        if(status != 0){
            model.addAttribute("publisherID",Integer.parseInt(params.get("id")));
            model.addAttribute("publisherName", params.get("publishername"));
            return "editPublisher";
        }
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    @PostMapping("/deletePublisher")
    public String deletePublisher(@RequestParam(name = "id", required = true) String id, Model model) {
        byte status = 0;
        try {
            Publisher publisher = publisherService.getByID(Integer.parseInt(id));
            publisherService.delete(publisher);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить издателя.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого издателя не существует.");
            status = 2;
        }catch(Exception ex){
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить издателя.");
            status = 1;
        }
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        model.addAttribute("submitDelStatus", status);
        return "publishers";
    }

    //КНИГИ
    @PostMapping("/add")
    public String add(Model model) {
        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();
        model.addAttribute("publishers", p);
        model.addAttribute("authors", a);
        return "add";
    }

    @PostMapping("/submitAdd")
    public String submitAdd(@RequestParam MultiValueMap<String, String> params, Model model) {
        byte status = 0;
        try {
            Book b = new Book();
            List<String> aNames = params.get("author");
            List<Author> newAuthors = new LinkedList<>();
            if (aNames != null)
                for (String aname : aNames) {
                    newAuthors.add(authorService.getByName(aname));
                }
            Publisher p = publisherService.getByName(params.get("publisher").get(0));
            b.setBookName(params.get("booktitle").get(0));
            b.setBrief(params.get("brief").get(0));
            b.setPublishYear(Integer.parseInt(params.get("publishYear").get(0)));
            if (b.getPublishYear() < 0 && b.getPublishYear() > Calendar.YEAR) {
                throw new YearOutOfBoundsException();
            }
            b.setPublisher(p);
            b.setAuthors(newAuthors);
            Book nb = bookService.addBook(b);
            for (int i = 0; i < newAuthors.size(); i++) {
                newAuthors.get(i).getBooks().add(nb);
                authorService.editAuthor(newAuthors.get(i));
            }
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такая книга уже существует или иное нарушение ограничений.");
            status = 1;

            return onWrongSubmitAddBook(params, model, status);
        } catch (YearOutOfBoundsException ex) {
            System.out.println("Неверный год.");
            status = 2;

            return onWrongSubmitAddBook(params, model, status);
        } catch (NumberFormatException ex) {
            System.out.println("Год должен быть числом.");
            status = 3;

            return onWrongSubmitAddBook(params, model, status);
        }
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        model.addAttribute("submitAddStatus", status);
        return "books";
    }

    private String onWrongSubmitAddBook(MultiValueMap<String, String> params, Model model, byte status) {
        model.addAttribute("bookTitle", params.get("booktitle").get(0));
        model.addAttribute("publishYear", params.get("publishYear").get(0));
        model.addAttribute("brief", params.get("brief").get(0));

        model.addAttribute("publishers", publisherService.getAll());
        model.addAttribute("bpublisher", publisherService.getByName(params.get("publisher").get(0)));

        model.addAttribute("authors", authorService.getAll());
        List<Author> bauthors = new ArrayList<>();
        List<String> bauthors_s = params.get("author");
        for (String bauthor : bauthors_s) {
            bauthors.add(authorService.getByName(bauthor));
        }
        model.addAttribute("bauthors", bauthors);
        model.addAttribute("author", authorService.getByName(params.get("author").get(0)));

        model.addAttribute("submitAddStatus", status);
        return "add";
    }

    @PostMapping("/edit")
    public String edit(@RequestParam(name = "id", required = true) String id, Model model) {
        Book b = bookService.getByID(Integer.parseInt(id));
        model.addAttribute("bookID", b.getBookID());
        model.addAttribute("bookName", b.getBookName());
        model.addAttribute("brief", b.getBrief());
        model.addAttribute("publishYear", b.getPublishYear());
        model.addAttribute("bpublisher", b.getPublisher());
        model.addAttribute("bauthors", b.getAuthors());
        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();
        model.addAttribute("publishers", p);
        model.addAttribute("authors", a);
        return "edit";
    }

    @PostMapping("/submitEdit")
    public String submitEdit(@RequestParam MultiValueMap<String, String> params, Model model) throws SQLException {
        byte status = 0;
        try {
            tstuff.bookEditTransaction(params,model);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такая книга уже существует или иное нарушение ограничений.");
            status = 1;

            return onWrongSubmitEditBook(params, model, status);
        } catch (YearOutOfBoundsException ex) {
            System.out.println("Неверный год.");
            status = 2;

            return onWrongSubmitEditBook(params, model, status);
        } catch (NumberFormatException ex) {
            System.out.println("Год должен быть числом.");
            status = 3;

            return onWrongSubmitEditBook(params, model, status);
        } catch(Exception ex){
            System.out.println(ex.getCause());
            System.out.println("Такая книга уже существует или иное нарушение ограничений.");
            status = 1;

            return onWrongSubmitEditBook(params, model, status);
        }
        model.addAttribute("books", bookService.getAll());
        model.addAttribute("submitEditStatus", status);
        return "books";
    }
    
    private String onWrongSubmitEditBook(MultiValueMap<String, String> params, Model model, byte status) {
        Book b = bookService.getByID(Integer.parseInt(params.get("id").get(0)));
        System.out.println(b);
        model.addAttribute("bookID", b.getBookID());
        model.addAttribute("bookName", params.get("booktitle").get(0));
        model.addAttribute("brief", params.get("brief").get(0));
        model.addAttribute("publishYear", params.get("publishYear").get(0));
        model.addAttribute("bpublisher", publisherService.getByName(params.get("publisher").get(0)));
        List<Author> bauthors = new ArrayList<>();
        List<String> bauthors_s = params.get("author");
        for (String bauthor : bauthors_s) {
            bauthors.add(authorService.getByName(bauthor));
        }
        model.addAttribute("bauthors", bauthors);
        for (Author author : b.getAuthors()) {
            System.out.println(author);
        }
        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();
        model.addAttribute("publishers", p);
        model.addAttribute("authors", a);

        model.addAttribute("submitEditStatus", status);
        return "edit";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(name = "id", required = true) String id, Model model) {
        byte status = 0;
        try {
            Book book = bookService.getByID(Integer.parseInt(id));
            for (Author author : book.getAuthors()) {
                author.getBooks().remove(book);
                authorService.editAuthor(author);
            }
            bookService.delete(book);
        } catch (JpaSystemException ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить книгу.");
            status = 1;
        }
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        model.addAttribute("submitDelStatus", status);
        return "books";
    }
}
