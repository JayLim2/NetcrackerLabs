//Модель
class Book {
    private String title = "default title";
    private Author author;
    private int publishYear = 1900;
    private String publisher = "default publisher";
    private String brief = "description goes here";

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

class DatabaseLayer implements ModelLayer {
    @Override
    public Book getBook() {
        return new Book();
    }
}

//Представление (View)
interface View {
    void printBook(Book book);
}

class ConsoleView implements View {
    @Override
    public void printBook(Book book) {
        System.out.println("Title: " + book.getTitle());
        System.out.println("Author: " + book.getAuthor());
    }
}

//Контроллер
public class BookController {
    private ModelLayer modelLayer = new DatabaseLayer();
    private View consoleView = new ConsoleView();

    public void execute() {
        Book book = modelLayer.getBook();
        consoleView.printBook(book);
    }
}
