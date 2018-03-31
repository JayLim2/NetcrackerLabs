package database.repository;


import entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {

    //поиск по имени, мб нужно будет поправить
    @Query("select b from Publisher b where b.name = :name")
    Publisher findByName(@Param("name") String name);
}
