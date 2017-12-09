/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.AuthorContainerController;
import Models.*;
import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * @author Алескандр
 */
public class AuthorContainerView {
    private AuthorContainerController aCC;//пока так. разберусь с событиями - перепилю.вроде как контрллер должен слушать то что мы во view посылаем

    public AuthorContainerView(AuthorContainerController aCC) {
        this.aCC = aCC;
    }

    public void mainLoop() {
        Scanner in = new Scanner(System.in);
        boolean menuState = false;
        while (true) {
            viewMenu(menuState);
            if (menuState) {
                String inp = in.nextLine();
                switch (inp) {
                    case "1":
                        viewBooks();
                        break;//view books
                    case "2":
                        viewAuthors();
                        menuState = false;
                        break;//view authors and switch to author edit
                    case "3":
                        addBook(in);
                        break;//add book
                    case "4":
                        deleteBook(in);
                        break;//delete book
                    case "5":
                        editBook(in);
                        break;
                    case "6":
                        loadFromFile(in);
                        break;
                    case "7":
                        saveToFile(in);
                        break;
                    case "q":
                        break;
                    default:
                        System.out.println("Unknown command");
                }
                if (inp.equals("q")) break;
            } else {
                String inp = in.nextLine();
                switch (inp) {
                    case "1":
                        viewBooks();
                        menuState = true;
                        break;//view books and switch to book edit
                    case "2":
                        viewAuthors();
                        break;//view authors 
                    case "3":
                        addAuthor(in);
                        break;//add author
                    case "4":
                        deleteAuthor(in);
                        break;//delete author
                    case "5":
                        editAuthor(in);
                        break;//edit author
                    case "6":
                        loadFromFile(in);
                        break;
                    case "7":
                        saveToFile(in);
                        break;
                    case "q":
                        break;
                    default:
                        System.out.println("Unknown command");
                }
                if (inp.equals("q")) break;
            }

            /*System.out.println("Чтобы продолжить введите 0.");
            while(in.nextLine().charAt(0) != '0') {
                System.out.println("Чтобы продолжить введите 0.");
            }*/
        }
    }

    private void loadFromFile(Scanner in) {
        try {

            System.out.print("Input name of the file: ");
            String str = in.nextLine();
            FileInputStream fin = new FileInputStream(str);
            aCC.load(fin);
            System.out.println("Loaded sucsefully");
        } catch (JAXBException ex) {
            System.out.println("File format error. Load cancelled");
        } catch (FileNotFoundException ex) {
            System.out.println("File not found. Load cancelled");
        }
    }


    private void saveToFile(Scanner in) {
        try {
            System.out.print("Input name of the file: ");
            String str = in.nextLine();
            File fout = new File(str);
            aCC.save(fout);
            System.out.println("Saved");
        } catch (JAXBException ex) {
            System.out.println("Should not happen at all. Save cancelled");
        }
    }


    private void viewBooks() {
        if (aCC.checkBooksInAuthor()) {
            System.out.printf("%45s%n", "=============== Book list ===============");
            for (int j = 0; j < aCC.getAuthorsContainer().getAuthors().size(); j++) {
                List<Book> tempB = aCC.getAuthorsContainer().getAuthors().get(j).getBooks();
                for (int i = 0; i < tempB.size(); i++) {
                    System.out.printf("%5d %25s %15s %5d\n", tempB.get(i).getId(), tempB.get(i).getTitle(), tempB.get(i).getAuthor().getName(), tempB.get(i).getPublishYear());
                }
            }
        } else
            System.out.println("Book list is empty\n");

        System.out.println();
    }

    private void viewAuthors() {
        if (!aCC.getAuthorsContainer().getAuthors().isEmpty()) {
            System.out.printf("%20s%n", "=============== Author list ===============");
            List<Author> tempA = aCC.getAuthorsContainer().getAuthors();
            for (int i = 0; i < tempA.size(); i++) {
                System.out.printf("%5d %15s\n", tempA.get(i).getId(), tempA.get(i).getName());
            }
        } else {
            System.out.println("Author list is empty\n");
        }
    }

    private void viewMenu(boolean menustate) {
        //menustate = true - MAIN MENU, else - AUTHOR EDIT MENU
        if (menustate) {
            System.out.println("Main menu\n================");
            System.out.println("1. Book list");
            System.out.println("2. Author list");
            System.out.println("3. Add book");
            System.out.println("4. Remove book");
            System.out.println("5. Change book");
            System.out.println("6. Load form file");
            System.out.println("7. Save to file");
            System.out.println("q. Quit");
        } else {
            System.out.println("Author mode\n================");
            System.out.println("1. Book list");
            System.out.println("2. Author list");
            System.out.println("3. Add author");
            System.out.println("4. Remove author");
            System.out.println("5. Change auhtor");
            System.out.println("6. Load form file");
            System.out.println("7. Save to file");
            System.out.println("q. Quit");
        }
        System.out.println();
        System.out.print("Input menu command id: ");
    }

    private void addBook(Scanner in) {
        System.out.print("Input book's title: ");
        String name = in.nextLine();
        cancelCheck(name);
        System.out.println("Input book's author: ");
        viewAuthors();
        System.out.printf("%5d %15s\n", -1, "Add author");
        boolean mark = false;
        boolean mark2 = false;
        while (!mark) {
            try {
                String test = in.nextLine();
                cancelCheck(test);
                int id = new Integer(test);
                Book tempB;
                if (id == -1) id = addAuthor(in);
                Author author = aCC.getAuthor(id);
                System.out.print("Input the year of publishing: ");
                while (!mark2) {
                    String test1 = in.nextLine();
                    cancelCheck(test1);
                    try {
                        int publishYear = new Integer(test1);
                        tempB = new Book(name, author, publishYear, "", "");
                        aCC.addBook(tempB, id, publishYear);
                        System.out.println("Book " + "\"" + tempB.getTitle() + "\"" + " added to author " + "\"" + author.getName() + "\"\n");
                        mark2 = true;
                        try {
                            aCC.save(new File("XML1.xml"));
                        } catch (JAXBException ex) {
                            System.out.println("Should not happen at all. Save cancelled");
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("Year must be a number. Try again");
                    } catch (YearOutOfBoundsException ex) {
                        System.out.println("Year out of range. Try again");
                    }
                }
                mark = true;
            } catch (NumberFormatException ex) {
                System.out.println("Id must be a number. Try again.");
            } catch (IndexOutOfBoundsException ex) {
                System.out.println("Index out of range. Try again.");
            }
        }
    }

    private int addAuthor(Scanner in) {
        System.out.print("Input author's name: ");
        String name = in.nextLine();
        cancelCheck(name);
        Author author = new Author(name);
        aCC.addAuthor(author);
        try{
            aCC.save(new File("XML1.xml"));
        }catch (JAXBException ex) {
            System.out.println("Should not happen at all. Save cancelled");
        }
        return author.getId();
    }

    private void deleteAuthor(Scanner in) {
        boolean mark = false;
        if (!aCC.getAuthorsContainer().getAuthors().isEmpty()) {
            viewAuthors();
            while (!mark) {
                System.out.print(("Input auhtor's id: "));
                try {
                    String check = in.nextLine();
                    cancelCheck(check);
                    int id = new Integer(check);
                    boolean mark2 = false;
                    while (!mark2) {
                        if (id < aCC.getAuthorsContainer().getAuthors().size()) {
                            System.out.print(("Warning! deleting an author will remove all his books as well. Procced? Y/N: "));
                            String str = in.nextLine();
                            cancelCheck(str);
                            if (str.toUpperCase().equals("Y")) {
                                aCC.removeAuthor(id);
                                System.out.println("Deletion succsessful.");
                                mark2 = true;
                                try {
                                    aCC.save(new File("XML1.xml"));
                                } catch (JAXBException ex) {
                                    System.out.println("Should not happen at all. Save cancelled");
                                }
                            } else if (str.toUpperCase().equals("N")) {
                                System.out.println("Ok. Deletion canceled.");
                                break;
                            } else System.out.println("Unknown command: " + str);
                        } else throw new IndexOutOfBoundsException();
                    }
                    mark = true;
                } catch (NumberFormatException ex) {
                    System.out.println("Id must be a number. Try again.");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Index out of range. Try again.");
                }
            }
        } else {
            System.out.println("Author list is empty\n");
        }
    }

    private void editAuthor(Scanner in) {
        boolean mark = false;
        if (!aCC.getAuthorsContainer().getAuthors().isEmpty()) {
            viewAuthors();
            while (!mark) {
                System.out.print(("Input auhtor's id: "));
                try {
                    String check = in.nextLine();
                    cancelCheck(check);
                    int id = new Integer(check);
                    Author cauthor = aCC.getAuthor(id);
                    String oldName = cauthor.getName();
                    System.out.println(oldName);
                    System.out.print(("Input auhtor's new name: "));

                    String str = in.nextLine();
                    cancelCheck(str);
                    cauthor.setName(str);
                    System.out.println("Author " + "\"" + oldName + "\"" + " got new name " + "\"" + cauthor.getName() + "\"\n");
                    mark = true;
                    try {
                        aCC.save(new File("XML1.xml"));
                    } catch (JAXBException ex) {
                        System.out.println("Should not happen at all. Save cancelled");
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Id must be a number. Try again.");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Index out of range. Try again.");
                }
            }
            System.out.println("Edition succsessful.\n");
        } else {
            System.out.println("Author list is empty");
        }

    }

    private void editBook(Scanner in) {
        boolean mark = false;
        if (aCC.checkBooksInAuthor()) {
            viewBooks();
            while (!mark) {
                System.out.print(("Input book's id: "));
                try {
                    String c = in.nextLine();
                    cancelCheck(c);
                    int id = new Integer(c);
                    Book cbook = aCC.getBook(id);
                    boolean mark1 = false;
                    while (!mark1) {
                        System.out.printf("%25s %15s\n", cbook.getTitle(), cbook.getAuthor().getName());
                        System.out.print("Edit book's title Y/N?: ");
                        String str = in.nextLine();
                        cancelCheck(str);
                        if (str.toUpperCase().equals("Y")) {
                            System.out.print("Input book's new title: ");
                            str = in.nextLine();
                            cancelCheck(str);
                            String oldTitle = cbook.getTitle();
                            cbook.setTitle(str);
                            System.out.println("Book " + "\"" + oldTitle + "\"" + " got new title " + "\"" + cbook.getTitle() + "\"\n");
                            mark1 = true;
                            try {
                                aCC.save(new File("XML1.xml"));
                            } catch (JAXBException ex) {
                                System.out.println("Should not happen at all. Save cancelled");
                            }
                        } else if (str.toUpperCase().equals("N")) {
                            break;
                        } else System.out.println("Unknown command: " + str);
                    }
                    boolean mark2 = false;
                    while (!mark2) {
                        System.out.print("Edit book's author Y/N?: ");
                        String str = in.nextLine();
                        cancelCheck(str);
                        if (str.toUpperCase().equals("Y")) {
                            System.out.printf("%5d %15s\n", -1, "Add author");
                            viewAuthors();
                            System.out.print("Input book's new auhtor's id: ");
                            try {
                                String check = in.nextLine();
                                cancelCheck(check);
                                int id2 = new Integer(check);
                                if (id2 == -1) id2 = addAuthor(in);
                                Author author = aCC.getAuthor(id2);
                                aCC.removeBook(id);
                                cbook.setAuthor(author);
                                aCC.addBook(cbook, id2);
                                System.out.println("Book " + "\"" + cbook.getTitle() + "\"" + " is moved to new author " + "\"" + aCC.getAuthor(id2).getName() + "\"" + " from old " + "\"" + aCC.getAuthor(id).getName() + "\"\n");
                                try {
                                    aCC.save(new File("XML1.xml"));
                                } catch (JAXBException ex) {
                                    System.out.println("Should not happen at all. Save cancelled");
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println("Id must be a number. Author Field Edition canceled.");
                            } catch (IndexOutOfBoundsException ex) {
                                System.out.println("Index out of range. Author Field Edition canceled.");
                            }
                            mark2 = true;
                        } else if (str.toUpperCase().equals("N")) {
                            break;
                        } else System.out.println("Unknow command: " + str);
                    }
                    boolean mark3 = false;
                    while (!mark3) {
                        System.out.print("Edit book's year of publishing Y/N?: ");
                        String str = in.nextLine();
                        cancelCheck(str);
                        if (str.toUpperCase().equals("Y")) {
                            System.out.print("Input book's new year of publishing: ");
                            try {
                                String check = in.nextLine();
                                cancelCheck(check);
                                int publishYear = new Integer(check);
                                int oldPY = cbook.getPublishYear();
                                cbook.setPublishYear(publishYear);
                                System.out.println("Old publish year " + "\"" + oldPY + "\"" + " is changed to new " + "\"" + cbook.getPublishYear() + "\"\n");
                                mark3 = true;
                                try {
                                    aCC.save(new File("XML1.xml"));
                                } catch (JAXBException ex) {
                                    System.out.println("Should not happen at all. Save cancelled");
                                }
                            } catch (NumberFormatException ex) {
                                System.out.println("Year must be a number. Try again.");
                            } catch (YearOutOfBoundsException ex) {
                                System.out.println("Year out of range. Try again.");
                            }
                        } else if (str.toUpperCase().equals("N")) {
                            break;
                        } else System.out.println("Unknow command: " + str);
                    }
                    mark = true;
                    System.out.println("Edition succsessful.\n");
                } catch (NumberFormatException ex) {
                    System.out.println("It must be a number. Try again.");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Index out of range.Try again.");
                }
            }
        } else
            System.out.println("Book list is empty.");
    }


    private void deleteBook(Scanner in) {

        if (aCC.checkBooksInAuthor()) {
            boolean mark = false;
            viewBooks();
            while (!mark) {
                try {
                    System.out.print(("Input book's id: "));
                    String check = in.nextLine();
                    cancelCheck(check);
                    int id = new Integer(check);
                    aCC.removeBook(id);
                    System.out.println("Deletion successdful");
                    mark = true;
                    try {
                        aCC.save(new File("XML1.xml"));
                    } catch (JAXBException ex) {
                        System.out.println("Should not happen at all. Save cancelled");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Id must be a number. Try again.");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Index out of range. Try again.");
                }
            }
        } else
            System.out.println("Book list is empty\n");

    }

    //Метод проверки ввода "отмены"
    private void cancelCheck(String in){
        if (in.trim().equalsIgnoreCase("cancel")){
            System.out.println("Action canceled");
            mainLoop();
        }
    }
}
