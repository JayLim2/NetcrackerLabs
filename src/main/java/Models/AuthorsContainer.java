package Models;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlSeeAlso(Author.class)
public class AuthorsContainer {
    private List<Author> authors;

    public AuthorsContainer() {
        authors = new LinkedList<Author>();
    }
  
    @XmlElement(name = "authorList")
    public List<Author> getAuthors(){
        return authors;
    }

    public void setAuthors(List<Author> authors){
        this.authors = authors;
    }

    public Author getAuthor(int id){
        return authors.get(id);
    }

    public void addAuthor(Author author){
        author.id=authors.size();
        authors.add(author);
        sort();
    }

    public void addAuthor(int index, Author author) {
        authors.add(index, author);
    }

    private void sort(){
        authors.sort(new AuthorComparator());
    }

}
