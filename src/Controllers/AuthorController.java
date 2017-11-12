package Controllers;

import Models.Author;
import Models.Book;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AuthorController {
    //список со всеми авторами
    private List<Author> authors;

    public List<Author> getAuthors(){
        return authors;
    }

    public void setAuthors(List<Author> authors){
        this.authors = authors;
    }

    public void addAuthor(Author author){
        getAuthors().add(author);
    }

    public void addAuthor(String name){
        Author author = new Author();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try{
            System.out.println("Введите Ф.И.О. автора");
            name = reader.readLine();
            author.setName(name);
        }
        catch (IOException e) {
            System.out.println("Вы не ввели данных автора");
        }
        getAuthors().add(author);

    }

}


