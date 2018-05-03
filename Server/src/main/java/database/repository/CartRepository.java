package database.repository;

import entity.Book;
import entity.Cart;
import entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @Query("from Cart where userid= :userId")
    List<Cart> getCartByUserId(@Param("userId") User userId);

    @Query("from Cart where userid= :userId and bookid = :bookId")
    Cart getCartByKey(@Param("userId") User userId, @Param("bookId") Book bookId);

    @Modifying
    @Transactional
    @Query("delete from Cart where cartID= :cartId")
    void deleteCartById(@Param("cartId") int cartId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Cart set counter= :newCount where cartID= :cartId")
    void updateCartCountById(@Param("cartId") int cartId, @Param("newCount") int count);
}
