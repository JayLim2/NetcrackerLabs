/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Views;

import Controllers.AuthorContainerController;
import Models.*;

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
                char fsymb = inp.charAt(0);
                switch (fsymb) {
                    case '1':
                        viewBooks();
                        break;//view books
                    case '2':
                        viewAuthors();
                        menuState = false;
                        break;//view authors and switch to author edit
                    case '3':
                        addBook(in);
                        break;//add book
                    case '4':
                        deleteBook(in);
                        break;//delete book
                    case '5':
                        editBook(in);
                        break;//edit book
                }
                if (fsymb == 'q') break;
            } else {
                String inp = in.nextLine();
                char fsymb = inp.charAt(0);
                switch (fsymb) {
                    case '1':
                        viewBooks();
                        menuState = true;
                        break;//view books and switch to book edit
                    case '2':
                        viewAuthors();
                        break;//view authors 
                    case '3':
                        addAuthor(in);
                        break;//add author
                    case '4':
                        deleteAuthor(in);
                        break;//delete author
                    case '5':
                        editAuthor(in);
                        break;//edit author
                }
                if (fsymb == 'q') break;
            }

            /*System.out.println("Чтобы продолжить введите 0.");
            while(in.nextLine().charAt(0) != '0') {
                System.out.println("Чтобы продолжить введите 0.");
            }*/
        }
    }

    private void viewBooks() {
        System.out.printf("%45s%n", "=============== Book list ===============");
        int id = 0;
        for (int j = 0; j < aCC.getAuthorsContainer().getAuthors().size(); j++) {
            List<Book> tempB = aCC.getAuthorsContainer().getAuthors().get(j).getBooks();
            for (int i = 0; i < tempB.size(); i++) {
                System.out.printf("%5d %25s %15s\n", id++, tempB.get(i).getTitle(), tempB.get(i).getAuthor().getName());
            }
        }
        System.out.println();
    }

    private void viewAuthors() {
        System.out.printf("%20s%n", "=============== Author list ===============");
        List<Author> tempA = aCC.getAuthorsContainer().getAuthors();
        for (int i = 0; i < tempA.size(); i++) {
            System.out.printf("%5d %15s\n", i, tempA.get(i).getName());
        }
    }

    private void viewMenu(boolean menustate) {
        //menustate = true - MAIN MENU, else - AUTHOR EDIT MENU
        if(menustate) {
            System.out.println("Main menu\n================");
            System.out.println("1. Book list");
            System.out.println("2. Author list");
            System.out.println("3. Add book");
            System.out.println("4. Remove book");
            System.out.println("5. Change book");
            System.out.println("q. Quit");
        } else {
            System.out.println("Author mode\n================");
            System.out.println("1. Book list");
            System.out.println("2. Author list");
            System.out.println("3. Add author");
            System.out.println("4. Remove author");
            System.out.println("5. Change auhtor");
            System.out.println("q. Quit");
        }
        System.out.println();
        System.out.print("Input menu command id: ");
    }

    private void addBook(Scanner in) {
        System.out.print("Input book's title: ");
        String name = in.nextLine();
        viewAuthors();
        System.out.printf("%5d %15s\n", aCC.getAuthorsContainer().getAuthors().size(), "Add author");
        int id = new Integer(in.nextLine());
        Book tempB;
        if (id < aCC.getAuthorsContainer().getAuthors().size()) {
            tempB = new Book(name, aCC.getAuthorsContainer().getAuthors().get(id), 0, "", "");
            aCC.addBook(tempB, id);
        } else if (id == aCC.getAuthorsContainer().getAuthors().size()) {
            addAuthor(in);
            tempB = new Book(name, aCC.getAuthorsContainer().getAuthors().get(id), 0, "", "");
            aCC.addBook(tempB, id);
        }
    }

    private void addAuthor(Scanner in) {
        System.out.print("Input author's name: ");
        String name = in.nextLine();
        aCC.addAuthor(new Author(name));
    }

    private void deleteAuthor(Scanner in) {
        System.out.print(("Input auhtor's id: "));
        int id = new Integer(in.nextLine());
        System.out.print(("Warning! deleting an author will remove all his books as well. Procced? Y/N"));
        String str = in.nextLine();
        if (str.equals("Y")) aCC.removeAuthor(id);
    }

    private void editAuthor(Scanner in) {
        System.out.print(("Input auhtor's id: "));
        int id = new Integer(in.nextLine());
        Author cauthor = aCC.getAuthor(id);
        System.out.println(cauthor.getName());
        System.out.print(("Input auhtor's new name: "));
        String str = in.nextLine();
        cauthor.setName(str);
    }

    private void editBook(Scanner in) {
        System.out.print(("Input book's id: "));
        int id = new Integer(in.nextLine());
        Book cbook = aCC.getBook(id);
        System.out.printf("%25s %15s\n", cbook.getTitle(), cbook.getAuthor().getName());
        System.out.print("Edit book's title Y/N?");
        String str = in.nextLine();
        if (str.equals("Y")) {
            System.out.print("Input book's new title: ");
            str = in.nextLine();
            cbook.setTitle(str);
        }
        System.out.print("Edit book's author Y/N?");
        str = in.nextLine();
        if (str.equals("Y")) {
            viewAuthors();
			System.out.printf("%5d %15s\n", aCC.getAuthorsContainer().getAuthors().size(), "Add author");
            System.out.print("Input book's new auhtor's id: ");
            int id2 = new Integer(in.nextLine());
            Book tempB;
            if (id2 < aCC.getAuthorsContainer().getAuthors().size()) {
                aCC.removeBook(id);
                cbook.setAuthor(aCC.getAuthor(id2));
                aCC.addBook(cbook, id2);
            } else if (id2 == aCC.getAuthorsContainer().getAuthors().size()) {
                addAuthor(in);
                aCC.removeBook(id);
                cbook.setAuthor(aCC.getAuthor(id2));
                aCC.addBook(cbook, id2);
            }
        }
    }

    private void deleteBook(Scanner in) {
        System.out.print(("Input book's id: "));
        int id = new Integer(in.nextLine());
        aCC.removeBook(id);
    }
}
