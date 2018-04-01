package database.repository;

import entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

//поиск по имени, мб нужно будет поправить
    @Query("select b from Author b where b.authorName = :name")
    Author findByName(@Param("name") String name);
    
    @Query("select b from Author b where b.authorID= :id")
    Author findByID(@Param("id") int id);
}
