package database.repository;


import entity.Book;
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
}