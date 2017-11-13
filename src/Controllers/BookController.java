//package Controllers;
//
//import Models.Author;
//import Models.Book;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//
//public class BookController  {
//
//    public void addBook(String author, String title){
//        Book book = new Book();
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        try{
//            System.out.printf("Введите автора");
//            author = reader.readLine();
//            book.getAuthor().setName(author);
//            System.out.println("Введите название книги");
//            title = reader.readLine();
//            book.setTitle(title);
//        }
//        catch (IOException e) {
//            System.out.println("Вы не ввели автора или название книги");
//        }
//        book.getAuthor().getBooks().add(book);
//
//    }
//}
//
