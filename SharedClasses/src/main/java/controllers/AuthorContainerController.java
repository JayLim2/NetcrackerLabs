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


/**
 * This class is used in any interactions 
 * with encapsulated database container
 * @author Alexander
 * @author Rostislav
 * @author Komarov
 */
public class AuthorContainerController {
    private AuthorsContainer authorsContainer;

    /**
     * main constructor
     * @param authorsContainer the controlled model in question 
     */
    public AuthorContainerController(AuthorsContainer authorsContainer) {
        this.authorsContainer = authorsContainer;
    }

    /**
     * 
     * @param author author to add to the encapsulated container
     * @throws InvalidCommandAction if author with the same name exists in this container already
     */
    public void addAuthor(Author author) throws InvalidCommandAction {
        if (authorExistsAlready(author)) throw new InvalidCommandAction("an author with this name already exists");
        authorsContainer.addAuthor(author);
        author.dispatchId();
    }

    /**
     * retrieves an author based on its index
     * index in this case means unique index dispatched to a
     * model upon addition to the container
     * @param id index used for identifying the author needed
     * @return an author with the same id as the param id
     * if there is no such author an IndexOutOfBoundsExcepcion is thrown
     */
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

    /**
     * retrieves a book based on its index
     * index in this case means unique index dispatched to a
     * model upon addition to the container
     * @param id index used for identifying the author needed
     * @return a book with the same id as the param id
     * if there is no such book an IndexOutOfBoundsExcepcion is thrown
     */
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

    /**
     * counts the total amount of books in the container
     * @return the total amount of books in the container
     */
    private int countBooks() {
        int size = 0;
        for (Author author : authorsContainer.getAuthors())
            size += author.getBooks().size();
        return size;
    }

    /**
     * load container  from filestream in xml
     * @param fin filestream to load from
     * @throws JAXBException in case of incorrect file format
     */
    public void load(FileInputStream fin) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
        Unmarshaller unmarsh = context.createUnmarshaller();
        AuthorsContainer authors = (AuthorsContainer) unmarsh.unmarshal(fin);
        authorsContainer = authors;
        //Author.resetId();
        //Book.resetId();
        reInitAuthorsInBooks();
    }

    /**
     * saves the container to a file in xml format
     * @param fout file for output
     * @throws JAXBException shoudnt happen
     */
    public void save(File fout) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
        Marshaller marsh = context.createMarshaller();
        marsh.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marsh.marshal(getAuthorsContainer(), fout);
    }

    /**
     * removes a book based on its index
     * index in this case means unique index dispatched to a
     * model upon addition to the container
     * @param id index used for identifying the author needed
     * if there is no such book an IndexOutOfBoundsExcepcion is thrown
     */
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

    /**
     * removes an author and all its books based on its index
     * index in this case means unique index dispatched to a
     * model upon addition to the container
     * @param id index used for identifying the author needed
     * if there is no such author an IndexOutOfBoundsExcepcion is thrown
     */
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

    /**
     * 
     * @return the underlying authorsContainer model 
     */
    public AuthorsContainer getAuthorsContainer() {
        return authorsContainer;
    }

    /**
     * adds a book to the container. It is assumed that the book lacks an id 
     * or a set author
     * uses getAuthor to retrive author by id
     * uses author.addBook to attempt to attach book to the author 
     * @param book the book to be added
     * @param id the id of the book's author 
     * @throws BookAlreadyExistsException if it is alredy attached to the author
     */
    public void addBook(Book book, int id) throws BookAlreadyExistsException {
        Author author = getAuthor(id);
        book.setAuthor(author);
        if (!author.addBook(book))throw new BookAlreadyExistsException();
        book.dispatchId();
    }
    
    
    /**
     * checks if an equvalent(not equal!) book alredy exists in the author
     * @param author to check for equvalents in
     * @param book to compare 
     * @return if an equvalent(not equal!) book alredy exists in the author
     */
    public boolean existAlready(Author author, Book book){
        BookController bcBook = new BookController(book);
        for(Book exBook: author.getBooks()){
            if (bcBook.isEquvalent(exBook))
                return true;
        }
        return false;
    }
    
    /**
     * changes a book with bid index in container
     * @param book encapsulated new parameters
     * @param bid index of the book to be changed
     * @param naid index of the new author of the book
     * @throws YearOutOfBoundsException if year of new book is out of bounds
     * @throws BookAlreadyExistsException if this change creates a duplicate
     */
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

    /**
     * restores author field in books in container
     * necessary because author field is transient
     */
    public void reInitAuthorsInBooks() {
        for (Author author : authorsContainer.getAuthors()) {
            for (Book book : author.getBooks())
                book.setAuthor(author);
        }
    }

    /**
     *???????????????????????
     * @return 
     */
    public boolean checkBooksInAuthor() {
        List<Author> tmp = authorsContainer.getAuthors();
        for (int i = 0; i < tmp.size(); i++) {
            if (!tmp.get(i).getBooks().isEmpty())
                return true;
        }
        return false;
    }
    
    /**
     * book search function
     * @param filter an ecapsulation of regexes used to check books
     * @return an authorContainer copy of current container
     * without any books that didn't pass the filter
     */
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
    
    
    /**
     * check if author with this name exists already in container
     * @param checkAuthor author to check
     * @return true if exists
     */
    public boolean authorExistsAlready(Author checkAuthor){
        for(Author author: authorsContainer.getAuthors()){
            if (author.getName().equals(checkAuthor.getName())) return true;
        }
        return false;
    }
    
    /**
     * retrives an author by name
     * @param name author's name
     * @return author is such exists, nul if doesn't
     */
    public Author getAuthorByName(String name){
        for(Author author: authorsContainer.getAuthors()){
            if (author.getName().equals(name)) return author;
        }
        return null;
    }
    
    
    /**
     * merge current container with another
     * equvalent book a skipped
     * author with the same name are merged in one
     * ids are not resolved
     * @param anotherAuthorsContainer 
     */
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
    
    /**
     * resolves id conflicts(2 models hvaing the same id)
     */
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