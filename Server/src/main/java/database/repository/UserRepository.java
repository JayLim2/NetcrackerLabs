package database.repository;

import entity.Publisher;
import entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    //поиск по имени, мб нужно будет поправить
    @Query("select b from User b where b.userLogin = :name")
    User findByName(@Param("name") String name);

    @Query("select b from User b where b.userPass= :id")
    User findByID(@Param("id") int id);
}
