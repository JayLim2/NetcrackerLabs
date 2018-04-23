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
import models.EmptyFieldException;
import models.YearOutOfBoundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

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
    @PostMapping("/books")
    public String greeting(Model model) {
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "books";
    }


    @GetMapping("/authors")
    @PostMapping("/authors")
    public String authors(Model model) {
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/publishers")
    @PostMapping("/publishers")
    public String publishers(Model model) {
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "publishers";
    }

    //АВТОРЫ
    @RequestMapping("/addAuthor")
    public String addAuthor(Model model) {
        return "addAuthor";
    }

    @PostMapping("/submitAddAuthor")
    public ModelAndView submitAddAuthor(@RequestParam Map<String, String> params, Model model,
                                        RedirectAttributes redirectAttributes) {
//        ModelAndView modelAndView = new ModelAndView();
//        RedirectView redirectView = new RedirectView("/authors");
//        modelAndView.setView(redirectView);
        byte status = 0;
        try {
            Author a = new Author();
            String authorName = params.get("authorname");
            if (authorName.trim().isEmpty())
                throw new EmptyFieldException();
            a.setAuthorName(authorName);
            authorService.addAuthor(a);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение целостности.");
            status = 1;
        } catch (EmptyFieldException ex) {
            System.out.println("Имя автора не заполнено.");
            status = 2;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Внутренняя ошибка.");
            status = 3;
        }
//        model.addAttribute("submitAddStatus", status);
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        if (status != 0) {
            redirectAttributes.addFlashAttribute("authorName", params.get("authorname"));
            return new ModelAndView(new RedirectView("/addAuthor"));
//            model.addAttribute("authorName", params.get("authorname"));
//            return "addAuthor";
        }
        List<Author> authors = authorService.getAll();
//        model.addAttribute("authors", authors);
//        return "authors";
        redirectAttributes.addFlashAttribute("authors", authors);

        return new ModelAndView(new RedirectView("/authors"));
    }

    @RequestMapping("/editAuthor")
    public String editAuthor(@RequestParam(name = "id", required = false) String id, Model model) {
        try {
            Author a = authorService.getByID(Integer.parseInt(id));
            model.addAttribute("authorID", a.getAuthorID());
            model.addAttribute("authorName", a.getAuthorName());
        } catch (Exception e) {
        }
        return "editAuthor";

    }

    @PostMapping("/submitEditAuthor")
    public ModelAndView submitEditAuthor(@RequestParam Map<String, String> params, Model model,
                                         RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Author a = authorService.getByID(Integer.parseInt(params.get("id")));
            String authorName = params.get("authorname");
            if (authorName.trim().isEmpty())
                throw new EmptyFieldException();
            a.setAuthorName(authorName);
//            model.addAttribute("author", a);
//            redirectAttributes.addFlashAttribute("author", a);
            authorService.editAuthor(a);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такой автор уже существует.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого автора не существует.");
            status = 2;
        } catch (EmptyFieldException ex) {
            System.out.println("Имя автора не заполнено.");
            status = 3;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение целостности.");
            status = 4;
        }
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        if (status != 0) {
            redirectAttributes.addFlashAttribute("authorID", Integer.parseInt(params.get("id")));
            redirectAttributes.addFlashAttribute("authorName", params.get("authorname"));
//            ModelAndView modelAndView = new ModelAndView();
//            RedirectView redirectView = new RedirectView("/editAuthor");
//            modelAndView.setView(redirectView);
            return new ModelAndView(new RedirectView("/editAuthor"));
//            model.addAttribute("submitEditStatus", status);
//            model.addAttribute("authorID",params.get("id"));
//            model.addAttribute("authorName",params.get("authorname"));
//            return "editAuthor";
        }
        List<Author> alist = authorService.getAll();
        redirectAttributes.addFlashAttribute("authors", alist);
//        model.addAttribute("authors", alist);
//        return "authors";
//        RedirectView redirectView = new RedirectView("/authors");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setView(redirectView);
        return new ModelAndView(new RedirectView("/authors"));
    }

    @PostMapping("/deleteAuthor")
    public ModelAndView deleteAuthor(@RequestParam(name = "id", required = true) String id, Model model,
                                     RedirectAttributes redirectAttributes) {
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
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить автора.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого автора не существует. Удаление невозможно.");
            status = 2;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить автора.");
            status = 1;
        }
        List<Author> authors = authorService.getAll();

//        model.addAttribute("authors", authors);
//        model.addAttribute("submitDelStatus", status);

//        ModelAndView modelAndView = new ModelAndView();
//        RedirectView redirectView = new RedirectView("/authors");
//        modelAndView.setView(redirectView);
        redirectAttributes.addFlashAttribute("authors", authors);
        redirectAttributes.addFlashAttribute("submitDelStatus", status);

        return new ModelAndView(new RedirectView("/authors"));
    }

    //ИЗДАТЕЛИ
    @RequestMapping("/addPublisher")
    public String addPublisher(Model model) {
        return "addPublisher";
    }

    @PostMapping("/submitAddPublisher")
    public ModelAndView submitAddPublisher(@RequestParam Map<String, String> params, Model model,
                                           RedirectAttributes redirectAttributes) {
//        ModelAndView modelAndView = new ModelAndView();
//        RedirectView redirectView = new RedirectView("/publishers");
//        modelAndView.setView(redirectView);
        byte status = 0;
        try {
            Publisher p = new Publisher();
            String pubname = params.get("publishername");
            if (pubname.trim().isEmpty())
                throw new EmptyFieldException();
            p.setPublisherName(pubname);
            publisherService.addPublisher(p);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Нарушение ограничений при добавлении издателя.");
            status = 1;
        } catch (EmptyFieldException ex) {
            System.out.println(ex.getCause());
            System.out.println("Название издателя не заполнено.");
            status = 2;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Такой издатель уже существует.");
            status = 3;
        }
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        if (status != 0) {
            redirectAttributes.addFlashAttribute("publisherName", params.get("publishername"));
            return new ModelAndView(new RedirectView("/addPublisher"));
//            model.addAttribute("submitAddStatus", status);
//            model.addAttribute("publisherName", params.get("publishername"));
//            return "addPublisher";

        }
        List<Publisher> publishers = publisherService.getAll();
//        model.addAttribute("publishers", publishers);
        //redirectAttributes.addFlashAttribute("submitAddStatus", status);
        redirectAttributes.addFlashAttribute("publishers", publishers);
        return new ModelAndView(new RedirectView("/publishers"));
    }

    //    @PostMapping("/editPublisher")
    @RequestMapping(value = "/editPublisher")
    public String editPublisher(@RequestParam(name = "id", required = false) String id, Model model) {
        try {
            Publisher p = publisherService.getByID(Integer.parseInt(id));
            model.addAttribute("publisherID", p.getPublisherID());
            model.addAttribute("publisherName", p.getPublisherName());
        } catch (Exception e) {
        }


        return "editPublisher";
    }

    @PostMapping("/submitEditPublisher")
    public ModelAndView submitEditPublisher(@RequestParam Map<String, String> params, Model model,
                                            RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Publisher p = publisherService.getByID(Integer.parseInt(params.get("id")));
            String pubname = params.get("publishername");
            if (pubname.trim().isEmpty())
                throw new EmptyFieldException();
            p.setPublisherName(pubname);
            publisherService.editPublisher(p);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такой издатель уже существует.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого издателя не существует.");
            status = 2;
        } catch (EmptyFieldException ex) {
            System.out.println(ex.getCause());
            System.out.println("Название издателя не заполнено.");
            status = 3;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Такой издатель уже существует.");
            status = 4;
        }
//        model.addAttribute("submitEditStatus", status);
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        if (status != 0) {
            redirectAttributes.addFlashAttribute("publisherID", Integer.parseInt(params.get("id")));
            redirectAttributes.addFlashAttribute("publisherName", params.get("publishername"));
//            model.addAttribute("publisherID",Integer.parseInt(params.get("id")));
//            model.addAttribute("publisherName", params.get("publishername"));
//            ModelAndView modelAndView = new ModelAndView();
//            RedirectView redirectView = new RedirectView("/editPublisher");
//            modelAndView.setView(redirectView);
            return new ModelAndView(new RedirectView("/editPublisher"));

//            return "editPublisher";

        }
        List<Publisher> publishers = publisherService.getAll();
//        model.addAttribute("publishers", publishers);
        redirectAttributes.addFlashAttribute("publishers", publishers);

//        RedirectView redirectView = new RedirectView("/publishers");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setView(redirectView);
        return new ModelAndView(new RedirectView("/publishers"));

    }

    @PostMapping("/deletePublisher")
    public ModelAndView deletePublisher(@RequestParam(name = "id", required = true) String id, Model model,
                                        RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Publisher publisher = publisherService.getByID(Integer.parseInt(id));
            publisherService.delete(publisher);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить издателя.");
            status = 1;
        } catch (NullPointerException ex) {
            System.out.println("Такого издателя не существует.");
            status = 2;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить издателя.");
            status = 1;
        }
        List<Publisher> publishers = publisherService.getAll();
//        model.addAttribute("publishers", publishers);
//        model.addAttribute("submitDelStatus", status);

//        ModelAndView modelAndView = new ModelAndView();
//        RedirectView redirectView = new RedirectView("/publishers");
//        modelAndView.setView(redirectView);
        redirectAttributes.addFlashAttribute("publishers", publishers);
        redirectAttributes.addFlashAttribute("submitDelStatus", status);
        return new ModelAndView(new RedirectView("/publishers"));

    }

    //КНИГИ
    @RequestMapping("/add")
    public String add(Model model) {
        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();
        model.addAttribute("publishers", p);
        model.addAttribute("authors", a);
        return "add";
    }

    @PostMapping("/submitAdd")
    public ModelAndView submitAdd(@RequestParam MultiValueMap<String, String> params, Model model,
                                  RedirectAttributes redirectAttributes) {
//        ModelAndView modelAndView = new ModelAndView();
//        RedirectView redirectView = new RedirectView("/books");
//        modelAndView.setView(redirectView);
        byte status = 0;
        try {
            Book b = new Book();
            List<String> aNames = params.get("author");
            List<Author> newAuthors = new LinkedList<>();
            if ((aNames != null) && (aNames.size() > 0))
                for (int i = 0; i < aNames.size(); i++) {
                    newAuthors.add(authorService.getByName(aNames.get(i)));
                }
            Publisher p = publisherService.getByName(params.get("publisher").get(0));
            b.setBookName(params.get("booktitle").get(0));
            if (b.getBookName().trim().isEmpty()) {
                throw new EmptyFieldException();
            }
            b.setBrief(params.get("brief").get(0));
            b.setPublishYear(Integer.parseInt(params.get("publishYear").get(0)));
            if (b.getPublishYear() < 0 || b.getPublishYear() > Calendar.getInstance().get(Calendar.YEAR)) {
                throw new YearOutOfBoundsException();
            }
            b.setPublisher(p);
            b.setAuthors(newAuthors);
            Book nb = bookService.addBook(b);
            if (newAuthors.size() > 0)
                for (int i = 0; i < newAuthors.size(); i++) {
                    newAuthors.get(i).getBooks().add(nb);
                    authorService.editAuthor(newAuthors.get(i));
                }
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такая книга уже существует или иное нарушение ограничений.");
            status = 1;
        } catch (YearOutOfBoundsException ex) {
            System.out.println("Неверный год.");
            status = 2;
        } catch (NumberFormatException ex) {
            System.out.println("Год должен быть числом.");
            status = 3;
        } catch (EmptyFieldException ex) {
            System.out.println(ex.getCause());
            System.out.println("Название книги не заполнено.");
            status = 4;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Иное нарушение ограничений.");
            status = 5;
        }
        if (status != 0) {
            return onWrongSubmitAddBook(params, model, status, redirectAttributes);
        }
        List<Book> books = bookService.getAll();
//        model.addAttribute("books", books);
//        model.addAttribute("submitAddStatus", status);
//        return "books";
        redirectAttributes.addFlashAttribute("books", books);
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        return new ModelAndView(new RedirectView("/books"));
    }

    private ModelAndView onWrongSubmitAddBook(MultiValueMap<String, String> params, Model model, byte status,
                                              RedirectAttributes redirectAttributes) {
//        model.addAttribute("bookTitle", params.get("booktitle").get(0));
//        model.addAttribute("publishYear", params.get("publishYear").get(0));
//        model.addAttribute("brief", params.get("brief").get(0));
//        model.addAttribute("publishers", publisherService.getAll());
//        model.addAttribute("bpublisher", publisherService.getByName(params.get("publisher").get(0)));
//        model.addAttribute("authors", authorService.getAll());

        redirectAttributes.addFlashAttribute("bookTitle", params.get("booktitle").get(0));
        redirectAttributes.addFlashAttribute("publishYear", params.get("publishYear").get(0));
        redirectAttributes.addFlashAttribute("brief", params.get("brief").get(0));
        redirectAttributes.addFlashAttribute("publishers", publisherService.getAll());
        redirectAttributes.addFlashAttribute("bpublisher", publisherService.getByName(params.get("publisher").get(0)));
        redirectAttributes.addFlashAttribute("authors", authorService.getAll());

        List<Author> bauthors = new ArrayList<>();
        List<String> bauthors_s = params.get("author");
        if (bauthors_s != null && bauthors_s.size() > 0)
            for (int i = 0; i < bauthors_s.size(); i++) {
                bauthors.add(authorService.getByName(bauthors_s.get(i)));
            }
//        model.addAttribute("bauthors", bauthors);
        redirectAttributes.addFlashAttribute("bauthors", bauthors);
        // model.addAttribute("author", authorService.getByName(params.get("author").get(0)));

//        model.addAttribute("submitAddStatus", status);
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        return new ModelAndView(new RedirectView("/add"));
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "id", required = false) String id, Model model) {
        try {
            Book b = bookService.getByID(Integer.parseInt(id));
            model.addAttribute("bookID", b.getBookID());
            model.addAttribute("bookName", b.getBookName());
            model.addAttribute("brief", b.getBrief());
            model.addAttribute("publishYear", b.getPublishYear());
            model.addAttribute("bpublisher", b.getPublisher());
            model.addAttribute("bauthors", b.getAuthors());
        } catch (Exception e) {
        }
        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();
        model.addAttribute("publishers", p);
        model.addAttribute("authors", a);
        return "edit";
    }

    @PostMapping("/submitEdit")
    public ModelAndView submitEdit(@RequestParam MultiValueMap<String, String> params, Model model,
                                   RedirectAttributes redirectAttributes) {
//        RedirectView redirectView = new RedirectView("/books");
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setView(redirectView);
        byte status = 0;
        try {
            if (params.get("booktitle").get(0).trim().isEmpty()) {
                throw new EmptyFieldException();
            }
            tstuff.bookEditTransaction(params, model);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Такая книга уже существует или иное нарушение ограничений.");
            status = 1;
        } catch (YearOutOfBoundsException ex) {
            System.out.println("Неверный год.");
            status = 2;
        } catch (NumberFormatException ex) {
            System.out.println("Год должен быть числом.");
            status = 3;
        } catch (EmptyFieldException ex) {
            System.out.println(ex.getCause());
            System.out.println("Название книги не заполнено.");
            status = 4;
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Иное нарушение ограничений.");
            status = 5;
        }

        if (status != 0) {
            return onWrongSubmitEditBook(params, model, status, redirectAttributes);
        }
//        model.addAttribute("books", bookService.getAll());
//        model.addAttribute("submitEditStatus", status);
//        return "books";
        redirectAttributes.addFlashAttribute("books", bookService.getAll());
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        return new ModelAndView(new RedirectView("/books"));

    }

    private ModelAndView onWrongSubmitEditBook(MultiValueMap<String, String> params, Model model, byte status,
                                               RedirectAttributes redirectAttributes) {
        Book b = bookService.getByID(Integer.parseInt(params.get("id").get(0)));
        System.out.println(b);
//        model.addAttribute("bookID", b.getBookID());
//        model.addAttribute("bookName", params.get("booktitle").get(0));
//        model.addAttribute("brief", params.get("brief").get(0));
//        model.addAttribute("publishYear", params.get("publishYear").get(0));
//        model.addAttribute("bpublisher", publisherService.getByName(params.get("publisher").get(0)));

        redirectAttributes.addFlashAttribute("bookID", b.getBookID());
        redirectAttributes.addFlashAttribute("bookName", params.get("booktitle").get(0));
        redirectAttributes.addFlashAttribute("brief", params.get("brief").get(0));
        redirectAttributes.addFlashAttribute("publishYear", params.get("publishYear").get(0));
        redirectAttributes.addFlashAttribute("bpublisher", publisherService.getByName(params.get("publisher").get(0)));
//test
        redirectAttributes.addFlashAttribute("authors", authorService.getAll());

        List<Author> bauthors = new ArrayList<>();
        List<String> bauthors_s = params.get("author");
        if (bauthors_s != null && bauthors_s.size() > 0)
            for (int i = 0; i < bauthors_s.size(); i++) {
                bauthors.add(authorService.getByName(bauthors_s.get(i)));
            }
//        model.addAttribute("bauthors", bauthors);
        redirectAttributes.addFlashAttribute("bauthors", bauthors);
//        for (Author author : b.getAuthors()) {
//            System.out.println(author);
//        }
        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();

//        model.addAttribute("publishers", p);
//        model.addAttribute("authors", a);
//        model.addAttribute("submitEditStatus", status);
//        return "edit";

        redirectAttributes.addFlashAttribute("publishers", p);
        redirectAttributes.addFlashAttribute("authors", a);
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        return new ModelAndView(new RedirectView("/edit"));


    }

    @PostMapping("/delete")
    public ModelAndView delete(@RequestParam(name = "id", required = true) String id, Model model,
                               RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Book book = bookService.getByID(Integer.parseInt(id));
            for (Author author : book.getAuthors()) {
                author.getBooks().remove(book);
                authorService.editAuthor(author);
            }
            bookService.delete(book);
        } catch (JpaSystemException | DataIntegrityViolationException ex) {
            System.out.println(ex.getCause());
            System.out.println("Невозможно удалить книгу.");
            status = 1;
        }
        List<Book> books = bookService.getAll();
//        model.addAttribute("books", books);
//        model.addAttribute("submitDelStatus", status);//
//        return "books";

//        ModelAndView modelAndView = new ModelAndView();
//        RedirectView redirectView = new RedirectView("/books");
//        modelAndView.setView(redirectView);
        redirectAttributes.addFlashAttribute("books", books);
        redirectAttributes.addFlashAttribute("submitDelStatus", status);
        return new ModelAndView(new RedirectView("/books"));

    }
}
