package database.service.impl;

import database.repository.CartRepository;
import database.service.CartService;
import entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public List<Cart> getCartByUserId(int userId) {
        return cartRepository.getCartByUserId(userId);
    }

    @Override
    public void addToCart(int userId, int bookId) {
        Cart cart = cartRepository.getCartByKey(userId, bookId);

        if (cart != null) {
            cart.setCount(cart.getCount() + 1);
        } else {
            cart = new Cart(userId, bookId, 1);
        }

        cartRepository.saveAndFlush(cart);
    }

    @Override
    public void deleteFromCart(int userId, int bookId) {
        Cart cart = new Cart(userId, bookId);
        cartRepository.delete(cart);
    }
}
