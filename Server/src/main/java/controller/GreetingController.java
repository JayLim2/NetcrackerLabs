/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.postgresql.PostgreSQLAuthorDAO;
import database.postgresql.PostgreSQLDAOFactory;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
 
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        ControllerW daoInterface;
        try {
            daoInterface = ControllerW.getInstance();
            List<Book> books = daoInterface.getAllBooks();
            model.addAttribute("books", books);
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "greeting";
    }

    @GetMapping("/delete")
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
        return "greeting";
    }
}
