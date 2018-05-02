package database.service;

import entity.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getCartByUserId(int userId);

    void addToCart(int userId, int bookId);

    void deleteFromCart(int userId, int bookId);
}
