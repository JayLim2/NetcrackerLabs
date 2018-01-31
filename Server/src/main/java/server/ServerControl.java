/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import controllers.AuthorContainerController;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.ReadWriteLock;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import models.Author;
import models.AuthorsContainer;
import models.Book;

/**
 *
 * @author Алескандр
 */
public class ServerControl implements Runnable{
    
    private AuthorContainerController aCC;
    private ReadWriteLock rwl;

    public ServerControl(AuthorContainerController aCC, ReadWriteLock rwl){
        this.aCC = aCC;
        this.rwl = rwl;
    }
    
    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        while (true) {
            viewMenu();
                String inp = in.nextLine();
                switch (inp) {
                    case "1":
                        viewAll();
                        break;//load from file
                    case "2":
                        merge(in);
                        break;//add another file 
                    case "3":
   
                        break;//maybe change default
                    case "q":
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            }
    }
    
    private void viewMenu() {
        System.out.println("Main menu\n================");
        System.out.println("1. Load from file(not implemented)");
        System.out.println("2. Merge with another file");
        System.out.println();
        System.out.print("Input menu command id: ");
    }
    
    private void merge(Scanner in){
        try {   
                JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
                Unmarshaller unmarsh = context.createUnmarshaller();
                AuthorsContainer authors;
//                new File("XML1.xml")
                String str = in.nextLine();
                authors = (AuthorsContainer)unmarsh.unmarshal(new File(str));
                AuthorContainerController aCC2 = new AuthorContainerController(authors);
                aCC2.reInitAuthorsInBooks();
                rwl.writeLock().lock();
                try{
                    aCC.merge(aCC2.getAuthorsContainer());
                    aCC.resolveIds();
                }
                finally{
                    rwl.writeLock().unlock();
                }
            } catch (JAXBException ex) {
                System.out.println("Default File not found, or corrupted.");
            }
    }
    
    public void viewAll(){
        rwl.readLock().lock();
        try{
        List<Author> tempA = aCC.getAuthorsContainer().getAuthors();
            for (int j = 0; j < aCC.getAuthorsContainer().getAuthors().size(); j++) {
                System.out.printf("%5d %15s\n", tempA.get(j).getId(), tempA.get(j).getName());
                List<Book> tempB = aCC.getAuthorsContainer().getAuthors().get(j).getBooks();
                for (int i = 0; i < tempB.size(); i++) {
                    System.out.printf("     %5d %25s %15s %5d\n", tempB.get(i).getId(), tempB.get(i).getTitle(), tempB.get(i).getAuthor().getName(), tempB.get(i).getPublishYear());
                }
            }
        }
        finally{
            rwl.readLock().unlock();
        }
    }
}
