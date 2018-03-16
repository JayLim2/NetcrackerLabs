/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.postgresql.PostgreSQLAuthorDAO;
import database.postgresql.PostgreSQLDAOFactory;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {
 
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        PostgreSQLDAOFactory daoFactory;
        try {
            daoFactory = PostgreSQLDAOFactory.getInstance("C:\\Users\\Алескандр\\Documents\\GitHub\\NetLabsStage2\\NetcrackerLabs\\Server\\src\\main\\java\\database\\databaseStartScript.sql");
            PostgreSQLAuthorDAO postgreSQLAuthorDAO = daoFactory.getAuthorDAO();
            model.addAttribute("name", postgreSQLAuthorDAO.read(Integer.parseInt(name)).getAuthorName());
        } catch (SQLException ex) {
            Logger.getLogger(GreetingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "greeting";
    }

}
