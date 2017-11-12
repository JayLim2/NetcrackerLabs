import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static List<BookController> bookControlList;
    private static List<AuthorController> authorControlList;
    private static Scanner in;
    
    public static void viewAuthors(){
        for(AuthorController authorController:authorControlList){
                authorController.updateView();
            }
    }
    
    public static void addAuthor() throws IOException{
        String inp;
        System.out.print("Name:");
        inp = in.nextLine();
        authorControlList.add(new AuthorController(new Author(inp), new ConsoleAuthorView()));
    }
    
    public static void addBook() throws IOException{
        String inp;
        System.out.print("Title:");
        inp = in.nextLine();
        viewAuthors();
        System.out.println(authorControlList.size() + " add new Author");
        System.out.print("Author id:");
        String inp2 = in.nextLine();
        int id = new Integer(inp2);
        if (id == authorControlList.size()) {
            addAuthor();
            Book book = new Book(inp, authorControlList.get(id).getAuthor(), 1800 , "default", "default");
            bookControlList.add(new BookController(new Book(inp, authorControlList.get(id).getAuthor(), 1800 , "default", "default"), new ConsoleView()));
            authorControlList.get(id).addBook(book);
        }
        else {
            Book book = new Book(inp, authorControlList.get(id).getAuthor(), 1800 , "default", "default");
            bookControlList.add(new BookController(new Book(inp, authorControlList.get(id).getAuthor(), 1800 , "default", "default"), new ConsoleView()));
            authorControlList.get(id).addBook(book);
        }
    }
    
    public static void main(String[] args) throws IOException {
        Author author1 = new Author("Пушкин А.С.");
        Author author2 = new Author("Толстой Л.Н.");
        Book book1 = new Book("Евгений Онегин", author1, 1832 , "default", "default");
        Book book2 = new Book("Дубровский", author1, 1841, "default", "default");
        Book book3 = new Book("Война и мир", author1, 1867 , "default", "default");
        Book book4 = new Book("Детсво.Отрочество.Юность", author1, 1852, "default", "default");
        author1.getBooks().add(book1);
        author1.getBooks().add(book2);
        author2.getBooks().add(book3);
        author2.getBooks().add(book4);
        List<Author> authors = new ArrayList<Author>();
        authors.add(author1);
        authors.add(author2);
        boolean menuState = true;
        in = new Scanner(System.in);
        List<Book> bookDisplayList = new ArrayList<Book>();
        bookControlList = new ArrayList<BookController>();
        authorControlList = new ArrayList<AuthorController>();
        for (Author author:authors) {
            bookDisplayList.addAll(author.getBooks());
            authorControlList.add(new AuthorController(author, new ConsoleAuthorView()));
        }
        for(Book book:bookDisplayList){
            bookControlList.add(new BookController(book,new ConsoleView()));
        }
        for(BookController bookController:bookControlList){
                    bookController.updateView();
            }
        while(true){
            if(menuState){
                String inp = in.nextLine();
                char fsymb = inp.charAt(0);
                switch(fsymb){
                    case '1':
                        for(BookController bookController:bookControlList){
                            bookController.updateView();
                        }
                        break;//view books
                    case '2':
                        viewAuthors();
                        menuState = false;
                        break;//view authors and switch to author edit
                    case '3':
                        addBook();
                        break;//add book
                    case '4':break;//delete book
                    case '5':break;//edit book
                }
                if (fsymb == 'q') break;
            }
            else{
                String inp = in.nextLine();
                char fsymb = inp.charAt(0);
                switch(fsymb){
                    case '1':
                        for(BookController bookController:bookControlList){
                            bookController.updateView();
                        }
                         menuState = true;
                        break;//view books and switch to book edit
                    case '2':
                        viewAuthors();
                        break;//view authors 
                    case '3':break;//add author
                    case '4':break;//delete author
                    case '5':break;//edit author
                }
                if (fsymb == 'q') break;
            }
        }
    }
}

