/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import entity.Author;
import entity.Book;
import entity.Publisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/books")
    public String greeting(Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            List<Book> books = daoInterface.getAllBooks();
            model.addAttribute("books", books);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "books";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam(name="id", required=true) String id, Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            daoInterface.deleteBook(0, Integer.parseInt(id));
            List<Book> books = daoInterface.getAllBooks();
            model.addAttribute("books", books);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "books";
    }
    
    @PostMapping("/edit")
    public String edit(@RequestParam(name="id", required=true) String id, Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            Book b = daoInterface.readBook(Integer.parseInt(id));
            model.addAttribute("book", daoInterface.readBook(Integer.parseInt(id)));
            model.addAttribute("publisher", daoInterface.readPublisher(b.getPublisherID()));
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "edit";
    }
    
    @PostMapping("/add")
    public String add( Model model) {
        ControllerW daoInterface;
//        try {
//        } catch (SQLException ex) {
//            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return "add";
    }
    
    @PostMapping("/submitEdit")
    public String submitEdit(@RequestParam Map<String,String> params, Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            Publisher pub = daoInterface.readPublisher(params.get("publisher"));
            String[] authorNames = params.get("authors").split(",");
            int[] aIds = new int[authorNames.length];
            int i = 0;
            for(String aname:authorNames){
                aIds[i] = daoInterface.readAuthor(aname).getAuthorID();
                i++;
            }
            daoInterface.updateBook(Integer.parseInt(params.get("id")), params.get("booktitle"),
                    Integer.parseInt(params.get("publishYear")),
                    params.get("brief"), pub.getPublisherID(), aIds);
            List<Book> books = daoInterface.getAllBooks();
            model.addAttribute("books", books);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "books";
    }
    
    @PostMapping("/submitAdd")
    public String submitAdd(@RequestParam Map<String,String> params, Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            Publisher pub = daoInterface.readPublisher(params.get("publisher"));
            String[] authorNames = params.get("authors").split(",");
            int[] aIds = new int[authorNames.length];
            int i = 0;
            for(String aname:authorNames){
                aIds[i] = daoInterface.readAuthor(aname).getAuthorID();
                i++;
            }
            daoInterface.createBook(params.get("booktitle"),
                    Integer.parseInt(params.get("publishYear")),
                    params.get("brief"), pub.getPublisherID(), aIds);
            List<Book> books = daoInterface.getAllBooks();
            model.addAttribute("books", books);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "books";
    }

    @GetMapping("/authors")
    public String authors(Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            List<Author> authors = daoInterface.getAllAuthors();
            model.addAttribute("authors", authors);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "authors";
    }

    @PostMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam(name="id", required=true) String id, Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            daoInterface.deleteAuthor(Integer.parseInt(id));
            List<Author> authors = daoInterface.getAllAuthors();
            model.addAttribute("authors", authors);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "authors";
    }
}
