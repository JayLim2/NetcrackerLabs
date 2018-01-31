package controllers;

import exceptions.InvalidCommandAction;
import models.Author;
import models.AuthorsContainer;
import models.Book;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import models.BookAlreadyExistsException;
import models.BookFilter;
import models.YearOutOfBoundsException;

public class AuthorContainerController {
    private AuthorsContainer authorsContainer;

    public AuthorContainerController(AuthorsContainer authorsContainer) {
        this.authorsContainer = authorsContainer;
    }

    public void addAuthor(Author author) throws InvalidCommandAction {
        if (authorExistsAlready(author)) throw new InvalidCommandAction("an author with this name already exists");
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

    public void addBook(Book book, int id) throws BookAlreadyExistsException {
        Author author = getAuthor(id);
        book.setAuthor(author);
        if (!author.addBook(book))throw new BookAlreadyExistsException();
        book.dispatchId();
    }
    
    public boolean existAlready(Author author, Book book){
        BookController bcBook = new BookController(book);
        for(Book exBook: author.getBooks()){
            if (bcBook.isEquvalent(exBook))
                return true;
        }
        return false;
    }
    
    public void changeBook(Book book, int bid, int naid) throws YearOutOfBoundsException, BookAlreadyExistsException{
        Book chBook = getBook(bid);
        Author author = getAuthor(naid);
        Author oldAuthor = chBook.getAuthor();
        chBook.getAuthor().getBooks().remove(chBook);
        if (existAlready(author,book)){
            oldAuthor.addBook(chBook);
            throw new BookAlreadyExistsException();
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
    
    public AuthorsContainer search(BookFilter filter){
        AuthorsContainer result = new AuthorsContainer();
        for(Author tempAuthor : authorsContainer.getAuthors()){
            Author copyAuthor = new Author(tempAuthor.getName());
            copyAuthor.setId(tempAuthor.getId());
            try {
                result.addAuthor(copyAuthor);
            } catch (InvalidCommandAction ex) {
                System.out.println("cant happen");//placeholders. replace
                    // cant happen normally
            }
            for(Book tempBook : tempAuthor.getBooks()){
                try {
                    Book copyBook = new Book(tempBook.getTitle(), tempBook.getAuthor(), tempBook.getPublishYear(), tempBook.getPublisher(), tempBook.getBrief());
                    copyBook.setId(tempBook.getId());
                    if (filter.accept(copyBook))
                        copyAuthor.addBook(copyBook);
                } catch (YearOutOfBoundsException ex) {
                    System.out.println("cant happen");
                    // cant happen normally
                }
                
            }
        }
        return result;
    }
    
    public boolean authorExistsAlready(Author checkAuthor){
        for(Author author: authorsContainer.getAuthors()){
            if (author.getName().equals(checkAuthor.getName())) return true;
        }
        return false;
    }
    
    public Author getAuthorByName(String name){
        for(Author author: authorsContainer.getAuthors()){
            if (author.getName().equals(name)) return author;
        }
        return null;
    }
    
    public void merge(AuthorsContainer anotherAuthorsContainer){
        for(Author author:anotherAuthorsContainer.getAuthors()){
            Author tempAuthor = getAuthorByName(author.getName());
            if(tempAuthor == null){
                tempAuthor = new Author(author.getName());
                tempAuthor.setId(author.getId());
                try {
                    addAuthor(tempAuthor);
                } catch (InvalidCommandAction ex) {
                    //cant happen
                }
            }
            for(Book book: author.getBooks()){
                if (!existAlready(tempAuthor, book)){
                    try {
                        Book tempBook = new Book(book.getTitle(), tempAuthor, book.getPublishYear(), book.getPublisher(), book.getBrief());
                        tempBook.setId(book.getId());
                        tempAuthor.addBook(tempBook);
                    } catch (YearOutOfBoundsException ex) {
                        //cant happen
                    }
                }
            }    
        }
    }
    
    public void resolveIds(){
        List<Integer> bookIds = new ArrayList<>();
        List<Integer> authorIds = new ArrayList<>();
        for(Author author:authorsContainer.getAuthors()){
            if (authorIds.contains(author.getId())){
                author.dispatchId();
            }
            else authorIds.add(author.getId());
            for(Book book:author.getBooks()){
                if (bookIds.contains(book.getId())){
                    book.dispatchId();
                }
                else bookIds.add(book.getId());
            }
        }
    }
}