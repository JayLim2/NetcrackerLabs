/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import database.service.AuthorService;
import database.service.BookService;
import database.service.PublisherService;
import entity.Author;
import entity.Book;
import entity.Publisher;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import models.YearOutOfBoundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author Алескандр
 */
@Service
@Transactional
public class TransactionalStuff {
    
    @Autowired
    BookService bookService;
    
    @Autowired
    AuthorService authorService;
    
    @Autowired
    PublisherService publisherService;
    
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void bookEditTransaction(MultiValueMap<String, String> params, Model model) throws YearOutOfBoundsException, BDModifiedException{
            Book b = bookService.getByID(Integer.parseInt(params.get("id").get(0)));
            if (b.getAuthors().size() > 0)
                for (int i = 0; i < b.getAuthors().size(); i++) {
                    b.getAuthors().get(i).getBooks().remove(b);
                    authorService.editAuthor(b.getAuthors().get(i));
                }
            List<String> aIDs = params.get("author");
            List<Author> newAuthors = new LinkedList<>();
            if ((aIDs != null)&&(aIDs.size() > 0))
                for (int i = 0; i < aIDs.size(); i++) {
                    if (authorService.getByID(Integer.parseInt(aIDs.get(i))) == null) throw new BDModifiedException();
                    newAuthors.add(authorService.getByID(Integer.parseInt(aIDs.get(i))));
                }
            Publisher p = publisherService.getByID(Integer.parseInt(params.get("publisher").get(0)));
            if (p == null) throw new BDModifiedException();
            b.setBookName(params.get("booktitle").get(0));
            b.setBrief(params.get("brief").get(0));
            b.setPublishYear(Integer.parseInt(params.get("publishYear").get(0)));
            if (b.getPublishYear() < 0 || b.getPublishYear() > Calendar.getInstance().get(Calendar.YEAR)) {
                throw new YearOutOfBoundsException();
            }
            b.setPublisher(p);
            //bookService.editBook(b);
            
            b.setAuthors(newAuthors);
            
            bookService.editBook(b);
            if (newAuthors.size() > 0)
                for (int i = 0; i < newAuthors.size(); i++) {
                    newAuthors.get(i).getBooks().add(b);
                    authorService.editAuthor(newAuthors.get(i));
                }
    }
}
