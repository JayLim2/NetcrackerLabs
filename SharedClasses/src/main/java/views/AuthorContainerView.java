/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import controllers.AuthorContainerController;
import models.Author;
import models.Book;
import models.BookAlreadyExistsException;
import models.YearOutOfBoundsException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;


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

    private static final int BOOK_TITLE_LINIT = 60;
    private static final int AUTHOR_NAME_LIMIT = 75;

    private void addBook(Scanner in) {
        System.out.print("Input book's title: ");
        String name = in.nextLine();
        while (true) {
            name = name.trim();
            if(!name.isEmpty()) {
                if(name.length() <= BOOK_TITLE_LINIT) {
                    break;
                } else {
                    System.out.println("Book's title is out of range. Try again.");
                    name = in.nextLine();
                }
            }
            else {
                System.out.println("Book's title is empty. Try again.");
                name = in.nextLine();
            }
        }
        System.out.println("Input book's author: ");
        viewAuthors();
        System.out.printf("%5d %15s\n", -1, "Add author");
        boolean mark = false;
        boolean mark2 = false;
        while (!mark) {
            try {
                String test = in.nextLine();
                if(test.equalsIgnoreCase("cancel")){
                    System.out.println("Action canceled");
                    break;}
                int id = new Integer(test);
                Book tempB;
                if (id == -1) id = addAuthor(in);
                Author author = aCC.getAuthor(id);
                System.out.print("Input the year of publishing: ");
                while (!mark2) {
                    String test1 = in.nextLine();
                    if(test1.equalsIgnoreCase("cancel")){
                        System.out.println("Action canceled");
                        break;}
                    try {
                        int publishYear = new Integer(test1);
                        tempB = new Book(name, author, publishYear, "", "");
                        if(!isExists(tempB, author.getBooks())) {
                            try {
                                aCC.addBook(tempB, id);
                            } catch (BookAlreadyExistsException e) {

                            }

                            System.out.println("Book " + "\"" + tempB.getTitle() + "\"" + " added to author " + "\"" + author.getName() + "\"\n");
                            mark2 = true;
                            try {
                                aCC.save(new File("XML1.xml"));
                            } catch (JAXBException ex) {
                                System.out.println("Should not happen at all. Save cancelled");
                            }
                        } else {
                            System.out.println("Book is already added. Book adding is cancelled.");
                            break;
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

    private boolean isExists(Book book, List<Book> bookList) {
        for (Book currentBook : bookList) {
            if (book.equals(currentBook))
                return true;
        }
        return false;
    }

    private int addAuthor(Scanner in) {
        System.out.print("Input author's name: ");
        String name = in.nextLine();
        while (true) {
            name = name.trim();
            if(!name.isEmpty()) {
                if(name.length() <= AUTHOR_NAME_LIMIT) {
                    break;
                } else {
                    System.out.println("Author's name is out of range. Try again.");
                    name = in.nextLine();
                }
            } else {
                System.out.println("Author's name is empty. Try again.");
                name = in.nextLine();
            }
        }

        Author author = new Author(name);
        if(!isExists(author, aCC.getAuthorsContainer().getAuthors())&& isValid(author)) {
            aCC.addAuthor(author);

            try{
                aCC.save(new File("XML1.xml"));
            }catch (JAXBException ex) {
                System.out.println("Should not happen at all. Save cancelled");
            }
            return author.getId();
        }
        else {
            System.out.println("Author is already added. Adding is cancelled.");
            return -1;
        }
    }

    private boolean isExists(Author author, List<Author> authors) {
        for (Author currentAuthor : authors) {
            if (author.equals(currentAuthor))
                return true;
        }
        return false;
    }

    private boolean isValid(Author author) {
        return !author.getName().isEmpty() &&
                author.getName().length() <=AUTHOR_NAME_LIMIT;
    }
 
    private void deleteAuthor(Scanner in) {
        boolean mark = false;
        if (!aCC.getAuthorsContainer().getAuthors().isEmpty()) {
            viewAuthors();
            while (!mark) {
                System.out.print(("Input auhtor's id: "));
                try {
                    String check = in.nextLine();
                    if(check.equalsIgnoreCase("cancel")){
                        System.out.println("Action canceled");
                        break;}
                    int id = new Integer(check);
                    boolean mark2 = false;
                    while (!mark2) {
                        if (id < aCC.getAuthorsContainer().getAuthors().size()) {
                            System.out.print(("Warning! deleting an author will remove all his books as well. Procced? Y/N: "));
                            String str = in.nextLine();
                            if(str.equalsIgnoreCase("cancel")){
                                System.out.println("Action canceled");
                                break;}
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
                    if(check.equalsIgnoreCase("cancel")){
                        System.out.println("Action canceled");
                        break;}

                    int id = new Integer(check);
                    Author cauthor = aCC.getAuthor(id);


                    System.out.print(("Input auhtor's new name: "));

                    String name = in.nextLine();
                    while (true) {
                        name = name.trim();
                        if(!name.isEmpty()) {
                            if(name.length() <= AUTHOR_NAME_LIMIT) {
                                break;
                            } else {
                                System.out.println("Author's name is out of range. Try again.");
                                name = in.nextLine();
                            }
                        } else {
                            System.out.println("Author's name is empty. Try again.");
                            name = in.nextLine();
                        }
                    }
                    String oldName = cauthor.getName();
                    for (Author authors: aCC.getAuthorsContainer().getAuthors()){
                        if (!name.equalsIgnoreCase(authors.getName())){
                            cauthor.setName(name);}
                            else{
                            System.out.println("Author is already exist. Try again");
                            name = in.nextLine();
                        }
                    }
                    System.out.println("Author " + "\"" + oldName + "\"" + " got new name " + "\"" + cauthor.getName() + "\"\n");
                    mark = true;
                    try {
                        aCC.save(new File("XML1.xml"));
                        System.out.println("Edition successful.\n");
                    } catch (JAXBException ex) {
                        System.out.println("Should not happen at all. Save cancelled");
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Id must be a number. Try again.");
                } catch (IndexOutOfBoundsException ex) {
                    System.out.println("Index out of range. Try again.");
                }
            }

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
                    if(c.equalsIgnoreCase("cancel")){
                        System.out.println("Action canceled");
                        break;}
                    int id = new Integer(c);
                    Book cbook = aCC.getBook(id);
                    boolean mark1 = false;
                    while (!mark1) {
                        System.out.printf("%25s %15s\n", cbook.getTitle(), cbook.getAuthor().getName());
                        System.out.print("Edit book's title Y/N?: ");
                        String str = in.nextLine();
                        if(str.equalsIgnoreCase("cancel")){
                            System.out.println("Action canceled");
                            break;}
                        if (str.toUpperCase().equals("Y")) {
                            System.out.print("Input book's new title: ");

                            str = in.nextLine();
                            while (true) {
                                str = str.trim();
                                if(!str.isEmpty()) {
                                    if(str.length() <= BOOK_TITLE_LINIT) {
                                        break;
                                    } else {
                                        System.out.println("Book's title is out of range. Try again.");
                                        str = in.nextLine();
                                    }
                                }
                                else {
                                    System.out.println("Book's title is empty. Try again.");
                                    str = in.nextLine();
                                }
                            }

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
                        if(str.equalsIgnoreCase("cancel")){
                            System.out.println("Action canceled");
                            break;}
                        if (str.toUpperCase().equals("Y")) {
                            System.out.printf("%5d %15s\n", -1, "Add author");
                            viewAuthors();
                            System.out.print("Input book's new auhtor's id: ");
                            try {
                                String check = in.nextLine();
                                if(check.equalsIgnoreCase("cancel")){
                                    System.out.println("Action canceled");
                                    break;}
                                int id2 = new Integer(check);
                                if (id2 == -1) id2 = addAuthor(in);
                                Author author = aCC.getAuthor(id2);
                                aCC.removeBook(id);
                                cbook.setAuthor(author);
                                try {
                                    aCC.addBook(cbook, id2);
                                } catch (BookAlreadyExistsException e) {

                                }
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
                        if(str.equalsIgnoreCase("cancel")){
                            System.out.println("Action canceled");
                            break;}
                        if (str.toUpperCase().equals("Y")) {
                            System.out.print("Input book's new year of publishing: ");
                            try {
                                String check = in.nextLine();
                                if(str.equalsIgnoreCase("cancel")){
                                    System.out.println("Action canceled");
                                    break;}
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
                    if(check.equalsIgnoreCase("cancel")){
                        System.out.println("Action canceled");
                        break;}
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
}
