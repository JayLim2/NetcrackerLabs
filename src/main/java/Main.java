import Controllers.AuthorContainerController;
import Models.*;
import Views.AuthorContainerView;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;


public class Main {
    
    public static void main(String[] args) throws IOException, JAXBException {
//            Author author1 = new Author("Пушкин А.С.");
//            Author author2 = new Author("Толстой Л.Н.");
//            Book book1 = new Book("Евгений Онегин", author1, 1832 , "default", "default");
//            Book book2 = new Book("Дубровский", author1, 1841, "default", "default");
//            Book book3 = new Book("Война и мир", author2, 1867 , "default", "default");
//            Book book4 = new Book("Детсво.Отрочество.Юность", author2, 1852, "default", "default");
//            author1.getBooks().add(book1);
//            author1.getBooks().add(book2);
//            author2.getBooks().add(book3);
//            author2.getBooks().add(book4);
            JAXBContext context = JAXBContext.newInstance(AuthorsContainer.class);
            Unmarshaller unmarsh = context.createUnmarshaller();
            AuthorsContainer authors = (AuthorsContainer)unmarsh.unmarshal(this.getClass().getResourceAsStream("TRANSFER.xls"));
            AuthorContainerController aCC = new AuthorContainerController(authors);
            aCC.reInitAuthorsInBooks();
//            aCC.addAuthor(author1);
//            aCC.addAuthor(author2);
            AuthorContainerView aCV = new AuthorContainerView(aCC);
            aCV.mainLoop();
            Marshaller mobj = context.createMarshaller();
            mobj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mobj.marshal(authors, new File("XML1.xml"));
    }
}


