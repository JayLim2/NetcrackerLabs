package database.repository;

import entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("from Cart where userID= :userId")
    List<Cart> getCartByUserId(@Param("userId") int userId);

    @Query("from Cart where userID= :userId and bookID = :bookId")
    Cart getCartByKey(@Param("userId") int userId, @Param("bookId") int bookId);

}
