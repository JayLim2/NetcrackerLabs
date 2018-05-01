/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.service.AuthorService;
import database.service.BookService;
import database.service.PublisherService;
import database.service.UserService;
import entity.Author;
import entity.Book;
import entity.Publisher;
import entity.User;
import models.EmptyFieldException;
import models.YearOutOfBoundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.sql.DataSource;
import javax.validation.Valid;
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

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(Model model){
        return "login";
    }
    
    @RequestMapping("/admin")
    public String admin(Model model){
        return "admin";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        return "index";
    }


//    @RequestMapping("/error")
//    public String error(Model model){
//        model.addAttribute("error", "у вас нет админских прав");
//        return "error";
//    }
    
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
    
    //для юзеров
    @GetMapping("/userbooks")
    @PostMapping("/userbooks")
    public String ugreeting(Model model) {
        List<Book> books = bookService.getAll();
        model.addAttribute("books", books);
        return "userbooks";
    }


    @GetMapping("/userauthors")
    @PostMapping("/userauthors")
    public String uauthors(Model model) {
        List<Author> authors = authorService.getAll();
        model.addAttribute("authors", authors);
        return "userauthors";
    }

    @GetMapping("/userpublishers")
    @PostMapping("/userpublishers")
    public String upublishers(Model model) {
        List<Publisher> publishers = publisherService.getAll();
        model.addAttribute("publishers", publishers);
        return "userpublishers";
    }

    //АВТОРЫ
    @RequestMapping("/addAuthor")
    public String addAuthor(Model model) {
        return "addAuthor";
    }

    @RequestMapping("/submitAddAuthor")
    public ModelAndView submitAddAuthor(@RequestParam Map<String, String> params, Model model,
                                        RedirectAttributes redirectAttributes) {
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
        
        if (status != 0) {
            model.addAttribute("submitAddStatus", status);
            model.addAttribute("authorName", params.get("authorname"));
            return new ModelAndView("/addAuthor");
        }
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        redirectAttributes.addFlashAttribute("authors", authorService.getAll());

        return new ModelAndView(new RedirectView("/authors"));
    }

    @RequestMapping("/editAuthor")
    public String editAuthor(@RequestParam(name = "id", required = true) String id, Model model,RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Author a = authorService.getByID(Integer.parseInt(id));
            model.addAttribute("authorID", a.getAuthorID());
            model.addAttribute("authorName", a.getAuthorName());
        } 
        catch (NullPointerException ex) {
            System.out.println("Такого автора не существует.");
            status = 2;
            redirectAttributes.addFlashAttribute("submitDelStatus", status);
            return "redirect:/authors";
        }
        return "editAuthor";

    }

    @RequestMapping("/submitEditAuthor")
    public ModelAndView submitEditAuthor(@RequestParam Map<String, String> params, Model model,
                                         RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Author a = authorService.getByID(Integer.parseInt(params.get("id")));
            String authorName = params.get("authorname");
            if (authorName.trim().isEmpty())
                throw new EmptyFieldException();
            a.setAuthorName(authorName);
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
        
        if (status != 0) {
            model.addAttribute("submitEditStatus", status);
            model.addAttribute("authorID", Integer.parseInt(params.get("id")));
            model.addAttribute("authorName", params.get("authorname"));
            return new ModelAndView("/editAuthor");
        }
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        redirectAttributes.addFlashAttribute("authors", authorService.getAll());
        return new ModelAndView(new RedirectView("/authors"));
    }

    @RequestMapping("/deleteAuthor")
    public ModelAndView deleteAuthor(@RequestParam(name = "id", required = true) String id,
                                     RedirectAttributes redirectAttributes) {
        byte status = 0;
        try {
            Author author = authorService.getByID(Integer.parseInt(id));
            List<Book> books = author.getBooks();
            for (int i = 0; i < books.size(); i++) {
                books.get(i).getAuthors().remove(author);
                bookService.editBook(books.get(i));
            }
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
        redirectAttributes.addFlashAttribute("authors", authorService.getAll());
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
       
        if (status != 0) {
            model.addAttribute("submitAddStatus", status);
            model.addAttribute("publisherName", params.get("publishername"));
            return new ModelAndView("/addPublisher");


        }
        List<Publisher> publishers = publisherService.getAll();
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        redirectAttributes.addFlashAttribute("publishers", publishers);
        return new ModelAndView(new RedirectView("/publishers"));
    }

    @RequestMapping(value = "/editPublisher")
    public String editPublisher(@RequestParam(name = "id", required = true) String id, Model model,RedirectAttributes redirectAttributes) {
        try {
            Publisher p = publisherService.getByID(Integer.parseInt(id));
            model.addAttribute("publisherID", p.getPublisherID());
            model.addAttribute("publisherName", p.getPublisherName());
        } catch (NullPointerException e) {
            redirectAttributes.addFlashAttribute("submitDelStatus", 2);
            return "redirect:/publishers";
        }

        return "editPublisher";
    }

    @RequestMapping("/submitEditPublisher")
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

        
        if (status != 0) {
            model.addAttribute("submitEditStatus", status);
            model.addAttribute("publisherID", Integer.parseInt(params.get("id")));
            model.addAttribute("publisherName", params.get("publishername"));

            return new ModelAndView("/editPublisher");


        }
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        List<Publisher> publishers = publisherService.getAll();

        redirectAttributes.addFlashAttribute("publishers", publishers);


        return new ModelAndView(new RedirectView("/publishers"));

    }

    @RequestMapping("/deletePublisher")
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
        redirectAttributes.addFlashAttribute("publishers", publishers);
        redirectAttributes.addFlashAttribute("submitDelStatus", status);
        return new ModelAndView(new RedirectView("/publishers"));

    }

    //КНИГИ
    @RequestMapping("/add")
    public String add(Model model) {
        if (!model.containsAttribute("publishers")){
            List<Publisher> p = publisherService.getAll();
            model.addAttribute("publishers", p);
        }
        if (!model.containsAttribute("authors")){
            List<Author> a = authorService.getAll();
            model.addAttribute("authors", a);
        }
        return "add";
    }

    @RequestMapping("/submitAdd")
    public ModelAndView submitAdd(@RequestParam MultiValueMap<String, String> params, Model model,
                                  RedirectAttributes redirectAttributes) {

        byte status = 0;
        try {
            Book b = new Book();
            List<String> aIDs = params.get("author");
            List<Author> newAuthors = new LinkedList<>();
            if ((aIDs != null) && (aIDs.size() > 0))
                for (int i = 0; i < aIDs.size(); i++) {
                    if(authorService.getByID(Integer.parseInt(aIDs.get(i))) == null) throw new BDModifiedException();
                    newAuthors.add(authorService.getByID(Integer.parseInt(aIDs.get(i))));
                }
            Publisher p = publisherService.getByID(Integer.parseInt(params.get("publisher").get(0)));
            if (p == null) throw new BDModifiedException();
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
        }catch (NullPointerException ex) {
            System.out.println(ex.getCause());
            System.out.println("Изменение бд");
            status = 6;
        }catch (BDModifiedException ex){
            System.out.println(ex.getCause());
            System.out.println("Изменение бд");
            status = 7;
        }
        catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Непредвиденная ошибка");
            status = 5;
        }
        if (status != 0) {
            return onWrongSubmitAddBook(params, model, status, redirectAttributes);
        }
        List<Book> books = bookService.getAll();

        redirectAttributes.addFlashAttribute("books", books);
        redirectAttributes.addFlashAttribute("submitAddStatus", status);
        return new ModelAndView(new RedirectView("/books"));
    }

    private ModelAndView onWrongSubmitAddBook(MultiValueMap<String, String> params, Model model, byte status,
                                              RedirectAttributes redirectAttributes) {

        model.addAttribute("bookTitle", params.get("booktitle").get(0));
        model.addAttribute("publishYear", params.get("publishYear").get(0));
        model.addAttribute("brief", params.get("brief").get(0));
        model.addAttribute("publishers", publisherService.getAll());
        model.addAttribute("bpublisher", publisherService.getByID(Integer.parseInt(params.get("publisher").get(0))));
        model.addAttribute("authors", authorService.getAll());

        List<Author> bauthors = new ArrayList<>();
        List<String> bauthors_s = params.get("author");
        if (bauthors_s != null && bauthors_s.size() > 0)
            for (int i = 0; i < bauthors_s.size(); i++) {
                bauthors.add(authorService.getByID(Integer.parseInt(bauthors_s.get(i))));
            }

        model.addAttribute("bauthors", bauthors);
        model.addAttribute("submitAddStatus", status);
        return new ModelAndView("/add");
    }

    @RequestMapping("/edit")
    public String edit(@RequestParam(name = "id", required = true) String id, Model model, RedirectAttributes redirectAttributes) {
        try {
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
        } catch (NullPointerException e) {
            redirectAttributes.addFlashAttribute("submitDelStatus", 2);
            return "redirect:/books";
        }
        return "edit";
    }

    @RequestMapping("/submitEdit")
    public ModelAndView submitEdit(@RequestParam MultiValueMap<String, String> params, Model model,
                                   RedirectAttributes redirectAttributes) {
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
        } catch (NullPointerException ex) {
            System.out.println(ex.getCause());
            System.out.println("Книги такой больше нет.");
            status = 6;
        } catch (BDModifiedException ex){
            status = 7;
        }
        catch (Exception ex) {
            System.out.println(ex.getCause());
            System.out.println("Непредвиденная ошибка.");
            status = 5;
        }

        if (status != 0) {
            return onWrongSubmitEditBook(params, model, status, redirectAttributes);
        }

        redirectAttributes.addFlashAttribute("books", bookService.getAll());
        redirectAttributes.addFlashAttribute("submitEditStatus", status);
        return new ModelAndView(new RedirectView("/books"));

    }

    private ModelAndView onWrongSubmitEditBook(MultiValueMap<String, String> params, Model model, byte status,
                                               RedirectAttributes redirectAttributes) {
        //Book b = bookService.getByID(Integer.parseInt(params.get("id").get(0)));
        //System.out.println(b);


                model.addAttribute("bookID", params.get("id").get(0));
                model.addAttribute("bookName", params.get("booktitle").get(0));
                model.addAttribute("publishYear", params.get("publishYear").get(0));
                model.addAttribute("brief", params.get("brief").get(0));
                model.addAttribute("publishers", publisherService.getAll());
                model.addAttribute("bpublisher", publisherService.getByID(Integer.parseInt(params.get("publisher").get(0))));
                model.addAttribute("authors", authorService.getAll());

                List<Author> bauthors = new ArrayList<>();
                List<String> bauthors_s = params.get("author");
                if (bauthors_s != null && bauthors_s.size() > 0)
                    for (int i = 0; i < bauthors_s.size(); i++) {
                        bauthors.add(authorService.getByID(Integer.parseInt(bauthors_s.get(i))));
                    }
                model.addAttribute("authors", authorService.getAll());

        model.addAttribute("bauthors", bauthors);

        List<Publisher> p = publisherService.getAll();
        List<Author> a = authorService.getAll();

        model.addAttribute("submitEditStatus", status);
        return new ModelAndView("/edit");


    }

    @RequestMapping("/delete")
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
        }catch (NullPointerException ex) {
            System.out.println("Такой книги не существует.");
            status = 2;
        }
        List<Book> books = bookService.getAll();

        redirectAttributes.addFlashAttribute("books", books);
        redirectAttributes.addFlashAttribute("submitDelStatus", status);
        return new ModelAndView(new RedirectView("/books"));

    }



//    @RequestMapping("/login")
//    public ModelAndView login(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

//    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
//    public ModelAndView home(){
//        ModelAndView modelAndView = new ModelAndView();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByEmail(auth.getName());
//        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
//        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
//        modelAndView.setViewName("admin/home");
//        return modelAndView;
//    }


}
