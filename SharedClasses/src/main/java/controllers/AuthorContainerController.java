package controllers;

import models.Author;
import models.AuthorsContainer;
import models.Book;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import models.YearOutOfBoundsException;

public class AuthorContainerController {
    private AuthorsContainer authorsContainer;

    public AuthorContainerController(AuthorsContainer authorsContainer) {
        this.authorsContainer = authorsContainer;
    }

    public void addAuthor(Author author) {
        authorsContainer.addAuthor(author);
        author.dispatchId();
    }

    public Author getAuthor(int id) {
        Author res = null;
        for (Author author : authorsContainer.getAuthors()) {
            if (id == author.getId()) {
                res = author;
                break;
            }
        }
        if (res == null) throw new IndexOutOfBoundsException();
        return res;
    }

    public Book getBook(int id) {
        Book res = null;
        a:
        {
            for (Author author : authorsContainer.getAuthors())
                for (Book book : author.getBooks()) {
                    if (id == book.getId()) {
                        res = book;
                        break a;
                    }
                }
        }
        if (res == null) throw new IndexOutOfBoundsException();
        return res;
    }

    private int countBooks() {
        int size = 0;
        for (Author author : authorsContainer.getAuthors())
            size += author.getBooks().size();
        return size;
    }

    public void load(FileInputStream fin) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
        Unmarshaller unmarsh = context.createUnmarshaller();
        AuthorsContainer authors = (AuthorsContainer) unmarsh.unmarshal(fin);
        authorsContainer = authors;
        Author.resetId();
        Book.resetId();
        reInitAuthorsInBooks();
    }

    public void save(File fout) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
        Marshaller marsh = context.createMarshaller();
        marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marsh.marshal(getAuthorsContainer(), fout);
    }

    public void removeBook(int id) {
        Book res = null;
        a:
        {
            for (Author author : authorsContainer.getAuthors()) {
                for (Book book : author.getBooks()) {
                    if (id == book.getId()) {
                        res = book;
                        break;
                    }
                }
                if (res != null) {
                    author.getBooks().remove(res);
                    Book.removeId(id);
                    break a;
                }
            }
        }
        if (res == null) throw new IndexOutOfBoundsException();
    }

    public void removeAuthor(int id) {
        Author res = null;
        for (Author author : authorsContainer.getAuthors()) {
            if (id == author.getId()) {
                res = author;
                break;
            }
        }
        if (res != null)
        {
            authorsContainer.getAuthors().remove(res);
            Author.removeId(id);
        }
        else
            throw new IndexOutOfBoundsException();
    }

    public AuthorsContainer getAuthorsContainer() {
        return authorsContainer;
    }

    public void addBook(Book book, int id) {
        Author author = authorsContainer.getAuthor(id);
        book.dispatchId();
        book.setAuthor(author);
        author.addBook(book);
    }
    
    public boolean existAlready(Author author, Book book){
        BookController bcBook = new BookController(book);
        for(Book exBook: author.getBooks()){
            if (bcBook.isEquvalent(exBook))
                return true;
        }
        return false;
    }
    
    public void changeBook(Book book, int bid, int naid) throws YearOutOfBoundsException{
        Book chBook = getBook(bid);
        Author author = getAuthor(naid);
        Author oldAuthor = chBook.getAuthor();
        chBook.getAuthor().getBooks().remove(chBook);
        if (existAlready(author,book)){
            oldAuthor.addBook(chBook);
        }//and throw Exception
        else{
            
            new BookController(chBook).modifyBook(book,author);
            author.addBook(chBook);
        }
    }

    public void reInitAuthorsInBooks() {//если оставим эту штуку с xml наверное будет приватным и вызыватся только при десериализации
        for (Author author : authorsContainer.getAuthors()) {
            for (Book book : author.getBooks())
                book.setAuthor(author);
        }
    }

    //Проверяет есть ли у автора книги. Если их нет - вернет false
    public boolean checkBooksInAuthor() {
        List<Author> tmp = authorsContainer.getAuthors();
        for (int i = 0; i < tmp.size(); i++) {
            if (!tmp.get(i).getBooks().isEmpty())
                return true;
        }
        return false;
    }
}