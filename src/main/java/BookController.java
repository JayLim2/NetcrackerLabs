class Book {
    private String title = "default title";
    private Author author;
    private int publishYear = 1900;
    private String publisher = "default publisher";
    private String brief = "description goes here";
    
    public Book(String title, Author author, int publishYear, String publisher, String brief){
        setTitle(title);
        setAuthor(author);
        setPublishYear(publishYear);
        setPublisher(publisher);
        setBrief(brief);
    }

    Book(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }
}

//Бизнес логика модели
interface ModelLayer {
    Book getBook();
}

//Представление (View)
interface View {
    void printBook(Book book);
}

class ConsoleView implements View {
    @Override
    public void printBook(Book book) {
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor().getName());
        System.out.flush();
    }
}

//Контроллер
public class BookController {
    private View consoleView;
    private Book bookModel;

    public BookController(Book bookModel, View view){
        this.bookModel = bookModel;
        consoleView = view;
    }
    
    public void updateView(){
        consoleView.printBook(bookModel);
    }
    
    public void execute() {
        Book book = new Book();
        consoleView.printBook(book);
    }
}

