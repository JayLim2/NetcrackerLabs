package database.repository;

import entity.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    //поиск по имени, мб нужно будет поправить
    @Query("select b from Book b where b.bookName= :name")
    Book findByName(@Param("name") String name);

    @Query("select b from Book b where b.bookID= :id")
    Book findByID(@Param("id") int id);
    
    @Query("select DISTINCT b from Book b join b.authors a where a.authorName like :filter or b.bookName like :filter or b.brief like :filter")
    List<Book> filterBooks(@Param("filter") String filter);
}